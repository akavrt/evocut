package com.akavrt.csp._1d.core;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * User: akavrt
 * Date: 26.06.13
 * Time: 00:22
 */
public class OrderTest {

    @Test
    public void feasibility() {
        Order order = new Order(0, 0);
        assertFalse(order.isValid());

        order = new Order(1, 0);
        assertFalse(order.isValid());

        order = new Order(0, 1);
        assertFalse(order.isValid());

        order = new Order(1, 1);
        assertTrue(order.isValid());
    }

}
