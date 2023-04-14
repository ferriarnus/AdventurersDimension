package com.ferriarnus.adventurersdimension.block;


import com.ferriarnus.adventurersdimension.dimensions.DimensionHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.phys.BlockHitResult;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public class DimensionAnchorBlock extends Block {

    public DimensionAnchorBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel instanceof ServerLevel serverLevel) {
            ServerLevel dimension = serverLevel.getServer().getLevel(Level.OVERWORLD);
            ResourceKey<Level> levelKey = ResourceKey.create(Registries.DIMENSION, new ResourceLocation("test"));
            Holder<DimensionType> typeHolder = dimension.dimensionTypeRegistration();
            BiFunction<MinecraftServer, ResourceKey<LevelStem>, LevelStem> dimensionFactory =
                    (minecraftServer, levelStemResourceKey) -> {
                        return new LevelStem(typeHolder, dimension.getChunkSource().getGenerator());
                    };
            ServerLevel newDimension = DimensionHelper.getOrCreateLevel(serverLevel.getServer(), levelKey, dimensionFactory);
            //pPlayer.changeDimension(newDimension);

        }
        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }
}
