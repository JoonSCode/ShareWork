package com.sharework;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.Profile;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.util.OptionalBoolean;
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

    private Button mCustomKakaoButton;//카카오 용
    private LoginButton mKakaoLoginButton;
    private SessionCallback callback;      //콜백 선언

    private Button mSignUpButton;
    private Button mSignInButton;

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


        mOAuthLoginButton = findViewById(R.id.buttonOAuthLoginImg);
        mOAuthLoginButton.setOAuthLoginHandler(mOAuthLoginHandler);
        mCustomNaverButton = findViewById(R.id.activity_login_btn_naver);
        mCustomNaverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOAuthLoginButton.performClick();
            }
        });

        mSignUpButton = findViewById(R.id.activity_login_btn_sign_up);
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });
        mSignInButton = findViewById(R.id.activity_login_btn_login);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PopupSignInActivity.class);
                startActivity(intent);
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
            setContentView(R.layout.activity_login);
        }
    }
    private void requestMe() {
        UserManagement.getInstance().me(new MeV2ResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Logger.d(message);
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                setContentView(R.layout.activity_login);
            }

            @Override
            public void onSuccess(MeV2Response response) {
                Logger.d("user id : " + response.getId());//요 정보를 회원 식별자로 사용하면 될거같아요

                UserAccount kakaoAccount = response.getKakaoAccount();
                if (kakaoAccount != null) {
                    String email = kakaoAccount.getEmail();
                    if (email != null) {
                        Logger.d("email: " + email);
                    } else if (kakaoAccount.emailNeedsAgreement() == OptionalBoolean.TRUE) {
                        // 동의 요청 후 이메일 획득 가능
                        // 단, 선택 동의로 설정되어 있다면 서비스 이용 시나리오 상에서 반드시 필요한 경우에만 요청해야 합니다.
                    } else {
                        // 이메일 획득 불가
                    }

                    Profile profile = kakaoAccount.getProfile();
                    if (profile != null) {
                        token = Long.toString(response.getId());//유저 정보들
                        name = profile.getNickname();
                        Logger.d("nickname: " + profile.getNickname());
                        Logger.d("profile image: " + profile.getProfileImageUrl());
                        Logger.d("thumbnail image: " + profile.getThumbnailImageUrl());
                    } else if (kakaoAccount.profileNeedsAgreement() == OptionalBoolean.TRUE) {
                        // 동의 요청 후 프로필 정보 획득 가능
                    } else {
                        // 프로필 획득 불가
                    }
                }
                redirectMainActivity();
            }

        });
    }
    protected void redirectMainActivity() {
        final Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
