package com.akavrt.csp._1d.solver.sequential;

import com.akavrt.csp._1d.core.Plan;
import com.akavrt.csp._1d.solver.pattern.PatternGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

public class VahrenkampProcedure extends SequentialProcedure {
    private static final String METHOD_NAME = "Vahrenkamp's sequential heuristic procedure";
    private static final String SHORT_METHOD_NAME = "Vahrenkamp's SHP";
    private static final Logger LOGGER = LogManager.getFormatterLogger(VahrenkampProcedure.class);
    private final VahrenkampProcedureParameters params;
    private final Random rGen;

    public VahrenkampProcedure(PatternGenerator generator) {
        this(generator, new VahrenkampProcedureParameters());
    }

    public VahrenkampProcedure(PatternGenerator generator, VahrenkampProcedureParameters params) {
        super(generator);
        this.params = params;

        rGen = new Random();
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

        double allowedTrimRatio = params.getTrimRatioLowerBound();
        int patternUsage = evaluatePatternUsage();

        while (!context.isCancelled() && !orderManager.isOrdersFulfilled()) {
            LOGGER.debug("#TRIM_AL: %.2f  #PU_AL: %d", allowedTrimRatio, patternUsage);

            // search for the next suitable pattern
            // to be added to the partial solution
            BuildingBlock block = patternGeneration(allowedTrimRatio, patternUsage);

            if (block != null) {
                // adjust production and stock
                orderManager.updateDemand(block.pattern, block.multiplier);

                // add pattern to the partial solution
                solution.addPattern(block.pattern, block.multiplier);

                // reset aspiration levels
                allowedTrimRatio = params.getTrimRatioLowerBound();
                patternUsage = evaluatePatternUsage();
                LOGGER.debug("  RESET  #TRIM_AL: %.2f  #PU_AL: %d", allowedTrimRatio, patternUsage);
            } else {
                // relax one of the aspiration levels
                // which level to relax is decided based on a draw
                double draw = rGen.nextDouble();
                if (draw > params.getGoalmix()) {
                    // relax pattern usage
                    /*
                    if (patternUsage > 1) {
                        patternUsage -= params.getPatternUsageRelaxStep();
                        LOGGER.debug("  RELAX  #PU_AL to %d", patternUsage);
                    } else {
                        LOGGER.debug("  [SKIP] RELAX for #PU_AL: %d", patternUsage);
                    }
                    */
                    if (patternUsage > 1) {
                        patternUsage -= params.getPatternUsageRelaxStep();
                        LOGGER.debug("  RELAX  #PU_AL to %d", patternUsage);
                    } else if (allowedTrimRatio < 1) {
                        // this should prevent endless looping
                        allowedTrimRatio += params.getTrimRatioRelaxStep();
                        LOGGER.debug("  [FORCE] RELAX  #TRIM_AL to %.2f", allowedTrimRatio);
                    }
                } else {
                    // relax trim
                    /*
                    if (allowedTrimRatio < params.getTrimRatioUpperBound()) {
                        allowedTrimRatio += params.getTrimRatioRelaxStep();
                        LOGGER.debug("  RELAX  #TRIM_AL to %.2f", allowedTrimRatio);
                    } else if (patternUsage == 1 && allowedTrimRatio < 1) {
                        // this should prevent endless looping
                        allowedTrimRatio += params.getTrimRatioRelaxStep();
                        LOGGER.debug("  [FORCE] RELAX  #TRIM_AL to %.2f", allowedTrimRatio);
                    }
                    */
                    if (allowedTrimRatio < params.getTrimRatioUpperBound()) {
                        allowedTrimRatio += params.getTrimRatioRelaxStep();
                        LOGGER.debug("  RELAX  #TRIM_AL to %.2f", allowedTrimRatio);
                    } else if (patternUsage > 1) {
                        // this should prevent endless looping
                        patternUsage -= params.getPatternUsageRelaxStep();
                        LOGGER.debug("  [FORCE] RELAX  #PU_AL to %d", patternUsage);
                    }

                }
            }
        }

        return solution;
    }

}
