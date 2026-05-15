package com.braindeadfarming.requirements;

public interface Requirement
{
	boolean isMet(RequirementContext ctx);

	String describe();
}

