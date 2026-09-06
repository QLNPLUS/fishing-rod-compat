package local.fishingrodcompat.mixin;

import com.teammetallurgy.aquaculture.block.blockentity.TackleBoxBlockEntity;
import com.teammetallurgy.aquaculture.inventory.container.TackleBoxContainer;
import com.teammetallurgy.aquaculture.inventory.container.slot.SlotFishingRod;
import local.fishingrodcompat.adapter.TackleBoxHooks;
import local.fishingrodcompat.adapter.TideRodEquipmentHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.items.SlotItemHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = TackleBoxContainer.class, remap = false)
public abstract class TackleBoxContainerMixin extends AbstractContainerMenu {
    @Shadow(remap = false) public TackleBoxBlockEntity tackleBox;

    protected TackleBoxContainerMixin(net.minecraft.world.inventory.MenuType<?> menuType, int containerId) {
        super(menuType, containerId);
    }

    @Inject(method = "<init>", at = @At("RETURN"), remap = false)
    private void fishingRodCompat$bindOwner(
            int containerId,
            BlockPos pos,
            Inventory inventory,
            CallbackInfo callbackInfo
    ) {
        if (tackleBox != null && getSlot(0) instanceof SlotFishingRod rodSlot) {
            TackleBoxHooks.bind(rodSlot, tackleBox);
        }
    }

    @Redirect(
            method = "m_150399_",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraftforge/items/SlotItemHandler;m_5852_(Lnet/minecraft/world/item/ItemStack;)V"
            ),
            remap = false
    )
    private void fishingRodCompat$preserveBaitOnSwap(SlotItemHandler slot, net.minecraft.world.item.ItemStack stack) {
        if (!(slot.getItemHandler() instanceof TideRodEquipmentHandler)) {
            slot.set(stack);
        }
    }
}
