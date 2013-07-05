package com.akavrt.csp._1d.tester.params;

import com.akavrt.csp._1d.solver.evo.es.EvolutionStrategyParameters;

/**
 * User: akavrt
 * Date: 26.04.13
 * Time: 00:10
 */
public class EvolutionStrategyParametersReader extends
        AbstractParametersReader<EvolutionStrategyParameters> {

    @Override
    protected EvolutionStrategyParameters createParameterSet() {
        return new EvolutionStrategyParameters();
    }
}

