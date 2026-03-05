package com.example.frontierresearch.research;

import com.example.frontierresearch.api.FrontierResearchAPI;
import com.example.frontierresearch.api.SciencePack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.EntityItemPickupEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.util.Optional;

/**
 * Handles <em>manual research</em> – the earliest tier of research in which
 * the player contributes science-pack items directly by either:
 * <ol>
 *   <li><strong>Picking up</strong> a science-pack item from the ground
 *       ({@link EntityItemPickupEvent} – consumes 1 pack from the dropped
 *       entity).</li>
 *   <li><strong>Right-clicking</strong> while holding a science-pack item
 *       in the main hand ({@link PlayerInteractEvent.RightClickItem} – consumes
 *       1 pack from the hand stack).</li>
 * </ol>
 *
 * <p>Both paths are server-side only.  After consuming the pack, one tick of
 * {@link ResearchManager#tickResearch} is forced so progress advances
 * immediately.</p>
 *
 * <p>Register this class on the NeoForge event bus in your mod constructor:
 * {@code NeoForge.EVENT_BUS.register(new ManualResearchHandler());}
 * </p>
 */
public class ManualResearchHandler {

    /**
     * Called when a player picks up an item entity.
     * If the item is a registered science pack and the player has active manual
     * research, one copy of the item is consumed for research.
     */
    @SubscribeEvent
    public void onItemPickup(EntityItemPickupEvent event) {
        Player player = event.getEntity();
        Level level = player.level();
        if (level.isClientSide()) return;

        ItemStack stack = event.getItem().getItem();
        Optional<SciencePack> optPack = findSciencePack(stack);
        if (optPack.isEmpty()) return;

        if (!(player instanceof net.minecraft.server.level.ServerPlayer serverPlayer)) return;

        PlayerResearchData data = ResearchManager.getData(serverPlayer);
        ResourceLocation activeId = data.getActiveResearch();
        if (activeId == null) return;

        // Only consume if active research requires this pack
        var optTech = FrontierResearchAPI.getTechnology(activeId);
        if (optTech.isEmpty()) return;
        boolean needsPack = optTech.get().getCosts().stream()
                .anyMatch(c -> c.sciencePackId().equals(optPack.get().id()));
        if (!needsPack) return;

        // Consume one pack from the dropped item entity
        if (stack.getCount() > 0) {
            stack.shrink(1);
            event.getItem().setItem(stack);
            ResearchManager.tickResearch(serverPlayer);
        }
    }

    /**
     * Called when a player right-clicks while holding an item.
     * If the item is a science pack and the player has active manual research,
     * one copy is consumed from their hand.
     */
    @SubscribeEvent
    public void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        Level level = player.level();
        if (level.isClientSide()) return;

        ItemStack stack = event.getItemStack();
        Optional<SciencePack> optPack = findSciencePack(stack);
        if (optPack.isEmpty()) return;

        if (!(player instanceof net.minecraft.server.level.ServerPlayer serverPlayer)) return;

        PlayerResearchData data = ResearchManager.getData(serverPlayer);
        ResourceLocation activeId = data.getActiveResearch();
        if (activeId == null) return;

        var optTech = FrontierResearchAPI.getTechnology(activeId);
        if (optTech.isEmpty()) return;
        boolean needsPack = optTech.get().getCosts().stream()
                .anyMatch(c -> c.sciencePackId().equals(optPack.get().id()));
        if (!needsPack) return;

        // Consume one pack from hand
        stack.shrink(1);
        ResearchManager.tickResearch(serverPlayer);
        event.setCanceled(true); // prevent further processing (e.g. block interaction)
    }

    // ------------------------------------------------------------------
    // Helper
    // ------------------------------------------------------------------

    private Optional<SciencePack> findSciencePack(ItemStack stack) {
        if (stack.isEmpty()) return Optional.empty();
        for (SciencePack pack : FrontierResearchAPI.getAllSciencePacks()) {
            if (stack.is(pack.item())) {
                return Optional.of(pack);
            }
        }
        return Optional.empty();
    }
}
