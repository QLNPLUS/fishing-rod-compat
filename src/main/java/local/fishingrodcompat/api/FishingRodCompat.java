package local.fishingrodcompat.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import local.fishingrodcompat.adapter.TideRodEquipmentHandler;
import com.teammetallurgy.aquaculture.init.AquaItems;
import com.li64.tide.data.rods.CustomRodManager;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import com.li64.tide.registries.items.TideFishingRodItem;
import local.fishingrodcompat.item.AquaStyledTideFishingRodItem;
import net.minecraft.world.phys.Vec3;

import java.util.Set;

public final class FishingRodCompat {
    public static final String AQUACULTURE_HOOK_PREFIX = "aquaculture_";

    /** Tide uses a nonlinear lure-speed scale, so Neptunium gets a small native-scale bonus. */
    public static final int NEPTUNIUM_LURE_SPEED_BONUS = 1;


    private static final Set<String> DISABLED_AQUACULTURE_RECIPE_PATHS = Set.of(
            "iron_fishing_rod",
            "golden_fishing_rod",
            "gold_fishing_rod",
            "diamond_fishing_rod",
            "neptunium_fishing_rod",
            "fishing_line",
            "bobber",
            "iron_hook",
            "gold_hook",
            "diamond_hook",
            "light_hook",
            "heavy_hook",
            "double_hook",
            "redstone_hook",
            "note_hook",
            "nether_star_hook"
    );

    private static final Set<String> DISABLED_AQUACULTURE_ITEM_IDS = Set.of(
            "aquaculture:iron_fishing_rod",
            "aquaculture:gold_fishing_rod",
            "aquaculture:diamond_fishing_rod",
            "aquaculture:neptunium_fishing_rod",
            "aquaculture:fishing_line",
            "aquaculture:bobber",
            "aquaculture:iron_hook",
            "aquaculture:gold_hook",
            "aquaculture:diamond_hook",
            "aquaculture:light_hook",
            "aquaculture:heavy_hook",
            "aquaculture:double_hook",
            "aquaculture:redstone_hook",
            "aquaculture:note_hook",
            "aquaculture:nether_star_hook"
    );

    private FishingRodCompat() {
    }

    public static boolean isAquaTideRod(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() instanceof AquaStyledTideFishingRodItem;
    }

    public static boolean isTideRod(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() instanceof TideFishingRodItem;
    }

    public static boolean isAquaTideNeptuniumRod(ItemStack stack) {
        return isAquaTideRod(stack)
                && stack.getItem() == local.fishingrodcompat.registry.CompatItems.AQUACULTURE_NEPTUNIUM_FISHING_ROD.get();
    }

    public static boolean isAquacultureWorm(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() == AquaItems.WORM.get();
    }

    public static boolean isAquaTideHookItem(ItemStack stack, String hookName) {
        if (stack.isEmpty()) {
            return false;
        }
        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return itemId.getNamespace().equals(local.fishingrodcompat.FishingRodCompatMod.MOD_ID)
                && itemId.getPath().equals(AQUACULTURE_HOOK_PREFIX + hookName + "_hook");
    }

    public static boolean hasAquaTideHook(ItemStack rod, String hookName) {
        return !rod.isEmpty() && isAquaTideHookItem(CustomRodManager.getHook(rod), hookName);
    }

    public static int getAquaTideHookLuckBonus(ItemStack rod) {
        return hasAquaTideHook(rod, "gold") || hasAquaTideHook(rod, "nether_star") ? 1 : 0;
    }

    public static double getAquaTideHookDurabilityChance(ItemStack rod) {
        if (hasAquaTideHook(rod, "iron")) {
            return 0.2D;
        }
        if (hasAquaTideHook(rod, "diamond") || hasAquaTideHook(rod, "nether_star")) {
            return 0.5D;
        }
        return 0.0D;
    }

    public static Vec3 getAquaTideHookWeight(ItemStack rod) {
        if (hasAquaTideHook(rod, "light")) {
            return new Vec3(1.5D, 1.0D, 1.5D);
        }
        if (hasAquaTideHook(rod, "heavy")) {
            return new Vec3(0.6D, 0.15D, 0.6D);
        }
        return null;
    }

    public static boolean isDisabledAquacultureRod(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        return stack.getItem() == AquaItems.IRON_FISHING_ROD.get()
                || stack.getItem() == AquaItems.GOLD_FISHING_ROD.get()
                || stack.getItem() == AquaItems.DIAMOND_FISHING_ROD.get()
                || stack.getItem() == AquaItems.NEPTUNIUM_FISHING_ROD.get();
    }

    public static boolean isDisabledAquacultureItem(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        return isDisabledAquacultureRod(stack)
                || stack.getItem() == AquaItems.FISHING_LINE.get()
                || stack.getItem() == AquaItems.BOBBER.get()
                || stack.getItem() == AquaItems.IRON_HOOK.get()
                || stack.getItem() == AquaItems.GOLD_HOOK.get()
                || stack.getItem() == AquaItems.DIAMOND_HOOK.get()
                || stack.getItem() == AquaItems.LIGHT_HOOK.get()
                || stack.getItem() == AquaItems.HEAVY_HOOK.get()
                || stack.getItem() == AquaItems.DOUBLE_HOOK.get()
                || stack.getItem() == AquaItems.REDSTONE_HOOK.get()
                || stack.getItem() == AquaItems.NOTE_HOOK.get()
                || stack.getItem() == AquaItems.NETHER_STAR_HOOK.get();
    }

    public static boolean isDisabledAquacultureRecipe(ResourceLocation id, JsonElement recipe) {
        if (!id.getNamespace().equals("aquaculture")) {
            return false;
        }
        if (DISABLED_AQUACULTURE_RECIPE_PATHS.contains(id.getPath())) {
            return true;
        }
        if (recipe == null || !recipe.isJsonObject()) {
            return false;
        }

        JsonObject result = recipe.getAsJsonObject().getAsJsonObject("result");
        if (result == null) {
            return false;
        }

        for (String key : new String[]{"id", "item"}) {
            JsonElement item = result.get(key);
            if (item != null && item.isJsonPrimitive()
                    && item.getAsJsonPrimitive().isString()
                    && DISABLED_AQUACULTURE_ITEM_IDS.contains(item.getAsString())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isTideHandler(IItemHandler handler) {
        return handler instanceof TideRodEquipmentHandler;
    }

    public static boolean isTideAccessory(RodAccessorySlot slot, ItemStack stack) {
        return local.fishingrodcompat.adapter.TideRodAccessoryBridge.INSTANCE.accepts(slot, stack);
    }

    public static boolean isTideTackle(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }

        return isTideRod(stack)
                || isTideAccessory(RodAccessorySlot.HOOK, stack)
                || isTideAccessory(RodAccessorySlot.BAIT, stack)
                || isTideAccessory(RodAccessorySlot.LINE, stack)
                || isTideAccessory(RodAccessorySlot.BOBBER, stack);
    }

    /** Kept as a source-compatible alias for earlier versions of the bridge API. */
    @Deprecated
    public static boolean isAquaTideTackle(ItemStack stack) {
        return isTideTackle(stack);
    }

    public static TideRodEquipmentHandler createTideHandler(
            ItemStack rod,
            Runnable onChanged
    ) {
        return new TideRodEquipmentHandler(rod, onChanged);
    }
}
