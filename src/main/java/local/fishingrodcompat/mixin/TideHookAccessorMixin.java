package local.fishingrodcompat.mixin;

import com.li64.tide.registries.entities.misc.fishing.HookAccessor;
import com.li64.tide.registries.entities.misc.fishing.TideFishingHook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/** Exposes Tide's wrapped hook to the vanilla movement bridge. */
@Mixin(value = HookAccessor.class, remap = false)
public interface TideHookAccessorMixin {
    @Accessor("hook")
    TideFishingHook fishingRodCompat$getTideHook();
}
