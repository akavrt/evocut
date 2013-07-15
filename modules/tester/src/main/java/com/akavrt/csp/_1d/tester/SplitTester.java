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
import com.akavrt.csp._1d.solver.evo.es.BaseStrategyComponentsFactory;
import com.akavrt.csp._1d.solver.evo.es.EvolutionStrategy;
import com.akavrt.csp._1d.solver.evo.es.EvolutionStrategyParameters;
import com.akavrt.csp._1d.solver.pattern.PatternGenerator;
import com.akavrt.csp._1d.solver.pattern.PatternGeneratorParameters;
import com.akavrt.csp._1d.solver.pattern.UnconstrainedPatternGenerator;
import com.akavrt.csp._1d.solver.x.ReductionAlgorithm;

/**
 * User: akavrt
 * Date: 07.07.13
 * Time: 01:06
 */
public class SplitTester {

    public static void main(String[] args) {
        new SplitTester().run();
    }

    private void run() {
        Problem problem = generateProblem();
        System.out.println(problem);

        Metric objective = createMetric();

        Algorithm auxAlgorithm = createAuxAlgorithm(objective);

        Algorithm splitAlgorithm = new ReductionAlgorithm(auxAlgorithm, 10);

        MultistartSolver solver = new MultistartSolver(problem, splitAlgorithm, 50);
        //        SimpleSolver solver = new SimpleSolver(problem, splitAlgorithm);

        solver.solve();

        Plan best = solver.getBestPlan(objective);
        if (best != null) {
            // trace solution
            System.out.println(best);
        }
    }

    private Metric createMetric() {
        ConstraintAwareMetricParameters objectiveParameters = new ConstraintAwareMetricParameters();
        objectiveParameters.setAggregatedTrimFactor(0.1);
        objectiveParameters.setPatternsFactor(0.9);

        return new ConstraintAwareMetric(objectiveParameters);
    }

    private Algorithm createAuxAlgorithm(Metric objectiveFunction) {
        PatternGeneratorParameters patternParameters = new PatternGeneratorParameters();
        patternParameters.setGenerationTrialsLimit(5);
        PatternGenerator generator = new UnconstrainedPatternGenerator(patternParameters);

        BaseStrategyComponentsFactory factory = new BaseStrategyComponentsFactory(generator);

        EvolutionStrategyParameters strategyParameters = new EvolutionStrategyParameters();
        strategyParameters.setPopulationSize(10);
        strategyParameters.setOffspringCount(9);
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

