package com.example.frontierresearch.research;

import com.example.frontierresearch.api.FrontierResearchAPI;
import com.example.frontierresearch.api.SciencePack;
import com.example.frontierresearch.api.Technology;
import com.example.frontierresearch.api.TechnologyCost;
import com.example.frontierresearch.api.event.ResearchUnlockedEvent;
import com.example.frontierresearch.api.event.SciencePackConsumedEvent;
import com.example.frontierresearch.network.ResearchSyncPayload;
import com.example.frontierresearch.registry.ModAttachments;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Server-side manager that drives the research loop for a player.
 *
 * <p>Call {@link #tickResearch(ServerPlayer)} once per server tick for every
 * player that has an active research set.  When a technology's total cost is
 * met the {@link ResearchUnlockedEvent} is fired, the technology is marked as
 * unlocked, and a sync packet is dispatched to the client.</p>
 *
 * <p>Science-pack consumption is gated by {@link SciencePackConsumedEvent},
 * allowing other mods to intercept or cancel individual consumption steps.</p>
 */
public final class ResearchManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResearchManager.class);

    /**
     * Number of server ticks between each science-pack consumption attempt
     * (20 ticks = 1 second baseline).
     */
    private static final int RESEARCH_TICK_INTERVAL = 20;

    private ResearchManager() {}

    // ------------------------------------------------------------------
    // Public API
    // ------------------------------------------------------------------

    /**
     * Returns the {@link PlayerResearchData} attached to the given player.
     * Creates a fresh (empty) instance on first access.
     *
     * @param player the server player
     * @return their research data (never null)
     */
    public static PlayerResearchData getData(ServerPlayer player) {
        return player.getData(ModAttachments.RESEARCH_DATA);
    }

    /**
     * Sets the active research for a player and resets progress.
     *
     * <p>Does nothing if the technology is already unlocked or a prerequisite
     * is not yet met.</p>
     *
     * @param player       the player
     * @param technologyId id of the technology to start researching
     * @return {@code true} if the research was started, {@code false} otherwise
     */
    public static boolean startResearch(ServerPlayer player, ResourceLocation technologyId) {
        Optional<Technology> optTech = FrontierResearchAPI.getTechnology(technologyId);
        if (optTech.isEmpty()) {
            LOGGER.warn("[FrontierResearch] Unknown technology '{}' requested by {}.", technologyId, player.getName().getString());
            return false;
        }
        Technology tech = optTech.get();
        PlayerResearchData data = getData(player);

        if (data.isUnlocked(technologyId)) {
            return false; // already researched
        }
        // Check prerequisites
        for (ResourceLocation prereq : tech.getPrerequisites()) {
            if (!data.isUnlocked(prereq)) {
                return false;
            }
        }
        data.setActiveResearch(technologyId);
        syncToClient(player);
        return true;
    }

    /**
     * Called every server tick to attempt a research progress step.
     *
     * <p>Only advances progress once every {@link #RESEARCH_TICK_INTERVAL} ticks.
     * The method is a no-op if the player has no active research.</p>
     *
     * @param player the player whose research should be ticked
     */
    public static void tickResearch(ServerPlayer player) {
        PlayerResearchData data = getData(player);
        ResourceLocation activeId = data.getActiveResearch();
        if (activeId == null) return;

        // Only tick every RESEARCH_TICK_INTERVAL game ticks
        if ((player.tickCount % RESEARCH_TICK_INTERVAL) != 0) return;

        Optional<Technology> optTech = FrontierResearchAPI.getTechnology(activeId);
        if (optTech.isEmpty()) {
            LOGGER.warn("[FrontierResearch] Active research '{}' is no longer registered – clearing.", activeId);
            data.setActiveResearch(null);
            return;
        }

        Technology tech = optTech.get();

        // Calculate total cost (sum of all pack counts)
        int totalCost = tech.getCosts().stream().mapToInt(TechnologyCost::count).sum();

        // Fire SciencePackConsumedEvent for each cost entry
        for (TechnologyCost cost : tech.getCosts()) {
            Optional<SciencePack> optPack = FrontierResearchAPI.getSciencePack(cost.sciencePackId());
            if (optPack.isEmpty()) continue;

            SciencePackConsumedEvent event = new SciencePackConsumedEvent(player, optPack.get(), cost.count());
            NeoForge.EVENT_BUS.post(event);
            if (event.isCanceled()) {
                // Another mod prevented consumption – skip this tick
                return;
            }
        }

        data.addProgress(1);

        if (data.getResearchProgress() >= totalCost) {
            completeResearch(player, tech, data);
        }
    }

    /**
     * Forcefully marks a technology as researched for the given player,
     * skipping the normal consumption loop.  Useful for admin commands and
     * testing.
     *
     * @param player     the player
     * @param technology the technology to unlock immediately
     */
    public static void forceUnlock(ServerPlayer player, Technology technology) {
        completeResearch(player, technology, getData(player));
    }

    // ------------------------------------------------------------------
    // Internal helpers
    // ------------------------------------------------------------------

    private static void completeResearch(ServerPlayer player, Technology tech, PlayerResearchData data) {
        ResearchUnlockedEvent event = new ResearchUnlockedEvent(player, tech);
        NeoForge.EVENT_BUS.post(event);
        if (event.isCanceled()) return;

        data.unlock(tech.getId());
        data.resetProgress();
        // Clear active research if it matches the completed one
        if (tech.getId().equals(data.getActiveResearch())) {
            data.setActiveResearch(null);
        }

        LOGGER.info("[FrontierResearch] {} researched '{}'.", player.getName().getString(), tech.getId());
        syncToClient(player);
    }

    private static void syncToClient(ServerPlayer player) {
        PlayerResearchData data = getData(player);
        PacketDistributor.sendToPlayer(player, new ResearchSyncPayload(
                data.getUnlockedTechnologies(),
                data.getActiveResearch(),
                data.getResearchProgress()
        ));
    }
}
