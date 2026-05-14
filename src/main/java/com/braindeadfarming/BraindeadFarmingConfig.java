package com.braindeadfarming;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("braindeadfarming")
public interface BraindeadFarmingConfig extends Config
{
    @ConfigItem(
        keyName = "showOverlay",
        name = "Show overlay",
        description = "Shows a simple starter overlay placeholder"
    )
    default boolean showOverlay()
    {
        return true;
    }
}
