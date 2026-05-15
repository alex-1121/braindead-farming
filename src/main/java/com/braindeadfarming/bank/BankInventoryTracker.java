package com.braindeadfarming.bank;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.Getter;
import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;

@Singleton
public class BankInventoryTracker
{
	private final Client client;
	private final ItemManager itemManager;

	@Getter
	private ItemAvailabilitySnapshot snapshot = ItemAvailabilitySnapshot.empty();

	private final Map<Integer, Map<Integer, Integer>> perContainerQuantities = new HashMap<>();

	@Inject
	public BankInventoryTracker(Client client, ItemManager itemManager)
	{
		this.client = client;
		this.itemManager = itemManager;
	}

	public void reset()
	{
		perContainerQuantities.clear();
		snapshot = ItemAvailabilitySnapshot.empty();
	}

	public ItemAvailabilitySnapshot getHandSnapshot()
	{
		Map<Integer, Integer> merged = new HashMap<>();
		for (int id : new int[]{ InventoryID.INVENTORY.getId(), InventoryID.EQUIPMENT.getId() })
		{
			Map<Integer, Integer> quantities = perContainerQuantities.get(id);
			if (quantities != null)
			{
				quantities.forEach((itemId, quantity) -> merged.merge(itemId, quantity, Integer::sum));
			}
		}

		return new ItemAvailabilitySnapshot(merged);
	}

	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged event)
	{
		int id = event.getContainerId();
		if (!isTrackedContainer(id))
		{
			return;
		}

		ItemContainer container = event.getItemContainer();
		if (container == null)
		{
			return;
		}

		Map<Integer, Integer> quantities = computeQuantities(container);
		perContainerQuantities.put(id, quantities);
		rebuildSnapshot();
	}

	private boolean isTrackedContainer(int containerId)
	{
		return containerId == InventoryID.BANK.getId()
			|| containerId == InventoryID.SEED_VAULT.getId()
			|| containerId == InventoryID.INVENTORY.getId()
			|| containerId == InventoryID.EQUIPMENT.getId();
	}

	private Map<Integer, Integer> computeQuantities(ItemContainer container)
	{
		Item[] items = container.getItems();
		if (items == null || items.length == 0)
		{
			return Collections.emptyMap();
		}

		Map<Integer, Integer> map = new HashMap<>();
		for (Item item : items)
		{
			if (item == null)
			{
				continue;
			}

			int id = item.getId();
			if (id <= 0)
			{
				continue;
			}

			int qty = item.getQuantity();
			if (qty <= 0)
			{
				continue;
			}

			int canonicalId = canonicalize(id);
			map.merge(canonicalId, qty, Integer::sum);
		}

		return map;
	}

	private int canonicalize(int itemId)
	{
		// Canonicalize noted/placeholder variants when possible.
		try
		{
			return itemManager.canonicalize(itemId);
		}
		catch (Exception e)
		{
			return itemId;
		}
	}

	private void rebuildSnapshot()
	{
		Map<Integer, Integer> merged = new HashMap<>();
		for (Map<Integer, Integer> container : perContainerQuantities.values())
		{
			for (Map.Entry<Integer, Integer> entry : container.entrySet())
			{
				merged.merge(entry.getKey(), entry.getValue(), Integer::sum);
			}
		}

		snapshot = new ItemAvailabilitySnapshot(merged);
	}
}
