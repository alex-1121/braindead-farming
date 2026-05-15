package com.braindeadfarming.route;

import com.braindeadfarming.BraindeadFarmingConfig;
import com.braindeadfarming.bank.ItemAvailabilitySnapshot;
import com.braindeadfarming.data.FarmingLocations;
import java.util.HashMap;
import java.util.Map;
import net.runelite.api.ItemID;
import org.junit.Assert;
import org.junit.Test;

public class RoutePlannerTest
{
	@Test
	public void prefersUserHintTeleportWhenAvailable()
	{
		BraindeadFarmingConfig config = new BraindeadFarmingConfig()
		{
			@Override
			public boolean includeTreeLumbridge()
			{
				return false;
			}

			@Override
			public boolean includeTreeGnomeStronghold()
			{
				return false;
			}

			@Override
			public boolean includeTreeFaladorPark()
			{
				return false;
			}

			@Override
			public boolean includeTreeTaverley()
			{
				return false;
			}

			@Override
			public boolean includeTreeVarlamore()
			{
				return false;
			}

			@Override
			public boolean includeFruitGnomeStronghold()
			{
				return false;
			}

			@Override
			public boolean includeFruitGnomeVillage()
			{
				return false;
			}

			@Override
			public boolean includeFruitLletya()
			{
				return false;
			}

			@Override
			public boolean includeFruitCatherby()
			{
				return false;
			}

			@Override
			public boolean includeFruitKastori()
			{
				return false;
			}

			@Override
			public boolean includeFruitBrimhaven()
			{
				return false;
			}
		};

		Map<Integer, Integer> quantities = new HashMap<>();
		quantities.put(ItemID.RING_OF_WEALTH, 1);
		ItemAvailabilitySnapshot items = new ItemAvailabilitySnapshot(quantities);

		RoutePlanner planner = new RoutePlanner();
		FarmingRoute route = planner.plan(config, items);
		Assert.assertEquals(1, route.getStops().size());
		Assert.assertEquals(FarmingLocations.TREE_VARROCK, route.getStops().get(0).getLocation().getId());
		Assert.assertNotNull(route.getStops().get(0).getTeleport());
		Assert.assertTrue(route.getStops().get(0).getTeleport().isUserHint());
	}
}

