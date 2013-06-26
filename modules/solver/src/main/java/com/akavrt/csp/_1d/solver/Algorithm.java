package com.akavrt.csp._1d.solver;

import com.akavrt.csp._1d.core.Plan;
import com.akavrt.csp._1d.utils.ParameterSet;

import java.util.List;

/**
 * User: akavrt
 * Date: 26.06.13
 * Time: 18:45
 */
public interface Algorithm {
    String name();
    List<Plan> execute(ExecutionContext context);
    List<ParameterSet> getParameters();
}
