package com.akavrt.csp._1d.core;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * User: akavrt
 * Date: 25.06.13
 * Time: 17:23
 */
public class Problem {
    private final List<Order> orders;
    private final int stockLength;

    public Problem(int stockLength) {
        this.stockLength = stockLength;

        orders = Lists.newArrayList();
    }

    public int size() {
        return orders.size();
    }

    public int getStockLength() {
        return stockLength;
    }

    public void addOrder(Order order) {
        Order existing = null;
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getLength() == order.getLength()) {
                existing = orders.remove(i);
                break;
            }
        }

        // sum up demands for two orders with equal length
        if (existing != null) {
            order = new Order(order.getLength(), order.getDemand() + existing.getDemand());
        }

        orders.add(order);
    }

    public List<Order> getOrders() {
        return orders;
    }

    public Order getOrder(int index) {
        return orders.get(index);
    }

}
