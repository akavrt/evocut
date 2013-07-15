package com.akavrt.csp._1d.tester;

import com.akavrt.csp._1d.analyzer.Average;
import com.akavrt.csp._1d.analyzer.SimpleCollector;
import com.akavrt.csp._1d.core.Problem;
import com.akavrt.csp._1d.cutgen.ProblemDescriptors;
import com.akavrt.csp._1d.metrics.*;
import com.akavrt.csp._1d.solver.Algorithm;
import com.akavrt.csp._1d.solver.MultistartSolver;
import com.akavrt.csp._1d.solver.ProblemClass;
import com.akavrt.csp._1d.solver.ProblemProvider;
import com.akavrt.csp._1d.solver.evo.EvolutionaryComponentsFactory;
import com.akavrt.csp._1d.solver.evo.es.BaseStrategyComponentsFactory;
import com.akavrt.csp._1d.solver.evo.es.EvolutionStrategy;
import com.akavrt.csp._1d.solver.evo.es.EvolutionStrategyParameters;
import com.akavrt.csp._1d.solver.pattern.PatternGenerator;
import com.akavrt.csp._1d.solver.pattern.PatternGeneratorParameters;
import com.akavrt.csp._1d.solver.pattern.UnconstrainedPatternGenerator;
import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * User: akavrt
 * Date: 09.07.13
 * Time: 00:55
 */
public class TradeoffAnalyzer {
    private static final Logger LOGGER = LogManager.getLogger(TradeoffAnalyzer.class);

    public static void main(String[] args) {
        new TradeoffAnalyzer().process();
    }

    public void process() {
        LOGGER.info("*** START OF THE RUN ***");

        List<ProblemClass> problemClasses = createProblemClasses();
        for (ProblemClass problemClass : problemClasses) {
            LOGGER.info("Processing class {}", problemClass.getName());
            int i = 0;
            double aggregatedTrimFactor = 0.1;
            while (aggregatedTrimFactor <= 0.91) {
                LOGGER.info("#{}: C_1 = {}, C_2 = {}", ++i,
                            String.format("%.1f", aggregatedTrimFactor),
                            String.format("%.1f", 1 - aggregatedTrimFactor));

                processClass(problemClass, aggregatedTrimFactor);

                aggregatedTrimFactor += 0.1;
            }

        }

        LOGGER.info("*** END OF THE RUN ***");
    }

    private void processClass(ProblemClass problemClass, double aggregatedTrimFactor) {
        Metric objectiveFunction = createMetric(aggregatedTrimFactor);
        Algorithm method = createAlgorithm(objectiveFunction);

        MultistartSolver solver = new MultistartSolver(method, 1);

        SimpleCollector collector = createCollector();
        solver.addCollector(collector);

        ProblemProvider problemProvider = new ProblemProvider(problemClass);
        int i = 0;
        for (Problem problem : problemProvider) {
            LOGGER.debug("Solving problem #{}", ++i);

            solver.setProblem(problem);
            solver.solve();
        }

        collector.process();
    }

    private List<ProblemClass> createProblemClasses() {
        List<ProblemClass> problemClasses = Lists.newArrayList();

        ProblemDescriptors fw01 = new ProblemDescriptors(10, 1000, 0.01, 0.2, 10);
        problemClasses.add(createProblemClass(fw01, "fw01"));

        /*
        ProblemDescriptors fw02 = new ProblemDescriptors(10, 1000, 0.01, 0.2, 100);
        problemClasses.add(createProblemClass(fw02, "fw02"));

        ProblemDescriptors fw07 = new ProblemDescriptors(10, 1000, 0.01, 0.8, 10);
        problemClasses.add(createProblemClass(fw07, "fw07"));

        ProblemDescriptors fw08 = new ProblemDescriptors(10, 1000, 0.01, 0.8, 100);
        problemClasses.add(createProblemClass(fw08, "fw08"));

        ProblemDescriptors fw13 = new ProblemDescriptors(10, 1000, 0.2, 0.8, 10);
        problemClasses.add(createProblemClass(fw13, "fw13"));

        ProblemDescriptors fw14 = new ProblemDescriptors(10, 1000, 0.2, 0.8, 100);
        problemClasses.add(createProblemClass(fw14, "fw14"));
        */

        return problemClasses;
    }

    private ProblemClass createProblemClass(ProblemDescriptors descriptors, String name) {
        return new ProblemClass(100, descriptors, 1994, name);
    }

    private Metric createMetric(double aggregatedTrimFactor) {
        ConstraintAwareMetricParameters objectiveParameters = new ConstraintAwareMetricParameters();
        objectiveParameters.setAggregatedTrimFactor(aggregatedTrimFactor);
        objectiveParameters.setPatternsFactor(1 - aggregatedTrimFactor);
        Metric objectiveFunction = new ConstraintAwareMetric(objectiveParameters);

        return objectiveFunction;
    }

    private Algorithm createAlgorithm(Metric objectiveFunction) {
        PatternGeneratorParameters patternParameters = new PatternGeneratorParameters();
        patternParameters.setGenerationTrialsLimit(5);
        PatternGenerator generator = new UnconstrainedPatternGenerator(patternParameters);

        EvolutionaryComponentsFactory factory = new BaseStrategyComponentsFactory(generator);

        EvolutionStrategyParameters strategyParameters = new EvolutionStrategyParameters();
        strategyParameters.setPopulationSize(50);
        strategyParameters.setOffspringCount(45);
        strategyParameters.setRunSteps(2000);

        return new EvolutionStrategy(factory, objectiveFunction, strategyParameters);
    }

    private SimpleCollector createCollector() {
        SimpleCollector collector = new SimpleCollector();

        collector.addMeasure(new Average());

        collector.addMetric(new MaterialUsage());
        collector.addMetric(new SetupCounter());

        return collector;
    }

}
