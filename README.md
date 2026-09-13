# Fishing Rod Compat

Forge 1.20.1 compatibility mod for Tide 2.1.1 and Aquaculture 2.5.5.

## Version 1.0

This version replaces Aquaculture's fishing rods and tackle accessories with separate items that use Tide's fishing system while keeping Aquaculture's appearance. The original Aquaculture rods, bobber, line, and hooks are hidden, cannot be used in the tackle box, and their recipes are removed from the loaded recipe list.

The replacement rods use Tide's native bait list, accessories, fishing entities, fishing tables, conditions, minigame, and bait-slot tooltip. The Diamond rod has two bait slots, and the Neptunium rod has four bait slots. Both keep their corresponding Aquaculture rod durability. The Neptunium rod adds `+1` Tide lure speed when it creates the Tide fishing hook, making fish bite sooner without forcing Tide's minimum bite timer. Its tooltip uses Tide's gold bonus-text style.

`aquaculture:worm` is registered as a Tide bait with `+1` speed, while remaining the original Aquaculture item so its existing recipes, loot, and other acquisition methods are unchanged. Its stack size is changed to 1 globally, and Tide rod bait storage plus the Aquaculture tackle box enforce the same limit. A successful Tide catch damages each stored worm by 1 durability instead of shrinking its stack; a worm is removed when its 20 durability is exhausted. Other Tide baits retain their normal count-based consumption.

The replacement Aquaculture-style hooks also map their original gameplay effects onto Tide's fishing hook:

- Iron hook: 20% chance to prevent rod durability loss.
- Gold hook: `+1` fishing luck.
- Diamond hook: 50% chance to prevent rod durability loss.
- Light/heavy hook: applies Aquaculture's initial movement weight to the Tide hook.
- Double hook: 10% chance to add an extra copy of the Tide catch.
- Redstone hook: changes Tide's bite reaction window to `35..70` ticks.
- Note hook: plays Aquaculture's note sound when fish bite.
- Nether Star hook: permits lava fishing, adds `+1` luck, and has a 50% durability-save chance.

The replacement bobber, line, and hooks remain Tide accessory items. Their Aquaculture textures are visual only unless an effect is listed above. Tide data packs can still change the fish tables, loot, conditions, and accessory tags; the item-specific hook effects are mapped by the compatibility mixins.

All Tide rods and Tide accessory items (bait, hooks, lines, and bobbers), including the compatibility replacements, can be placed in `aquaculture:tackle_box`. The four tackle slots edit the rod's Tide accessory data directly, so changes persist on the rod and are used by Tide when it is cast. Each accessory remains limited to its matching tackle-box slot. The replacement rods and accessories are listed in Tide's `tide:tide` creative-mode tab, not Minecraft's Tools & Utilities tab. The replacement rods also extend Tide's `fishing_rods` tag.

The replacement accessory items are Tide-native hook, line, and bobber items. Their inventory models use Aquaculture textures, and their entity rendering is redirected to Aquaculture's existing hook/bobber textures. They can be placed in Aquaculture's `aquaculture:tackle_box` alongside Tide's native accessories.

The disabled original items are:

- `aquaculture:iron_fishing_rod`
- `aquaculture:gold_fishing_rod`
- `aquaculture:diamond_fishing_rod`
- `aquaculture:neptunium_fishing_rod`
- `aquaculture:fishing_line`
- `aquaculture:bobber`
- `aquaculture:iron_hook`, `gold_hook`, `diamond_hook`, `light_hook`, `heavy_hook`
- `aquaculture:double_hook`, `redstone_hook`, `note_hook`, `nether_star_hook`

Use the `fishing_rod_compat:aquaculture_*` items for the Tide-based behavior. The item names no longer include a Tide-system suffix.

The `RodAccessoryBridge` API isolates logical equipment slots from native rod storage. Future cross-mod accessory conversion can be added there without changing the tackle-box mixins.

## Build

Use JDK 17:

```powershell
.\gradlew.bat build
```

The output is `build/libs/fishing_rod_compat-1.0.jar`.

Copy that JAR into the instance's `mods` directory alongside Tide and Aquaculture. The included files under `libs` are compile/runtime dependencies for the development project and are not bundled into the output JAR.

## License

Original source code and project files are All Rights Reserved (ARR). See [LICENSE](LICENSE).

Third-party dependencies and bundled third-party assets remain under their respective licenses.
