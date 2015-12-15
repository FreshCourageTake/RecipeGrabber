package com.android.andrewgarver.recipegrabber;

/**
 * Simple class for the Ingredients
 * <p>
 * Contains a non-default constructor and getters for name, quantity, and metric.
 * </p>
 * @author  Andrew Garver, Landon Jamieson, and Reed Atwood
 * @version 1.0
 * @since   12/10/2015
 */
public class Ingredient {

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
    private int id;
    private int required;

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

    /**
     * Get the quantity as a string
     *
     * @return quantity
     */
    public String getQuantityString() {
        return Integer.toString(quantity);
    }

    /**
     * set the quantity
     *
     * @param newQuantity How many of this ingredient by metric
     */
    public void setQuantity(int newQuantity) {
        if (newQuantity > 0)
            quantity = newQuantity;
    }
    /**
     * Get the id
     *
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * set the
     *
     * @param newId the id of this ingredient
     */
    public void setId(int newId) {
        if (newId >= 0)
            id = newId;
    }

    /**
     * Get the number required
     *
     * @return required
     */
    public int getRequired() {
        return required;
    }

    /**
     * set the
     *
     * @param num the number required by all recipes planned
     */
    public void setRequired(int num) {
        if (num >= 0)
            required = num;
    }
}