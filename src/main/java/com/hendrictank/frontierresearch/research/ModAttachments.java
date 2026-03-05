package com.hendrictank.frontierresearch.research;

import com.hendrictank.frontierresearch.FrontierResearch;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

/**
 * Data attachments for storing player research data
 */
public class ModAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = 
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, FrontierResearch.MODID);
    
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<PlayerResearchData>> PLAYER_RESEARCH =
            ATTACHMENT_TYPES.register("player_research", () -> 
                    AttachmentType.serializable(PlayerResearchData::new).build());
}
