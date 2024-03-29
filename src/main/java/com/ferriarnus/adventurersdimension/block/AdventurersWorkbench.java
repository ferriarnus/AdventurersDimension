package com.ferriarnus.adventurersdimension.block;


import com.ferriarnus.adventurersdimension.blockentity.BlockEntityRegistry;
import com.ferriarnus.adventurersdimension.blockentity.AdventurersWorkbenchBlockEntity;
import com.ferriarnus.adventurersdimension.menu.AdventurersWorkbenchMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class AdventurersWorkbench extends Block implements EntityBlock {

    public AdventurersWorkbench(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            BlockEntity blockentity = pLevel.getBlockEntity(pPos);
            if (blockentity instanceof AdventurersWorkbenchBlockEntity anchor) {
                BlockPos finalPPos = pPos;
                MenuProvider p = new MenuProvider() {
                    @Override
                    public Component getDisplayName() {
                        return Component.translatable("screen.adventurersdimension.adventurers_workbench");
                    }

                    @Override
                    public AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player pPlayer) {
                        return new AdventurersWorkbenchMenu(pContainerId, pInventory, finalPPos, pLevel);
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
        return BlockEntityRegistry.ADVENTURERS_WORKBENCH.get().create(pPos, pState);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        BlockEntity blockentity = pLevel.getBlockEntity(pPos);
        if (blockentity instanceof AdventurersWorkbenchBlockEntity anchor) {
            Containers.dropContents(pLevel, pPos, new RecipeWrapper(anchor.getHandler()));
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }
}
