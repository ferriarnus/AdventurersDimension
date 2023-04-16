package com.ferriarnus.adventurersdimension.item;

import com.ferriarnus.adventurersdimension.AdventurersDimension;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry {

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AdventurersDimension.MODID);

    public static void register() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<AdventureCompass> COMPASS = ITEMS.register("adventure_compass", () -> new AdventureCompass(new Item.Properties().stacksTo(1)));
}
