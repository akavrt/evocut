package com.akavrt.csp._1d.tester.batch;

import com.akavrt.csp._1d.metrics.ConstraintAwareMetric;
import com.akavrt.csp._1d.metrics.ConstraintAwareMetricParameters;
import com.akavrt.csp._1d.metrics.Metric;
import com.akavrt.csp._1d.solver.Algorithm;
import com.akavrt.csp._1d.solver.evo.EvolutionaryComponentsFactory;
import com.akavrt.csp._1d.solver.evo.es.BaseStrategyComponentsFactory;
import com.akavrt.csp._1d.solver.evo.es.EvolutionStrategy;
import com.akavrt.csp._1d.solver.evo.es.EvolutionStrategyParameters;
import com.akavrt.csp._1d.solver.pattern.PatternGenerator;
import com.akavrt.csp._1d.solver.pattern.PatternGeneratorParameters;
import com.akavrt.csp._1d.solver.pattern.UnconstrainedPatternGenerator;
import com.akavrt.csp._1d.solver.x.ReductionAlgorithm;
import com.akavrt.csp._1d.tester.batch.config.StrategyBatchConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * User: akavrt
 * Date: 07.07.13
 * Time: 01:34
 */
public class SplitBatchTester extends BatchTester {
    private static final Logger LOGGER = LogManager.getLogger(SplitBatchTester.class);
    private final StrategyBatchConfig config;

    public SplitBatchTester(StrategyBatchConfig config) {
        super(config.getBatchFilePath(), config.getNumberOfRuns());

        this.config = config;
    }

    public static void main(String[] args) {
        StrategyBatchConfig config = new StrategyBatchConfig(args);
        if (config.isLoaded()) {
            SplitBatchTester tester = new SplitBatchTester(config);
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

    private Metric createConstrainedObjectiveFunction() {
        ConstraintAwareMetricParameters constrainedMetricParameters = config
                .getObjectiveFunctionParameters();
        if (constrainedMetricParameters == null) {
            constrainedMetricParameters = new ConstraintAwareMetricParameters();
        }

        return new ConstraintAwareMetric(constrainedMetricParameters);
    }

    @Override
    protected Algorithm createAlgorithm() {
        PatternGenerator generator = createPatternGenerator();
        EvolutionaryComponentsFactory factory = new BaseStrategyComponentsFactory(generator);
        Metric objectiveFunction = createConstrainedObjectiveFunction();

        EvolutionStrategyParameters strategyParameters = config.getAlgorithmParameters();
        if (strategyParameters == null) {
            strategyParameters = new EvolutionStrategyParameters();
        }

        Algorithm auxAlgorithm = new EvolutionStrategy(factory, objectiveFunction,
                                                       strategyParameters);

        return new ReductionAlgorithm(auxAlgorithm, 10);
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

}
