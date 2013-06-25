package com.akavrt.csp._1d.core;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * User: akavrt
 * Date: 26.06.13
 * Time: 00:25
 */
public class ProblemTest {

    @Test
    public void orderDuplication() {
        List<Order> orders;
        Problem problem;

        // empty problem
        orders = Lists.newArrayList();
        problem = new Problem(100, orders);
        assertEquals(0, problem.size());

        // two orders with different lengths
        orders = Lists.newArrayList();
        orders.add(new Order(20, 5));
        orders.add(new Order(30, 10));
        problem = new Problem(100, orders);

        assertEquals(2, problem.size());

        // order with length 20 encountered twice,
        // corresponding demands are summed up
        orders = Lists.newArrayList();
        orders.add(new Order(20, 5));
        orders.add(new Order(30, 10));
        orders.add(new Order(20, 3));
        problem = new Problem(100, orders);

        assertEquals(2, problem.size());
    }


}
