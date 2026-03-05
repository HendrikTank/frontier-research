package com.example.frontierresearch.api.event;

import com.example.frontierresearch.api.Technology;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

/**
 * Fired on the {@link net.neoforged.neoforge.common.NeoForge#EVENT_BUS NeoForge event bus}
 * when a player completes a {@link Technology}.
 *
 * <p>This event is <em>cancellable</em>.  Cancelling it prevents the
 * technology from being marked as researched and stops any recipe unlocks
 * from being sent to the client.</p>
 *
 * <p>Fired on the <strong>server</strong> thread only.</p>
 */
public class ResearchUnlockedEvent extends Event implements ICancellableEvent {

    private final ServerPlayer player;
    private final Technology technology;

    public ResearchUnlockedEvent(ServerPlayer player, Technology technology) {
        this.player = player;
        this.technology = technology;
    }

    /**
     * Returns the player who completed the research.
     *
     * @return the server player
     */
    public ServerPlayer getPlayer() {
        return player;
    }

    /**
     * Returns the technology that was just researched.
     *
     * @return completed technology
     */
    public Technology getTechnology() {
        return technology;
    }
}
