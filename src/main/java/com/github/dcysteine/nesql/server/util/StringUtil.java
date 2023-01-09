package com.github.dcysteine.nesql.server.util;

import java.io.File;

public class StringUtil {
    // Static class.
    private StringUtil() {}

    public static String formatFilePath(String filePath) {
        return filePath.replace(File.separatorChar, '/');
    }
}
