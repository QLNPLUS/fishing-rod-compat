package local.fishingrodcompat.client;

import com.li64.tide.client.TideItemModelProperties;
import local.fishingrodcompat.FishingRodCompatMod;
import local.fishingrodcompat.registry.CompatItems;
import net.minecraft.client.renderer.item.ItemProperties;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(
        modid = FishingRodCompatMod.MOD_ID,
        bus = EventBusSubscriber.Bus.MOD,
        value = Dist.CLIENT
)
public final class FishingRodCompatClient {
    private FishingRodCompatClient() {
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            registerCastProperty(CompatItems.AQUACULTURE_IRON_FISHING_ROD);
            registerCastProperty(CompatItems.AQUACULTURE_GOLD_FISHING_ROD);
            registerCastProperty(CompatItems.AQUACULTURE_DIAMOND_FISHING_ROD);
            registerCastProperty(CompatItems.AQUACULTURE_NEPTUNIUM_FISHING_ROD);
        });
    }

    private static void registerCastProperty(
            net.neoforged.neoforge.registries.DeferredItem<?> item
    ) {
        ItemProperties.register(
                item.get(),
                TideItemModelProperties.CAST_PROPERTY,
                TideItemModelProperties.CAST_FUNCTION
        );
    }
}
