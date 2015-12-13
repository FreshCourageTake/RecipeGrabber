package com.android.andrewgarver.recipegrabber;

import java.io.Serializable;

/**
 * Simple class for the Ingredients
 * <p>
 * Contains a non-default constructor and getters for name, quantity, and metric. It is serializable
 * to allow it to be put as an extra into an intent.
 * </p>
 * @author  Andrew Garver, Landon Jamieson, and Reed Atwood
 * @version 1.0
 * @since   12/10/2015
 */
public class Ingredient implements Serializable {

    /**
     * Debugging Tag to display LogCat messages for debugging
     */
    private static final String TAG = Ingredient.class.getSimpleName();

    /**
     * Set up the types of ingredients
     */
    private String name;
    private String metric;
    private int quantity;

    /**
     * Non-Default constructor that has a name, quantity and metric units
     *
     * @param name
     * @param quantity
     * @param metric
     */
    public Ingredient (String name, int quantity, String metric) {
        this.name = name;
        this.quantity = quantity;
        this.metric = metric;
    }

    /**
     * Get the name
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Get metric string
     *
     * @return metric
     */
    public String getMetric() {
        return metric;
    }

    /**
     * Get the quantity
     *
     * @return quantity
     */
    public int getQuantity() {
        return quantity;
    }
}