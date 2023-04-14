package com.ferriarnus.adventurersdimension.network;

import com.ferriarnus.adventurersdimension.AdventurersDimension;
import com.ferriarnus.adventurersdimension.dimensions.PacketSyncDimensionListChanges;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class AdventureNetwork {
    public static SimpleChannel INSTANCE;

    public static void registerChannel() {
        INSTANCE = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(AdventurersDimension.MODID, "channel"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE.registerMessage(0, PacketSyncDimensionListChanges.class, PacketSyncDimensionListChanges::toBytes, PacketSyncDimensionListChanges::new, PacketSyncDimensionListChanges::handle);
    }
}
