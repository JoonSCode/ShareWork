package com.sharework.partTimeFragment;

import android.os.Bundle;
import android.util.Log;
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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("프래그먼트", "2만들어짐");

        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_part_time_menu2, container, false);
        return viewGroup;
    }


}
