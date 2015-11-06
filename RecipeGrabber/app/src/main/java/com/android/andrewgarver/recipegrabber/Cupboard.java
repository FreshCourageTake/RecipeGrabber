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

/**
 * Created by Andrew Garver on 11/2/2015.
 */
public class Cupboard extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String[] items = {"Salt", "Pepper", "Milk", "Pepperoni", "Mozzarella Cheese", "Parsley", "Nutmeg",
                "Salt", "Pepper", "Milk", "Pepperoni", "Mozzarella Cheese", "Parsley", "Nutmeg"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                R.layout.row_layout,
                items);

        View view = inflater.inflate(R.layout.frag_cupboard, container, false);
        ListView list = (ListView) view.findViewById(R.id.listView);
        list.setAdapter(adapter);

        ImageButton addBtn = (ImageButton) view.findViewById(R.id.addTo);
        addBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddToCupboard.class));
            }
        });

        return view;
    }


}
