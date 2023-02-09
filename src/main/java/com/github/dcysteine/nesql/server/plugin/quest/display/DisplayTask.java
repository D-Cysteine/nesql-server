package com.github.dcysteine.nesql.server.plugin.quest.display;

import com.github.dcysteine.nesql.server.common.display.Icon;
import com.github.dcysteine.nesql.server.common.service.DisplayService;
import com.github.dcysteine.nesql.server.plugin.base.display.fluid.DisplayFluidStack;
import com.github.dcysteine.nesql.server.plugin.base.display.item.DisplayItemGroup;
import com.github.dcysteine.nesql.sql.quest.Task;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

@AutoValue
public abstract class DisplayTask implements Comparable<DisplayTask> {
    public static DisplayTask create(Task task, DisplayService service) {
        ImmutableList<Icon> items =
                task.getItems().stream()
                        .map(itemGroup -> DisplayItemGroup.buildIcon(itemGroup, service))
                        .collect(ImmutableList.toImmutableList());
        ImmutableList<Icon> fluids =
                task.getFluids().stream()
                        .map(fluidStack -> DisplayFluidStack.buildIcon(fluidStack, service))
                        .collect(ImmutableList.toImmutableList());

        return new AutoValue_DisplayTask(task, items, fluids);
    }

    public abstract Task getTask();
    public abstract ImmutableList<Icon> items();
    public abstract ImmutableList<Icon> fluids();

    @Override
    public int compareTo(DisplayTask other) {
        return getTask().compareTo(other.getTask());
    }
}
