package com.ferriarnus.adventurersdimension.item;

import com.ferriarnus.adventurersdimension.blockentity.DimensionAnchorBlockEntity;
import com.ferriarnus.adventurersdimension.dimensions.SpawnHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Collections;

public class AdventureCompass extends Item {
    public AdventureCompass(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        ItemStack stack = pContext.getItemInHand();
        BlockEntity be = level.getBlockEntity(pContext.getClickedPos());
        if (be instanceof DimensionAnchorBlockEntity anchor) {
            ResourceKey<Level> levelResourceKey = anchor.getLevelResourceKey();
            if (levelResourceKey != null && !level.isClientSide) {
                CompoundTag tag = new CompoundTag();
                tag.putLong("pos", pContext.getClickedPos().asLong());
                tag.putString("level", level.dimension().location().toString());
                tag.putString("to", levelResourceKey.location().toString());
                stack.getOrCreateTag().put("adventure", tag);
                ServerLevel newLevel = level.getServer().getLevel(levelResourceKey);
                Player player = pContext.getPlayer();
                BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(player.position().x(), player.position().y(), player.position().z());
                SpawnHelper.getSpawn(newLevel, pos);
                player.teleportTo(newLevel, pos.getX(), pos.getY(), pos.getZ(), Collections.emptySet(), player.getYRot(), player.getXRot());
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.CONSUME;
        }
        return super.useOn(pContext);
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 64;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.CUSTOM;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pUsedHand);
        pPlayer.startUsingItem(pUsedHand);
        return InteractionResultHolder.consume(itemstack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        if (pLevel instanceof ServerLevel serverLevel) {
            CompoundTag tag = pStack.getOrCreateTag();
            if (tag.contains("adventure")) {
                CompoundTag adventure = tag.getCompound("adventure");
                BlockPos pos = BlockPos.of(adventure.getLong("pos"));
                ResourceKey<Level> levelkey = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(adventure.getString("level")));
                ResourceKey<Level> to = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(adventure.getString("to")));
                if (!to.equals(serverLevel.dimension())) { //Only allow travel from inside the dimension
                    if (pLivingEntity instanceof ServerPlayer player) {
                        player.sendSystemMessage(Component.translatable("chat.adventurersdimension.wrongdimension"));
                    }
                    return pStack;
                }
                ServerLevel returnLevel = serverLevel.getServer().getLevel(levelkey);
                if (returnLevel == null) {
                    if (pLivingEntity instanceof ServerPlayer player) {
                        pos = player.getRespawnPosition();
                        returnLevel = serverLevel.getServer().getLevel(player.getRespawnDimension());
                    }
                }
                pLivingEntity.teleportTo(returnLevel, pos.getX(), pos.getY() + 1.2, pos.getZ(), Collections.emptySet(), pLivingEntity.getXRot(), pLivingEntity.getYRot() );
            }
        }
        return pStack;
    }
}
