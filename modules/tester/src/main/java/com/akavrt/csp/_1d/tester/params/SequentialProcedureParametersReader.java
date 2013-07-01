package com.akavrt.csp._1d.tester.params;

import com.akavrt.csp._1d.solver.sequential.SequentialProcedureParameters;

/**
 * User: akavrt
 * Date: 01.07.13
 * Time: 01:34
 */
public class SequentialProcedureParametersReader extends
        AbstractParametersReader<SequentialProcedureParameters> {

    @Override
    protected SequentialProcedureParameters createParameterSet() {
        return new SequentialProcedureParameters();
    }

}
