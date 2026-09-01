package local.fishingrodcompat.adapter;

import com.teammetallurgy.aquaculture.block.blockentity.TackleBoxBlockEntity;
import com.teammetallurgy.aquaculture.inventory.container.slot.SlotFishingRod;
import net.minecraft.world.item.ItemStack;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

public final class TackleBoxHooks {
    private static final Map<SlotFishingRod, TackleBoxBlockEntity> OWNERS =
            Collections.synchronizedMap(new WeakHashMap<>());

    private TackleBoxHooks() {
    }

    public static void bind(SlotFishingRod slot, TackleBoxBlockEntity blockEntity) {
        if (blockEntity != null) {
            OWNERS.put(slot, blockEntity);
        }
    }

    public static TideRodEquipmentHandler createTideHandler(SlotFishingRod slot, ItemStack rod) {
        return new TideRodEquipmentHandler(rod, () -> {
            TackleBoxBlockEntity blockEntity = OWNERS.get(slot);
            if (blockEntity != null) {
                blockEntity.setChanged();
            }
        });
    }
}
