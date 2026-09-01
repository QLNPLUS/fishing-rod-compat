package local.fishingrodcompat.api;

public enum RodAccessorySlot {
    HOOK(0),
    BAIT(1),
    LINE(2),
    BOBBER(3);

    private final int index;

    RodAccessorySlot(int index) {
        this.index = index;
    }

    public int index() {
        return index;
    }

    public static RodAccessorySlot fromIndex(int index) {
        return switch (index) {
            case 0 -> HOOK;
            case 1 -> BAIT;
            case 2 -> LINE;
            case 3 -> BOBBER;
            default -> throw new IllegalArgumentException("Unsupported rod accessory slot: " + index);
        };
    }
}
