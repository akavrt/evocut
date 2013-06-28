package com.akavrt.csp._1d.analyzer;

import com.akavrt.csp._1d.core.Plan;
import com.akavrt.csp._1d.metrics.Metric;

import java.util.List;

/**
 * User: akavrt
 * Date: 23.03.13
 * Time: 00:43
 */
public interface Measure {
    // average, variance, etc.
    String name();
    double calculate(List<Plan> solutions, Metric metric);
    double calculate(List<Long> times);
}
