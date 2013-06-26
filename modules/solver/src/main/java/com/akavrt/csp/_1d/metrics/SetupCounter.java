package com.akavrt.csp._1d.metrics;

import com.akavrt.csp._1d.core.Plan;

/**
 * User: akavrt
 * Date: 26.06.13
 * Time: 01:25
 */
public class SetupCounter extends MinimizationMetric {

    @Override
    public double evaluate(Plan plan) {
        return plan.getSetups();
    }

    @Override
    public String abbreviation() {
        return "SC";
    }

    @Override
    public String name() {
        return "Number of setups";
    }

}
