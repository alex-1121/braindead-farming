package com.braindeadfarming.route;

import com.braindeadfarming.data.PatchLocation;
import com.braindeadfarming.data.TeleportOption;
import java.util.Collections;
import java.util.List;
import lombok.Value;

@Value
public class RouteStop
{
	PatchLocation location;
	TeleportOption teleport;
	boolean teleportRequirementsMet;
	List<String> unmetTeleportRequirements;

	public static RouteStop of(PatchLocation location, TeleportOption teleport, boolean teleportRequirementsMet, List<String> unmetTeleportRequirements)
	{
		return new RouteStop(
			location,
			teleport,
			teleportRequirementsMet,
			unmetTeleportRequirements == null ? Collections.emptyList() : unmetTeleportRequirements
		);
	}
}

