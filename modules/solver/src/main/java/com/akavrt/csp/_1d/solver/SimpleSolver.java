package com.akavrt.csp._1d.solver;

import com.akavrt.csp._1d.core.Plan;
import com.akavrt.csp._1d.core.Problem;
import com.akavrt.csp._1d.metrics.Metric;

import java.util.List;

/**
 * User: akavrt
 * Date: 26.06.13
 * Time: 19:00
 */
public class SimpleSolver implements Solver {
    private Problem problem;
    private final Algorithm algorithm;
    private List<Plan> plans;

    public SimpleSolver(Algorithm algorithm) {
        this(null, algorithm);
    }

    public SimpleSolver(Problem problem, Algorithm algorithm) {
        this.problem = problem;
        this.algorithm = algorithm;
    }

    protected List<Plan> run() {
        return algorithm.execute(context);
    }

    @Override
    public final List<Plan> solve() {
        plans = null;

        if (problem != null && algorithm != null) {
            plans = run();
        }

        return plans;
    }

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public List<Plan> getPlans() {
        return plans;
    }

    public Plan getBestPlan(Metric metric) {
        if (plans == null) {
            return null;
        }

        Plan best = null;
        for (Plan plan : plans) {
            if (best == null || metric.compare(plan, best) > 0) {
                best = plan;
            }
        }

        return best;
    }

    private final ExecutionContext context = new ExecutionContext() {

        @Override
        public Problem getProblem() {
            return problem;
        }

        @Override
        public boolean isCancelled() {
            return false;
        }
    };

}
