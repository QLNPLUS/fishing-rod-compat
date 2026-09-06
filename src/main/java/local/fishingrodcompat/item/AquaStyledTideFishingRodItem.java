package local.fishingrodcompat.item;

import com.li64.tide.registries.items.TideFishingRodItem;
import local.fishingrodcompat.api.FishingRodCompat;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * A Tide-native rod whose item model is supplied by Aquaculture.
 */
public final class AquaStyledTideFishingRodItem extends TideFishingRodItem {
    public AquaStyledTideFishingRodItem(int baitSlots, double durability, Item.Properties properties) {
        super(baitSlots, durability, properties);
    }

    @Override
    public void appendHoverText(
            ItemStack stack,
            Level level,
            List<Component> tooltip,
            TooltipFlag flag
    ) {
        super.appendHoverText(stack, level, tooltip, flag);
        if (FishingRodCompat.isAquaTideNeptuniumRod(stack)) {
            tooltip.add(Component.translatable("text.fishing_rod_compat.neptunium_bonus")
                    .withStyle(ChatFormatting.GOLD));
        }
    }
}
