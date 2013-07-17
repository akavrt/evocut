package com.akavrt.csp._1d.tester;

import com.akavrt.csp._1d.core.Plan;
import com.akavrt.csp._1d.core.Problem;
import com.akavrt.csp._1d.cutgen.ProblemDescriptors;
import com.akavrt.csp._1d.cutgen.ProblemGenerator;
import com.akavrt.csp._1d.cutgen.PseudoRandom;
import com.akavrt.csp._1d.solver.Algorithm;
import com.akavrt.csp._1d.solver.SimpleSolver;
import com.akavrt.csp._1d.solver.Solver;
import com.akavrt.csp._1d.solver.evo.EvolutionaryAlgorithmParameters;
import com.akavrt.csp._1d.solver.evo.EvolutionaryOperator;
import com.akavrt.csp._1d.solver.evo.operators.*;
import com.akavrt.csp._1d.solver.moea.MoeaAlgorithm;
import com.akavrt.csp._1d.solver.pattern.PatternGenerator;
import com.akavrt.csp._1d.solver.pattern.PatternGeneratorParameters;
import com.akavrt.csp._1d.solver.pattern.UnconstrainedPatternGenerator;
import com.akavrt.csp._1d.solver.sequential.SimplifiedProcedure;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * User: akavrt
 * Date: 17.07.13
 * Time: 00:48
 */
public class MoeaTester {
    public static void main(String[] args) {
        new MoeaTester().run();
    }

    private void run() {
        Problem problem = generateProblem();
        System.out.println(problem);

        Algorithm algorithm = createMoeaAlgorithm();

        Solver solver = new SimpleSolver(problem, algorithm);
        List<Plan> plans = solver.solve();

        Collections.sort(plans, new Comparator<Plan>() {
            @Override
            public int compare(Plan lhs, Plan rhs) {
                int result;
                if (lhs.getMaterialUsage() == rhs.getMaterialUsage()) {
                    result = lhs.getSetups() == rhs.getSetups()
                            ? 0
                            : (lhs.getSetups() < rhs.getSetups() ? -1 : 1);
                } else {
                    result = lhs.getMaterialUsage() < rhs.getMaterialUsage() ? -1 : 1;
                }

                return result;
            }
        });

        traceResults(plans);
    }

    private void traceResults(List<Plan> plans) {
        int i = 0;
        for (Plan plan : plans) {
            String feasibility = plan.isFeasible() ? "feasible" : "infeasible";
            String trace = String.format("#%2d:  M = %d  S = %d, %s", ++i,
                                         plan.getMaterialUsage(), plan.getSetups(), feasibility);
            System.out.println(trace);
        }
    }

    private PatternGenerator createPatternGenerator() {
        PatternGeneratorParameters patternParameters = new PatternGeneratorParameters();
        patternParameters.setGenerationTrialsLimit(5);

        return new UnconstrainedPatternGenerator(patternParameters);
    }

    private Algorithm createInitAlgorithm(PatternGenerator generator) {
        return new SimplifiedProcedure(generator);
    }

    private EvolutionaryOperator createMutationOperator(PatternGenerator generator) {
        CompositeMutation mutation = new CompositeMutation();

        mutation.addOperator(new IncrementMultiplierMutation(generator));
        mutation.addOperator(new DecrementMultiplierMutation(generator));
        mutation.addOperator(new AdaptPatternMutation(generator));
        mutation.addOperator(new AdaptMultiplierMutation(generator));
        mutation.addOperator(new MergePatternsMutation(generator, 2, 10));

        return mutation;
    }

    private Algorithm createMoeaAlgorithm() {
        PatternGenerator generator = createPatternGenerator();

        Algorithm initAlgorithm = createInitAlgorithm(generator);

        EvolutionaryOperator mutation = createMutationOperator(generator);

        EvolutionaryAlgorithmParameters moeaParameters = new EvolutionaryAlgorithmParameters();
        moeaParameters.setPopulationSize(100);
        moeaParameters.setRunSteps(2000);

        return new MoeaAlgorithm(initAlgorithm, mutation, moeaParameters);
    }

    private Problem generateProblem() {
        ProblemDescriptors descriptors = new ProblemDescriptors(10, 1000, 0.01, 0.2, 10);

        PseudoRandom rGen = new PseudoRandom(1994);
        ProblemGenerator pGen = new ProblemGenerator(rGen, descriptors);

        return pGen.nextProblem();
    }

}
