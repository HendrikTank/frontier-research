package com.hendrictank.frontierresearch.blockentity;

import com.hendrictank.frontierresearch.FrontierResearch;
import com.hendrictank.frontierresearch.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registry for all mod block entities
 */
public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = 
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, FrontierResearch.MODID);
    
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ResearchTableBlockEntity>> RESEARCH_TABLE =
            BLOCK_ENTITIES.register("research_table", () -> 
                    BlockEntityType.Builder.of(ResearchTableBlockEntity::new, 
                            ModBlocks.RESEARCH_TABLE.get()).build(null));
    
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BurnerLabBlockEntity>> BURNER_LAB =
            BLOCK_ENTITIES.register("burner_lab", () -> 
                    BlockEntityType.Builder.of(BurnerLabBlockEntity::new, 
                            ModBlocks.BURNER_LAB.get()).build(null));
}
