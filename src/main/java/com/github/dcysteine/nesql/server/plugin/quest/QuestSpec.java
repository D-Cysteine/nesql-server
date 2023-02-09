package com.github.dcysteine.nesql.server.plugin.quest;

import com.github.dcysteine.nesql.server.common.util.QueryUtil;
import com.github.dcysteine.nesql.sql.quest.Quest;
import com.github.dcysteine.nesql.sql.quest.Quest_;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

public class QuestSpec {
    public static final Sort DEFAULT_SORT = Sort.by(Quest_.NAME, Quest_.ID);

    // Static class.
    private QuestSpec() {}

    /** Matches by regex. */
    public static Specification<Quest> buildQuestNameSpec(String questName) {
        return (root, query, builder) ->
                QueryUtil.regexMatch(
                        builder,
                        root.get(Quest_.NAME),
                        builder.literal(questName));
    }

    /** Matches by regex. */
    public static Specification<Quest> buildQuestDescriptionSpec(String questDescription) {
        return (root, query, builder) ->
                QueryUtil.regexMatch(
                        builder,
                        root.get(Quest_.DESCRIPTION),
                        builder.literal(questDescription));
    }

    public static Specification<Quest> buildQuestIdSpec(int questId) {
        return (root, query, builder) ->
                builder.equal(root.get(Quest_.QUEST_ID), questId);
    }

    /** Matches by regex. */
    public static Specification<Quest> buildVisibilitySpec(String visibility) {
        return (root, query, builder) ->
                QueryUtil.regexMatch(
                        builder,
                        root.get(Quest_.VISIBILITY),
                        builder.literal(visibility));
    }
}