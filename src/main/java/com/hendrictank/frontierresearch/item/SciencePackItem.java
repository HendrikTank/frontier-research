package com.hendrictank.frontierresearch.item;

import com.hendrictank.frontierresearch.FrontierResearch;
import com.hendrictank.frontierresearch.api.ISciencePack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import java.util.List;

/**
 * Base class for science pack items
 */
public class SciencePackItem extends Item implements ISciencePack {
    private final int tier;
    private final int researchValue;
    
    public SciencePackItem(int tier, int researchValue) {
        super(new Item.Properties());
        this.tier = tier;
        this.researchValue = researchValue;
    }
    
    @Override
    public int getTier() {
        return tier;
    }
    
    @Override
    public int getResearchValue() {
        return researchValue;
    }
    
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("item.frontierresearch.science_pack.tier", tier));
        tooltipComponents.add(Component.translatable("item.frontierresearch.science_pack.value", researchValue));
    }
}
