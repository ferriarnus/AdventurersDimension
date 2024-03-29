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

    public static final RegistryObject<BlockEntityType<AdventurersWorkbenchBlockEntity>> ADVENTURERS_WORKBENCH = BLOCK_ENTITIES.register("adventurers_workbench", () -> BlockEntityType.Builder.of(AdventurersWorkbenchBlockEntity::new, BlockRegistry.ADVENTURERS_WORKBENCH.get()).build(null));
}
