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
	public void picksNearestBankBy2dDistance()
	{
		BankLocation bank = BankLocations.nearestTo(new WorldPoint(3180, 3434, 0));

		Assert.assertEquals("Varrock West", bank.getName());
	}

	@Test
	public void picksLumbridgeCastleBankFromGroundFloor()
	{
		BankLocation bank = BankLocations.nearestTo(new WorldPoint(3208, 3220, 0));

		Assert.assertEquals("Lumbridge Castle", bank.getName());
	}

	@Test
	public void picksLumbridgeCastleBankFromMiddleFloor()
	{
		BankLocation bank = BankLocations.nearestTo(new WorldPoint(3208, 3220, 1));

		Assert.assertEquals("Lumbridge Castle", bank.getName());
	}

	@Test
	public void picksLumbridgeCastleBankFromTopFloor()
	{
		BankLocation bank = BankLocations.nearestTo(new WorldPoint(3208, 3220, 2));

		Assert.assertEquals("Lumbridge Castle", bank.getName());
	}
}
