package com.example.frontierresearch.block;

import com.example.frontierresearch.blockentity.ResearchTableBlockEntity;
import com.example.frontierresearch.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

/**
 * The Research Table is a single-block lab that processes science packs at
 * {@link com.example.frontierresearch.api.LabTier#RESEARCH_TABLE} tier without
 * requiring any fuel.  Right-clicking it opens the research-table GUI.
 */
public class ResearchTableBlock extends BaseEntityBlock {

    public static final Component TITLE = Component.translatable("container.frontierresearch.research_table");

    public ResearchTableBlock(Properties properties) {
        super(properties);
    }

    // ------------------------------------------------------------------
    // Block Entity
    // ------------------------------------------------------------------

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ResearchTableBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) return null;
        return createTickerHelper(type, ModBlockEntities.RESEARCH_TABLE.get(),
                ResearchTableBlockEntity::tick);
    }

    // ------------------------------------------------------------------
    // Interaction
    // ------------------------------------------------------------------

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level,
            BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof ResearchTableBlockEntity tableEntity) {
                serverPlayer.openMenu(tableEntity, pos);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    // ------------------------------------------------------------------
    // Rendering
    // ------------------------------------------------------------------

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}
