package com.android.andrewgarver.recipegrabber;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.andrewgarver.recipegrabber.extendCalView.CalendarProvider;
import com.android.andrewgarver.recipegrabber.extendCalView.Event;
import com.android.andrewgarver.recipegrabber.extendCalView.ExtendedCalendarView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Andrew Garver on 11/2/2015.
 */
public class Menu extends Fragment {
    private static final String TAG = Menu.class.getSimpleName();

    private ListView listView;
    private ExtendedCalendarView extendedCalendarView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        String[] items = {"Pancakes", "Pizza", "Burger"};

        View view = inflater.inflate(R.layout.frag_menu, container, false);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getApplicationContext(),
                R.layout.row_layout,
                items);


        Log.i(TAG, "The day of the month is ");


        Log.i(TAG, "Use to test: ");

        Log.i(TAG, "The Month is ");


        listView = (ListView) view.findViewById(R.id.menuListView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                startActivity(new Intent(getActivity(), DisplayRecipe.class));
            }
        });

        // add the day to the calender


        return view;
    }


    ContentValues values = new ContentValues();
    //values.put(CalendarProvider.COLOR, Event.COLOR_RED);
}