package com.akavrt.csp._1d.tester.params;

import com.akavrt.csp._1d.solver.pattern.PatternGeneratorParameters;

/**
 * User: akavrt
 * Date: 13.04.13
 * Time: 19:07
 */
public class PatternGeneratorParametersReader extends
        AbstractParametersReader<PatternGeneratorParameters> {

    @Override
    protected PatternGeneratorParameters createParameterSet() {
        return new PatternGeneratorParameters();
    }
}
