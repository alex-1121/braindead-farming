package com.braindeadfarming.requirements;

import com.braindeadfarming.bank.ItemAvailabilitySnapshot;
import lombok.Value;
import net.runelite.api.Client;

@Value
public class RequirementContext
{
	Client client;
	ItemAvailabilitySnapshot items;
}

