package com.ferriarnus.adventurersdimension.network;

import com.ferriarnus.adventurersdimension.blockentity.AdventurersWorkbenchBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CreateDimensionPacket {

    private final BlockPos pos;
    private final ResourceKey<Level> level;

    public CreateDimensionPacket(BlockPos pos, ResourceKey<Level> level) {
        this.pos = pos;
        this.level = level;

    }

    public CreateDimensionPacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
        this.level = buf.readResourceKey(Registries.DIMENSION);
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeResourceKey(level);
    }

    public static void handle(CreateDimensionPacket packet, Supplier<NetworkEvent.Context> contextGetter) {
        contextGetter.get().enqueueWork(() -> {
            MinecraftServer server = contextGetter.get().getSender().server;
            BlockEntity be = server.getLevel(packet.level).getBlockEntity(packet.pos);
            if (be instanceof AdventurersWorkbenchBlockEntity anchor) {
                anchor.confirmLevel(server.getLevel(packet.level), contextGetter.get().getSender());
            }
        });
        contextGetter.get().setPacketHandled(true);
    }
}
