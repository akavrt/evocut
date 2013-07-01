package com.akavrt.csp._1d.solver.evo.operators;

import com.akavrt.csp._1d.core.Pattern;
import com.akavrt.csp._1d.core.Plan;
import com.akavrt.csp._1d.solver.pattern.PatternGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * User: akavrt
 * Date: 02.07.13
 * Time: 00:11
 */
public class MergePatternsMutation extends Mutation {
    private static final Logger LOGGER = LogManager.getLogger(MergePatternsMutation.class);

    public MergePatternsMutation(PatternGenerator generator) {
        super(generator);
    }

    @Override
    public Plan apply(Plan... chromosomes) {
        final Plan mutated = new Plan(chromosomes[0]);

        if (mutated.size() < 2) {
            LOGGER.warn("Nothing to merge.");
            return mutated;
        }

        double toleranceRatio = calculateLengthToleranceRatio(mutated);

        // pick two patterns randomly
        Pattern firstPattern = mutated.pickPattern(rGen);
        if (firstPattern == null) {
            LOGGER.warn("Failed to randomly pick first pattern.");
            return mutated;
        }

        int firstMultiplier = mutated.removePattern(firstPattern);

        Pattern secondPattern = mutated.pickPattern(rGen);
        if (secondPattern == null) {
            LOGGER.warn("Failed to randomly pick second pattern.");
            return mutated;
        }

        int secondMultiplier = mutated.removePattern(secondPattern);

        int multiplier = firstMultiplier + secondMultiplier;

        int[] demand = calculateDemand(mutated, multiplier);
        int[] cuts = generator.generate(demand, toleranceRatio);
        if (cuts != null) {
            mutated.addPattern(cuts, multiplier);
        }

        return mutated;
    }

}
