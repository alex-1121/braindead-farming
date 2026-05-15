package com.braindeadfarming.ui;

import com.braindeadfarming.BraindeadFarmingConfig;
import com.braindeadfarming.bank.BankLocation;
import com.braindeadfarming.bank.BankLocations;
import com.braindeadfarming.bank.BankInventoryTracker;
import com.braindeadfarming.bank.InventoryRequirements;
import com.braindeadfarming.route.FarmingRoute;
import com.braindeadfarming.route.RoutePlanner;
import com.braindeadfarming.route.RouteStop;
import com.braindeadfarming.state.RunState;
import com.braindeadfarming.state.RunStateManager;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import java.util.function.Supplier;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.PluginPanel;

public class BraindeadFarmingPanel extends PluginPanel
{
	private final BraindeadFarmingConfig config;
	private final BankInventoryTracker bankInventoryTracker;
	private final RoutePlanner routePlanner;
	private final RunStateManager runStateManager;
	private final Supplier<WorldPoint> playerLocationSupplier;

	private final JLabel stateLabel = new JLabel();
	private final JLabel bankLabel = new JLabel();
	private final JTextArea routeArea = new JTextArea();

	public BraindeadFarmingPanel(
		BraindeadFarmingConfig config,
		BankInventoryTracker bankInventoryTracker,
		RoutePlanner routePlanner,
		RunStateManager runStateManager,
		Supplier<WorldPoint> playerLocationSupplier)
	{
		this.config = config;
		this.bankInventoryTracker = bankInventoryTracker;
		this.routePlanner = routePlanner;
		this.runStateManager = runStateManager;
		this.playerLocationSupplier = playerLocationSupplier;

		setLayout(new BorderLayout());

		JPanel header = new JPanel(new GridBagLayout());
		header.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(0, 0, 4, 0);
		header.add(stateLabel, c);

		c.gridy = 1;
		c.insets = new Insets(0, 0, 0, 0);
		header.add(bankLabel, c);

		JPanel buttons = new JPanel(new GridBagLayout());
		GridBagConstraints bc = new GridBagConstraints();
		bc.gridx = 0;
		bc.gridy = 0;
		bc.weightx = 1;
		bc.fill = GridBagConstraints.HORIZONTAL;
		bc.insets = new Insets(0, 0, 6, 0);

		JButton planButton = new JButton("Plan route");
		planButton.addActionListener(e -> planRoute());
		buttons.add(planButton, bc);

		bc.gridy++;
		JButton startButton = new JButton("Start run");
		startButton.addActionListener(e -> startRun());
		buttons.add(startButton, bc);

		bc.gridy++;
		JButton nextButton = new JButton("Next step");
		nextButton.addActionListener(e -> nextStep());
		buttons.add(nextButton, bc);

		bc.gridy++;
		JButton stopButton = new JButton("Stop");
		stopButton.addActionListener(e -> stopRun());
		buttons.add(stopButton, bc);

		JPanel top = new JPanel(new BorderLayout());
		top.add(header, BorderLayout.NORTH);
		top.add(buttons, BorderLayout.SOUTH);

		routeArea.setEditable(false);
		routeArea.setLineWrap(true);
		routeArea.setWrapStyleWord(true);
		routeArea.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		routeArea.setMinimumSize(new Dimension(0, 0));

		JScrollPane scroll = new JScrollPane(routeArea);
		scroll.setBorder(BorderFactory.createEmptyBorder());

		add(top, BorderLayout.NORTH);
		add(scroll, BorderLayout.CENTER);

		refresh();
	}

	public void refresh()
	{
		RunState state = runStateManager.getState();
		stateLabel.setText("State: " + state);
		bankLabel.setText("Bank synced: " + (runStateManager.isBankSynced() ? "Yes" : "No") + " (open bank/seed vault)");

		StringBuilder sb = new StringBuilder();

		FarmingRoute route = runStateManager.getRoute();
		if (state == RunState.NEEDS_BANK_SYNC && route != null && !route.getStops().isEmpty())
		{
			BankLocation bank = BankLocations.nearestTo(currentPlayerLocation());
			sb.append("Go to bank: ").append(bank.getName()).append("\n");
			sb.append("Open your bank or seed vault so the checklist can update.\n\n");
		}

		List<InventoryRequirements.RequiredItem> requiredItems = route == null ? null : route.getRequiredItems();
		if (requiredItems != null && !requiredItems.isEmpty())
		{
			sb.append("Bank checklist:\n");
			for (InventoryRequirements.RequiredItem item : requiredItems)
			{
				int have = item.have(bankInventoryTracker.getSnapshot());
				sb.append("- ").append(item.getLabel()).append(": ").append(have).append("/").append(item.getQuantity()).append("\n");
			}
			sb.append("\n");
		}

		RouteStop current = runStateManager.currentStop();
		if (state == RunState.NEEDS_BANK_SYNC && route != null && !route.getStops().isEmpty())
		{
			sb.append("Farming route is planned. Patch steps will show after bank sync.\n");
		}
		else if (current != null)
		{
			sb.append("Current stop: ").append(current.getLocation().getName()).append("\n");
			if (current.getTeleport() != null)
			{
				sb.append("Teleport: ").append(current.getTeleport().getName()).append("\n");
			}
			if (!current.isTeleportRequirementsMet() && !current.getUnmetTeleportRequirements().isEmpty())
			{
				sb.append("Missing:\n");
				for (String req : current.getUnmetTeleportRequirements())
				{
					sb.append("- ").append(req).append("\n");
				}
			}
			sb.append("\n");
			if (current.getTeleport() != null)
			{
				sb.append("Steps:\n");
				current.getTeleport().getSteps().forEach(step -> sb.append("- ").append(step.getInstruction()).append("\n"));
			}
		}
		else
		{
			sb.append("No route planned.\n");
		}

		routeArea.setText(sb.toString());
		routeArea.setCaretPosition(0);
	}

	private void planRoute()
	{
		FarmingRoute route = routePlanner.plan(config, bankInventoryTracker.getSnapshot());
		runStateManager.start(route);
		refresh();
	}

	private void startRun()
	{
		if (runStateManager.getRoute() == null || runStateManager.getRoute().getStops().isEmpty())
		{
			planRoute();
			return;
		}

		if (runStateManager.getState() == RunState.IDLE)
		{
			runStateManager.start(runStateManager.getRoute());
		}

		refresh();
	}

	private void nextStep()
	{
		runStateManager.next();
		refresh();
	}

	private void stopRun()
	{
		runStateManager.stop();
		refresh();
	}

	private net.runelite.api.coords.WorldPoint currentPlayerLocation()
	{
		if (playerLocationSupplier == null)
		{
			return null;
		}

		return playerLocationSupplier.get();
	}
}
