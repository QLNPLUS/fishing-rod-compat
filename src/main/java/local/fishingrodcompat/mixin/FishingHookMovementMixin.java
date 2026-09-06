package local.fishingrodcompat.mixin;

import com.li64.tide.registries.entities.misc.fishing.TideFishingHook;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Makes vanilla FishingHook observers see Tide's real hook velocity. */
@Mixin(Entity.class)
public abstract class FishingHookMovementMixin {
    @Inject(method = "getDeltaMovement", at = @At("HEAD"), cancellable = true)
    private void fishingRodCompat$delegateTideHookMovement(
            CallbackInfoReturnable<Vec3> callbackInfo
    ) {
        if ((Object) this instanceof TideHookAccessorMixin accessor) {
            TideFishingHook hook = accessor.fishingRodCompat$getTideHook();
            if (hook != null) {
                callbackInfo.setReturnValue(hook.getDeltaMovement());
            }
        }
    }
}
