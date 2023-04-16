package com.ferriarnus.adventurersdimension.recipe;

import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.Nullable;

public class DimensionRecipe implements Recipe<RecipeWrapper> {

    public static final Serializer SERIALIZER = new Serializer();
    private final ResourceLocation id;
    private final Ingredient input;
    private final ResourceKey<Level> levelResourceKey;
    private final boolean player;

    public DimensionRecipe(ResourceLocation id, Ingredient input, ResourceKey<Level> levelResourceKey, boolean player) {
        this.id = id;
        this.input = input;
        this.levelResourceKey = levelResourceKey;
        this.player = player;
    }

    @Override
    public boolean matches(RecipeWrapper pContainer, Level pLevel) {
        return input.test(pContainer.getItem(0));
    }

    @Override
    public ItemStack assemble(RecipeWrapper p_44001_, RegistryAccess p_267165_) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess p_267052_) {
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(this.input);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeRegistry.DIMENSION_TYPE.get();
    }

    public ResourceKey<Level> getLevelResourceKey() {
        return levelResourceKey;
    }

    public boolean isPlayer() {
        return player;
    }

    static class Serializer implements RecipeSerializer<DimensionRecipe> {

        @Override
        public DimensionRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            Ingredient input = Ingredient.fromJson(pSerializedRecipe.get("input"));
            ResourceKey<Level> levelResourceKey = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(pSerializedRecipe.get("dimension").getAsString()));
            boolean player = pSerializedRecipe.get("player").getAsBoolean();
            return new DimensionRecipe(pRecipeId, input, levelResourceKey, player);
        }

        @Override
        public @Nullable DimensionRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            Ingredient input = Ingredient.fromNetwork(pBuffer);
            ResourceKey<Level> levelResourceKey = pBuffer.readResourceKey(Registries.DIMENSION);
            boolean player = pBuffer.readBoolean();
            return new DimensionRecipe(pRecipeId, input, levelResourceKey, player);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, DimensionRecipe pRecipe) {
            pRecipe.input.toNetwork(pBuffer);
            pBuffer.writeResourceKey(pRecipe.levelResourceKey);
            pBuffer.writeBoolean(pRecipe.player);
        }
    }
}
