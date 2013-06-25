package com.akavrt.csp._1d.utils;

/**
 * <p>Collection of utility methods.</p>
 *
 * @author Victor Balabanov <akavrt@gmail.com>
 */
public class Utils {

    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    public static String convertIntArrayToString(String name, int[] array) {
        StringBuilder builder = new StringBuilder();
        if (!Utils.isEmpty(name)) {
            builder.append(name);
            builder.append(": ");
        }

        if (array == null) {
            builder.append("null");
        } else {
            builder.append("[ ");
            for (int i : array) {
                builder.append(i);
                builder.append(" ");
            }
            builder.append("]");
        }

        return builder.toString();
    }

}