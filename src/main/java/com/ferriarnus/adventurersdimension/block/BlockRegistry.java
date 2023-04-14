package com.ferriarnus.adventurersdimension.block;

import com.ferriarnus.adventurersdimension.AdventurersDimension;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockRegistry {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, AdventurersDimension.MODID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AdventurersDimension.MODID);

    public static void register() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<DimensionAnchorBlock> DIMENSION_ANCHOR = BLOCKS.register("dimensional_anchor", () -> new DimensionAnchorBlock(BlockBehaviour.Properties.of(Material.STONE)));
    public static final RegistryObject<BlockItem> DIMENSION_ANCHOR_ITEM = ITEMS.register("dimensional_anchor", () -> new BlockItem(DIMENSION_ANCHOR.get(), new Item.Properties()));
}
