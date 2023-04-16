package com.ferriarnus.adventurersdimension.block;


import com.ferriarnus.adventurersdimension.blockentity.BlockEntityRegistry;
import com.ferriarnus.adventurersdimension.blockentity.DimensionAnchorBlockEntity;
import com.ferriarnus.adventurersdimension.menu.DimensionalAnchorMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class DimensionAnchorBlock extends Block implements EntityBlock {

    public DimensionAnchorBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            BlockEntity blockentity = pLevel.getBlockEntity(pPos);
            if (blockentity instanceof DimensionAnchorBlockEntity anchor) {
                MenuProvider p = new MenuProvider() {
                    @Override
                    public Component getDisplayName() {
                        return Component.translatable("screen.adventurersdimension.dimensionanchor");
                    }

                    @Override
                    public AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player pPlayer) {
                        return new DimensionalAnchorMenu(pContainerId, pInventory, pPos, pLevel);
                    }
                };
                NetworkHooks.openScreen((ServerPlayer) pPlayer, p, blockentity.getBlockPos());
            }
            return InteractionResult.CONSUME;
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return BlockEntityRegistry.DIMENSIONAL_ANCHOR.get().create(pPos, pState);
    }
}
