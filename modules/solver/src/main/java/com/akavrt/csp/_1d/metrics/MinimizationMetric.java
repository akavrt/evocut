package com.akavrt.csp._1d.metrics;

import com.akavrt.csp._1d.core.Plan;
import com.akavrt.csp._1d.utils.ParameterSet;

import java.util.Collections;
import java.util.Comparator;

/**
 * User: akavrt
 * Date: 26.06.13
 * Time: 00:52
 */
public abstract class MinimizationMetric implements Metric {

    @Override
    public int compare(Plan p1, Plan p2) {
        double s1eval = evaluate(p1);
        double s2eval = evaluate(p2);

        return s1eval > s2eval ? -1 : (s1eval < s2eval ? 1 : 0);
    }

    @Override
    public Comparator<Plan> getComparator() {
        return new Comparator<Plan>() {

            @Override
            public int compare(Plan lhs, Plan rhs) {
                return MinimizationMetric.this.compare(lhs, rhs);
            }
        };
    }

    @Override
    public Comparator<Plan> getReverseComparator() {
        return Collections.reverseOrder(getComparator());
    }

    @Override
    public void setContextMetricProvider(ContextMetricProvider provider) {
        // unused
    }

    @Override
    public ParameterSet getParameters() {
        return null;
    }
}
