package local.fishingrodcompat.mixin;

import com.teammetallurgy.aquaculture.init.AquaItems;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class AquacultureWormStackSizeMixin {
    @Inject(
            method = "getMaxStackSize",
            at = @At("HEAD"),
            cancellable = true
    )
    private void fishingRodCompat$makeWormNonStackable(
            CallbackInfoReturnable<Integer> callbackInfo
    ) {
        if ((Object) this == AquaItems.WORM.get()) {
            callbackInfo.setReturnValue(1);
        }
    }
}
