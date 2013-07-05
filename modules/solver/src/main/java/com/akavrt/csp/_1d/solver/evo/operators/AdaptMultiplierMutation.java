package com.akavrt.csp._1d.solver.evo.operators;

import com.akavrt.csp._1d.core.Pattern;
import com.akavrt.csp._1d.core.Plan;
import com.akavrt.csp._1d.solver.pattern.PatternGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * User: akavrt
 * Date: 02.07.13
 * Time: 00:25
 */
public class AdaptMultiplierMutation extends Mutation {
    private static final Logger LOGGER = LogManager.getLogger(AdaptMultiplierMutation.class);

    public AdaptMultiplierMutation(PatternGenerator generator) {
        super(generator);
    }

    @Override
    public Plan apply(Plan... chromosomes) {
        final Plan mutated = new Plan(chromosomes[0]);

        if (mutated.size() == 0) {
            LOGGER.warn("Trying to change empty plan.");
            return mutated;
        }

        double toleranceRatio = calculateLengthToleranceRatio(mutated);

        // pick pattern randomly
        Pattern pattern = mutated.pickPattern(rGen);
        if (pattern == null) {
            LOGGER.debug("Failed to randomly pick pattern.");
            return mutated;
        }

        mutated.removePattern(pattern);

        // calculate new multiplier as an upper bound for residual problem
        int multiplier = calculateUpperBound(mutated);

        int[] demand = calculateDemand(mutated, multiplier);
        int[] cuts = generator.generate(demand, toleranceRatio);
        if (cuts != null) {
            mutated.addPattern(cuts, multiplier);
        }

        return mutated;
    }

}

