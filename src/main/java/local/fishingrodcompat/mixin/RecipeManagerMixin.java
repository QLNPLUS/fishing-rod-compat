package local.fishingrodcompat.mixin;

import com.google.gson.JsonElement;
import local.fishingrodcompat.api.FishingRodCompat;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.RecipeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(RecipeManager.class)
public abstract class RecipeManagerMixin {
    @Inject(
            method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V",
            at = @At("RETURN")
    )
    private void fishingRodCompat$removeDisabledRecipes(
            Map<ResourceLocation, JsonElement> recipes,
            ResourceManager resourceManager,
            ProfilerFiller profiler,
            CallbackInfo callbackInfo
    ) {
        RecipeManager manager = (RecipeManager) (Object) this;
        manager.replaceRecipes(manager.getRecipes().stream()
                .filter(recipe -> !FishingRodCompat.isDisabledAquacultureRecipe(recipe.getId()))
                .toList());
    }
}
