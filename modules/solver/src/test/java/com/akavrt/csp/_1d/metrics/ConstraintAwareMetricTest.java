package com.akavrt.csp._1d.metrics;

import com.akavrt.csp._1d.core.Order;
import com.akavrt.csp._1d.core.Plan;
import com.akavrt.csp._1d.core.Problem;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * User: akavrt
 * Date: 26.06.13
 * Time: 17:12
 */
public class ConstraintAwareMetricTest {

    @Test
    public void ordering() {
        ConstraintAwareMetric metric = new ConstraintAwareMetric();

        List<Order> orders = Lists.newArrayList();
        orders.add(new Order(20, 10));

        Problem problem = new Problem(100, orders);

        Plan infeasibleBest = new Plan(problem);
        infeasibleBest.addPattern(new int[]{3}, 3);

        Plan infeasibleWorst = new Plan(problem);
        infeasibleWorst.addPattern(new int[]{4}, 2);

        Plan optimal = new Plan(problem);
        optimal.addPattern(new int[]{5}, 2);

        Plan excessive = new Plan(problem);
        excessive.addPattern(new int[]{4}, 3);

        List<Plan> plans = Lists.newArrayList();
        plans.add(infeasibleBest);
        plans.add(infeasibleWorst);
        plans.add(optimal);
        plans.add(excessive);

        Collections.sort(plans, metric.getComparator());

        // the worst plan has to the first one in a sorted list
        // the best plan has to be the last one in a sorted list
        assertEquals(0, plans.indexOf(infeasibleWorst));
        assertEquals(1, plans.indexOf(infeasibleBest));
        assertEquals(2, plans.indexOf(excessive));
        assertEquals(3, plans.indexOf(optimal));

        Collections.sort(plans, metric.getReverseComparator());

        // the best plan has to be the first one in a sorted list
        // the worst plan has to the last one in a sorted list
        assertEquals(0, plans.indexOf(optimal));
        assertEquals(1, plans.indexOf(excessive));
        assertEquals(2, plans.indexOf(infeasibleBest));
        assertEquals(3, plans.indexOf(infeasibleWorst));
    }

}
