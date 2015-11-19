package com.android.andrewgarver.recipegrabber;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Andrew Garver on 11/2/2015.
 */
public class Menu extends Fragment {
    private static final String TAG = Menu.class.getSimpleName();
    private ListView listView;
    private GregorianCalendar gCalender;

    // add the days to the calender
    private RecyclerView recyclerView;
    private DayAdapter dayAdapter;

    private int num_month_days = 31;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        String[] items = {"Pancakes", "Pizza", "Burger", "Spaghetti", "Peanut Butter and Jelly",
                "Pancakes", "Pizza", "Burger", "Spaghetti", "Peanut Butter and Jelly",
                "Pancakes", "Pizza", "Burger", "Spaghetti", "Peanut Butter and Jelly"};

        View view = inflater.inflate(R.layout.frag_menu, container, false);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getApplicationContext(),
                R.layout.row_layout,
                items);

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
        recyclerView = (RecyclerView) view.findViewById(R.id.calendarRV);
        dayAdapter = new DayAdapter(getActivity(), getDay(num_month_days));
        recyclerView.setAdapter(dayAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 7));

        gCalender = new GregorianCalendar(Locale.US);

                //gCalender.getFirstDayOfWeek();

        return view;
    }

    public static List<CalDay> getDay(int num_month_days) {
        List<CalDay> numDays = new ArrayList<>();

        //log the number of days
        Log.i(TAG, "The number of days in the month are " + num_month_days);

        //error log ensures that the number of days is valid
        if (num_month_days > 31 || num_month_days < 28)
            Log.e(TAG, num_month_days + " is not a valid number of days for any month. Change num_month_days.");

        String[] days = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14",
                "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28",
                "29", "30", "31"};
        for (int i = 0; i < days.length; i++) {

            // add the number of days to each month
            CalDay current = new CalDay();
            current.day_of_month = days[i];
            numDays.add(current);
        }

        return numDays;
    }
}
