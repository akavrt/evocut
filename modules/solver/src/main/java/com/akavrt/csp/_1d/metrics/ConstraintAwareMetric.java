package com.akavrt.csp._1d.metrics;

import com.akavrt.csp._1d.core.Plan;

import java.util.Collections;
import java.util.Comparator;

/**
 * User: akavrt
 * Date: 26.06.13
 * Time: 01:31
 */
public class ConstraintAwareMetric implements Metric {
    private final ConstraintAwareMetricParameters params;

    public ConstraintAwareMetric() {
        this(new ConstraintAwareMetricParameters());
    }

    public ConstraintAwareMetric(ConstraintAwareMetricParameters params) {
        this.params = params;
    }

    @Override
    public double evaluate(Plan plan) {
        return params.getAggregatedTrimFactor() * plan.getMaterialWasteRatio()
                + params.getPatternsFactor() * plan.getPatternReductionRatio();
    }

    @Override
    public int compare(Plan p1, Plan p2) {
        int p1ResidualDemand = p1.getTotalResidualDemand();
        int p2ResidualDemand = p2.getTotalResidualDemand();

        int result;
        if (p1ResidualDemand == 0 && p2ResidualDemand == 0) {
            // dealing with two feasible solutions,
            // minimization problem is implied
            double p1eval = evaluate(p1);
            double p2eval = evaluate(p2);

            result = p1eval == p2eval ? 0 : (p1eval > p2eval ? -1 : 1);
        } else if (p1ResidualDemand > 0 && p2ResidualDemand > 0) {
            // dealing with two infeasible solutions
            result = p1ResidualDemand == p2ResidualDemand ? 0
                    : (p1ResidualDemand > p2ResidualDemand ? -1 : 1);
        } else {
            // only one solution is feasible
            result = p1ResidualDemand > 0 ? -1 : 1;
        }

        return result;
    }

    @Override
    public Comparator<Plan> getComparator() {
        return new Comparator<Plan>() {

            @Override
            public int compare(Plan lhs, Plan rhs) {
                return ConstraintAwareMetric.this.compare(lhs, rhs);
            }
        };
    }

    @Override
    public Comparator<Plan> getReverseComparator() {
        return Collections.reverseOrder(getComparator());
    }

    @Override
    public String abbreviation() {
        return "CONSTRAINED-OF";
    }

    @Override
    public String name() {
        return "Constraint aware parameter-less metric";
    }

}
