package com.example.frontierresearch.registry;

import com.example.frontierresearch.FrontierResearch;
import com.example.frontierresearch.research.PlayerResearchData;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

/**
 * Registers NeoForge data-attachment types used by Frontier Research.
 *
 * <p>Attachments allow arbitrary data to be stored directly on game objects
 * (players, levels, etc.) and persisted automatically.</p>
 */
public final class ModAttachments {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, FrontierResearch.MODID);

    /**
     * Per-player research data: unlocked technologies, active research, progress.
     *
     * <p>Access via {@code player.getData(ModAttachments.RESEARCH_DATA)}.
     * Automatically serialised to / from NBT and persisted across sessions.</p>
     */
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<PlayerResearchData>> RESEARCH_DATA =
            ATTACHMENT_TYPES.register("research_data", () ->
                    AttachmentType.<PlayerResearchData>builder(PlayerResearchData::new)
                            .serialize(PlayerResearchData.CODEC)
                            .copyOnDeath() // keep research progress on respawn
                            .build()
            );

    private ModAttachments() {}

    public static void register(IEventBus modEventBus) {
        ATTACHMENT_TYPES.register(modEventBus);
    }
}
