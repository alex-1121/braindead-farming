package com.braindeadfarming;

import com.google.inject.Provides;
import java.awt.image.BufferedImage;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.ImageUtil;
import com.braindeadfarming.bank.BankLocation;
import com.braindeadfarming.bank.BankLocations;
import com.braindeadfarming.bank.BankInventoryTracker;
import com.braindeadfarming.navigation.ShortestPathIntegration;
import com.braindeadfarming.overlay.BankHighlightOverlay;
import com.braindeadfarming.overlay.FarmingRunOverlay;
import com.braindeadfarming.route.RoutePlanner;
import com.braindeadfarming.route.RouteStop;
import com.braindeadfarming.state.RunState;
import com.braindeadfarming.state.RunStateManager;
import com.braindeadfarming.ui.BraindeadFarmingPanel;
import net.runelite.api.coords.WorldPoint;

@Slf4j
@PluginDescriptor(
    name = "Braindead Farming",
    description = "No more need to plan your farming and birdhouse runs. Turn off your brain and just follow the instructions.",
    tags = {"farming", "farm", "birdhouse", "runs", "osrs"}
)
public class BraindeadFarmingPlugin extends Plugin
{
	private static final int SHORTEST_PATH_DEFAULT_REACHED_DISTANCE = 5;

    @Inject
    private BraindeadFarmingConfig config;

	@Inject
	private Client client;

	@Inject
	private ClientToolbar clientToolbar;

	@Inject
	private EventBus eventBus;

	@Inject
	private BankInventoryTracker bankInventoryTracker;

	@Inject
	private RoutePlanner routePlanner;

	@Inject
	private RunStateManager runStateManager;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private FarmingRunOverlay farmingRunOverlay;

	@Inject
	private BankHighlightOverlay bankHighlightOverlay;

	@Inject
	private ShortestPathIntegration shortestPathIntegration;

	private BraindeadFarmingPanel panel;
	private NavigationButton navigationButton;
	private WorldPoint lastPlayerLocation;
	private WorldPoint lastShortestPathTarget;
	private int shortestPathRefreshTicks;

    @Override
    protected void startUp()
    {
        log.info("Braindead farming started");

		panel = new BraindeadFarmingPanel(config, bankInventoryTracker, routePlanner, runStateManager, () -> lastPlayerLocation);
		BufferedImage icon = ImageUtil.loadImageResource(getClass(), "/icon.png");

		navigationButton = NavigationButton.builder()
			.tooltip("Braindead Farming")
			.icon(icon)
			.priority(5)
			.panel(panel)
			.build();

		clientToolbar.addNavigation(navigationButton);
		eventBus.register(bankInventoryTracker);
		overlayManager.add(farmingRunOverlay);
		overlayManager.add(bankHighlightOverlay);
    }

    @Override
    protected void shutDown()
    {
        log.info("Braindead farming stopped");

		if (navigationButton != null)
		{
			clientToolbar.removeNavigation(navigationButton);
			navigationButton = null;
		}

		if (panel != null)
		{
			panel = null;
		}

		eventBus.unregister(bankInventoryTracker);
		overlayManager.remove(farmingRunOverlay);
		overlayManager.remove(bankHighlightOverlay);

		shortestPathIntegration.clearTarget();
		lastPlayerLocation = null;
		lastShortestPathTarget = null;
		shortestPathRefreshTicks = 0;
		runStateManager.reset();
    }

    @Subscribe
    public void onGameTick(GameTick gameTick)
    {
		lastPlayerLocation = currentPlayerLocation();

		if (!config.showOverlay())
		{
			if (lastShortestPathTarget != null)
			{
				shortestPathIntegration.clearTarget();
				lastShortestPathTarget = null;
			}
			return;
		}

		WorldPoint target = currentNavigationTarget();

		if (shortestPathIntegration.isAvailable())
		{
			if (target == null && lastShortestPathTarget != null)
			{
				shortestPathIntegration.clearTarget();
				lastShortestPathTarget = null;
			}
			else if (target != null)
			{
				if (hasReachedShortestPathTarget(lastPlayerLocation, target))
				{
					if (lastShortestPathTarget != null)
					{
						shortestPathIntegration.clearTarget();
						lastShortestPathTarget = null;
					}
					shortestPathRefreshTicks = 0;
					return;
				}

				boolean targetChanged = !target.equals(lastShortestPathTarget);
				boolean shouldRefreshBankTarget = runStateManager.getState() == RunState.NEEDS_BANK_SYNC
					&& ++shortestPathRefreshTicks >= 5;

				if (targetChanged)
				{
					if (lastShortestPathTarget != null)
					{
						shortestPathIntegration.clearTarget();
					}
					shortestPathRefreshTicks = 0;
					shortestPathIntegration.setTarget(target);
					lastShortestPathTarget = target;
					if (panel != null && runStateManager.getState() == RunState.NEEDS_BANK_SYNC)
					{
						panel.refresh();
					}
				}
				else if (shouldRefreshBankTarget)
				{
					shortestPathRefreshTicks = 0;
					shortestPathIntegration.setTarget(target);
				}
			}
		}
    }

	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged event)
	{
		int id = event.getContainerId();
		if (isBankSyncContainerId(id))
		{
			runStateManager.setBankSynced(true);
		}

		if (isTrackedContainerId(id))
		{
			if (panel != null)
			{
				panel.refresh();
			}
		}
	}

	private WorldPoint currentNavigationTarget()
	{
		if (runStateManager.getState() == RunState.NEEDS_BANK_SYNC)
		{
			BankLocation bank = BankLocations.nearestTo(lastPlayerLocation);
			return bank.getWorldPoint();
		}

		RouteStop stop = runStateManager.currentStop();
		return stop == null ? null : stop.getLocation().getWorldPoint();
	}

	private WorldPoint currentPlayerLocation()
	{
		if (client == null || client.getLocalPlayer() == null)
		{
			return null;
		}

		return client.getLocalPlayer().getWorldLocation();
	}

	static boolean isBankSyncContainerId(int containerId)
	{
		return containerId == InventoryID.BANK.getId() || containerId == InventoryID.SEED_VAULT.getId();
	}

	static boolean isTrackedContainerId(int containerId)
	{
		return isBankSyncContainerId(containerId)
			|| containerId == InventoryID.INVENTORY.getId()
			|| containerId == InventoryID.EQUIPMENT.getId();
	}

	static boolean hasReachedShortestPathTarget(WorldPoint playerLocation, WorldPoint target)
	{
		return playerLocation != null
			&& target != null
			&& playerLocation.distanceTo(target) < SHORTEST_PATH_DEFAULT_REACHED_DISTANCE;
	}

    @Provides
    BraindeadFarmingConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(BraindeadFarmingConfig.class);
    }
}
