package com.hendrictank.frontierresearch.research;

import com.hendrictank.frontierresearch.FrontierResearch;
import com.hendrictank.frontierresearch.api.Research;
import com.hendrictank.frontierresearch.api.ResearchRegistry;
import com.hendrictank.frontierresearch.item.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import java.util.List;

/**
 * Manages the research system and registers default research entries
 */
public class ResearchManager {
    
    public static void init() {
        registerDefaultResearch();
    }
    
    private static void registerDefaultResearch() {
        FrontierResearch.LOGGER.info("Registering default research");
        
        // Basic Automation - Tier 0
        ResearchRegistry.register(new Research.Builder(
                ResourceLocation.fromNamespaceAndPath(FrontierResearch.MODID, "basic_automation"))
                .tier(0)
                .requiredProgress(100)
                .sciencePacks(List.of(new ItemStack(ModItems.BASIC_SCIENCE_PACK.get(), 10)))
                .unlocks(() -> List.of(
                        ResourceLocation.fromNamespaceAndPath("minecraft", "hopper"),
                        ResourceLocation.fromNamespaceAndPath("minecraft", "dropper")
                ))
                .build());
        
        // Advanced Machinery - Tier 1
        ResearchRegistry.register(new Research.Builder(
                ResourceLocation.fromNamespaceAndPath(FrontierResearch.MODID, "advanced_machinery"))
                .tier(1)
                .requiredProgress(250)
                .prerequisites(List.of(
                        ResourceLocation.fromNamespaceAndPath(FrontierResearch.MODID, "basic_automation")
                ))
                .sciencePacks(List.of(
                        new ItemStack(ModItems.BASIC_SCIENCE_PACK.get(), 10),
                        new ItemStack(ModItems.ADVANCED_SCIENCE_PACK.get(), 5)
                ))
                .unlocks(() -> List.of(
                        ResourceLocation.fromNamespaceAndPath("minecraft", "piston"),
                        ResourceLocation.fromNamespaceAndPath("minecraft", "sticky_piston")
                ))
                .build());
        
        // Redstone Engineering - Tier 0
        ResearchRegistry.register(new Research.Builder(
                ResourceLocation.fromNamespaceAndPath(FrontierResearch.MODID, "redstone_engineering"))
                .tier(0)
                .requiredProgress(150)
                .sciencePacks(List.of(new ItemStack(ModItems.BASIC_SCIENCE_PACK.get(), 15)))
                .unlocks(() -> List.of(
                        ResourceLocation.fromNamespaceAndPath("minecraft", "repeater"),
                        ResourceLocation.fromNamespaceAndPath("minecraft", "comparator"),
                        ResourceLocation.fromNamespaceAndPath("minecraft", "observer")
                ))
                .build());
        
        FrontierResearch.LOGGER.info("Registered {} research entries", ResearchRegistry.getAllResearch().size());
    }
}
