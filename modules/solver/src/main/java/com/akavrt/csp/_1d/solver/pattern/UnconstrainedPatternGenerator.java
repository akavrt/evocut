package com.akavrt.csp._1d.solver.pattern;

import com.akavrt.csp._1d.core.Problem;

import java.util.Arrays;
import java.util.Random;

/**
 * <p>Modification of the pattern generation procedure proposed by Vahrenkamp in this
 * <a href="http://dx.doi.org/10.1016/0377-2217(95)00198-0">paper</a>. Constraint on the maximum
 * number of cuts allowed within one pattern can be imposed.</p>
 *
 * <p>This implementation is not thread safe.</p>
 *
 * @author Victor Balabanov <akavrt@gmail.com>
 */
public class UnconstrainedPatternGenerator implements PatternGenerator {
    private final Random rGen;
    private int [] lengths;
    private int stockLength;
    private PatternGeneratorParameters params;
    private int[] currPattern;
    private int[] bestPattern;
    private int currTrim;

    public UnconstrainedPatternGenerator() {
        this(null, new PatternGeneratorParameters());
    }

    public UnconstrainedPatternGenerator(PatternGeneratorParameters params) {
        this(null, params);
    }

    public UnconstrainedPatternGenerator(Problem problem) {
        this(problem, new PatternGeneratorParameters());
    }

    public UnconstrainedPatternGenerator(Problem problem, PatternGeneratorParameters params) {
        if (problem != null) {
            initialize(problem);
        }

        this.params = params;
        rGen = new Random();
    }

    @Override
    public PatternGeneratorParameters getParameters() {
        return params;
    }

    @Override
    public void initialize(Problem problem) {
        lengths = new int[problem.size()];
        for (int i = 0; i < problem.size(); i++) {
            lengths[i] = problem.getOrder(i).getLength();
        }

        currPattern = new int[lengths.length];
        bestPattern = new int[lengths.length];

        stockLength = problem.getStockLength();
    }

    @Override
    public int[] generate(int[] demand, double allowedTrimRatio) {
        if (lengths == null) {
            return null;
        }

        int totalItems = 0;
        int totalWidth = 0;
        int minUnfulfilledLength = 0;
        for (int i = 0; i < lengths.length; i++) {
            totalItems += demand[i];
            totalWidth += lengths[i] * demand[i];

            if ((minUnfulfilledLength == 0 || lengths[i] < minUnfulfilledLength) && demand[i] > 0) {
                minUnfulfilledLength = lengths[i];
            }
        }

        if (minUnfulfilledLength == 0 || totalItems == 0) {
            return null;
        }

        // check whether simple greedy placement is possible
        if (totalWidth <= stockLength) {
            // use greedy placement
            greedyPlacement(demand);
        } else {
            // use randomized generation procedure
            int trialCounter = 0;
            int bestTrim = stockLength;
            int allowedTrim = (int) (stockLength * allowedTrimRatio);

            do {
                trial(demand, totalItems);

                // if new pattern is better than the current best,
                // replace latter one with new pattern
                if (getCurrentTrim() < bestTrim) {
                    bestTrim = getCurrentTrim();

                    int[] generatedPattern = getCurrentPattern();
                    System.arraycopy(generatedPattern, 0, bestPattern, 0, bestPattern.length);
                }

                trialCounter++;
            }
            while (trialCounter < params.getGenerationTrialsLimit() && bestTrim > allowedTrim);
        }

        return bestPattern.clone();
    }

    private void greedyPlacement(int[] demand) {
        bestPattern = demand.clone();
    }

    private void trial(int[] demand, int totalItems) {
        // reuse pattern, no need to create a new one
        int[] trialPattern = getCurrentPattern();

        // reset pattern
        Arrays.fill(trialPattern, 0);

        int addedItems = 0;
        int trialUnusedWidth = stockLength;
        boolean cutCanBeMade = true;

        // generate pattern
        do {
            int index = rGen.nextInt(lengths.length);
            if (lengths[index] <= trialUnusedWidth && demand[index] - trialPattern[index] > 0) {
                trialPattern[index]++;
                trialUnusedWidth -= lengths[index];
                addedItems++;

                // pattern was changed,
                // check if any unfulfilled order can be cut from width remained
                cutCanBeMade = false;
                for (int i = 0; i < lengths.length; i++) {
                    if (demand[i] - trialPattern[i] > 0 && lengths[i] <= trialUnusedWidth) {
                        cutCanBeMade = true;
                        break;
                    }
                }
            }

        }
        while (cutCanBeMade && totalItems - addedItems > 0);

        // no need to calculate trim twice
        // we can use this value when evaluation pattern quality
        setCurrentTrim(trialUnusedWidth);
        setCurrentPattern(trialPattern);
    }

    private int getCurrentTrim() {
        return currTrim;
    }

    private void setCurrentTrim(int unusedWidth) {
        this.currTrim = unusedWidth;
    }

    private int[] getCurrentPattern() {
        return currPattern;
    }

    private void setCurrentPattern(int[] pattern) {
        this.currPattern = pattern;
    }
}
