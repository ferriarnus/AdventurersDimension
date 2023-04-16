package com.ferriarnus.adventurersdimension.recipe;

import com.ferriarnus.adventurersdimension.AdventurersDimension;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RecipeRegistry {

    private static final DeferredRegister<RecipeType<?>> RECIPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, AdventurersDimension.MODID);
    private static final DeferredRegister<RecipeSerializer<?>> SERIALIZER = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, AdventurersDimension.MODID);

    public static void register() {
        RECIPES.register(FMLJavaModLoadingContext.get().getModEventBus());
        SERIALIZER.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<RecipeType<DimensionRecipe>> DIMENSION_TYPE = RECIPES.register("dimension", () -> new RecipeTypeImpl<>(new ResourceLocation(AdventurersDimension.MODID,"dimension")));
    public static final RegistryObject<RecipeSerializer<DimensionRecipe>> DIMENSION_SERIALIZER = SERIALIZER.register("dimension", () -> DimensionRecipe.SERIALIZER);

    private record RecipeTypeImpl<T extends Recipe<?>>(ResourceLocation rl) implements RecipeType<T> {

        @Override
            public String toString() {
                return rl.toString();
            }
        }
}
