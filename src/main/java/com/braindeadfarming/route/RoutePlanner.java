package com.braindeadfarming.route;

import com.braindeadfarming.BraindeadFarmingConfig;
import com.braindeadfarming.bank.InventoryRequirements;
import com.braindeadfarming.bank.ItemAvailabilitySnapshot;
import com.braindeadfarming.data.FarmingLocations;
import com.braindeadfarming.data.PatchLocation;
import com.braindeadfarming.data.TeleportOption;
import com.braindeadfarming.data.TravelStep;
import com.braindeadfarming.requirements.Requirement;
import com.braindeadfarming.requirements.RequirementContext;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.coords.WorldPoint;

@Singleton
public class RoutePlanner
{
	@Inject
	public RoutePlanner()
	{
	}

	public FarmingRoute plan(BraindeadFarmingConfig config, ItemAvailabilitySnapshot items)
	{
		if (config == null)
		{
			return FarmingRoute.empty();
		}

		List<PatchLocation> selected = FarmingLocations.coreLocations().stream()
			.filter(l -> isLocationEnabled(config, l.getId()))
			.collect(Collectors.toList());

		RequirementContext ctx = new RequirementContext(null, items == null ? ItemAvailabilitySnapshot.empty() : items);

		List<RouteStop> stops = new ArrayList<>();
		List<Requirement> chosenTeleportRequirements = new ArrayList<>();

		for (PatchLocation location : selected)
		{
			TeleportPick pick = pickTeleport(config, location, ctx);
			stops.add(RouteStop.of(location, pick.teleport, pick.requirementsMet, pick.unmetDescriptions));
			if (pick.teleport != null)
			{
				chosenTeleportRequirements.addAll(pick.teleport.getAccessRequirements());
			}
		}

		stops.sort(Comparator
			.comparingInt((RouteStop s) -> bestHintPriority(s.getTeleport()))
			.thenComparing(s -> s.getLocation().getName()));

		localImprove(stops);

		List<PatchLocation> routeLocations = stops.stream().map(RouteStop::getLocation).collect(Collectors.toList());
		List<InventoryRequirements.RequiredItem> requiredItems = InventoryRequirements.computeRequiredItems(config, routeLocations, dedupe(chosenTeleportRequirements));

		return new FarmingRoute(stops, requiredItems, null);
	}

	private static int bestHintPriority(TeleportOption teleport)
	{
		if (teleport == null)
		{
			return 9999;
		}

		return teleport.isUserHint() ? teleport.getHintPriority() : (100 + teleport.getHintPriority());
	}

	private void localImprove(List<RouteStop> stops)
	{
		if (stops.size() < 3)
		{
			return;
		}

		boolean improved = true;
		int iterations = 0;
		while (improved && iterations++ < 5)
		{
			improved = false;
			for (int i = 0; i < stops.size() - 1; i++)
			{
				double current = adjacencyCost(stops.get(i), stops.get(i + 1));
				double swapped = adjacencyCost(stops.get(i + 1), stops.get(i));
				if (swapped + 1e-6 < current)
				{
					RouteStop a = stops.get(i);
					stops.set(i, stops.get(i + 1));
					stops.set(i + 1, a);
					improved = true;
				}
			}
		}
	}

	private double adjacencyCost(RouteStop a, RouteStop b)
	{
		WorldPoint awp = a.getLocation().getWorldPoint();
		WorldPoint bwp = b.getLocation().getWorldPoint();
		if (awp == null || bwp == null)
		{
			return 0;
		}
		return awp.distanceTo2D(bwp);
	}

	private TeleportPick pickTeleport(BraindeadFarmingConfig config, PatchLocation location, RequirementContext ctx)
	{
		List<TeleportOption> candidates = location.getTeleports().stream()
			.filter(t -> isTeleportAllowed(config, t))
			.collect(Collectors.toList());

	 TeleportOption bestMet = null;
	 double bestScoreMet = Double.POSITIVE_INFINITY;
	 List<String> bestUnmet = new ArrayList<>();
	 boolean bestMetOk = false;

	 TeleportOption bestAny = null;
	 double bestScoreAny = Double.POSITIVE_INFINITY;
	 List<String> bestAnyUnmet = new ArrayList<>();
	 boolean bestAnyOk = false;

	 for (TeleportOption option : candidates)
	 {
		 List<String> unmet = unmetRequirements(option.getAccessRequirements(), ctx);
		 boolean met = unmet.isEmpty();
		 double score = teleportScore(location, option, met);
		 if (met && score < bestScoreMet)
		 {
			 bestMet = option;
			 bestScoreMet = score;
			 bestUnmet = unmet;
			 bestMetOk = true;
		 }
		 if (score < bestScoreAny)
		 {
			 bestAny = option;
			 bestScoreAny = score;
			 bestAnyUnmet = unmet;
			 bestAnyOk = met;
		 }
	 }

	 if (bestMet != null)
	 {
		 return new TeleportPick(bestMet, bestMetOk, bestUnmet);
	 }

	 return new TeleportPick(bestAny, bestAnyOk, bestAnyUnmet);
	}

	private double teleportScore(PatchLocation location, TeleportOption option, boolean requirementsMet)
	{
		if (option == null)
		{
			return Double.POSITIVE_INFINITY;
		}

		double score = 0;
		score += option.getPriority();
		if (option.isUserHint())
		{
			score -= 5 + option.getHintPriority();
		}

		WorldPoint patch = location.getWorldPoint();
		WorldPoint arrival = firstArrival(option);
		if (patch != null && arrival != null)
		{
			score += arrival.distanceTo2D(patch) / 10.0;
		}

		if (!requirementsMet)
		{
			score += 10_000;
		}

		return score;
	}

	private WorldPoint firstArrival(TeleportOption option)
	{
		if (option.getSteps().isEmpty())
		{
			return null;
		}

		for (TravelStep step : option.getSteps())
		{
			if (step.getType() == TravelStep.TravelStepType.WALK)
			{
				continue;
			}
			return step.getTarget();
		}

		return option.getSteps().get(0).getTarget();
	}

	private static List<String> unmetRequirements(List<Requirement> requirements, RequirementContext ctx)
	{
		if (requirements == null || requirements.isEmpty())
		{
			return new ArrayList<>();
		}

		List<String> unmet = new ArrayList<>();
		for (Requirement req : requirements)
		{
			if (req == null)
			{
				continue;
			}

			if (!req.isMet(ctx))
			{
				unmet.add(req.describe());
			}
		}
		return unmet;
	}

	private boolean isTeleportAllowed(BraindeadFarmingConfig config, TeleportOption option)
	{
		for (TravelStep step : option.getSteps())
		{
			switch (step.getMethod())
			{
				case FAIRY_RING:
					if (!config.allowFairyRings())
					{
						return false;
					}
					break;
				case SPIRIT_TREE:
					if (!config.allowSpiritTrees())
					{
						return false;
					}
					break;
				case SPELLBOOK_OR_TABLET:
					if (!config.allowStandardTeleports())
					{
						return false;
					}
					break;
				case JEWELLERY:
					if (!config.allowJewelleryTeleports())
					{
						return false;
					}
					break;
				default:
					break;
			}
		}

		return true;
	}

	private boolean isLocationEnabled(BraindeadFarmingConfig config, String id)
	{
		switch (id)
		{
			case FarmingLocations.TREE_VARROCK:
				return config.includeTreeVarrock();
			case FarmingLocations.TREE_LUMBRIDGE:
				return config.includeTreeLumbridge();
			case FarmingLocations.TREE_GNOME_STRONGHOLD:
				return config.includeTreeGnomeStronghold();
			case FarmingLocations.TREE_FALADOR_PARK:
				return config.includeTreeFaladorPark();
			case FarmingLocations.TREE_TAVERLEY:
				return config.includeTreeTaverley();
			case FarmingLocations.TREE_VARLAMORE:
				return config.includeTreeVarlamore();
			case FarmingLocations.FRUIT_GNOME_STRONGHOLD:
				return config.includeFruitGnomeStronghold();
			case FarmingLocations.FRUIT_TREE_GNOME_VILLAGE:
				return config.includeFruitGnomeVillage();
			case FarmingLocations.FRUIT_LLETYA:
				return config.includeFruitLletya();
			case FarmingLocations.FRUIT_CATHERBY:
				return config.includeFruitCatherby();
			case FarmingLocations.FRUIT_KASTORI:
				return config.includeFruitKastori();
			case FarmingLocations.FRUIT_BRIMHAVEN:
				return config.includeFruitBrimhaven();
			default:
				return true;
		}
	}

	private static List<Requirement> dedupe(List<Requirement> requirements)
	{
		if (requirements == null || requirements.isEmpty())
		{
			return requirements;
		}

		Set<String> seen = new HashSet<>();
		List<Requirement> out = new ArrayList<>();
		for (Requirement req : requirements)
		{
			if (req == null)
			{
				continue;
			}

			String key = req.describe();
			if (seen.add(key))
			{
				out.add(req);
			}
		}
		return out;
	}

	private static class TeleportPick
	{
		final TeleportOption teleport;
		final boolean requirementsMet;
		final List<String> unmetDescriptions;

		TeleportPick(TeleportOption teleport, boolean requirementsMet, List<String> unmetDescriptions)
		{
			this.teleport = teleport;
			this.requirementsMet = requirementsMet;
			this.unmetDescriptions = unmetDescriptions;
		}
	}
}
