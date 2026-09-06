package local.fishingrodcompat.mixin;

import com.li64.tide.registries.items.FishingBobberItem;
import local.fishingrodcompat.registry.CompatItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = FishingBobberItem.class, remap = false)
public abstract class TideFishingBobberItemMixin {
    @Inject(method = "getTexture", at = @At("HEAD"), cancellable = true, remap = false)
    private static void fishingRodCompat$useAquacultureBobberTexture(
            ItemStack stack,
            CallbackInfoReturnable<ResourceLocation> callbackInfo
    ) {
        if (stack.getItem() == CompatItems.AQUACULTURE_BOBBER.get()) {
            callbackInfo.setReturnValue(new ResourceLocation(
                    "aquaculture",
                    "textures/entity/rod/bobber/bobber.png"
            ));
        }
    }
}
