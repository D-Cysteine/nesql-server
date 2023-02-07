package com.github.dcysteine.nesql.server.common.util;

import com.google.common.math.DoubleMath;

import java.text.NumberFormat;

public class NumberUtil {
    // Static class.
    private NumberUtil() {}

    public static boolean fuzzyEquals(double a, double b) {
        return DoubleMath.fuzzyEquals(a, b, 0.000001d);
    }

    public static String formatInteger(long integer) {
        NumberFormat numberFormat = NumberFormat.getIntegerInstance();
        return numberFormat.format(integer);
    }

    public static String formatCompact(long integer) {
        if (Math.abs(integer) < 10_000) {
            return formatInteger(integer);
        }

        NumberFormat numberFormat = NumberFormat.getCompactNumberInstance();
        return numberFormat.format(integer);
    }

    public static String formatDouble(double d) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(6);
        return numberFormat.format(d);
    }

    public static String formatPercentage(double probability) {
        NumberFormat numberFormat = NumberFormat.getPercentInstance();
        numberFormat.setMaximumFractionDigits(2);
        return numberFormat.format(probability);
    }
}
