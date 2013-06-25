package com.akavrt.csp._1d.core;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * User: akavrt
 * Date: 25.06.13
 * Time: 22:33
 */
public class PlanTest {
    private static final double DELTA = 1e-15;

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
    public void feasibility() {
        // using diagonal basis as a feasible solution
        Plan feasible = new Plan(problem);

        feasible.addPattern(new int[]{1, 0, 0}, 10);
        feasible.addPattern(new int[]{0, 1, 0}, 10);
        feasible.addPattern(new int[]{0, 0, 1}, 20);

        assertTrue(feasible.isFeasible());

        Plan infeasible = new Plan(problem);

        // empty plan
        assertFalse(infeasible.isFeasible());
        assertEquals(40, infeasible.getResidualDemand());

        infeasible.addPattern(new int[]{1, 0, 0}, 9);
        infeasible.addPattern(new int[]{0, 1, 0}, 9);
        infeasible.addPattern(new int[]{0, 0, 1}, 19);

        assertFalse(infeasible.isFeasible());
        assertEquals(3, infeasible.getResidualDemand());

        infeasible.addPattern(new int[]{1, 0, 0}, 1);
        infeasible.addPattern(new int[]{0, 1, 0}, 1);
        infeasible.addPattern(new int[]{0, 0, 1}, 1);

        assertTrue(infeasible.isFeasible());
        assertEquals(0, infeasible.getResidualDemand());

        // excessive production doesn't
        // lead to negative residual demands
        infeasible.addPattern(new int[]{1, 0, 0}, 1);
        infeasible.addPattern(new int[]{0, 1, 0}, 1);
        infeasible.addPattern(new int[]{0, 0, 1}, 1);

        assertTrue(infeasible.isFeasible());
        assertEquals(0, infeasible.getResidualDemand());
    }

    @Test
    public void setupsCount() {
        assertEquals(3, problem.size());

        Plan plan = new Plan(problem);
        plan.addPattern(new int[]{1, 0, 1});
        assertEquals(1, plan.getSetups());

        plan.addPattern(new int[]{1, 1, 1});
        assertEquals(2, plan.getSetups());

        plan.addPattern(new int[]{1, 0, 1});
        assertEquals(2, plan.getSetups());
    }

    @Test
    public void materialWasteRatio() {
        Plan plan = new Plan(problem);
        plan.addPattern(new int[]{2, 2, 0}, 5);
        plan.addPattern(new int[]{0, 0, 2}, 10);

        assertTrue(plan.isFeasible());
        assertEquals(0, plan.getMaterialWasteRatio(), DELTA);
    }

    @Test
    public void patternReductionRatio() {
        Plan plan = new Plan(problem);

        // single pattern
        plan.addPattern(new int[]{1, 1, 1}, 1);
        assertEquals(0, plan.getPatternReductionRatio(), DELTA);

        // duplicated pattern
        plan.addPattern(new int[]{1, 0, 1}, 2);
        assertEquals(0.5, plan.getPatternReductionRatio(), DELTA);
    }

}
