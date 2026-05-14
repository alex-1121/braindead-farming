# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

RuneLite plugin for Old School RuneScape (OSRS).

No more need to plan your farming and birdhouse runs. Turn off your brain and just follow the instructions.

Java 11, Gradle 8.8, compiles against the latest RuneLite client release. Uses Lombok for boilerplate reduction.

## Build & Run

```bash
./gradlew build          # compile + run tests
./gradlew run            # launch RuneLite in developer mode with the plugin loaded
./gradlew test           # run JUnit 4 tests only
```

## Architecture

This is a standard single-plugin RuneLite project:

- **Plugin entrypoint**: `BraindeadFarmingPlugin` extends `Plugin`. Lifecycle via `startUp()`/`shutDown()`, event handling via `@Subscribe` (e.g., `onGameTick`). Config is injected with Guice and exposed via `@Provides`.
- **Config**: `BraindeadFarmingConfig` interface extends `Config`. Config group is `"braindeadfarming"`. Add new settings as `@ConfigItem` methods here.
- **Test harness**: `BraindeadFarmingPluginTest.main()` boots a full RuneLite client with the plugin loaded as an external plugin — this is what `./gradlew run` executes (set as `pluginMainClass` in build.gradle).
- **Plugin-hub metadata**: `src/main/resources/runelite-plugin.properties` — update when renaming or adding plugin classes.

## Conventions

- RuneLite uses Allman brace style (opening brace on its own line for classes and methods).
- All RuneLite API dependencies are `compileOnly` — the client provides them at runtime.
