package com.braindeadfarming.route;

import com.braindeadfarming.bank.InventoryRequirements;
import java.util.Collections;
import java.util.List;
import lombok.Value;
import net.runelite.api.coords.WorldPoint;

@Value
public class FarmingRoute
{
	List<RouteStop> stops;
	List<InventoryRequirements.RequiredItem> requiredItems;
	WorldPoint startPoint;

	public static FarmingRoute empty()
	{
		return new FarmingRoute(Collections.emptyList(), Collections.emptyList(), null);
	}
}

