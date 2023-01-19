package com.github.dcysteine.nesql.server.common.display;

import com.google.auto.value.AutoValue;

/** Display class holding a link. */
@AutoValue
public abstract class Link {
    public static Link create(String text, String url) {
        return new AutoValue_Link(text, url);
    }

    public abstract String getText();
    public abstract String getUrl();
}
