package com.hendrictank.frontierresearch.api;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;

/**
 * Interface for research labs.
 * Implement this in block entities that can conduct research.
 */
public interface IResearchLab {
    /**
     * Gets the tier of this lab.
     * Higher tier labs can conduct more advanced research faster.
     * 
     * @return Lab tier (0 = manual, 1 = basic table, 2 = burner, etc.)
     */
    int getLabTier();
    
    /**
     * Gets the research speed multiplier for this lab.
     * 
     * @return Speed multiplier (1.0 = normal speed)
     */
    default double getResearchSpeed() {
        return 1.0;
    }
    
    /**
     * Checks if this lab can conduct the given research.
     * 
     * @param researchId The research to check
     * @return true if this lab can conduct the research
     */
    boolean canResearch(String researchId);
    
    /**
     * Attempts to progress research using available science packs.
     * 
     * @param researchId The research to progress
     * @return true if progress was made
     */
    boolean progressResearch(String researchId);
    
    /**
     * Gets the current research being conducted in this lab.
     * 
     * @return The research ID, or null if none
     */
    String getCurrentResearch();
    
    /**
     * Sets the research to be conducted in this lab.
     * 
     * @param researchId The research ID
     */
    void setCurrentResearch(String researchId);
}
