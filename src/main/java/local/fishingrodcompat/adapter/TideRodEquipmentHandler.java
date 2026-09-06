package local.fishingrodcompat.adapter;

import local.fishingrodcompat.api.RodAccessoryBridge;
import local.fishingrodcompat.api.RodAccessorySlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public final class TideRodEquipmentHandler extends ItemStackHandler {
    private final ItemStack rod;
    private final RodAccessoryBridge bridge;
    private final Runnable onChanged;

    public TideRodEquipmentHandler(ItemStack rod, Runnable onChanged) {
        this(rod, TideRodAccessoryBridge.INSTANCE, onChanged);
    }

    public TideRodEquipmentHandler(ItemStack rod, RodAccessoryBridge bridge, Runnable onChanged) {
        super(4);
        this.rod = rod;
        this.bridge = bridge;
        this.onChanged = onChanged;

        for (RodAccessorySlot slot : RodAccessorySlot.values()) {
            stacks.set(slot.index(), bridge.read(rod, slot));
        }
    }

    @Override
    public int getSlotLimit(int slot) {
        return bridge.getSlotLimit(RodAccessorySlot.fromIndex(slot));
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        if (slot < 0 || slot >= getSlots()) {
            return false;
        }
        return bridge.accepts(RodAccessorySlot.fromIndex(slot), stack);
    }

    @Override
    protected void onContentsChanged(int slot) {
        if (slot >= 0 && slot < getSlots()) {
            bridge.write(rod, RodAccessorySlot.fromIndex(slot), getStackInSlot(slot));
            onChanged.run();
        }
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (slot < 0 || slot >= getSlots()) {
            return stack.copy();
        }
        ItemStack remainder = bridge.insert(rod, RodAccessorySlot.fromIndex(slot), stack, simulate);
        if (!simulate && !ItemStack.matches(remainder, stack)) {
            syncSlot(slot);
            onChanged.run();
        }
        return remainder;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (slot < 0 || slot >= getSlots()) {
            return ItemStack.EMPTY;
        }
        ItemStack extracted = bridge.extract(rod, RodAccessorySlot.fromIndex(slot), amount, simulate);
        if (!simulate && !extracted.isEmpty()) {
            syncSlot(slot);
            onChanged.run();
        }
        return extracted;
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        if (slot < 0 || slot >= getSlots()) {
            throw new RuntimeException("Slot " + slot + " not in valid range - [0, " + getSlots() + ")");
        }
        if (slot == RodAccessorySlot.BAIT.index()) {
            ItemStack normalized = stack.isEmpty()
                    ? ItemStack.EMPTY
                    : stack.copyWithCount(Math.min(stack.getCount(),
                    bridge.getStackLimit(RodAccessorySlot.BAIT, stack)));
            stacks.set(slot, normalized);
            bridge.write(rod, RodAccessorySlot.BAIT, normalized);
            syncSlot(slot);
            onChanged.run();
            return;
        }
        super.setStackInSlot(slot, stack);
    }

    private void syncSlot(int slot) {
        stacks.set(slot, bridge.read(rod, RodAccessorySlot.fromIndex(slot)));
    }

    public void markChanged() {
        onChanged.run();
    }
}
