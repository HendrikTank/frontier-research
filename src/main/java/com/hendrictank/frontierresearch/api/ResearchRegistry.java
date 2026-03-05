package com.hendrictank.frontierresearch.api;

import com.hendrictank.frontierresearch.FrontierResearch;
import net.minecraft.resources.ResourceLocation;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Central registry for research entries.
 * Mods can register custom research via this API.
 */
public class ResearchRegistry {
    private static final Map<ResourceLocation, Research> RESEARCH_MAP = new ConcurrentHashMap<>();
    private static final Map<Integer, List<Research>> RESEARCH_BY_TIER = new ConcurrentHashMap<>();
    
    /**
     * Registers a new research entry.
     * 
     * @param research The research to register
     */
    public static void register(Research research) {
        if (RESEARCH_MAP.containsKey(research.getId())) {
            FrontierResearch.LOGGER.warn("Research {} is already registered, overwriting", research.getId());
        }
        
        RESEARCH_MAP.put(research.getId(), research);
        RESEARCH_BY_TIER.computeIfAbsent(research.getTier(), k -> new ArrayList<>()).add(research);
        
        FrontierResearch.LOGGER.debug("Registered research: {}", research.getId());
    }
    
    /**
     * Gets a research by its ID.
     * 
     * @param id The research ID
     * @return The research, or null if not found
     */
    public static Research getResearch(ResourceLocation id) {
        return RESEARCH_MAP.get(id);
    }
    
    /**
     * Gets all research for a specific tier.
     * 
     * @param tier The tier
     * @return List of research in that tier
     */
    public static List<Research> getResearchByTier(int tier) {
        return RESEARCH_BY_TIER.getOrDefault(tier, List.of());
    }
    
    /**
     * Gets all registered research.
     * 
     * @return Collection of all research
     */
    public static Collection<Research> getAllResearch() {
        return RESEARCH_MAP.values();
    }
    
    /**
     * Checks if a research is registered.
     * 
     * @param id The research ID
     * @return true if registered
     */
    public static boolean isRegistered(ResourceLocation id) {
        return RESEARCH_MAP.containsKey(id);
    }
    
    /**
     * Clears all registered research.
     * Used for testing or reloading.
     */
    public static void clear() {
        RESEARCH_MAP.clear();
        RESEARCH_BY_TIER.clear();
    }
}
