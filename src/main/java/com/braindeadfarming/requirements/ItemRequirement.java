package com.braindeadfarming.requirements;

import com.braindeadfarming.bank.ItemAvailabilitySnapshot;
import lombok.Getter;

@Getter
public class ItemRequirement implements Requirement
{
	private final String label;
	private final int itemId;
	private final int quantity;

	public ItemRequirement(String label, int itemId)
	{
		this(label, itemId, 1);
	}

	public ItemRequirement(String label, int itemId, int quantity)
	{
		this.label = label;
		this.itemId = itemId;
		this.quantity = quantity;
	}

	@Override
	public boolean isMet(RequirementContext ctx)
	{
		if (ctx == null)
		{
			return false;
		}

		ItemAvailabilitySnapshot items = ctx.getItems();
		if (items == null)
		{
			return false;
		}

		return items.getQuantity(itemId) >= quantity;
	}

	@Override
	public String describe()
	{
		String qty = quantity <= 1 ? "" : (" x" + quantity);
		return (label == null || label.isEmpty()) ? ("Item " + itemId + qty) : (label + qty);
	}
}

