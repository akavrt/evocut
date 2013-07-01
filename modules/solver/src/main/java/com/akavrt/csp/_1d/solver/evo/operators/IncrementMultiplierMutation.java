package com.akavrt.csp._1d.solver.evo.operators;

import com.akavrt.csp._1d.core.Pattern;
import com.akavrt.csp._1d.core.Plan;
import com.akavrt.csp._1d.solver.pattern.PatternGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * User: akavrt
 * Date: 01.07.13
 * Time: 23:59
 */
public class IncrementMultiplierMutation extends Mutation {
    private static final Logger LOGGER = LogManager.getLogger(IncrementMultiplierMutation.class);

    public IncrementMultiplierMutation(PatternGenerator generator) {
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
            LOGGER.warn("Failed to randomly pick pattern.");
            return mutated;
        }

        int multiplier = mutated.removePattern(pattern);

        // increment multiplier
        multiplier++;

        int[] demand = calculateDemand(mutated, multiplier);
        int[] cuts = generator.generate(demand, toleranceRatio);
        if (cuts != null) {
            mutated.addPattern(cuts, multiplier);
        }

        return mutated;
    }

}

