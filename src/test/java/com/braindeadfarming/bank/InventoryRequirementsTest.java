package com.braindeadfarming.bank;

import com.braindeadfarming.BraindeadFarmingConfig;
import com.braindeadfarming.data.FarmingLocations;
import com.braindeadfarming.data.PatchLocation;
import com.braindeadfarming.data.SeedData;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import net.runelite.api.ItemID;
import org.junit.Assert;
import org.junit.Test;

public class InventoryRequirementsTest
{
	@Test
	public void computesSaplingsAndTools()
	{
		BraindeadFarmingConfig config = new BraindeadFarmingConfig()
		{
			@Override
			public SeedData.TreeSeed treeSeed()
			{
				return SeedData.TreeSeed.YEW;
			}

			@Override
			public SeedData.FruitTreeSeed fruitTreeSeed()
			{
				return SeedData.FruitTreeSeed.PALM;
			}
		};

		List<PatchLocation> locations = Arrays.asList(
			FarmingLocations.coreById().get(FarmingLocations.TREE_VARROCK),
			FarmingLocations.coreById().get(FarmingLocations.TREE_LUMBRIDGE),
			FarmingLocations.coreById().get(FarmingLocations.FRUIT_CATHERBY)
		);

		List<InventoryRequirements.RequiredItem> required = InventoryRequirements.computeRequiredItems(config, locations, null);

		Assert.assertTrue(required.stream().anyMatch(r -> r.getDisplayItemId() == ItemID.YEW_SAPLING && r.getQuantity() == 2));
		Assert.assertTrue(required.stream().anyMatch(r -> r.getDisplayItemId() == ItemID.PALM_SAPLING && r.getQuantity() == 1));
		Assert.assertTrue(required.stream().anyMatch(r -> r.getDisplayItemId() == ItemID.SPADE));
		Assert.assertTrue(required.stream().anyMatch(r -> r.getDisplayItemId() == ItemID.SEED_DIBBER));
		Assert.assertTrue(required.stream().anyMatch(r -> r.getDisplayItemId() == ItemID.BRONZE_AXE && r.getQuantity() == 1));
		Assert.assertTrue(required.stream().anyMatch(r -> r.getDisplayItemId() == ItemID.LOG_BASKET && r.getQuantity() == 1));
		Assert.assertTrue(required.stream().anyMatch(r -> IntStream.of(r.getItemIds()).anyMatch(id -> id == ItemID.CRYSTAL_FELLING_AXE)));
		Assert.assertTrue(required.stream().anyMatch(r -> IntStream.of(r.getItemIds()).anyMatch(id -> id == ItemID.OPEN_LOG_BASKET)));
	}

	@Test
	public void omitsTreeOnlyToolsWhenRouteHasNoTreePatches()
	{
		BraindeadFarmingConfig config = new BraindeadFarmingConfig()
		{
			@Override
			public SeedData.FruitTreeSeed fruitTreeSeed()
			{
				return SeedData.FruitTreeSeed.PALM;
			}
		};

		List<PatchLocation> locations = Arrays.asList(
			FarmingLocations.coreById().get(FarmingLocations.FRUIT_CATHERBY),
			FarmingLocations.coreById().get(FarmingLocations.FRUIT_BRIMHAVEN)
		);

		List<InventoryRequirements.RequiredItem> required = InventoryRequirements.computeRequiredItems(config, locations, null);

		Assert.assertFalse(required.stream().anyMatch(r -> r.getDisplayItemId() == ItemID.BRONZE_AXE));
		Assert.assertFalse(required.stream().anyMatch(r -> r.getDisplayItemId() == ItemID.LOG_BASKET));
	}
}
