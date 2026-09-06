# Changelog

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
