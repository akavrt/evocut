package com.akavrt.csp._1d.tester.batch;

import com.akavrt.csp._1d.solver.Algorithm;
import com.akavrt.csp._1d.solver.pattern.PatternGenerator;
import com.akavrt.csp._1d.solver.pattern.PierceGenerator;
import com.akavrt.csp._1d.solver.sequential.HaesslerProcedure;
import com.akavrt.csp._1d.solver.sequential.SequentialProcedureParameters;
import com.akavrt.csp._1d.tester.batch.config.HaesslerBatchConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * User: akavrt
 * Date: 29.06.13
 * Time: 00:49
 */
public class HaesslerBatchTester extends BatchTester {
    private static final Logger LOGGER = LogManager.getLogger(HaesslerBatchTester.class);
    private final HaesslerBatchConfig config;

    public HaesslerBatchTester(HaesslerBatchConfig config) {
        super(config.getBatchFilePath(), config.getNumberOfRuns());

        this.config = config;
    }

    public static void main(String[] args) {
        HaesslerBatchConfig config = new HaesslerBatchConfig(args);
        if (config.isLoaded()) {
            HaesslerBatchTester tester = new HaesslerBatchTester(config);
            tester.process();
        } else {
            config.printHelp();
        }
    }

    @Override
    protected Algorithm createAlgorithm() {
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

}
