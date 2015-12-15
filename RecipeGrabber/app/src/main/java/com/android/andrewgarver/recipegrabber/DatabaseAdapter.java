package com.android.andrewgarver.recipegrabber;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;

/**
 *
 *
 *
 *
 * @author  Andrew Garver, Landon Jamieson, and Reed Atwood
 * @version 1.0
 * @since   12/10/2015
 */
public class DatabaseAdapter {

    /**
     * Debugging Tag to display LogCat messages for debugging
     */
    private static final String TAG = DatabaseAdapter.class.getSimpleName();

    private DatabaseHelper helper;

    /**
     *
     *
     * @param context The context of the current activity
     */
    public DatabaseAdapter(Context context) {
        helper = DatabaseHelper.getInstance(context);
    }

    public DatabaseHelper getHelper() {
        return helper;
    }

    /**
     * Add Ingredient
     *
     * @param quant A string representing the currently selected quantity
     * @param metric A string representing the currently selected unit or measurement
     * @param name A string representing the name of the ingredient
     * @return id A positive number representing the row of the table the ingredient was added to, or a negative number if insertion failed
     */
    public long addIngredient(String quant, String metric, String name) {

        /**
         * ContentValues stores data to be inserted and is then used to perform the insertion
         */
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.QUANTITY, quant);
        contentValues.put(DatabaseHelper.METRIC, metric);
        contentValues.put(DatabaseHelper.NAME, name);
        long id = db.insert(DatabaseHelper.TABLE_INGREDIENTS, null, contentValues);
        return id;
    }

    /**
     *
     *
     * @param name String representing the name of the ingredient to be deleted
     * @return
     */
    public boolean deleteIngredient(String name) {
        SQLiteDatabase db = helper.getWritableDatabase();
        return db.delete(DatabaseHelper.TABLE_INGREDIENTS, DatabaseHelper.NAME + "='" + name + "'", null) > 0;
    }

    /**
     *
     *
     * @param ingred An ingredient object to be deleted
     * @return
     */
    public boolean deleteIngredient(Ingredient ingred) {
        SQLiteDatabase db = helper.getWritableDatabase();
        return db.delete(DatabaseHelper.TABLE_INGREDIENTS, DatabaseHelper.NAME + "='" +
                ingred.getName() + "' and " + DatabaseHelper.METRIC + "='" + ingred.getMetric() +
                "'", null) > 0;
    }

    public boolean deletePlannedIngredient(Ingredient i) {
        SQLiteDatabase db = helper.getWritableDatabase();
        return db.delete(DatabaseHelper.TABLE_PLANNEDINGREDIENTS, DatabaseHelper.INGREDIENT_ID + "='" +
                i.getId() + "'", null) > 0;
    }

    /**
     * Add Recipe Name and Instructions
     *
     *
     *
     * @param recName Name of the recipe to be added
     * @param instructions String representing the instructions on how to make the recipe
     * @return id A positive number representing the row of the table the ingredient was added to, or a negative number if insertion failed
     */
    public long addRecipeInfo(String recName, String instructions) {

        /**
         * Initialize the needed variables and objects
         */
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues recContentValues = new ContentValues();

        /**
         * ContentValues stores data to be inserted and is then used to perform the insertion
         */
        recContentValues.put(DatabaseHelper.NAME, recName);
//        contentValues.put(DatabaseHelper.PIC, pic); TODO: Worry about the picture functionality later as stretch goal
        recContentValues.put(DatabaseHelper.INSTRUCTIONS, instructions);
        long id = db.insert(DatabaseHelper.TABLE_RECIPES, null, recContentValues);
        return id;
    }

    public long addPlannedIngredients(String name) {
        return addPlannedIngredients(getRecipeId(name));
    }

    public long addPlannedIngredients(ArrayList<Ingredient> ingreds) {
        long id = 0;
        for (Ingredient i : ingreds)
            id = addPlannedIngredient(i.getId(), i);
        return id;
    }

    public long addPlannedIngredients(long recId) {
        ArrayList<Ingredient> ingreds = getRecipeIngredientsVerbose(recId);
        long id = 0;
        for (Ingredient i : ingreds)
            id = addPlannedIngredient(i, recId);
        return id;
    }

    public long addPlannedIngredient(Ingredient i) {
        return addPlannedIngredient(i.getId(), i);
    }

    public long addPlannedIngredient(int ingId, Ingredient i) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues plannedIng = new ContentValues();
        plannedIng.put(DatabaseHelper.INGREDIENT_ID, ingId);
        plannedIng.put(DatabaseHelper.QUANTITY, i.getQuantity());
        plannedIng.put(DatabaseHelper.NUM_REQUIRED, i.getRequired());
        return db.insert(DatabaseHelper.TABLE_PLANNEDINGREDIENTS, null, plannedIng);
    }

    public long addPlannedIngredient(Ingredient i, long recId) {
        return addPlannedIngredient(getRecipeIngredientID(i.getName(), (int) recId), i);
    }

    /**
     * Add Ingredients that are part of a recipe
     *
     *
     *
     * @param quant A string representing the currently selected quantity
     * @param metric A string representing the currently selected unit or measurement
     * @param name A string representing the name of the ingredient
     * @param recipeId A long refering to the primary key of the desired recipe
     * @return id
     */
    public long addRecipeIngredient(String name, String quant, String metric, long recipeId) {

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues ingContentValues = new ContentValues();

        /**
         * ContentValues stores data to be inserted and is then used to perform the insertion
         */
        ingContentValues.put(DatabaseHelper.NAME, name);
        ingContentValues.put(DatabaseHelper.QUANTITY, quant);
        ingContentValues.put(DatabaseHelper.METRIC, metric);
        ingContentValues.put(DatabaseHelper.RECIPE_ID, recipeId);
        long id = db.insert(DatabaseHelper.TABLE_RECIPEINGREDIENTS, null, ingContentValues);
        return id;
    }

    /**
     * Get all the ingredients in the cupboard
     *
     * @return items An ArrayList of all ingredients in the planned ingredients table
     */
    public ArrayList<String> getAllIngredients() {

        SQLiteDatabase db = helper.getWritableDatabase();

        /**
         * db.query will return us this information as an array
         */
        String[] columns = {helper.PRIMARY_KEY, helper.QUANTITY, helper.METRIC, helper.NAME};

        /**
         * Query the ingredients table, return information formatted as specified by columns
         */
        Cursor cursor = db.query(helper.TABLE_INGREDIENTS, columns, null, null, null, null, null);
        ArrayList<String> items = new ArrayList<>();

        /**
         * While we still have new data to parse, create a string for the ingredient and add it to the returned ArrayList
         */
        while (cursor.moveToNext()) {
//            int cid = cursor.getInt(0);
            String quant = cursor.getString(1);
            String unit = cursor.getString(2);
            String name = cursor.getString(3);
            String item = name + " - " + quant + " " + unit;
            items.add(item);
        }

        return items;
    }

    /**
     * Get all the ingredients in the cupboard (for computation)
     *
     *
     * @return ingredients ArrayList of Ingredients with discrete quanitiy, name, and metric values
     */
    public ArrayList<Ingredient> getAllIngredientsVerbose() {

        /**
         * db.query will return us this information as an array
         */
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {helper.PRIMARY_KEY, helper.QUANTITY, helper.METRIC, helper.NAME};
        Cursor cursor = db.query(helper.TABLE_INGREDIENTS, columns, null, null, null, null, null);
        ArrayList<Ingredient> ingredients = new ArrayList<>();

        /**
         * While we still have new data to parse, create a string for the ingredient and add it to the returned ArrayList
         */
        while (cursor.moveToNext()) {
            int quant = cursor.getInt(1);
            String unit = cursor.getString(2);
            String name = cursor.getString(3);
            ingredients.add(new Ingredient(name, quant, unit));
        }

        return ingredients;
    }

    /**
     * Get all the ingredients in the planned meals
     *
     *
     *
     * @return ingredients ArrayList of the ingredients for all the planned recipes
     */
    public ArrayList<Ingredient> getPlannedIngredients() {
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        Ingredient i;
        SQLiteDatabase db = helper.getWritableDatabase();
        /**
         * Returns all ingredients
         */
        String [] columns = {helper.INGREDIENT_ID, helper.QUANTITY, helper.NUM_REQUIRED};
        Cursor cursor = db.query(helper.TABLE_PLANNEDINGREDIENTS, columns, null, null, null, null, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            i = getIngredientVerbose(id);
            i.setQuantity(cursor.getInt(1));
            i.setId(id);
            i.setRequired(cursor.getInt(2));
            ingredients.add(i);
        }

        return ingredients;
    }

    public ArrayList<Ingredient> getPlannedRecipeIngredients(int recId) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ArrayList<Ingredient> recIng = getRecipeIngredientsVerbose(recId);
        String [] columns = {helper.QUANTITY, helper.NUM_REQUIRED};
        Cursor c;
        for (Ingredient i : recIng) {
            c = db.query(helper.TABLE_PLANNEDINGREDIENTS, columns, helper.INGREDIENT_ID + "='" + i.getId() +
                    "'", null, null, null, null);
            if (c.moveToNext()) {
                i.setQuantity(c.getInt(0));
                i.setRequired(c.getInt(1));
            }
        }

        return recIng;
    }

    public void removePlannedRecipe(String name) {

        int recId = getRecipeId(name);
        ArrayList<Ingredient> forRec = getRecipeIngredientsVerbose(recId);
        ArrayList<Ingredient> plannedRecIng = getPlannedRecipeIngredients(recId);
        ArrayList<Ingredient> shoppingList = getAutoAddedShoppingList();
        int stdQuantity;
        int diff;
        for (Ingredient i : forRec) {
            stdQuantity = i.getQuantity();
            for (Ingredient planned : plannedRecIng)
                if(i.getName().equalsIgnoreCase(planned.getName()) && i.getMetric().equalsIgnoreCase(planned.getMetric())) {
                    i.setRequired(planned.getRequired() - i.getQuantity());
                    diff = planned.getQuantity() - i.getQuantity();
                    i.setQuantity(diff > 0 ? diff : 0);
                    deletePlannedIngredient(planned);
                    if (i.getRequired() > 0)
                        addPlannedIngredient(i);
                    break;
                }
            for (Ingredient onShopList : shoppingList) //TODO Not fully correct, might need to see if we have at least the required in cupboard before deleting from here?
                if (i.getName().equalsIgnoreCase(onShopList.getName()) && i.getMetric().equalsIgnoreCase(onShopList.getMetric())) {
                    deleteFromShoppingList(onShopList);
                    diff = onShopList.getQuantity() - stdQuantity;
                    if (diff > 0)
                        addToShoppingList(onShopList.getName(), Integer.toString(diff), onShopList.getMetric(), i.getRequired() > 0);
                    break;
                }
        }
    }
    /**
     * Get the id of a recipe
     *
     *
     *
     * @param recipeName Name of the recipe we want the id for
     * @return
     */
    public int getRecipeId(String recipeName) {

        /**
         * db.query will return us this information as an array
         */
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {helper.PRIMARY_KEY};
        Cursor cursor = db.query(helper.TABLE_RECIPES, columns, "NAME=?", new String[]{recipeName}, null, null, null, "1");

        /**
         * While we still have new data to parse, create a string for the ingredient and add it to the returned ArrayList
         */
        if (cursor.moveToNext()) {
            int cid = cursor.getInt(0);
            return cid;
        }

        return -1;
    }

    public int getRecipeIngredientID(String ingName, int recID) {

        /**
         * db.query will return us this information as an array
         */
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {helper.PRIMARY_KEY};
        Cursor cursor = db.query(helper.TABLE_RECIPEINGREDIENTS, columns, "NAME=? and RECIPE_ID=?", new String[]{ingName, Integer.toString(recID)}, null, null, null, "1");

        /**
         * While we still have new data to parse, create a string for the ingredient and add it to the returned ArrayList
         */
        if (cursor.moveToNext()) {
            int cid = cursor.getInt(0);
            return cid;
        }

        return -1;
    }

    /**
     *
     *
     * @return items Names of all the recipes in the cookbook
     */
    public ArrayList<String> getAllRecipes() {

        /**
         * db.query will return us this information as an array
         */
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {helper.PRIMARY_KEY, helper.NAME, helper.INSTRUCTIONS};
        Cursor cursor = db.query(helper.TABLE_RECIPES, columns, null, null, null, null, helper.NAME + " COLLATE NOCASE");
        ArrayList<String> items = new ArrayList<>();

        /**
         * While we still have new data to parse, create a string for the ingredient and add it to the returned ArrayList
         */
        while (cursor.moveToNext()) {
            int cid = cursor.getInt(0);
            String name = cursor.getString(1);
            String instructions = cursor.getString(2);
            items.add(name);
        }

        return items;
    }

    /**
     *
     *
     * @param name The name of the recipe we want to return
     * @return items the recipe to return
     */
    public String[] getRecipe(String name) {

        /**
         * db.query will return us this information as an array
         */
        Log.i(TAG, "getting recipe");
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] recipeColumns = {helper.PRIMARY_KEY, helper.NAME, helper.INSTRUCTIONS};
        Cursor cursor = db.query(helper.TABLE_RECIPES, recipeColumns, "NAME=?", new String[]{name}, null, null, null, "1");

        String[] items = new String[4];

        /**
         * While we still have new data to parse, create a string for the ingredient and add it to the returned ArrayList
         */
        while (cursor.moveToNext()) {
            int cid = cursor.getInt(0);
            String displayName = cursor.getString(1);
            String instructions = cursor.getString(2);
            items[0] = Integer.toString(cid);
            items[1] = displayName;
            items[2] = instructions;
        }
        return items;
    }

    public Ingredient getCupboardIngredient(String ingName, String ingMetric) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {helper.NAME, helper.QUANTITY, helper.METRIC};
        Cursor cursor = db.query(helper.TABLE_INGREDIENTS, columns, "NAME=? and METRIC=?", new String[]{ingName, ingMetric}, null, null, null);
        return cursor.moveToNext() ? new Ingredient(cursor.getString(0), cursor.getInt(1), cursor.getString(2)) : null;
    }

    public Ingredient getIngredientVerbose(int id) {

        /**
         * db.query will return us this information as an array
         */
        Log.i(TAG, "getting ingredient");
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] ingredientColumns = {helper.NAME, helper.QUANTITY, helper.METRIC};
        Cursor cursor = db.query(helper.TABLE_RECIPEINGREDIENTS, ingredientColumns, "ID=?", new String[]{Integer.toString(id)}, null, null, null, "1");

        /**
         * While we still have new data to parse, create a string for the ingredient and add it to the returned ArrayList
         */
        if (cursor.moveToNext())
            return new Ingredient(cursor.getString(0), cursor.getInt(1), cursor.getString(2));
        return new Ingredient("", 0 , "");
    }

    /**
     *
     *
     * @param name Name of the recipe to be deleted
     * @return returns true if delete was successfull and false otherwise
     */
    public boolean deleteRecipe(String name) {

        /**
         * Gets all the ingredients for the requested recipe
         */
        SQLiteDatabase db = helper.getWritableDatabase();
        ArrayList<Ingredient> recIngreds = getRecipeIngredientsVerbose(getRecipeId(name));

        /**
         * Deletes all the ingredients linked the specified recipe
         */
        for (Ingredient ingred : recIngreds)
            db.delete(DatabaseHelper.TABLE_RECIPEINGREDIENTS, DatabaseHelper.NAME + "='" + ingred.getName() + "'" , null);

        return db.delete(DatabaseHelper.TABLE_RECIPES, DatabaseHelper.NAME + "='" + name + "'", null) > 0;
    }

    /**
     *
     *
     * @param id Primary key of the recipe we have the verbose ingredients for
     * @return ingredients Array list of all ingredients in the recipe
     */
    public ArrayList<Ingredient> getRecipeIngredientsVerbose(long id) {

        SQLiteDatabase db = helper.getWritableDatabase();

        String[] columns = {helper.PRIMARY_KEY, helper.QUANTITY, helper.METRIC, helper.NAME};
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        Cursor cursor = db.query(helper.TABLE_RECIPEINGREDIENTS, columns, "RECIPE_ID=?", new String[]{String.valueOf(id)}, null, null, null, null);

        /**
         * While we still have new data to parse, create a string for the ingredient and add it to the returned ArrayList
         */
        while (cursor.moveToNext()) {
            int quant = cursor.getInt(1);
            String unit = cursor.getString(2);
            String name = cursor.getString(3);
            Ingredient i = new Ingredient(name, quant, unit);
            i.setId(cursor.getInt(0));
            ingredients.add(i);
        }

        return ingredients;
    }

    /**
     * Get the ingredients of a specific recipe
     *
     *
     * @param id Primary key of the recipe we want the ingredients from
     * @return ingredients String of the ingredients for the queried recipe
     */
    public String getRecipeIngredients(long id) {

        /**
         * db.query will return us this information as an array
         */
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] ingredientColumns = {helper.PRIMARY_KEY, helper.NAME, helper.QUANTITY, helper.METRIC, helper.RECIPE_ID};
        Cursor cursor = db.query(helper.TABLE_RECIPEINGREDIENTS, ingredientColumns, "RECIPE_ID=?", new String[]{Long.toString(id)}, null, null, null, null);
        String ingredients = "";
        Log.i(TAG, String.valueOf(cursor.getCount()));

        /**
         * While we still have new data to parse, create a string for the ingredient and add it to the returned ArrayList
         */
        while (cursor.moveToNext()) {
            ingredients += cursor.getString(2) + " " + cursor.getString(3) + " " + cursor.getString(1) + "\n";
            Log.i(TAG, ingredients);
        }

        return ingredients;
    }

    /**
     * Insert item(s) into shopping list database
     *
     *
     *
     * @param quant A string representing the currently selected quantity
     * @param unit A string representing the currently selected unit or measurement
     * @param name A string representing the name of the ingredient
     * @param auto true if the recipe was added from the program, false if added manually by the user
     * @return id
     */
    public long addToShoppingList(String name, String quant, String unit, boolean auto) {

        SQLiteDatabase db = helper.getWritableDatabase();
        int quantNum = Integer.parseInt(quant);
        ArrayList<Ingredient> shoppingList = getAllShoppingListItemsVerbose();

        /**
         * Check each ingredient in the the shopping list against the added ingredient to check for duplicates
         */
        for (Ingredient i : shoppingList) {

            /**
             * If the ingredient already exists and the measurement unit matches, proceed
             */
            if (i.getName().equalsIgnoreCase(name) && i.getMetric().equals(unit)) {

                /**
                 * Update the value instead of adding a new instance of the ingredient
                 */
                quantNum += i.getQuantity();
                Log.i(TAG, "same name: " + name + " Metric: " + unit);
                db.delete(helper.TABLE_SHOPPINGLIST, "NAME=? AND METRIC=?", new String[]{name, unit});
                db.delete(helper.TABLE_SHOPPINGLIST, "NAME=? AND METRIC=?", new String[]{name.toLowerCase(), unit});
            }
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.NAME, name);
        contentValues.put(DatabaseHelper.QUANTITY, String.valueOf(quantNum));
        contentValues.put(DatabaseHelper.METRIC, unit);

        /**
         * Add the recipe to the database
         */
        if (auto)
            contentValues.put(DatabaseHelper.MANUAL_ADD, 0);
        else
            contentValues.put(DatabaseHelper.MANUAL_ADD, 1);
        long id = db.insert(DatabaseHelper.TABLE_SHOPPINGLIST, null, contentValues);

        return id;
    }

    /**
     *
     *
     * @param name Name of the ingredient to be deleted from the shopping list
     * @return
     */
    public boolean deleteFromShoppingList(String name) {
        SQLiteDatabase db = helper.getWritableDatabase();
        return db.delete(DatabaseHelper.TABLE_SHOPPINGLIST, DatabaseHelper.NAME + "='" + name + "'", null) > 0;
    }

    /**
     *
     *
     * @param ingred Ingredient object of the ingredient to be deleted
     * @return
     */
    public boolean deleteFromShoppingList(Ingredient ingred) {
        SQLiteDatabase db = helper.getWritableDatabase();
        return db.delete(DatabaseHelper.TABLE_SHOPPINGLIST,
                DatabaseHelper.NAME + "='" + ingred.getName() + "' and " + DatabaseHelper.METRIC +
                        "= '" + ingred.getMetric() + "'" , null) > 0;
    }

    /**
     * Get all the recipes in the cookbook
     * @return items
     */
    public ArrayList<String> getAllShoppingListItems() {
        /**
         * db.query will return us this information as an array
         */
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {helper.PRIMARY_KEY, helper.NAME, helper.QUANTITY, helper.METRIC};
        Cursor cursor = db.query(helper.TABLE_SHOPPINGLIST, columns, null, null, null, null, helper.NAME + " COLLATE NOCASE");
        ArrayList<String> items = new ArrayList<>();

        /**
         * While we still have new data to parse, create a string for the ingredient and add it to the returned ArrayList
         */
        while (cursor.moveToNext()) {
            int cid = cursor.getInt(0);
            String name = cursor.getString(1);
            String quant = cursor.getString(2);
            String unit = cursor.getString(3);
            String item = name + " - " + quant + " " + unit;
            items.add(item);
        }

        return items;
    }

    /**
     * Get all the recipes in the cookbook (for computation)
     *
     * @return ingredients Get all the shopping list itesms with discrete values
     */
    public ArrayList<Ingredient> getAllShoppingListItemsVerbose() {
        /**
         * db.query will return us this information as an array
         */
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {helper.PRIMARY_KEY, helper.NAME, helper.QUANTITY, helper.METRIC};
        Cursor cursor = db.query(helper.TABLE_SHOPPINGLIST, columns, null, null, null, null, null);
        ArrayList<Ingredient> ingredients = new ArrayList<>();

        /**
         * While we still have new data to parse, create a string for the ingredient and add it to the returned ArrayList
         */
        while (cursor.moveToNext()) {
            int cid = cursor.getInt(0);
            String name = cursor.getString(1);
            int quant = cursor.getInt(2);
            String unit = cursor.getString(3);
            ingredients.add(new Ingredient(name, quant, unit));
        }

        return ingredients;
    }

    /**
     * Grab Ingredients that were added Automatically
     */
    public ArrayList<Ingredient> getAutoAddedShoppingList() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String [] colums = {helper.NAME, helper.QUANTITY, helper.METRIC};
        Cursor cursor = db.query(helper.TABLE_SHOPPINGLIST, colums, helper.MANUAL_ADD + "=?",
                new String[]{"0"}, null, null, null, null);
        ArrayList<Ingredient> results = new ArrayList<>();
        while (cursor.moveToNext())
            results.add(new Ingredient(cursor.getString(0), cursor.getInt(1), cursor.getString(2)));

        return results;
    }

    /**
     * DatabaseHelper allows for better cohesion
     *
     * <p>DatabaseHelper contains the global constants for use when
     * querying the database.  This ensures that correct syntax and spelling is used
     * throughout the program.  DatabaseHelper also provides the means for refreshing and
     * updating the tables in the database.</p>
     */
    public static class DatabaseHelper extends SQLiteOpenHelper {

        /**
         * Database global constants
         */
        public static final String DATABASE_NAME = "recipe_grabber";
        public static final String TABLE_RECIPES = "recipes";
        public static final String TABLE_RECIPEINGREDIENTS = "recipeIngredients";
        public static final String TABLE_PLANNEDINGREDIENTS = "plannedIngredients";
        public static final String TABLE_INGREDIENTS = "ingredients";
        public static final String TABLE_SHOPPINGLIST = "shoppingList";
        public static final String PRIMARY_KEY = "ID";
        public static final String NAME = "NAME";
        public static final String PIC = "PIC";
        public static final String INSTRUCTIONS = "INSTRUCTIONS";
        public static final String QUANTITY = "QUANTITY";
        public static final String METRIC = "METRIC";
        public static final String INGREDIENTS = "INGREDIENTS";
        public static final String INGREDIENT_ID = "INGREDIENT_ID";
        public static final String RECIPE_ID = "RECIPE_ID";
        public static final String ITEM = "item";
        public static final String MANUAL_ADD = "MANUAL_ADD";
        public static final String NUM_REQUIRED = "NUM_REQUIRED";
        public static final int DATABASE_VERSION = 52;

        private static DatabaseHelper instance = null;

        /**
         * Singleton to prevent an error see DatabaseHelper constructor for more details
         */
        public static DatabaseHelper getInstance(Context context){
            if (instance == null)
                instance = new DatabaseHelper(context.getApplicationContext());
            return instance;
        }

        /**
         * Had to make this a singleton to fix a warning/notice about SQLite connection object being
         * leaked for the database. This was the particular fix for this on stack overflow
         */
        private DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        /**
         *
         *
         *
         * @param db the Database object referring to the database we want created
         */
        public void onCreate(SQLiteDatabase db) {
            try {

                /**
                 * Creates the recipes table
                 */
                Log.i("thing", "Inside onCreate");
                db.execSQL("CREATE TABLE recipes (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "NAME VARCHAR(255), " +
//                        "PIC VARCHAR(255), " + TODO: stretch goal
                        "INSTRUCTIONS VARCHAR(255));");

                /**
                 * Creates the ingredients table
                 */
                db.execSQL("CREATE TABLE ingredients (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "NAME VARCHAR(255), " +
                        "QUANTITY INTEGER, " +
                        "METRIC VARCHAR(255));");

                /**
                 * Creates the recipeIngredients table
                 */
                db.execSQL("CREATE TABLE recipeIngredients (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "NAME VARCHAR(255), " +
                        "QUANTITY INTEGER, " +
                        "METRIC VARCHAR(255), " +
                        "RECIPE_ID INTEGER, " +
                        "FOREIGN KEY(RECIPE_ID) REFERENCES recipes(ID));");

                /**
                 * Creates the shoppingList table
                 */
                db.execSQL("CREATE TABLE shoppingList (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "NAME VARCHAR(255), " +
                        "QUANTITY INTEGER, " +
                        "METRIC VARCHAR(255), " +
                        "MANUAL_ADD INTEGER);");

                /**
                 * Creates the plannedRecipeIngredients table
                 */
                db.execSQL("CREATE TABLE plannedIngredients (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "INGREDIENT_ID INTEGER, " +
                        "QUANTITY INTEGER, " +
                        "NUM_REQUIRED INTEGER, " +
                        "FOREIGN KEY(INGREDIENT_ID) REFERENCES recipeIngredients(ID));");

                Log.i("thing", "Table creation successful");
            } catch (SQLException e) {

                /**
                 * If you actually read this, let me know and I'll give you a pat on the back
                 */
                Log.e(TAG, "Failure to create database", e);
            }
        }

        /**
         *
         *
         *
         * @param db The database to be upgraded
         * @param oldVersion The old database version number
         * @param newVersion The new database version number
         */
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                /**
                 *
                 */
                db.execSQL("DROP TABLE IF EXISTS recipes");
                db.execSQL("DROP TABLE IF EXISTS ingredients");
                db.execSQL("DROP TABLE IF EXISTS recipeIngredients");
                db.execSQL("DROP TABLE IF EXISTS shoppingList");
                db.execSQL("DROP TABLE IF EXISTS plannedIngredients");
                onCreate(db);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
