package com.akavrt.csp._1d.solver.evo;

import com.akavrt.csp._1d.core.Plan;
import com.akavrt.csp._1d.solver.Algorithm;

import java.util.List;

/**
 * User: akavrt
 * Date: 26.06.13
 * Time: 20:05
 */
public interface Population {
    int getAge();
    List<Plan> getChromosomes();

    void sort();
    void initialize(Algorithm initializationProcedure);
    void initialize(Algorithm initializationProcedure, EvolutionProgressChangeListener listener);
    void generation(EvolutionaryOperator... operators);
    List<Plan> getSolutions();
}
