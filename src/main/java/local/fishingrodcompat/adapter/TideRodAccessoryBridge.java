package local.fishingrodcompat.adapter;

import com.li64.tide.data.item.TideItemData;
import com.li64.tide.data.rods.BaitContents;
import com.li64.tide.data.rods.CustomRodManager;
import com.li64.tide.util.BaitUtils;
import local.fishingrodcompat.api.FishingRodCompat;
import local.fishingrodcompat.api.RodAccessoryBridge;
import local.fishingrodcompat.api.RodAccessorySlot;
import local.fishingrodcompat.registry.CompatItems;
import com.li64.tide.registries.items.TideFishingRodItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public final class TideRodAccessoryBridge implements RodAccessoryBridge {
    public static final TideRodAccessoryBridge INSTANCE = new TideRodAccessoryBridge();

    private static final TagKey<Item> HOOKS = tag("hooks");
    private static final TagKey<Item> BAITS = tag("bait_items");
    private static final TagKey<Item> LINES = tag("lines");
    private static final TagKey<Item> BOBBERS = tag("bobbers");
    private static final Field BAIT_SLOTS_FIELD = findBaitSlotsField();

    private TideRodAccessoryBridge() {
    }

    @Override
    public boolean accepts(RodAccessorySlot slot, ItemStack accessory) {
        if (accessory.isEmpty()) {
            return true;
        }

        return switch (slot) {
            case HOOK -> accessory.is(HOOKS);
            case BAIT -> accessory.is(BAITS) || BaitUtils.isBait(accessory);
            case LINE -> accessory.is(LINES);
            case BOBBER -> accessory.is(BOBBERS)
                    || accessory.getItem() == CompatItems.AQUACULTURE_BOBBER.get();
        };
    }

    @Override
    public int getSlotLimit(RodAccessorySlot slot) {
        return slot == RodAccessorySlot.BAIT ? 64 : 1;
    }

    @Override
    public int getStackLimit(RodAccessorySlot slot, ItemStack stack) {
        if (slot == RodAccessorySlot.BAIT && FishingRodCompat.isAquacultureWorm(stack)) {
            return 1;
        }
        return RodAccessoryBridge.super.getStackLimit(slot, stack);
    }

    @Override
    public ItemStack read(ItemStack rod, RodAccessorySlot slot) {
        return switch (slot) {
            case HOOK -> CustomRodManager.hasHook(rod) ? CustomRodManager.getHook(rod).copy() : ItemStack.EMPTY;
            case BAIT -> BaitUtils.getFirstBaitItem(rod).map(TideRodAccessoryBridge::normalizeBaitStack).orElse(ItemStack.EMPTY);
            case LINE -> CustomRodManager.hasLine(rod) ? CustomRodManager.getLine(rod).copy() : ItemStack.EMPTY;
            case BOBBER -> CustomRodManager.hasBobber(rod) ? CustomRodManager.getBobber(rod).copy() : ItemStack.EMPTY;
        };
    }

    @Override
    public void write(ItemStack rod, RodAccessorySlot slot, ItemStack accessory) {
        switch (slot) {
            case HOOK -> CustomRodManager.setHook(rod, accessory);
            case LINE -> CustomRodManager.setLine(rod, accessory);
            case BOBBER -> CustomRodManager.setBobber(rod, accessory);
            case BAIT -> writeFirstBait(rod, accessory);
        }
    }

    private static void writeFirstBait(ItemStack rod, ItemStack accessory) {
        List<ItemStack> items = getBaits(rod);
        int first = firstBaitIndex(items);
        if (accessory.isEmpty()) {
            if (first >= 0) {
                items.remove(first);
            }
        } else if (first >= 0) {
            items.set(first, normalizeBaitStack(accessory));
        } else if (items.size() < getBaitSlotCount(rod)) {
            items.add(normalizeBaitStack(accessory));
        }
        setBaits(rod, items);
    }

    @Override
    public ItemStack insert(ItemStack rod, RodAccessorySlot slot, ItemStack incoming, boolean simulate) {
        if (slot != RodAccessorySlot.BAIT) {
            return RodAccessoryBridge.super.insert(rod, slot, incoming, simulate);
        }
        if (incoming.isEmpty() || !accepts(slot, incoming)) {
            return incoming.copy();
        }

        List<ItemStack> items = getBaits(rod);
        int first = firstBaitIndex(items);
        if (FishingRodCompat.isAquacultureWorm(incoming)) {
            if (first >= 0 || items.size() >= getBaitSlotCount(rod)) {
                return incoming.copy();
            }

            if (!simulate) {
                items.add(incoming.copyWithCount(1));
                setBaits(rod, items);
            }
            return incoming.copyWithCount(incoming.getCount() - 1);
        }

        if (first >= 0) {
            ItemStack current = items.get(first);
            if (!ItemStack.isSameItemSameTags(current, incoming) || !current.isStackable()) {
                return incoming.copy();
            }

            int accepted = Math.min(incoming.getCount(),
                    Math.max(0, getStackLimit(slot, incoming) - current.getCount()));
            if (!simulate && accepted > 0) {
                items.set(first, current.copyWithCount(current.getCount() + accepted));
                setBaits(rod, items);
            }
            return incoming.copyWithCount(incoming.getCount() - accepted);
        }

        if (items.size() >= getBaitSlotCount(rod)) {
            return incoming.copy();
        }

        int accepted = Math.min(incoming.getCount(), getStackLimit(slot, incoming));
        if (!simulate && accepted > 0) {
            items.add(normalizeBaitStack(incoming.copyWithCount(accepted)));
            setBaits(rod, items);
        }
        return incoming.copyWithCount(incoming.getCount() - accepted);
    }

    @Override
    public ItemStack extract(ItemStack rod, RodAccessorySlot slot, int amount, boolean simulate) {
        if (slot != RodAccessorySlot.BAIT) {
            return RodAccessoryBridge.super.extract(rod, slot, amount, simulate);
        }
        if (amount <= 0) {
            return ItemStack.EMPTY;
        }

        List<ItemStack> items = getBaits(rod);
        int first = firstBaitIndex(items);
        if (first < 0) {
            return ItemStack.EMPTY;
        }

        ItemStack current = items.get(first);
        int extracted = Math.min(amount, current.getCount());
        if (!simulate) {
            if (extracted >= current.getCount()) {
                items.remove(first);
            } else {
                items.set(first, current.copyWithCount(current.getCount() - extracted));
            }
            setBaits(rod, items);
        }
        return current.copyWithCount(extracted);
    }

    private static List<ItemStack> getBaits(ItemStack rod) {
        BaitContents current = TideItemData.BAIT_CONTENTS.getOrDefault(rod, new BaitContents());
        List<ItemStack> items = new ArrayList<>(current.items().size());
        current.items().forEach(stack -> {
            if (stack != null && !stack.isEmpty()) {
                items.add(normalizeBaitStack(stack));
            }
        });
        return items;
    }

    private static void setBaits(ItemStack rod, List<ItemStack> items) {
        TideItemData.BAIT_CONTENTS.set(rod, new BaitContents(List.copyOf(items)));
    }

    private static ItemStack normalizeBaitStack(ItemStack bait) {
        if (bait.isEmpty()) {
            return ItemStack.EMPTY;
        }
        return FishingRodCompat.isAquacultureWorm(bait) ? bait.copyWithCount(1) : bait.copy();
    }

    private static int firstBaitIndex(List<ItemStack> items) {
        for (int index = 0; index < items.size(); index++) {
            if (!items.get(index).isEmpty()) {
                return index;
            }
        }
        return -1;
    }

    private static int getBaitSlotCount(ItemStack rod) {
        if (rod.getItem() instanceof TideFishingRodItem tideRod && BAIT_SLOTS_FIELD != null) {
            try {
                return Math.max(1, BAIT_SLOTS_FIELD.getInt(tideRod));
            } catch (IllegalAccessException ignored) {
                // Fall through to the conservative default if the field cannot be read.
            }
        }
        return 1;
    }

    private static Field findBaitSlotsField() {
        try {
            Field field = TideFishingRodItem.class.getDeclaredField("baitSlots");
            field.setAccessible(true);
            return field;
        } catch (ReflectiveOperationException ignored) {
            return null;
        }
    }

    private static TagKey<Item> tag(String path) {
        return TagKey.create(Registries.ITEM, new ResourceLocation("tide", path));
    }
}
