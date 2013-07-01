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
import com.akavrt.csp._1d.solver.SimpleSolver;
import com.akavrt.csp._1d.solver.evo.EvolutionaryComponentsFactory;
import com.akavrt.csp._1d.solver.evo.es.BaseStrategyComponentsFactory;
import com.akavrt.csp._1d.solver.evo.es.EvolutionStrategy;
import com.akavrt.csp._1d.solver.evo.es.EvolutionStrategyParameters;
import com.akavrt.csp._1d.solver.pattern.PatternGenerator;
import com.akavrt.csp._1d.solver.pattern.PatternGeneratorParameters;
import com.akavrt.csp._1d.solver.pattern.UnconstrainedPatternGenerator;

import java.util.List;

/**
 * User: akavrt
 * Date: 02.07.13
 * Time: 00:52
 */
public class StrategyTester {

    public static void main(String[] args) {
        new StrategyTester().run();
    }

    private void run() {
        Problem problem = generateProblem();
        System.out.println(problem);

        Algorithm algorithm = createAlgorithm();
        SimpleSolver solver = new SimpleSolver(problem, algorithm);

        List<Plan> plans = solver.solve();

        if (plans.size() > 0 && plans.get(0) != null) {
            // trace solution
            System.out.println(plans.get(0));
        }
    }

    private Algorithm createAlgorithm() {
        PatternGeneratorParameters patternParameters = new PatternGeneratorParameters();
        patternParameters.setGenerationTrialsLimit(5);
        PatternGenerator generator = new UnconstrainedPatternGenerator(patternParameters);

        ConstraintAwareMetricParameters objectiveParameters = new ConstraintAwareMetricParameters();
        objectiveParameters.setAggregatedTrimFactor(0.1);
        objectiveParameters.setPatternsFactor(0.9);
        Metric objectiveFunction = new ConstraintAwareMetric(objectiveParameters);

        EvolutionaryComponentsFactory factory = new BaseStrategyComponentsFactory(generator);

        EvolutionStrategyParameters strategyParameters = new EvolutionStrategyParameters();
        strategyParameters.setPopulationSize(50);
        strategyParameters.setOffspringCount(45);
        strategyParameters.setRunSteps(5000);

        return new EvolutionStrategy(factory, objectiveFunction, strategyParameters);
    }


    private Problem generateProblem() {
        ProblemDescriptors descriptors = new ProblemDescriptors(40, 1000, 0.2, 0.8, 100);

        PseudoRandom rGen = new PseudoRandom(1994);
        ProblemGenerator pGen = new ProblemGenerator(rGen, descriptors);

        return pGen.nextProblem();
    }
}
