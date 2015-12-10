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
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.android.andrewgarver.recipegrabber.extendCalView.CalendarProvider;
import com.android.andrewgarver.recipegrabber.extendCalView.Day;
import com.android.andrewgarver.recipegrabber.extendCalView.Event;
import com.android.andrewgarver.recipegrabber.extendCalView.ExtendedCalendarView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew Garver on 11/2/2015.
 */
public class Menu extends Fragment {
    private static final String TAG = Menu.class.getSimpleName();
    private static final int recipeRequest = 1;

    private ListView listView;
    private ExtendedCalendarView extendedCalendarView;
    private Day selDay;
    private ArrayList<String> items;
    private ArrayAdapter<String> adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_menu, container, false);
        listView = (ListView) view.findViewById(R.id.menuListView);
        items = new ArrayList();
        adapter = new ArrayAdapter<>(getContext(), R.layout.row_layout, items);
        listView.setAdapter(adapter);

        extendedCalendarView = (ExtendedCalendarView) view.findViewById(R.id.calendarMenu);
        extendedCalendarView.setMonthTextBackgroundColor(R.color.black);
        extendedCalendarView.setOnDayClickListener(new ExtendedCalendarView.OnDayClickListener() {
            @Override
            public void onDayClicked(AdapterView<?> adapter, View view, int position, long id, Day day) {
                selDay = day;
                getEventDetails(day);
            }
        });

        ImageButton addBtn = (ImageButton) view.findViewById(R.id.addEvent);
        addBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (selDay != null)
                    startActivityForResult(new Intent(getContext(), PickRecipe.class), recipeRequest);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                // Get the name of the activity
                String recipeName = listView.getItemAtPosition(position).toString();
                // Use bundles to share data between activities
                Intent intent = new Intent(getActivity(), DisplayRecipe.class);
                Bundle bundle = new Bundle(); //we can use intent.putExtra("recipeName", recipeName); don't need bundle
                bundle.putString("recipeName", recipeName);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        return view;
    }

    private void getEventDetails(Day day) {
        for (Event e : day.getEvents()) {Log.i(TAG, "Event in List: " + e.getTitle());
            adapter.add(e.getTitle());}
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == recipeRequest) {
            Log.i(TAG, "Request == recipeRequest");
            if (resultCode == getActivity().RESULT_OK) {
                Log.i(TAG, "RESULT OKAY");
                String recipe = data.getStringExtra("recipeName");
                ContentValues values = new ContentValues();
                values.put(CalendarProvider.COLOR, Event.COLOR_RED);
                values.put(CalendarProvider.EVENT, recipe);
                int eventJulDay = selDay.getStartDay();
                Log.i(TAG, "Event Date (day/month/year): " + selDay.getDay() + '/' +
                        (selDay.getMonth() + 1) + '/' + selDay.getYear());
                Log.i(TAG, "Event Julian Day: " + eventJulDay);
                Log.i(TAG, "Event Name: " + recipe);
                values.put(CalendarProvider.START_DAY, eventJulDay);
                values.put(CalendarProvider.END_DAY, eventJulDay);
                getActivity().getContentResolver().insert(CalendarProvider.CONTENT_URI, values);
                extendedCalendarView.refreshCalendar();
                adapter.add(recipe);
            }
        }
    }


}