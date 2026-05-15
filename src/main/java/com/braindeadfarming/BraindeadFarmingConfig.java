package com.braindeadfarming;

import com.braindeadfarming.data.SeedData;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;

@ConfigGroup("braindeadfarming")
public interface BraindeadFarmingConfig extends Config
{
	@ConfigSection(
		name = "Seeds",
		description = "What you want to plant",
		position = 0
	)
	String seedsSection = "seeds";

	@ConfigSection(
		name = "Locations",
		description = "Which patches to include",
		position = 1
	)
	String locationsSection = "locations";

	@ConfigSection(
		name = "Teleports",
		description = "What travel methods you can use",
		position = 2
	)
	String teleportsSection = "teleports";

    @ConfigItem(
        keyName = "showOverlay",
        name = "Show overlay",
        description = "Shows a simple starter overlay placeholder"
    )
    default boolean showOverlay()
    {
        return true;
    }

	@ConfigItem(
		keyName = "treeSeed",
		name = "Tree seed",
		description = "Tree sapling to plan for",
		section = seedsSection,
		position = 0
	)
	default SeedData.TreeSeed treeSeed()
	{
		return SeedData.TreeSeed.MAPLE;
	}

	@ConfigItem(
		keyName = "fruitTreeSeed",
		name = "Fruit tree seed",
		description = "Fruit tree sapling to plan for",
		section = seedsSection,
		position = 1
	)
	default SeedData.FruitTreeSeed fruitTreeSeed()
	{
		return SeedData.FruitTreeSeed.PAPAYA;
	}

	@ConfigItem(
		keyName = "useCompost",
		name = "Use compost",
		description = "Include compost in the bank checklist",
		section = seedsSection,
		position = 2
	)
	default boolean useCompost()
	{
		return true;
	}

	@ConfigItem(
		keyName = "compostQuantityPerPatch",
		name = "Compost per patch",
		description = "How many compost buckets to plan per patch (usually 1)",
		section = seedsSection,
		position = 3
	)
	@Range(
		min = 0,
		max = 10
	)
	default int compostQuantityPerPatch()
	{
		return 1;
	}

	@ConfigItem(
		keyName = "includeTreeVarrock",
		name = "Varrock (Tree)",
		description = "Include Varrock tree patch",
		section = locationsSection,
		position = 0
	)
	default boolean includeTreeVarrock()
	{
		return true;
	}

	@ConfigItem(
		keyName = "includeTreeLumbridge",
		name = "Lumbridge (Tree)",
		description = "Include Lumbridge tree patch",
		section = locationsSection,
		position = 1
	)
	default boolean includeTreeLumbridge()
	{
		return true;
	}

	@ConfigItem(
		keyName = "includeTreeGnomeStronghold",
		name = "Gnome Stronghold (Tree)",
		description = "Include Tree Gnome Stronghold tree patch",
		section = locationsSection,
		position = 2
	)
	default boolean includeTreeGnomeStronghold()
	{
		return true;
	}

	@ConfigItem(
		keyName = "includeTreeFaladorPark",
		name = "Falador Park (Tree)",
		description = "Include Falador Park tree patch",
		section = locationsSection,
		position = 3
	)
	default boolean includeTreeFaladorPark()
	{
		return true;
	}

	@ConfigItem(
		keyName = "includeTreeTaverley",
		name = "Taverley (Tree)",
		description = "Include Taverley tree patch",
		section = locationsSection,
		position = 4
	)
	default boolean includeTreeTaverley()
	{
		return true;
	}

	@ConfigItem(
		keyName = "includeTreeVarlamore",
		name = "Varlamore (Tree)",
		description = "Include Varlamore tree patch",
		section = locationsSection,
		position = 5
	)
	default boolean includeTreeVarlamore()
	{
		return true;
	}

	@ConfigItem(
		keyName = "includeFruitGnomeStronghold",
		name = "Gnome Stronghold (Fruit)",
		description = "Include Tree Gnome Stronghold fruit tree patch",
		section = locationsSection,
		position = 6
	)
	default boolean includeFruitGnomeStronghold()
	{
		return true;
	}

	@ConfigItem(
		keyName = "includeFruitGnomeVillage",
		name = "Gnome Village (Fruit)",
		description = "Include Tree Gnome Village fruit tree patch",
		section = locationsSection,
		position = 7
	)
	default boolean includeFruitGnomeVillage()
	{
		return true;
	}

	@ConfigItem(
		keyName = "includeFruitLletya",
		name = "Lletya (Fruit)",
		description = "Include Lletya fruit tree patch",
		section = locationsSection,
		position = 8
	)
	default boolean includeFruitLletya()
	{
		return true;
	}

	@ConfigItem(
		keyName = "includeFruitCatherby",
		name = "Catherby (Fruit)",
		description = "Include Catherby fruit tree patch",
		section = locationsSection,
		position = 9
	)
	default boolean includeFruitCatherby()
	{
		return true;
	}

	@ConfigItem(
		keyName = "includeFruitKastori",
		name = "Kastori (Fruit)",
		description = "Include Kastori fruit tree patch",
		section = locationsSection,
		position = 10
	)
	default boolean includeFruitKastori()
	{
		return true;
	}

	@ConfigItem(
		keyName = "includeFruitBrimhaven",
		name = "Brimhaven (Fruit)",
		description = "Include Brimhaven fruit tree patch",
		section = locationsSection,
		position = 11
	)
	default boolean includeFruitBrimhaven()
	{
		return true;
	}

	@ConfigItem(
		keyName = "allowStandardTeleports",
		name = "Standard teleports",
		description = "Allow spellbook/tablet teleports (Varrock/Lumbridge/Falador/Camelot/etc.)",
		section = teleportsSection,
		position = 0
	)
	default boolean allowStandardTeleports()
	{
		return true;
	}

	@ConfigItem(
		keyName = "allowJewelleryTeleports",
		name = "Jewellery teleports",
		description = "Allow jewellery teleports (Ring of wealth, Slayer ring, Glory, etc.)",
		section = teleportsSection,
		position = 1
	)
	default boolean allowJewelleryTeleports()
	{
		return true;
	}

	@ConfigItem(
		keyName = "allowFairyRings",
		name = "Fairy rings",
		description = "Allow fairy ring travel steps",
		section = teleportsSection,
		position = 2
	)
	default boolean allowFairyRings()
	{
		return true;
	}

	@ConfigItem(
		keyName = "allowSpiritTrees",
		name = "Spirit trees",
		description = "Allow spirit tree travel steps",
		section = teleportsSection,
		position = 3
	)
	default boolean allowSpiritTrees()
	{
		return true;
	}
}
