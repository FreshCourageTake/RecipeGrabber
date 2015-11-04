package com.android.andrewgarver.recipegrabber;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by Andrew Garver on 11/2/2015.
 */
public class ShoppingList extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String[] items = {"Cereal", "Soda Pop", "Tomato Paste", "Spaghetti Noodles", "Chocolate Chips", "Hot Dogs", "Snacks",
                "Cereal", "Soda Pop", "Tomato Paste", "Spaghetti Noodles", "Chocolate Chips", "Hot Dogs", "Snacks"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                R.layout.row_layout,
                items);

        View view = inflater.inflate(R.layout.frag_shoppinglist, container, false);
        ListView list = (ListView) view.findViewById(R.id.listView);
        list.setAdapter(adapter);


        return view;
    }
}
