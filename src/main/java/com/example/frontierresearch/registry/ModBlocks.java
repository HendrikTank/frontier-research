package com.example.frontierresearch.registry;

import com.example.frontierresearch.FrontierResearch;
import com.example.frontierresearch.block.BurnerLabBlock;
import com.example.frontierresearch.block.ResearchTableBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registers all blocks added by Frontier Research.
 */
public final class ModBlocks {

    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(FrontierResearch.MODID);

    // ------------------------------------------------------------------
    // Research Table – single-block, no fuel, low-tier research
    // ------------------------------------------------------------------
    public static final DeferredBlock<ResearchTableBlock> RESEARCH_TABLE =
            BLOCKS.register("research_table", () -> new ResearchTableBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.WOOD)
                            .sound(SoundType.WOOD)
                            .strength(2.5f)
                            .requiresCorrectToolForDrops()
            ));

    // ------------------------------------------------------------------
    // Burner Lab – single-block, fuel-driven research
    // ------------------------------------------------------------------
    public static final DeferredBlock<BurnerLabBlock> BURNER_LAB =
            BLOCKS.register("burner_lab", () -> new BurnerLabBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.STONE)
                            .sound(SoundType.STONE)
                            .strength(3.5f)
                            .requiresCorrectToolForDrops()
                            .lightLevel(state -> state.getValue(BurnerLabBlock.LIT) ? 13 : 0)
            ));

    private ModBlocks() {}

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
    }
}
