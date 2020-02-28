package com.sharework;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.sharework.Data.JoinData;
import com.sharework.Data.JoinResponse;
import com.sharework.Data.RetrofitClient;
import com.sharework.Data.ServiceApi;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    private Button mConfirmBtn;
    private ImageButton mBackBtn;

    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mNameView;

    private ServiceApi service;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mBackBtn = findViewById(R.id.activity_sign_up_btn_back);
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mConfirmBtn = findViewById(R.id.activity_sign_up_btn_confirm);
        mConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptJoin();
            }
        });


        mEmailView = findViewById(R.id.sign_up_email);
        mPasswordView = findViewById(R.id.sign_up_pwd);
        mNameView = findViewById(R.id.sign_up_name);

        service = RetrofitClient.getClient().create(ServiceApi.class);


    }


    private void attemptJoin() {
        mNameView.setError(null);
        mEmailView.setError(null);
        mPasswordView.setError(null);

        String name = mNameView.getText().toString();
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // 패스워드의 유효성 검사
        if (password.isEmpty()) {
            mEmailView.setError("비밀번호를 입력해주세요.");
            focusView = mEmailView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mPasswordView.setError("6자 이상의 비밀번호를 입력해주세요.");
            focusView = mPasswordView;
            cancel = true;
        }

        // 이메일의 유효성 검사
        if (email.isEmpty()) {
            mEmailView.setError("이메일을 입력해주세요.");
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError("@를 포함한 유효한 이메일을 입력해주세요.");
            focusView = mEmailView;
            cancel = true;
        }

        // 이름의 유효성 검사
        if (name.isEmpty()) {
            mNameView.setError("이름을 입력해주세요.");
            focusView = mNameView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            startJoin(new JoinData(name, email, password));
        }
    }

    private void startJoin(JoinData data) {
        service.userJoin(data).enqueue(new Callback<JoinResponse>() {
            @Override
            public void onResponse(Call<JoinResponse> call, Response<JoinResponse> response) {
                JoinResponse result = response.body();
                Toast.makeText(SignUpActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();

                final Intent intent = new Intent(getApplicationContext(), SignUp2Activity.class);
                startActivity(intent);
                finish();

                if (result.getCode() == 200) {
                    finish();
                }
            }

            @Override
            public void onFailure(Call<JoinResponse> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, "회원가입 에러 발생", Toast.LENGTH_SHORT).show();
                Log.e("회원가입 에러 발생", t.getMessage());
            }
        });
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }


}
