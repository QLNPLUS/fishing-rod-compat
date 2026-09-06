package local.fishingrodcompat.mixin;

import com.li64.tide.registries.items.TideFishingRodItem;
import local.fishingrodcompat.adapter.TideRodAccessoryBridge;
import local.fishingrodcompat.api.FishingRodCompat;
import local.fishingrodcompat.api.RodAccessorySlot;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = TideFishingRodItem.class, remap = false)
public abstract class TideFishingRodItemMixin {
    @Inject(method = "m_142305_", at = @At("HEAD"), cancellable = true, remap = false)
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

    @ModifyArg(
            method = "castHook",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/li64/tide/registries/entities/misc/fishing/TideFishingHook;<init>(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/Level;IIFLnet/minecraft/world/item/ItemStack;)V"
            ),
            index = 4,
            remap = false
    )
    private int fishingRodCompat$boostNeptuniumLureSpeed(
            EntityType<?> entityType,
            Player player,
            Level level,
            int luck,
            int lure,
            float speed,
            ItemStack rod
    ) {
        if (FishingRodCompat.isAquaTideNeptuniumRod(rod)) {
            return lure + FishingRodCompat.NEPTUNIUM_LURE_SPEED_BONUS;
        }
        return lure;
    }

    @ModifyArg(
            method = "castHook",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/li64/tide/registries/entities/misc/fishing/TideFishingHook;<init>(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/Level;IIFLnet/minecraft/world/item/ItemStack;)V"
            ),
            index = 3,
            remap = false
    )
    private int fishingRodCompat$boostHookLuck(
            EntityType<?> entityType,
            Player player,
            Level level,
            int luck,
            int lure,
            float speed,
            ItemStack rod
    ) {
        return luck + FishingRodCompat.getAquaTideHookLuckBonus(rod);
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
