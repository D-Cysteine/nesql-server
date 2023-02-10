package com.github.dcysteine.nesql.server.plugin.quest.display;

import com.github.dcysteine.nesql.server.common.Table;
import com.github.dcysteine.nesql.server.common.display.Icon;
import com.github.dcysteine.nesql.server.common.display.InfoPanel;
import com.github.dcysteine.nesql.server.common.service.DisplayService;
import com.github.dcysteine.nesql.server.common.util.NumberUtil;
import com.github.dcysteine.nesql.sql.quest.Quest;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

@AutoValue
public abstract class DisplayQuest implements Comparable<DisplayQuest> {
    public static DisplayQuest create(Quest quest, DisplayService service) {
        String description = quest.getDescription().replace("\n", "<br>");


        ImmutableList<Icon> questLines =
                quest.getQuestLines().stream()
                        .sorted()
                        .map(questLine -> DisplayQuestLine.buildIcon(questLine, service))
                        .collect(ImmutableList.toImmutableList());

        ImmutableList<Icon> requiredQuests =
                quest.getRequiredQuests().stream()
                        .sorted()
                        .map(requiredQuest -> DisplayQuest.buildIcon(requiredQuest, service))
                        .collect(ImmutableList.toImmutableList());
        ImmutableList<Icon> requiredByQuests =
                quest.getRequiredByQuests().stream()
                        .sorted()
                        .map(requiredByQuest -> DisplayQuest.buildIcon(requiredByQuest, service))
                        .collect(ImmutableList.toImmutableList());

        ImmutableList<DisplayTask> tasks =
                quest.getTasks().stream()
                        .map(task -> DisplayTask.create(task, service))
                        .collect(ImmutableList.toImmutableList());
        ImmutableList<DisplayReward> rewards =
                quest.getRewards().stream()
                        .map(reward -> DisplayReward.create(reward, service))
                        .collect(ImmutableList.toImmutableList());

        return new AutoValue_DisplayQuest(
                quest, buildIcon(quest, service), description,
                questLines, requiredQuests, requiredByQuests, tasks, rewards,
                service.buildAdditionalInfo(Quest.class, quest));
    }

    public static Icon buildIcon(Quest quest, DisplayService service) {
        Icon.Builder builder =
                Icon.builder()
                        .setDescription(quest.getName())
                        .setUrl(Table.QUEST.getViewUrl(quest))
                        .setImage(quest.getIcon().getImageFilePath())
                        .setBottomRight(NumberUtil.formatInteger(quest.getQuestId()));

        if (quest.getRepeatTime() > 0) {
            builder.setTopLeft("*");
        }

        return builder.build();
    }

    public abstract Quest getQuest();
    public abstract Icon getIcon();
    public abstract String getDescription();
    public abstract ImmutableList<Icon> getQuestLines();
    public abstract ImmutableList<Icon> getRequiredQuests();
    public abstract ImmutableList<Icon> getRequiredByQuests();
    public abstract ImmutableList<DisplayTask> getTasks();
    public abstract ImmutableList<DisplayReward> getRewards();
    public abstract ImmutableList<InfoPanel> getAdditionalInfo();

    @Override
    public int compareTo(DisplayQuest other) {
        return getQuest().compareTo(other.getQuest());
    }
}
