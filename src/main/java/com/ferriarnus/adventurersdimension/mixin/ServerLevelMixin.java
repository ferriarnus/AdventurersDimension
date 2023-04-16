package com.ferriarnus.adventurersdimension.mixin;

import com.ferriarnus.adventurersdimension.AdventurersDimension;
import com.mojang.datafixers.DataFixer;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.util.ProgressListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.function.BiFunction;
import java.util.function.Supplier;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin extends Level {

    @Unique
    private long seed;
    @Unique
    private Path path;

    @Shadow @Nonnull public abstract MinecraftServer getServer();

    @Shadow @Final private ServerLevelData serverLevelData;

    protected ServerLevelMixin(WritableLevelData p_270739_, ResourceKey<Level> p_270683_, RegistryAccess p_270200_, Holder<DimensionType> p_270240_, Supplier<ProfilerFiller> p_270692_, boolean p_270904_, boolean p_270470_, long p_270248_, int p_270466_) {
        super(p_270739_, p_270683_, p_270200_, p_270240_, p_270692_, p_270904_, p_270470_, p_270248_, p_270466_);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void load(MinecraftServer pServer, Executor pDispatcher, LevelStorageSource.LevelStorageAccess pLevelStorageAccess, ServerLevelData pServerLevelData, ResourceKey pDimensionKey, LevelStem pLevelStem, ChunkProgressListener pProgressListener, boolean pIsDebug, long pSeed, List pCustomSpawners, boolean pTickTime, CallbackInfo ci) {
        loadSeed(pLevelStorageAccess);
    }

    @Inject(method = "getSeed", at = @At("RETURN"), cancellable = true)
    public void getSeed(CallbackInfoReturnable<Long> cir) {
        if (dimension().location().getNamespace().equals(AdventurersDimension.MODID)) {
            cir.setReturnValue(seed);
        }
    }

    @Inject(method = "save", at = @At("TAIL"))
    public void safeDate(ProgressListener pProgress, boolean pFlush, boolean pSkipSave, CallbackInfo ci) throws IOException {
        File pFile = path.toFile();
        if (!pFile.exists()) {
            pFile.createNewFile();
        }
        saveToFile(pFile);
    }

    public void loadSeed(LevelStorageSource.LevelStorageAccess access) {
        File file1 = access.getDimensionPath(dimension()).resolve("data").toFile();
        file1.mkdirs();
        path = file1.toPath().resolve("adventure.dat");
        readLevelData(path, (path, dataFixer) -> {
            try {
                CompoundTag compoundTag = NbtIo.readCompressed(path.toFile());
                seed = compoundTag.getLong("seed");
            } catch (Exception e) {
                AdventurersDimension.LOGGER.error("Exception reading {}", path, e);
            }
            return ""; // Return non-null to prevent level.dat-);
        });
    }

    <T> T readLevelData(Path path, BiFunction<Path, DataFixer, T> pLevelDatReader) {
        if (!Files.exists(path)) {
            seed = new Random().nextLong();
            return (T)null;
        } else {
            if (Files.exists(path)) {
                T t = pLevelDatReader.apply(path, this.getServer().getFixerUpper());
                if (t != null) {
                    return t;
                }
            }

            return (T)(Files.exists(path) ? pLevelDatReader.apply(path, this.getServer().getFixerUpper()) : null);
        }
    }

    public void saveToFile(File pFile) {
            CompoundTag compoundtag = new CompoundTag();
            compoundtag.putLong("seed", seed);
            NbtUtils.addCurrentDataVersion(compoundtag);
            try {
                NbtIo.writeCompressed(compoundtag, pFile);
            } catch (IOException ioexception) {
                AdventurersDimension.LOGGER.error("Could not save data {}", this, ioexception);
            }

    }
}
