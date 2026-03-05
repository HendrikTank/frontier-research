package com.hendrictank.frontierresearch.api;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import java.util.List;
import java.util.function.Supplier;

/**
 * Represents a research entry in the tech tree.
 * Use ResearchRegistry to register custom research.
 */
public class Research {
    private final ResourceLocation id;
    private final int tier;
    private final int requiredProgress;
    private final List<ResourceLocation> prerequisites;
    private final List<ItemStack> sciencePackRequirements;
    private final Supplier<List<ResourceLocation>> unlocks;
    
    public Research(ResourceLocation id, int tier, int requiredProgress, 
                   List<ResourceLocation> prerequisites,
                   List<ItemStack> sciencePackRequirements,
                   Supplier<List<ResourceLocation>> unlocks) {
        this.id = id;
        this.tier = tier;
        this.requiredProgress = requiredProgress;
        this.prerequisites = prerequisites;
        this.sciencePackRequirements = sciencePackRequirements;
        this.unlocks = unlocks;
    }
    
    public ResourceLocation getId() {
        return id;
    }
    
    public int getTier() {
        return tier;
    }
    
    public int getRequiredProgress() {
        return requiredProgress;
    }
    
    public List<ResourceLocation> getPrerequisites() {
        return prerequisites;
    }
    
    public List<ItemStack> getSciencePackRequirements() {
        return sciencePackRequirements;
    }
    
    public List<ResourceLocation> getUnlocks() {
        return unlocks.get();
    }
    
    /**
     * Builder for creating Research entries
     */
    public static class Builder {
        private final ResourceLocation id;
        private int tier = 0;
        private int requiredProgress = 100;
        private List<ResourceLocation> prerequisites = List.of();
        private List<ItemStack> sciencePackRequirements = List.of();
        private Supplier<List<ResourceLocation>> unlocks = List::of;
        
        public Builder(ResourceLocation id) {
            this.id = id;
        }
        
        public Builder tier(int tier) {
            this.tier = tier;
            return this;
        }
        
        public Builder requiredProgress(int progress) {
            this.requiredProgress = progress;
            return this;
        }
        
        public Builder prerequisites(List<ResourceLocation> prerequisites) {
            this.prerequisites = prerequisites;
            return this;
        }
        
        public Builder sciencePacks(List<ItemStack> packs) {
            this.sciencePackRequirements = packs;
            return this;
        }
        
        public Builder unlocks(Supplier<List<ResourceLocation>> unlocks) {
            this.unlocks = unlocks;
            return this;
        }
        
        public Research build() {
            return new Research(id, tier, requiredProgress, prerequisites, 
                              sciencePackRequirements, unlocks);
        }
    }
}
