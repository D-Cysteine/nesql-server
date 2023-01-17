package com.github.dcysteine.nesql.server.util;

import com.github.dcysteine.nesql.sql.base.recipe.Dimension;

import java.util.function.Consumer;

public class StringUtil {
    // Static class.
    private StringUtil() {}

    public static String prettyPrintDimension(Dimension dimension) {
        return String.format("%d x %d", dimension.getWidth(), dimension.getHeight());
    }

    public static String prettyPrintNbt(String nbt) {
        StringBuilder builder = new StringBuilder();
        int indent = 0;

        Consumer<Integer> lineBreak = i -> {
            builder.append('\n');
            builder.append(" ".repeat(i));
        };

        for (char c : nbt.toCharArray()) {
            switch (c) {
                case ':' -> {
                    builder.append(c);
                    builder.append(' ');
                }

                case ',' -> {
                    builder.append(c);
                    lineBreak.accept(indent);
                }

                case '[', '{' -> {
                    builder.append(c);
                    indent += 2;
                    lineBreak.accept(indent);
                }

                case ']', '}' -> {
                    indent -= 2;
                    lineBreak.accept(indent);
                    builder.append(c);
                    lineBreak.accept(indent);
                }

                default -> builder.append(c);
            }
        }

        return builder.toString();
    }
}
