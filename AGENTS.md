# Braindead Farming (RuneLite Plugin)

## Purpose

Provide a brain-dead simple guided OSRS farming run for **tree + fruit tree** patches: pick what to plant, pick which patches to do, then follow a single step-by-step route with a bank checklist.

The plugin descriptor still says “farming and birdhouse runs”, but the current code only implements the farming/tree-run MVP. Do not assume birdhouse support exists unless you are adding it deliberately.

## Current Product Shape

This repo is a **v0.1 MVP skeleton**. It currently has:

- A RuneLite sidebar panel with four buttons: `Plan route`, `Start run`, `Next step`, `Stop`.
- Configurable seed choices, patch toggles, compost quantity, overlay visibility, and travel-method toggles.
- A static dataset of 12 core locations: 6 tree patches and 6 fruit tree patches.
- A simple inventory availability tracker for bank, seed vault, inventory, and equipment containers.
- A bank checklist for saplings, spade, seed dibber, compost buckets, and selected teleport items.
- A bank-first sync flow: when a route is planned before bank/seed vault data has been seen, the sidebar directs the player to the closest known bank and keeps patch instructions hidden until sync.
- A route planner that filters configured locations and allowed travel methods, prefers user-hint teleports, checks item requirements, then applies a small adjacent-swap improvement pass using tile distance.
- A manual run-state manager with no automatic patch completion detection.
- Scene and bank overlays:
  - Current patch tile highlight: green when teleport requirements are met, red otherwise.
  - Required bank item outline: green/amber/red based on checklist availability.
- Best-effort Shortest Path integration via RuneLite plugin messages:
  - In `NEEDS_BANK_SYNC`, targets the closest known bank and retargets as the closest bank changes while the player moves.
  - After bank sync, targets the current route stop on game ticks.

## Planning Documents

- Current player-facing behavior is tracked in `features.md`; keep it aligned when adding, removing, or materially changing implemented functionality.
- Deferred or partially implemented work is tracked in `plans/todo.md`; consult it before expanding scope, and update it when follow-up items are completed or re-scoped.
- Save all future implementation plans, exploratory plans, and source-plan notes under the gitignored `plans/` directory.
- Source-plan context may exist in `plans/noble-fluttering-bachman.updated.md`, but `features.md` and `plans/todo.md` are the active project-status references.

## Near-Term Vision

- Optimize tree-run planning around **what the player has** in bank / seed vault / inventory / equipment.
- Account for **which travel methods the player can use**.
- Present **one linear route** with clear manual progression.
- Hand off navigation to Shortest Path when available.
- Highlight destinations in-world and required items in-bank.

## How The MVP Is Supposed To Work

1. Configure seeds, enabled locations, compost, and allowed travel methods in RuneLite settings.
2. Open the plugin panel and press `Plan route`; this creates a farming route and initializes run state.
3. If bank/seed vault has not been seen this session, follow the sidebar and Shortest Path target to the closest known bank.
4. Open bank or seed vault once so bank sync completes and checklist counts become meaningful.
5. Inventory and equipment updates still improve item counts, but do not satisfy bank sync by themselves.
6. Withdraw or prepare missing items.
7. Follow the current stop’s travel steps.
8. Press `Next step` manually after each patch.
9. If Shortest Path is installed and compatible, it should receive the current bank or patch target automatically.

## Technical Stack

- Java 11.
- Gradle wrapper uses Gradle 8.8.
- RuneLite dependency is `latest.release`.
- Lombok is used for boilerplate reduction.
- JUnit 4 tests live under `src/test/java`.
- RuneLite client dependencies are `compileOnly`; the client provides them at runtime.

## Build And Run

- Run tests: `./gradlew test`
- Run dev client: `./gradlew run`
- Full build: `./gradlew build`

The custom `run` task launches RuneLite in developer mode using `BraindeadFarmingPluginTest.main()`, with `pluginMainClass` configured in `build.gradle`.

## Primary Entry Points

- Plugin lifecycle, toolbar panel, overlays, item-container events, and Shortest Path target updates: `src/main/java/com/braindeadfarming/BraindeadFarmingPlugin.java`
- Config group `braindeadfarming`: `src/main/java/com/braindeadfarming/BraindeadFarmingConfig.java`
- Sidebar UI text, checklist rendering, current stop display, and button actions: `src/main/java/com/braindeadfarming/ui/BraindeadFarmingPanel.java`
- Dev-client launcher: `src/test/java/com/braindeadfarming/BraindeadFarmingPluginTest.java`
- Plugin-hub metadata: `src/main/resources/runelite-plugin.properties`

## Core Modules

- **Data models and static dataset**
  - `src/main/java/com/braindeadfarming/data/`
  - Core locations live in `FarmingLocations.java`.
  - Travel options are represented by `TeleportOption` and `TravelStep`.
- **Requirements**
  - `src/main/java/com/braindeadfarming/requirements/`
  - Current requirement evaluation is mostly item-based.
  - Skill, quest, diary, and varbit access modeling is deferred.
- **Inventory tracking**
  - `src/main/java/com/braindeadfarming/bank/BankInventoryTracker.java`
  - Static bank target selection: `BankLocations.java` / `BankLocation.java`.
  - Snapshot type: `ItemAvailabilitySnapshot`.
  - Checklist calculation: `InventoryRequirements`.
- **Routing**
  - `src/main/java/com/braindeadfarming/route/RoutePlanner.java`
  - Output route types: `FarmingRoute`, `RouteStop`.
- **Run state**
  - `src/main/java/com/braindeadfarming/state/RunStateManager.java`
  - States are manual and intentionally simple.
- **Navigation**
  - `src/main/java/com/braindeadfarming/navigation/ShortestPathIntegration.java`
  - Integration is best-effort and uses RuneLite `PluginMessage` events for the external Shortest Path plugin.
- **Overlays**
  - `src/main/java/com/braindeadfarming/overlay/FarmingRunOverlay.java`
  - `src/main/java/com/braindeadfarming/overlay/BankHighlightOverlay.java`
## Coding Conventions

- Follow existing RuneLite-style Java formatting; opening braces for classes and methods go on their own line.
- Keep changes focused on the requested scope. This codebase has been over-scoped, so prefer tightening the MVP over adding broad new systems.
- Add new RuneLite settings as `@ConfigItem` methods in `BraindeadFarmingConfig`.
- Keep plugin metadata in `src/main/resources/runelite-plugin.properties` aligned when renaming classes or changing public plugin description/tags.
- Do not treat config labels or README-style claims as implemented behavior; verify against code first.
- Finish feature work by updating the project-status docs:
  - Update `features.md` for any player-facing behavior change, new feature, removed feature, or materially changed test coverage.
  - Update `plans/todo.md` when deferred work is completed, re-scoped, or newly discovered.
  - Update `AGENTS.md` when the change affects architecture, core workflow, contributor guidance, primary entry points, known gaps, or anything future agents must know before editing.

## Commit Message Conventions

- When creating commits, use Conventional Commit-style prefixes so history stays readable and automation-friendly.
- Prefer these common prefixes:
  - `feat`: player-facing feature or new behavior.
  - `fix`: bug fix or corrected behavior.
  - `chore`: maintenance, dependencies, build scripts, or housekeeping that does not change runtime behavior.
  - `style`: formatting-only changes with no logic change.
  - `refactor`: code restructuring without intended behavior changes.
  - `docs`: documentation-only changes.
  - `perf`: performance improvements.
  - `test`: adding or updating tests.
  - `ci`: CI/CD workflow or configuration changes.
- Write the subject in imperative mood, for example `fix: handle missing bank snapshot`.
- Keep the subject concise and clear, aiming for roughly 50-72 characters when practical.
- Add a commit body when the change needs context, especially for behavior changes, tradeoffs, migrations, or follow-up work.

## Known Gaps

- No automatic “arrived at patch” or “completed patch” detection.
- No patch-state detection, disease/dead/tree-grown checks, or farming action automation.
- Requirements do not yet fully model quests, diaries, fairy ring unlocks, spirit tree unlocks, POH portal setup, spellbook/rune availability, or other nuanced access rules.
- Some teleport requirements are represented as broad any-of item checks and may need refinement.
- Shortest Path integration may fail if the external plugin changes its plugin-message contract.
- Route scoring is simplistic and is not a full TSP/optimal solver.
- Bank checklist depends on item containers being seen at least once during the session.
- Closest-bank routing uses a small static list of known bank tiles and is not teleport-aware.
