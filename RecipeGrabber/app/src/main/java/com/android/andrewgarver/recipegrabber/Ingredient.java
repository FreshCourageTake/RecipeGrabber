package com.android.andrewgarver.recipegrabber;

/**
 * Created by Andrew Garver on 12/9/2015.
 */
public class Ingredient {
    String name;
    String metric;
    int quantity;
    public Ingredient(String name, int quantity, String metric) {
        this.name = name;
        this.quantity = quantity;
        this.metric = metric;
    }

    public String getName() {
        return name;
    }

    public String getMetric() {
        return metric;
    }

    public int getQuantity() {
        return quantity;
    }
}
