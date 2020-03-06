package com.sharework;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kakao.auth.AccessTokenCallback;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.auth.authorization.accesstoken.AccessToken;
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
import com.sharework.Data.Users;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private static OAuthLogin mOAuthLoginInstance;
    private static Context mContext;

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

    //파이어베이스
    final FirebaseFirestore db = FirebaseFirestore.getInstance();

    Users user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getAppKeyHash();

        initData();
        initView();
    }
    //초기화-------------------------------------------------
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

        user = new Users();
    }

    private void initView() {
        mCustomKakaoButton = findViewById(R.id.activity_login_btn_kakao);
        mKakaoLoginButton = findViewById(R.id.btn_kakao_login);
        mCustomKakaoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("카카오 로그인", "버튼 클릭");
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
//네이버 로그인-----------------------------------------
private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
    @Override
    public void run(boolean success) {
        if (success) {
            redirectMainActivity(1);
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
            requestMe();
            //redirectMainActivity();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Log.d("카카오 로그인", "로그인 실패");
        }
    }
    //카카오 정보 받는 함수
    private void requestMe() {
        UserManagement.getInstance().me(new MeV2ResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Log.d("카카오 로그인",message);
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                setContentView(R.layout.activity_login);
            }

            @Override
            public void onSuccess(MeV2Response response) {
                Log.d("카카오 로그인 id", Long.toString(response.getId()));//요 정보를 회원 식별자로 사용하면 될거같아요
                final String USER_KEY = Long.toString(response.getId());
                user.setId(USER_KEY);

                UserAccount kakaoAccount = response.getKakaoAccount();
                if (kakaoAccount != null) {
                    String email = kakaoAccount.getEmail();
                    //user.setEmail(email);
                    if (email != null) {
                        Log.d("카카오 로그인 email", email);
                        user.setEmail(email);
                    } else if (kakaoAccount.emailNeedsAgreement() == OptionalBoolean.TRUE) {
                        handleScopeError(kakaoAccount);
                        return;
                        // 동의 요청 후 이메일 획득 가능
                        // 단, 선택 동의로 설정되어 있다면 서비스 이용 시나리오 상에서 반드시 필요한 경우에만 요청해야 합니다.
                    } else {
                        // 이메일 획득 불가
                    }

                    Profile profile = kakaoAccount.getProfile();
                    if (profile != null) {
                        user.setName(profile.getNickname());
                        Log.d("카카오 로그인 nickname", profile.getNickname());
                        Logger.d("profile image: " + profile.getProfileImageUrl());
                        Logger.d("thumbnail image: " + profile.getThumbnailImageUrl());
                    } else if (kakaoAccount.profileNeedsAgreement() == OptionalBoolean.TRUE) {
                        // 동의 요청 후 프로필 정보 획득 가능
                    } else {
                        // 프로필 획득 불가
                    }
                }



                DocumentReference userRef = db.collection("Users").document(USER_KEY); //유저 확인
                userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) { //가입 이미 되어있음
                                Log.d("로그인", "기존유저");
                                user = task.getResult().toObject(Users.class);
                                redirectMainActivity(user.getType());
                            } else {//안되어있음
                                Log.d("로그인", "신규유저");
                                redirectSignUp2Activity();
                            }
                        } else {
                            Log.d("에러", "get failed with ", task.getException());
                        }
                    }
                });
            }

        });
    }
    private void handleScopeError(UserAccount account) {
        List<String> neededScopes = new ArrayList<>();
        if (account.needsScopeAccountEmail()) {
            neededScopes.add("account_email");
        }
        if (account.needsScopeGender()) {
            neededScopes.add("gender");
        }
        Session.getCurrentSession().updateScopes(this, neededScopes, new
                AccessTokenCallback() {
                    @Override
                    public void onAccessTokenReceived(AccessToken accessToken) {
                        requestMe();
                    }

                    @Override
                    public void onAccessTokenFailure(ErrorResult errorResult) {
                        // 동의 얻기 실패
                    }
                });
    }




    //기존 유저: 유저 타입을 받아 해당하는 화면으로 전환
    protected void redirectMainActivity(int type) {
        final Intent intent;

        HashMap<String , Object> updates = new HashMap<>();//최근 접속시간 갱신
        updates.put("last_login_at", Calendar.getInstance().getTime());
        db.collection("Users").document(user.getId()).update(updates);

        if(type == 0) {//알바
            intent = new Intent(this, PtMainActivity.class);
        }
        else {
            intent = new Intent(this, BsMainActivity.class);
        }
        Log.d("로그인", "1");
        startActivity(intent);
        Log.d("로그인", "2");
        finish();
    }
//신규유저 로그인 유저 타입을 받기 위해 이동
    protected void redirectSignUp2Activity() {
        Intent intent = new Intent(this, SignUp2Activity.class);
        intent.putExtra("USER", user);
        startActivity(intent);
        finish();
    }





    //카카오 로그인 초기에 key hash값 추가 필요--------------------------------

    private void getAppKeyHash() {
        try {
            @SuppressLint("PackageManagerGetSignatures") PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.e("Hash key", something);
            } }
        catch (Exception e) {
            // TODO Auto-generated catch block Log.e("name not found", e.toString());
        }
    }



}
