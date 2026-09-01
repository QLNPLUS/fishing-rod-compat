package local.fishingrodcompat.mixin;

import com.teammetallurgy.aquaculture.inventory.container.slot.SlotHidable;
import local.fishingrodcompat.adapter.TideRodEquipmentHandler;
import local.fishingrodcompat.api.FishingRodCompat;
import local.fishingrodcompat.api.RodAccessorySlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "com.teammetallurgy.aquaculture.inventory.container.TackleBoxContainer$2", remap = false)
@Pseudo
public abstract class TackleBaitSlotMixin {
    @Inject(method = "mayPlace", at = @At("HEAD"), cancellable = true, remap = false)
    private void fishingRodCompat$allowTideBait(ItemStack stack, CallbackInfoReturnable<Boolean> callbackInfo) {
        IItemHandler handler = ((SlotHidable) (Object) this).getItemHandler();
        if (handler instanceof TideRodEquipmentHandler) {
            callbackInfo.setReturnValue(FishingRodCompat.isTideAccessory(RodAccessorySlot.BAIT, stack)
                    && handler.isItemValid(RodAccessorySlot.BAIT.index(), stack));
        }
    }

    @Inject(method = "mayPickup", at = @At("HEAD"), cancellable = true, remap = false)
    private void fishingRodCompat$allowBaitPickup(
            Player player,
            CallbackInfoReturnable<Boolean> callbackInfo
    ) {
        SlotHidable slot = (SlotHidable) (Object) this;
        IItemHandler handler = slot.getItemHandler();
        if (handler instanceof TideRodEquipmentHandler) {
            callbackInfo.setReturnValue(slot.hasItem()
                    && !handler.getStackInSlot(RodAccessorySlot.BAIT.index()).isEmpty());
        }
    }

}
