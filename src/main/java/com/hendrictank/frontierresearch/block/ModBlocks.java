package com.hendrictank.frontierresearch.block;

import com.hendrictank.frontierresearch.FrontierResearch;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import com.hendrictank.frontierresearch.item.ModItems;

/**
 * Registry for all mod blocks
 */
public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = 
            DeferredRegister.createBlocks(FrontierResearch.MODID);
    
    // Research Table
    public static final DeferredBlock<Block> RESEARCH_TABLE = BLOCKS.register("research_table",
            () -> new ResearchTableBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .strength(2.5F)
                    .sound(SoundType.WOOD)));
    
    public static final DeferredItem<BlockItem> RESEARCH_TABLE_ITEM = 
            ModItems.ITEMS.registerSimpleBlockItem("research_table", RESEARCH_TABLE);
    
    // Burner Lab
    public static final DeferredBlock<Block> BURNER_LAB = BLOCKS.register("burner_lab",
            () -> new BurnerLabBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(3.5F)
                    .sound(SoundType.METAL)
                    .requiresCorrectToolForDrops()));
    
    public static final DeferredItem<BlockItem> BURNER_LAB_ITEM = 
            ModItems.ITEMS.registerSimpleBlockItem("burner_lab", BURNER_LAB);
}
