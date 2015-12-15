package com.android.andrewgarver.recipegrabber;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.android.andrewgarver.recipegrabber.extendCalView.CalendarAdapter;
import com.android.andrewgarver.recipegrabber.extendCalView.CalendarProvider;
import com.android.andrewgarver.recipegrabber.extendCalView.Day;
import com.android.andrewgarver.recipegrabber.extendCalView.Event;
import com.android.andrewgarver.recipegrabber.extendCalView.ExtendedCalendarView;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * Displays a calendar with the recipes that are planned
 * <p>
 * Highlights the current day. Implements the <a href="https://github.com/tyczj/ExtendedCalendarView">
 *     ExtendedCalendarView</a> from a third party.
 * Clicking the plus button, lets you add a recipe to the ListView below the calendar.
 * LongClicking deletes event from calendar.
 *
 * @author  Andrew Garver, Landon Jamieson, and Reed Atwood
 * @version 1.0
 * @since   12/10/2015
 */
public class Menu extends Fragment {

    /**
     * Debugging Tag to display LogCat messages for debugging
     */
    private static final String TAG = Menu.class.getSimpleName();

    /**
     * Constant recipeRequest = 1
     */
    private static final int recipeRequest = 1;

    /**
     * Setup variables
     */
    private ListView list;
    private DatabaseAdapter dbHelper;
    private static ExtendedCalendarView extCalendar;
    private Day selDay;
    private static ArrayAdapter<String> adapter;

    /**
     * Sets up the ExtendedCalendarView and the ListView
     *
     * @param inflater The LayoutInflater object that can be used to inflate any
     *                 views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's
     *                  UI should be attached to. The fragment should not add the
     *                  view itself, but this can be used to generate the
     *                  LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_menu, container, false);
        list = (ListView) view.findViewById(R.id.menuListView);
        ArrayList<String> items = new ArrayList();
        adapter = new ArrayAdapter<>(getContext(), R.layout.row_layout, items);
        list.setAdapter(adapter);
        dbHelper = new DatabaseAdapter(getContext());

        extCalendar = (ExtendedCalendarView) view.findViewById(R.id.calendarMenu);
        extCalendar.setMonthTextBackgroundColor(R.color.black);
        extCalendar.setOnDayClickListener(new ExtendedCalendarView.OnDayClickListener() {

            /**
             * Gets the day that was clicked
             *
             * @param adapter Saves the where the item was clicked.
             * @param view The view that was clicked.
             * @param position The position inside the ListView that was clicked.
             * @param id Not used in this function, needed because parent overridden.
             * @param day The current day that is clicked.
             */
            @Override
            public void onDayClicked(AdapterView<?> adapter, View view, int position, long id, Day day) {
                selDay = day;
                getEventDetails(day);
            }
        });

        /**
         * Displays the plus(+) to set it to the onclickListener
         */
        ImageButton addBtn = (ImageButton) view.findViewById(R.id.addEvent);

        addBtn.setOnClickListener(new View.OnClickListener() {

            /**
             * Starts the PickRecipe activity only if a day has been selected
             *
             * @param v The view that was clicked. Labeled v.
             */
            public void onClick(View v) {

                /**
                 * If plus button clicked without selecting a day it won't do anything
                 */
                if (selDay != null)
                    startActivityForResult(new Intent(getContext(), PickRecipe.class), recipeRequest);
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            /**
             * Opens the DisplayRecipe activity to be display the recipe
             *
             * @param parent Saves the where the item was clicked.
             * @param view The view that was clicked.
             * @param position The position inside the ListView that was clicked.
             * @param id Not used in this function, needed because parent overridden.
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                /**
                 * Get the name of the activity
                 */
                String recipeName = list.getItemAtPosition(position).toString();

                /**
                 * Use Extras on intent to share information
                 */
                Intent intent = new Intent(getActivity(), DisplayRecipe.class);
                intent.putExtra("recipeName", recipeName);
                startActivity(intent);
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            /**
             * On Item Long Click displays message to delete the recipe
             *
             * @param parent Saves the where the item was clicked
             * @param view The view that was clicked.
             * @param position The position inside the ListView that was clicked.
             * @param id Not used in this function, needed because parent overridden
             * @return true Consumes the long click
             */
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                /**
                 * This must be final since it is accessed from an inner class
                 */
                final String toDel = adapter.getItem(position);

                /**
                 * Message to be displayed when deleting a recipe
                 */
                AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
                adb.setTitle("Delete planned recipe: " + toDel + '?');
                adb.setMessage("Are you sure you want to remove this recipe from your menu?");

                adb.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    /**
                     * Display message to delete recipe
                     *
                     * @param dialog Dialog that was started
                     * @param which Which button that was clicked
                     */
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.remove(toDel);
                        getActivity().getContentResolver().delete(CalendarProvider.CONTENT_URI,
                                CalendarProvider.EVENT + "='" + toDel + "' and " +
                                        CalendarProvider.START_DAY + "='" + selDay.getStartDay() + "'", null);
                        dbHelper.removePlannedRecipe(toDel);
                        refreshMenu();
                        Toast.makeText(getContext(), "Deleting from menu", Toast.LENGTH_LONG).show();
                    }
                });

                adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    /**
                     * Display message to cancel deleting recipe
                     *
                     * @param dialog Dialog that was started
                     * @param which Which button that was clicked
                     */
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                /**
                 * Displays box with the delete and cancel message
                 */
                AlertDialog ad = adb.create();
                ad.show();

                /**
                 * Consumes the click so it wont continue to click
                 */
                return true;
            }
        });

        return view;
    }

    /**
     * Gets the events and adds the name of the recipes to the ListView
     *
     * @param  day The current day that is clicked.
     */
    private void getEventDetails(Day day) {

        /**
         * Clear the ListView so there is no duplication
         */
        adapter.clear();

        /**
         * Adds the names of all recipes
         */
        for (Event e : day.getEvents())
            adapter.add(e.getTitle());
    }

    /**
     * Allows another class to refresh the menu. Especially if contents were deleted
     */
    public static void refreshMenu() {
        extCalendar.refreshCalendar();
        adapter.clear();
    }

    /**
     * This is what gets called when the Activity that was called finishes
     *
     * @deprecated Time was deprecated and changed by use to mathematical formula
     *
     * @param requestCode Code that was sent with the activity
     * @param resultCode If the result was okay
     * @param data The intent that was sent with the result
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        /**
         * If it was a request for the recipe then if it was okay we will add the recipe to the
         * correct place on the calendar
         */
        if (requestCode == recipeRequest) {
            Log.i(TAG, "Request == recipeRequest");
            if (resultCode == getActivity().RESULT_OK) {

                Log.i(TAG, "RESULT OKAY");
                String recipe = data.getStringExtra("recipeName");
                int eventJulDay = selDay.getStartDay();
                Calendar now = Calendar.getInstance();
                if (eventJulDay >= CalendarAdapter.julianDay(now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH))) {
                    Log.i(TAG, "Event added after today");
                    int recipeId = dbHelper.getRecipeId(recipe);
                    ArrayList<Ingredient> recIngredients = dbHelper.getRecipeIngredientsVerbose(recipeId);
                    ArrayList<Ingredient> alreadyPlanned = dbHelper.getPlannedIngredients();
                    ArrayList<Ingredient> inCupboard = dbHelper.getAllIngredientsVerbose();
                    boolean inPlanned = false;
                    boolean addToShopping = true;

                    for (Ingredient i : recIngredients) {
                        //set the ingredient id
                        i.setId(dbHelper.getRecipeIngredientID(i.getName(), recipeId));
                        Log.i(TAG, "Checking Ingredient: " + i.getName());
                        //check if each ingredient is already planned.
                        for (Ingredient planned : alreadyPlanned) {
                            Log.i(TAG, "Checking against planned " + planned.getName());
                            if (planned.getName().equalsIgnoreCase(i.getName()) // the same ingredient
                                    && planned.getMetric().equals(i.getMetric())) {
                                Log.i(TAG,"Ingredient is planned.");
                                inPlanned = true;
                                //set required
                                i.setRequired(i.getQuantity() + planned.getRequired());
                                Log.i(TAG, "Planned Quanity is: " + planned.getQuantity());
                                if (planned.getQuantity() == 0)
                                    for (Ingredient cupboard : inCupboard) {
                                        Log.i(TAG, "Checking planned required against " + cupboard.getName() + " in cupboard");
                                        if (cupboard.getName().equalsIgnoreCase(i.getName()) // the same ingredient
                                                && cupboard.getMetric().equals(i.getMetric())) {
                                            Log.i(TAG, "Planned item is in cupboard");
                                            int diff = i.getRequired() - cupboard.getQuantity();
                                            Log.i(TAG, "The difference between required and in cupboard is " + diff);
                                            if (diff > 0)
                                                i.setQuantity(diff);
                                            else {
                                                Log.i(TAG, "More in cupboard.");
                                                i.setQuantity(0);
                                                addToShopping = false;
                                            }
                                            break;
                                        }
                                    }
                                Log.i(TAG, "Add to shopping? " + addToShopping);
                                //add the new amount to the shoppingList if more is needed.
                                if (addToShopping)
                                    dbHelper.addToShoppingList(i.getName(), i.getQuantityString(), i.getMetric(), true);
                                Log.i(TAG, "Delete old and make new planned entry.");
                                i.setQuantity(i.getQuantity() + planned.getQuantity()); //set new quantity
                                dbHelper.deletePlannedIngredient(planned); //delete old planned
                                dbHelper.addPlannedIngredient(i); //insert the new planned ingredient
                                break;
                            }
                    }

                        if (!inPlanned) {
                            i.setRequired(i.getQuantity());
                            Log.i(TAG, "Not Planned. Set required to Quanity " + i.getRequired());
                            for (Ingredient cupboard : inCupboard)
                                if (cupboard.getName().equalsIgnoreCase(i.getName()) // the same ingredient
                                        && cupboard.getMetric().equals(i.getMetric())) {
                                    int diff = i.getRequired() - cupboard.getQuantity();
                                    Log.i(TAG, "NewPlanned Item contained in Cupboard, the difference " + diff);
                                    if (diff > 0)
                                        i.setQuantity(diff);
                                    else {
                                        i.setQuantity(0);
                                        addToShopping = false;
                                        Log.i(TAG, "More in cupboard.");
                                    }
                                    break;
                                }
                            Log.i(TAG, "Add to shopping? " + addToShopping);
                            if (addToShopping)
                                dbHelper.addToShoppingList(i.getName(), i.getQuantityString(), i.getMetric(), true);
                            dbHelper.addPlannedIngredient(i); //insert the new planned ingredient
                        }
                    }
                }

                if(ShoppingList.isActive)
                    ShoppingList.refreshShoppingList();
                /**
                 * getStartDay was modified from ExtendedCalendarView because Time was deprecated
                 */
                ContentValues values = new ContentValues();
                values.put(CalendarProvider.COLOR, Event.COLOR_RED);
                values.put(CalendarProvider.EVENT, recipe);



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