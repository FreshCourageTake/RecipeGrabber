package com.android.andrewgarver.recipegrabber;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 *
 * <p>
 *
 *
 * @author  Andrew Garver, Landon Jamieson, and Reed Atwood
 * @version 1.0
 * @since   12/10/2015
 */
public class PickRecipe extends AppCompatActivity {

    /**
     * Debugging Tag to display LogCat messages for debugging
     */
    private static final String TAG = PickRecipe.class.getSimpleName();

    /**
     *
     */
    ListView recipesList;
    DatabaseAdapter dbHelper;

    /**
     *
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_recipe);

        /**
         *
         */
        dbHelper = new DatabaseAdapter(getApplicationContext());
        ArrayList<String> items = dbHelper.getAllRecipes();

        /**
         *
         */
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.row_layout, items);
        recipesList = (ListView) findViewById(R.id.allRecipes);
        recipesList.setAdapter(adapter);

        /**
         *
         */
        recipesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            /**
             *
             *
             * @param parent
             * @param view
             * @param position
             * @param id
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                /**
                 *
                 */
                Intent data = new Intent();
                data.putExtra("recipeName", recipesList.getItemAtPosition(position).toString());
                setResult(RESULT_OK, data); //allows us to access this data in the previous activity
                finish();
            }
        });

        /**
         *
         */
        recipesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            /**
             *
             *
             * @param parent
             * @param view
             * @param position
             * @param id
             * @return true
             */
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                /**
                 * Get the name of the activity
                 */
                String recipeName = recipesList.getItemAtPosition(position).toString();

                /**
                 * Use bundles to share data between activities
                 */
                Intent intent = new Intent(getApplicationContext(), DisplayRecipe.class);
                Bundle bundle = new Bundle();
                bundle.putString("recipeName", recipeName);
                intent.putExtras(bundle);
                startActivity(intent);

                return true;
            }
        });
    }
}