package com.github.dcysteine.nesql.server.common.display;

import com.google.auto.value.AutoValue;

import java.util.Optional;

/** Display class holding a link. */
@AutoValue
public abstract class Link {
    public static Link create(String text, String url) {
        return new AutoValue_Link(Optional.empty(), text, url);
    }

    public static Link create(String icon, String text, String url) {
        return new AutoValue_Link(Optional.of(icon), text, url);
    }

    /**
     * If provided, this should be the name of a Bootstrap icon, which will then be rendered in the
     * button. E.g., {@code "bi-search"}.
     */
    public abstract Optional<String> getIcon();
    public abstract String getText();
    public abstract String getUrl();
}
