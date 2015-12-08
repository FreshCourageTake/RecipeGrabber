package com.android.andrewgarver.recipegrabber;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class PickRecipe extends AppCompatActivity {

    ListView recipesList;
    DatabaseAdapter dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_recipe);
        dbHelper = new DatabaseAdapter(getApplicationContext());
        ArrayList<String> items = dbHelper.getAllRecipes();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.row_layout, items);
        recipesList = (ListView) findViewById(R.id.allRecipes);
        recipesList.setAdapter(adapter);

        recipesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent data = new Intent();
                data.putExtra("recipeName", recipesList.getItemAtPosition(position).toString());
                setResult(RESULT_OK, data); //allows us to access this data in the previous activity
                finish();
            }
        });
    }
}
