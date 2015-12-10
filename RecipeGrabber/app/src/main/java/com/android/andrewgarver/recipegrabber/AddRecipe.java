package com.android.andrewgarver.recipegrabber;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
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
import android.widget.RelativeLayout;
import android.widget.Spinner;

public class AddRecipe extends AppCompatActivity {
    private static final String TAG = AddRecipe.class.getSimpleName();

    DatabaseAdapter dbHelper;


    int numNewLines; // TODO: On refresh we need to reset this to 0!!!


    int ids[] = {R.id.newRow1, R.id.newRow2, R.id.newRow3, R.id.newRow4, R.id.newRow5,
            R.id.newRow6, R.id.newRow7, R.id.newRow8, R.id.newRow9, R.id.newRow10,
            R.id.newRow11, R.id.newRow12, R.id.newRow13, R.id.newRow14, R.id.newRow15,
            R.id.newRow16, R.id.newRow17, R.id.newRow18, R.id.newRow19, R.id.newRow20};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbHelper = new DatabaseAdapter(this);
        numNewLines = 0;

        //this must be final since it is accessed from an inner class
        final ImageButton add = (ImageButton) findViewById(R.id.addMore);
        final Button addRecipe = (Button) findViewById(R.id.addRecipe);

        //need to add the listener to add an extra row of input fields
        add.setOnClickListener(new View.OnClickListener() {
            @Override

            //When they click the + button, they will get another row for input.
            public void onClick(View view) {
                // Don't let the user enter any more than 20 new lines
                if (numNewLines > 19)
                    return;

                //We use the context of the button, since it is on the activity we are using
                LayoutInflater vi = (LayoutInflater) add.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = vi.inflate(R.layout.input_field, null); //This is the layout of the new row

                // update id of container and then increment number of new lines
                v.setId(ids[numNewLines]);
                ++numNewLines;

                //There is an (at first) empty container LinearLayout that we insert these into
                ((ViewGroup) findViewById(R.id.container)).addView(v);
            }
        });

        addRecipe.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                Log.i(TAG, "adding recipe");

                // insert the name and instructions of the recipe into the DB
                String recName = ((EditText) findViewById(R.id.recipeName)).getText().toString();
                String recInstructions = ((EditText) findViewById(R.id.editText)).getText().toString();
                long id = -1;
                if (!recName.equals("") && !recInstructions.equals("")) {
                    id = dbHelper.addRecipeInfo(recName, recInstructions);
                    if (id < 0) {
                        Log.i(TAG, "failed to add recipe");
                        return;
                    }
                }
                Log.i(TAG, "added recipe");
                String ingredients = "";

                // input the first line of ingredients
                String ingQuant = ((EditText) findViewById(R.id.ingQuant)).getText().toString();
                String ingUnit = ((Spinner) findViewById(R.id.ingUnit)).getSelectedItem().toString();
                String ingName = ((EditText) findViewById(R.id.ingName)).getText().toString();
                if (!ingQuant.equals("") && !ingUnit.equals("") && !ingName.equals("")) {
                    dbHelper.addRecipeIngredients(ingName, ingQuant, ingUnit, id);
                }


                for (int i = 0; i < numNewLines; ++i) {
                    RelativeLayout rel = ((RelativeLayout) findViewById(ids[i]));
                    ingQuant = ((EditText) rel.findViewById(R.id.quanNewRow)).getText().toString();
                    ingUnit = ((Spinner) rel.findViewById(R.id.unitNewRow)).getSelectedItem().toString();
                    ingName = ((EditText) rel.findViewById(R.id.nameNewRow)).getText().toString();
                    if (!ingQuant.equals("") && !ingUnit.equals("") && !ingName.equals("")) {
                        dbHelper.addRecipeIngredients(ingName, ingQuant, ingUnit, id);
                    }
                }

                Intent data = new Intent();
                data.putExtra("recipeName", recName);
                setResult(RESULT_OK, data); //allows us to access this data in the previous fragment
                finish(); // This takes us back to the previous fragment
            }
        });

    }


}
