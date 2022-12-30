package com.github.dcysteine.nesql.server.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/** Utility class containing methods for handling NBTs in string form. */
public final class NbtUtil {
    /** Static class. */
    private NbtUtil() {}

    /**
     * The counterpart to this method is
     * {@code com.github.dcysteine.nesql.exporter.util.IdUtil.encodeNbt()}.
     */
    public static String decodeNbt(String encodedNbt) {
        return new String(Base64.getUrlDecoder().decode(encodedNbt), StandardCharsets.UTF_8);
    }
}
