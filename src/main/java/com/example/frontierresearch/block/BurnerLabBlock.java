package com.example.frontierresearch.block;

import com.example.frontierresearch.blockentity.BurnerLabBlockEntity;
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
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

/**
 * Single-block burner lab that consumes solid fuel to power research.
 * Belongs to {@link com.example.frontierresearch.api.LabTier#BURNER_LAB}.
 *
 * <p>The block has a {@code lit} boolean state property that drives its
 * light level and can be used to change the model/texture when active.</p>
 */
public class BurnerLabBlock extends BaseEntityBlock {

    public static final Component TITLE = Component.translatable("container.frontierresearch.burner_lab");

    /** {@code true} when the lab is actively consuming fuel and researching. */
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public BurnerLabBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(this.stateDefinition.any().setValue(LIT, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<net.minecraft.world.level.block.Block, BlockState> builder) {
        builder.add(LIT);
    }

    // ------------------------------------------------------------------
    // Block Entity
    // ------------------------------------------------------------------

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BurnerLabBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) return null;
        return createTickerHelper(type, ModBlockEntities.BURNER_LAB.get(),
                BurnerLabBlockEntity::tick);
    }

    // ------------------------------------------------------------------
    // Interaction
    // ------------------------------------------------------------------

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level,
            BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof BurnerLabBlockEntity labEntity) {
                serverPlayer.openMenu(labEntity, pos);
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
