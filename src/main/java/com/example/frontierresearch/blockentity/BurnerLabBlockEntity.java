package com.example.frontierresearch.blockentity;

import com.example.frontierresearch.block.BurnerLabBlock;
import com.example.frontierresearch.menu.BurnerLabMenu;
import com.example.frontierresearch.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

/**
 * Block entity for the {@link com.example.frontierresearch.block.BurnerLabBlock}.
 *
 * <p>The Burner Lab has 1 fuel slot and 5 science-pack input slots.  It burns
 * fuel to generate research ticks, consuming one tick of fuel per server game
 * tick while active research is in progress.</p>
 *
 * <p>Slot layout:</p>
 * <ul>
 *   <li>Slot 0 – fuel</li>
 *   <li>Slots 1–5 – science pack inputs</li>
 * </ul>
 */
public class BurnerLabBlockEntity extends BaseContainerBlockEntity {

    public static final int FUEL_SLOT = 0;
    public static final int PACK_SLOT_START = 1;
    public static final int PACK_SLOT_COUNT = 5;
    public static final int SLOT_COUNT = 1 + PACK_SLOT_COUNT;

    private static final String TAG_FUEL_TIME = "FuelTime";
    private static final String TAG_FUEL_TIME_TOTAL = "FuelTimeTotal";

    /** Remaining burn time in ticks for the current fuel item. */
    private int fuelTime = 0;
    /** Total burn time of the last fuel item consumed (for progress bar). */
    private int fuelTimeTotal = 1;

    private final ItemStackHandler itemHandler = new ItemStackHandler(SLOT_COUNT) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            if (slot == FUEL_SLOT) {
                return net.minecraft.world.item.crafting.RecipeType.SMELTING != null
                        && stack.getBurnTime(RecipeType.SMELTING) > 0;
            }
            return super.isItemValid(slot, stack);
        }
    };

    public BurnerLabBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BURNER_LAB.get(), pos, state);
    }

    // ------------------------------------------------------------------
    // Ticker (server-side)
    // ------------------------------------------------------------------

    public static void tick(Level level, BlockPos pos, BlockState state,
                            BurnerLabBlockEntity be) {
        if (level.isClientSide()) return;

        boolean wasLit = state.getValue(BurnerLabBlock.LIT);
        boolean hasActiveResearch = false;

        // Check if any player with this lab open has active research
        for (Player player : level.players()) {
            if (player instanceof ServerPlayer serverPlayer
                    && serverPlayer.containerMenu instanceof BurnerLabMenu menu
                    && menu.getBlockPos().equals(pos)) {
                var data = com.example.frontierresearch.research.ResearchManager.getData(serverPlayer);
                if (data.getActiveResearch() != null) {
                    hasActiveResearch = true;
                    break;
                }
            }
        }

        boolean isLit = false;

        if (hasActiveResearch) {
            if (be.fuelTime > 0) {
                be.fuelTime--;
                isLit = true;
                // Advance research for all players with this lab open
                for (Player player : level.players()) {
                    if (player instanceof ServerPlayer serverPlayer
                            && serverPlayer.containerMenu instanceof BurnerLabMenu menu
                            && menu.getBlockPos().equals(pos)) {
                        com.example.frontierresearch.research.ResearchManager.tickResearch(serverPlayer);
                    }
                }
            } else {
                // Try to consume a new fuel item
                ItemStack fuelStack = be.itemHandler.getStackInSlot(FUEL_SLOT);
                if (!fuelStack.isEmpty()) {
                    int burnTime = fuelStack.getBurnTime(RecipeType.SMELTING);
                    if (burnTime > 0) {
                        be.fuelTimeTotal = burnTime;
                        be.fuelTime = burnTime - 1;
                        be.itemHandler.extractItem(FUEL_SLOT, 1, false);
                        isLit = true;
                        be.setChanged();
                    }
                }
            }
        }

        if (wasLit != isLit) {
            level.setBlockAndUpdate(pos, state.setValue(BurnerLabBlock.LIT, isLit));
            be.setChanged();
        }
    }

    // ------------------------------------------------------------------
    // Container interface delegation
    // ------------------------------------------------------------------

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            if (!itemHandler.getStackInSlot(i).isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return itemHandler.getStackInSlot(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        return itemHandler.extractItem(slot, amount, false);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack stack = itemHandler.getStackInSlot(slot).copy();
        itemHandler.setStackInSlot(slot, ItemStack.EMPTY);
        return stack;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        itemHandler.setStackInSlot(slot, stack);
        if (!stack.isEmpty() && stack.getCount() > getMaxStackSize()) {
            stack.setCount(getMaxStackSize());
        }
        setChanged();
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            itemHandler.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    // ------------------------------------------------------------------
    // Accessors
    // ------------------------------------------------------------------

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    /** Fuel burn progress as a fraction 0.0–1.0 (1.0 = full, 0.0 = empty). */
    public float getFuelProgress() {
        return fuelTimeTotal > 0 ? (float) fuelTime / fuelTimeTotal : 0f;
    }

    public int getFuelTime() {
        return fuelTime;
    }

    public int getFuelTimeTotal() {
        return fuelTimeTotal;
    }

    // ------------------------------------------------------------------
    // BaseContainerBlockEntity / MenuProvider
    // ------------------------------------------------------------------

    @Override
    protected Component getDefaultName() {
        return BurnerLabBlock.TITLE;
    }

    @Override
    protected AbstractContainerMenu createMenu(int windowId, Inventory inventory) {
        return new BurnerLabMenu(windowId, inventory, getBlockPos());
    }

    @Override
    public int getContainerSize() {
        return SLOT_COUNT;
    }

    // ------------------------------------------------------------------
    // NBT
    // ------------------------------------------------------------------

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Items", itemHandler.serializeNBT(registries));
        tag.putInt(TAG_FUEL_TIME, fuelTime);
        tag.putInt(TAG_FUEL_TIME_TOTAL, fuelTimeTotal);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("Items")) {
            itemHandler.deserializeNBT(registries, tag.getCompound("Items"));
        }
        fuelTime = tag.getInt(TAG_FUEL_TIME);
        fuelTimeTotal = tag.contains(TAG_FUEL_TIME_TOTAL) ? tag.getInt(TAG_FUEL_TIME_TOTAL) : 1;
    }
}
