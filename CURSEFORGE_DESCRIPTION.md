# Fishing Rod Compat

## One-Sentence Description

Brings Aquaculture-styled fishing rods and tackle accessories to Tide's fishing system on Forge 1.20.1.

## Full Description

Fishing Rod Compat bridges **Aquaculture** and **Tide**, replacing Aquaculture's fishing rods and tackle accessories with Tide-based items that keep Aquaculture's familiar appearance and gameplay identity.

The mod is designed for Forge 1.20.1 and requires **Tide 2.1.1 or newer** and **Aquaculture 2.5.5 or newer**.

## Features

- Aquaculture-styled fishing rods that use Tide's fishing system.
- Tide's native fishing entities, fishing tables, conditions, minigame, bait list, and accessory storage.
- Diamond fishing rod with two bait slots.
- Neptunium fishing rod with four bait slots and `+10` Tide lure speed.
- Aquaculture's rod durability values retained by the replacement rods.
- Aquaculture-styled Tide hook, line, and bobber items with matching textures.
- Replacement accessories that work in Aquaculture's tackle box alongside Tide accessories.
- Persistent tackle-box changes written directly to the rod's Tide accessory data.
- Replacement rods added to Tide's fishing rod tag and Tide's creative tab.
- Aquaculture's worm registered as a Tide bait with `+1` speed.
- Worms use durability-based consumption: each successful catch damages the worm by 1, and a worm is removed after 20 durability.
- Tide data packs can continue to control fish tables, loot, fishing conditions, and accessory tags.

## Hook Effects

The replacement hooks map the original Aquaculture effects onto Tide's fishing hook:

| Hook | Effect |
| --- | --- |
| Iron Hook | 20% chance to prevent rod durability loss. |
| Gold Hook | `+1` fishing luck. |
| Diamond Hook | 50% chance to prevent rod durability loss. |
| Light Hook | Applies Aquaculture's light initial movement weight. |
| Heavy Hook | Applies Aquaculture's heavy initial movement weight. |
| Double Hook | 10% chance to add an extra copy of the Tide catch. |
| Redstone Hook | Changes the bite reaction window to `35..70` ticks. |
| Note Hook | Plays Aquaculture's note sound when a fish bites. |
| Nether Star Hook | Allows lava fishing, adds `+1` luck, and has a 50% durability-save chance. |

## Installation

1. Install Forge for Minecraft 1.20.1.
2. Install Tide 2.1.1 or newer.
3. Install Aquaculture 2.5.5 or newer.
4. Place the Fishing Rod Compat JAR in the instance's `mods` folder.

## Item IDs

Use the replacement items from the `fishing_rod_compat` namespace. Their names follow the `fishing_rod_compat:aquaculture_*` pattern, for example:

- `fishing_rod_compat:aquaculture_iron_fishing_rod`
- `fishing_rod_compat:aquaculture_neptunium_fishing_rod`
- `fishing_rod_compat:aquaculture_fishing_line`
- `fishing_rod_compat:aquaculture_bobber`
- `fishing_rod_compat:aquaculture_iron_hook`

The original Aquaculture fishing rods, bobber, line, and supported hooks are disabled by the compatibility layer. Their recipes are removed from the loaded recipe list, and the replacement items should be used for Tide-based fishing.

## License

Original source code and project files are All Rights Reserved (ARR). Third-party dependencies and bundled third-party assets remain under their respective licenses.
