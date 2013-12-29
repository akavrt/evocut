package com.akavrt.csp._1d.solver.pattern;

import com.akavrt.csp._1d.core.Problem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

/**
 * User: akavrt
 * Date: 29.06.13
 * Time: 16:13
 */
public class PierceGenerator implements PatternGenerator {
    private static final Logger LOGGER = LogManager.getLogger(PierceGenerator.class);
    private int stockLength;
    private int[] lengths;
    private int[] cuts;

    public PierceGenerator() {
    }

    public PierceGenerator(Problem problem) {
        initialize(problem);
    }

    @Override
    public void initialize(Problem problem) {
        stockLength = problem.getStockLength();

        cuts = new int[problem.size()];

        lengths = new int[problem.size()];
        for (int i = 0; i < problem.size(); i++) {
            lengths[i] = problem.getOrder(i).getWidth();
        }
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
            LOGGER.warn("Trying to generate pattern for zero demand.");
            return null;
        }

        return totalLength <= stockLength ? greedy(demand) : search(demand, allowedTrimRatio);
    }

    private int[] greedy(int[] demand) {
        return demand.clone();
    }

    private int[] search(int[] demand, double allowedTrimRatio) {
        int allowedTrim = (int) (allowedTrimRatio * stockLength);

        int[] pattern = null;
        Arrays.fill(cuts, 0);

        int j = -1;
        do {
            if (j >= 0) {
                cuts[j]--;
            }

            int unused = stockLength;
            int k = -1;
            for (int i = 0; i < cuts.length; i++) {
                if (i > j) {
                    int upperBound = unused / lengths[i];
                    cuts[i] = Math.min(upperBound, demand[i]);
                }

                unused -= cuts[i] * lengths[i];

                if (cuts[i] > 0) {
                    k = i;
                }
            }

            j = k;

            if (unused <= allowedTrim) {
                pattern = cuts.clone();
            }
        }
        while (j >= 0 && pattern == null);

        return pattern;
    }

    @Override
    public PatternGeneratorParameters getParameters() {
        return null;
    }

}
