package com.braindeadfarming.overlay;

import com.braindeadfarming.route.RouteStop;
import com.braindeadfarming.BraindeadFarmingConfig;
import com.braindeadfarming.state.RunStateManager;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.OverlayUtil;

@Singleton
public class FarmingRunOverlay extends Overlay
{
	private final Client client;
	private final RunStateManager runStateManager;
	private final BraindeadFarmingConfig config;

	@Inject
	public FarmingRunOverlay(Client client, RunStateManager runStateManager, BraindeadFarmingConfig config)
	{
		this.client = client;
		this.runStateManager = runStateManager;
		this.config = config;
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_SCENE);
		setPriority(OverlayPriority.HIGH);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (!config.showOverlay())
		{
			return null;
		}

		RouteStop stop = runStateManager.currentStop();
		if (stop == null)
		{
			return null;
		}

		WorldPoint wp = stop.getLocation().getWorldPoint();
		if (wp == null)
		{
			return null;
		}

		LocalPoint lp = LocalPoint.fromWorld(client, wp);
		if (lp == null)
		{
			return null;
		}

		Polygon poly = Perspective.getCanvasTilePoly(client, lp);
		if (poly == null)
		{
			return null;
		}

		Color color = stop.isTeleportRequirementsMet() ? new Color(0, 255, 0, 80) : new Color(255, 0, 0, 80);
		OverlayUtil.renderPolygon(graphics, poly, color);
		return null;
	}
}
