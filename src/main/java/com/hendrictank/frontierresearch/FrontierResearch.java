package com.hendrictank.frontierresearch;

import com.hendrictank.frontierresearch.block.ModBlocks;
import com.hendrictank.frontierresearch.blockentity.ModBlockEntities;
import com.hendrictank.frontierresearch.item.ModItems;
import com.hendrictank.frontierresearch.research.ResearchManager;
import com.hendrictank.frontierresearch.research.ModAttachments;
import com.hendrictank.frontierresearch.research.ManualResearchHandler;
import com.hendrictank.frontierresearch.integration.CCTweakedIntegration;
import com.hendrictank.frontierresearch.integration.MoreRedIntegration;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

@Mod(FrontierResearch.MODID)
public class FrontierResearch {
    public static final String MODID = "frontierresearch";
    public static final Logger LOGGER = LogUtils.getLogger();
    
    // Creative Tab
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = 
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> RESEARCH_TAB = 
            CREATIVE_MODE_TABS.register("research_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.frontierresearch"))
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .icon(() -> ModItems.BASIC_SCIENCE_PACK.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        // Add all items
                        output.accept(ModItems.BASIC_SCIENCE_PACK.get());
                        output.accept(ModItems.ADVANCED_SCIENCE_PACK.get());
                        
                        // Add all blocks
                        output.accept(ModBlocks.RESEARCH_TABLE.get());
                        output.accept(ModBlocks.BURNER_LAB.get());
                    }).build());

    public FrontierResearch(IEventBus modEventBus, ModContainer modContainer) {
        LOGGER.info("Initializing Frontier Research");
        
        // Register deferred registers
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModAttachments.ATTACHMENT_TYPES.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        
        // Register event listeners
        modEventBus.addListener(this::commonSetup);
        
        // Register to the game event bus
        NeoForge.EVENT_BUS.register(this);
        NeoForge.EVENT_BUS.register(ResearchManager.class);
        NeoForge.EVENT_BUS.register(ManualResearchHandler.class);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("Frontier Research common setup");
        
        event.enqueueWork(() -> {
            // Initialize research system
            ResearchManager.init();
            
            // Initialize optional integrations
            CCTweakedIntegration.init();
            MoreRedIntegration.init();
            
            if (CCTweakedIntegration.isLoaded()) {
                LOGGER.info("CC:Tweaked integration enabled");
            }
            if (MoreRedIntegration.isLoaded()) {
                LOGGER.info("MoreRed integration enabled");
            }
        });
    }
}
