package local.fishingrodcompat.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Common config for Fishing Rod Compat.
 *
 * <p>Every entry under {@code fixes} guards one compatibility patch that exists only because an
 * upstream mod is currently broken. When upstream fixes the underlying problem, set the matching
 * entry to {@code false}: the patch then falls through to the original upstream call, so this mod
 * can stay installed across an upstream upgrade without changing upstream behaviour.</p>
 */
public final class FishingRodCompatConfig {
    public static final ModConfigSpec SPEC;
    private static final Fixes FIXES;

    static {
        Pair<Fixes, ModConfigSpec> pair = new ModConfigSpec.Builder().configure(Fixes::new);
        FIXES = pair.getLeft();
        SPEC = pair.getRight();
    }

    private FishingRodCompatConfig() {
    }

    public static boolean stardewHookCrashGuard() {
        return FIXES.stardewHookCrashGuard.get();
    }

    public static boolean tideHookLinkRecovery() {
        return FIXES.tideHookLinkRecovery.get();
    }

    public static final class Fixes {
        public final ModConfigSpec.BooleanValue stardewHookCrashGuard;
        public final ModConfigSpec.BooleanValue tideHookLinkRecovery;

        private Fixes(ModConfigSpec.Builder builder) {
            builder.comment(
                    "Compatibility patches for upstream bugs.",
                    "Set an entry to false once the upstream mod fixes it, to restore that mod's",
                    "own behaviour."
            ).push("fixes");

            stardewHookCrashGuard = builder
                    .comment(
                            "Tide 2.1.1 x Stardew Fishing: TideFishingHook.catchingFish() hands the",
                            "player's fishing hook to Stardew Fishing without checking it, so a broken",
                            "hook link throws a NullPointerException while the bobber entity ticks and",
                            "crashes the world save.",
                            "",
                            "true  - skip the Stardew reward lookup and log a warning instead of crashing",
                            "false - call Tide exactly as it ships, i.e. original upstream behaviour",
                            "",
                            "Set to false if a newer Tide or Stardew Fishing release fixes this itself."
                    )
                    .define("stardew_hook_crash_guard", true);

            tideHookLinkRecovery = builder
                    .comment(
                            "Tide 2.1.1: the active bobber is stored in Player.fishing, a single field",
                            "shared by vanilla and every fishing mod. If the field is cleared or taken",
                            "over while the Tide bobber is still alive, the bobber becomes unreachable:",
                            "the rod reels in nothing, new casts cannot link to the player, and the",
                            "stray bobber floats forever until the player relogs.",
                            "",
                            "true  - re-link a live bobber whose owner link went missing, and ignore a",
                            "        foreign hook sitting in that slot instead of failing to link",
                            "false - leave the state exactly as Tide leaves it",
                            "",
                            "Keep this enabled when a fishing-automation or third-party fishing mod",
                            "manages Player.fishing directly.",
                            "",
                            "Set to false if a newer Tide release manages the owner link itself."
                    )
                    .define("tide_hook_link_recovery", true);

            builder.pop();
        }
    }
}
