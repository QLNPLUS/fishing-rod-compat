package local.fishingrodcompat.mixin;

import com.li64.tide.data.rods.BaitContents;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(value = BaitContents.Mutable.class, remap = false)
public interface BaitContentsMutableAccessor {
    @Accessor("items")
    List<ItemStack> fishingRodCompat$getItems();
}
