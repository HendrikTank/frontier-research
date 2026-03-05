package com.example.frontierresearch.api;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Describes a single researchable technology (analogous to a Factorio technology).
 *
 * <p>A technology has:</p>
 * <ul>
 *   <li>a unique {@link ResourceLocation} id</li>
 *   <li>a display name (translation key)</li>
 *   <li>zero or more prerequisite technology ids that must be researched first</li>
 *   <li>a list of {@link TechnologyCost} science-pack costs</li>
 *   <li>a minimum {@link LabTier} required to research it</li>
 *   <li>an optional list of recipe ids that are unlocked when the technology completes</li>
 * </ul>
 *
 * <p>Use {@link Builder} to construct instances, then register them via
 * {@link FrontierResearchAPI#registerTechnology}.</p>
 */
public final class Technology {

    private final ResourceLocation id;
    private final String translationKey;
    private final List<ResourceLocation> prerequisites;
    private final List<TechnologyCost> costs;
    private final LabTier requiredTier;
    private final List<ResourceLocation> unlockedRecipes;

    private Technology(Builder builder) {
        this.id = builder.id;
        this.translationKey = builder.translationKey;
        this.prerequisites = Collections.unmodifiableList(new ArrayList<>(builder.prerequisites));
        this.costs = Collections.unmodifiableList(new ArrayList<>(builder.costs));
        this.requiredTier = builder.requiredTier;
        this.unlockedRecipes = Collections.unmodifiableList(new ArrayList<>(builder.unlockedRecipes));
    }

    // -----------------------------------------------------------------
    // Accessors
    // -----------------------------------------------------------------

    /** @return the unique registry id of this technology */
    public ResourceLocation getId() {
        return id;
    }

    /** @return the translation key used to render this technology's display name */
    public String getTranslationKey() {
        return translationKey;
    }

    /**
     * Returns a translatable {@link Component} for this technology's name.
     *
     * @return translatable name component
     */
    public Component getDisplayName() {
        return Component.translatable(translationKey);
    }

    /** @return prerequisite technology ids; all must be researched before this one */
    public List<ResourceLocation> getPrerequisites() {
        return prerequisites;
    }

    /** @return the science-pack costs required to complete this technology */
    public List<TechnologyCost> getCosts() {
        return costs;
    }

    /**
     * Returns the minimum lab tier capable of researching this technology.
     *
     * @return minimum {@link LabTier}
     */
    public LabTier getRequiredTier() {
        return requiredTier;
    }

    /**
     * Returns the list of recipe {@link ResourceLocation}s that become available
     * once this technology has been researched.
     *
     * @return unlocked recipe ids
     */
    public List<ResourceLocation> getUnlockedRecipes() {
        return unlockedRecipes;
    }

    @Override
    public String toString() {
        return "Technology[" + id + "]";
    }

    // -----------------------------------------------------------------
    // Builder
    // -----------------------------------------------------------------

    /**
     * Creates a new builder for a technology with the given id.
     *
     * @param id unique registry id
     * @return a fresh builder
     */
    public static Builder builder(ResourceLocation id) {
        return new Builder(id);
    }

    /** Fluent builder for {@link Technology}. */
    public static final class Builder {

        private final ResourceLocation id;
        private String translationKey;
        private final List<ResourceLocation> prerequisites = new ArrayList<>();
        private final List<TechnologyCost> costs = new ArrayList<>();
        private LabTier requiredTier = LabTier.RESEARCH_TABLE;
        private final List<ResourceLocation> unlockedRecipes = new ArrayList<>();

        private Builder(ResourceLocation id) {
            this.id = id;
            this.translationKey = "technology." + id.getNamespace() + "." + id.getPath();
        }

        /**
         * Override the default translation key.
         *
         * @param key full i18n translation key
         * @return this builder
         */
        public Builder translationKey(String key) {
            this.translationKey = key;
            return this;
        }

        /**
         * Add a prerequisite technology that must be researched first.
         *
         * @param prerequisiteId id of the prerequisite technology
         * @return this builder
         */
        public Builder requires(ResourceLocation prerequisiteId) {
            this.prerequisites.add(prerequisiteId);
            return this;
        }

        /**
         * Add a science-pack cost.
         *
         * @param sciencePackId registry id of the {@link SciencePack}
         * @param count         number of packs needed
         * @return this builder
         */
        public Builder costs(ResourceLocation sciencePackId, int count) {
            this.costs.add(new TechnologyCost(sciencePackId, count));
            return this;
        }

        /**
         * Set the minimum lab tier required to research this technology.
         *
         * @param tier minimum {@link LabTier}
         * @return this builder
         */
        public Builder tier(LabTier tier) {
            this.requiredTier = tier;
            return this;
        }

        /**
         * Register a recipe id that is unlocked by completing this technology.
         *
         * @param recipeId recipe resource location
         * @return this builder
         */
        public Builder unlocks(ResourceLocation recipeId) {
            this.unlockedRecipes.add(recipeId);
            return this;
        }

        /**
         * Build the {@link Technology} instance.
         *
         * @return constructed technology
         */
        public Technology build() {
            return new Technology(this);
        }
    }
}
