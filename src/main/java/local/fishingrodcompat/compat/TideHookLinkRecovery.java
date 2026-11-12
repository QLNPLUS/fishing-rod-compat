package local.fishingrodcompat.compat;

import com.li64.tide.Tide;
import com.li64.tide.registries.entities.misc.fishing.HookAccessor;
import com.li64.tide.registries.entities.misc.fishing.TideFishingHook;
import local.fishingrodcompat.mixin.TideHookAccessorMixin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;

/**
 * Repairs Tide's bobber-to-player link.
 *
 * <p>Tide stores the active bobber in {@code Player.fishing}, which is a single field shared by
 * every fishing mod and by vanilla. When that field is cleared or replaced while the bobber entity
 * is still alive, nothing can reach the bobber any more: the rod reels in nothing, casting a new
 * hook leaves it unlinked, and the orphan bobber keeps floating where it was left. Vanilla only
 * clears the field together with its hook, so Tide never expects this state, but other fishing
 * mods and auto-fishing automation can produce it.</p>
 *
 * <p>The helpers below rebuild the link when it is provably missing, and drop a link that can no
 * longer resolve to a live Tide hook. They never touch a link that already points at a live Tide
 * hook, so normal fishing is unchanged.</p>
 */
public final class TideHookLinkRecovery {
    private TideHookLinkRecovery() {
    }

    /** Returns the Tide hook behind a player's link, or {@code null} when the link is not Tide's. */
    public static TideFishingHook linkedHook(FishingHook link) {
        if (link instanceof HookAccessor accessor) {
            return ((TideHookAccessorMixin) accessor).fishingRodCompat$getTideHook();
        }
        return null;
    }

    public static boolean isLiveLink(FishingHook link) {
        TideFishingHook hook = linkedHook(link);
        return hook != null && !hook.isRemoved();
    }

    /**
     * Drops a link that can no longer produce a usable Tide hook.
     *
     * <p>On the server a foreign hook in the slot also has to be released, because otherwise a new
     * Tide bobber could never be linked to the player and every cast would produce an orphan.</p>
     */
    public static void clearStaleLink(Player player, boolean clientSide) {
        FishingHook link = player.fishing;
        if (link == null) {
            return;
        }
        if (link instanceof HookAccessor) {
            if (!isLiveLink(link)) {
                player.fishing = null;
            }
            return;
        }
        if (!clientSide) {
            player.fishing = null;
        }
    }

    /**
     * Re-links an orphaned Bobber to its owner.
     *
     * @return {@code true} when a new link was created
     */
    public static boolean ensureLinked(TideFishingHook hook, boolean warn) {
        Player owner = hook.getPlayerOwner();
        if (owner == null || hook.isRemoved()) {
            return false;
        }

        FishingHook link = owner.fishing;
        TideFishingHook linked = linkedHook(link);
        if (linked == hook) {
            return false;
        }
        if (linked != null && !linked.isRemoved()) {
            // Another live Tide bobber owns the player; do not steal it.
            return false;
        }

        owner.fishing = new HookAccessor(hook, hook.level());
        if (warn) {
            Tide.LOG.warn(
                    "Restored the missing owner link for Tide fishing bobber {}. Player.fishing was"
                            + " cleared or replaced while the bobber was still alive; the bobber can"
                            + " now be reeled in normally.",
                    hook.getUUID()
            );
        }
        return true;
    }
}
