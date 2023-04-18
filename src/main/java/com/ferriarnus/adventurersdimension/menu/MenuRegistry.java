package com.ferriarnus.adventurersdimension.menu;

import com.ferriarnus.adventurersdimension.AdventurersDimension;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MenuRegistry {
    private static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, AdventurersDimension.MODID);

    public static void register() {
        MENUS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<MenuType<AdventurersWorkbenchMenu>> ADVENTURERS_WORKBENCH = MENUS.register("adventurers_workbench", () -> IForgeMenuType.create(AdventurersWorkbenchMenu::new));
}
