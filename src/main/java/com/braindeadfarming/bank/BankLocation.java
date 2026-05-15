package com.braindeadfarming.bank;

import lombok.Value;
import net.runelite.api.coords.WorldPoint;

@Value
public class BankLocation
{
	String name;
	WorldPoint worldPoint;
}
