package com.braindeadfarming.bank;

import java.util.Collections;
import java.util.Map;
import lombok.Value;

@Value
public class ItemAvailabilitySnapshot
{
	Map<Integer, Integer> quantities;

	public static ItemAvailabilitySnapshot empty()
	{
		return new ItemAvailabilitySnapshot(Collections.emptyMap());
	}

	public int getQuantity(int itemId)
	{
		if (quantities == null)
		{
			return 0;
		}

		return quantities.getOrDefault(itemId, 0);
	}
}
