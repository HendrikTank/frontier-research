package com.example.frontierresearch.menu;

import com.example.frontierresearch.blockentity.BurnerLabBlockEntity;
import com.example.frontierresearch.registry.ModBlockEntities;
import com.example.frontierresearch.registry.ModMenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

/**
 * Container menu for the Burner Lab block entity.
 *
 * <p>Layout:</p>
 * <ul>
 *   <li>Slot 0 – fuel input</li>
 *   <li>Slots 1–5 – science pack inputs</li>
 *   <li>Slots 6–32 – player inventory (3×9)</li>
 *   <li>Slots 33–41 – player hotbar (1×9)</li>
 * </ul>
 *
 * <p>Container data indices:</p>
 * <ul>
 *   <li>[0] – {@code fuelTime} remaining ticks</li>
 *   <li>[1] – {@code fuelTimeTotal} max ticks</li>
 * </ul>
 */
public class BurnerLabMenu extends AbstractContainerMenu {

    /** Number of container data values synced to the client. */
    private static final int DATA_SIZE = 2;
    private static final int DATA_FUEL_TIME = 0;
    private static final int DATA_FUEL_TIME_TOTAL = 1;

    private final BlockPos blockPos;
    private final BurnerLabBlockEntity blockEntity;
    private final ContainerData containerData;

    /** Server-side constructor. */
    public BurnerLabMenu(int windowId, Inventory playerInventory, BlockPos pos) {
        super(ModMenuTypes.BURNER_LAB.get(), windowId);
        this.blockPos = pos;
        Level level = playerInventory.player.level();
        this.blockEntity = level.getBlockEntity(pos) instanceof BurnerLabBlockEntity be ? be : null;

        IItemHandler handler = blockEntity != null
                ? blockEntity.getItemHandler()
                : new net.neoforged.neoforge.items.ItemStackHandler(BurnerLabBlockEntity.SLOT_COUNT);

        if (blockEntity != null) {
            this.containerData = new ContainerData() {
                @Override
                public int get(int index) {
                    return switch (index) {
                        case DATA_FUEL_TIME -> blockEntity.getFuelTime();
                        case DATA_FUEL_TIME_TOTAL -> blockEntity.getFuelTimeTotal();
                        default -> 0;
                    };
                }

                @Override
                public void set(int index, int value) { /* read-only on server */ }

                @Override
                public int getCount() {
                    return DATA_SIZE;
                }
            };
        } else {
            this.containerData = new SimpleContainerData(DATA_SIZE);
        }

        addDataSlots(containerData);

        // Fuel slot
        addSlot(new SlotItemHandler(handler, BurnerLabBlockEntity.FUEL_SLOT, 8, 53));

        // Science pack input slots
        for (int i = 0; i < BurnerLabBlockEntity.PACK_SLOT_COUNT; i++) {
            addSlot(new SlotItemHandler(handler, BurnerLabBlockEntity.PACK_SLOT_START + i,
                    62 + i * 18, 17));
        }

        // Player inventory (3 rows)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(playerInventory, col + row * 9 + 9,
                        8 + col * 18, 84 + row * 18));
            }
        }
        // Player hotbar
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            result = stack.copy();
            int containerSlots = BurnerLabBlockEntity.SLOT_COUNT;
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
                || BurnerLabBlockEntity.stillValid(
                net.minecraft.world.inventory.ContainerLevelAccess.create(
                        player.level(), blockPos),
                player,
                com.example.frontierresearch.registry.ModBlocks.BURNER_LAB.get());
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    /** Returns remaining fuel ticks (synced to client via container data). */
    public int getFuelTime() {
        return containerData.get(DATA_FUEL_TIME);
    }

    /** Returns total fuel ticks of the last consumed fuel item. */
    public int getFuelTimeTotal() {
        return containerData.get(DATA_FUEL_TIME_TOTAL);
    }

    /** Returns fuel progress as 0.0–1.0 (1.0 = full fuel remaining). */
    public float getFuelProgress() {
        int total = getFuelTimeTotal();
        return total > 0 ? (float) getFuelTime() / total : 0f;
    }
}
