package com.github.dcysteine.nesql.server.plugin.thaumcraft.spec;

import com.github.dcysteine.nesql.server.common.util.QueryUtil;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.base.item.Item_;
import com.github.dcysteine.nesql.sql.thaumcraft.Aspect;
import com.github.dcysteine.nesql.sql.thaumcraft.AspectEntry;
import com.github.dcysteine.nesql.sql.thaumcraft.AspectEntry_;
import com.github.dcysteine.nesql.sql.thaumcraft.Aspect_;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

public class AspectEntrySpec {
    public static final Sort DEFAULT_SORT =
            Sort.unsorted()
                    .and(
                            Sort.sort(AspectEntry.class)
                                    .by(AspectEntry::getItem)
                                    .by(Item::getId))
                    .and(
                            Sort.sort(AspectEntry.class)
                                    .by(AspectEntry::getAmount)
                                    .descending())
                    .and(
                            Sort.sort(AspectEntry.class)
                                    .by(AspectEntry::getAspect)
                                    .by(Aspect::isPrimal)
                                    .descending())
                    .and(
                            Sort.sort(AspectEntry.class)
                                    .by(AspectEntry::getAspect)
                                    .by(Aspect::getName));

    // Static class.
    private AspectEntrySpec() {}

    /** Matches by regex. */
    public static Specification<AspectEntry> buildItemNameSpec(String localizedName) {
        return (root, query, builder) ->
                QueryUtil.regexMatch(
                        builder,
                        root.get(AspectEntry_.ITEM).get(Item_.LOCALIZED_NAME),
                        builder.literal(localizedName));
    }

    /** Matches by regex. */
    public static Specification<AspectEntry> buildItemModIdSpec(String modId) {
        return (root, query, builder) ->
                QueryUtil.regexMatch(
                        builder,
                        root.get(AspectEntry_.ITEM).get(Item_.MOD_ID),
                        builder.literal(modId));
    }

    public static Specification<AspectEntry> buildItemIdSpec(String itemId) {
        return (root, query, builder) ->
                builder.equal(root.get(AspectEntry_.ITEM).get(Item_.ID), itemId);
    }

    public static Specification<AspectEntry> buildAspectNameSpec(String aspectName) {
        return (root, query, builder) ->
                QueryUtil.regexMatch(
                        builder,
                        root.get(AspectEntry_.ASPECT).get(Aspect_.NAME),
                        builder.literal(aspectName));
    }

    public static Specification<AspectEntry> buildAspectIdSpec(String aspectId) {
        return (root, query, builder) ->
                builder.equal(root.get(AspectEntry_.ASPECT).get(Aspect_.ID), aspectId);
    }


    public static Specification<AspectEntry> buildMinAmountSpec(int amount) {
        return (root, query, builder) ->
                builder.greaterThanOrEqualTo(root.get(AspectEntry_.AMOUNT), amount);
    }

    public static Specification<AspectEntry> buildMaxAmountSpec(int amount) {
        return (root, query, builder) ->
                builder.lessThanOrEqualTo(root.get(AspectEntry_.AMOUNT), amount);
    }
}