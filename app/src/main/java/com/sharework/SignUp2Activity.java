package com.sharework;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sharework.Data.Users;

import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;

public class SignUp2Activity extends AppCompatActivity {

    private Button mUserPt;
    private Button mUserBs;
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Users user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user_type);

        user = (Users)getIntent().getSerializableExtra("USER");
        mUserBs = findViewById(R.id.activity_user_type_bs);
        mUserPt = findViewById(R.id.activity_user_type_pt);

        mUserPt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUpComplete(0);
            }
        });
        mUserBs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUpComplete(1);
            }
        });
    }

    private void SignUpComplete(int type){
        Intent intent;
        DocumentReference docRef = db.collection("Users").document(user.getId());
        user.setType(type);
        user.setCreated_at(Calendar.getInstance().getTime());
        user.setLast_login_at(Calendar.getInstance().getTime());
        docRef.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("로그인", "타입까지 설정하여 회원가입 완료");
            }
        });
        if(type == 0){
            intent = new Intent(this, PtMainActivity.class);
            intent.putExtra("USER", user);
        }
        else{
            intent = new Intent(this, BsMainActivity.class);
            intent.putExtra("USER", user);
        }
        startActivity(intent);
        finish();
    }


}
