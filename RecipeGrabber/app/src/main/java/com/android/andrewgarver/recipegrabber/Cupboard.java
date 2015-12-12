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
 *
 *
 *
 *
 * @author  Andrew Garver, Landon Jamieson, and Reed Atwood
 * @version 1.0
 * @since   11/2/2015
 */
public class Cupboard extends Fragment {

    /**
     * Debugging Tag to display LogCat messages for debugging
     */
    private static final String TAG = Cupboard.class.getSimpleName();

    /**
     * Constant ingredCode = 2
     */
    private static final int ingredCode = 2;

    /**
     *
     */
    private DatabaseAdapter dbHelper;
    private ArrayAdapter<String> adapter;
    private ListView list;

    /**
     *
     *
     *
     * @param inflater The LayoutInflater object that can be used to inflate any
     *                 views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's
     *                  UI should be attached to. The fragment should not add the
     *                  view itself, but this can be used to generate the
     *                  LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /**
         *
         */
        dbHelper = new DatabaseAdapter(getActivity());

        /**
         *
         */
        ArrayList<String> items = dbHelper.getAllIngredients();

        /**
         *
         */
        adapter = new ArrayAdapter<>(getContext(), R.layout.row_layout, items);

        /**
         *
         */
        View view = inflater.inflate(R.layout.frag_cupboard, container, false);
        list = (ListView) view.findViewById(R.id.listView);
        list.setAdapter(adapter);

        /**
         *
         */
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            /**
             *
             *
             *
             * @param parent
             * @param view
             * @param position
             * @param id
             * @return true
             */
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                /**
                 *
                 */
                final String preSplit = adapter.getItem(position);
                final String toDel = preSplit.split(" - ")[0];

                /**
                 *
                 */
                AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
                adb.setTitle("Delete ingredient: " + toDel + '?');
                adb.setMessage("Are you sure you want to remove this ingredient from your cupboard?");

                /**
                 *
                 */
                adb.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    /**
                     *
                     *
                     *
                     * @param dialog
                     * @param which
                     */
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.remove(preSplit);
                        dbHelper.deleteIngredient(toDel);
                        Toast.makeText(getContext(), "Deleting ingredient", Toast.LENGTH_LONG).show();
                    }
                });

                /**
                 *
                 */
                adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    /**
                     *
                     *
                     * @param dialog
                     * @param which
                     */
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                /**
                 *
                 */
                AlertDialog ad = adb.create();
                ad.show();
                return true;
            }
        });

        /**
         *
         */
        ImageButton addBtn = (ImageButton) view.findViewById(R.id.addTo);
        addBtn.setOnClickListener(new View.OnClickListener() {
            /**
             *
             *
             * @param v is a view
             */
            public void onClick(View v) {
                startActivityForResult(new Intent(getContext(), AddToCupboard.class), ingredCode);
            }
        });

        return view;
    }

    /**
     *
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        /**
         *
         */
        if (requestCode == ingredCode) {

            /**
             *
             */
            if (resultCode == getActivity().RESULT_OK) {
                ArrayList<String> ingredList = data.getStringArrayListExtra("results");
                adapter.addAll(ingredList);
            }
        }
    }
}
