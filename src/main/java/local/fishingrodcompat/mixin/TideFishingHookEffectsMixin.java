package local.fishingrodcompat.mixin;

import com.li64.tide.data.rods.BaitContents;
import com.li64.tide.registries.entities.misc.fishing.TideFishingHook;
import local.fishingrodcompat.api.FishingRodCompat;
import local.fishingrodcompat.compat.TideBaitConsumption;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.sounds.SoundSource;
import com.teammetallurgy.aquaculture.init.AquaSounds;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = TideFishingHook.class, remap = false)
public abstract class TideFishingHookEffectsMixin {
    @Shadow
    protected List<ItemStack> hookedItems;

    @Shadow
    private int nibble;

    @Unique
    private boolean fishingRodCompat$noteSoundPlayed;

    @Unique
    private boolean fishingRodCompat$redstoneWindowApplied;

    @Inject(
            method = "<init>(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/Level;IIFLnet/minecraft/world/item/ItemStack;)V",
            at = @At("RETURN"),
            remap = false
    )
    private void fishingRodCompat$applyHookWeight(
            EntityType<?> entityType,
            Player player,
            Level level,
            int luck,
            int lureSpeed,
            float strength,
            ItemStack rod,
            CallbackInfo callbackInfo
    ) {
        Vec3 weight = FishingRodCompat.getAquaTideHookWeight(rod);
        if (weight != null) {
            TideFishingHook hook = (TideFishingHook) (Object) this;
            hook.setDeltaMovement(hook.getDeltaMovement().multiply(weight));
        }
    }

    @Inject(method = "tick", at = @At("RETURN"), remap = false)
    private void fishingRodCompat$expandRedstoneCatchWindow(CallbackInfo callbackInfo) {
        TideFishingHook hook = (TideFishingHook) (Object) this;
        if (nibble <= 0) {
            fishingRodCompat$redstoneWindowApplied = false;
        } else if (!fishingRodCompat$redstoneWindowApplied
                && FishingRodCompat.hasAquaTideHook(hook.getRod(), "redstone")) {
            nibble = 35 + hook.getRandom().nextInt(36);
            fishingRodCompat$redstoneWindowApplied = true;
        }
    }

    @Inject(method = "tick", at = @At("RETURN"), remap = false)
    private void fishingRodCompat$playNoteHookSound(CallbackInfo callbackInfo) {
        TideFishingHook hook = (TideFishingHook) (Object) this;
        if (nibble <= 0) {
            fishingRodCompat$noteSoundPlayed = false;
            return;
        }
        if (!fishingRodCompat$noteSoundPlayed
                && !hook.level().isClientSide
                && FishingRodCompat.hasAquaTideHook(hook.getRod(), "note")) {
            hook.level().playSound(
                    null,
                    hook.blockPosition(),
                    AquaSounds.BOBBER_NOTE.get(),
                    SoundSource.NEUTRAL,
                    1.0F,
                    1.0F
            );
            fishingRodCompat$noteSoundPlayed = true;
        }
    }

    @Inject(
            method = "selectCatch(Lnet/minecraft/world/item/ItemStack;)V",
            at = @At("RETURN"),
            remap = false
    )
    private void fishingRodCompat$applyDoubleHook(ItemStack rod, CallbackInfo callbackInfo) {
        TideFishingHook hook = (TideFishingHook) (Object) this;
        if (FishingRodCompat.hasAquaTideHook(rod, "double")
                && hookedItems != null
                && !hookedItems.isEmpty()
                && hook.getRandom().nextDouble() <= 0.1D) {
            List<ItemStack> catches = new ArrayList<>(hookedItems);
            catches.add(hookedItems.getFirst().copy());
            hookedItems = catches;
        }
    }

    @Inject(
            method = "retrieve(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/player/Player;)I",
            at = @At("RETURN"),
            remap = false
    )
    private void fishingRodCompat$applyDurabilityProtection(
            ItemStack rod,
            net.minecraft.server.level.ServerLevel level,
            Player player,
            CallbackInfoReturnable<Integer> callbackInfo
    ) {
        double chance = FishingRodCompat.getAquaTideHookDurabilityChance(rod);
        TideFishingHook hook = (TideFishingHook) (Object) this;
        if (chance > 0.0D
                && callbackInfo.getReturnValue() > 0
                && hook.getRandom().nextDouble() < chance) {
            callbackInfo.setReturnValue(0);
        }
    }

    @Redirect(
            method = "retrieve(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/player/Player;)I",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/li64/tide/data/rods/BaitContents$Mutable;shrinkAll()V"
            ),
            remap = false
    )
    private void fishingRodCompat$consumeBait(BaitContents.Mutable mutable) {
        TideBaitConsumption.consume(mutable);
    }
}
