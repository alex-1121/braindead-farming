package com.braindeadfarming.data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;

public class CoreLocationsTest
{
	@Test
	public void coreLocationsAreUniqueAndComplete()
	{
		List<PatchLocation> locations = FarmingLocations.coreLocations();
		Assert.assertEquals(12, locations.size());

		Set<String> ids = new HashSet<>();
		for (PatchLocation l : locations)
		{
			Assert.assertNotNull(l.getId());
			Assert.assertTrue(ids.add(l.getId()));
			Assert.assertNotNull(l.getName());
			Assert.assertNotNull(l.getPatchType());
			Assert.assertNotNull(l.getWorldPoint());
			Assert.assertNotNull(l.getTeleports());
		}
	}
}

