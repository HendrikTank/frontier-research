package com.hendrictank.frontierresearch.research;

import com.hendrictank.frontierresearch.FrontierResearch;
import com.hendrictank.frontierresearch.api.ResearchRegistry;
import com.hendrictank.frontierresearch.api.Research;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.EntityItemPickupEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

/**
 * Handles manual research triggered by player actions
 */
public class ManualResearchHandler {
    
    /**
     * Triggered when a player picks up an item
     * Can unlock basic research through item discovery
     */
    @SubscribeEvent
    public static void onItemPickup(EntityItemPickupEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ItemStack stack = event.getItem().getItem();
            
            // Check if player has manual research enabled (early game)
            PlayerResearchData data = player.getData(ModAttachments.PLAYER_RESEARCH);
            
            // Example: Picking up iron unlocks basic automation
            if (stack.getItem().toString().contains("iron")) {
                ResourceLocation researchId = ResourceLocation.fromNamespaceAndPath(
                    FrontierResearch.MODID, "basic_automation");
                    
                if (!data.hasCompleted(researchId)) {
                    data.addProgress(researchId, 5); // 5% progress per iron item
                    
                    if (data.getProgress(researchId) >= 100) {
                        player.sendSystemMessage(Component.translatable(
                            "message.frontierresearch.research_completed",
                            "Basic Automation"
                        ));
                    }
                }
            }
        }
    }
    
    /**
     * Triggered when a player right-clicks a block
     * Can be used for "inspecting" blocks to gain research
     */
    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        if (player instanceof ServerPlayer serverPlayer) {
            // TODO: Implement block inspection for manual research
            // Example: Right-click a furnace to learn about burner labs
        }
    }
}
