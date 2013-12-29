package com.akavrt.csp._1d.solver.x;

import com.akavrt.csp._1d.core.Order;
import com.akavrt.csp._1d.core.Pattern;
import com.akavrt.csp._1d.core.Plan;
import com.akavrt.csp._1d.core.Problem;
import com.akavrt.csp._1d.solver.Algorithm;
import com.akavrt.csp._1d.solver.ExecutionContext;
import com.akavrt.csp._1d.utils.ParameterSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * User: akavrt
 * Date: 06.07.13
 * Time: 22:10
 */
public class ReductionAlgorithm implements Algorithm {
    private static final Logger LOGGER = LogManager.getLogger(ReductionAlgorithm.class);
    private final Algorithm auxAlgorithm;
    private final int subProblemSize;

    public ReductionAlgorithm(Algorithm auxAlgorithm, int subProblemSize) {
        this.auxAlgorithm = auxAlgorithm;
        this.subProblemSize = subProblemSize;
    }

    @Override
    public String name() {
        return "Reduction algorithm";
    }

    @Override
    public List<ParameterSet> getParameters() {
        return null;
    }


    @Override
    public List<Plan> execute(ExecutionContext context) {
        if (context.getProblem() == null) {
            return null;
        }

        Plan solution = search(context);

        return Lists.newArrayList(solution);
    }

    private Plan search(ExecutionContext context) {
        Problem problem = context.getProblem();

        List<Problem> subProblems = splitProblem(problem);

        Plan masterSolution = new Plan(problem);
        for (Problem subProblem : subProblems) {
            Plan splitSolution = solveSubProblem(context, subProblem);
            if (splitSolution != null) {
                addSubProblemSolution(masterSolution, splitSolution);
            }
        }

        return masterSolution;
    }

    private List<Problem> splitProblem(Problem problem) {
        List<Problem> subProblems = Lists.newArrayList();

        List<Order> orders = Lists.newArrayList();
        for (int i = 0; i < problem.size(); i++) {
            orders.add(problem.getOrder(i));
        }

        int subProblemMaxSize = (int) Math.ceil(subProblemSize * 1.5);

        Random rGen = new Random();
        // split main problem into subproblems
        while (orders.size() > 0) {
            List<Order> subproblemOrders = Lists.newArrayList();
            if (orders.size() <= subProblemMaxSize) {
                // add all remained orders to the subproblem
                subproblemOrders.addAll(orders);
                orders.clear();
            } else {
                // add prespecified number of orders to the subproblem
                while (subproblemOrders.size() < subProblemSize) {
                    // orders are selected randomly
                    int index = rGen.nextInt(orders.size());

                    Order order = orders.remove(index);
                    subproblemOrders.add(order);
                }
            }

            Problem subProblem = new Problem(problem.getStockLength(), subproblemOrders);
            subProblems.add(subProblem);
        }

        return subProblems;
    }

    private Plan solveSubProblem(ExecutionContext parentContext, Problem subProblem) {
        LocalExecutionContext context = new LocalExecutionContext(parentContext, subProblem);
        List<Plan> subProblemSolutions = auxAlgorithm.execute(context);

        return subProblemSolutions.isEmpty() ? null : subProblemSolutions.get(0);
    }

    private void addSubProblemSolution(Plan master, Plan split) {
        Problem masterProblem = master.getProblem();
        Map<Integer, Integer> indicesMap = Maps.newHashMap();
        for (int i = 0; i < masterProblem.size(); i++) {
            Order order = masterProblem.getOrder(i);
            indicesMap.put(order.getWidth(), i);
        }

        Problem splitProblem = split.getProblem();
        for (Map.Entry<Pattern, Integer> each : split.getPatterns()) {
            int[] masterCuts = new int[masterProblem.size()];

            int[] splitCuts = each.getKey().getCuts();
            for (int i = 0; i < splitProblem.size(); i++) {
                Order splitOrder = splitProblem.getOrder(i);

                if (indicesMap.containsKey(splitOrder.getWidth())) {
                    int index = indicesMap.get(splitOrder.getWidth());
                    masterCuts[index] = splitCuts[i];
                } else {
                    LOGGER.warn("Can't find master order index for split order with length {}",
                                splitOrder.getWidth());
                }

            }

            int splitMultiplier = each.getValue();
            master.addPattern(masterCuts, splitMultiplier);
        }
    }

    private static class LocalExecutionContext implements ExecutionContext {
        private final ExecutionContext parentContext;
        private final Problem subProblem;

        public LocalExecutionContext(ExecutionContext parentContext, Problem subProblem) {
            this.parentContext = parentContext;
            this.subProblem = subProblem;
        }

        @Override
        public Problem getProblem() {
            return subProblem;
        }

        @Override
        public boolean isCancelled() {
            return parentContext.isCancelled();
        }

    }

}
