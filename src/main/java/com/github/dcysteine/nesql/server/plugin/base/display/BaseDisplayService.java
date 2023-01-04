package com.github.dcysteine.nesql.server.plugin.base.display;

import com.github.dcysteine.nesql.server.display.Icon;
import com.github.dcysteine.nesql.server.plugin.base.display.fluid.DisplayFluid;
import com.github.dcysteine.nesql.server.plugin.base.display.fluid.DisplayFluidGroup;
import com.github.dcysteine.nesql.server.plugin.base.display.fluid.DisplayFluidStack;
import com.github.dcysteine.nesql.server.plugin.base.display.fluid.DisplayFluidStackWithProbability;
import com.github.dcysteine.nesql.server.plugin.base.display.item.DisplayItem;
import com.github.dcysteine.nesql.server.plugin.base.display.item.DisplayItemGroup;
import com.github.dcysteine.nesql.server.plugin.base.display.item.DisplayItemStack;
import com.github.dcysteine.nesql.server.plugin.base.display.item.DisplayItemStackWithProbability;
import com.github.dcysteine.nesql.server.plugin.base.display.item.DisplayWildcardItemStack;
import com.github.dcysteine.nesql.server.plugin.base.display.recipe.DisplayRecipe;
import com.github.dcysteine.nesql.server.plugin.base.display.recipe.DisplayRecipeType;
import com.github.dcysteine.nesql.sql.base.fluid.FluidGroup;
import com.github.dcysteine.nesql.sql.base.fluid.FluidGroupRepository;
import com.github.dcysteine.nesql.sql.base.fluid.Fluid;
import com.github.dcysteine.nesql.sql.base.fluid.FluidRepository;
import com.github.dcysteine.nesql.sql.base.fluid.FluidStack;
import com.github.dcysteine.nesql.sql.base.fluid.FluidStackWithProbability;
import com.github.dcysteine.nesql.sql.base.item.ItemGroup;
import com.github.dcysteine.nesql.sql.base.item.ItemGroupRepository;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.base.item.ItemRepository;
import com.github.dcysteine.nesql.sql.base.item.ItemStack;
import com.github.dcysteine.nesql.sql.base.item.ItemStackWithProbability;
import com.github.dcysteine.nesql.sql.base.item.WildcardItemStack;
import com.github.dcysteine.nesql.sql.base.recipe.Recipe;
import com.github.dcysteine.nesql.sql.base.recipe.RecipeType;
import com.github.dcysteine.nesql.sql.base.recipe.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// TODO I should probably refactor this... somehow
/** Makes life easier for us by taking care of all required dependencies for display objects. */
@Service
public class BaseDisplayService {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemGroupRepository itemGroupRepository;

    @Autowired
    private FluidRepository fluidRepository;

    @Autowired
    private FluidGroupRepository fluidGroupRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    public DisplayItem buildDisplayItem(Item item) {
        return DisplayItem.create(item, itemRepository, itemGroupRepository, recipeRepository);
    }

    public Icon buildDisplayItemIcon(Item item) {
        return DisplayItem.buildIcon(item);
    }

    public DisplayItemStack buildDisplayItemStack(ItemStack itemStack) {
        return DisplayItemStack.create(itemStack);
    }

    public Icon buildDisplayItemStackIcon(ItemStack itemStack) {
        return DisplayItemStack.buildIcon(itemStack);
    }

    public DisplayItemStackWithProbability buildDisplayItemStackWithProbability(
            ItemStackWithProbability itemStack) {
        return DisplayItemStackWithProbability.create(itemStack);
    }

    public Icon buildDisplayItemStackWithProbabilityIcon(
            ItemStackWithProbability itemStack) {
        return DisplayItemStackWithProbability.buildIcon(itemStack);
    }

    public DisplayWildcardItemStack buildDisplayWildcardItemStack(
            WildcardItemStack wildcardItemStack) {
        return DisplayWildcardItemStack.create(wildcardItemStack, itemRepository);
    }

    public Icon buildDisplayWildcardItemStackIcon(
            WildcardItemStack wildcardItemStack) {
        return DisplayWildcardItemStack.buildIcon(wildcardItemStack, itemRepository);
    }

    public DisplayItemGroup buildDisplayItemGroup(ItemGroup itemGroup) {
        return DisplayItemGroup.create(itemGroup, itemRepository);
    }

    public Icon buildDisplayItemGroupIcon(ItemGroup itemGroup) {
        return DisplayItemGroup.buildIcon(itemGroup, itemRepository);
    }

    public DisplayFluid buildDisplayFluid(Fluid fluid) {
        return DisplayFluid.create(fluid, fluidGroupRepository, recipeRepository);
    }

    public Icon buildDisplayFluidIcon(Fluid fluid) {
        return DisplayFluid.buildIcon(fluid);
    }

    public DisplayFluidStack buildDisplayFluidStack(FluidStack fluidStack) {
        return DisplayFluidStack.create(fluidStack);
    }

    public Icon buildDisplayFluidStackIcon(FluidStack fluidStack) {
        return DisplayFluidStack.buildIcon(fluidStack);
    }

    public DisplayFluidStackWithProbability buildDisplayFluidStackWithProbability(
            FluidStackWithProbability fluidStack) {
        return DisplayFluidStackWithProbability.create(fluidStack);
    }

    public Icon buildDisplayFluidStackWithProbabilityIcon(
            FluidStackWithProbability fluidStack) {
        return DisplayFluidStackWithProbability.buildIcon(fluidStack);
    }

    public DisplayFluidGroup buildDisplayFluidGroup(FluidGroup fluidGroup) {
        return DisplayFluidGroup.create(fluidGroup);
    }

    public Icon buildDisplayFluidGroupIcon(FluidGroup fluidGroup) {
        return DisplayFluidGroup.buildIcon(fluidGroup);
    }

    public DisplayRecipe buildDisplayRecipe(Recipe recipe) {
        return DisplayRecipe.create(recipe, itemRepository);
    }

    public Icon buildDisplayRecipeIcon(Recipe recipe) {
        return DisplayRecipe.buildIcon(recipe);
    }

    public DisplayRecipeType buildDisplayRecipeType(RecipeType recipe) {
        return DisplayRecipeType.create(recipe);
    }
}
