package com.example.frontierresearch.api.event;

import com.example.frontierresearch.api.SciencePack;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

/**
 * Fired on the {@link net.neoforged.neoforge.common.NeoForge#EVENT_BUS NeoForge event bus}
 * when a science pack is consumed by a lab or by manual research.
 *
 * <p>This event is <em>cancellable</em>.  Cancelling it prevents the pack from
 * being removed from the inventory / lab and suppresses the associated research
 * progress tick.</p>
 *
 * <p>Fired on the <strong>server</strong> thread only.</p>
 */
public class SciencePackConsumedEvent extends Event implements ICancellableEvent {

    private final ServerPlayer player;
    private final SciencePack sciencePack;
    private final int count;

    public SciencePackConsumedEvent(ServerPlayer player, SciencePack sciencePack, int count) {
        this.player = player;
        this.sciencePack = sciencePack;
        this.count = count;
    }

    /**
     * Returns the player that caused the consumption (may be {@code null} if
     * triggered by an automated lab with no associated player).
     *
     * @return the player, or {@code null}
     */
    public ServerPlayer getPlayer() {
        return player;
    }

    /**
     * Returns the science pack that was consumed.
     *
     * @return consumed science pack
     */
    public SciencePack getSciencePack() {
        return sciencePack;
    }

    /**
     * Returns how many pack items were consumed.
     *
     * @return count (&ge; 1)
     */
    public int getCount() {
        return count;
    }
}
