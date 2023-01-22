package com.github.dcysteine.nesql.server.common.display;

import com.google.auto.value.AutoValue;

import java.util.Optional;

/** Display class holding a key-value property. */
@AutoValue
public abstract class Property {
    public static Property ofString(String key, String value) {
        return new AutoValue_Property(key, Optional.of(value), Optional.empty());
    }

    public static Property ofBoolean(String key, boolean value) {
        return new AutoValue_Property(key, Optional.empty(), Optional.of(value));
    }

    public abstract String getKey();
    public abstract Optional<String> getString();
    public abstract Optional<Boolean> getBoolean();
}
