package local.fishingrodcompat.mixin;

import local.fishingrodcompat.api.FishingRodCompat;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "com.teammetallurgy.aquaculture.block.blockentity.TackleBoxBlockEntity$2", remap = false)
@Pseudo
public abstract class TackleBoxBlockEntityHandlerMixin {
    @Inject(method = "isItemValid", at = @At("HEAD"), cancellable = true, remap = false)
    private void fishingRodCompat$allowTideRod(
            int slot,
            ItemStack stack,
            CallbackInfoReturnable<Boolean> callbackInfo
    ) {
        if (FishingRodCompat.isDisabledAquacultureItem(stack)) {
            callbackInfo.setReturnValue(false);
            return;
        }
        if (slot == 0 && FishingRodCompat.isTideRod(stack)) {
            callbackInfo.setReturnValue(true);
        }
    }
}
