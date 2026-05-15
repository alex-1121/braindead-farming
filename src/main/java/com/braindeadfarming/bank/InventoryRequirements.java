package com.braindeadfarming.bank;

import com.braindeadfarming.BraindeadFarmingConfig;
import com.braindeadfarming.data.PatchLocation;
import com.braindeadfarming.data.PatchType;
import com.braindeadfarming.data.SeedData;
import com.braindeadfarming.requirements.AnyOfItemRequirement;
import com.braindeadfarming.requirements.ItemRequirement;
import com.braindeadfarming.requirements.Requirement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Value;
import net.runelite.api.ItemID;

public final class InventoryRequirements
{
	private InventoryRequirements()
	{
	}

	@Value
	public static class RequiredItem
	{
		String label;
		int displayItemId;
		int[] itemIds;
		int quantity;

		public int have(ItemAvailabilitySnapshot snapshot)
		{
			if (snapshot == null || itemIds == null || itemIds.length == 0)
			{
				return 0;
			}

			int total = 0;
			for (int id : itemIds)
			{
				total += snapshot.getQuantity(id);
			}
			return total;
		}
	}

	public static List<RequiredItem> computeRequiredItems(BraindeadFarmingConfig config, List<PatchLocation> routeLocations, List<Requirement> teleportRequirements)
	{
		Map<Integer, RequiredItem> merged = new HashMap<>();

		int treeCount = (int) routeLocations.stream().filter(l -> l.getPatchType() == PatchType.TREE).count();
		int fruitCount = (int) routeLocations.stream().filter(l -> l.getPatchType() == PatchType.FRUIT_TREE).count();

		SeedData.TreeSeed treeSeed = config.treeSeed();
		if (treeCount > 0 && treeSeed != null)
		{
			addSingle(merged, "Tree sapling (" + treeSeed.getDisplayName() + ")", treeSeed.getSaplingItemId(), treeCount);
		}

		SeedData.FruitTreeSeed fruitSeed = config.fruitTreeSeed();
		if (fruitCount > 0 && fruitSeed != null)
		{
			addSingle(merged, "Fruit tree sapling (" + fruitSeed.getDisplayName() + ")", fruitSeed.getSaplingItemId(), fruitCount);
		}

		addSingle(merged, "Spade", ItemID.SPADE, 1);
		addSingle(merged, "Seed dibber", ItemID.SEED_DIBBER, 1);

		if (config.useCompost() && config.compostQuantityPerPatch() > 0)
		{
			int compostCount = (treeCount + fruitCount) * config.compostQuantityPerPatch();
			addSingle(merged, "Compost (bucket)", net.runelite.api.gameval.ItemID.BUCKET_COMPOST, compostCount);
		}

		// Add teleport items (best-effort): collect all item requirements, merge by item id.
		if (teleportRequirements != null)
		{
			for (Requirement req : teleportRequirements)
			{
				if (req instanceof AnyOfItemRequirement)
				{
					AnyOfItemRequirement ar = (AnyOfItemRequirement) req;
					addAny(merged, ar.getLabel(), ar.getQuantity(), ar.getItemIds());
					continue;
				}

				if (!(req instanceof ItemRequirement))
				{
					continue;
				}

				ItemRequirement ir = (ItemRequirement) req;
				addAny(merged, ir.getLabel(), ir.getQuantity(), ir.getItemId());
			}
		}

		return new ArrayList<>(merged.values());
	}

	private static void addSingle(Map<Integer, RequiredItem> merged, String label, int itemId, int quantity)
	{
		addAny(merged, label, quantity, itemId);
	}

	private static void addAny(Map<Integer, RequiredItem> merged, String label, int quantity, int... itemIds)
	{
		if (itemIds == null || itemIds.length == 0 || quantity <= 0)
		{
			return;
		}

		int displayItemId = itemIds[0];
		RequiredItem existing = merged.get(displayItemId);
		if (existing == null)
		{
			merged.put(displayItemId, new RequiredItem(label, displayItemId, itemIds, quantity));
			return;
		}

		merged.put(displayItemId, new RequiredItem(existing.getLabel(), displayItemId, existing.getItemIds(), existing.getQuantity() + quantity));
	}
}
