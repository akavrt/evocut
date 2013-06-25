package com.akavrt.csp._1d.core;

import java.util.Arrays;

/**
 * User: akavrt
 * Date: 25.06.13
 * Time: 22:44
 */
public class Pattern {
    private final int[] cuts;

    public Pattern(int[] cuts) {
        this.cuts = cuts;
    }

    public int[] getCuts() {
        return cuts;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(cuts);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Pattern)) {
            return false;
        }

        Pattern rhs = (Pattern) o;

        return hashCode() == rhs.hashCode();
    }
}
