package com.android.andrewgarver.recipegrabber;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.TextView;

import java.util.ArrayList;

public class AddToCupboard extends AppCompatActivity {
    private static final String TAG = AddToCupboard.class.getSimpleName();
    DatabaseAdapter dbHelper;

    int numNewLines; // TODO: On refresh we need to reset this to 0!!!

    int ids[] = {R.id.newRow1, R.id.newRow2, R.id.newRow3, R.id.newRow4, R.id.newRow5,
            R.id.newRow6, R.id.newRow7, R.id.newRow8, R.id.newRow9, R.id.newRow10,
            R.id.newRow11, R.id.newRow12, R.id.newRow13, R.id.newRow14, R.id.newRow15,
            R.id.newRow16, R.id.newRow17, R.id.newRow18, R.id.newRow19, R.id.newRow20};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_cupboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        numNewLines = 0;
        Log.i(TAG, "Started Add To Cupboard");

        dbHelper = new DatabaseAdapter(this);

        //this must be final since it is accessed from an inner class
        final ImageButton add = (ImageButton) findViewById(R.id.addMore);
        final Button addIng = (Button) findViewById(R.id.addIng);
        final ArrayList<String> results = new ArrayList<>();

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

                Log.i(TAG, "added line " + numNewLines + " with id of " + ids[numNewLines - 1]);

            }
        });

        addIng.setOnClickListener(new View.OnClickListener() {
            @Override

            //Clicking the add button adds the first row to the database
            public void onClick(View view) {
                Log.i(TAG, "adding to cupboard");

                // adds the first row to database
                String quant = ((EditText)findViewById(R.id.ingQuant)).getText().toString();
                String unit = ((Spinner)findViewById(R.id.ingUnit)).getSelectedItem().toString();
                String ingName = ((EditText)findViewById(R.id.ingName)).getText().toString();

                if (!quant.equals("") && !unit.equals("") && !ingName.equals("")) {
                    if (dbHelper.addIngredient(quant, unit, ingName) > 0) {
                        Log.i(TAG, "added to cupboard");
                        results.add(ingName + ": " + quant + ' ' + unit);
                    }
                }

                // adds any additional rows
                for (int i = 0; i < numNewLines; ++i) {
                    RelativeLayout rel = ((RelativeLayout)findViewById(ids[i]));
                    String dynamicQuant = ((EditText)rel.findViewById(R.id.quanNewRow)).getText().toString();
                    String dynamicUnit = ((Spinner)rel.findViewById(R.id.unitNewRow)).getSelectedItem().toString();
                    String dynamicIngName = ((EditText)rel.findViewById(R.id.nameNewRow)).getText().toString();

                    if (!dynamicQuant.equals("") && !dynamicUnit.equals("") && !dynamicIngName.equals("")) {
                        if (dbHelper.addIngredient(dynamicQuant, dynamicUnit, dynamicIngName) > 0) {
                            Log.i(TAG, "added to cupboard");
                            results.add(dynamicIngName + ": " + dynamicQuant + ' ' + dynamicUnit);
                        }
                    }
                    Log.i(TAG, "added line " + i + 1 + " to DB with id of " + ids[i]);
                }

                Intent data = new Intent();
                data.putStringArrayListExtra("results", results);
                setResult(RESULT_OK, data);
                finish(); // This takes us back to the previous fragment
            }
        });
    }
}
