package com.braindeadfarming.data;

import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class TeleportHintsTest
{
	@Test
	public void eachCoreLocationHasAtLeastOneTeleport()
	{
		for (PatchLocation location : FarmingLocations.coreLocations())
		{
			Assert.assertFalse(location.getTeleports().isEmpty());
		}
	}

	@Test
	public void preferredHintLocationsHaveUserHintTeleport()
	{
		assertHasHint(FarmingLocations.TREE_GNOME_STRONGHOLD);
		assertHasHint(FarmingLocations.TREE_VARLAMORE);
		assertHasHint(FarmingLocations.FRUIT_GNOME_STRONGHOLD);
		assertHasHint(FarmingLocations.FRUIT_TREE_GNOME_VILLAGE);
		assertHasHint(FarmingLocations.FRUIT_CATHERBY);
		assertHasHint(FarmingLocations.FRUIT_KASTORI);
		assertHasHint(FarmingLocations.FRUIT_BRIMHAVEN);
	}

	private void assertHasHint(String id)
	{
		PatchLocation l = FarmingLocations.coreById().get(id);
		Assert.assertNotNull(l);

		boolean hasHint = l.getTeleports().stream().anyMatch(TeleportOption::isUserHint);
		Assert.assertTrue("Expected userHint teleport for " + id, hasHint);
	}
}

