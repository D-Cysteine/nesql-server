package com.github.dcysteine.nesql.server.display;

import com.google.auto.value.AutoValue;

/** Immutable class representing a width and height. */
@AutoValue
public abstract class Dimension {
    public static Dimension create(int width, int height) {
        return new AutoValue_Dimension(width, height);
    }

    public static Dimension create(int width) {
        return new AutoValue_Dimension(width, width);
    }

    public abstract int getWidth();
    public abstract int getHeight();
}
