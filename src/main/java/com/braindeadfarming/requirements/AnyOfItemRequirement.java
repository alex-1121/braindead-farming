package com.braindeadfarming.requirements;

import com.braindeadfarming.bank.ItemAvailabilitySnapshot;
import java.util.Arrays;
import lombok.Getter;

@Getter
public class AnyOfItemRequirement implements Requirement
{
	private final String label;
	private final int[] itemIds;
	private final int quantity;

	public AnyOfItemRequirement(String label, int quantity, int... itemIds)
	{
		this.label = label;
		this.quantity = quantity;
		this.itemIds = itemIds == null ? new int[0] : itemIds;
	}

	@Override
	public boolean isMet(RequirementContext ctx)
	{
		if (ctx == null || ctx.getItems() == null)
		{
			return false;
		}

		ItemAvailabilitySnapshot items = ctx.getItems();
		int total = 0;
		for (int id : itemIds)
		{
			total += items.getQuantity(id);
			if (total >= quantity)
			{
				return true;
			}
		}

		return false;
	}

	@Override
	public String describe()
	{
		String qty = quantity <= 1 ? "" : (" x" + quantity);
		return (label == null || label.isEmpty())
			? ("Any of " + Arrays.toString(itemIds) + qty)
			: (label + qty);
	}
}
