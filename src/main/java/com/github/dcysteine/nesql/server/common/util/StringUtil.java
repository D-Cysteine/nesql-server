package com.github.dcysteine.nesql.server.common.util;

import com.github.dcysteine.nesql.sql.base.recipe.Dimension;

public class StringUtil {
    // Static class.
    private StringUtil() {}

    public static String prettyPrintDimension(Dimension dimension) {
        return String.format("%d x %d", dimension.getWidth(), dimension.getHeight());
    }

    public static String prettyPrintNbt(String nbt) {
        return new NbtPrettyPrinter().prettyPrint(nbt);
    }

    private static class NbtPrettyPrinter {
        private int indent = 0;
        private boolean isNewLine = true;
        private StringBuilder builder;

        public String prettyPrint(String nbt) {
            builder = new StringBuilder();
            for (char c : nbt.toCharArray()) {
                switch (c) {
                    case ':' -> {
                        // ':' should never be the first character on a line, but just in case...
                        applyIndent();
                        builder.append(c);
                        builder.append(' ');
                    }

                    case ',' -> {
                        newLine();
                    }

                    case '[', '{' -> {
                        applyIndent();
                        builder.append(c);
                        indent++;
                        newLine();
                    }

                    case ']', '}' -> {
                        indent--;
                        newLine();
                        applyIndent();
                        builder.append(c);
                        newLine();
                    }

                    default -> {
                        applyIndent();
                        builder.append(c);
                    }
                }
            }
            return builder.toString();
        }

        private void newLine() {
            if (isNewLine) {
                return;
            }

            builder.append('\n');
            isNewLine = true;
        }

        private void applyIndent() {
            if (!isNewLine) {
                return;
            }

            // \u2003 is an em space, which won't get collapsed by HTML rendering.
            builder.append("\u2003".repeat(2 * indent));
            isNewLine = false;
        }
    }
}
