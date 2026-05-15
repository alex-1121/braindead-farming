package com.braindeadfarming.bank;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import net.runelite.api.coords.WorldPoint;

public final class BankLocations
{
	private static final BankLocation DEFAULT_BANK = new BankLocation("Grand Exchange", new WorldPoint(3164, 3487, 0));

	private static final List<BankLocation> BANKS = Arrays.asList(
		DEFAULT_BANK,
		new BankLocation("Varrock West", new WorldPoint(3185, 3436, 0)),
		new BankLocation("Varrock East", new WorldPoint(3253, 3420, 0)),
		new BankLocation("Lumbridge Castle", new WorldPoint(3208, 3220, 2)),
		new BankLocation("Draynor Village", new WorldPoint(3092, 3245, 0)),
		new BankLocation("Falador East", new WorldPoint(3013, 3355, 0)),
		new BankLocation("Falador West", new WorldPoint(2946, 3368, 0)),
		new BankLocation("Edgeville", new WorldPoint(3094, 3492, 0)),
		new BankLocation("Seers' Village", new WorldPoint(2725, 3491, 0)),
		new BankLocation("Catherby", new WorldPoint(2809, 3441, 0)),
		new BankLocation("Ardougne North", new WorldPoint(2615, 3332, 0)),
		new BankLocation("Castle Wars", new WorldPoint(2443, 3083, 0)),
		new BankLocation("Ferox Enclave", new WorldPoint(3130, 3632, 0)),
		new BankLocation("TzHaar", new WorldPoint(2446, 5178, 0)),
		new BankLocation("Shilo Village", new WorldPoint(2852, 2954, 0))
	);

	private BankLocations()
	{
	}

	public static BankLocation nearestTo(WorldPoint playerLocation)
	{
		if (playerLocation == null)
		{
			return DEFAULT_BANK;
		}

		return BANKS.stream()
			.min(Comparator
				.comparingInt((BankLocation bank) -> bank.getWorldPoint().getPlane() == playerLocation.getPlane() ? 0 : 1)
				.thenComparingInt(bank -> bank.getWorldPoint().distanceTo2D(playerLocation))
				.thenComparing(BankLocation::getName))
			.orElse(DEFAULT_BANK);
	}
}
