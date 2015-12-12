package com.android.andrewgarver.recipegrabber;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

/**
 *  Main Activity for the start of the app
 *
 *
 *
 * @author  Andrew Garver, Landon Jamieson, and Reed Atwood
 * @version 1.0
 * @since   12/10/2015
 */
public class ActivityMain extends AppCompatActivity {

    /**
     * Debugging Tag to display LogCat messages for debugging
     */
    private static final String TAG = ActivityMain.class.getSimpleName();

    /**
     *
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    /**
     * The SQLiteOpenHelper object that will facilitate database queries
     */
    DatabaseAdapter dbHelper;
    public static SQLiteDatabase database;


    /**
     *
     *
     * @param savedInstanceState save the activity for reopening
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_main);

        /**
         *
         */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        /**
         *
         */
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        /**
         *
         */
        Log.i("Main", "here");
        dbHelper = new DatabaseAdapter(this);
        database = dbHelper.helper.getWritableDatabase();
        Log.i("Main", "after getWriteableDatabase");

    }

    /**
     * Inflates the menu and adds items to to the action bar if present
     *
     * @param menu
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    /**
     * Handles action bar item clicks
     *
     * Action bar clicks open About Page
     *   automatically handle clicks on the Home/Up button, so long
     *   as you specify a parent activity in AndroidManifest.xml.
     *
     * @param item About Page menu item
     * @return true if action bar about is click and opened otherwise
     *           super.onOptionsItemSelected(item)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        /*
         * Save the Menu items id so that we can open the correct page
         */
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            startActivity(new Intent(this, About_page.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

















    /*********************** May need to be its own java file





    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     *
     * @author  Andrew Garver, Landon Jamieson, and Reed Atwood
     * @version 1.0
     * @since   12/10/2015
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        /**
         * Sets the FragmentManager to the super class
         *
         * @param fm FragmentManager
         */
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Gets the Items given the position
         *
         * @param position which tab you are on
         * @return Cookbook, Menu, Cupboard, or ShoppingList object depending on the
         *         position passed in
         */
        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0:
                    return new Cookbook();
                case 1:
                    return new com.android.andrewgarver.recipegrabber.Menu();
                case 2:
                    return new Cupboard();
                default:
                    return new ShoppingList();
            }
        }

        /**
         * Gets the number of tabs and pages
         *
         * @return 4 - the number of tabs and pages.
         */
        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }

        /**
         * Gets the title of each page
         *
         * @param position which page you are on
         * @return Cookbook, Menu, My Cupboard, Shopping List, or null depending
         *           on the position passed in
         */
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Cookbook";
                case 1:
                    return "Menu";
                case 2:
                    return "My Cupboard";
                case 3:
                    return "Shopping List";
            }
            return null;
        }
    }





    /****
     *                        ARE WE EVEN USING THIS CLASS??????????????
     *
     */



    /**
     * A placeholder fragment containing a simple view.
     *
     *
     *
     * @author  Andrew Garver, Landon Jamieson, and Reed Atwood
     * @version 1.0
     * @since   12/10/2015
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment. "Constant"
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         *   number.
         *
         * @param sectionNumber
         * @return fragment
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }


        /**
         * DESCRIPTION
         *
         * @param inflater
         * @param container
         * @param savedInstanceState save the activity for reopening
         * @return  rootView
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_activity_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }
}
