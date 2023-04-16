package com.ferriarnus.adventurersdimension;

import com.ferriarnus.adventurersdimension.block.BlockRegistry;
import com.ferriarnus.adventurersdimension.blockentity.BlockEntityRegistry;
import com.ferriarnus.adventurersdimension.config.AdventureConfig;
import com.ferriarnus.adventurersdimension.dimensions.DimensionHelper;
import com.ferriarnus.adventurersdimension.item.ItemRegistry;
import com.ferriarnus.adventurersdimension.menu.MenuRegistry;
import com.ferriarnus.adventurersdimension.network.AdventureNetwork;
import com.ferriarnus.adventurersdimension.recipe.RecipeRegistry;
import com.ferriarnus.adventurersdimension.saveddata.TimeSavedData;
import com.mojang.logging.LogUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.slf4j.Logger;

@Mod.EventBusSubscriber
@Mod(AdventurersDimension.MODID)
public class AdventurersDimension
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "adventurersdimension";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    public AdventurersDimension() {
        ItemRegistry.register();
        BlockRegistry.register();
        BlockEntityRegistry.register();
        MenuRegistry.register();
        RecipeRegistry.register();
        AdventureNetwork.registerChannel();

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, AdventureConfig.SPEC);
    }

    @SubscribeEvent
    static void removeLevel(TickEvent.LevelTickEvent event) {
        if (event.level instanceof ServerLevel serverLevel) {
            if (serverLevel.dimension().location().getNamespace().equals(MODID)) {
                TimeSavedData time = serverLevel.getDataStorage().computeIfAbsent(TimeSavedData::load, () -> new TimeSavedData(20L, serverLevel), "time");
                if (time.getTime() <= serverLevel.getGameTime()) {
                    DimensionHelper.markDimensionForUnregistration(serverLevel.getServer(), serverLevel.dimension());
                }
            }
            if (serverLevel.dimension().equals(Level.OVERWORLD) && event.phase == TickEvent.Phase.END) {
                DimensionHelper.unregisterScheduledDimensions(serverLevel.getServer());
            }
        }
    }
}
