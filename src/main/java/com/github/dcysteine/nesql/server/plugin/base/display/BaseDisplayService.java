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
import com.github.dcysteine.nesql.sql.base.fluid.Fluid;
import com.github.dcysteine.nesql.sql.base.fluid.FluidStack;
import com.github.dcysteine.nesql.sql.base.fluid.FluidStackWithProbability;
import com.github.dcysteine.nesql.sql.base.item.ItemGroup;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.base.item.ItemStack;
import com.github.dcysteine.nesql.sql.base.item.ItemStackWithProbability;
import com.github.dcysteine.nesql.sql.base.item.WildcardItemStack;
import com.github.dcysteine.nesql.sql.base.recipe.Recipe;
import com.github.dcysteine.nesql.sql.base.recipe.RecipeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Service providing convenient methods for building display objects. */
@Service
public class BaseDisplayService {
    @Autowired
    private BaseDisplayDeps deps;

    public DisplayItem buildDisplayItem(Item item) {
        return DisplayItem.create(item, deps);
    }

    public Icon buildDisplayItemIcon(Item item) {
        return DisplayItem.buildIcon(item, deps);
    }

    public DisplayItemStack buildDisplayItemStack(ItemStack itemStack) {
        return DisplayItemStack.create(itemStack, deps);
    }

    public Icon buildDisplayItemStackIcon(ItemStack itemStack) {
        return DisplayItemStack.buildIcon(itemStack, deps);
    }

    public DisplayItemStackWithProbability buildDisplayItemStackWithProbability(
            ItemStackWithProbability itemStack) {
        return DisplayItemStackWithProbability.create(itemStack, deps);
    }

    public Icon buildDisplayItemStackWithProbabilityIcon(
            ItemStackWithProbability itemStack) {
        return DisplayItemStackWithProbability.buildIcon(itemStack, deps);
    }

    public DisplayWildcardItemStack buildDisplayWildcardItemStack(
            WildcardItemStack wildcardItemStack) {
        return DisplayWildcardItemStack.create(wildcardItemStack, deps);
    }

    public Icon buildDisplayWildcardItemStackIcon(
            WildcardItemStack wildcardItemStack) {
        return DisplayWildcardItemStack.buildIcon(wildcardItemStack, deps);
    }

    public DisplayItemGroup buildDisplayItemGroup(ItemGroup itemGroup) {
        return DisplayItemGroup.create(itemGroup, deps);
    }

    public Icon buildDisplayItemGroupIcon(ItemGroup itemGroup) {
        return DisplayItemGroup.buildIcon(itemGroup, deps);
    }

    public DisplayFluid buildDisplayFluid(Fluid fluid) {
        return DisplayFluid.create(fluid, deps);
    }

    public Icon buildDisplayFluidIcon(Fluid fluid) {
        return DisplayFluid.buildIcon(fluid, deps);
    }

    public DisplayFluidStack buildDisplayFluidStack(FluidStack fluidStack) {
        return DisplayFluidStack.create(fluidStack, deps);
    }

    public Icon buildDisplayFluidStackIcon(FluidStack fluidStack) {
        return DisplayFluidStack.buildIcon(fluidStack, deps);
    }

    public DisplayFluidStackWithProbability buildDisplayFluidStackWithProbability(
            FluidStackWithProbability fluidStack) {
        return DisplayFluidStackWithProbability.create(fluidStack, deps);
    }

    public Icon buildDisplayFluidStackWithProbabilityIcon(
            FluidStackWithProbability fluidStack) {
        return DisplayFluidStackWithProbability.buildIcon(fluidStack, deps);
    }

    public DisplayFluidGroup buildDisplayFluidGroup(FluidGroup fluidGroup) {
        return DisplayFluidGroup.create(fluidGroup, deps);
    }

    public Icon buildDisplayFluidGroupIcon(FluidGroup fluidGroup) {
        return DisplayFluidGroup.buildIcon(fluidGroup, deps);
    }

    public DisplayRecipe buildDisplayRecipe(Recipe recipe) {
        return DisplayRecipe.create(recipe, deps);
    }

    public Icon buildDisplayRecipeIcon(Recipe recipe) {
        return DisplayRecipe.buildIcon(recipe, deps);
    }

    public DisplayRecipeType buildDisplayRecipeType(RecipeType recipeType) {
        return DisplayRecipeType.create(recipeType, deps);
    }

    public Icon buildDisplayRecipeTypeIcon(RecipeType recipeType) {
        return DisplayRecipeType.buildIcon(recipeType, deps);
    }
}
