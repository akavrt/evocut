package com.akavrt.csp._1d.solver.evo;

import com.akavrt.csp._1d.core.Plan;
import com.akavrt.csp._1d.solver.ExecutionContext;

/**
 * User: akavrt
 * Date: 26.06.13
 * Time: 20:08
 */
public interface EvolutionaryOperator {
    void initialize(ExecutionContext context);
    Plan apply(Plan... chromosomes);
}
