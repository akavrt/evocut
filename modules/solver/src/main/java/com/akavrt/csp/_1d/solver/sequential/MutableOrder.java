package com.akavrt.csp._1d.solver.sequential;

import com.akavrt.csp._1d.core.Order;

public class MutableOrder {
    private final Order order;
    private int produced;

    public MutableOrder(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }

    public int getProduced() {
        return produced;
    }

    public void addProduced(int produced) {
        this.produced += produced;
    }

    public boolean isFulfilled() {
        return produced >= order.getDemand();
    }

    public int getUnfulfilledDemand() {
        return isFulfilled() ? 0 : order.getDemand() - produced;
    }

    public int getUnfulfilledLength() {
        return getUnfulfilledDemand() * order.getLength();
    }

}
