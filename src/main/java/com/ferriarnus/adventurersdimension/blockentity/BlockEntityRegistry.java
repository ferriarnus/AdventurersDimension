package com.ferriarnus.adventurersdimension.blockentity;

import com.ferriarnus.adventurersdimension.AdventurersDimension;
import com.ferriarnus.adventurersdimension.block.BlockRegistry;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityRegistry {

    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, AdventurersDimension.MODID);

    public static void register() {
        BLOCK_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<BlockEntityType<DimensionAnchorBlockEntity>> DIMENSIONAL_ANCHOR = BLOCK_ENTITIES.register("dimensonal_anchor", () -> BlockEntityType.Builder.of(DimensionAnchorBlockEntity::new, BlockRegistry.DIMENSION_ANCHOR.get()).build(null));
}
