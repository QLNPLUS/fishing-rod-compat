package local.fishingrodcompat.compat;

import com.li64.tide.data.item.TideItemData;
import com.li64.tide.data.rods.BaitContents;
import local.fishingrodcompat.api.FishingRodCompat;
import local.fishingrodcompat.mixin.BaitContentsMutableAccessor;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.ListIterator;

public final class TideBaitConsumption {
    private TideBaitConsumption() {
    }

    public static void consume(ItemStack rod) {
        if (!FishingRodCompat.isTideRod(rod)) {
            return;
        }

        BaitContents current = TideItemData.BAIT_CONTENTS.getOrDefault(rod, new BaitContents());
        BaitContents.Mutable mutable = new BaitContents.Mutable(current);
        consume(mutable);
        TideItemData.BAIT_CONTENTS.set(rod, mutable.toImmutable());
    }

    public static void consume(BaitContents.Mutable mutable) {
        List<ItemStack> items = ((BaitContentsMutableAccessor) (Object) mutable).fishingRodCompat$getItems();
        for (ListIterator<ItemStack> iterator = items.listIterator(); iterator.hasNext(); ) {
            ItemStack bait = iterator.next();
            if (FishingRodCompat.isAquacultureWorm(bait)) {
                int maxDamage = bait.getMaxDamage();
                if (maxDamage > 0) {
                    // Aquaculture's worm has max durability 20 despite exposing a stack size of 64.
                    bait.setCount(1);
                    int nextDamage = bait.getDamageValue() + 1;
                    if (nextDamage >= maxDamage) {
                        iterator.remove();
                    } else {
                        bait.setDamageValue(nextDamage);
                    }
                } else {
                    bait.shrink(1);
                    if (bait.isEmpty()) {
                        iterator.remove();
                    }
                }
            } else {
                bait.shrink(1);
                if (bait.isEmpty()) {
                    iterator.remove();
                }
            }
        }
    }
}
