package com.android.andrewgarver.recipegrabber;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
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
import android.widget.ListView;
import android.widget.Toast;

import com.android.andrewgarver.recipegrabber.extendCalView.CalendarProvider;
import com.android.andrewgarver.recipegrabber.extendCalView.Day;
import com.android.andrewgarver.recipegrabber.extendCalView.Event;
import com.android.andrewgarver.recipegrabber.extendCalView.ExtendedCalendarView;

import java.util.ArrayList;

/**
 * Created by Andrew Garver on 11/2/2015.
 */
public class Menu extends Fragment {
    private static final String TAG = Menu.class.getSimpleName();
    private static final int recipeRequest = 1;

    private ListView list;
    private ExtendedCalendarView extCalendar;
    private Day selDay;
    private ArrayAdapter<String> adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_menu, container, false);
        list = (ListView) view.findViewById(R.id.menuListView);
        ArrayList<String> items = new ArrayList();
        adapter = new ArrayAdapter<>(getContext(), R.layout.row_layout, items);
        list.setAdapter(adapter);

        extCalendar = (ExtendedCalendarView) view.findViewById(R.id.calendarMenu);
        extCalendar.setMonthTextBackgroundColor(R.color.black);
        extCalendar.setOnDayClickListener(new ExtendedCalendarView.OnDayClickListener() {
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

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                // Get the name of the activity
                String recipeName = list.getItemAtPosition(position).toString();
                // Use bundles to share data between activities
                Intent intent = new Intent(getActivity(), DisplayRecipe.class);
                Bundle bundle = new Bundle(); //we can use intent.putExtra("recipeName", recipeName); don't need bundle
                bundle.putString("recipeName", recipeName);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final String toDel = adapter.getItem(position);
                AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
                adb.setTitle("Delete planned recipe: " + toDel + '?');
                adb.setMessage("Are you sure you want to remove this recipe from your menu?");
                adb.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.remove(toDel);
                        getActivity().getContentResolver().delete(CalendarProvider.CONTENT_URI, CalendarProvider.EVENT + "='" + toDel + "'", null);
                        extCalendar.refreshCalendar();
                        Toast.makeText(getContext(), "Deleting from menu", Toast.LENGTH_LONG).show();
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

        return view;
    }

    private void getEventDetails(Day day) {
        adapter.clear();
        for (Event e : day.getEvents())
            adapter.add(e.getTitle());
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
                extCalendar.refreshCalendar();
                adapter.add(recipe);
            }
        }
    }


}