package com.example.frontierresearch;

import com.example.frontierresearch.network.ResearchSyncPayload;
import com.example.frontierresearch.registry.*;
import com.example.frontierresearch.research.ManualResearchHandler;
import com.example.frontierresearch.research.ResearchManager;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main entry point for <strong>Frontier Research</strong>.
 *
 * <p>Frontier Research adds a Factorio-inspired technology research system
 * to Minecraft.  Players craft science packs, which are consumed by
 * research labs to unlock new recipes and capabilities.</p>
 *
 * <h2>Lab tiers</h2>
 * <ol>
 *   <li>Manual – item pick-up or hand-use</li>
 *   <li>Research Table – simple single-block lab</li>
 *   <li>Burner Lab – single-block, fuel-powered</li>
 *   <li>Multi-Block Burner Lab</li>
 *   <li>Multi-Block Electric Lab (upgradable ×2)</li>
 *   <li>Multi-Block Cryo-Lab (requires coolant)</li>
 * </ol>
 *
 * <h2>API</h2>
 * <p>Other mods can register science packs, technologies, and lab types via
 * {@link com.example.frontierresearch.api.FrontierResearchAPI}.</p>
 */
@Mod(FrontierResearch.MODID)
public class FrontierResearch {

    public static final String MODID = "frontierresearch";
    public static final Logger LOGGER = LoggerFactory.getLogger(FrontierResearch.class);

    public FrontierResearch(IEventBus modEventBus) {
        // Register all Deferred registries
        ModAttachments.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModCreativeTabs.register(modEventBus);

        // Common setup (science packs + technologies)
        modEventBus.addListener(this::commonSetup);

        // Capability registration
        modEventBus.addListener(this::registerCapabilities);

        // Network payload registration
        modEventBus.addListener(this::registerPayloadHandlers);

        // Game event listeners
        NeoForge.EVENT_BUS.register(new ManualResearchHandler());
        NeoForge.EVENT_BUS.addListener(this::onPlayerTick);

        LOGGER.info("[FrontierResearch] Mod initialising.");
    }

    // ------------------------------------------------------------------
    // Common Setup
    // ------------------------------------------------------------------

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ModSciencePacks.registerAll();
            ModTechnologies.registerAll();
            LOGGER.info("[FrontierResearch] Registered {} science packs and {} technologies.",
                    com.example.frontierresearch.api.FrontierResearchAPI.getAllSciencePacks().size(),
                    com.example.frontierresearch.api.FrontierResearchAPI.getAllTechnologies().size());
        });
    }

    // ------------------------------------------------------------------
    // Capabilities
    // ------------------------------------------------------------------

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.RESEARCH_TABLE.get(),
                (be, side) -> be.getItemHandler()
        );
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.BURNER_LAB.get(),
                (be, side) -> be.getItemHandler()
        );
    }

    // ------------------------------------------------------------------
    // Networking
    // ------------------------------------------------------------------

    private void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(
                ResearchSyncPayload.TYPE,
                ResearchSyncPayload.STREAM_CODEC,
                (payload, ctx) -> payload.handleOnClient(ctx)
        );
    }

    // ------------------------------------------------------------------
    // Server-tick: research ticking for players NOT inside a lab GUI
    // (lab block entities tick research for players that ARE in the GUI)
    // ------------------------------------------------------------------

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer serverPlayer)) return;
        // Only tick here if the player has manual research selected
        // (lab block entities handle their own players)
        if (serverPlayer.containerMenu == serverPlayer.inventoryMenu) {
            ResearchManager.tickResearch(serverPlayer);
        }
    }
}
