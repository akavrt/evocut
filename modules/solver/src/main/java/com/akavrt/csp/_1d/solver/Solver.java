package com.akavrt.csp._1d.solver;

import com.akavrt.csp._1d.core.Plan;

import java.util.List;

/**
 * User: akavrt
 * Date: 26.06.13
 * Time: 19:00
 */
public interface Solver {
    List<Plan> solve();
}
