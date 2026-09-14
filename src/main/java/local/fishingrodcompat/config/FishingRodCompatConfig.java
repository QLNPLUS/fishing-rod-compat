package local.fishingrodcompat.config;

import net.minecraftforge.common.ForgeConfigSpec;
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
    public static final ForgeConfigSpec SPEC;
    private static final Fixes FIXES;

    static {
        Pair<Fixes, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder().configure(Fixes::new);
        FIXES = pair.getLeft();
        SPEC = pair.getRight();
    }

    private FishingRodCompatConfig() {
    }

    public static boolean stardewHookCrashGuard() {
        return FIXES.stardewHookCrashGuard.get();
    }

    public static final class Fixes {
        public final ForgeConfigSpec.BooleanValue stardewHookCrashGuard;

        private Fixes(ForgeConfigSpec.Builder builder) {
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

            builder.pop();
        }
    }
}
