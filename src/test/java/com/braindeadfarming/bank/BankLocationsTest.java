package com.braindeadfarming.bank;

import net.runelite.api.coords.WorldPoint;
import org.junit.Assert;
import org.junit.Test;

public class BankLocationsTest
{
	@Test
	public void defaultsToGrandExchangeWithoutPlayerLocation()
	{
		BankLocation bank = BankLocations.nearestTo(null);

		Assert.assertEquals("Grand Exchange", bank.getName());
	}

	@Test
	public void picksNearestSamePlaneBank()
	{
		BankLocation bank = BankLocations.nearestTo(new WorldPoint(3180, 3434, 0));

		Assert.assertEquals("Varrock West", bank.getName());
	}
}
