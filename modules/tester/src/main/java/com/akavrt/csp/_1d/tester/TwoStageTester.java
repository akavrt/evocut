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
import com.akavrt.csp._1d.solver.evo.EvolutionaryComponentsFactory;
import com.akavrt.csp._1d.solver.evo.es.BaseStrategyComponentsFactory;
import com.akavrt.csp._1d.solver.evo.es.EvolutionStrategy;
import com.akavrt.csp._1d.solver.evo.es.EvolutionStrategyParameters;
import com.akavrt.csp._1d.solver.pattern.PatternGenerator;
import com.akavrt.csp._1d.solver.pattern.PatternGeneratorParameters;
import com.akavrt.csp._1d.solver.pattern.UnconstrainedPatternGenerator;
import com.akavrt.csp._1d.solver.x.ReductionAlgorithm;
import com.akavrt.csp._1d.solver.x.TunableStrategyComponentsFactory;

/**
 * User: akavrt
 * Date: 07.07.13
 * Time: 19:13
 */
public class TwoStageTester {

    public static void main(String[] args) {
        new TwoStageTester().run();
    }

    private void run() {
        Problem problem = generateProblem();
        System.out.println(problem);


        // let's tie everything together

        // pattern generator and objective function
        // are reused across entire two-step algorithm
        Metric objectiveFunction = createObjectiveFunction();
        PatternGenerator generator = createPatternGenerator();

        // creating aux algorithm used in the first step
        Algorithm auxAlgorithm = createAuxAlgorithm(generator, objectiveFunction);

        // creating first step algorithm (based on splitting of the original problem)
        Algorithm firstStepAlgorithm = createFirstStepAlgorithm(auxAlgorithm);

        // splitting algorithm is used as initialization algorithm in the second step
        PatternGenerator generator2 = createPatternGenerator();
        Algorithm secondStepAlgorithm = createSecondStepAlgorithm(generator2, objectiveFunction,
                                                                  firstStepAlgorithm);

        MultistartSolver solver = new MultistartSolver(problem, secondStepAlgorithm, 2);

        solver.solve();

        Plan best = solver.getBestPlan(objectiveFunction);
        if (best != null) {
            // trace solution
            System.out.println(best);
        }
    }

    private Metric createObjectiveFunction() {
        ConstraintAwareMetricParameters objectiveParameters = new ConstraintAwareMetricParameters();
        objectiveParameters.setAggregatedTrimFactor(0.2);
        objectiveParameters.setPatternsFactor(0.8);
        Metric objectiveFunction = new ConstraintAwareMetric(objectiveParameters);

        return objectiveFunction;
    }

    private PatternGenerator createPatternGenerator() {
        PatternGeneratorParameters patternParameters = new PatternGeneratorParameters();
        patternParameters.setGenerationTrialsLimit(5);

        return new UnconstrainedPatternGenerator(patternParameters);
    }

    private Algorithm createAuxAlgorithm(PatternGenerator generator, Metric objectiveFunction) {
        EvolutionaryComponentsFactory factory = new BaseStrategyComponentsFactory(generator);

        EvolutionStrategyParameters strategyParameters = new EvolutionStrategyParameters();
        strategyParameters.setPopulationSize(10);
        strategyParameters.setOffspringCount(9);
        strategyParameters.setRunSteps(1000);

        return new EvolutionStrategy(factory, objectiveFunction, strategyParameters);
    }

    private Algorithm createFirstStepAlgorithm(Algorithm auxAlgorithm) {
        return new ReductionAlgorithm(auxAlgorithm, 10);
    }

    private Algorithm createSecondStepAlgorithm(PatternGenerator generator,
                                                Metric objectiveFunction,
                                                Algorithm firstStepAlgorithm) {
        EvolutionaryComponentsFactory factory =
                new TunableStrategyComponentsFactory(generator, firstStepAlgorithm);

        EvolutionStrategyParameters strategyParameters = new EvolutionStrategyParameters();
        strategyParameters.setPopulationSize(50);
        strategyParameters.setOffspringCount(45);
        strategyParameters.setRunSteps(2000);

        return new EvolutionStrategy(factory, objectiveFunction, strategyParameters);
    }

    private Problem generateProblem() {
        ProblemDescriptors descriptors = new ProblemDescriptors(40, 1000, 0.2, 0.8, 100);

        PseudoRandom rGen = new PseudoRandom(1994);
        ProblemGenerator pGen = new ProblemGenerator(rGen, descriptors);

        return pGen.nextProblem();
    }

}
