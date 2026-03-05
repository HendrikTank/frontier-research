package com.hendrictank.frontierresearch.blockentity;

import com.hendrictank.frontierresearch.api.IResearchLab;
import com.hendrictank.frontierresearch.api.ISciencePack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.items.ItemStackHandler;

/**
 * Block entity for the Burner Lab
 * Uses burnable items (coal, wood, etc.) as fuel to conduct research
 */
public class BurnerLabBlockEntity extends BlockEntity implements IResearchLab {
    private static final int SCIENCE_SLOTS = 9;
    private static final int FUEL_SLOT = 9;
    private static final int INVENTORY_SIZE = 10;
    
    private final ItemStackHandler inventory = new ItemStackHandler(INVENTORY_SIZE) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };
    
    private String currentResearch = null;
    private int researchProgress = 0;
    private int burnTime = 0;
    private int maxBurnTime = 0;
    private int tickCounter = 0;
    
    public BurnerLabBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BURNER_LAB.get(), pos, state);
    }
    
    @Override
    public int getLabTier() {
        return 2;
    }
    
    @Override
    public double getResearchSpeed() {
        return 1.5; // 50% faster than basic table
    }
    
    @Override
    public boolean canResearch(String researchId) {
        // Burner lab can do tier 0-2 research
        return true;
    }
    
    @Override
    public boolean progressResearch(String researchId) {
        if (burnTime <= 0) {
            return false; // No fuel
        }
        
        // Check if we have science packs
        for (int i = 0; i < SCIENCE_SLOTS; i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem() instanceof ISciencePack sciencePack) {
                researchProgress += (int)(sciencePack.getResearchValue() * getResearchSpeed());
                stack.shrink(1);
                setChanged();
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String getCurrentResearch() {
        return currentResearch;
    }
    
    @Override
    public void setCurrentResearch(String researchId) {
        this.currentResearch = researchId;
        this.researchProgress = 0;
        setChanged();
    }
    
    public ItemStackHandler getInventory() {
        return inventory;
    }
    
    public int getResearchProgress() {
        return researchProgress;
    }
    
    public int getBurnTime() {
        return burnTime;
    }
    
    public int getMaxBurnTime() {
        return maxBurnTime;
    }
    
    public boolean isBurning() {
        return burnTime > 0;
    }
    
    private void consumeFuel() {
        ItemStack fuelStack = inventory.getStackInSlot(FUEL_SLOT);
        if (!fuelStack.isEmpty()) {
            int fuelValue = CommonHooks.getBurnTime(fuelStack, RecipeType.SMELTING);
            if (fuelValue > 0) {
                burnTime = fuelValue;
                maxBurnTime = fuelValue;
                fuelStack.shrink(1);
                setChanged();
            }
        }
    }
    
    public static void tick(Level level, BlockPos pos, BlockState state, BurnerLabBlockEntity blockEntity) {
        if (level.isClientSide) {
            return;
        }
        
        // Consume fuel
        if (blockEntity.burnTime > 0) {
            blockEntity.burnTime--;
        }
        
        // Try to start burning if needed
        if (blockEntity.burnTime <= 0 && blockEntity.currentResearch != null) {
            blockEntity.consumeFuel();
        }
        
        blockEntity.tickCounter++;
        
        // Progress research every 10 ticks (0.5 seconds) - faster than basic table
        if (blockEntity.tickCounter >= 10) {
            blockEntity.tickCounter = 0;
            
            if (blockEntity.currentResearch != null && blockEntity.isBurning()) {
                blockEntity.progressResearch(blockEntity.currentResearch);
            }
        }
    }
    
    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Inventory", inventory.serializeNBT(registries));
        if (currentResearch != null) {
            tag.putString("CurrentResearch", currentResearch);
        }
        tag.putInt("ResearchProgress", researchProgress);
        tag.putInt("BurnTime", burnTime);
        tag.putInt("MaxBurnTime", maxBurnTime);
    }
    
    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        inventory.deserializeNBT(registries, tag.getCompound("Inventory"));
        if (tag.contains("CurrentResearch")) {
            currentResearch = tag.getString("CurrentResearch");
        }
        researchProgress = tag.getInt("ResearchProgress");
        burnTime = tag.getInt("BurnTime");
        maxBurnTime = tag.getInt("MaxBurnTime");
    }
}
