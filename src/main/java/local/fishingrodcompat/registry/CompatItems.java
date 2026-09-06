package local.fishingrodcompat.registry;

import local.fishingrodcompat.FishingRodCompatMod;
import local.fishingrodcompat.item.AquaStyledTideBobberItem;
import local.fishingrodcompat.item.AquaStyledTideHookItem;
import local.fishingrodcompat.item.AquaStyledTideLineItem;
import local.fishingrodcompat.item.AquaStyledTideFishingRodItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class CompatItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, FishingRodCompatMod.MOD_ID);

    public static final RegistryObject<AquaStyledTideFishingRodItem> AQUACULTURE_IRON_FISHING_ROD =
            registerRod("aquaculture_iron_fishing_rod", 1, 125.0D);
    public static final RegistryObject<AquaStyledTideFishingRodItem> AQUACULTURE_GOLD_FISHING_ROD =
            registerRod("aquaculture_gold_fishing_rod", 1, 55.0D);
    public static final RegistryObject<AquaStyledTideFishingRodItem> AQUACULTURE_DIAMOND_FISHING_ROD =
            registerRod("aquaculture_diamond_fishing_rod", 2, 450.0D);
    public static final RegistryObject<AquaStyledTideFishingRodItem> AQUACULTURE_NEPTUNIUM_FISHING_ROD =
            registerRod("aquaculture_neptunium_fishing_rod", 4, 1000.0D);

    public static final RegistryObject<AquaStyledTideBobberItem> AQUACULTURE_BOBBER =
            ITEMS.register("aquaculture_bobber", () -> new AquaStyledTideBobberItem(new Item.Properties()));
    public static final RegistryObject<AquaStyledTideLineItem> AQUACULTURE_FISHING_LINE =
            ITEMS.register("aquaculture_fishing_line", () -> new AquaStyledTideLineItem(new Item.Properties()));

    public static final RegistryObject<AquaStyledTideHookItem> AQUACULTURE_IRON_HOOK = registerHook("aquaculture_iron_hook", "text.fishing_rod_compat.aquaculture_iron_hook");
    public static final RegistryObject<AquaStyledTideHookItem> AQUACULTURE_GOLD_HOOK = registerHook("aquaculture_gold_hook", "text.fishing_rod_compat.aquaculture_gold_hook");
    public static final RegistryObject<AquaStyledTideHookItem> AQUACULTURE_DIAMOND_HOOK = registerHook("aquaculture_diamond_hook", "text.fishing_rod_compat.aquaculture_diamond_hook");
    public static final RegistryObject<AquaStyledTideHookItem> AQUACULTURE_LIGHT_HOOK = registerHook("aquaculture_light_hook", "text.fishing_rod_compat.aquaculture_light_hook");
    public static final RegistryObject<AquaStyledTideHookItem> AQUACULTURE_HEAVY_HOOK = registerHook("aquaculture_heavy_hook", "text.fishing_rod_compat.aquaculture_heavy_hook");
    public static final RegistryObject<AquaStyledTideHookItem> AQUACULTURE_DOUBLE_HOOK = registerHook("aquaculture_double_hook", "text.fishing_rod_compat.aquaculture_double_hook");
    public static final RegistryObject<AquaStyledTideHookItem> AQUACULTURE_REDSTONE_HOOK = registerHook("aquaculture_redstone_hook", "text.fishing_rod_compat.aquaculture_redstone_hook");
    public static final RegistryObject<AquaStyledTideHookItem> AQUACULTURE_NOTE_HOOK = registerHook("aquaculture_note_hook", "text.fishing_rod_compat.aquaculture_note_hook");
    public static final RegistryObject<AquaStyledTideHookItem> AQUACULTURE_NETHER_STAR_HOOK = registerHook("aquaculture_nether_star_hook", "text.fishing_rod_compat.aquaculture_nether_star_hook");

    private CompatItems() {
    }

    private static RegistryObject<AquaStyledTideFishingRodItem> registerRod(
            String name,
            int baitSlots,
            double durability
    ) {
        return ITEMS.register(name, () -> new AquaStyledTideFishingRodItem(
                baitSlots,
                durability,
                new Item.Properties()
        ));
    }

    private static RegistryObject<AquaStyledTideHookItem> registerHook(String name, String description) {
        return ITEMS.register(name, () -> new AquaStyledTideHookItem(new Item.Properties(), description));
    }
}
