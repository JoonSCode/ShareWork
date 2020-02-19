package com.sharework;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.usermgmt.LoginButton;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private static OAuthLogin mOAuthLoginInstance;
    private static Context mContext;

    /**
     * client 정보를 넣어준다.
     */
    private static String OAUTH_CLIENT_ID = "fFL3jEXaGGiMSQrJxFgY";
    private static String OAUTH_CLIENT_SECRET = "WV6aaYZZXH";
    private static String OAUTH_CLIENT_NAME = "ShareWork";


    private OAuthLoginButton mOAuthLoginButton;//네이버 용
    private Button mCustomNaverButton;

    private Button mCustomKakaoButton;
    private LoginButton mKakaoLoginButton;
    private SessionCallback callback;      //콜백 선언
    //유저프로필
    String token = "";
    String name = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initData();
        initView();
    }
    //초기화
    private void initData() {
        //네이버 용
        mOAuthLoginInstance = OAuthLogin.getInstance();

        mContext = getApplicationContext();
        mOAuthLoginInstance.showDevelopersLog(true);
        mOAuthLoginInstance.init(mContext, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME);
        //카카오 용
        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        Session.getCurrentSession().checkAndImplicitOpen();
    }

    private void initView() {
        mCustomKakaoButton = findViewById(R.id.activity_login_btn_kakao);
        mKakaoLoginButton = findViewById(R.id.btn_kakao_login);
        mCustomKakaoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mKakaoLoginButton.performClick();
            }
        });


        mOAuthLoginButton = (OAuthLoginButton) findViewById(R.id.buttonOAuthLoginImg);
        mOAuthLoginButton.setOAuthLoginHandler(mOAuthLoginHandler);
        mCustomNaverButton = findViewById(R.id.activity_login_btn_naver);
        mCustomNaverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOAuthLoginButton.performClick();
            }
        });

    }
//네이버 로그인
private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
    @Override
    public void run(boolean success) {
        if (success) {
            redirectMainActivity();
        } else {

            Toast.makeText(mContext, "네이버 로그인 실패", Toast.LENGTH_SHORT).show();
        }
    };
};



    //카카오 로그인
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            redirectMainActivity();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if(exception != null) {
                Logger.e(exception);
            }
        }
    }

    protected void redirectMainActivity() {
        final Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
