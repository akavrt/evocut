package com.akavrt.csp._1d.tester;

import com.akavrt.csp._1d.core.Order;
import com.akavrt.csp._1d.core.Problem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * User: akavrt
 * Date: 29.06.13
 * Time: 20:04
 */
public class Enumerator {
    private final static Logger LOGGER = LogManager.getLogger(Enumerator.class);
    private final int stockLength;
    private final int[] lengths;
    private final int[] demands;
    private final int[] cuts;
    private final int minOrderLength;
    private int effectiveCounter;
    private int feasibleCounter;

    public Enumerator(Problem problem) {
        stockLength = problem.getStockLength();

        List<Order> orders = new ArrayList<Order>();
        for (int i = 0; i < problem.size(); i++) {
            orders.add(problem.getOrder(i));
        }

        // this sorting is needed when only effective patters are being enumerated
        Collections.sort(orders, new Comparator<Order>() {
            @Override
            public int compare(Order lhs, Order rhs) {
                return lhs.getLength() == rhs.getLength()
                        ? 0
                        : (lhs.getLength() > rhs.getLength() ? -1 : 1);
            }
        });

        lengths = new int[orders.size()];
        demands = new int[orders.size()];
        for (int i = 0; i < orders.size(); i++) {
            lengths[i] = orders.get(i).getLength();
            demands[i] = orders.get(i).getDemand();
        }

        minOrderLength = lengths[lengths.length - 1];

        cuts = new int[problem.size()];
    }

    /**
     * <p>Pierce used term 'dominating processes' to denote the set of effective patterns.</p>
     *
     * @return number of effective patterns.
     */
    public int getEffectivePatternsCount() {
        return effectiveCounter;
    }

    /**
     * <p>Pierce used term 'exhaustive processes' to denote the set of feasible patterns.</p>
     *
     * @return number of feasible patterns.
     */
    public int getFeasiblePatternsCount() {
        return feasibleCounter;
    }

    /**
     * <p>Enumerates patterns in sequential manner using algorithm proposed by Pierce (1964).</p>
     */
    public void enumerate() {
        effectiveCounter = 0;
        feasibleCounter = 0;

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
                    cuts[i] = Math.min(upperBound, demands[i]);
                }

                unused -= cuts[i] * lengths[i];

                if (cuts[i] > 0) {
                    k = i;
                }
            }

            j = k;

            if (unused < stockLength) {
                feasibleCounter++;

                if (LOGGER.isDebugEnabled()) {
                    debugPattern(unused, unused < minOrderLength);
                }

                if (unused < minOrderLength) {
                    effectiveCounter++;
                }
            }
        }
        while (j >= 0);
    }

    private void debugPattern(int unused, boolean isEffective) {
        String ratio = String.format("%.4f", 1 - unused / (double) stockLength);
        LOGGER.debug("#{}  {}  {}  {}", feasibleCounter, ratio,
                     isEffective ? "EFFECTIVE" : "FEASIBLE ", formatPattern(cuts));
    }

    private String formatPattern(int[] pattern) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < pattern.length; i++) {
            sb.append(" " + pattern[i]);
        }
        sb.append(" ]");

        return sb.toString();
    }

}
