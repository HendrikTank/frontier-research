package com.example.frontierresearch.api;

import net.minecraft.resources.ResourceLocation;

/**
 * Represents a single science-pack ingredient cost inside a {@link Technology}.
 *
 * @param sciencePackId the {@link SciencePack} registry id
 * @param count         number of packs required (must be &ge; 1)
 */
public record TechnologyCost(ResourceLocation sciencePackId, int count) {

    public TechnologyCost {
        if (count < 1) {
            throw new IllegalArgumentException("TechnologyCost count must be >= 1, got " + count);
        }
    }
}
