package com.android.andrewgarver.recipegrabber;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Andrew Garver on 11/2/2015.
 */
public class ShoppingList extends Fragment {

    private final static int shoppingCode = 3;
    private DatabaseAdapter dbHelper;
    private ListView list;
    private ArrayAdapter<String> adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dbHelper = new DatabaseAdapter(getActivity());
        ArrayList<String> items = dbHelper.getAllShoppingListItems();

        adapter = new ArrayAdapter<>(getContext(), R.layout.row_layout, items);

        View view = inflater.inflate(R.layout.frag_shoppinglist, container, false);
        list = (ListView) view.findViewById(R.id.listView);
        list.setAdapter(adapter);

        ImageButton addBtn = (ImageButton) view.findViewById(R.id.addToShoppingList);
        addBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivityForResult(new Intent(getContext(), AddToShoppingList.class), shoppingCode);
            }
        });

        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == shoppingCode) {
            if (resultCode == getActivity().RESULT_OK) {
                ArrayList<String> shopping = data.getStringArrayListExtra("results");
                adapter.addAll(shopping);
            }
        }
    }
}
