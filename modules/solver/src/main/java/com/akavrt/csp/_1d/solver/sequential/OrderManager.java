package com.akavrt.csp._1d.solver.sequential;

import com.akavrt.csp._1d.core.Order;
import com.akavrt.csp._1d.core.Problem;
import com.google.common.collect.Lists;

import java.util.List;

public class OrderManager {
    private final List<MutableOrder> orders;
    private final Problem problem;

    public OrderManager(Problem problem) {
        this.problem = problem;

        orders = Lists.newArrayList();

        for (int i = 0; i < problem.size(); i++) {
            Order immutable = problem.getOrder(i);
            MutableOrder mutable = new MutableOrder(immutable);
            orders.add(mutable);
        }
    }

    public int getMaxUnfulfilledDemand() {
        int maxDemand = 0;
        for (MutableOrder order : orders) {
            if (maxDemand == 0 || order.getUnfulfilledDemand() > maxDemand) {
                maxDemand = order.getUnfulfilledDemand();
            }
        }

        return maxDemand;
    }

    public int getPatternLength(int[] pattern) {
        if (pattern == null) {
            return 0;
        }

        int patternLength = 0;
        for (int i = 0; i < problem.size(); i++) {
            patternLength += pattern[i] * problem.getOrder(i).getWidth();
        }

        return patternLength;
    }

    public double getPatternTrimRatio(int[] pattern) {
        if (pattern == null) {
            return 0;
        }

        return 1 - getPatternLength(pattern) / (double) problem.getStockLength();
    }

    public boolean isOrdersFulfilled() {
        boolean isFulfilled = true;
        for (int i = 0; i < orders.size() && isFulfilled; i++) {
            isFulfilled = orders.get(i).isFulfilled();
        }

        return isFulfilled;
    }

    public int[] calculateDemand(int multiplier) {
        if (multiplier == 0) {
            return null;
        }

        int[] demand = new int[orders.size()];
        for (int i = 0; i < orders.size(); i++) {
            MutableOrder order = orders.get(i);
            if (!order.isFulfilled()) {
                demand[i] = order.getUnfulfilledDemand() / multiplier;

                // rounding multiplier up is allowed only if getMaterialUsage of the group equals to one
                // this rule is used to implicitly control overproduction
                if (multiplier == 1 && demand[i] == 0) {
                    demand[i] = 1;
                }
            }
        }

        return demand;
    }

    public void updateDemand(int[] pattern, int multiplier) {
        if (pattern == null) {
            return;
        }

        for (int i = 0; i < orders.size(); i++) {
            orders.get(i).updateDemand(pattern[i] * multiplier);
        }
    }

}
