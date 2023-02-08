package com.github.dcysteine.nesql.server.plugin.base.display;

import com.github.dcysteine.nesql.server.common.display.Icon;
import com.github.dcysteine.nesql.server.common.service.DisplayService;
import com.github.dcysteine.nesql.server.plugin.base.display.fluid.DisplayFluid;
import com.github.dcysteine.nesql.server.plugin.base.display.fluid.DisplayFluidGroup;
import com.github.dcysteine.nesql.server.plugin.base.display.fluid.DisplayFluidStack;
import com.github.dcysteine.nesql.server.plugin.base.display.fluid.DisplayFluidStackWithProbability;
import com.github.dcysteine.nesql.server.plugin.base.display.item.DisplayItem;
import com.github.dcysteine.nesql.server.plugin.base.display.item.DisplayItemGroup;
import com.github.dcysteine.nesql.server.plugin.base.display.item.DisplayItemStack;
import com.github.dcysteine.nesql.server.plugin.base.display.item.DisplayItemStackWithProbability;
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
import com.github.dcysteine.nesql.sql.base.recipe.Recipe;
import com.github.dcysteine.nesql.sql.base.recipe.RecipeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Service providing convenient methods for building display objects. */
@Service
public class BaseDisplayFactory {
    @Autowired
    private DisplayService service;

    public DisplayItem buildDisplayItem(Item item) {
        return DisplayItem.create(item, service);
    }

    public Icon buildDisplayItemIcon(Item item) {
        return DisplayItem.buildIcon(item, service);
    }

    public DisplayItemStack buildDisplayItemStack(ItemStack itemStack) {
        return DisplayItemStack.create(itemStack, service);
    }

    public Icon buildDisplayItemStackIcon(ItemStack itemStack) {
        return DisplayItemStack.buildIcon(itemStack, service);
    }

    public DisplayItemStackWithProbability buildDisplayItemStackWithProbability(
            ItemStackWithProbability itemStack) {
        return DisplayItemStackWithProbability.create(itemStack, service);
    }

    public Icon buildDisplayItemStackWithProbabilityIcon(
            ItemStackWithProbability itemStack) {
        return DisplayItemStackWithProbability.buildIcon(itemStack, service);
    }

    public DisplayItemGroup buildDisplayItemGroup(ItemGroup itemGroup) {
        return DisplayItemGroup.create(itemGroup, service);
    }

    public Icon buildDisplayItemGroupIcon(ItemGroup itemGroup) {
        return DisplayItemGroup.buildIcon(itemGroup, service);
    }

    public DisplayFluid buildDisplayFluid(Fluid fluid) {
        return DisplayFluid.create(fluid, service);
    }

    public Icon buildDisplayFluidIcon(Fluid fluid) {
        return DisplayFluid.buildIcon(fluid, service);
    }

    public DisplayFluidStack buildDisplayFluidStack(FluidStack fluidStack) {
        return DisplayFluidStack.create(fluidStack, service);
    }

    public Icon buildDisplayFluidStackIcon(FluidStack fluidStack) {
        return DisplayFluidStack.buildIcon(fluidStack, service);
    }

    public DisplayFluidStackWithProbability buildDisplayFluidStackWithProbability(
            FluidStackWithProbability fluidStack) {
        return DisplayFluidStackWithProbability.create(fluidStack, service);
    }

    public Icon buildDisplayFluidStackWithProbabilityIcon(
            FluidStackWithProbability fluidStack) {
        return DisplayFluidStackWithProbability.buildIcon(fluidStack, service);
    }

    public DisplayFluidGroup buildDisplayFluidGroup(FluidGroup fluidGroup) {
        return DisplayFluidGroup.create(fluidGroup, service);
    }

    public Icon buildDisplayFluidGroupIcon(FluidGroup fluidGroup) {
        return DisplayFluidGroup.buildIcon(fluidGroup, service);
    }

    public DisplayRecipe buildDisplayRecipe(Recipe recipe) {
        return DisplayRecipe.create(recipe, service);
    }

    public Icon buildDisplayRecipeIcon(Recipe recipe) {
        return DisplayRecipe.buildIcon(recipe, service);
    }

    public DisplayRecipeType buildDisplayRecipeType(RecipeType recipeType) {
        return DisplayRecipeType.create(recipeType, service);
    }

    public Icon buildDisplayRecipeTypeIcon(RecipeType recipeType) {
        return DisplayRecipeType.buildIcon(recipeType, service);
    }
}
