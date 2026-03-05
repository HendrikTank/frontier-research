package com.hendrictank.frontierresearch.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Utility class for validating multi-block structures
 * This will be used by multi-block lab implementations
 */
public class MultiBlockHelper {
    
    /**
     * Validates a multi-block structure against a pattern
     * 
     * @param level The world
     * @param origin The center/origin block position
     * @param pattern The structure pattern to validate
     * @return true if the structure is valid
     */
    public static boolean validateStructure(Level level, BlockPos origin, StructurePattern pattern) {
        for (Map.Entry<BlockPos, Predicate<BlockState>> entry : pattern.getPattern().entrySet()) {
            BlockPos pos = origin.offset(entry.getKey());
            BlockState state = level.getBlockState(pos);
            
            if (!entry.getValue().test(state)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Represents a multi-block structure pattern
     */
    public static class StructurePattern {
        private final Map<BlockPos, Predicate<BlockState>> pattern = new HashMap<>();
        
        /**
         * Adds a required block at a relative position
         * 
         * @param offset Offset from origin
         * @param requirement Block state requirement
         */
        public StructurePattern add(BlockPos offset, Predicate<BlockState> requirement) {
            pattern.put(offset, requirement);
            return this;
        }
        
        /**
         * Adds a required block type at a relative position
         */
        public StructurePattern add(int x, int y, int z, Block requiredBlock) {
            return add(new BlockPos(x, y, z), state -> state.is(requiredBlock));
        }
        
        /**
         * Adds an air requirement at a relative position
         */
        public StructurePattern addAir(int x, int y, int z) {
            return add(new BlockPos(x, y, z), BlockState::isAir);
        }
        
        public Map<BlockPos, Predicate<BlockState>> getPattern() {
            return pattern;
        }
    }
    
    /**
     * Example: Create a 3x3x3 structure pattern
     */
    public static StructurePattern createExample3x3x3() {
        StructurePattern pattern = new StructurePattern();
        
        // Bottom layer - all blocks
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                pattern.add(x, 0, z, net.minecraft.world.level.block.Blocks.IRON_BLOCK);
            }
        }
        
        // Middle layer - hollow
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                if (x == 0 && z == 0) {
                    pattern.addAir(x, 1, z); // Center is air
                } else {
                    pattern.add(x, 1, z, net.minecraft.world.level.block.Blocks.GLASS);
                }
            }
        }
        
        // Top layer - all blocks
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                pattern.add(x, 2, z, net.minecraft.world.level.block.Blocks.IRON_BLOCK);
            }
        }
        
        return pattern;
    }
}
