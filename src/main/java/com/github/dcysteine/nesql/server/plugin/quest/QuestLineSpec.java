package com.github.dcysteine.nesql.server.plugin.quest;

import com.github.dcysteine.nesql.server.common.util.QueryUtil;
import com.github.dcysteine.nesql.sql.quest.QuestLine;
import com.github.dcysteine.nesql.sql.quest.QuestLine_;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

public class QuestLineSpec {
    public static final Sort DEFAULT_SORT = Sort.by(QuestLine_.NAME, QuestLine_.ID);

    // Static class.
    private QuestLineSpec() {}

    /** Matches by regex. */
    public static Specification<QuestLine> buildQuestLineNameSpec(String questLineName) {
        return (root, query, builder) ->
                QueryUtil.regexMatch(
                        builder,
                        root.get(QuestLine_.NAME),
                        builder.literal(questLineName));
    }

    /** Matches by regex. */
    public static Specification<QuestLine> buildQuestLineDescriptionSpec(
            String questLineDescription) {
        return (root, query, builder) ->
                QueryUtil.regexMatch(
                        builder,
                        root.get(QuestLine_.DESCRIPTION),
                        builder.literal(questLineDescription));
    }

    public static Specification<QuestLine> buildQuestLineIdSpec(String questLineId) {
        return (root, query, builder) ->
                builder.equal(root.get(QuestLine_.QUEST_LINE_ID), questLineId);
    }

    /** Matches by regex. */
    public static Specification<QuestLine> buildVisibilitySpec(String visibility) {
        return (root, query, builder) ->
                QueryUtil.regexMatch(
                        builder,
                        root.get(QuestLine_.VISIBILITY),
                        builder.literal(visibility));
    }
}