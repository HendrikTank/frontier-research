package com.hendrictank.frontierresearch.api;

import net.minecraft.world.item.ItemStack;

/**
 * Interface for registering custom science packs.
 * Science packs are consumed in labs to unlock research.
 */
public interface ISciencePack {
    /**
     * Gets the tier of this science pack.
     * Higher tiers may be required for advanced research.
     * 
     * @return The tier level (0 = basic, 1 = advanced, etc.)
     */
    int getTier();
    
    /**
     * Gets the research value of this science pack.
     * This determines how much research progress one pack provides.
     * 
     * @return Research value per pack
     */
    int getResearchValue();
    
    /**
     * Checks if this science pack is valid for the given research.
     * 
     * @param researchId The research being conducted
     * @return true if this pack can be used for the research
     */
    default boolean isValidFor(String researchId) {
        return true;
    }
}
