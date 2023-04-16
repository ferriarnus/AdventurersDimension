package com.ferriarnus.adventurersdimension;

import com.ferriarnus.adventurersdimension.block.BlockRegistry;
import com.ferriarnus.adventurersdimension.blockentity.BlockEntityRegistry;
import com.ferriarnus.adventurersdimension.config.AdventureConfig;
import com.ferriarnus.adventurersdimension.item.ItemRegistry;
import com.ferriarnus.adventurersdimension.menu.MenuRegistry;
import com.ferriarnus.adventurersdimension.network.AdventureNetwork;
import com.ferriarnus.adventurersdimension.recipe.RecipeRegistry;
import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
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
}
