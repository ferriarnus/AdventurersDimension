package com.ferriarnus.adventurersdimension.dimensions;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.PlayerRespawnLogic;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.Heightmap;

public class SpawnHelper {

    public static void getSpawn(ServerLevel pLevel, BlockPos.MutableBlockPos pos) {
        ServerChunkCache serverchunkcache = pLevel.getChunkSource();
        ChunkPos chunkpos = new ChunkPos(pos);
        int i = serverchunkcache.getGenerator().getSpawnHeight(pLevel);
        if (i < pLevel.getMinBuildHeight()) {
            BlockPos blockpos = chunkpos.getWorldPosition();
            i = pLevel.getHeight(Heightmap.Types.WORLD_SURFACE, blockpos.getX() + 8, blockpos.getZ() + 8);
        }

        pos.set(chunkpos.getWorldPosition().offset(8, i, 8));
        int k1 = 0;
        int j = 0;
        int k = 0;
        int l = -1;
        int i1 = 5;

        for(int j1 = 0; j1 < Mth.square(11); ++j1) {
            if (k1 >= -5 && k1 <= 5 && j >= -5 && j <= 5) {
                BlockPos blockpos1 = PlayerRespawnLogic.getSpawnPosInChunk(pLevel, new ChunkPos(chunkpos.x + k1, chunkpos.z + j));
                if (blockpos1 != null) {
                    pos.set(blockpos1);
                    break;
                }
            }

            if (k1 == j || k1 < 0 && k1 == -j || k1 > 0 && k1 == 1 - j) {
                int l1 = k;
                k = -l;
                l = l1;
            }

            k1 += k;
            j += l;
        }
    }
}
