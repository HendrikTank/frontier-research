package com.example.frontierresearch.network;

import com.example.frontierresearch.FrontierResearch;
import com.example.frontierresearch.research.PlayerResearchData;
import com.example.frontierresearch.registry.ModAttachments;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

/**
 * Server → Client packet that synchronises a player's research state.
 *
 * <p>Sent whenever a technology is unlocked or the active research changes.
 * On the client the local {@link PlayerResearchData} attachment is updated
 * so GUIs always show current data.</p>
 */
public record ResearchSyncPayload(
        Set<ResourceLocation> unlockedTechnologies,
        @Nullable ResourceLocation activeResearch,
        int researchProgress
) implements CustomPacketPayload {

    public static final Type<ResearchSyncPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(FrontierResearch.MODID, "research_sync"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ResearchSyncPayload> STREAM_CODEC =
            StreamCodec.of(ResearchSyncPayload::encode, ResearchSyncPayload::decode);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    // ------------------------------------------------------------------
    // Codec helpers
    // ------------------------------------------------------------------

    private static void encode(RegistryFriendlyByteBuf buf, ResearchSyncPayload payload) {
        buf.writeVarInt(payload.unlockedTechnologies.size());
        for (ResourceLocation id : payload.unlockedTechnologies) {
            buf.writeResourceLocation(id);
        }
        boolean hasActive = payload.activeResearch != null;
        buf.writeBoolean(hasActive);
        if (hasActive) {
            buf.writeResourceLocation(payload.activeResearch);
        }
        buf.writeVarInt(payload.researchProgress);
    }

    private static ResearchSyncPayload decode(RegistryFriendlyByteBuf buf) {
        int count = buf.readVarInt();
        Set<ResourceLocation> unlocked = new HashSet<>();
        for (int i = 0; i < count; i++) {
            unlocked.add(buf.readResourceLocation());
        }
        ResourceLocation active = buf.readBoolean() ? buf.readResourceLocation() : null;
        int progress = buf.readVarInt();
        return new ResearchSyncPayload(unlocked, active, progress);
    }

    // ------------------------------------------------------------------
    // Handler (client-side)
    // ------------------------------------------------------------------

    /**
     * Handles this packet on the client thread.
     *
     * @param context payload context
     */
    public void handleOnClient(IPayloadContext context) {
        context.enqueueWork(() -> {
            var player = Minecraft.getInstance().player;
            if (player == null) return;
            PlayerResearchData data = player.getData(ModAttachments.RESEARCH_DATA);
            // Fully replace the unlocked set with what the server says
            data.replaceUnlocked(unlockedTechnologies);
            if (activeResearch != null) {
                data.setActiveResearch(activeResearch);
                data.addProgress(researchProgress);
            } else {
                data.setActiveResearch(null);
            }
        });
    }
}
