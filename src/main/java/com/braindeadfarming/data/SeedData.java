package com.braindeadfarming.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.ItemID;

public final class SeedData
{
	private SeedData()
	{
	}

	@RequiredArgsConstructor
	@Getter
	public enum TreeSeed
	{
		OAK("Oak", 15, ItemID.OAK_SAPLING),
		WILLOW("Willow", 30, ItemID.WILLOW_SAPLING),
		MAPLE("Maple", 45, ItemID.MAPLE_SAPLING),
		YEW("Yew", 60, ItemID.YEW_SAPLING),
		MAGIC("Magic", 75, ItemID.MAGIC_SAPLING),
		;

		private final String displayName;
		private final int farmingLevel;
		private final int saplingItemId;

		@Override
		public String toString()
		{
			return displayName;
		}
	}

	@RequiredArgsConstructor
	@Getter
	public enum FruitTreeSeed
	{
		APPLE("Apple", 27, ItemID.APPLE_SAPLING),
		BANANA("Banana", 33, ItemID.BANANA_SAPLING),
		ORANGE("Orange", 39, ItemID.ORANGE_SAPLING),
		CURRY("Curry", 42, ItemID.CURRY_SAPLING),
		PINEAPPLE("Pineapple", 51, ItemID.PINEAPPLE_SAPLING),
		PAPAYA("Papaya", 57, ItemID.PAPAYA_SAPLING),
		PALM("Palm", 68, ItemID.PALM_SAPLING),
		DRAGONFRUIT("Dragonfruit", 81, ItemID.DRAGONFRUIT_SAPLING),
		;

		private final String displayName;
		private final int farmingLevel;
		private final int saplingItemId;

		@Override
		public String toString()
		{
			return displayName;
		}
	}
}

