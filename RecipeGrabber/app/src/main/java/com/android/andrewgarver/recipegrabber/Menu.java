package com.android.andrewgarver.recipegrabber;

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
    private GregorianCalendar gCalender;
    private boolean menuItem = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        String[] items = {"Pancakes", "Pizza", "Burger", "Spaghetti", "Peanut Butter and Jelly",
                "Pancakes", "Pizza", "Burger", "Spaghetti", "Peanut Butter and Jelly",
                "Pancakes", "Pizza", "Burger", "Spaghetti", "Peanut Butter and Jelly"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getApplicationContext(),
                R.layout.row_layout,
                items);

        View view = inflater.inflate(R.layout.frag_menu, container, false);

//        textView = (TextView) textView.findViewById(R.id.dayTV);
//        gridView = (GridView) gridView.findViewById(R.id.calendarGV);
//        imageView = (ImageView) imageView.findViewById(R.id.dayIV);

        //if (menuItem) {}


            listView = (ListView) view.findViewById(R.id.menuListView);
            listView.setAdapter(adapter);


        gCalender = new GregorianCalendar(Locale.US);

        //gCalender.getFirstDayOfWeek();

        return view;
    }
}
