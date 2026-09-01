package local.fishingrodcompat.mixin;

import com.teammetallurgy.aquaculture.item.AquaFishingRodItem;
import local.fishingrodcompat.api.FishingRodCompat;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = AquaFishingRodItem.class, remap = false)
public abstract class AquaFishingRodItemMixin {
    @Inject(method = "use", at = @At("HEAD"), cancellable = true, remap = false)
    private void fishingRodCompat$disableOriginalRod(
            Level level,
            Player player,
            InteractionHand hand,
            CallbackInfoReturnable<InteractionResultHolder<ItemStack>> callbackInfo
    ) {
        ItemStack rod = player.getItemInHand(hand);
        if (FishingRodCompat.isDisabledAquacultureRod(rod)) {
            callbackInfo.setReturnValue(InteractionResultHolder.fail(rod));
        }
    }
}
