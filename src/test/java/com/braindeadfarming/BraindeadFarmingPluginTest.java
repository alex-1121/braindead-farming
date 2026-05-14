package com.braindeadfarming;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class BraindeadFarmingPluginTest
{
    public static void main(String[] args) throws Exception
    {
        ExternalPluginManager.loadBuiltin(BraindeadFarmingPlugin.class);
        RuneLite.main(args);
    }
}
