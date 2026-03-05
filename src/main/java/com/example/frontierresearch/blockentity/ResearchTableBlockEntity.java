package com.example.frontierresearch.blockentity;

import com.example.frontierresearch.block.ResearchTableBlock;
import com.example.frontierresearch.menu.ResearchTableMenu;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

/**
 * Block entity for the {@link com.example.frontierresearch.block.ResearchTableBlock}.
 *
 * <p>Holds 5 input slots for science packs.  Every server tick it attempts to
 * advance research for any player that has this block open as their active lab
 * (tracked via the menu). The Research Table does not require fuel.</p>
 */
public class ResearchTableBlockEntity extends BaseContainerBlockEntity {

    /** Number of science-pack input slots. */
    public static final int SLOT_COUNT = 5;

    private final ItemStackHandler itemHandler = new ItemStackHandler(SLOT_COUNT) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    public ResearchTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.RESEARCH_TABLE.get(), pos, state);
    }

    // ------------------------------------------------------------------
    // Ticker (server-side)
    // ------------------------------------------------------------------

    public static void tick(Level level, BlockPos pos, BlockState state,
                            ResearchTableBlockEntity be) {
        if (level.isClientSide()) return;
        // Advance research for all players who have this block entity open
        for (Player player : level.players()) {
            if (player instanceof ServerPlayer serverPlayer
                    && serverPlayer.containerMenu instanceof ResearchTableMenu menu
                    && menu.getBlockPos().equals(pos)) {
                com.example.frontierresearch.research.ResearchManager.tickResearch(serverPlayer);
            }
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
    // Item handler access
    // ------------------------------------------------------------------

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    // ------------------------------------------------------------------
    // BaseContainerBlockEntity / MenuProvider
    // ------------------------------------------------------------------

    @Override
    protected Component getDefaultName() {
        return ResearchTableBlock.TITLE;
    }

    @Override
    protected AbstractContainerMenu createMenu(int windowId, Inventory inventory) {
        return new ResearchTableMenu(windowId, inventory, getBlockPos());
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
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("Items")) {
            itemHandler.deserializeNBT(registries, tag.getCompound("Items"));
        }
    }
}
