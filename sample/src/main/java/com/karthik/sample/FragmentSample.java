package com.karthik.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.karthik.pretty_annotation.Pretty;

/**
 * Created by karthikrk on 25/03/18.
 */

public class FragmentSample extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sample,container,false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        intializeFragment(getArguments());
    }

    @Pretty(headerName = "FragmentExample")
    public void intializeFragment(Bundle args){
        /**
         * intialize the fragment with arguments.
         */
    }
}
