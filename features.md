# Features implemented so far (player-facing)

This file documents **all user-visible features currently present in code**, regardless of whether they’ve been thoroughly play-tested.

## 1) Plugin basics
- Plugin name: **Braindead Farming**
- Sidebar entry is registered via a navigation button with an icon: `src/main/resources/icon.png`
- Panel is reachable from the RuneLite sidebar: `src/main/java/com/braindeadfarming/BraindeadFarmingPlugin.java`

## 2) Settings / configuration (RuneLite config UI)
File: `src/main/java/com/braindeadfarming/BraindeadFarmingConfig.java`

### Seeds
- Tree sapling selection (enum-backed): `treeSeed()`
- Fruit tree sapling selection (enum-backed): `fruitTreeSeed()`
- Compost toggle: `useCompost()`
- Compost quantity per patch: `compostQuantityPerPatch()`

### Location include/exclude toggles (12 core patches)
Each of the following can be enabled/disabled:
- Trees: Varrock, Lumbridge, Gnome Stronghold, Falador Park, Taverley, Varlamore
- Fruit trees: Gnome Stronghold, Gnome Village, Lletya, Catherby, Kastori, Brimhaven

### Teleport capability toggles
These toggles gate which teleport candidates the planner is allowed to choose:
- Standard spellbook/tablet teleports: `allowStandardTeleports()`
- Jewellery teleports: `allowJewelleryTeleports()`
- Fairy rings: `allowFairyRings()`
- Spirit trees: `allowSpiritTrees()`

### Overlay toggle
- `showOverlay()` currently gates:
  - In-world destination highlight overlay
  - Bank highlight overlay
  - Shortest Path target updates

## 3) Sidebar panel UX
File: `src/main/java/com/braindeadfarming/ui/BraindeadFarmingPanel.java`

### Panel actions
- `Plan route`
  - Generates a route based on enabled locations + allowed teleports + currently tracked items.
  - Also initializes run state to the first stop (internally calls `RunStateManager.start(route)`).
- `Start run`
  - If there is no route, it plans one first.
  - If in IDLE state with an existing route, it starts it.
- `Next step`
  - Advances to the next stop in the planned route (manual progression).
- `Stop`
  - Resets run state back to idle and clears route index.

### Panel information display
- Shows current run state (IDLE / NEEDS_BANK_SYNC / AT_BANK / TRAVELING / AT_PATCH / ROUTE_COMPLETE)
- Shows “Bank synced: Yes/No”
  - “Bank synced” flips to Yes when bank or seed vault containers update.
  - Inventory and equipment containers still update visible item counts, but do not satisfy bank sync.
- When a route exists:
  - Shows a “Bank checklist” with `have/need` counts
  - If bank is not synced yet, shows the closest bank target and asks the player to open bank or seed vault before showing patch instructions
  - While bank sync is needed, the closest bank suggestion can update as the player moves
  - Shows “Current stop” location name and chosen teleport name
  - Shows “Missing” requirements for the chosen teleport if unmet
  - Shows “Steps” (human-readable travel steps) for the chosen teleport

## 4) Core route dataset (12 locations)
File: `src/main/java/com/braindeadfarming/data/FarmingLocations.java`

### What the dataset includes
For each core location:
- Patch type: TREE or FRUIT_TREE
- A specific destination `WorldPoint` (tile)
- A set of teleport candidates (`TeleportOption`) made of one or more `TravelStep`s
- Some teleport candidates are tagged as “user hint” (preferred) to bias routing

### Travel steps supported (display only)
File: `src/main/java/com/braindeadfarming/data/TravelStep.java`
- TELEPORT / FAIRY_RING / SPIRIT_TREE / WALK
- Each step contains a free-text instruction and a target `WorldPoint`
- Each step has a `TravelMethod` which is used to enforce the teleport capability toggles

## 5) Route planning (MVP)
File: `src/main/java/com/braindeadfarming/route/RoutePlanner.java`

### Inputs
- Config toggles (enabled locations, allowed travel methods, selected seeds, compost)
- Item availability snapshot (bank/seed vault/inventory/equipment)

### Behavior
- Filters the 12 core locations by config toggles
- For each location, picks a teleport candidate by scoring:
  - Penalizes teleports whose requirements are not met (large penalty)
  - Applies teleport priority
  - Applies a “hint bonus” when `userHint` is true
  - Adds a small walk-distance penalty (arrival tile to patch tile)
- Creates a stop list and sorts it (stable/deterministic) with a small local swap-improvement pass

### Output
- `FarmingRoute` with:
  - Ordered stops (`RouteStop`)
  - Derived bank checklist items
- Each `RouteStop` includes:
  - Destination location
  - Selected teleport option
  - Whether teleport requirements are met
  - Human-readable unmet requirement descriptions

## 6) Inventory availability tracking (MVP)
File: `src/main/java/com/braindeadfarming/bank/BankInventoryTracker.java`

### Tracked containers
- Bank
- Seed vault
- Inventory
- Equipment

### Player-facing impact
- Route planning uses the merged snapshot to choose travel methods from everything seen in bank, seed vault, inventory, and equipment.
- The bank checklist in the panel uses the inventory/equipment-only snapshot to show carried `have/need`.
- The bank overlay uses an inventory/equipment-only snapshot to hide bank outlines once the player is already carrying or wearing enough of a required item.
- Teleport selection can prefer hint teleports if the required teleport item(s) are present.
- Only bank and seed vault updates mark the run as bank-synced; inventory/equipment updates are tracked for counts only.

### Closest bank targeting
Files:
- `src/main/java/com/braindeadfarming/bank/BankLocations.java`
- `src/main/java/com/braindeadfarming/bank/BankLocation.java`

- During `NEEDS_BANK_SYNC`, the plugin picks the closest known bank tile from a static MVP bank list.
- The choice is based on current player tile using 2D tile distance only; plane/floor is ignored.
- If the closest known bank changes while the player moves, the sidebar and Shortest Path target are refreshed.

## 7) Bank checklist generation
File: `src/main/java/com/braindeadfarming/bank/InventoryRequirements.java`

### Items currently planned for
- Saplings:
  - Tree saplings: quantity equals number of enabled TREE patches
  - Fruit tree saplings: quantity equals number of enabled FRUIT_TREE patches
- Tools: spade + seed dibber (always 1 each)
- Compost: bucket compost multiplied by (tree+fruit patch count) × `compostQuantityPerPatch()`
- Teleport items:
  - Collected from chosen teleports’ requirements
  - Supports “any-of” item requirements for charged jewellery variants (e.g., Slayer ring charges)

## 8) Manual run state (MVP)
File: `src/main/java/com/braindeadfarming/state/RunStateManager.java`

### Player-facing behavior
- The run has a “current stop index”.
- `Next step` advances the index until complete.
- “Bank synced” influences the initial state:
  - If not synced, run starts in NEEDS_BANK_SYNC.
- In NEEDS_BANK_SYNC, the current farming route is kept, but patch instructions are held until the player opens bank or seed vault.

## 9) In-world overlay (destination highlight)
File: `src/main/java/com/braindeadfarming/overlay/FarmingRunOverlay.java`

### What you should see
- When a run is active and `showOverlay` is enabled:
  - A highlighted polygon on the tile of the current stop.
  - Color:
    - Green-tinted when teleport requirements are met
    - Red-tinted when teleport requirements are not met

## 10) Bank overlay (required item highlighting)
File: `src/main/java/com/braindeadfarming/overlay/BankHighlightOverlay.java`

### What you should see
- When viewing the bank and `showOverlay` is enabled:
  - Required items still needing withdrawal get a colored outline:
    - Green: requirement met (have >= need)
    - Amber: partially met (0 < have < need)
    - Red: missing (have == 0)
  - Items already satisfied by inventory/equipment are not outlined in the bank.

## 11) Shortest Path integration (best-effort)
File: `src/main/java/com/braindeadfarming/navigation/ShortestPathIntegration.java`

### What you should see (if Shortest Path plugin is installed)
- While `showOverlay` is enabled and a route has a current stop:
  - If the run needs bank sync, the plugin attempts to set Shortest Path target to the closest known bank tile.
  - While bank sync is still needed, the plugin refreshes/retargets Shortest Path as the closest known bank changes.
  - After bank sync, the plugin attempts to set Shortest Path target to the current stop’s tile.
- Notes:
  - This uses RuneLite `PluginMessage` events for the external Shortest Path plugin (`shortestpath` namespace).
  - It may fail if Shortest Path changes its plugin-message contract.

## 12) Automated tests (non player-facing, but validates behavior)
- Core dataset sanity: `src/test/java/com/braindeadfarming/data/CoreLocationsTest.java`
- Hint teleports present: `src/test/java/com/braindeadfarming/data/TeleportHintsTest.java`
- Checklist generation sanity: `src/test/java/com/braindeadfarming/bank/InventoryRequirementsTest.java`
- Hand-only inventory snapshot sanity: `src/test/java/com/braindeadfarming/bank/BankInventoryTrackerTest.java`
- Closest bank selection sanity: `src/test/java/com/braindeadfarming/bank/BankLocationsTest.java`
- Planner hint preference sanity: `src/test/java/com/braindeadfarming/route/RoutePlannerTest.java`
- Container sync classification sanity: `src/test/java/com/braindeadfarming/BraindeadFarmingPluginContainerTest.java`
