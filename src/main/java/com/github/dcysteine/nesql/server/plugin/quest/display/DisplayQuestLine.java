package com.github.dcysteine.nesql.server.plugin.quest.display;

import com.github.dcysteine.nesql.server.common.Table;
import com.github.dcysteine.nesql.server.common.display.Icon;
import com.github.dcysteine.nesql.server.common.display.InfoPanel;
import com.github.dcysteine.nesql.server.common.service.DisplayService;
import com.github.dcysteine.nesql.server.common.util.NumberUtil;
import com.github.dcysteine.nesql.sql.quest.QuestLine;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

@AutoValue
public abstract class DisplayQuestLine implements Comparable<DisplayQuestLine> {
    public static DisplayQuestLine create(QuestLine questLine, DisplayService service) {
        String description = questLine.getDescription().replace("\n", "<br>");

        ImmutableList<Icon> quests =
                questLine.getQuests().stream()
                        .sorted()
                        .map(quest -> DisplayQuest.buildIcon(quest, service))
                        .collect(ImmutableList.toImmutableList());

        return new AutoValue_DisplayQuestLine(
                questLine, buildIcon(questLine, service), description, quests,
                service.buildAdditionalInfo(QuestLine.class, questLine));
    }

    public static Icon buildIcon(QuestLine questLine, DisplayService service) {
        return Icon.builder()
                .setDescription(questLine.getName())
                .setUrl(Table.QUEST_LINE.getViewUrl(questLine))
                .setImage(questLine.getIcon().getImageFilePath())
                .setBottomRight(NumberUtil.formatInteger(questLine.getQuestLineId()))
                .build();
    }

    public abstract QuestLine getQuestLine();
    public abstract Icon getIcon();
    public abstract String getDescription();
    public abstract ImmutableList<Icon> getQuests();
    public abstract ImmutableList<InfoPanel> getAdditionalInfo();

    @Override
    public int compareTo(DisplayQuestLine other) {
        return getQuestLine().compareTo(other.getQuestLine());
    }
}
