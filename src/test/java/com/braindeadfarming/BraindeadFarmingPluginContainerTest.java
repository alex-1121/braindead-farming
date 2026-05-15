package com.braindeadfarming;

import net.runelite.api.InventoryID;
import net.runelite.api.coords.WorldPoint;
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

	@Test
	public void shortestPathTargetIsReachedWithinDefaultFinishDistance()
	{
		WorldPoint target = new WorldPoint(3185, 3436, 0);

		Assert.assertTrue(BraindeadFarmingPlugin.hasReachedShortestPathTarget(new WorldPoint(3185, 3436, 0), target));
		Assert.assertTrue(BraindeadFarmingPlugin.hasReachedShortestPathTarget(new WorldPoint(3189, 3436, 0), target));
		Assert.assertFalse(BraindeadFarmingPlugin.hasReachedShortestPathTarget(new WorldPoint(3190, 3436, 0), target));
	}

	@Test
	public void shortestPathTargetReachRequiresSamePlaneAndKnownLocations()
	{
		WorldPoint target = new WorldPoint(3185, 3436, 0);

		Assert.assertFalse(BraindeadFarmingPlugin.hasReachedShortestPathTarget(new WorldPoint(3185, 3436, 1), target));
		Assert.assertFalse(BraindeadFarmingPlugin.hasReachedShortestPathTarget(null, target));
		Assert.assertFalse(BraindeadFarmingPlugin.hasReachedShortestPathTarget(new WorldPoint(3185, 3436, 0), null));
	}
}
