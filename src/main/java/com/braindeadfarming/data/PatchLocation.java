package com.braindeadfarming.data;

import com.braindeadfarming.requirements.Requirement;
import java.util.Collections;
import java.util.List;
import lombok.Value;
import net.runelite.api.coords.WorldPoint;

@Value
public class PatchLocation
{
	String id;
	String name;
	PatchType patchType;
	WorldPoint worldPoint;
	List<Requirement> accessRequirements;
	List<TeleportOption> teleports;

	public static PatchLocation of(
		String id,
		String name,
		PatchType patchType,
		WorldPoint worldPoint,
		List<Requirement> accessRequirements,
		List<TeleportOption> teleports)
	{
		return new PatchLocation(
			id,
			name,
			patchType,
			worldPoint,
			accessRequirements == null ? Collections.emptyList() : accessRequirements,
			teleports == null ? Collections.emptyList() : teleports
		);
	}
}

