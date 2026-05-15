package com.braindeadfarming.data;

import lombok.Value;
import net.runelite.api.coords.WorldPoint;

@Value
public class TravelStep
{
	TravelStepType type;
	String instruction;
	WorldPoint target;
	TravelMethod method;

	public enum TravelStepType
	{
		TELEPORT,
		FAIRY_RING,
		SPIRIT_TREE,
		WALK
	}

	public enum TravelMethod
	{
		SPELLBOOK_OR_TABLET,
		JEWELLERY,
		FAIRY_RING,
		SPIRIT_TREE,
		WALK,
		OTHER
	}
}
