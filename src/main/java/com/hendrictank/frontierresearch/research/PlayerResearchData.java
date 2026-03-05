package com.hendrictank.frontierresearch.research;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.util.INBTSerializable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Stores research progress data for a player
 */
public class PlayerResearchData implements INBTSerializable<CompoundTag> {
    private final Set<ResourceLocation> completedResearch = ConcurrentHashMap.newKeySet();
    private final Map<ResourceLocation, Integer> researchProgress = new ConcurrentHashMap<>();
    
    /**
     * Checks if a player has completed a research
     */
    public boolean hasCompleted(ResourceLocation researchId) {
        return completedResearch.contains(researchId);
    }
    
    /**
     * Marks a research as completed
     */
    public void completeResearch(ResourceLocation researchId) {
        completedResearch.add(researchId);
        researchProgress.remove(researchId);
    }
    
    /**
     * Gets the progress for a research (0-100)
     */
    public int getProgress(ResourceLocation researchId) {
        return researchProgress.getOrDefault(researchId, 0);
    }
    
    /**
     * Sets the progress for a research
     */
    public void setProgress(ResourceLocation researchId, int progress) {
        if (progress >= 100) {
            completeResearch(researchId);
        } else {
            researchProgress.put(researchId, progress);
        }
    }
    
    /**
     * Adds progress to a research
     */
    public void addProgress(ResourceLocation researchId, int amount) {
        int current = getProgress(researchId);
        setProgress(researchId, current + amount);
    }
    
    /**
     * Gets all completed research
     */
    public Set<ResourceLocation> getCompletedResearch() {
        return Collections.unmodifiableSet(completedResearch);
    }
    
    /**
     * Gets all research in progress
     */
    public Map<ResourceLocation, Integer> getResearchInProgress() {
        return Collections.unmodifiableMap(researchProgress);
    }
    
    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        
        // Save completed research
        ListTag completedList = new ListTag();
        for (ResourceLocation id : completedResearch) {
            completedList.add(StringTag.valueOf(id.toString()));
        }
        tag.put("Completed", completedList);
        
        // Save research progress
        CompoundTag progressTag = new CompoundTag();
        for (Map.Entry<ResourceLocation, Integer> entry : researchProgress.entrySet()) {
            progressTag.putInt(entry.getKey().toString(), entry.getValue());
        }
        tag.put("Progress", progressTag);
        
        return tag;
    }
    
    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        completedResearch.clear();
        researchProgress.clear();
        
        // Load completed research
        if (tag.contains("Completed", Tag.TAG_LIST)) {
            ListTag completedList = tag.getList("Completed", Tag.TAG_STRING);
            for (int i = 0; i < completedList.size(); i++) {
                completedResearch.add(ResourceLocation.parse(completedList.getString(i)));
            }
        }
        
        // Load research progress
        if (tag.contains("Progress", Tag.TAG_COMPOUND)) {
            CompoundTag progressTag = tag.getCompound("Progress");
            for (String key : progressTag.getAllKeys()) {
                researchProgress.put(ResourceLocation.parse(key), progressTag.getInt(key));
            }
        }
    }
}
