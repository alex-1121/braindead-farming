package com.braindeadfarming.data;

import com.braindeadfarming.requirements.Requirement;
import java.util.Collections;
import java.util.List;
import lombok.Value;

@Value
public class TeleportOption
{
	String name;
	List<TravelStep> steps;
	List<Requirement> accessRequirements;
	int priority;
	int hintPriority;
	boolean userHint;

	public static TeleportOption of(String name, List<TravelStep> steps, List<Requirement> accessRequirements, int priority, int hintPriority, boolean userHint)
	{
		return new TeleportOption(
			name,
			steps == null ? Collections.emptyList() : steps,
			accessRequirements == null ? Collections.emptyList() : accessRequirements,
			priority,
			hintPriority,
			userHint
		);
	}
}

