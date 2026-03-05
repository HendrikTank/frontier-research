package com.example.frontierresearch.menu;

import com.example.frontierresearch.blockentity.ResearchTableBlockEntity;
import com.example.frontierresearch.registry.ModBlockEntities;
import com.example.frontierresearch.registry.ModMenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

/**
 * Container menu for the Research Table block entity.
 *
 * <p>Layout (top to bottom):</p>
 * <ul>
 *   <li>Row 0, slots 0–4 – science pack input slots</li>
 *   <li>Rows 1–3, slots 5–31 – player inventory (3×9)</li>
 *   <li>Row 4, slots 32–40 – player hotbar (1×9)</li>
 * </ul>
 */
public class ResearchTableMenu extends AbstractContainerMenu {

    private final BlockPos blockPos;
    private final ResearchTableBlockEntity blockEntity;

    /** Server-side constructor (opened via block interaction). */
    public ResearchTableMenu(int windowId, Inventory playerInventory, BlockPos pos) {
        super(ModMenuTypes.RESEARCH_TABLE.get(), windowId);
        this.blockPos = pos;
        Level level = playerInventory.player.level();
        this.blockEntity = level.getBlockEntity(pos) instanceof ResearchTableBlockEntity be ? be : null;

        IItemHandler handler = blockEntity != null
                ? blockEntity.getItemHandler()
                : new net.neoforged.neoforge.items.ItemStackHandler(ResearchTableBlockEntity.SLOT_COUNT);

        // Science pack input slots (top row, centred)
        for (int i = 0; i < ResearchTableBlockEntity.SLOT_COUNT; i++) {
            addSlot(new SlotItemHandler(handler, i, 8 + i * 18, 20));
        }

        // Player inventory (3 rows)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(playerInventory, col + row * 9 + 9,
                        8 + col * 18, 58 + row * 18));
            }
        }
        // Player hotbar
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(playerInventory, col, 8 + col * 18, 116));
        }
    }

    /** Client-side constructor (packet-driven). */
    public ResearchTableMenu(int windowId, Inventory playerInventory, net.minecraft.core.BlockPos pos) {
        this(windowId, playerInventory, (BlockPos) pos);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            result = stack.copy();
            int containerSlots = ResearchTableBlockEntity.SLOT_COUNT;
            if (index < containerSlots) {
                if (!moveItemStackTo(stack, containerSlots, slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!moveItemStackTo(stack, 0, containerSlots, false)) {
                    return ItemStack.EMPTY;
                }
            }
            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return result;
    }

    @Override
    public boolean stillValid(Player player) {
        return blockEntity == null
                || ResearchTableBlockEntity.stillValid(
                net.minecraft.world.inventory.ContainerLevelAccess.create(
                        player.level(), blockPos),
                player,
                com.example.frontierresearch.registry.ModBlocks.RESEARCH_TABLE.get());
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }
}
