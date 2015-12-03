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

    DatabaseHelper helper;
    public DatabaseAdapter(Context context) {
        helper = new DatabaseHelper(context);
    }
    public long addIngredient(String quant, String metric, String name) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.QUANTITY, quant);
        contentValues.put(DatabaseHelper.METRIC, metric);
        contentValues.put(DatabaseHelper.NAME, name);
        long id = db.insert(DatabaseHelper.TABLE_INGREDIENTS, null, contentValues);
        return id;
    }

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

    static class DatabaseHelper extends SQLiteOpenHelper {

        // Database global constants
        public static final String DATABASE_NAME = "recipe_grabber";
        public static final String TABLE_RECIPES = "recipes";
        public static final String TABLE_INGREDIENTS = "ingredients";
        public static final String TABLE_RECING = "recing";
        public static final String PRIMARY_KEY = "ID";
        public static final String NAME = "NAME";
        public static final String PIC = "PIC";
        public static final String INSTRUCTIONS = "INSTRUCTIONS";
        public static final String QUANTITY = "QUANTITY";
        public static final String METRIC = "METRIC";
        public static final int DATABASE_VERSION = 31;

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            try {
                Log.i("thing", "Inside onCreate");
                db.execSQL("CREATE TABLE recipes (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "NAME VARCHAR(255), " +
                        "PIC VARCHAR(255), " +
                        "INSTRUCTIONS VARCHAR(255));");
                db.execSQL("CREATE TABLE ingredients (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "NAME VARCHAR(255), " +
                        "QUANTITY INTEGER, " +
                        "METRIC VARCHAR(255));");
                db.execSQL("CREATE TABLE recing (ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "RECIPE_ID INTEGER," +
                        "INGREDIENT_ID INTEGER," +
                        "FOREIGN KEY(RECIPE_ID) REFERENCES recipes(ID)," +
                        "FOREIGN KEY(INGREDIENT_ID) REFERENCES ingredients(ID));");
                Log.i("thing", "Table creation successful");
            } catch(SQLException e) {
                Log.e("thing", "Failure to create database", e);
            }
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                db.execSQL("DROP TABLE IF EXISTS recipes");
                db.execSQL("DROP TABLE IF EXISTS ingredients");
                db.execSQL("DROP TABLE IF EXISTS recing");
                onCreate(db);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
