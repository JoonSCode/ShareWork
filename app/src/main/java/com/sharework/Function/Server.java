package com.sharework.Function;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sharework.Data.Users;

import androidx.annotation.NonNull;

public class Server {

    final private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static Users user;
    private boolean isExist = true;

    public Server() {
    }


    public void loadUser(String KEY){
        DocumentReference userRef = db.collection("Users").document(KEY); //유저 확인
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) { //가입 이미 되어있음
                        Log.d("로그인", "기존유저");
                        user = task.getResult().toObject(Users.class);
                        isExist = true;
                    } else {//안되어있음
                        Log.d("로그인", "신규유저");
                        isExist = false;
                    }
                } else {
                    Log.d("에러", "get failed with ", task.getException());
                }
            }
        });
    }

    public boolean isUserExist(){
        return isExist;
    }

    public Users getUsers(){
        return user;
    }

    public static void setUser(Users user) {
        Server.user = user;
    }

    public void updateUser(){
        db.collection("Users").document(user.getId()).set(user);
    }


}
