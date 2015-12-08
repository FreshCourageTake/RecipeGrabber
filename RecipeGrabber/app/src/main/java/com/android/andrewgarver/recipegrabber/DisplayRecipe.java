package com.android.andrewgarver.recipegrabber;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class DisplayRecipe extends AppCompatActivity {
    private static final String TAG = "displayRecipe";

    DatabaseAdapter dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_recipe);
        dbHelper = new DatabaseAdapter(this);

        Log.i(TAG, "Displaying recipe: ");
        //get the name of the selected recipe
        Bundle bundle = getIntent().getExtras();
        String recipeName = bundle.getString("recipeName");
        Log.i(TAG, recipeName);

        // retrieve the recipe information
        String[] recipeInfo = dbHelper.getRecipe(recipeName);
        Long id = Long.valueOf(recipeInfo[0]);
        String recipeIngredients = dbHelper.getRecipeIngredients(id);
        Log.i(TAG, "Successfully saved recipeInfo into array");

        // update text in recipe
        TextView textView = (TextView)findViewById(R.id.recipeName);
        textView.setText(recipeInfo[1]);
        textView = (TextView)findViewById(R.id.recipeInstructions);
        textView.setText(recipeInfo[2]);
        textView = (TextView)findViewById(R.id.ingQuant);
        textView.setText(recipeIngredients);
    }
}
