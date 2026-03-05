package com.hendrictank.frontierresearch.item;

import com.hendrictank.frontierresearch.FrontierResearch;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registry for all mod items
 */
public class ModItems {
    public static final DeferredRegister.Items ITEMS = 
            DeferredRegister.createItems(FrontierResearch.MODID);
    
    // Science Packs
    public static final DeferredItem<SciencePackItem> BASIC_SCIENCE_PACK = 
            ITEMS.register("basic_science_pack", () -> new SciencePackItem(0, 10));
    
    public static final DeferredItem<SciencePackItem> ADVANCED_SCIENCE_PACK = 
            ITEMS.register("advanced_science_pack", () -> new SciencePackItem(1, 25));
}
