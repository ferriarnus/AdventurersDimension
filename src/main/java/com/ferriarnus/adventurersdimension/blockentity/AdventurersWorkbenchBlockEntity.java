package com.ferriarnus.adventurersdimension.blockentity;

import com.ferriarnus.adventurersdimension.AdventurersDimension;
import com.ferriarnus.adventurersdimension.config.AdventureConfig;
import com.ferriarnus.adventurersdimension.dimensions.DimensionHelper;
import com.ferriarnus.adventurersdimension.recipe.DimensionRecipe;
import com.ferriarnus.adventurersdimension.recipe.RecipeRegistry;
import com.ferriarnus.adventurersdimension.saveddata.TimeSavedData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;

public class AdventurersWorkbenchBlockEntity extends BlockEntity {

    private ItemStackHandler handler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            if (level instanceof ServerLevel serverLevel) {
                loadDimension(serverLevel);
            }
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    };
    private LazyOptional<IItemHandler> lazy = LazyOptional.of(()-> handler);
    private ResourceKey<Level> levelResourceKey;
    private ResourceLocation copied;
    private boolean player;
    private long time;

    public AdventurersWorkbenchBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.ADVENTURERS_WORKBENCH.get(), pPos, pBlockState);
    }

    public void confirmLevel(ServerLevel serverLevel, ServerPlayer player) {
        if (player.hasPermissions(AdventureConfig.PERMISSION.get())) {
            ServerLevel oldLevel = loadDimension(serverLevel);
            makeOrLoadLevel(oldLevel, player);
            serverLevel.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    public void remake(ServerLevel serverLevel) {
        if (levelResourceKey != null && copied != null) {
            ServerLevel oldLevel = serverLevel.getServer().getLevel(ResourceKey.create(Registries.DIMENSION, copied));
            if (oldLevel == null) {
                return;
            }
            Holder<DimensionType> typeHolder = serverLevel.dimensionTypeRegistration();
            BiFunction<MinecraftServer, ResourceKey<LevelStem>, LevelStem> dimensionFactory =
                    (minecraftServer, levelStemResourceKey) -> {
                        return new LevelStem(typeHolder, oldLevel.getChunkSource().getGenerator());
                    };
            ServerLevel newLevel = DimensionHelper.getOrCreateLevel(serverLevel.getServer(), levelResourceKey, dimensionFactory);
            TimeSavedData savedtime = newLevel.getDataStorage().computeIfAbsent(TimeSavedData::load, () -> new TimeSavedData(time, serverLevel), "time");
            if (time == -1) { //Infinite time
                savedtime.setTime(time);
            }
            time = savedtime.getTime();
        }

    }

    /**
     * Makes or loads an adventurers level from another mods level.
     * @param serverLevel The provided level to copy.
     */
    public void makeOrLoadLevel(ServerLevel serverLevel, ServerPlayer serverplayer) {
        ResourceKey<Level> levelKey = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(AdventurersDimension.MODID,
                (this.player ? serverplayer.getName().getString() : "") + serverLevel.getLevel().dimension().location().getPath()));
        Holder<DimensionType> typeHolder = serverLevel.dimensionTypeRegistration();
        BiFunction<MinecraftServer, ResourceKey<LevelStem>, LevelStem> dimensionFactory =
                (minecraftServer, levelStemResourceKey) -> {
            return new LevelStem(typeHolder, serverLevel.getChunkSource().getGenerator());
        };
        this.levelResourceKey = levelKey;
        ServerLevel newLevel = DimensionHelper.getOrCreateLevel(serverLevel.getServer(), levelKey, dimensionFactory);
        TimeSavedData savedtime = newLevel.getDataStorage().computeIfAbsent(TimeSavedData::load, () -> new TimeSavedData(time, serverLevel), "time");
        time = savedtime.getTime();
    }

    public ServerLevel loadDimension(ServerLevel level) {
        this.levelResourceKey = null;
        this.copied = null;
        this.time = 0;
        if (level.dimension().location().getNamespace().equals(AdventurersDimension.MODID)) { //Don't allow inception
            return null;
        }
        Optional<DimensionRecipe> recipe = level.getRecipeManager().getRecipeFor(RecipeRegistry.DIMENSION_TYPE.get(), new RecipeWrapper(handler), level);
        AtomicReference<ResourceKey<Level>> atomicReference = new AtomicReference<>(null);
        recipe.ifPresent(r -> {
            this.player = r.isPlayer();
            if (r.getLevelResourceKey() == null || !level.getServer().levelKeys().contains(r.getLevelResourceKey())) {
                return;
            }
            time = r.getTime();
            atomicReference.set(r.getLevelResourceKey());
            copied = r.getLevelResourceKey().location();
        });
        setChanged();
        return atomicReference.get() == null ? null : level.getServer().getLevel(atomicReference.get());
    }

    public ItemStackHandler getHandler() {
        return handler;
    }

    public ResourceKey<Level> getLevelResourceKey() {
        return levelResourceKey;
    }

    public ResourceLocation getCopied() {
        return copied;
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        lazy.invalidate();
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.levelResourceKey = null;
        if (pTag.contains("levelResourceKey")) {
            this.levelResourceKey = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(pTag.getString("levelResourceKey")));
        }
        this.copied = null;
        if (pTag.contains("copied")) {
            this.copied = new ResourceLocation(pTag.getString("copied"));
        }
        this.player = pTag.getBoolean("player");
        handler.deserializeNBT(pTag.getCompound("item"));
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        if (this.levelResourceKey != null) {
            pTag.putString("levelResourceKey", levelResourceKey.location().toString());
        }
        if (this.copied != null) {
            pTag.putString("copied", copied.toString());
        }
        pTag.putBoolean("player", player);
        pTag.put("item", handler.serializeNBT());
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap== ForgeCapabilities.ITEM_HANDLER) {
            return lazy.cast();
        }
        return super.getCapability(cap, side);
    }

    public long getTime() {
        return time;
    }
}
