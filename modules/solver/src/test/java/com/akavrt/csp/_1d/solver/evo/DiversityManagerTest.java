package com.akavrt.csp._1d.solver.evo;

import com.akavrt.csp._1d.core.Order;
import com.akavrt.csp._1d.core.Plan;
import com.akavrt.csp._1d.core.Problem;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * User: akavrt
 * Date: 01.07.13
 * Time: 19:06
 */
public class DiversityManagerTest {
    private Problem problem;

    @Before
    public void setupProblem() {
        List<Order> orders = Lists.newArrayList();
        orders.add(new Order(20, 10));
        orders.add(new Order(30, 10));
        orders.add(new Order(50, 20));

        problem = new Problem(100, orders);
    }

    @Test
    public void presence() {
        Plan first = new Plan(problem);
        Plan second = new Plan(problem);

        DiversityManager dm = new DiversityManager();

        dm.add(first);
        assertEquals(1, dm.getMeasure());
        assertTrue(dm.isAdded(second));

        first.addPattern(new int[]{1, 0, 1});
        first.addPattern(new int[]{1, 1, 1});

        dm.add(first);
        assertEquals(2, dm.getMeasure());

        second.addPattern(new int[]{1, 1, 1});
        assertFalse(dm.isAdded(second));

        second.addPattern(new int[]{1, 0, 1});
        assertTrue(dm.isAdded(second));

        dm.add(second);
        assertEquals(2, dm.getMeasure());
    }

}
