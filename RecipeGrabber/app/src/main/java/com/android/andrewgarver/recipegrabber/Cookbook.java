package com.android.andrewgarver.recipegrabber;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Andrew Garver on 11/2/2015.
 */
public class Cookbook extends Fragment {
    private static final String TAG = Cookbook.class.getSimpleName();
    private static final int newRecipeCode = 0;

    ListView list;
    DatabaseAdapter dbHelper;
    ArrayAdapter<String> adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_cookbook, container, false);
        dbHelper = new DatabaseAdapter(getActivity());
        final ArrayList<String> items = dbHelper.getAllRecipes();

        adapter = new ArrayAdapter<>(getContext(), R.layout.row_layout, items);
        list = (ListView) view.findViewById(R.id.listView);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the name of the activity
                String recipeName = list.getItemAtPosition(position).toString();
                // Use bundles to share data between activities
                Intent intent = new Intent(getActivity(), DisplayRecipe.class);
                Bundle bundle = new Bundle();
                bundle.putString("recipeName", recipeName);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final String toDel = adapter.getItem(position);
                AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
                adb.setTitle("Delete recipe: " + toDel + '?');
                adb.setMessage("Are you sure you want to remove this recipe?");
                adb.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.remove(toDel);
                        //need to remove from database still
                        Toast.makeText(getContext(), "Recipe Deleted", Toast.LENGTH_LONG).show();
                    }
                });
                adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog ad = adb.create();
                ad.show();
                return true;
            }
        });

        ImageButton addBtn = (ImageButton) view.findViewById(R.id.addRecipe);
        addBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivityForResult(new Intent(getContext(), AddRecipe.class), newRecipeCode);
            }
            });

        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == newRecipeCode) {
            if (resultCode == getActivity().RESULT_OK) {
                Log.i(TAG, "RESULT OKAY");
                String recipe = data.getStringExtra("recipeName");
                adapter.add(recipe);
            }
        }
    }

}
