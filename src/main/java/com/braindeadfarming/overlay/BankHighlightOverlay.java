package com.braindeadfarming.overlay;

import com.braindeadfarming.BraindeadFarmingConfig;
import com.braindeadfarming.bank.BankInventoryTracker;
import com.braindeadfarming.bank.InventoryRequirements;
import com.braindeadfarming.route.FarmingRoute;
import com.braindeadfarming.state.RunStateManager;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.WidgetItemOverlay;

@Singleton
public class BankHighlightOverlay extends WidgetItemOverlay
{
	private final ItemManager itemManager;
	private final RunStateManager runStateManager;
	private final BankInventoryTracker bankInventoryTracker;
	private final BraindeadFarmingConfig config;

	private FarmingRoute cachedRoute;
	private Map<Integer, InventoryRequirements.RequiredItem> cachedRequiredById = new HashMap<>();

	@Inject
	public BankHighlightOverlay(ItemManager itemManager, RunStateManager runStateManager, BankInventoryTracker bankInventoryTracker, BraindeadFarmingConfig config)
	{
		this.itemManager = itemManager;
		this.runStateManager = runStateManager;
		this.bankInventoryTracker = bankInventoryTracker;
		this.config = config;
		showOnBank();
	}

	@Override
	public void renderItemOverlay(Graphics2D graphics, int itemId, WidgetItem widgetItem)
	{
		if (!config.showOverlay())
		{
			return;
		}

		FarmingRoute route = runStateManager.getRoute();
		if (route == null || route.getRequiredItems() == null || route.getRequiredItems().isEmpty())
		{
			return;
		}

		int canonicalItemId = canonicalize(itemId);
		InventoryRequirements.RequiredItem req = requiredItemsById(route).get(canonicalItemId);
		if (req == null)
		{
			return;
		}

		int have = req.have(bankInventoryTracker.getSnapshot());
		int haveInHand = req.have(bankInventoryTracker.getHandSnapshot());
		int need = req.getQuantity();
		int remaining = need - haveInHand;

		if (remaining <= 0)
		{
			return;
		}

		Color color;
		if (have >= need)
		{
			color = new Color(0, 255, 0, 120);
		}
		else if (have > 0)
		{
			color = new Color(255, 200, 0, 120);
		}
		else
		{
			color = new Color(255, 0, 0, 120);
		}

		Rectangle bounds = widgetItem.getCanvasBounds();
		graphics.setColor(color);
		graphics.draw(bounds);
	}

	private int canonicalize(int itemId)
	{
		try
		{
			return itemManager.canonicalize(itemId);
		}
		catch (Exception e)
		{
			return itemId;
		}
	}

	private Map<Integer, InventoryRequirements.RequiredItem> requiredItemsById(FarmingRoute route)
	{
		if (route == cachedRoute)
		{
			return cachedRequiredById;
		}

		Map<Integer, InventoryRequirements.RequiredItem> map = new HashMap<>();
		for (InventoryRequirements.RequiredItem item : route.getRequiredItems())
		{
			if (item.getItemIds() == null || item.getItemIds().length == 0)
			{
				continue;
			}

			for (int id : item.getItemIds())
			{
				map.put(id, item);
			}
		}

		cachedRoute = route;
		cachedRequiredById = map;
		return map;
	}
}
