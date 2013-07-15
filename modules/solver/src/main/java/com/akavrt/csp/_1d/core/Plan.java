package com.akavrt.csp._1d.core;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * User: akavrt
 * Date: 25.06.13
 * Time: 18:34
 */
public class Plan {
    private static final Logger LOGGER = LogManager.getLogger(Plan.class);
    private final Map<Pattern, Integer> patterns;
    private final Problem problem;
    private int cachedMaterialUsage;
    private int cachedTotalResidualDemand;
    private double cachedTrimRatio;
    private boolean useCachedHashCode;
    private int cachedHashCode;
    private int cachedResidualDemandLength;
    private int[] cachedResidualDemand;
    private int[] producedHolder;

    public Plan(Problem problem) {
        this.problem = problem;

        patterns = new TreeMap<Pattern, Integer>();

        producedHolder = new int[problem.size()];

        commit();
    }

    /**
     * <p>Copy constructor.</p>
     *
     * @param plan Plan to be copied.
     */
    public Plan(Plan plan) {
        this(plan.problem);

        for (Map.Entry<Pattern, Integer> each : plan.patterns.entrySet()) {
            Pattern pattern = new Pattern(each.getKey());
            int multiplier = each.getValue();

            patterns.put(pattern, multiplier);
        }
    }

    public int getStockLength() {
        return problem.getStockLength();
    }

    public Problem getProblem() {
        return problem;
    }

    public int size() {
        return patterns.size();
    }

    public int getSetups() {
        return patterns.size();
    }

    public boolean isFeasible() {
        return getTotalResidualDemand() == 0;
    }

    public void addPattern(int[] cuts) {
        addPattern(cuts, 1);
    }

    public void addPattern(int[] cuts, int multiplier) {
        if (cuts == null) {
            LOGGER.warn("Incompatible pattern encountered: cuts array is null");
            return;
        }

        if (cuts.length != problem.size()) {
            LOGGER.warn("Incompatible pattern encountered: cuts.length is {}, problem.size() is {}",
                        cuts.length, problem.size());
            return;
        }

        int length = 0;
        for (int i = 0; i < problem.size(); i++) {
            length += cuts[i] * problem.getOrder(i).getLength();
        }

        if (length == 0) {
            LOGGER.debug("Inactive pattern encountered.");
            return;
        }

        if (length > problem.getStockLength()) {
            LOGGER.warn("Infeasible pattern encountered with length {} exceeds stock length of {}.",
                        length, problem.getStockLength());
            return;
        }

        Pattern pattern = new Pattern(cuts);
        if (patterns.containsKey(pattern)) {
            multiplier += patterns.get(pattern);
        }

        patterns.put(pattern, multiplier);
        commit();
    }

    public int removePattern(Pattern pattern) {
        Integer multiplier = patterns.remove(pattern);
        commit();

        return multiplier == null ? 0 : multiplier;
    }

    private void commit() {
        useCachedHashCode = false;

        cachedMaterialUsage = -1;
        cachedTotalResidualDemand = -1;
        cachedTrimRatio = -1;
        cachedResidualDemandLength = -1;
        cachedResidualDemand = null;
    }

    public int getMultiplier(Pattern pattern) {
        Integer multiplier = patterns.get(pattern);

        return multiplier == null ? 0 : multiplier;
    }

    public Pattern pickPattern(Random random) {
        if (patterns.size() == 0) {
            return null;
        }

        int index = random.nextInt(patterns.size());

        return Iterables.get(patterns.keySet(), index);
    }

    public Set<Map.Entry<Pattern, Integer>> getPatterns() {
        return patterns.entrySet();
    }

    public int getMaterialUsage() {
        if (cachedMaterialUsage < 0) {
            int result = 0;
            for (Integer multiplier : patterns.values()) {
                result += multiplier;
            }

            cachedMaterialUsage = result;
        }

        return cachedMaterialUsage;
    }

    public int getTotalResidualDemand() {
        if (cachedTotalResidualDemand < 0) {
            int[] produced = getProducedDemand();

            int result = 0;
            for (int i = 0; i < problem.size(); i++) {
                int residualDemand = problem.getOrder(i).getDemand() - produced[i];
                result += residualDemand > 0 ? residualDemand : 0;
            }

            cachedTotalResidualDemand = result;
        }

        return cachedTotalResidualDemand;
    }

    public double getMaterialWasteRatio() {
        double useful = problem.getTotalOrderLength();
        double used = problem.getStockLength() * getMaterialUsage();

        return 1 - useful / used;
    }

    public double getTrimRatio() {
        if (cachedTrimRatio < 0) {
            int trim = 0;
            for (Map.Entry<Pattern, Integer> patternEntry : patterns.entrySet()) {
                int[] cuts = patternEntry.getKey().getCuts();
                int multiplier = patternEntry.getValue();
                int patternLength = 0;
                for (int i = 0; i < problem.size(); i++) {
                    patternLength += cuts[i] * problem.getOrder(i).getLength();
                }

                trim += (problem.getStockLength() - patternLength) * multiplier;
            }

            double used = problem.getStockLength() * getMaterialUsage();

            cachedTrimRatio = trim / used;
        }

        return cachedTrimRatio;
    }

    public double getPatternReductionRatio() {
        double result = 0;

        int size = getMaterialUsage();
        if (size > 1) {
            result = (getSetups() - 1) / (double) (size - 1);
        }

        return result;
    }

    public int[] getResidualDemand() {
        if (cachedResidualDemand == null) {
            int[] produced = getProducedDemand();

            int[] residualDemand = new int[problem.size()];
            for (int i = 0; i < problem.size(); i++) {
                int orderResidual = problem.getOrder(i).getDemand() - produced[i];
                residualDemand[i] = orderResidual > 0 ? orderResidual : 0;
            }

            cachedResidualDemand = residualDemand;
        }

        return cachedResidualDemand.clone();
    }

    public int getResidualDemandLength() {
        if (cachedResidualDemandLength < 0) {
            int[] produced = getProducedDemand();

            int residualLength = 0;
            for (int i = 0; i < problem.size(); i++) {
                int residualDemand = problem.getOrder(i).getDemand() - produced[i];
                if (residualDemand > 0) {
                    residualLength += residualDemand * problem.getOrder(i).getLength();
                }
            }

            cachedResidualDemandLength = residualLength;
        }

        return cachedResidualDemandLength;
    }

    private int[] getProducedDemand() {
        Arrays.fill(producedHolder, 0);
        for (Map.Entry<Pattern, Integer> each : patterns.entrySet()) {
            int[] cuts = each.getKey().getCuts();
            int multiplier = each.getValue();
            for (int i = 0; i < problem.size(); i++) {
                producedHolder[i] += cuts[i] * multiplier;
            }
        }

        return producedHolder;
    }

    @Override
    public int hashCode() {
        if (!useCachedHashCode) {
            useCachedHashCode = true;

            if (patterns.size() == 0) {
                cachedHashCode = 0;
            } else {
                int[] hashes = new int[patterns.size()];
                int i = 0;
                for (Map.Entry<Pattern, Integer> each : patterns.entrySet()) {
                    hashes[i++] = Objects.hashCode(each.getKey(), each.getValue());
                }

                cachedHashCode = Arrays.hashCode(hashes);
            }
        }

        return cachedHashCode;
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
        builder.append("\n  Residual demands: " + getTotalResidualDemand());
        builder.append("\n    Material usage: " + getMaterialUsage() + " stock pieces");
        builder.append("\n     Setups needed: " + getSetups());
        builder.append("\n        Trim ratio: " + String.format("%.2f%%",
                                                                100 * getMaterialWasteRatio()));

        return builder.toString();
    }

}
