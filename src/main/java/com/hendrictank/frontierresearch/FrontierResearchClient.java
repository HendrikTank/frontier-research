package com.hendrictank.frontierresearch;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Client-side initialization for Frontier Research
 */
@EventBusSubscriber(modid = FrontierResearch.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class FrontierResearchClient {
    
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        FrontierResearch.LOGGER.info("Frontier Research client setup");
        
        // TODO: Register screens/GUIs here
        // MenuScreens.register(ModMenuTypes.RESEARCH_TABLE, ResearchTableScreen::new);
        // MenuScreens.register(ModMenuTypes.BURNER_LAB, BurnerLabScreen::new);
    }
}
