package com.sharework.function;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sharework.data.Business;
import com.sharework.data.Users;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Server {

    final private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static Users user;
    private boolean isExist = true;
    private ArrayList<Business> businessArrayList;
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

    public void RunBsAsync(){
        new BusinessAsync().execute();
    }

    public class BusinessAsync extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            getBusiness();
            while(businessArrayList == null){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    public void getBusiness(){
        Query query = db.collection("Business").whereEqualTo("user_id", user.getId()); //유저 확인
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("사업장", "Listen failed.", e);
                    return;
                }
                businessArrayList =  new ArrayList<>();
                for (QueryDocumentSnapshot doc : value)
                   businessArrayList.add(doc.toObject(Business.class));

                Log.d("사업장", "가져옴");
            }
        });
    }







}
