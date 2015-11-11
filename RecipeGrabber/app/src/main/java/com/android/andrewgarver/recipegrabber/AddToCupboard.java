package com.android.andrewgarver.recipegrabber;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class AddToCupboard extends AppCompatActivity {

    int addMoreLoc = 5;
    int btnMar = 19;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_cupboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();
    }

    public void init() {
        final RelativeLayout addTCLayout = (RelativeLayout)findViewById(R.id.addTClayout);
        final ImageButton add = (ImageButton) findViewById(R.id.addMore);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputField theField = new InputField(v.getContext());
                RelativeLayout.LayoutParams myParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                myParams.addRule(RelativeLayout.BELOW, addTCLayout.getChildAt(addMoreLoc).getId());
                addTCLayout.addView(theField, myParams);
                addMoreLoc += 3;

                btnMar += 100;
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)add.getLayoutParams();
                params.setMargins(0, btnMar, 0, 0); //substitute parameters for left, top, right, bottom
                add.setLayoutParams(params);
            }
        });
    }
}
