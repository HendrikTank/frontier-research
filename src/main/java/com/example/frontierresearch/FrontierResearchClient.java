package com.example.frontierresearch;

import com.example.frontierresearch.client.screen.BurnerLabScreen;
import com.example.frontierresearch.client.screen.ResearchTableScreen;
import com.example.frontierresearch.registry.ModMenuTypes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

/**
 * Client-side initialiser for Frontier Research.
 *
 * <p>Registers GUI screens for all lab menus.  This class only loads on the
 * logical client; it is safe to reference client-only Minecraft classes here.</p>
 */
@Mod(value = FrontierResearch.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = FrontierResearch.MODID, value = Dist.CLIENT)
public class FrontierResearchClient {

    public FrontierResearchClient(ModContainer container) {
        FrontierResearch.LOGGER.info("[FrontierResearch] Client initialising.");
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        FrontierResearch.LOGGER.info("[FrontierResearch] Client setup complete.");
    }

    @SubscribeEvent
    static void onRegisterMenuScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.RESEARCH_TABLE.get(), ResearchTableScreen::new);
        event.register(ModMenuTypes.BURNER_LAB.get(), BurnerLabScreen::new);
    }
}
