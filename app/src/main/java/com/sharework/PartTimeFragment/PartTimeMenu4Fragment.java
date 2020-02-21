package com.sharework.PartTimeFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sharework.R;

import androidx.fragment.app.Fragment;

public class PartTimeMenu4Fragment extends Fragment {
    ViewGroup viewGroup;

    public PartTimeMenu4Fragment() {
        // Required empty public constructor
    }
    public static PartTimeMenu4Fragment newInstance(String param1, String param2) {
        PartTimeMenu4Fragment fragment = new PartTimeMenu4Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_part_time_menu4, container, false);
        return viewGroup;
    }

}
