# Changelog

## 1.2.1

### Fixed

- Kept the NeoForge 1.21.1 replacement recipes compatible with NeoForge's common material tags.

## 1.2.0

### Fixed

- AutoFish continues fishing in the background while an inventory or container screen is open instead of triggering a reel-in.
- Hardened recipe reload compatibility when an upstream recipe uses a non-object result format.
- Changed the Neptunium fishing-rod recipe to use Aquaculture's Neptunium ingot item directly.

## 1.0.2

### Added

- Added a config file, `config/fishing_rod_compat-common.toml`, with a `[fixes]` section for compatibility patches that only exist because an upstream mod is currently broken.
- Added `fixes.stardew_hook_crash_guard` (default `true`). Set it to `false` to restore Tide's own behaviour, so the mod can stay installed after upstream fixes the bug.

### Fixed

- Fixed a crash that took down the world save when Tide and Stardew Fishing were installed together. `TideFishingHook.catchingFish()` passed the player's fishing hook to Stardew Fishing without checking it, so a bobber whose hook link had broken threw a NullPointerException out of the entity tick. The reward lookup is now skipped for such a bobber, and a warning naming the bobber is logged once.
- Removed a stale `fishing_rod_compat.mixins.json` from the project root. It was a left-over copy listing only 15 mixins, was not read by the build, and could easily be edited by mistake.

## 1.0.1

### Fixed

- Fixed Aquaculture fishing-rod recipes so they craft the Tide-based replacement rods instead of the disabled original rods.
- Covered both recipe ID naming variants used by Aquaculture, including gold_fishing_rod and golden_fishing_rod.

## 1.0

### Changed

- Adjusted the Neptunium rod's Tide lure-speed bonus to match Tide's nonlinear bite timer.
- Clarified that hook durability-saving effects protect the fishing rod.

## 0.9.1

### Fixed

- Fixed AutoFish compatibility with Tide fishing rods so the rod no longer retracts immediately before a bite.

## 0.9.0

### Added

- Added Stardew Fishing 3.7 compatibility for NeoForge 1.21.1.
- Added direct inventory right-click installation for the replacement bobber.

### Changed

- Aquaculture worms retain their 20-durability, non-stackable behavior.
- Successful Tide and Stardew catches now consume worm durability consistently.
- No-durability bait falls back to count-based consumption.

### Compatibility

- Requires NeoForge 1.21.1, Tide 2.1.1 or newer, and Aquaculture 2.7.21 or newer.

## 0.8.0

### Added

- Added Aquaculture-styled Tide fishing rods for iron, gold, diamond, and neptunium tiers.
- Added Aquaculture-styled Tide hooks, fishing line, and bobber accessories.
- Added direct Tide accessory storage integration for Aquaculture's tackle box.
- Added compatibility behavior for Aquaculture hook effects, including durability saving, luck, movement weight, extra catches, bite timing, note sounds, and lava fishing.
- Added `aquaculture:worm` as a Tide bait with `+1` speed.
- Added durability-based worm consumption with a 20-durability lifespan.
- Added replacement rods and accessories to the appropriate Tide tags and creative tab.
- Added Aquaculture textures and entity rendering support for replacement Tide accessories.
- Added the `RodAccessoryBridge` API for separating logical accessory slots from native rod storage.

### Changed

- Replacement rods now use Tide's native fishing entities, fishing tables, conditions, minigame, and bait-slot tooltip.
- Diamond rods provide two bait slots.
- Neptunium rods provide four bait slots and `+10` Tide lure speed.
- Replacement rods retain the durability values of their corresponding Aquaculture rods.
- Aquaculture worms now have a maximum stack size of 1, including in Tide rod storage and Aquaculture's tackle box.

### Compatibility

- Requires NeoForge 1.21.1.
- Requires Tide 2.1.1 or newer.
- Requires Aquaculture 2.7.21 or newer.
- Tide data packs can still customize fishing tables, loot, conditions, and accessory tags.
