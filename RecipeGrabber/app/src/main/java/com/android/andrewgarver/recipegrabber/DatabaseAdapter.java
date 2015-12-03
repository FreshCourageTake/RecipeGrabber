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
    private static final String TAG = "DatabaseAdapter";

    DatabaseHelper helper;
    public DatabaseAdapter(Context context) {
        helper = new DatabaseHelper(context);
    }

    /**
     * Created by Andrew Garver on 11/19/2015.
     *
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

    /**
     * Created by Andrew Garver on 11/19/2015.
     *
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
     *
     * Add Ingredients that are part of a recipe
     */
    public long addRecipeIngredients(String ingredients, long recipeId) { // long here might give probs because it's int in table
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues ingContentValues = new ContentValues();

        ingContentValues.put(DatabaseHelper.INGREDIENTS, ingredients);
        ingContentValues.put(DatabaseHelper.RECIPE_ID, recipeId);
        long id = db.insert(DatabaseHelper.TABLE_RECIPEINGREDIENTS, null, ingContentValues);
        return id;
    }

    /**
     * Created by Andrew Garver on 11/19/2015.
     *
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
            String item = name + ": " + quant + " " + unit;
            items.add(item);
        }
        return items;
    }

    /**
     * Created by Andrew Garver on 11/19/2015.
     *
     * Get all the recipes in the cookbook
     */
    public ArrayList<String> getAllRecipes() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {helper.PRIMARY_KEY, helper.NAME, helper.INSTRUCTIONS};
        Cursor cursor = db.query(helper.TABLE_RECIPES, columns, null, null, null, null, null);
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
     *
     * Get a specific recipe
     */
    public String[] getRecipe(String name) {
        Log.i(TAG, "getting recipe");
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] recipeColumns = {helper.PRIMARY_KEY, helper.NAME, helper.INSTRUCTIONS};
        Cursor cursor = db.query(helper.TABLE_RECIPES, recipeColumns, "NAME=?", new String[] {name}, null, null, null, "1");
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

    /**
     * Created by Andrew Garver on 11/19/2015.
     *
     * Get the ingredients of a specific recipe
     */
    public String getRecipeIngredients(long id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] ingredientColumns = {helper.PRIMARY_KEY, helper.INGREDIENTS, helper.RECIPE_ID};
        Cursor cursor = db.query(helper.TABLE_RECIPEINGREDIENTS, ingredientColumns, "RECIPE_ID=?", new String[] {Long.toString(id)}, null, null, null, "1");
        String ingredients = null;
        while(cursor.moveToNext()) {
            ingredients = cursor.getString(1);
        }
        return ingredients;
    }

    static class DatabaseHelper extends SQLiteOpenHelper {

        // Database global constants
        public static final String DATABASE_NAME = "recipe_grabber";
        public static final String TABLE_RECIPES = "recipes";
        public static final String TABLE_RECIPEINGREDIENTS = "recipeIngredients";
        public static final String TABLE_INGREDIENTS = "ingredients";
        public static final String PRIMARY_KEY = "ID";
        public static final String NAME = "NAME";
        public static final String PIC = "PIC";
        public static final String INSTRUCTIONS = "INSTRUCTIONS";
        public static final String QUANTITY = "QUANTITY";
        public static final String METRIC = "METRIC";
        public static final String INGREDIENTS = "INGREDIENTS";
        public static final String RECIPE_ID = "RECIPE_ID";

        public static final int DATABASE_VERSION = 35;

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
                        "INGREDIENTS VARCHAR(255), " +
                        "RECIPE_ID INTEGER, " +
                        "FOREIGN KEY(RECIPE_ID) REFERENCES recipes(ID));");
                Log.i("thing", "Table creation successful");
            } catch(SQLException e) {
                Log.e("thing", "Failure to create database", e);
            }
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                db.execSQL("DROP TABLE IF EXISTS recipes");
                db.execSQL("DROP TABLE IF EXISTS ingredients");
                db.execSQL("DROP TABLE IF EXISTS recipeIngredients");
                onCreate(db);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
