package com.android.andrewgarver.recipegrabber;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

public class AddRecipe extends AppCompatActivity {

    private static final String TAG = "AddRecipe";

    DatabaseAdapter dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbHelper = new DatabaseAdapter(this);

        //this must be final since it is accessed from an inner class
        final ImageButton add = (ImageButton) findViewById(R.id.addMore);
        final Button addRecipe = (Button) findViewById(R.id.addRecipe);

        //need to add the listener to add an extra row of input fields
        add.setOnClickListener(new View.OnClickListener() {
            @Override

            //When they click the + button, they will get another row for input.
            public void onClick(View view) {

                //We use the context of the button, since it is on the activity we are using
                LayoutInflater vi = (LayoutInflater) add.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = vi.inflate(R.layout.input_field, null); //This is the layout of the new row

                //if we need to change the id we would do it here

                //There is an (at first) empty container LinearLayout that we insert these into
                ((ViewGroup) findViewById(R.id.container)).addView(v);
            }
        });

        addRecipe.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                Log.i(TAG, "adding recipe");

                String recName = ((EditText)findViewById(R.id.recipeName)).getText().toString();
                String recInstructions = ((EditText)findViewById(R.id.editText)).getText().toString();
                long id = dbHelper.addRecipeInfo(recName, recInstructions);
                if (id < 0){
                    Log.i(TAG, "failed to add recipe");
                    return;
                }
                Log.i(TAG, "added recipe");
                String ingredients = ((EditText)findViewById(R.id.ingQuant)).getText().toString() + " " +
                        ((Spinner)findViewById(R.id.ingUnit)).getSelectedItem().toString() + " " +
                        ((EditText)findViewById(R.id.ingName)).getText().toString();
                if (dbHelper.addRecipeIngredients(ingredients, id) > 0) {
                    Log.i(TAG, "added ingredients");
                } else {
                    Log.i(TAG, "failed to add ingredients");
                }
                finish(); // This takes us back to the previous fragment
            }
        });

    }

}
