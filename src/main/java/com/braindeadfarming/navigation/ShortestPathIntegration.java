package com.braindeadfarming.navigation;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.events.PluginMessage;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginManager;

@Singleton
public class ShortestPathIntegration
{
	private static final String SHORTEST_PATH_PLUGIN_CLASS = "shortestpath.ShortestPathPlugin";
	private static final String LEGACY_SHORTEST_PATH_PLUGIN_CLASS = "net.runelite.client.plugins.shortestpath.ShortestPathPlugin";
	private static final String PLUGIN_MESSAGE_NAMESPACE = "shortestpath";
	private static final String PLUGIN_MESSAGE_PATH = "path";
	private static final String PLUGIN_MESSAGE_CLEAR = "clear";
	private static final String PLUGIN_MESSAGE_TARGET = "target";

	private final PluginManager pluginManager;
	private final EventBus eventBus;

	@Inject
	public ShortestPathIntegration(PluginManager pluginManager, EventBus eventBus)
	{
		this.pluginManager = pluginManager;
		this.eventBus = eventBus;
	}

	public boolean isAvailable()
	{
		return findShortestPathPlugin() != null;
	}

	public void setTarget(WorldPoint target)
	{
		if (!isAvailable() || target == null)
		{
			return;
		}

		Map<String, Object> data = new HashMap<>();
		data.put(PLUGIN_MESSAGE_TARGET, target);
		eventBus.post(new PluginMessage(PLUGIN_MESSAGE_NAMESPACE, PLUGIN_MESSAGE_PATH, data));
	}

	public void clearTarget()
	{
		if (!isAvailable())
		{
			return;
		}

		eventBus.post(new PluginMessage(PLUGIN_MESSAGE_NAMESPACE, PLUGIN_MESSAGE_CLEAR, Collections.emptyMap()));
	}

	private Plugin findShortestPathPlugin()
	{
		for (Plugin plugin : pluginManager.getPlugins())
		{
			if (plugin == null)
			{
				continue;
			}

			String className = plugin.getClass().getName();
			if (className.equals(SHORTEST_PATH_PLUGIN_CLASS) || className.equals(LEGACY_SHORTEST_PATH_PLUGIN_CLASS))
			{
				return plugin;
			}
		}
		return null;
	}
}
