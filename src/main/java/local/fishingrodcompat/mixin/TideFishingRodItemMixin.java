package local.fishingrodcompat.mixin;

import com.li64.tide.registries.items.TideFishingRodItem;
import local.fishingrodcompat.adapter.TideRodAccessoryBridge;
import local.fishingrodcompat.api.FishingRodCompat;
import local.fishingrodcompat.api.RodAccessorySlot;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(value = TideFishingRodItem.class, remap = false)
public abstract class TideFishingRodItemMixin {
    @Inject(method = "overrideOtherStackedOnMe", at = @At("HEAD"), cancellable = true, remap = false)
    private void fishingRodCompat$insertBobber(
            ItemStack rod,
            ItemStack incoming,
            Slot slot,
            ClickAction clickAction,
            Player player,
            SlotAccess slotAccess,
            CallbackInfoReturnable<Boolean> callbackInfo
    ) {
        if (clickAction != ClickAction.SECONDARY
                || incoming.isEmpty()
                || !FishingRodCompat.isTideAccessory(RodAccessorySlot.BOBBER, incoming)
                || !slot.mayPickup(player)) {
            return;
        }

        ItemStack remainder = TideRodAccessoryBridge.INSTANCE.insert(
                rod,
                RodAccessorySlot.BOBBER,
                incoming,
                false
        );
        if (remainder.getCount() == incoming.getCount()) {
            return;
        }

        slot.setChanged();
        slotAccess.set(remainder);
        callbackInfo.setReturnValue(true);
    }

    @ModifyArgs(
            method = "castHook",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/li64/tide/registries/entities/misc/fishing/TideFishingHook;<init>(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/Level;IIFLnet/minecraft/world/item/ItemStack;)V"
            ),
            remap = false
    )
    private void fishingRodCompat$boostNeptuniumLureSpeed(Args args) {
        ItemStack rod = args.get(6);
        if (FishingRodCompat.isAquaTideNeptuniumRod(rod)) {
            args.set(4, (Integer) args.get(4) + FishingRodCompat.NEPTUNIUM_LURE_SPEED_BONUS);
        }
        int luckBonus = FishingRodCompat.getAquaTideHookLuckBonus(rod);
        if (luckBonus != 0) {
            args.set(3, (Integer) args.get(3) + luckBonus);
        }
    }

    @Inject(method = "isLavaproof", at = @At("HEAD"), cancellable = true, remap = false)
    private void fishingRodCompat$allowNetherStarLavaFishing(
            ItemStack rod,
            CallbackInfoReturnable<Boolean> callbackInfo
    ) {
        if (FishingRodCompat.hasAquaTideHook(rod, "nether_star")) {
            callbackInfo.setReturnValue(true);
        }
    }
}
