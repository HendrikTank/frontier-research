package com.example.frontierresearch.registry;

import com.example.frontierresearch.FrontierResearch;
import com.example.frontierresearch.api.FrontierResearchAPI;
import com.example.frontierresearch.api.LabTier;
import com.example.frontierresearch.api.Technology;
import net.minecraft.resources.ResourceLocation;

/**
 * Registers the built-in technologies shipped with Frontier Research.
 *
 * <p>Technologies are registered into {@link FrontierResearchAPI}.  Other mods
 * can add their own technologies by calling
 * {@link FrontierResearchAPI#registerTechnology} at any time before
 * {@code FMLCommonSetupEvent} completes.</p>
 */
public final class ModTechnologies {

    // ------------------------------------------------------------------
    // Technology IDs (publicly accessible for cross-mod prerequisites)
    // ------------------------------------------------------------------

    public static final ResourceLocation BASIC_LOGISTICS =
            ResourceLocation.fromNamespaceAndPath(FrontierResearch.MODID, "basic_logistics");

    public static final ResourceLocation AUTOMATION =
            ResourceLocation.fromNamespaceAndPath(FrontierResearch.MODID, "automation");

    public static final ResourceLocation ADVANCED_CRAFTING =
            ResourceLocation.fromNamespaceAndPath(FrontierResearch.MODID, "advanced_crafting");

    public static final ResourceLocation ELECTRIC_POWER =
            ResourceLocation.fromNamespaceAndPath(FrontierResearch.MODID, "electric_power");

    public static final ResourceLocation CRYO_RESEARCH =
            ResourceLocation.fromNamespaceAndPath(FrontierResearch.MODID, "cryo_research");

    private ModTechnologies() {}

    /**
     * Registers all built-in technologies into {@link FrontierResearchAPI}.
     * Called during {@code FMLCommonSetupEvent}.
     */
    public static void registerAll() {

        // Tier 1 – researchable at a plain Research Table with basic packs
        FrontierResearchAPI.registerTechnology(
                Technology.builder(BASIC_LOGISTICS)
                        .tier(LabTier.RESEARCH_TABLE)
                        .costs(ModSciencePacks.BASIC, 5)
                        .build()
        );

        // Tier 2 – requires Burner Lab + automation pack
        FrontierResearchAPI.registerTechnology(
                Technology.builder(AUTOMATION)
                        .tier(LabTier.BURNER_LAB)
                        .requires(BASIC_LOGISTICS)
                        .costs(ModSciencePacks.BASIC, 10)
                        .costs(ModSciencePacks.AUTOMATION, 5)
                        .build()
        );

        // Tier 3 – requires multi-block Burner Lab
        FrontierResearchAPI.registerTechnology(
                Technology.builder(ADVANCED_CRAFTING)
                        .tier(LabTier.BURNER_LAB_MULTI)
                        .requires(AUTOMATION)
                        .costs(ModSciencePacks.AUTOMATION, 15)
                        .costs(ModSciencePacks.APPLIED, 5)
                        .build()
        );

        // Tier 4 – requires Electric Lab
        FrontierResearchAPI.registerTechnology(
                Technology.builder(ELECTRIC_POWER)
                        .tier(LabTier.ELECTRIC_LAB)
                        .requires(ADVANCED_CRAFTING)
                        .costs(ModSciencePacks.APPLIED, 20)
                        .costs(ModSciencePacks.ADVANCED, 10)
                        .build()
        );

        // Tier 5 – requires Cryo-Lab
        FrontierResearchAPI.registerTechnology(
                Technology.builder(CRYO_RESEARCH)
                        .tier(LabTier.CRYO_LAB)
                        .requires(ELECTRIC_POWER)
                        .costs(ModSciencePacks.ADVANCED, 30)
                        .costs(ModSciencePacks.CRYO, 20)
                        .build()
        );
    }
}
