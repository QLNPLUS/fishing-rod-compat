package local.fishingrodcompat.api;

import net.minecraft.world.item.ItemStack;

/**
 * Isolates a rod implementation's native accessory storage from the tackle-box UI.
 *
 * Future cross-mod accessory support belongs here: the UI and the handler should
 * continue to deal in the four logical slots while the bridge translates effects.
 */
public interface RodAccessoryBridge {
    boolean accepts(RodAccessorySlot slot, ItemStack accessory);

    ItemStack read(ItemStack rod, RodAccessorySlot slot);

    void write(ItemStack rod, RodAccessorySlot slot, ItemStack accessory);

    default int getSlotLimit(RodAccessorySlot slot) {
        return 1;
    }

    /**
     * Returns the maximum count for one item stack in a logical accessory slot.
     * This is separate from the number of logical slots so special items can
     * impose a stricter per-stack limit without changing the UI contract.
     */
    default int getStackLimit(RodAccessorySlot slot, ItemStack stack) {
        return Math.min(getSlotLimit(slot), stack.isEmpty() ? getSlotLimit(slot) : stack.getMaxStackSize());
    }

    default ItemStack insert(ItemStack rod, RodAccessorySlot slot, ItemStack incoming, boolean simulate) {
        if (incoming.isEmpty() || !accepts(slot, incoming)) {
            return incoming.copy();
        }

        ItemStack current = read(rod, slot);
        int limit = getStackLimit(slot, incoming);
        if (current.isEmpty()) {
            int accepted = Math.min(incoming.getCount(), limit);
            if (!simulate && accepted > 0) {
                write(rod, slot, incoming.copyWithCount(accepted));
            }
            return incoming.copyWithCount(incoming.getCount() - accepted);
        }

        if (!ItemStack.isSameItemSameComponents(current, incoming) || !current.isStackable()) {
            return incoming.copy();
        }

        int accepted = Math.min(incoming.getCount(), Math.max(0, limit - current.getCount()));
        if (!simulate && accepted > 0) {
            write(rod, slot, current.copyWithCount(current.getCount() + accepted));
        }
        return incoming.copyWithCount(incoming.getCount() - accepted);
    }

    default ItemStack extract(ItemStack rod, RodAccessorySlot slot, int amount, boolean simulate) {
        if (amount <= 0) {
            return ItemStack.EMPTY;
        }

        ItemStack current = read(rod, slot);
        if (current.isEmpty()) {
            return ItemStack.EMPTY;
        }

        int extracted = Math.min(amount, current.getCount());
        if (!simulate) {
            write(rod, slot, current.copyWithCount(current.getCount() - extracted));
        }
        return current.copyWithCount(extracted);
    }
}
