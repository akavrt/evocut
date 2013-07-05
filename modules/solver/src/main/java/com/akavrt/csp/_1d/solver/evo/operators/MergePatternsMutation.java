package com.akavrt.csp._1d.solver.evo.operators;

import com.akavrt.csp._1d.core.Pattern;
import com.akavrt.csp._1d.core.Plan;
import com.akavrt.csp._1d.solver.pattern.PatternGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * User: akavrt
 * Date: 02.07.13
 * Time: 20:47
 */
public class MergePatternsMutation extends Mutation {
    private static final Logger LOGGER = LogManager.getLogger(MergePatternsMutation.class);
    private final int lbMergeCount;
    private final int ubMergeCount;

    public MergePatternsMutation(PatternGenerator generator, int lb, int ub) {
        super(generator);
        this.lbMergeCount = lb;
        this.ubMergeCount = ub;
    }

    @Override
    public Plan apply(Plan... chromosomes) {
        final Plan mutated = new Plan(chromosomes[0]);

        int bound = lbMergeCount + rGen.nextInt(ubMergeCount - lbMergeCount + 1);
        if (mutated.size() < bound) {
            LOGGER.debug("Not enough patterns to merge.");
            return mutated;
        }

        double toleranceRatio = calculateLengthToleranceRatio(mutated);

        // pick patterns randomly
        int multiplier = 0;
        for (int i = 0; i < bound; i++) {
            Pattern pattern = mutated.pickPattern(rGen);
            if (pattern == null) {
                LOGGER.warn("Failed to randomly pick pattern #{}.", i + 1);
                return mutated;
            }

            multiplier += mutated.removePattern(pattern);
        }

        int[] demand = calculateDemand(mutated, multiplier);
        int[] cuts = generator.generate(demand, toleranceRatio);
        if (cuts != null) {
            mutated.addPattern(cuts, multiplier);
        }

        if (bound > 2 && !mutated.isFeasible()) {
            // lets try to compensate feasibility loss
            // with additional pattern

            int additionalPatterns = 0;
            int additionalPatternsBound = bound - 2;
            do  {
                multiplier = calculateUpperBound(mutated);

                demand = calculateDemand(mutated, multiplier);
                cuts = generator.generate(demand, toleranceRatio);
                if (cuts != null) {
                    mutated.addPattern(cuts, multiplier);
                    additionalPatterns++;
                }
            }
            while (!mutated.isFeasible() && additionalPatterns < additionalPatternsBound);
        }

        return mutated;
    }

}
