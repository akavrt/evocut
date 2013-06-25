package com.akavrt.csp._1d.core;

import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * User: akavrt
 * Date: 25.06.13
 * Time: 17:23
 */
public class Problem {
    private static final Logger LOGGER = LogManager.getLogger(Problem.class);
    private final List<Order> orders;
    private final int stockLength;
    private final int totalOrderLength;

    public Problem(int stockLength, List<Order> orders) {
        this.stockLength = stockLength;

        this.orders = Lists.newArrayList();

        for (Order order : orders) {
            addOrder(order);
        }

        totalOrderLength = calculateTotalOrderLength();
    }

    public int getStockLength() {
        return stockLength;
    }

    public int size() {
        return orders.size();
    }

    public Order getOrder(int index) {
        return orders.get(index);
    }

    private void addOrder(Order order) {
        if (order == null || !order.isValid()) {
            LOGGER.warn("Incomplete order encountered.");
            return;
        }

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

    private int calculateTotalOrderLength() {
        int result = 0;
        for (Order order : orders) {
            result += order.getLength() * order.getDemand();
        }

        return result;
    }

    public int getTotalOrderLength() {
        return totalOrderLength;
    }

}
