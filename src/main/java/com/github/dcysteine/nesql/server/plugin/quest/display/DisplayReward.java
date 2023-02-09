package com.github.dcysteine.nesql.server.plugin.quest.display;

import com.github.dcysteine.nesql.server.common.display.Icon;
import com.github.dcysteine.nesql.server.common.service.DisplayService;
import com.github.dcysteine.nesql.server.plugin.base.display.item.DisplayItemGroup;
import com.github.dcysteine.nesql.server.plugin.quest.QuestSpec;
import com.github.dcysteine.nesql.sql.quest.QuestRepository;
import com.github.dcysteine.nesql.sql.quest.Reward;
import com.github.dcysteine.nesql.sql.quest.RewardType;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

import java.util.Optional;

@AutoValue
public abstract class DisplayReward implements Comparable<DisplayReward> {
    public static DisplayReward create(Reward reward, DisplayService service) {
        ImmutableList<Icon> items =
                reward.getItems().stream()
                        .map(itemGroup -> DisplayItemGroup.buildIcon(itemGroup, service))
                        .collect(ImmutableList.toImmutableList());

        Optional<Icon> completeQuest = Optional.empty();
        if (reward.getType() == RewardType.COMPLETE_QUEST) {
            QuestRepository questRepository = service.getQuestRepository();
            completeQuest =
                    questRepository.findOne(QuestSpec.buildQuestIdSpec(reward.getCompleteQuestId()))
                            .map(quest -> DisplayQuest.buildIcon(quest, service));
        }

        return new AutoValue_DisplayReward(reward, items, completeQuest);
    }

    public abstract Reward getReward();
    public abstract ImmutableList<Icon> items();
    public abstract Optional<Icon> completeQuest();

    @Override
    public int compareTo(DisplayReward other) {
        return getReward().compareTo(other.getReward());
    }
}
