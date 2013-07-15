package com.akavrt.csp._1d.solver.evo.operators;

import com.akavrt.csp._1d.core.Pattern;
import com.akavrt.csp._1d.core.Plan;
import com.akavrt.csp._1d.solver.pattern.PatternGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: akavrt
 * Date: 05.07.13
 * Time: 22:45
 */
public class MergeTwoPatternsMutation extends Mutation {
    private static final Logger LOGGER = LogManager.getLogger(MergeTwoPatternsMutation.class);

    public MergeTwoPatternsMutation(PatternGenerator generator) {
        super(generator);
    }

    @Override
    public Plan apply(Plan... chromosomes) {
        final Plan mutated = new Plan(chromosomes[0]);

        if (mutated.size() < 2) {
            LOGGER.debug("Not enough patterns to merge.");
            return mutated;
        }

        adaptiveMerge(mutated);
//        if (!exactMerge(mutated)) {
//            // use adaptive pattern generation
//        }

        return mutated;
    }

    private boolean exactMerge(Plan plan) {
        List<Pattern> patterns = new ArrayList<Pattern>(plan.size());
        List<Integer> multipliers = new ArrayList<Integer>(plan.size());

        for (Map.Entry<Pattern, Integer> each : plan.getPatterns()) {
            patterns.add(each.getKey());
            multipliers.add(each.getValue());
        }

        int firstIndex = -1;
        int secondIndex = -1;
        for (int i = 0; i < patterns.size() - 1; i++) {
            boolean isMergeable = false;

            for (int j = i + 1; j < patterns.size(); j++) {
                isMergeable = isMergeable(patterns.get(i).getCuts(), multipliers.get(i),
                                          patterns.get(j).getCuts(), multipliers.get(j));
                if (isMergeable) {
                    secondIndex = j;
                    break;
                }
            }

            if (isMergeable) {
                firstIndex = i;
                break;
            }
        }

        boolean isMergeablePatternsFound = firstIndex >= 0 && secondIndex >= 0;
        if (isMergeablePatternsFound) {
            Pattern firstPattern = patterns.get(firstIndex);
            int firstMultiplier = plan.removePattern(firstPattern);

            Pattern secondPattern = patterns.get(secondIndex);
            int secondMultiplier = plan.removePattern(secondPattern);

            int totalMultiplier = firstMultiplier + secondMultiplier;
            int[] cuts = new int[plan.getProblem().size()];
            for (int i = 0; i < cuts.length; i++) {
                int totalCuts = firstPattern.getCuts()[i] * firstMultiplier +
                        secondPattern.getCuts()[i] * secondMultiplier;
                cuts[i] = totalCuts / totalMultiplier;
            }

            plan.addPattern(cuts, totalMultiplier);
        }

        return isMergeablePatternsFound;
    }

    private boolean isMergeable(int[] firstCuts, int firstMultiplier,
                                int[] secondCuts, int secondMultiplier) {
        int totalMultiplier = firstMultiplier + secondMultiplier;

        if (totalMultiplier == 0) {
            return false;
        }

        for (int i = 0; i < firstCuts.length; i++) {
            int totalCuts = firstCuts[i] * firstMultiplier + secondCuts[i] * secondMultiplier;
            if (totalCuts % totalMultiplier != 0) {
                return false;
            }
        }

        return true;
    }

    private void adaptiveMerge(Plan plan) {
        double toleranceRatio = calculateLengthToleranceRatio(plan);

        // pick two patterns randomly
        Pattern firstPattern = plan.pickPattern(rGen);
        if (firstPattern == null) {
            LOGGER.warn("Failed to randomly pick first pattern.");
            return;
        }

        int firstMultiplier = plan.removePattern(firstPattern);

        Pattern secondPattern = plan.pickPattern(rGen);
        if (secondPattern == null) {
            LOGGER.warn("Failed to randomly pick second pattern.");
            return;
        }

        int secondMultiplier = plan.removePattern(secondPattern);

        int multiplier = firstMultiplier + secondMultiplier;

        int[] demand = calculateDemand(plan, multiplier);
        int[] cuts = generator.generate(demand, toleranceRatio);
        if (cuts != null) {
            plan.addPattern(cuts, multiplier);
        }
    }

}
