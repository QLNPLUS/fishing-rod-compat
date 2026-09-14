package local.fishingrodcompat;

import local.fishingrodcompat.compat.StardewFishingCompat;
import local.fishingrodcompat.config.FishingRodCompatConfig;
import local.fishingrodcompat.registry.CompatItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.ModList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mod(FishingRodCompatMod.MOD_ID)
public final class FishingRodCompatMod {
    public static final String MOD_ID = "fishing_rod_compat";
    private static final ResourceKey<CreativeModeTab> TIDE_CREATIVE_TAB = ResourceKey.create(
            Registries.CREATIVE_MODE_TAB,
            new ResourceLocation("tide", "tide")
    );

    public FishingRodCompatMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        CompatItems.ITEMS.register(modEventBus);
        modEventBus.addListener(FishingRodCompatMod::addCreativeItems);

        // Common config: the compatibility patches under [fixes] are decided by the server, and
        // the fishing hook runs on both sides, so a common config keeps them in step.
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, FishingRodCompatConfig.SPEC);

        if (ModList.get().isLoaded("stardew_fishing")) {
            MinecraftForge.EVENT_BUS.register(StardewFishingCompat.INSTANCE);
        }
    }

    private static void addCreativeItems(BuildCreativeModeTabContentsEvent event) {
        for (net.minecraft.world.item.Item item : disabledAquacultureItems()) {
            List<ItemStack> matches = new ArrayList<>();
            for (Map.Entry<ItemStack, ?> entry : event.getEntries()) {
                if (entry.getKey().getItem() == item) {
                    matches.add(entry.getKey());
                }
            }
            matches.forEach(event.getEntries()::remove);
        }

        if (event.getTabKey().equals(TIDE_CREATIVE_TAB)) {
            event.accept(CompatItems.AQUACULTURE_IRON_FISHING_ROD.get());
            event.accept(CompatItems.AQUACULTURE_GOLD_FISHING_ROD.get());
            event.accept(CompatItems.AQUACULTURE_DIAMOND_FISHING_ROD.get());
            event.accept(CompatItems.AQUACULTURE_NEPTUNIUM_FISHING_ROD.get());
            event.accept(CompatItems.AQUACULTURE_BOBBER.get());
            event.accept(CompatItems.AQUACULTURE_FISHING_LINE.get());
            event.accept(CompatItems.AQUACULTURE_IRON_HOOK.get());
            event.accept(CompatItems.AQUACULTURE_GOLD_HOOK.get());
            event.accept(CompatItems.AQUACULTURE_DIAMOND_HOOK.get());
            event.accept(CompatItems.AQUACULTURE_LIGHT_HOOK.get());
            event.accept(CompatItems.AQUACULTURE_HEAVY_HOOK.get());
            event.accept(CompatItems.AQUACULTURE_DOUBLE_HOOK.get());
            event.accept(CompatItems.AQUACULTURE_REDSTONE_HOOK.get());
            event.accept(CompatItems.AQUACULTURE_NOTE_HOOK.get());
            event.accept(CompatItems.AQUACULTURE_NETHER_STAR_HOOK.get());
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
                local.fishingrodcompat.api.FishingRodCompat.getAquacultureHook("iron"),
                local.fishingrodcompat.api.FishingRodCompat.getAquacultureHook("gold"),
                local.fishingrodcompat.api.FishingRodCompat.getAquacultureHook("diamond"),
                local.fishingrodcompat.api.FishingRodCompat.getAquacultureHook("light"),
                local.fishingrodcompat.api.FishingRodCompat.getAquacultureHook("heavy"),
                local.fishingrodcompat.api.FishingRodCompat.getAquacultureHook("double"),
                local.fishingrodcompat.api.FishingRodCompat.getAquacultureHook("redstone"),
                local.fishingrodcompat.api.FishingRodCompat.getAquacultureHook("note"),
                local.fishingrodcompat.api.FishingRodCompat.getAquacultureHook("nether_star")
        };
    }
}
