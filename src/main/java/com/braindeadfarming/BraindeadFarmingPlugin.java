package com.braindeadfarming;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
@PluginDescriptor(
    name = "Braindead Farming",
    description = "No more need to plan your farming and birdhouse runs. Turn off your brain and just follow the instructions.",
    tags = {"farming", "farm", "birdhouse", "runs", "osrs"}
)
public class BraindeadFarmingPlugin extends Plugin
{
    @Inject
    private BraindeadFarmingConfig config;

    @Override
    protected void startUp()
    {
        log.info("Braindead farming started");
    }

    @Override
    protected void shutDown()
    {
        log.info("Braindead farming stopped");
    }

    @Subscribe
    public void onGameTick(GameTick gameTick)
    {
        if (!config.showOverlay())
        {
            return;
        }

        // Starter hook for your farming automation helpers.
    }

    @Provides
    BraindeadFarmingConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(BraindeadFarmingConfig.class);
    }
}
