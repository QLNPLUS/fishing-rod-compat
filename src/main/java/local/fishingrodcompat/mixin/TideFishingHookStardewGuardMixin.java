package local.fishingrodcompat.mixin;

import com.li64.tide.Tide;
import com.li64.tide.compat.CompatHelper;
import com.li64.tide.registries.entities.misc.fishing.HookAccessor;
import com.li64.tide.registries.entities.misc.fishing.TideFishingHook;
import local.fishingrodcompat.config.FishingRodCompatConfig;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

/**
 * Guards the crash in {@code TideFishingHook.catchingFish()}.
 *
 * <p>Tide hands the player's fishing hook to Stardew Fishing like this:</p>
 *
 * <pre>{@code
 * List<ItemStack> rewards = CompatHelper.stardewFishingGetRewards(
 *         (HookAccessor) this.getPlayerOwner().fishing);   // TideFishingHook line 428
 * }</pre>
 *
 * <p>Nothing there is null-checked. {@code player.fishing} is an independently managed field, so
 * it can already be null while the bobber entity is still being ticked. Stardew Fishing then
 * dereferences the null hook (a Forge capability lookup on 1.20.1, a data-attachment lookup on
 * 1.21.1) and throws a NullPointerException out of the entity tick, which takes the whole world
 * save down.</p>
 *
 * <p>The redirect below diverts only the reward lookup, so the rest of {@code catchingFish} -
 * bite timers, particles, minigame startup - still runs exactly as Tide intends. The single
 * visible difference is that a broken bobber grants no Stardew rewards instead of crashing.</p>
 *
 * <p>Controlled by {@code fixes.stardew_hook_crash_guard}. When disabled, the original upstream
 * result is reproduced, so this mod stays transparent once upstream fixes the bug.</p>
 */
@Mixin(value = TideFishingHook.class, remap = false)
public abstract class TideFishingHookStardewGuardMixin {
    @Unique
    private boolean fishingRodCompat$brokenHookLinkWarned;

    @Redirect(
            method = "catchingFish",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/li64/tide/compat/CompatHelper;stardewFishingGetRewards"
                            + "(Lcom/li64/tide/registries/entities/misc/fishing/HookAccessor;)"
                            + "Ljava/util/List;"
            ),
            remap = false
    )
    private List<ItemStack> fishingRodCompat$guardStardewRewards(HookAccessor hook) {
        if (hook != null) {
            return CompatHelper.stardewFishingGetRewards(hook);
        }

        if (!FishingRodCompatConfig.stardewHookCrashGuard()) {
            // Switch off: reproduce exactly what Tide does on its own. This is the original
            // upstream behaviour, crash included.
            return CompatHelper.stardewFishingGetRewards(hook);
        }

        if (!fishingRodCompat$brokenHookLinkWarned) {
            fishingRodCompat$brokenHookLinkWarned = true;
            Tide.LOG.warn(
                    "Tide fishing bobber {} has no linked hook on its owner; skipping the Stardew"
                            + " Fishing reward lookup to avoid a crash. Retrieve the bobber to clear"
                            + " this state. Set fixes.stardew_hook_crash_guard=false in the"
                            + " fishing_rod_compat config to restore Tide's original behaviour.",
                    ((TideFishingHook) (Object) this).getUUID()
            );
        }
        return List.of();
    }
}
