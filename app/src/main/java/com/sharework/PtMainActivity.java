package com.sharework;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.sharework.PartTimeFragment.PartTimeMenu1Fragment;
import com.sharework.PartTimeFragment.PartTimeMenu2Fragment;
import com.sharework.PartTimeFragment.PartTimeMenu3Fragment;
import com.sharework.PartTimeFragment.PartTimeMenu4Fragment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class PtMainActivity extends AppCompatActivity {

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

        checkPermissions();
    }

    private void HideFragment(){
        for(Object tmp : list){
            getSupportFragmentManager().beginTransaction().hide((Fragment)tmp).commit();
        }
    }
    private void initView(){
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

    //권한관련 함수------------------------------------------------------
    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            initView();
            // 권한 승인이 필요없을 때 실행할 함수
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(getApplicationContext(), "권한 허용을 하지 않으면 서비스를 이용할 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
    };


    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= 23){ // 마시멜로(안드로이드 6.0) 이상 권한 체크
            TedPermission.with(getApplicationContext())
                    .setPermissionListener(permissionlistener)
                    .setRationaleMessage("위치 설정을 위해서 접근 권한이 필요합니다")
                    .setDeniedMessage("앱에서 요구하는 권한설정이 필요합니다...\n [설정] > [권한] 에서 사용으로 활성화해주세요.")
                    .setPermissions(new String[] { Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION})
                    .check();

        } else {
            initView();
        }
    }
}
