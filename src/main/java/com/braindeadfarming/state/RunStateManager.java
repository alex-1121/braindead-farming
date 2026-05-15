package com.braindeadfarming.state;

import com.braindeadfarming.route.FarmingRoute;
import com.braindeadfarming.route.RouteStop;
import javax.inject.Singleton;
import lombok.Getter;

@Singleton
public class RunStateManager
{
	@Getter
	private RunState state = RunState.IDLE;

	@Getter
	private FarmingRoute route = FarmingRoute.empty();

	@Getter
	private int routeIndex = -1;

	@Getter
	private boolean bankSynced = false;

	public void reset()
	{
		state = RunState.IDLE;
		route = FarmingRoute.empty();
		routeIndex = -1;
		bankSynced = false;
	}

	public void setBankSynced(boolean synced)
	{
		this.bankSynced = synced;
		if (synced && state == RunState.NEEDS_BANK_SYNC)
		{
			state = RunState.AT_BANK;
		}
	}

	public void start(FarmingRoute newRoute)
	{
		if (newRoute == null || newRoute.getStops().isEmpty())
		{
			route = FarmingRoute.empty();
			routeIndex = -1;
			state = RunState.ROUTE_COMPLETE;
			return;
		}

		this.route = newRoute;
		this.routeIndex = 0;
		this.state = bankSynced ? RunState.AT_BANK : RunState.NEEDS_BANK_SYNC;
	}

	public void stop()
	{
		reset();
	}

	public RouteStop currentStop()
	{
		if (route == null || route.getStops() == null)
		{
			return null;
		}

		if (routeIndex < 0 || routeIndex >= route.getStops().size())
		{
			return null;
		}

		return route.getStops().get(routeIndex);
	}

	public void next()
	{
		if (route == null || route.getStops() == null || route.getStops().isEmpty())
		{
			state = RunState.ROUTE_COMPLETE;
			return;
		}

		if (routeIndex < 0)
		{
			routeIndex = 0;
		}
		else
		{
			routeIndex++;
		}

		if (routeIndex >= route.getStops().size())
		{
			routeIndex = route.getStops().size();
			state = RunState.ROUTE_COMPLETE;
		}
		else
		{
			state = RunState.TRAVELING;
		}
	}
}
