package local.fishingrodcompat.mixin;

import com.teammetallurgy.aquaculture.inventory.container.slot.SlotHidable;
import local.fishingrodcompat.adapter.TideRodEquipmentHandler;
import local.fishingrodcompat.api.FishingRodCompat;
import local.fishingrodcompat.api.RodAccessorySlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "com.teammetallurgy.aquaculture.inventory.container.TackleBoxContainer$4", remap = false)
@Pseudo
public abstract class TackleBobberSlotMixin {
    @Inject(method = "m_5857_", at = @At("HEAD"), cancellable = true, remap = false)
    private void fishingRodCompat$allowTideBobber(ItemStack stack, CallbackInfoReturnable<Boolean> callbackInfo) {
        if (FishingRodCompat.isDisabledAquacultureItem(stack)) {
            callbackInfo.setReturnValue(false);
            return;
        }
        IItemHandler handler = ((SlotHidable) (Object) this).getItemHandler();
        if (handler instanceof TideRodEquipmentHandler) {
            callbackInfo.setReturnValue(FishingRodCompat.isTideAccessory(RodAccessorySlot.BOBBER, stack)
                    && handler.isItemValid(RodAccessorySlot.BOBBER.index(), stack));
        }
    }
}
