package com.ferriarnus.adventurersdimension.screen;

import com.ferriarnus.adventurersdimension.AdventurersDimension;
import com.ferriarnus.adventurersdimension.menu.MenuRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = AdventurersDimension.MODID, value = Dist.CLIENT)
public class ScreenRegistry {

    @SubscribeEvent
    static void register(FMLClientSetupEvent event) {
        MenuScreens.register(MenuRegistry.DIMENSIONAL_ANCHOR.get(), DilmensionalAnchorScreen::new);
    }
}
