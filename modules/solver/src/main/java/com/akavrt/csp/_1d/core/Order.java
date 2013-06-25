package com.akavrt.csp._1d.core;

/**
 * User: akavrt
 * Date: 25.06.13
 * Time: 16:56
 */
public class Order {
    private int length;
    private int demand;

    public Order(int length, int demand) {
        this.length = length;
        this.demand = demand;
    }

    public int getLength() {
        return length;
    }

    public int getDemand() {
        return demand;
    }
}
