package com.akavrt.csp._1d.solver.evo;

import com.akavrt.csp._1d.solver.Algorithm;

public interface EvolutionaryComponentsFactory {
    EvolutionaryOperator createMutation();
    Algorithm createInitializationProcedure();
}
