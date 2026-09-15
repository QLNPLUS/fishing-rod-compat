package local.fishingrodcompat.client;

import com.mojang.logging.LogUtils;
import com.li64.tide.registries.entities.misc.fishing.HookAccessor;
import local.fishingrodcompat.api.FishingRodCompat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import org.slf4j.Logger;

import java.lang.reflect.Field;

/** Requests AutoFish's normal reel/recast cycle when a gameplay screen opens. */
final class AutoFishScreenCycle {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Target[] TARGETS = {
            new Target(
                    "in.northwestw.autofish.handler.AutoFishHandler",
                    "in.northwestw.autofish.config.Config",
                    "autoFish"
            ),
            new Target(
                    "ml.northwestwind.forgeautofish.handler.AutoFishHandler",
                    null,
                    "autofish"
            )
    };

    private static boolean screenCycleArmed;
    private static Target resolvedTarget;
    private static boolean targetResolved;
    private static boolean reflectionWarningLogged;

    private AutoFishScreenCycle() {
    }

    static void onScreenOpening(ScreenEvent.Opening event) {
        Screen screen = event.getScreen();
        if (screen == null || screen.isPauseScreen() || screenCycleArmed) {
            return;
        }

        screenCycleArmed = true;
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null || minecraft.gameMode == null || !hasActiveTideHook(player)) {
            return;
        }

        requestReelIn();
    }

    static void onClientTick(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.screen == null) {
            screenCycleArmed = false;
        }
    }

    private static boolean hasActiveTideHook(LocalPlayer player) {
        if (!FishingRodCompat.isTideRod(player.getMainHandItem())
                && !FishingRodCompat.isTideRod(player.getOffhandItem())) {
            return false;
        }
        return HookAccessor.getHook(player) != null;
    }

    private static void requestReelIn() {
        Target target = findTarget();
        if (target == null) {
            return;
        }

        try {
            Class<?> handlerClass = load(target.handlerClassName);
            Class<?> enabledClass = target.enabledClassName == null
                    ? handlerClass
                    : load(target.enabledClassName);

            Field enabled = accessible(enabledClass.getDeclaredField(target.enabledFieldName));
            if (!enabled.getBoolean(null)) {
                return;
            }

            Field pendingReelIn = accessible(handlerClass.getDeclaredField("pendingReelIn"));
            Field pendingRecast = accessible(handlerClass.getDeclaredField("pendingRecast"));
            if (pendingRecast.getBoolean(null) || pendingReelIn.getBoolean(null)) {
                return;
            }

            pendingReelIn.setBoolean(null, true);
            accessible(handlerClass.getDeclaredField("tick")).setLong(null, 0L);
        } catch (ReflectiveOperationException | SecurityException exception) {
            logReflectionWarning(target, exception);
        }
    }

    private static Target findTarget() {
        if (targetResolved) {
            return resolvedTarget;
        }

        targetResolved = true;
        for (Target target : TARGETS) {
            try {
                load(target.handlerClassName);
                resolvedTarget = target;
                return target;
            } catch (ClassNotFoundException ignored) {
                // AutoFish is optional; the next target is the other loader's package name.
            }
        }
        return null;
    }

    private static Class<?> load(String name) throws ClassNotFoundException {
        return Class.forName(name, false, AutoFishScreenCycle.class.getClassLoader());
    }

    private static Field accessible(Field field) throws IllegalAccessException {
        if (!field.trySetAccessible()) {
            throw new IllegalAccessException("Could not access " + field);
        }
        return field;
    }

    private static void logReflectionWarning(Target target, Exception exception) {
        if (!reflectionWarningLogged) {
            reflectionWarningLogged = true;
            LOGGER.warn(
                    "AutoFish screen-cycle compatibility could not access {}. "
                            + "Automatic reel/recast on screen opening is disabled.",
                    target.handlerClassName,
                    exception
            );
        }
    }

    private record Target(String handlerClassName, String enabledClassName, String enabledFieldName) {
    }
}
