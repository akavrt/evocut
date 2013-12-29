package com.akavrt.csp._1d.core;

/**
 * User: akavrt
 * Date: 25.06.13
 * Time: 16:56
 */
public class Order {
    private int width;
    private int quantity;

    public Order(int width, int quantity) {
        this.width = width;
        this.quantity = quantity;
    }

    public int getWidth() {
        return width;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isValid() {
        return width > 0 && quantity > 0;
    }

}
