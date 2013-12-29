package com.akavrt.csp._1d.solver.sequential;

import com.akavrt.csp._1d.core.Order;

public class MutableOrder {
    private final Order order;
    private int actualDemand;

    public MutableOrder(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }

    public int getActualDemand() {
        return actualDemand;
    }

    public void updateDemand(int produced) {
        this.actualDemand += produced;
    }

    public boolean isFulfilled() {
        return actualDemand >= order.getQuantity();
    }

    public int getUnfulfilledDemand() {
        return isFulfilled() ? 0 : (order.getQuantity() - actualDemand);
    }

}
