package local.fishingrodcompat.mixin;

import com.li64.tide.registries.items.TideFishingRodItem;
import local.fishingrodcompat.api.FishingRodCompat;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(value = TideFishingRodItem.class, remap = false)
public abstract class TideFishingRodItemMixin {
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
