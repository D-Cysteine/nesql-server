package com.github.dcysteine.nesql.server.common;

public class Constants {
    // Static class.
    private Constants() {}

    /** Return this for cases where we lack a valid URL due to not finding an entity. */
    public static final String NOT_FOUND_URL = "~/notfound";

    /** Return this for cases where we lack an image to show (e.g., empty item groups). */
    public static final String MISSING_IMAGE = "missing.png";
}
