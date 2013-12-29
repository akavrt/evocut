package com.akavrt.csp._1d.solver.pattern;

import com.akavrt.csp._1d.core.Problem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    private static final Logger LOGGER = LogManager.getLogger(UnconstrainedPatternGenerator.class);
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
            lengths[i] = problem.getOrder(i).getWidth();
        }

        currPattern = new int[lengths.length];
        bestPattern = new int[lengths.length];

        stockLength = problem.getStockLength();
    }

    @Override
    public int[] generate(int[] demand, double allowedTrimRatio) {
        if (lengths == null) {
            LOGGER.warn("Trying to use uninitialized pattern generator.");
            return null;
        }

        int totalLength = 0;
        for (int i = 0; i < lengths.length; i++) {
            totalLength += lengths[i] * demand[i];
        }

        if (totalLength == 0) {
            LOGGER.debug("Trying to generate pattern for zero demand.");
            return null;
        }

        return totalLength <= stockLength ? greedy(demand) : search(demand, allowedTrimRatio);
    }

    private int[] greedy(int[] demand) {
        // use greedy placement
        return demand.clone();
    }

    private int[] search(int[] demand, double allowedTrimRatio) {
        // use randomized generation procedure
        int totalItems = 0;
        for (int i = 0; i < lengths.length; i++) {
            totalItems += demand[i];
        }

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

        return bestPattern.clone();
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
