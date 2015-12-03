package com.android.andrewgarver.recipegrabber;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

    ListView list;
    DatabaseAdapter dbHelper;

//    String[] items = {"Pancakes", "Pizza", "Burger", "Spaghetti", "Peanut Butter and Jelly",
//            "Pancakes", "Pizza", "Burger", "Spaghetti", "Peanut Butter and Jelly",
//            "Pancakes", "Pizza", "Burger", "Spaghetti", "Peanut Butter and Jelly"};

//    Integer[] imageId = { R.drawable.burger, R.drawable.burger, R.drawable.burger, R.drawable.burger,
//            R.drawable.burger, R.drawable.burger, R.drawable.burger, R.drawable.burger,
//            R.drawable.burger, R.drawable.burger, R.drawable.burger, R.drawable.burger,
//            R.drawable.burger, R.drawable.burger, R.drawable.burger };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_cookbook, container, false);
        dbHelper = new DatabaseAdapter(getActivity());
        ArrayList<String> items = dbHelper.getAllRecipes();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                R.layout.row_layout,
                items);
//        CustomList adapter = new CustomList(getActivity(), items, imageId);
        list = (ListView) view.findViewById(R.id.listView);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getActivity(), DisplayRecipe.class));
            }
        });

        ImageButton addBtn = (ImageButton) view.findViewById(R.id.addRecipe);
            addBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddRecipe.class));
            }
        });

        return view;
    }


}
