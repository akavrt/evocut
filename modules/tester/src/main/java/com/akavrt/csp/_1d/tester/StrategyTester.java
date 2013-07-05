package com.akavrt.csp._1d.tester;

import com.akavrt.csp._1d.core.Plan;
import com.akavrt.csp._1d.core.Problem;
import com.akavrt.csp._1d.cutgen.ProblemDescriptors;
import com.akavrt.csp._1d.cutgen.ProblemGenerator;
import com.akavrt.csp._1d.cutgen.PseudoRandom;
import com.akavrt.csp._1d.metrics.ConstraintAwareMetric;
import com.akavrt.csp._1d.metrics.ConstraintAwareMetricParameters;
import com.akavrt.csp._1d.metrics.Metric;
import com.akavrt.csp._1d.solver.Algorithm;
import com.akavrt.csp._1d.solver.MultistartSolver;
import com.akavrt.csp._1d.solver.evo.es.EvolutionStrategy;
import com.akavrt.csp._1d.solver.evo.es.EvolutionStrategyParameters;
import com.akavrt.csp._1d.solver.pattern.PatternGenerator;
import com.akavrt.csp._1d.solver.pattern.PatternGeneratorParameters;
import com.akavrt.csp._1d.solver.pattern.UnconstrainedPatternGenerator;
import com.akavrt.csp._1d.solver.sequential.HaesslerProcedure;
import com.akavrt.csp._1d.solver.sequential.VahrenkampProcedure;
import com.akavrt.csp._1d.solver.sequential.VahrenkampProcedureParameters;

/**
 * User: akavrt
 * Date: 02.07.13
 * Time: 00:52
 */
public class StrategyTester {
    private TraceableStrategyComponentsFactory factory;

    public static void main(String[] args) {
        new StrategyTester().run();
    }

    private void run() {
        Problem problem = generateProblem();
        System.out.println(problem);

        Metric objective = createMetric();

        Algorithm algorithm = createAlgorithm(objective);

        MultistartSolver solver = new MultistartSolver(problem, algorithm, 10);

        solver.solve();

        Plan best = solver.getBestPlan(objective);
        if (best != null) {
            // trace solution
            System.out.println(best);
        }

        factory.traceMutation();
    }

    private Metric createMetric() {
        ConstraintAwareMetricParameters objectiveParameters = new ConstraintAwareMetricParameters();
        objectiveParameters.setAggregatedTrimFactor(0.1);
        objectiveParameters.setPatternsFactor(0.9);
        Metric objectiveFunction = new ConstraintAwareMetric(objectiveParameters);

        return objectiveFunction;
    }

    private Algorithm createAlgorithm(Metric objectiveFunction) {
        PatternGeneratorParameters patternParameters = new PatternGeneratorParameters();
        patternParameters.setGenerationTrialsLimit(5);
        PatternGenerator generator = new UnconstrainedPatternGenerator(patternParameters);

        factory = new TraceableStrategyComponentsFactory(generator, objectiveFunction);

        EvolutionStrategyParameters strategyParameters = new EvolutionStrategyParameters();
        strategyParameters.setPopulationSize(50);
        strategyParameters.setOffspringCount(45);
        strategyParameters.setRunSteps(10000);

        return new EvolutionStrategy(factory, objectiveFunction, strategyParameters);
    }

    private Problem generateProblem() {
        ProblemDescriptors descriptors = new ProblemDescriptors(40, 1000, 0.2, 0.8, 100);

        PseudoRandom rGen = new PseudoRandom(1994);
        ProblemGenerator pGen = new ProblemGenerator(rGen, descriptors);

        return pGen.nextProblem();
    }

}
