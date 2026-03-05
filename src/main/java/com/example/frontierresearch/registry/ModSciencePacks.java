package com.example.frontierresearch.registry;

import com.example.frontierresearch.FrontierResearch;
import com.example.frontierresearch.api.FrontierResearchAPI;
import com.example.frontierresearch.api.SciencePack;
import net.minecraft.resources.ResourceLocation;

/**
 * Registers the built-in science packs shipped with Frontier Research.
 *
 * <p>Each pack is identified by a {@link ResourceLocation} and backed by an
 * {@link net.minecraft.world.item.Item} registered in {@link ModItems}.</p>
 *
 * <p>Other mods may add their own packs at any time before
 * {@code FMLCommonSetupEvent} completes by calling
 * {@link FrontierResearchAPI#registerSciencePack}.</p>
 */
public final class ModSciencePacks {

    /** Basic science pack – red tint. */
    public static final ResourceLocation BASIC =
            ResourceLocation.fromNamespaceAndPath(FrontierResearch.MODID, "basic_science_pack");

    /** Automation science pack – orange tint. */
    public static final ResourceLocation AUTOMATION =
            ResourceLocation.fromNamespaceAndPath(FrontierResearch.MODID, "automation_science_pack");

    /** Applied science pack – blue tint. */
    public static final ResourceLocation APPLIED =
            ResourceLocation.fromNamespaceAndPath(FrontierResearch.MODID, "applied_science_pack");

    /** Advanced science pack – purple tint. */
    public static final ResourceLocation ADVANCED =
            ResourceLocation.fromNamespaceAndPath(FrontierResearch.MODID, "advanced_science_pack");

    /** Cryo science pack – cyan tint. */
    public static final ResourceLocation CRYO =
            ResourceLocation.fromNamespaceAndPath(FrontierResearch.MODID, "cryo_science_pack");

    private ModSciencePacks() {}

    /**
     * Registers all built-in science packs into {@link FrontierResearchAPI}.
     * Called during {@code FMLCommonSetupEvent}.
     */
    public static void registerAll() {
        FrontierResearchAPI.registerSciencePack(new SciencePack(
                BASIC, () -> ModItems.BASIC_SCIENCE_PACK.get(), 200, 60, 60));

        FrontierResearchAPI.registerSciencePack(new SciencePack(
                AUTOMATION, () -> ModItems.AUTOMATION_SCIENCE_PACK.get(), 220, 130, 30));

        FrontierResearchAPI.registerSciencePack(new SciencePack(
                APPLIED, () -> ModItems.APPLIED_SCIENCE_PACK.get(), 60, 120, 200));

        FrontierResearchAPI.registerSciencePack(new SciencePack(
                ADVANCED, () -> ModItems.ADVANCED_SCIENCE_PACK.get(), 150, 60, 200));

        FrontierResearchAPI.registerSciencePack(new SciencePack(
                CRYO, () -> ModItems.CRYO_SCIENCE_PACK.get(), 60, 210, 220));
    }
}
