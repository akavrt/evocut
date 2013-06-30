package com.akavrt.csp._1d.solver.sequential;

import com.akavrt.csp._1d.core.Plan;
import com.akavrt.csp._1d.core.Problem;
import com.akavrt.csp._1d.solver.Algorithm;
import com.akavrt.csp._1d.solver.ExecutionContext;
import com.akavrt.csp._1d.solver.pattern.PatternGenerator;
import com.akavrt.csp._1d.utils.ParameterSet;
import com.akavrt.csp._1d.utils.Utils;
import com.google.common.collect.Lists;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * <p>Immediate base class for all implemented versions of sequential heuristic procedures.</p>
 */
public abstract class SequentialProcedure implements Algorithm {
    private final PatternGenerator patternGenerator;
    protected ExecutionContext context;
    protected OrderManager orderManager;

    public SequentialProcedure(PatternGenerator generator) {
        this.patternGenerator = generator;
    }

    protected abstract String getShortMethodName();

    protected abstract SequentialProcedureParameters getMethodParameters();

    protected abstract Plan search();

    protected abstract Logger getLogger();

    @Override
    public List<Plan> execute(ExecutionContext context) {
        if (patternGenerator == null || context.getProblem() == null) {
            return null;
        }

        this.context = context;

        Problem problem = context.getProblem();
        patternGenerator.initialize(problem);

        orderManager = new OrderManager(problem);

        Plan solution = search();

        return Lists.newArrayList(solution);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ParameterSet> getParameters() {
        List<ParameterSet> parameters = Lists.newArrayList();
        parameters.add(getMethodParameters());
        parameters.add(patternGenerator.getParameters());

        return parameters;
    }

    protected int evaluatePatternUsage() {
        // let's evaluate maximum possible pattern usage
        // sometimes we need to restrict pattern usage,
        // this is done using corresponding parameter defined as fractional ratio:
        // for ratio = 1.0 schedule all unfulfilled orders,
        // for ratio = 0.5 schedule only one half of the unfulfilled orders, etc.
        double upperBoundRatio = getMethodParameters().getPatternUsageUpperBound();
        int evaluated = (int) (upperBoundRatio * orderManager.getMaxUnfulfilledDemand());

        return Math.max(1, evaluated);
    }

    protected BuildingBlock patternGeneration(double allowedTrimRatio, int patternUsage) {
        BuildingBlock block = null;

        int[] demand = orderManager.calculateDemand(patternUsage);

        int[] pattern = patternGenerator.generate(demand, allowedTrimRatio);
        if (pattern != null && orderManager.getPatternTrimRatio(pattern) <= allowedTrimRatio) {
            // pattern search succeeded
            block = new BuildingBlock(pattern, patternUsage);

            if (getLogger().isDebugEnabled()) {
                debugPatternGeneration(demand, block);
            }
        }

        return block;
    }

    private void debugPatternGeneration(int[] demand, BuildingBlock block) {
        double trimRatio = orderManager.getPatternTrimRatio(block.pattern);

        getLogger().debug(Utils.convertIntArrayToString("       demand", demand));
        getLogger().debug(Utils.convertIntArrayToString("      pattern", block.pattern));
        getLogger().debug("      TL ratio = %.4f", trimRatio);
    }

    protected static class BuildingBlock {
        public final int[] pattern;
        public final int multiplier;

        public BuildingBlock(int[] pattern, int multiplier) {
            this.pattern = pattern;
            this.multiplier = multiplier;
        }
    }

}
