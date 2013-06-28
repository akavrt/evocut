package com.akavrt.csp._1d.core;

import com.google.common.base.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

/**
 * User: akavrt
 * Date: 25.06.13
 * Time: 18:34
 */
public class Plan {
    private static final Logger LOGGER = LogManager.getLogger(Plan.class);
    private final Map<Pattern, Integer> patterns;
    private final Problem problem;

    public Plan(Problem problem) {
        this.problem = problem;

        patterns = new TreeMap<Pattern, Integer>();
    }

    public void addPattern(int[] cuts) {
        addPattern(cuts, 1);
    }

    public void addPattern(int[] cuts, int multiplier) {
        if (cuts == null || cuts.length != problem.size()) {
            LOGGER.warn("Incompatible pattern encountered.");
            return;
        }

        int length = 0;
        for (int i = 0; i < problem.size(); i++) {
            length += cuts[i] * problem.getOrder(i).getLength();
        }

        if (length == 0 || length > problem.getStockLength()) {
            LOGGER.warn("Inactive or infeasible pattern encountered.");
            return;
        }

        Pattern pattern = new Pattern(cuts);
        if (patterns.containsKey(pattern)) {
            multiplier += patterns.get(pattern);
        }

        patterns.put(pattern, multiplier);
    }

    public int getSetups() {
        return patterns.size();
    }

    public int size() {
        int result = 0;
        for (Integer multiplier : patterns.values()) {
            result += multiplier;
        }

        return result;
    }

    public int getResidualDemand() {
        int[] produced = new int[problem.size()];
        for (Map.Entry<Pattern, Integer> patternEntry : patterns.entrySet()) {
            int[] cuts = patternEntry.getKey().getCuts();
            int multiplier = patternEntry.getValue();
            for (int i = 0; i < problem.size(); i++) {
                produced[i] += cuts[i] * multiplier;
            }
        }

        int result = 0;
        for (int i = 0; i < problem.size(); i++) {
            if (problem.getOrder(i).getDemand() > produced[i]) {
                result += problem.getOrder(i).getDemand() - produced[i];
            }
        }

        return result;
    }

    public boolean isFeasible() {
        return getResidualDemand() == 0;
    }

    public double getMaterialWasteRatio() {
        double useful = problem.getTotalOrderLength();
        double used = problem.getStockLength() * size();

        return 1 - useful / used;
    }

    public double getPatternReductionRatio() {
        double ratio = 0;

        int size = size();
        if (size > 1) {
            ratio = (getSetups() - 1) / (double) (size - 1);
        }

        return ratio;
    }

    @Override
    public int hashCode() {
        if (patterns.size() == 0) {
            return 0;
        }

        int[] hashes = new int[patterns.size()];
        int i = 0;
        for (Map.Entry<Pattern, Integer> each : patterns.entrySet()) {
            hashes[i++] = Objects.hashCode(each.getKey(), each.getValue());
        }

        return Arrays.hashCode(hashes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Plan)) {
            return false;
        }

        Plan rhs = (Plan) o;

        return hashCode() == rhs.hashCode();
    }

    @Override
    public String toString() {
        int maxCuts = 0;
        int maxMult = 0;
        for (Map.Entry<Pattern, Integer> each : patterns.entrySet()) {
            int multiplier = each.getValue();
            if (maxMult == 0 || maxMult < multiplier) {
                maxMult = multiplier;
            }

            int[] cuts = each.getKey().getCuts();
            for (int i = 0; i < cuts.length; i++) {
                if (maxCuts == 0 || maxCuts < cuts[i]) {
                    maxCuts = cuts[i];
                }
            }
        }

        int indexDigits = (int) Math.log10(patterns.size()) + 1;
        int cutsDigits = (int) Math.log10(maxCuts) + 1;
        int multDigits = (int) Math.log10(maxMult) + 1;

        String indexFormat = "\n    #%" + indexDigits + "d:  [";
        String cutsFormat = " %" + cutsDigits + "d";
        String multFormat = " ]  X  %" + multDigits + "d";

        StringBuilder builder = new StringBuilder();
        builder.append("SOLUTION: ");
        builder.append(isFeasible() ? "feasible" : "infeasible");
        int i = 0;
        for (Map.Entry<Pattern, Integer> each : patterns.entrySet()) {
            builder.append(String.format(indexFormat, ++i));

            int[] cuts = each.getKey().getCuts();
            for (int j = 0; j < cuts.length; j++) {
                builder.append(String.format(cutsFormat, cuts[j]));
            }

            builder.append(String.format(multFormat, each.getValue()));
        }

        builder.append("\nANALYSIS: ");
        builder.append("\n  Residual demands: " + getResidualDemand());
        builder.append("\n    Material usage: " + size() + " stock pieces");
        builder.append("\n     Setups needed: " + getSetups());
        builder.append("\n        Trim ratio: " + String.format("%.2f%%",
                                                                  100 * getMaterialWasteRatio()));

        return builder.toString();
    }

}
