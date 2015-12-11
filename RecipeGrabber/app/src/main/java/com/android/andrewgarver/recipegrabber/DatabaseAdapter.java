package com.android.andrewgarver.recipegrabber;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew Garver on 11/19/2015.
 */
public class DatabaseAdapter {
    private static final String TAG = DatabaseAdapter.class.getSimpleName();

    DatabaseHelper helper;

    public DatabaseAdapter(Context context) {
        helper = new DatabaseHelper(context);
    }

    /**
     * Created by Andrew Garver on 11/19/2015.
     * <p/>
     * Add Ingredient
     */
    public long addIngredient(String quant, String metric, String name) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.QUANTITY, quant);
        contentValues.put(DatabaseHelper.METRIC, metric);
        contentValues.put(DatabaseHelper.NAME, name);
        long id = db.insert(DatabaseHelper.TABLE_INGREDIENTS, null, contentValues);
        return id;
    }

    public boolean deleteIngredient(String name) {
        SQLiteDatabase db = helper.getWritableDatabase();
        return db.delete(DatabaseHelper.TABLE_INGREDIENTS, DatabaseHelper.NAME + "='" + name + "'", null) > 0;
    }

    /**
     * Created by Andrew Garver on 11/19/2015.
     * <p />
     * Add Recipe Name and Instructions
     */
    public long addRecipeInfo(String recName, String instructions) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues recContentValues = new ContentValues();

        recContentValues.put(DatabaseHelper.NAME, recName);
//        contentValues.put(DatabaseHelper.PIC, pic); TODO: Worry about the picture functionality later as stretch goal
        recContentValues.put(DatabaseHelper.INSTRUCTIONS, instructions);
        long id = db.insert(DatabaseHelper.TABLE_RECIPES, null, recContentValues);
        return id;
    }

    /**
     * Created by Andrew Garver on 11/19/2015.
     * <p/>
     * Add Ingredients that are part of a recipe
     */
    public long addRecipeIngredients(String name, String quant, String metric, long recipeId) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues ingContentValues = new ContentValues();

        ingContentValues.put(DatabaseHelper.NAME, name);
        ingContentValues.put(DatabaseHelper.QUANTITY, quant);
        ingContentValues.put(DatabaseHelper.METRIC, metric);
        ingContentValues.put(DatabaseHelper.RECIPE_ID, recipeId);
        long id = db.insert(DatabaseHelper.TABLE_RECIPEINGREDIENTS, null, ingContentValues);
        return id;
    }

    /**
     * Created by Andrew Garver on 11/19/2015.
     * <p/>
     * Get all the ingredients in the cupboard
     */
    public ArrayList<String> getAllIngredients() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {helper.PRIMARY_KEY, helper.QUANTITY, helper.METRIC, helper.NAME};
        Cursor cursor = db.query(helper.TABLE_INGREDIENTS, columns, null, null, null, null, null);
        ArrayList<String> items = new ArrayList<>();
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
     * Created by Andrew Garver on 11/19/2015.
     * <p/>
     * Get all the ingredients in the cupboard (for computation)
     */
    public ArrayList<Ingredient> getAllIngredientsVerbose() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {helper.PRIMARY_KEY, helper.QUANTITY, helper.METRIC, helper.NAME};
        Cursor cursor = db.query(helper.TABLE_INGREDIENTS, columns, null, null, null, null, null);
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        while (cursor.moveToNext()) {
            int quant = cursor.getInt(1);
            String unit = cursor.getString(2);
            String name = cursor.getString(3);
            ingredients.add(new Ingredient(name, quant, unit));
        }
        return ingredients;
    }

    /**
     * Created by Andrew Garver on 11/19/2015.
     * <p/>
     * Get all the ingredients in the planned meals
     */
    public ArrayList<Ingredient> getPlannedIngredients(ArrayList<Integer> plannedRecipes) {
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        for (int i : plannedRecipes)
            ingredients.addAll(getRecipeIngredientsVerbose(i));

        return ingredients;
    }

    /**
     * Created by Andrew Garver on 11/19/2015.
     * <p/>
     * Get the id of a recipe
     */
    public int getRecipeId(String recipeName) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {helper.PRIMARY_KEY};
        Cursor cursor = db.query(helper.TABLE_RECIPES, columns, "NAME=?", new String[]{recipeName}, null, null, null, "1");
        while (cursor.moveToNext()) {
            int cid = cursor.getInt(0);
            return cid;
        }
        return -1;
    }

    /**
     * Created by Andrew Garver on 11/19/2015.
     * <p/>
     * Get all the recipes in the cookbook
     */
    public ArrayList<String> getAllRecipes() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {helper.PRIMARY_KEY, helper.NAME, helper.INSTRUCTIONS};
        Cursor cursor = db.query(helper.TABLE_RECIPES, columns, null, null, null, null, helper.NAME + " COLLATE NOCASE");
        ArrayList<String> items = new ArrayList<>();
        while (cursor.moveToNext()) {
            int cid = cursor.getInt(0);
            String name = cursor.getString(1);
            String instructions = cursor.getString(2);
            items.add(name);
        }
        return items;
    }

    /**
     * Created by Andrew Garver on 11/19/2015.
     * <p/>
     * Get a specific recipe
     */
    public String[] getRecipe(String name) {
        Log.i(TAG, "getting recipe");
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] recipeColumns = {helper.PRIMARY_KEY, helper.NAME, helper.INSTRUCTIONS};
        Cursor cursor = db.query(helper.TABLE_RECIPES, recipeColumns, "NAME=?", new String[]{name}, null, null, null, "1");
        Log.i(TAG, "It's working!");
        String[] items = new String[4];
        Log.i(TAG, "about to parse returned data from recipe");
        while (cursor.moveToNext()) {
            int cid = cursor.getInt(0);
            String displayName = cursor.getString(1);
            String instructions = cursor.getString(2);
            items[0] = Integer.toString(cid);
            items[1] = displayName;
            items[2] = instructions;
        }
        Log.i(TAG, "returning recipe for you sir");
        return items;
    }

    public boolean deleteRecipe(String name) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ArrayList<Ingredient> recIngreds = getRecipeIngredientsVerbose(getRecipeId(name));
        for (Ingredient ingred : recIngreds)
            db.delete(DatabaseHelper.TABLE_RECIPEINGREDIENTS, DatabaseHelper.NAME + "='" + ingred.getName() + "'" , null);

        return db.delete(DatabaseHelper.TABLE_RECIPES, DatabaseHelper.NAME + "='" + name + "'", null) > 0;
    }

    public ArrayList<Ingredient> getRecipeIngredientsVerbose(long id) {
        SQLiteDatabase db = helper.getWritableDatabase();

        String[] columns = {helper.PRIMARY_KEY, helper.QUANTITY, helper.METRIC, helper.NAME};
        ArrayList<Ingredient> ingredients = new ArrayList<>();
            Cursor cursor = db.query(helper.TABLE_RECIPEINGREDIENTS, columns, "RECIPE_ID=?", new String[]{String.valueOf(id)}, null, null, null, null);
        while (cursor.moveToNext()) {
            int quant = cursor.getInt(1);
            String unit = cursor.getString(2);
            String name = cursor.getString(3);
            ingredients.add(new Ingredient(name, quant, unit));
        }
        return ingredients;
    }

    /**
     * Created by Andrew Garver on 11/19/2015.
     * <p/>
     * Get the ingredients of a specific recipe
     */
    public String getRecipeIngredients(long id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] ingredientColumns = {helper.PRIMARY_KEY, helper.NAME, helper.QUANTITY, helper.METRIC, helper.RECIPE_ID};
        Cursor cursor = db.query(helper.TABLE_RECIPEINGREDIENTS, ingredientColumns, "RECIPE_ID=?", new String[]{Long.toString(id)}, null, null, null, null);
        String ingredients = "";
        Log.i(TAG, String.valueOf(cursor.getCount()));
        while (cursor.moveToNext()) {
            ingredients += cursor.getString(2) + " " + cursor.getString(3) + " " + cursor.getString(1) + "\n";
            Log.i(TAG, ingredients);
        }
        return ingredients;
    }

    /**
     * Created by Andrew Garver on 11/19/2015.
     * <p/>
     * Insert item(s) into shopping list database
     */
    public long addToShoppingList(String name, String quant, String unit, boolean auto) {
        SQLiteDatabase db = helper.getWritableDatabase();

        int quantNum = Integer.parseInt(quant);
        ArrayList<Ingredient> shoppingList = getAllShoppingListItemsVerbose();
        for (Ingredient i : shoppingList) {
            if (i.getName().equalsIgnoreCase(name) && i.getMetric().equals(unit)) {
                quantNum += i.getQuantity();
                db.delete(helper.TABLE_SHOPPINGLIST, "NAME=? AND METRIC=?", new String[]{name, unit});
            }
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.NAME, name);
        contentValues.put(DatabaseHelper.QUANTITY, String.valueOf(quantNum));
        contentValues.put(DatabaseHelper.METRIC, unit);
        if (auto)
            contentValues.put(DatabaseHelper.MANUAL_ADD, 0);
        else
            contentValues.put(DatabaseHelper.MANUAL_ADD, 1);
        Log.i(TAG, "called addToShoppingList");
        long id = db.insert(DatabaseHelper.TABLE_SHOPPINGLIST, null, contentValues);
        return id;
    }

    public boolean deleteFromShoppingList(String name) {
        SQLiteDatabase db = helper.getWritableDatabase();
        return db.delete(DatabaseHelper.TABLE_SHOPPINGLIST, DatabaseHelper.NAME + "='" + name + "'", null) > 0;
    }

    /**
     * Created by Andrew Garver on 11/19/2015.
     * <p/>
     * Get all the recipes in the cookbook
     */
    public ArrayList<String> getAllShoppingListItems() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {helper.PRIMARY_KEY, helper.NAME, helper.QUANTITY, helper.METRIC};
        Cursor cursor = db.query(helper.TABLE_SHOPPINGLIST, columns, null, null, null, null, helper.NAME + " COLLATE NOCASE");
        ArrayList<String> items = new ArrayList<>();
        while (cursor.moveToNext()) {
            int cid = cursor.getInt(0);
            String name = cursor.getString(1);
            String quant = cursor.getString(2);
            String unit = cursor.getString(3);
            String item = name + " - " + quant + " " + unit;
            Log.i(TAG, "called getAllShoppingListItems");
            items.add(item);
        }
        return items;
    }

    /**
     * Created by Andrew Garver on 11/19/2015.
     * <p/>
     * Get all the recipes in the cookbook (for computation)
     */
    public ArrayList<Ingredient> getAllShoppingListItemsVerbose() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {helper.PRIMARY_KEY, helper.NAME, helper.QUANTITY, helper.METRIC};
        Cursor cursor = db.query(helper.TABLE_SHOPPINGLIST, columns, null, null, null, null, null);
        ArrayList<Ingredient> ingredients = new ArrayList<>();
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
     * Created by Andrew Garver on 11/19/2015.
     * <p/>
     * Drop the shopping list table and rebuild it.
     */
    public void refreshShoppingList() {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(helper.TABLE_SHOPPINGLIST, helper.MANUAL_ADD+"=?", new String[] {"0"});
    }

    static class DatabaseHelper extends SQLiteOpenHelper {

        // Database global constants
        public static final String DATABASE_NAME = "recipe_grabber";
        public static final String TABLE_RECIPES = "recipes";
        public static final String TABLE_RECIPEINGREDIENTS = "recipeIngredients";
        public static final String TABLE_INGREDIENTS = "ingredients";
        public static final String TABLE_SHOPPINGLIST = "shoppingList";
        public static final String PRIMARY_KEY = "ID";
        public static final String NAME = "NAME";
        public static final String PIC = "PIC";
        public static final String INSTRUCTIONS = "INSTRUCTIONS";
        public static final String QUANTITY = "QUANTITY";
        public static final String METRIC = "METRIC";
        public static final String INGREDIENTS = "INGREDIENTS";
        public static final String RECIPE_ID = "RECIPE_ID";
        public static final String ITEM = "item";
        public static final String MANUAL_ADD = "MANUAL_ADD";

        public static final int DATABASE_VERSION = 50;

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            try {
                Log.i("thing", "Inside onCreate");
                db.execSQL("CREATE TABLE recipes (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "NAME VARCHAR(255), " +
//                        "PIC VARCHAR(255), " + TODO: stretch goal
                        "INSTRUCTIONS VARCHAR(255));");
                db.execSQL("CREATE TABLE ingredients (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "NAME VARCHAR(255), " +
                        "QUANTITY INTEGER, " +
                        "METRIC VARCHAR(255));");
                db.execSQL("CREATE TABLE recipeIngredients (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "NAME VARCHAR(255), " +
                        "QUANTITY INTEGER, " +
                        "METRIC VARCHAR(255), " +
                        "RECIPE_ID INTEGER, " +
                        "FOREIGN KEY(RECIPE_ID) REFERENCES recipes(ID));");
                db.execSQL("CREATE TABLE shoppingList (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "NAME VARCHAR(255), " +
                        "QUANTITY INTEGER, " +
                        "METRIC VARCHAR(255), " +
                        "MANUAL_ADD INTEGER);");
                db.execSQL("CREATE TABLE plannedRecipes (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "RECIPE_ID INTEGER, " +
                        "DATE VARCHAR(255), " +
                        "FOREIGN KEY(RECIPE_ID) REFERENCES recipes(ID));");
                Log.i("thing", "Table creation successful");
            } catch (SQLException e) {
                Log.e("thing", "Failure to create database", e);
            }
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                db.execSQL("DROP TABLE IF EXISTS recipes");
                db.execSQL("DROP TABLE IF EXISTS ingredients");
                db.execSQL("DROP TABLE IF EXISTS recipeIngredients");
                db.execSQL("DROP TABLE IF EXISTS shoppingList");
                db.execSQL("DROP TABLE IF EXISTS plannedRecipes");
                onCreate(db);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
