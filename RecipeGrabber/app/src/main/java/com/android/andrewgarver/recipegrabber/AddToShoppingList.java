package com.android.andrewgarver.recipegrabber;

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

import java.util.ArrayList;

/**
 * Add items to shopping list
 *
 *
 *
 * @author  Andrew Garver, Landon Jamieson, and Reed Atwood
 * @version 1.0
 * @since   12/10/2015
 */
public class AddToShoppingList extends AppCompatActivity {

    /**
     * Debugging Tag to display LogCat messages for debugging
     */
    private static final String TAG = AddToShoppingList.class.getSimpleName();
    DatabaseAdapter dbHelper;

    /**
     *
     */
    int numNewLines; // TODO: On refresh we need to reset this to 0!!!

    /**
     *
     */
    int ids[] = {R.id.newRow1, R.id.newRow2, R.id.newRow3, R.id.newRow4, R.id.newRow5,
            R.id.newRow6, R.id.newRow7, R.id.newRow8, R.id.newRow9, R.id.newRow10,
            R.id.newRow11, R.id.newRow12, R.id.newRow13, R.id.newRow14, R.id.newRow15,
            R.id.newRow16, R.id.newRow17, R.id.newRow18, R.id.newRow19, R.id.newRow20};


    /**
     *
     *
     * @param savedInstanceState save the activity for reopening
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_shoppinglist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        numNewLines = 0;

        /**
         *
         */
        dbHelper = new DatabaseAdapter(this);

        /**
         * this must be final since it is accessed from an inner class
         */
        final ImageButton add = (ImageButton) findViewById(R.id.addMore);
        final Button addIng = (Button) findViewById(R.id.addIng);
        final ArrayList<String> results = new ArrayList<>();

        /**
         * need to add the listener to add an extra row of input fields
         */
        add.setOnClickListener(new View.OnClickListener() {

            /**
             * When they click the + button, they will get another row for input.
             *
             * @param view
             */
            @Override
            public void onClick(View view) {

                /**
                 * We use the context of the button, since it is on the activity we are using
                 */
                LayoutInflater vi = (LayoutInflater) add.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = vi.inflate(R.layout.input_field, null); //This is the layout of the new row

                /**
                 * if we need to change the id we would do it here
                 */
                v.setId(ids[numNewLines]);
                ++numNewLines;

                /**
                 * There is an (at first) empty container LinearLayout that we insert these into
                 */
                ((ViewGroup) findViewById(R.id.container)).addView(v);
            }
        });

        addIng.setOnClickListener(new View.OnClickListener() {

            /**
             *
             *
             * @param view
             */
            @Override
            public void onClick(View view) {
                Log.i(TAG, "adding item");

                String item = "";

                /**
                 * input the first line of ingredients
                 */
                String ingQuant = ((EditText) findViewById(R.id.ingQuant)).getText().toString();
                String ingUnit = ((Spinner) findViewById(R.id.ingUnit)).getSelectedItem().toString();
                String ingName = ((EditText) findViewById(R.id.ingName)).getText().toString();

                /**
                 *
                 */
                if (!ingQuant.equals("") && !ingName.equals("")) {
                    item = ingName + " - " + ingQuant + " " + ingUnit;

                    /**
                     *
                     */
                    if (dbHelper.addToShoppingList(ingName, ingQuant, ingUnit, false) > 0) {
                        Log.i(TAG, "added ingredients");
                        results.add(item);
                    } else {
                        Log.i(TAG, "failed to add ingredients");
                    }
                }

                /**
                 *
                 */
                for (int i = 0; i < numNewLines; ++i) {
                    RelativeLayout rel = ((RelativeLayout) findViewById(ids[i]));
                    ingQuant = ((EditText) rel.findViewById(R.id.quanNewRow)).getText().toString();
                    ingUnit = ((Spinner) rel.findViewById(R.id.unitNewRow)).getSelectedItem().toString();
                    ingName = ((EditText) rel.findViewById(R.id.nameNewRow)).getText().toString();

                    /**
                     *
                     */
                    if (!ingQuant.equals("") && !ingName.equals("")) {
                        item = ingName + " - " + ingQuant + " " + ingUnit;

                        /**
                         *
                         */
                        if (dbHelper.addToShoppingList(ingName, ingQuant, ingUnit, false) > 0) {
                            Log.i(TAG, "added ingredients");
                            results.add(item);
                        } else {
                            Log.i(TAG, "failed to add ingredients");
                        }
                    }
                }

                /**
                 *
                 */
                Intent data = new Intent();
                data.putStringArrayListExtra("results", results);
                setResult(RESULT_OK, data);
                finish(); // This takes us back to the previous fragment
            }
        });
    }
}
