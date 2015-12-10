package com.android.andrewgarver.recipegrabber;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.android.andrewgarver.recipegrabber.extendCalView.CalendarProvider;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Andrew Garver on 11/2/2015.
 */
public class ShoppingList extends Fragment {
    String TAG = "ShoppingList";
    DatabaseAdapter dbHelper;
    CalendarProvider cpHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        dbHelper = new DatabaseAdapter(getActivity());
        cpHelper = new CalendarProvider();
        cpHelper.setContext(getActivity());

        // This is where we automatically add things to the shopping list

        // We need to refresh the shoppingList before anything so we don't get duplicates
        dbHelper.refreshShoppingList();

        // Gets the recipe names
        ArrayList<String> plannedRecipes = cpHelper.getPlannedRecipes();
        // Get the recipe id numbers and store them in an ArrayList
        ArrayList<Integer> plannedRecipeIds = new ArrayList<>();
        for (String s : plannedRecipes) {
            plannedRecipeIds.add(dbHelper.getRecipeId(s));
        }
        // Get the ingredients from the recipes using the recipe_ids
        ArrayList<Ingredient> plannedIngredients = dbHelper.getPlannedIngredients(plannedRecipeIds);

        // Get the ingredients in the Cupboard
        ArrayList<Ingredient> cupboardIngredients = dbHelper.getAllIngredientsVerbose();

        // Get the already planned items on the shopping list
        ArrayList<Ingredient> shoppingListItems = dbHelper.getAllShoppingListItemsVerbose();

        // Do math with ingredients
        for (Ingredient planned : plannedIngredients) {
            boolean haveInCupboard = false;
            for (Ingredient cupboard : cupboardIngredients) {
                if (planned.getName().toLowerCase().equals(cupboard.getName().toLowerCase()) // the same ingredient
                    && planned.getMetric().equals(cupboard.getMetric())) { // with the same metric unit
                    haveInCupboard = true;
                            if (planned.getQuantity() > cupboard.getQuantity()) { // don't have enough in the cupboard
                        // Add the results to the shopping list
                        dbHelper.addToShoppingList(planned.getName(), String.valueOf(planned.getQuantity() - cupboard.getQuantity()), planned.getMetric(), true);
                    }
                }
            }
            if (!haveInCupboard) {
                dbHelper.addToShoppingList(planned.getName(), String.valueOf(planned.getQuantity()), planned.getMetric(), true);
            }
        }

        // This just gets all the items in the shopping list.  This includes manually added as well as automatically added things.
        ArrayList<String> items = dbHelper.getAllShoppingListItems(); // this is what we need to update (In another function)

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                R.layout.row_layout,
                items);

        View view = inflater.inflate(R.layout.frag_shoppinglist, container, false);
        ListView list = (ListView) view.findViewById(R.id.listView);
        list.setAdapter(adapter);

        ImageButton addBtn = (ImageButton) view.findViewById(R.id.addToShoppingList);
        addBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddToShoppingList.class));
            }
        });


        return view;
    }
}
