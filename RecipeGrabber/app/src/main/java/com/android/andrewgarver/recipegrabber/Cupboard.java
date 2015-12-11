package com.android.andrewgarver.recipegrabber;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
public class Cupboard extends Fragment {

    private static final int ingredCode = 2;
    private DatabaseAdapter dbHelper;
    private ArrayAdapter<String> adapter;
    private ListView list;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        dbHelper = new DatabaseAdapter(getActivity());

        ArrayList<String> items = dbHelper.getAllIngredients();

        adapter = new ArrayAdapter<>(getContext(), R.layout.row_layout, items);

        View view = inflater.inflate(R.layout.frag_cupboard, container, false);
        list = (ListView) view.findViewById(R.id.listView);
        list.setAdapter(adapter);

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final String preSplit = adapter.getItem(position);
                final String toDel = preSplit.split(" - ")[0];
                AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
                adb.setTitle("Delete ingredient: " + toDel + '?');
                adb.setMessage("Are you sure you want to remove this ingredient from your cupboard?");
                adb.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.remove(preSplit);
                        dbHelper.deleteIngredient(toDel);
                        Toast.makeText(getContext(), "Deleting ingredient", Toast.LENGTH_LONG).show();
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

        ImageButton addBtn = (ImageButton) view.findViewById(R.id.addTo);
        addBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivityForResult(new Intent(getContext(), AddToCupboard.class), ingredCode);
            }
        });

        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ingredCode) {
            if (resultCode == getActivity().RESULT_OK) {
                ArrayList<String> ingredList = data.getStringArrayListExtra("results");
                adapter.addAll(ingredList);
            }
        }
    }


}
