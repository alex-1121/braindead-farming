package com.braindeadfarming.data;

import static com.braindeadfarming.data.TravelStep.TravelStepType.FAIRY_RING;
import static com.braindeadfarming.data.TravelStep.TravelStepType.SPIRIT_TREE;
import static com.braindeadfarming.data.TravelStep.TravelStepType.TELEPORT;
import static com.braindeadfarming.data.TravelStep.TravelStepType.WALK;

import com.braindeadfarming.requirements.AnyOfItemRequirement;
import com.braindeadfarming.requirements.ItemRequirement;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.runelite.api.ItemID;
import net.runelite.api.coords.WorldPoint;

public final class FarmingLocations
{
	public static final String TREE_VARROCK = "TREE_VARROCK";
	public static final String TREE_LUMBRIDGE = "TREE_LUMBRIDGE";
	public static final String TREE_GNOME_STRONGHOLD = "TREE_GNOME_STRONGHOLD";
	public static final String TREE_FALADOR_PARK = "TREE_FALADOR_PARK";
	public static final String TREE_TAVERLEY = "TREE_TAVERLEY";
	public static final String TREE_VARLAMORE = "TREE_VARLAMORE";

	public static final String FRUIT_GNOME_STRONGHOLD = "FRUIT_GNOME_STRONGHOLD";
	public static final String FRUIT_TREE_GNOME_VILLAGE = "FRUIT_TREE_GNOME_VILLAGE";
	public static final String FRUIT_LLETYA = "FRUIT_LLETYA";
	public static final String FRUIT_CATHERBY = "FRUIT_CATHERBY";
	public static final String FRUIT_KASTORI = "FRUIT_KASTORI";
	public static final String FRUIT_BRIMHAVEN = "FRUIT_BRIMHAVEN";

	private FarmingLocations()
	{
	}

	/**
	 * Core patch tiles are sourced from RuneLite's world map farming patch locations.
	 */
	public static List<PatchLocation> coreLocations()
	{
		PatchLocation treeVarrock = PatchLocation.of(
			TREE_VARROCK,
			"Varrock (Tree)",
			PatchType.TREE,
			new WorldPoint(3226, 3457, 0),
			Collections.emptyList(),
			Arrays.asList(
				TeleportOption.of(
					"Varrock Teleport",
					Arrays.asList(
						new TravelStep(TELEPORT, "Cast Varrock teleport (or use tablet).", new WorldPoint(3213, 3424, 0), TravelStep.TravelMethod.SPELLBOOK_OR_TABLET),
						new TravelStep(WALK, "Walk to Varrock tree patch.", new WorldPoint(3226, 3457, 0), TravelStep.TravelMethod.WALK)
					),
					Arrays.asList(new ItemRequirement("Varrock teleport (tablet or runes)", ItemID.VARROCK_TELEPORT)),
					10,
					10,
					false
				),
				TeleportOption.of(
					"Grand Exchange (Ring of wealth)",
					Arrays.asList(
						new TravelStep(TELEPORT, "Teleport to Grand Exchange (Ring of wealth).", new WorldPoint(3162, 3480, 0), TravelStep.TravelMethod.JEWELLERY),
						new TravelStep(WALK, "Walk to Varrock tree patch.", new WorldPoint(3226, 3457, 0), TravelStep.TravelMethod.WALK)
					),
					Arrays.asList(new AnyOfItemRequirement("Ring of wealth", 1,
						ItemID.RING_OF_WEALTH,
						ItemID.RING_OF_WEALTH_1, ItemID.RING_OF_WEALTH_2, ItemID.RING_OF_WEALTH_3, ItemID.RING_OF_WEALTH_4, ItemID.RING_OF_WEALTH_5,
						ItemID.RING_OF_WEALTH_I,
						ItemID.RING_OF_WEALTH_I1, ItemID.RING_OF_WEALTH_I2, ItemID.RING_OF_WEALTH_I3, ItemID.RING_OF_WEALTH_I4, ItemID.RING_OF_WEALTH_I5
					)),
					20,
					0,
					true
				)
			)
		);

		PatchLocation treeLumbridge = PatchLocation.of(
			TREE_LUMBRIDGE,
			"Lumbridge (Tree)",
			PatchType.TREE,
			new WorldPoint(3189, 3233, 0),
			Collections.emptyList(),
			Arrays.asList(
				TeleportOption.of(
					"Lumbridge Teleport",
					Arrays.asList(
						new TravelStep(TELEPORT, "Cast Lumbridge teleport (or use tablet).", new WorldPoint(3222, 3218, 0), TravelStep.TravelMethod.SPELLBOOK_OR_TABLET),
						new TravelStep(WALK, "Walk to Lumbridge tree patch.", new WorldPoint(3189, 3233, 0), TravelStep.TravelMethod.WALK)
					),
					Arrays.asList(new ItemRequirement("Lumbridge teleport (tablet or runes)", ItemID.LUMBRIDGE_TELEPORT)),
					10,
					10,
					false
				)
			)
		);

		PatchLocation treeGnomeStronghold = PatchLocation.of(
			TREE_GNOME_STRONGHOLD,
			"Tree Gnome Stronghold (Tree)",
			PatchType.TREE,
			new WorldPoint(2434, 3418, 0),
			Collections.emptyList(),
			Arrays.asList(
				TeleportOption.of(
					"Spirit Tree (GE -> Stronghold)",
					Arrays.asList(
						new TravelStep(SPIRIT_TREE, "Use Spirit Tree: Grand Exchange -> Tree Gnome Stronghold.", new WorldPoint(2461, 3444, 0), TravelStep.TravelMethod.SPIRIT_TREE),
						new TravelStep(WALK, "Walk to the tree patch.", new WorldPoint(2434, 3418, 0), TravelStep.TravelMethod.WALK)
					),
					Collections.emptyList(),
					10,
					0,
					true
				),
				TeleportOption.of(
					"Slayer Ring (Stronghold Slayer Cave)",
					Arrays.asList(
						new TravelStep(TELEPORT, "Teleport with Slayer ring to Stronghold Slayer Cave.", new WorldPoint(2433, 3421, 0), TravelStep.TravelMethod.JEWELLERY),
						new TravelStep(WALK, "Walk to the tree patch.", new WorldPoint(2434, 3418, 0), TravelStep.TravelMethod.WALK)
					),
					Arrays.asList(new AnyOfItemRequirement("Slayer ring", 1,
						ItemID.SLAYER_RING_1, ItemID.SLAYER_RING_2, ItemID.SLAYER_RING_3, ItemID.SLAYER_RING_4,
						ItemID.SLAYER_RING_5, ItemID.SLAYER_RING_6, ItemID.SLAYER_RING_7, ItemID.SLAYER_RING_8,
						ItemID.SLAYER_RING_ETERNAL
					)),
					20,
					10,
					false
				)
			)
		);

		PatchLocation treeFaladorPark = PatchLocation.of(
			TREE_FALADOR_PARK,
			"Falador Park (Tree)",
			PatchType.TREE,
			new WorldPoint(3005, 3375, 0),
			Collections.emptyList(),
			Arrays.asList(
				TeleportOption.of(
					"Falador Teleport",
					Arrays.asList(
						new TravelStep(TELEPORT, "Cast Falador teleport (or use tablet).", new WorldPoint(2965, 3381, 0), TravelStep.TravelMethod.SPELLBOOK_OR_TABLET),
						new TravelStep(WALK, "Walk to Falador Park tree patch.", new WorldPoint(3005, 3375, 0), TravelStep.TravelMethod.WALK)
					),
					Arrays.asList(new ItemRequirement("Falador teleport (tablet or runes)", ItemID.FALADOR_TELEPORT)),
					20,
					10,
					false
				),
				TeleportOption.of(
					"Falador Park (Ring of wealth)",
					Arrays.asList(
						new TravelStep(TELEPORT, "Teleport to Falador Park (Ring of wealth).", new WorldPoint(2995, 3375, 0), TravelStep.TravelMethod.JEWELLERY),
						new TravelStep(WALK, "Walk to the tree patch.", new WorldPoint(3005, 3375, 0), TravelStep.TravelMethod.WALK)
					),
					Arrays.asList(new AnyOfItemRequirement("Ring of wealth", 1,
						ItemID.RING_OF_WEALTH,
						ItemID.RING_OF_WEALTH_1, ItemID.RING_OF_WEALTH_2, ItemID.RING_OF_WEALTH_3, ItemID.RING_OF_WEALTH_4, ItemID.RING_OF_WEALTH_5,
						ItemID.RING_OF_WEALTH_I,
						ItemID.RING_OF_WEALTH_I1, ItemID.RING_OF_WEALTH_I2, ItemID.RING_OF_WEALTH_I3, ItemID.RING_OF_WEALTH_I4, ItemID.RING_OF_WEALTH_I5
					)),
					10,
					0,
					true
				)
			)
		);

		PatchLocation treeTaverley = PatchLocation.of(
			TREE_TAVERLEY,
			"Taverley (Tree)",
			PatchType.TREE,
			new WorldPoint(2933, 3436, 0),
			Collections.emptyList(),
			Arrays.asList(
				TeleportOption.of(
					"Falador Teleport + walk",
					Arrays.asList(
						new TravelStep(TELEPORT, "Cast Falador teleport (or use tablet).", new WorldPoint(2965, 3381, 0), TravelStep.TravelMethod.SPELLBOOK_OR_TABLET),
						new TravelStep(WALK, "Walk to Taverley tree patch (via north-west exit).", new WorldPoint(2933, 3436, 0), TravelStep.TravelMethod.WALK)
					),
					Arrays.asList(new ItemRequirement("Falador teleport (tablet or runes)", ItemID.FALADOR_TELEPORT)),
					20,
					10,
					false
				),
				TeleportOption.of(
					"House teleport (Taverley portal)",
					Arrays.asList(
						new TravelStep(TELEPORT, "Teleport to your POH and use Taverley portal (if set).", new WorldPoint(2933, 3436, 0), TravelStep.TravelMethod.OTHER)
					),
					Collections.emptyList(),
					10,
					10,
					false
				)
			)
		);

		PatchLocation treeVarlamore = PatchLocation.of(
			TREE_VARLAMORE,
			"Varlamore (Tree)",
			PatchType.TREE,
			new WorldPoint(1366, 3318, 0),
			Collections.emptyList(),
			Arrays.asList(
				TeleportOption.of(
					"Fairy ring (AIS) + walk",
					Arrays.asList(
						new TravelStep(FAIRY_RING, "Use fairy ring code AIS.", new WorldPoint(1390, 3315, 0), TravelStep.TravelMethod.FAIRY_RING),
						new TravelStep(WALK, "Walk to the tree patch.", new WorldPoint(1366, 3318, 0), TravelStep.TravelMethod.WALK)
					),
					Collections.emptyList(),
					10,
					0,
					true
				),
				TeleportOption.of(
					"Civitas illa Fortis Teleport + walk",
					Arrays.asList(
						new TravelStep(TELEPORT, "Cast Civitas illa Fortis teleport.", new WorldPoint(1681, 3133, 0), TravelStep.TravelMethod.SPELLBOOK_OR_TABLET),
						new TravelStep(WALK, "Travel/walk to the tree patch.", new WorldPoint(1366, 3318, 0), TravelStep.TravelMethod.WALK)
					),
					Arrays.asList(new ItemRequirement("Civitas illa Fortis teleport (tablet or runes)", ItemID.CIVITAS_ILLA_FORTIS_TELEPORT)),
					30,
					10,
					false
				)
			)
		);

		PatchLocation fruitGnomeStronghold = PatchLocation.of(
			FRUIT_GNOME_STRONGHOLD,
			"Tree Gnome Stronghold (Fruit Tree)",
			PatchType.FRUIT_TREE,
			new WorldPoint(2472, 3445, 0),
			Collections.emptyList(),
			Arrays.asList(
				TeleportOption.of(
					"Spirit Tree (GE -> Stronghold)",
					Arrays.asList(
						new TravelStep(SPIRIT_TREE, "Use Spirit Tree: Grand Exchange -> Tree Gnome Stronghold.", new WorldPoint(2461, 3444, 0), TravelStep.TravelMethod.SPIRIT_TREE),
						new TravelStep(WALK, "Walk to the fruit tree patch.", new WorldPoint(2472, 3445, 0), TravelStep.TravelMethod.WALK)
					),
					Collections.emptyList(),
					10,
					0,
					true
				),
				TeleportOption.of(
					"Slayer Ring (Stronghold Slayer Cave)",
					Arrays.asList(
						new TravelStep(TELEPORT, "Teleport with Slayer ring to Stronghold Slayer Cave.", new WorldPoint(2433, 3421, 0), TravelStep.TravelMethod.JEWELLERY),
						new TravelStep(WALK, "Walk to the fruit tree patch.", new WorldPoint(2472, 3445, 0), TravelStep.TravelMethod.WALK)
					),
					Arrays.asList(new AnyOfItemRequirement("Slayer ring", 1,
						ItemID.SLAYER_RING_1, ItemID.SLAYER_RING_2, ItemID.SLAYER_RING_3, ItemID.SLAYER_RING_4,
						ItemID.SLAYER_RING_5, ItemID.SLAYER_RING_6, ItemID.SLAYER_RING_7, ItemID.SLAYER_RING_8,
						ItemID.SLAYER_RING_ETERNAL
					)),
					20,
					10,
					false
				)
			)
		);

		PatchLocation fruitGnomeVillage = PatchLocation.of(
			FRUIT_TREE_GNOME_VILLAGE,
			"Tree Gnome Village (Fruit Tree)",
			PatchType.FRUIT_TREE,
			new WorldPoint(2487, 3181, 0),
			Collections.emptyList(),
			Arrays.asList(
				TeleportOption.of(
					"Fairy ring (CIQ) + walk",
					Arrays.asList(
						new TravelStep(FAIRY_RING, "Use fairy ring code CIQ.", new WorldPoint(2487, 3181, 0), TravelStep.TravelMethod.FAIRY_RING),
						new TravelStep(WALK, "Walk to Tree Gnome Village fruit tree patch.", new WorldPoint(2487, 3181, 0), TravelStep.TravelMethod.WALK)
					),
					Collections.emptyList(),
					10,
					0,
					true
				),
				TeleportOption.of(
					"Spirit Tree (Stronghold -> Village)",
					Arrays.asList(
						new TravelStep(SPIRIT_TREE, "Use Spirit Tree: Tree Gnome Stronghold -> Tree Gnome Village.", new WorldPoint(2487, 3181, 0), TravelStep.TravelMethod.SPIRIT_TREE),
						new TravelStep(WALK, "Walk to the fruit tree patch.", new WorldPoint(2487, 3181, 0), TravelStep.TravelMethod.WALK)
					),
					Collections.emptyList(),
					20,
					10,
					false
				)
			)
		);

		PatchLocation fruitLletya = PatchLocation.of(
			FRUIT_LLETYA,
			"Lletya (Fruit Tree)",
			PatchType.FRUIT_TREE,
			new WorldPoint(2343, 3160, 0),
			Collections.emptyList(),
			Arrays.asList(
				TeleportOption.of(
					"Teleport crystal (Lletya)",
					Arrays.asList(
						new TravelStep(TELEPORT, "Use teleport crystal to Lletya.", new WorldPoint(2343, 3160, 0), TravelStep.TravelMethod.OTHER)
					),
					Arrays.asList(new ItemRequirement("Teleport crystal", ItemID.TELEPORT_CRYSTAL)),
					10,
					10,
					false
				)
			)
		);

		PatchLocation fruitCatherby = PatchLocation.of(
			FRUIT_CATHERBY,
			"Catherby (Fruit Tree)",
			PatchType.FRUIT_TREE,
			new WorldPoint(2858, 3432, 0),
			Collections.emptyList(),
			Arrays.asList(
				TeleportOption.of(
					"Camelot Teleport + walk",
					Arrays.asList(
						new TravelStep(TELEPORT, "Cast Camelot teleport (or use tablet).", new WorldPoint(2757, 3477, 0), TravelStep.TravelMethod.SPELLBOOK_OR_TABLET),
						new TravelStep(WALK, "Walk to Catherby fruit tree patch.", new WorldPoint(2858, 3432, 0), TravelStep.TravelMethod.WALK)
					),
					Arrays.asList(new ItemRequirement("Camelot teleport (tablet or runes)", ItemID.CAMELOT_TELEPORT)),
					20,
					0,
					true
				),
				TeleportOption.of(
					"Catherby Teleport + walk",
					Arrays.asList(
						new TravelStep(TELEPORT, "Cast Catherby teleport (Lunar).", new WorldPoint(2802, 3449, 0), TravelStep.TravelMethod.SPELLBOOK_OR_TABLET),
						new TravelStep(WALK, "Walk to Catherby fruit tree patch.", new WorldPoint(2858, 3432, 0), TravelStep.TravelMethod.WALK)
					),
					Arrays.asList(new ItemRequirement("Catherby teleport (Lunar runes/tablet)", ItemID.CATHERBY_TELEPORT)),
					10,
					10,
					false
				)
			)
		);

		PatchLocation fruitKastori = PatchLocation.of(
			FRUIT_KASTORI,
			"Kastori (Fruit Tree)",
			PatchType.FRUIT_TREE,
			new WorldPoint(1347, 3058, 0),
			Collections.emptyList(),
			Arrays.asList(
				TeleportOption.of(
					"Civitas illa Fortis Teleport + walk",
					Arrays.asList(
						new TravelStep(TELEPORT, "Cast Civitas illa Fortis teleport.", new WorldPoint(1681, 3133, 0), TravelStep.TravelMethod.SPELLBOOK_OR_TABLET),
						new TravelStep(WALK, "Travel/walk to Kastori fruit tree patch.", new WorldPoint(1347, 3058, 0), TravelStep.TravelMethod.WALK)
					),
					Arrays.asList(new ItemRequirement("Civitas illa Fortis teleport (tablet or runes)", ItemID.CIVITAS_ILLA_FORTIS_TELEPORT)),
					10,
					0,
					true
				)
			)
		);

		PatchLocation fruitBrimhaven = PatchLocation.of(
			FRUIT_BRIMHAVEN,
			"Brimhaven (Fruit Tree)",
			PatchType.FRUIT_TREE,
			new WorldPoint(2765, 3211, 0),
			Collections.emptyList(),
			Arrays.asList(
				TeleportOption.of(
					"Spirit Tree (Karamja) + walk",
					Arrays.asList(
						new TravelStep(SPIRIT_TREE, "Use Spirit Tree on Karamja.", new WorldPoint(2782, 3212, 0), TravelStep.TravelMethod.SPIRIT_TREE),
						new TravelStep(WALK, "Walk to Brimhaven fruit tree patch.", new WorldPoint(2765, 3211, 0), TravelStep.TravelMethod.WALK)
					),
					Collections.emptyList(),
					10,
					0,
					true
				),
				TeleportOption.of(
					"Karamja (Amulet of glory) + walk",
					Arrays.asList(
						new TravelStep(TELEPORT, "Teleport to Karamja (Amulet of glory).", new WorldPoint(2918, 3176, 0), TravelStep.TravelMethod.JEWELLERY),
						new TravelStep(WALK, "Travel/walk to Brimhaven fruit tree patch.", new WorldPoint(2765, 3211, 0), TravelStep.TravelMethod.WALK)
					),
					Arrays.asList(new AnyOfItemRequirement("Amulet of glory", 1,
						ItemID.AMULET_OF_GLORY,
						ItemID.AMULET_OF_GLORY1, ItemID.AMULET_OF_GLORY2, ItemID.AMULET_OF_GLORY3, ItemID.AMULET_OF_GLORY4,
						ItemID.AMULET_OF_GLORY5, ItemID.AMULET_OF_GLORY6,
						ItemID.AMULET_OF_GLORY_T,
						ItemID.AMULET_OF_GLORY_T1, ItemID.AMULET_OF_GLORY_T2, ItemID.AMULET_OF_GLORY_T3, ItemID.AMULET_OF_GLORY_T4,
						ItemID.AMULET_OF_GLORY_T5, ItemID.AMULET_OF_GLORY_T6
					)),
					20,
					10,
					false
				)
			)
		);

		return Arrays.asList(
			treeVarrock,
			treeLumbridge,
			treeGnomeStronghold,
			treeFaladorPark,
			treeTaverley,
			treeVarlamore,
			fruitGnomeStronghold,
			fruitGnomeVillage,
			fruitLletya,
			fruitCatherby,
			fruitKastori,
			fruitBrimhaven
		);
	}

	public static Map<String, PatchLocation> coreById()
	{
		return coreLocations().stream().collect(Collectors.toMap(PatchLocation::getId, l -> l));
	}
}
