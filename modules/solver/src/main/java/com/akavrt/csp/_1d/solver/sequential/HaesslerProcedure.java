package com.akavrt.csp._1d.solver.sequential;

import com.akavrt.csp._1d.core.Plan;
import com.akavrt.csp._1d.solver.pattern.PatternGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HaesslerProcedure extends SequentialProcedure {
    private static final String METHOD_NAME = "Haessler's sequential heuristic procedure";
    private static final String SHORT_METHOD_NAME = "Haessler's SHP";
    private static final Logger LOGGER = LogManager.getFormatterLogger(HaesslerProcedure.class);
    private final SequentialProcedureParameters params;

    public HaesslerProcedure(PatternGenerator generator) {
        this(generator, new SequentialProcedureParameters());
    }

    public HaesslerProcedure(PatternGenerator generator, SequentialProcedureParameters params) {
        super(generator);
        this.params = params;
    }

    @Override
    protected String getShortMethodName() {
        return SHORT_METHOD_NAME;
    }

    @Override
    protected SequentialProcedureParameters getMethodParameters() {
        return params;
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    @Override
    public String name() {
        return METHOD_NAME;
    }

    @Override
    protected Plan search() {
        Plan solution = new Plan(context.getProblem());
        while (!context.isCancelled() && !orderManager.isOrdersFulfilled()) {
            BuildingBlock block = trimStep();

            if (block != null) {
                // adjust production
                orderManager.updateProduction(block.pattern, block.multiplier);

                // add pattern to the partial solution
                solution.addPattern(block.pattern, block.multiplier);
            }
        }

        return solution;
    }

    protected BuildingBlock trimStep() {
        BuildingBlock block = null;

        double allowedTrimRatio = params.getTrimRatioLowerBound();

        while (!context.isCancelled() && allowedTrimRatio < 1 && block == null) {
            LOGGER.debug("#TRIM_AL: %.2f", allowedTrimRatio);

            block = patternUsageStep(allowedTrimRatio);

            // relax requirements for trim loss
            allowedTrimRatio += params.getTrimRatioRelaxStep();
        }

        return block;
    }

    private BuildingBlock patternUsageStep(double allowedTrimRatio) {
        BuildingBlock block = null;

        // let's evaluate maximum possible pattern usage
        int patternUsage = evaluatePatternUsage();

        while (!context.isCancelled() && patternUsage > 0 && block == null) {
            LOGGER.debug("#TRIM_AL: %.2f  ##PU_AL: %d", allowedTrimRatio, patternUsage);

            block = patternGeneration(allowedTrimRatio, patternUsage);

            // relax requirements for pattern usage
            patternUsage -= params.getPatternUsageRelaxStep();
        }

        return block;
    }

}