package org.nulladmin1.minecraftrivals;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class ThornlashWall extends Block {
    public ThornlashWall(Settings settings) {
        super(settings);
    }

    @Override
    public void onPlaced(World world, BlockPos blockPos, BlockState blockState, LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, blockPos, blockState, placer, itemStack);

        if (!world.isClient) {
            Direction facing = placer.getHorizontalFacing();
            createWall(world, blockPos, facing);
        }
    }

    private void createWall(World world, BlockPos blockPos, Direction facing) {
        for (int y = 0; y < 3; y++) {
            for (int i = -1; i <= 1; i++) {
                BlockPos target;

                if (facing == Direction.NORTH || facing == Direction.SOUTH) {
                    target = blockPos.add(i, y, 0);
                } else {
                    target = blockPos.add(0, y, i);
                }

                if (target.equals(blockPos) || world.getBlockState(target).isAir()) {
                    world.setBlockState(target, ModItems.THORNLASH_WALL_CHILD.getDefaultState());
                }
            }
        }
    }

}
