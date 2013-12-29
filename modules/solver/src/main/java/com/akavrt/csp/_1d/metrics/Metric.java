package com.akavrt.csp._1d.metrics;

import com.akavrt.csp._1d.core.Plan;
import com.akavrt.csp._1d.utils.ParameterSet;

import java.util.Comparator;

/**
 * User: akavrt
 * Date: 26.06.13
 * Time: 00:51
 */
public interface Metric {
    double evaluate(Plan plan);
    int compare(Plan p1, Plan p2);
    String abbreviation();
    String name();
    Comparator<Plan> getComparator();
    Comparator<Plan> getReverseComparator();
    void setContextMetricProvider(ContextMetricProvider provider);
    ParameterSet getParameters();
}