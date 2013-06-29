package com.akavrt.csp._1d.tester.params;

import com.akavrt.csp._1d.solver.sequential.VahrenkampProcedureParameters;

/**
 * User: akavrt
 * Date: 24.04.13
 * Time: 19:08
 */
public class VahrenkampProcedureParametersReader extends
        AbstractParametersReader<VahrenkampProcedureParameters> {

    @Override
    protected VahrenkampProcedureParameters createParameterSet() {
        return new VahrenkampProcedureParameters();
    }
}

