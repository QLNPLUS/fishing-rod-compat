package local.fishingrodcompat.mixin;

import com.li64.tide.registries.entities.misc.fishing.HookAccessor;
import com.li64.tide.registries.entities.misc.fishing.TideFishingHook;
import local.fishingrodcompat.compat.TideHookLinkRecovery;
import local.fishingrodcompat.config.FishingRodCompatConfig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Keeps a live Tide bobber reachable through {@code Player.fishing}.
 *
 * <p>Tide only links a bobber when the player's fishing field is empty, and only clears it through
 * its own {@code HookAccessor}. If the field is cleared or taken over by another fishing mod while
 * the bobber is still alive, the bobber becomes unreachable and cannot be reeled in until the
 * player relogs. The two injectors below make that state recoverable without changing normal
 * fishing.</p>
 */
@Mixin(value = TideFishingHook.class, remap = false)
public abstract class TideFishingHookLinkRecoveryMixin {
    @Unique
    private FishingHook fishingRodCompat$foreignLink;

    @Unique
    private boolean fishingRodCompat$linkRecoveryWarned;

    /**
     * {@code updateOwnerInfo} unconditionally casts {@code Player.fishing} to Tide's
     * {@code HookAccessor}. A foreign hook in that slot therefore threw a ClassCastException out
     * of the removal path and stopped the bobber from ever being discarded. Move a foreign link
     * aside for the duration of the call and put it back afterwards when Tide did not replace it.
     */
    @Inject(method = "updateOwnerInfo", at = @At("HEAD"), remap = false)
    private void fishingRodCompat$detachForeignLink(TideFishingHook hook, CallbackInfo callbackInfo) {
        if (!FishingRodCompatConfig.tideHookLinkRecovery()) {
            return;
        }
        TideFishingHook self = (TideFishingHook) (Object) this;
        Player player = self.getPlayerOwner();
        if (player == null) {
            return;
        }
        FishingHook link = player.fishing;
        if (link != null && !(link instanceof HookAccessor)) {
            fishingRodCompat$foreignLink = link;
            player.fishing = null;
        }
    }

    @Inject(method = "updateOwnerInfo", at = @At("RETURN"), remap = false)
    private void fishingRodCompat$restoreForeignLink(TideFishingHook hook, CallbackInfo callbackInfo) {
        if (fishingRodCompat$foreignLink == null) {
            return;
        }
        TideFishingHook self = (TideFishingHook) (Object) this;
        Player player = self.getPlayerOwner();
        FishingHook foreign = fishingRodCompat$foreignLink;
        fishingRodCompat$foreignLink = null;
        if (player != null && player.fishing == null) {
            player.fishing = foreign;
        }
    }

    /**
     * Re-links a bobber whose owner lost the link while the bobber kept ticking. Runs at the end
     * of the server tick, after {@code shouldKeepFishing} has had the chance to discard a bobber
     * whose rod is no longer held, so a discarded bobber is never revived.
     */
    @Inject(method = "m_8119_", at = @At("RETURN"), remap = false)
    private void fishingRodCompat$recoverOrphanedBobber(CallbackInfo callbackInfo) {
        if (!FishingRodCompatConfig.tideHookLinkRecovery()) {
            return;
        }
        TideFishingHook self = (TideFishingHook) (Object) this;
        if (self.level().isClientSide || self.isRemoved()) {
            return;
        }
        if (TideHookLinkRecovery.ensureLinked(self, !fishingRodCompat$linkRecoveryWarned)) {
            fishingRodCompat$linkRecoveryWarned = true;
        }
    }
}
