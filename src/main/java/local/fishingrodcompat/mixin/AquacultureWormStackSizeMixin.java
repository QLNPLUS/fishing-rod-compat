package local.fishingrodcompat.mixin;

import com.teammetallurgy.aquaculture.init.AquaItems;
import com.teammetallurgy.aquaculture.item.BaitItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BaitItem.class, remap = false)
public abstract class AquacultureWormStackSizeMixin {
    @Inject(
            method = "getMaxStackSize(Lnet/minecraft/world/item/ItemStack;)I",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private void fishingRodCompat$makeWormNonStackable(
            ItemStack stack,
            CallbackInfoReturnable<Integer> callbackInfo
    ) {
        if (stack.getItem() == AquaItems.WORM.get()) {
            callbackInfo.setReturnValue(1);
        }
    }
}
