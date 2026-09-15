package local.fishingrodcompat.client;

import com.li64.tide.client.TideItemModelProperties;
import local.fishingrodcompat.FishingRodCompatMod;
import local.fishingrodcompat.registry.CompatItems;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.RegistryObject;

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
        MinecraftForge.EVENT_BUS.addListener(AutoFishScreenCycle::onScreenOpening);
        MinecraftForge.EVENT_BUS.addListener(AutoFishScreenCycle::onClientTick);
        event.enqueueWork(() -> {
            registerCastProperty(CompatItems.AQUACULTURE_IRON_FISHING_ROD);
            registerCastProperty(CompatItems.AQUACULTURE_GOLD_FISHING_ROD);
            registerCastProperty(CompatItems.AQUACULTURE_DIAMOND_FISHING_ROD);
            registerCastProperty(CompatItems.AQUACULTURE_NEPTUNIUM_FISHING_ROD);
        });
    }

    private static void registerCastProperty(
            RegistryObject<? extends Item> item
    ) {
        ItemProperties.register(
                item.get(),
                TideItemModelProperties.CAST_PROPERTY,
                TideItemModelProperties.CAST_FUNCTION
        );
    }
}
