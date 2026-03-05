package com.example.frontierresearch.registry;

import com.example.frontierresearch.FrontierResearch;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registers all items added by Frontier Research.
 *
 * <p>Science-pack items are registered here so they can be referenced by
 * {@link ModSciencePacks}.  Block items are registered automatically as
 * companions to their blocks.</p>
 */
public final class ModItems {

    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(FrontierResearch.MODID);

    // ------------------------------------------------------------------
    // Science Packs
    // ------------------------------------------------------------------

    /** Basic science pack – earliest research material, craftable by hand. */
    public static final DeferredItem<Item> BASIC_SCIENCE_PACK =
            ITEMS.registerSimpleItem("basic_science_pack", new Item.Properties().stacksTo(200));

    /** Automation science pack – requires crafting infrastructure. */
    public static final DeferredItem<Item> AUTOMATION_SCIENCE_PACK =
            ITEMS.registerSimpleItem("automation_science_pack", new Item.Properties().stacksTo(200));

    /** Applied science pack – mid-tier; requires automation science. */
    public static final DeferredItem<Item> APPLIED_SCIENCE_PACK =
            ITEMS.registerSimpleItem("applied_science_pack", new Item.Properties().stacksTo(200));

    /** Advanced science pack – high-tier; requires electric lab. */
    public static final DeferredItem<Item> ADVANCED_SCIENCE_PACK =
            ITEMS.registerSimpleItem("advanced_science_pack", new Item.Properties().stacksTo(200));

    /** Cryo science pack – endgame; requires cryo-lab. */
    public static final DeferredItem<Item> CRYO_SCIENCE_PACK =
            ITEMS.registerSimpleItem("cryo_science_pack", new Item.Properties().stacksTo(200));

    // ------------------------------------------------------------------
    // Block Items
    // ------------------------------------------------------------------

    public static final DeferredItem<BlockItem> RESEARCH_TABLE_ITEM =
            ITEMS.registerSimpleBlockItem("research_table", ModBlocks.RESEARCH_TABLE);

    public static final DeferredItem<BlockItem> BURNER_LAB_ITEM =
            ITEMS.registerSimpleBlockItem("burner_lab", ModBlocks.BURNER_LAB);

    private ModItems() {}

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}
