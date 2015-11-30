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
import android.widget.TextView;
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
    private GregorianCalendar gCalendar;

    // add the days to the calender
    private RecyclerView recyclerView;
    private DayAdapter dayAdapter;
    private String month;
    private int monthNum;
    private int year;
    private static int day;
    private int dayNum;
    private TextView monthTextView;
    private int [] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private static int num_month_days;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        String[] items = {"Pancakes", "Pizza", "Burger"};

        View view = inflater.inflate(R.layout.frag_menu, container, false);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getApplicationContext(),
                R.layout.row_layout,
                items);

        gCalendar = new GregorianCalendar(Locale.US);
        gCalendar.setFirstDayOfWeek(Calendar.MONDAY);
        year = gCalendar.get(Calendar.YEAR);
        monthNum = gCalendar.get(Calendar.MONTH);
        dayNum = gCalendar.get(Calendar.DAY_OF_MONTH);
        month =  gCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
        day = gCalendar.get(Calendar.DAY_OF_MONTH);

        Log.i(TAG, "The day of the month is " + day);

        num_month_days = daysInMonth[gCalendar.get(Calendar.MONTH)];
        if (month.equals("February") && gCalendar.isLeapYear(year))
            ++num_month_days;

        Log.i(TAG, "Use to test: ");


        monthTextView = (TextView) view.findViewById(R.id.monthTV);
        Log.i(TAG, "The Month is " + month);
        monthTextView.setText(month);

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
        dayAdapter = new DayAdapter(getActivity(), getDay());
        recyclerView.setAdapter(dayAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 7));

        //gCalendar.getFirstDayOfWeek();

        return view;
    }

    public static List<CalDay> getDay() {
        List<CalDay> numDays = new ArrayList<>();

        //log the number of days
        Log.i(TAG, "The number of days in the month are " + num_month_days);

        /////////day.setBackgroundColor(context.getResources().getColor(R.color.white));

        //error log ensures that the number of days is valid
        if (num_month_days > 31 || num_month_days < 28)
            Log.e(TAG, num_month_days + " is not a valid number of days for any month. Change num_month_days.");

        // add each day the list of Calender Days
        for (int i = 1; i <= num_month_days; i++) {

            // add the number of days to each month
            CalDay current = new CalDay();
            current.day_of_month = Integer.toString(i);
            numDays.add(current);
            //Log.i(TAG, "day " + i);
        }

        return numDays;
    }
}
