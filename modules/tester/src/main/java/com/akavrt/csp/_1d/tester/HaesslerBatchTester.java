package com.akavrt.csp._1d.tester;

import com.akavrt.csp._1d.solver.Algorithm;
import com.akavrt.csp._1d.solver.pattern.PatternGenerator;
import com.akavrt.csp._1d.solver.pattern.PatternGeneratorParameters;
import com.akavrt.csp._1d.solver.pattern.PierceGenerator;
import com.akavrt.csp._1d.solver.pattern.UnconstrainedPatternGenerator;
import com.akavrt.csp._1d.solver.sequential.HaesslerProcedure;
import com.akavrt.csp._1d.solver.sequential.SequentialProcedureParameters;
import com.akavrt.csp._1d.tester.config.SequentialProcedureBatchConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * User: akavrt
 * Date: 29.06.13
 * Time: 00:49
 */
public class HaesslerBatchTester extends BatchTester {
    private static final Logger LOGGER = LogManager.getLogger(HaesslerBatchTester.class);
    private final SequentialProcedureBatchConfiguration config;

    public HaesslerBatchTester(SequentialProcedureBatchConfiguration config) {
        super(config.getBatchFilePath(), config.getNumberOfRuns());

        this.config = config;
    }

    public static void main(String[] args) {
        SequentialProcedureBatchConfiguration config = new SequentialProcedureBatchConfiguration(args);
        if (config.isLoaded()) {
            HaesslerBatchTester tester = new HaesslerBatchTester(config);
            tester.process();
        } else {
            config.printHelp();
            System.exit(0);
        }
    }

    protected PatternGenerator createPatternGenerator() {
        PatternGeneratorParameters patternParameters = config.getPatternParameters();
        if (patternParameters == null) {
            patternParameters = new PatternGeneratorParameters();
        }

        return new UnconstrainedPatternGenerator(patternParameters);
    }

    @Override
    protected Algorithm createAlgorithm() {
//        PatternGenerator generator = createPatternGenerator();
        PatternGenerator generator = new PierceGenerator();

        SequentialProcedureParameters procedureParameters = config.getAlgorithmParameters();
        if (procedureParameters == null) {
            procedureParameters = new SequentialProcedureParameters();
        }

        return new HaesslerProcedure(generator, procedureParameters);
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    protected SequentialProcedureBatchConfiguration getBatchConfig() {
        return config;
    }

}
