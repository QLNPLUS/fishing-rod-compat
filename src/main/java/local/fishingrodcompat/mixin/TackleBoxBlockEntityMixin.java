package local.fishingrodcompat.mixin;

import com.teammetallurgy.aquaculture.block.blockentity.TackleBoxBlockEntity;
import local.fishingrodcompat.api.FishingRodCompat;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = TackleBoxBlockEntity.class, remap = false)
public abstract class TackleBoxBlockEntityMixin {
    @Inject(method = "canBePutInTackleBox", at = @At("HEAD"), cancellable = true, remap = false)
    private static void fishingRodCompat$allowTideTackle(
            ItemStack stack,
            CallbackInfoReturnable<Boolean> callbackInfo
    ) {
        if (FishingRodCompat.isDisabledAquacultureItem(stack)) {
            callbackInfo.setReturnValue(false);
            return;
        }
        if (FishingRodCompat.isTideTackle(stack)) {
            callbackInfo.setReturnValue(true);
        }
    }
}
