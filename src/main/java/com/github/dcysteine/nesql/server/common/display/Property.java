package com.github.dcysteine.nesql.server.common.display;

import com.google.auto.value.AutoValue;

/** Display class holding a key-value property. */
@AutoValue
public abstract class Property {
    public static Property create(String key, String value) {
        return new AutoValue_Property(key, value);
    }

    public abstract String getKey();
    public abstract String getValue();
}
