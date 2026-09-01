package local.fishingrodcompat.mixin;

import com.teammetallurgy.aquaculture.inventory.container.slot.SlotHidable;
import local.fishingrodcompat.adapter.TideRodEquipmentHandler;
import net.neoforged.neoforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = SlotHidable.class, remap = false)
public abstract class SlotHidableMixin {
    @Inject(method = "setChanged", at = @At("HEAD"), cancellable = true, remap = false)
    private void fishingRodCompat$persistTideRod(CallbackInfo callbackInfo) {
        IItemHandler handler = ((SlotHidable) (Object) this).getItemHandler();
        if (handler instanceof TideRodEquipmentHandler tideHandler) {
            tideHandler.markChanged();
            callbackInfo.cancel();
        }
    }
}
