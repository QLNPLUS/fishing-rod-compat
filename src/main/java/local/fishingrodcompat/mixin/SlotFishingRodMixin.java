package local.fishingrodcompat.mixin;

import com.teammetallurgy.aquaculture.inventory.container.slot.SlotFishingRod;
import local.fishingrodcompat.adapter.TackleBoxHooks;
import local.fishingrodcompat.api.FishingRodCompat;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = SlotFishingRod.class, remap = false)
public abstract class SlotFishingRodMixin {
    @Inject(method = "mayPlace", at = @At("HEAD"), cancellable = true, remap = false)
    private void fishingRodCompat$allowTideRod(ItemStack stack, CallbackInfoReturnable<Boolean> callbackInfo) {
        if (FishingRodCompat.isDisabledAquacultureRod(stack)) {
            callbackInfo.setReturnValue(false);
            return;
        }
        if (FishingRodCompat.isTideRod(stack)) {
            callbackInfo.setReturnValue(true);
        }
    }

    @Inject(method = "setChanged", at = @At("HEAD"), cancellable = true, remap = false)
    private void fishingRodCompat$createTideHandler(CallbackInfo callbackInfo) {
        SlotFishingRod slot = (SlotFishingRod) (Object) this;
        ItemStack rod = slot.getItem();
        if (FishingRodCompat.isTideRod(rod)) {
            slot.rodHandler = TackleBoxHooks.createTideHandler(slot, rod);
            callbackInfo.cancel();
        }
    }
}
