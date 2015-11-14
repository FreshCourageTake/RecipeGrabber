package com.android.andrewgarver.recipegrabber;

import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by Andrew Garver on 11/2/2015.
 */
public class Menu extends Fragment {

    private GridView gridView;
    private ListView listView;
    private TextView textView;
    private ImageView imageView;
    private GregorianCalendar gCal;
    private boolean menuItem = true;

    String[] items = {"Pancakes", "Pizza", "Burger", "Spaghetti", "Peanut Butter and Jelly",
            "Pancakes", "Pizza", "Burger", "Spaghetti", "Peanut Butter and Jelly",
            "Pancakes", "Pizza", "Burger", "Spaghetti", "Peanut Butter and Jelly"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                R.layout.row_layout,
                items);

        View view = inflater.inflate(R.layout.frag_menu, container, false);
        listView = (ListView) view.findViewById(R.id.dayMenuListView);
        listView.setAdapter(adapter);


        gCal = new GregorianCalendar(Locale.US);

        return view;
    }
}
