package com.sharework;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sharework.PartTimeFragment.PartTimeMenu1Fragment;
import com.sharework.PartTimeFragment.PartTimeMenu2Fragment;
import com.sharework.PartTimeFragment.PartTimeMenu3Fragment;
import com.sharework.PartTimeFragment.PartTimeMenu4Fragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;


    private PartTimeMenu1Fragment fragment1;
    private PartTimeMenu2Fragment fragment2;
    private PartTimeMenu3Fragment fragment3;
    private PartTimeMenu4Fragment fragment4;
    private ArrayList <Object> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.activity_main_bottomNavigationView);
        fragment1 = new PartTimeMenu1Fragment();
        fragment2 = new PartTimeMenu2Fragment();
        fragment3 = new PartTimeMenu3Fragment();
        fragment4 = new PartTimeMenu4Fragment();
        list = new ArrayList<>();
        list.add(fragment1);
        list.add(fragment2);
        list.add(fragment3);
        list.add(fragment4);

        getSupportFragmentManager().beginTransaction().add(R.id.activity_main_fragment_container,fragment1).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.activity_main_fragment_container,fragment2).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.activity_main_fragment_container,fragment3).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.activity_main_fragment_container,fragment4).commit();
        HideFragment();
        getSupportFragmentManager().beginTransaction().show(fragment1).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                HideFragment();
                switch(menuItem.getItemId()){
                    case R.id.pt_tab1:
                        getSupportFragmentManager().beginTransaction().show(fragment1).commit();
                        return true;
                    case R.id.pt_tab2:
                        getSupportFragmentManager().beginTransaction().show(fragment2).commit();
                        return true;
                    case R.id.pt_tab3:
                        getSupportFragmentManager().beginTransaction().show(fragment3).commit();
                        return true;
                    case R.id.pt_tab4:
                        getSupportFragmentManager().beginTransaction().show(fragment4).commit();
                        return true;
                }
                return false;
            }
        });
    }

    private void HideFragment(){
        for(Object tmp : list){
            getSupportFragmentManager().beginTransaction().hide((Fragment)tmp).commit();
        }
    }

}
