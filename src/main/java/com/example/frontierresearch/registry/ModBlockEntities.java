package com.example.frontierresearch.registry;

import com.example.frontierresearch.FrontierResearch;
import com.example.frontierresearch.blockentity.BurnerLabBlockEntity;
import com.example.frontierresearch.blockentity.ResearchTableBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registers all {@link BlockEntityType}s for Frontier Research.
 */
public final class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, FrontierResearch.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ResearchTableBlockEntity>> RESEARCH_TABLE =
            BLOCK_ENTITIES.register("research_table", () ->
                    BlockEntityType.Builder
                            .of(ResearchTableBlockEntity::new, ModBlocks.RESEARCH_TABLE.get())
                            .build(null)
            );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BurnerLabBlockEntity>> BURNER_LAB =
            BLOCK_ENTITIES.register("burner_lab", () ->
                    BlockEntityType.Builder
                            .of(BurnerLabBlockEntity::new, ModBlocks.BURNER_LAB.get())
                            .build(null)
            );

    private ModBlockEntities() {}

    public static void register(IEventBus modEventBus) {
        BLOCK_ENTITIES.register(modEventBus);
    }
}
