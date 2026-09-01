package local.fishingrodcompat;

import local.fishingrodcompat.registry.CompatItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTab.TabVisibility;

@Mod(FishingRodCompatMod.MOD_ID)
public final class FishingRodCompatMod {
    public static final String MOD_ID = "fishing_rod_compat";
    private static final ResourceKey<CreativeModeTab> TIDE_CREATIVE_TAB = ResourceKey.create(
            Registries.CREATIVE_MODE_TAB,
            ResourceLocation.fromNamespaceAndPath("tide", "tide")
    );

    public FishingRodCompatMod(IEventBus modEventBus) {
        CompatItems.ITEMS.register(modEventBus);
        modEventBus.addListener(FishingRodCompatMod::addCreativeItems);
    }

    private static void addCreativeItems(BuildCreativeModeTabContentsEvent event) {
        for (net.minecraft.world.item.Item item : disabledAquacultureItems()) {
            event.remove(new ItemStack(item), TabVisibility.PARENT_AND_SEARCH_TABS);
        }

        if (event.getTabKey().equals(TIDE_CREATIVE_TAB)) {
            event.accept(CompatItems.AQUACULTURE_IRON_FISHING_ROD);
            event.accept(CompatItems.AQUACULTURE_GOLD_FISHING_ROD);
            event.accept(CompatItems.AQUACULTURE_DIAMOND_FISHING_ROD);
            event.accept(CompatItems.AQUACULTURE_NEPTUNIUM_FISHING_ROD);
            event.accept(CompatItems.AQUACULTURE_BOBBER);
            event.accept(CompatItems.AQUACULTURE_FISHING_LINE);
            event.accept(CompatItems.AQUACULTURE_IRON_HOOK);
            event.accept(CompatItems.AQUACULTURE_GOLD_HOOK);
            event.accept(CompatItems.AQUACULTURE_DIAMOND_HOOK);
            event.accept(CompatItems.AQUACULTURE_LIGHT_HOOK);
            event.accept(CompatItems.AQUACULTURE_HEAVY_HOOK);
            event.accept(CompatItems.AQUACULTURE_DOUBLE_HOOK);
            event.accept(CompatItems.AQUACULTURE_REDSTONE_HOOK);
            event.accept(CompatItems.AQUACULTURE_NOTE_HOOK);
            event.accept(CompatItems.AQUACULTURE_NETHER_STAR_HOOK);
        }
    }

    private static net.minecraft.world.item.Item[] disabledAquacultureItems() {
        return new net.minecraft.world.item.Item[]{
                com.teammetallurgy.aquaculture.init.AquaItems.IRON_FISHING_ROD.get(),
                com.teammetallurgy.aquaculture.init.AquaItems.GOLD_FISHING_ROD.get(),
                com.teammetallurgy.aquaculture.init.AquaItems.DIAMOND_FISHING_ROD.get(),
                com.teammetallurgy.aquaculture.init.AquaItems.NEPTUNIUM_FISHING_ROD.get(),
                com.teammetallurgy.aquaculture.init.AquaItems.FISHING_LINE.get(),
                com.teammetallurgy.aquaculture.init.AquaItems.BOBBER.get(),
                com.teammetallurgy.aquaculture.init.AquaItems.IRON_HOOK.get(),
                com.teammetallurgy.aquaculture.init.AquaItems.GOLD_HOOK.get(),
                com.teammetallurgy.aquaculture.init.AquaItems.DIAMOND_HOOK.get(),
                com.teammetallurgy.aquaculture.init.AquaItems.LIGHT_HOOK.get(),
                com.teammetallurgy.aquaculture.init.AquaItems.HEAVY_HOOK.get(),
                com.teammetallurgy.aquaculture.init.AquaItems.DOUBLE_HOOK.get(),
                com.teammetallurgy.aquaculture.init.AquaItems.REDSTONE_HOOK.get(),
                com.teammetallurgy.aquaculture.init.AquaItems.NOTE_HOOK.get(),
                com.teammetallurgy.aquaculture.init.AquaItems.NETHER_STAR_HOOK.get()
        };
    }
}
