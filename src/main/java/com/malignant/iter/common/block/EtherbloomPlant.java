package com.malignant.iter.common.block;

import com.malignant.iter.common.registry.ModBlocks;
import com.malignant.iter.common.registry.ModItems;
import com.malignant.iter.common.registry.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.Tags;

public class EtherbloomPlant extends CropBlock {

    public static final int MAX_AGE = 3;
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;

    public EtherbloomPlant(Properties pProperties) {
        super(pProperties);
    }

    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(7.0D, 0.0D, 7.0D, 9.0D, 2.0D, 9.0D),
            Block.box(6.0D, 0.0D, 6.0D, 10.D, 5.0D, 10.0D),
            Block.box(5.0D, 0.0D, 5.0D, 11.0D, 8.0D, 11.0D),
            Block.box(5.0D, 0.0D, 5.0D, 11.0D, 10.0D, 11.0D)};

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE_BY_AGE[this.getAge(pState)];
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return ModItems.ETHERBLOOM_SEEDS.get();
    }

    @Override
    public IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public boolean isRandomlyTicking(BlockState pState) {
        return true;
    }

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (!pLevel.isAreaLoaded(pPos, 1)) return;
        int range = 7;
        if (isOnGoodSoil(pLevel, pPos)){range = 10;}
        int i = this.getAge(pState);
        if (i < this.getMaxAge()) {
            float f = getGrowthSpeed(pState, pLevel, pPos);
            if (pLevel.random.nextInt(range) < 5) {return;};
            if (pRandom.nextInt((int) (25.0F / f) + 1) == 0) {
                pLevel.setBlock(pPos, this.getStateForAge(i + 1), 2);
            }
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader pLevel, BlockPos pPos, BlockState pState) {
        return (getAge(pState) < getMaxAge());
    }

    @Override
    public void performBonemeal(ServerLevel pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
        this.growCrops(pLevel, pPos, pState);
    }

    public void growCrops(Level pLevel, BlockPos pPos, BlockState pState) {
        int range = 7;
        if (isOnGoodSoil(pLevel, pPos)){range = 10;}
        if (pLevel.random.nextInt(range) >= 5) {
            int i = this.getAge(pState) + 1;
            if (i <= this.getMaxAge()) {
                pLevel.setBlock(pPos, this.getStateForAge(i), 2);
            }
        }
    }

    @Override
    public int getMaxAge() {
        return MAX_AGE;
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        BlockPos belowPos = pPos.below();
        BlockState belowState = pLevel.getBlockState(belowPos);
        return this.mayPlaceOn(belowState, pLevel, belowPos);
    }

    @Override
    protected boolean mayPlaceOn(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return pState.is(BlockTags.DIRT) || pState.getBlock() instanceof FarmBlock || pState.is(ModTags.Blocks.ETHERBLOOM_SOIL);
    }

    private boolean isOnGoodSoil(BlockGetter pLevel, BlockPos pPos) {
        BlockPos belowPos = pPos.below();
        BlockState belowState = pLevel.getBlockState(belowPos);
        return belowState.is(ModTags.Blocks.ETHERBLOOM_SOIL);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(AGE);
    }
}