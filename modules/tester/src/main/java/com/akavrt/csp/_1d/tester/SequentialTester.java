package com.akavrt.csp._1d.tester;

import com.akavrt.csp._1d.core.Plan;
import com.akavrt.csp._1d.core.Problem;
import com.akavrt.csp._1d.cutgen.ProblemDescriptors;
import com.akavrt.csp._1d.cutgen.ProblemGenerator;
import com.akavrt.csp._1d.cutgen.PseudoRandom;
import com.akavrt.csp._1d.solver.Algorithm;
import com.akavrt.csp._1d.solver.SimpleSolver;
import com.akavrt.csp._1d.solver.pattern.PatternGenerator;
import com.akavrt.csp._1d.solver.pattern.UnconstrainedPatternGenerator;
import com.akavrt.csp._1d.solver.sequential.HaesslerProcedure;
import com.akavrt.csp._1d.solver.sequential.SimplifiedProcedure;
import com.akavrt.csp._1d.solver.sequential.VahrenkampProcedure;

import java.util.List;

/**
 * User: akavrt
 * Date: 27.06.13
 * Time: 17:52
 */
public class SequentialTester {

    public static void main(String[] args) {
        new SequentialTester().run();
    }

    private void run() {
        Problem problem = generateProblem();
        System.out.println(problem);

        PatternGenerator generator = new UnconstrainedPatternGenerator();
        Algorithm algorithm = new VahrenkampProcedure(generator);

        SimpleSolver solver = new SimpleSolver(problem, algorithm);

        List<Plan> plans = solver.solve();

        if (plans.size() > 0 && plans.get(0) != null) {
            // trace solution
            System.out.println(plans.get(0));
        }
    }

    private Problem generateProblem() {
        ProblemDescriptors descriptors = new ProblemDescriptors(40, 1000, 0.2, 0.8, 100);

        PseudoRandom rGen = new PseudoRandom(1994);
        ProblemGenerator pGen = new ProblemGenerator(rGen, descriptors);

        return pGen.nextProblem();
    }

}
