package com.example.josimuddin.emess.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.josimuddin.emess.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OthersInputFragment extends Fragment {


    public OthersInputFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_others_input, container, false);
    }

}
