package com.android.andrewgarver.recipegrabber;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Andrew Garver on 11/19/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TAG = DatabaseHelper.class.getSimpleName();

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
    public static final int DATABASE_VERSION = 21;

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        try {
            Log.i(TAG, "Inside onCreate");
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
                    "INGREDIENT_ID," +
                    "FOREIGN KEY(RECIPE_ID) REFERENCES recipes(ID)," +
                    "FOREIGN KEY(INGREDIENT_ID) REFERENCES ingredients(ID));");
            Log.i(TAG, "Table creation successful");
        } catch(SQLException e) {
            Log.e(TAG, "Failure to create database", e);
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
