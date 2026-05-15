package com.braindeadfarming;

import net.runelite.api.InventoryID;
import org.junit.Assert;
import org.junit.Test;

public class BraindeadFarmingPluginContainerTest
{
	@Test
	public void bankSyncContainersExcludeInventoryAndEquipment()
	{
		Assert.assertTrue(BraindeadFarmingPlugin.isBankSyncContainerId(InventoryID.BANK.getId()));
		Assert.assertTrue(BraindeadFarmingPlugin.isBankSyncContainerId(InventoryID.SEED_VAULT.getId()));
		Assert.assertFalse(BraindeadFarmingPlugin.isBankSyncContainerId(InventoryID.INVENTORY.getId()));
		Assert.assertFalse(BraindeadFarmingPlugin.isBankSyncContainerId(InventoryID.EQUIPMENT.getId()));
	}

	@Test
	public void trackedContainersStillIncludeInventoryAndEquipment()
	{
		Assert.assertTrue(BraindeadFarmingPlugin.isTrackedContainerId(InventoryID.BANK.getId()));
		Assert.assertTrue(BraindeadFarmingPlugin.isTrackedContainerId(InventoryID.SEED_VAULT.getId()));
		Assert.assertTrue(BraindeadFarmingPlugin.isTrackedContainerId(InventoryID.INVENTORY.getId()));
		Assert.assertTrue(BraindeadFarmingPlugin.isTrackedContainerId(InventoryID.EQUIPMENT.getId()));
	}
}
