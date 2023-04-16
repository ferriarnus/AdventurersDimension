package com.ferriarnus.adventurersdimension.network;

import com.ferriarnus.adventurersdimension.AdventurersDimension;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class AdventureNetwork {
    public static SimpleChannel INSTANCE;
    private static int id = -1;

    private static int getId() {
        id++;
        return id;
    }

    public static void registerChannel() {
        INSTANCE = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(AdventurersDimension.MODID, "channel"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE.registerMessage(getId(), PacketSyncDimensionListChanges.class, PacketSyncDimensionListChanges::toBytes, PacketSyncDimensionListChanges::new, PacketSyncDimensionListChanges::handle);
        INSTANCE.registerMessage(getId(), CreateDimensionPacket.class, CreateDimensionPacket::write, CreateDimensionPacket::new, CreateDimensionPacket::handle);
    }
}
