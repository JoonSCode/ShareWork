package com.sharework;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sharework.PartTimeFragment.PartTimeMenu1Fragment;
import com.sharework.PartTimeFragment.PartTimeMenu2Fragment;
import com.sharework.PartTimeFragment.PartTimeMenu3Fragment;
import com.sharework.PartTimeFragment.PartTimeMenu4Fragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;


    private PartTimeMenu1Fragment fragment1;
    private PartTimeMenu2Fragment fragment2;
    private PartTimeMenu3Fragment fragment3;
    private PartTimeMenu4Fragment fragment4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.activity_main_bottomNavigationView);
        fragment1 = new PartTimeMenu1Fragment();
        fragment2 = new PartTimeMenu2Fragment();
        fragment3 = new PartTimeMenu3Fragment();
        fragment4 = new PartTimeMenu4Fragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.activity_main_fragment_container,fragment1).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.pt_tab1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.activity_main_fragment_container,fragment1).commit();
                        return true;
                    case R.id.pt_tab2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.activity_main_fragment_container,fragment2).commit();
                        return true;
                    case R.id.pt_tab3:
                        getSupportFragmentManager().beginTransaction().replace(R.id.activity_main_fragment_container,fragment3).commit();
                        return true;
                    case R.id.pt_tab4:
                        getSupportFragmentManager().beginTransaction().replace(R.id.activity_main_fragment_container,fragment4).commit();
                        return true;
                }
                return false;
            }
        });

    }
}
