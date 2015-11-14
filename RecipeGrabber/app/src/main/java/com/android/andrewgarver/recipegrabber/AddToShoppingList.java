package com.android.andrewgarver.recipegrabber;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class AddToShoppingList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_shoppinglist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //this must be final since it is accessed from an inner class
        final ImageButton add = (ImageButton) findViewById(R.id.addMore);

        //need to add the listener to add an extra row of input fields
        add.setOnClickListener(new View.OnClickListener() {
            @Override

            //When they click the + button, they will get another row for input.
            public void onClick(View view) {

                //We use the context of the button, since it is on the activity we are using
                LayoutInflater vi = (LayoutInflater) add.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = vi.inflate(R.layout.input_field, null); //This is the layout of the new row

                //if we need to change the id we would do it here

                //There is an (at first) empty container LinearLayout that we insert these into
                ((ViewGroup) findViewById(R.id.container)).addView(v);
            }
        });
    }

}
