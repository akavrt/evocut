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

    public int getLowerBound() {
        return (int) Math.ceil(totalOrderLength / (double) stockLength);
    }

    @Override
    public String toString() {
        int maxLength = 0;
        int maxDemand = 0;
        for (Order order : orders) {
            if (maxLength == 0 || maxLength < order.getLength()) {
                maxLength = order.getLength();
            }

            if (maxDemand == 0 || maxDemand < order.getDemand()) {
                maxDemand = order.getDemand();
            }
        }

        int indexDigits = (int) Math.log10(orders.size()) + 1;
        int lengthDigits = (int) Math.log10(maxLength) + 1;
        int demandDigits = (int) Math.log10(maxDemand) + 1;

        String format = "\n  #%" + indexDigits + "d:  %" + lengthDigits + "d  X  %" +
                demandDigits + "d";

        StringBuilder builder = new StringBuilder();
        builder.append("\nPROBLEM:  m = " + orders.size() + "  L = " + stockLength + "  LB = " +
                               getLowerBound());
        builder.append("\n  ORDERS, (l_i  x  d_i):");
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            builder.append(String.format(format, i + 1, order.getLength(), order.getDemand()));
        }

        return builder.toString();
    }

}
