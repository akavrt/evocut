package com.akavrt.csp._1d.solver.evo.operators;

import com.akavrt.csp._1d.core.Plan;
import com.akavrt.csp._1d.core.Problem;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * <p>Calculates upper bound for residual problem corresponding to the specific cutting plan.</p>
 */
public class UpperBoundHelper {


    public int compute(Plan plan) {
        if (plan.isFeasible()) {
            return 0;
        }

        Problem problem = plan.getProblem();
        int[] demand = plan.getResidualDemand();
        List<Holder> orders = Lists.newArrayList();
        for (int i = 0; i < demand.length; i++) {
            if (demand[i] > 0) {
                int length = problem.getOrder(i).getLength();
                orders.add(new Holder(length, demand[i]));
            }
        }

        // sort orders in decreasing order of lengths
        Collections.sort(orders, new Comparator<Holder>() {
            @Override
            public int compare(Holder lhs, Holder rhs) {
                int lhsLength = lhs.getLength();
                int rhsLength = rhs.getLength();

                return lhsLength == rhsLength ? 0 : (lhsLength > rhsLength ? -1 : 0);
            }
        });

        return ffdBound(problem.getStockLength(), orders);
    }

    private int ffdBound(int stockLength, List<Holder> orders) {
        List<Integer> stock = Lists.newArrayList();
        while (orders.size() > 0) {
            int orderLength = orders.get(0).getLength();
            int stockIndex = -1;
            for (int i = 0; i < stock.size(); i++) {
                if (orderLength <= stock.get(i)) {
                    // sufficiently large stock piece found
                    stockIndex = i;
                    break;
                }
            }

            if (stockIndex >= 0) {
                int usedStockLength = stock.get(stockIndex);
                stock.set(stockIndex, usedStockLength - orderLength);
            } else {
                // start new stock piece
                stock.add(stockLength - orderLength);
            }

            // adjust demand
            int orderDemand = orders.get(0).getDemand();
            if (orderDemand > 1) {
                orders.get(0).setDemand(orderDemand - 1);
            } else {
                // remove fulfilled order
                orders.remove(0);
            }
        }

        return stock.size();
    }

    private static class Holder {
        private final int length;
        private int demand;

        public Holder(int length, int demand) {
            this.length = length;
            this.demand = demand;
        }

        public int getLength() {
            return length;
        }

        public int getDemand() {
            return demand;
        }

        public void setDemand(int demand) {
            this.demand = demand;
        }
    }

}
