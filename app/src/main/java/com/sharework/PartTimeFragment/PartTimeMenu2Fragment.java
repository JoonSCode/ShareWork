package com.sharework.PartTimeFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sharework.R;

import androidx.fragment.app.Fragment;


public class PartTimeMenu2Fragment extends Fragment {

    ViewGroup viewGroup;

    public PartTimeMenu2Fragment() {
        // Required empty public constructor
    }


    public static PartTimeMenu2Fragment newInstance(String param1, String param2) {
        PartTimeMenu2Fragment fragment = new PartTimeMenu2Fragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_part_time_menu2, container, false);
        return viewGroup;
    }


}
