package local.fishingrodcompat.compat;

import com.bonker.stardewfishing.server.event.StardewMinigameEndedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class StardewFishingCompat {
    public static final StardewFishingCompat INSTANCE = new StardewFishingCompat();

    private StardewFishingCompat() {
    }

    @SubscribeEvent
    public void onMinigameEnded(StardewMinigameEndedEvent event) {
        if (event.wasSuccessful()) {
            TideBaitConsumption.consume(event.getFishingRod());
        }
    }
}
