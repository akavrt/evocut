package com.akavrt.csp._1d.core;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * User: akavrt
 * Date: 26.06.13
 * Time: 00:31
 */
public class PatternTest {

    @Test
    public void equality() {
        Pattern original = new Pattern(new int[]{1, 0, 1});

        Pattern equal = new Pattern(new int[]{1, 0, 1});
        assertTrue(original.equals(equal));

        Pattern different = new Pattern(new int[]{1, 1, 1});
        assertFalse(original.equals(different));
    }

}
