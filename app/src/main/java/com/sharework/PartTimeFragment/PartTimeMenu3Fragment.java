package com.sharework.PartTimeFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sharework.R;

import androidx.fragment.app.Fragment;



public class PartTimeMenu3Fragment extends Fragment {
    ViewGroup viewGroup;

    public PartTimeMenu3Fragment() {
        // Required empty public constructor
    }


    public static PartTimeMenu3Fragment newInstance(String param1, String param2) {
        PartTimeMenu3Fragment fragment = new PartTimeMenu3Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_part_time_menu3, container, false);
        return viewGroup;
    }

}