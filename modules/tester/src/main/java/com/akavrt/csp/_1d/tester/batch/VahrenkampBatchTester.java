package com.akavrt.csp._1d.tester.batch;

import com.akavrt.csp._1d.solver.Algorithm;
import com.akavrt.csp._1d.solver.pattern.PatternGenerator;
import com.akavrt.csp._1d.solver.pattern.PatternGeneratorParameters;
import com.akavrt.csp._1d.solver.pattern.UnconstrainedPatternGenerator;
import com.akavrt.csp._1d.solver.sequential.VahrenkampProcedure;
import com.akavrt.csp._1d.solver.sequential.VahrenkampProcedureParameters;
import com.akavrt.csp._1d.tester.batch.config.VahrenkampBatchConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * User: akavrt
 * Date: 01.07.13
 * Time: 00:41
 */
public class VahrenkampBatchTester extends BatchTester {
    private static final Logger LOGGER = LogManager.getLogger(VahrenkampBatchTester.class);
    private final VahrenkampBatchConfig config;

    public VahrenkampBatchTester(VahrenkampBatchConfig config) {
        super(config.getBatchFilePath(), config.getNumberOfRuns());

        this.config = config;
    }

    public static void main(String[] args) {
        VahrenkampBatchConfig config = new VahrenkampBatchConfig(args);
        if (config.isLoaded()) {
            VahrenkampBatchTester tester = new VahrenkampBatchTester(config);
            tester.process();
        } else {
            config.printHelp();
        }
    }

    private PatternGenerator createPatternGenerator() {
        PatternGeneratorParameters patternParameters = config.getPatternParameters();
        if (patternParameters == null) {
            patternParameters = new PatternGeneratorParameters();
        }

        return new UnconstrainedPatternGenerator(patternParameters);
    }

    @Override
    protected Algorithm createAlgorithm() {
        PatternGenerator generator = createPatternGenerator();

        VahrenkampProcedureParameters procedureParameters = config.getAlgorithmParameters();
        if (procedureParameters == null) {
            procedureParameters = new VahrenkampProcedureParameters();
        }

        return new VahrenkampProcedure(generator, procedureParameters);
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

}
