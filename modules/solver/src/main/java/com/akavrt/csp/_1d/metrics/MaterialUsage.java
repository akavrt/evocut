package com.akavrt.csp._1d.metrics;

import com.akavrt.csp._1d.core.Plan;

/**
 * User: akavrt
 * Date: 26.06.13
 * Time: 01:26
 */
public class MaterialUsage extends MinimizationMetric {

    @Override
    public double evaluate(Plan plan) {
        return plan.getMaterialUsage();
    }

    @Override
    public String abbreviation() {
        return "MU";
    }

    @Override
    public String name() {
        return "Total number of stock pieces used";
    }

}
