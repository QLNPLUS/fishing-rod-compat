package local.fishingrodcompat.mixin;

import com.li64.tide.registries.items.FishingHookItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = FishingHookItem.class, remap = false)
public abstract class TideFishingHookItemMixin {
    @Inject(method = "getTexture", at = @At("HEAD"), cancellable = true, remap = false)
    private static void fishingRodCompat$useAquacultureHookTexture(
            ItemStack stack,
            CallbackInfoReturnable<ResourceLocation> callbackInfo
    ) {
        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
        if (itemId.getNamespace().equals("fishing_rod_compat")
                && itemId.getPath().startsWith("aquaculture_")
                && itemId.getPath().endsWith("_hook")) {
            String texture = itemId.getPath().substring("aquaculture_".length());
            callbackInfo.setReturnValue(ResourceLocation.fromNamespaceAndPath(
                    "aquaculture",
                    "textures/entity/rod/hook/" + texture + ".png"
            ));
        }
    }
}
