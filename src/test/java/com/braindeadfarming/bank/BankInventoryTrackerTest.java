package com.braindeadfarming.bank;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import net.runelite.api.InventoryID;
import net.runelite.api.ItemID;
import org.junit.Assert;
import org.junit.Test;

public class BankInventoryTrackerTest
{
	@Test
	public void getHandSnapshotIncludesInventoryAndEquipment() throws Exception
	{
		BankInventoryTracker tracker = new BankInventoryTracker(null, null);
		Map<Integer, Map<Integer, Integer>> containers = perContainerQuantities(tracker);
		containers.put(InventoryID.INVENTORY.getId(), quantities(ItemID.SPADE, 1));
		containers.put(InventoryID.EQUIPMENT.getId(), quantities(ItemID.RING_OF_DUELING8, 1));

		ItemAvailabilitySnapshot snapshot = tracker.getHandSnapshot();

		Assert.assertEquals(1, snapshot.getQuantity(ItemID.SPADE));
		Assert.assertEquals(1, snapshot.getQuantity(ItemID.RING_OF_DUELING8));
	}

	@Test
	public void getHandSnapshotExcludesBankAndSeedVault() throws Exception
	{
		BankInventoryTracker tracker = new BankInventoryTracker(null, null);
		Map<Integer, Map<Integer, Integer>> containers = perContainerQuantities(tracker);
		containers.put(InventoryID.BANK.getId(), quantities(ItemID.SPADE, 1));
		containers.put(InventoryID.SEED_VAULT.getId(), quantities(ItemID.YEW_SAPLING, 6));

		ItemAvailabilitySnapshot snapshot = tracker.getHandSnapshot();

		Assert.assertEquals(0, snapshot.getQuantity(ItemID.SPADE));
		Assert.assertEquals(0, snapshot.getQuantity(ItemID.YEW_SAPLING));
	}

	@SuppressWarnings("unchecked")
	private Map<Integer, Map<Integer, Integer>> perContainerQuantities(BankInventoryTracker tracker) throws Exception
	{
		Field field = BankInventoryTracker.class.getDeclaredField("perContainerQuantities");
		field.setAccessible(true);
		return (Map<Integer, Map<Integer, Integer>>) field.get(tracker);
	}

	private Map<Integer, Integer> quantities(int itemId, int quantity)
	{
		Map<Integer, Integer> quantities = new HashMap<>();
		quantities.put(itemId, quantity);
		return quantities;
	}
}
