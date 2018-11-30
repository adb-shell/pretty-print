package com.karthik.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.karthik.pretty_annotation.Pretty;

/**
 * Created by karthikrk on 25/03/18.
 */

public class SecondActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        intializeActivity(getIntent().getExtras());
    }

    @Pretty(headerName = "SecondActivity")
    private void intializeActivity(Bundle args) {
        /**
         * Intailize the activity with args.
         */
    }
}
