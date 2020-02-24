package com.sharework;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.List;

public class SelectUserTypeActivity extends AppCompatActivity {
    private Button mUserPt;
    private Button mUserBs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user_type);


        mUserBs = findViewById(R.id.activity_user_type_bs);
        mUserPt = findViewById(R.id.activity_user_type_pt);

        mUserPt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissions();
            }
        });
    }
    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Intent mIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mIntent);
            // 권한 승인이 필요없을 때 실행할 함수
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(SelectUserTypeActivity.this, "권한 허용을 하지 않으면 서비스를 이용할 수 없습니다.", Toast.LENGTH_SHORT).show();
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
            Intent mIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mIntent);
        }
    }


}
