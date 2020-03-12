package com.sharework;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
import com.sharework.Function.Server;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private static OAuthLogin mOAuthLoginInstance;
    private static Context mContext;

    private static String OAUTH_CLIENT_ID = "CLIENT_ID";
    private static String OAUTH_CLIENT_SECRET = "CLIENT_SECRET";
    private static String OAUTH_CLIENT_NAME = "ShareWork";


    private OAuthLoginButton mOAuthLoginButton;//네이버 용
    private Button mCustomNaverButton;

    private Button mCustomKakaoButton;//카카오 용
    private LoginButton mKakaoLoginButton;
    private SessionCallback callback;      //콜백 선언

    private Button mSignUpButton;
    private Button mSignInButton;


    private Users user;
    private Server server = new Server();
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

        mContext = LoginActivity.this;
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


        mCustomNaverButton = findViewById(R.id.activity_login_btn_naver);
        mCustomNaverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOAuthLoginInstance.startOauthLoginActivity((Activity) mContext, mOAuthLoginHandler);
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
            String accessToken = mOAuthLoginInstance.getAccessToken(mContext);
            String refreshToken = mOAuthLoginInstance.getRefreshToken(mContext);
            long expiresAt = mOAuthLoginInstance.getExpiresAt(mContext);
            String tokenType = mOAuthLoginInstance.getTokenType(mContext);

            new NaverProfileTask().execute(accessToken);
            Log.d("네이버 로그인", mOAuthLoginInstance.getAccessToken(getApplicationContext()));
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

                new getUserTask().execute(USER_KEY);//유저 체크하고 있으면 받아오고 없으면 타입 받는 것으로 이동

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

        user.setLast_login_at(Calendar.getInstance().getTime());//최근 접속시간 갱신
        server.setUser(user);
        server.updateUser();

        if(type == 0) //알바
            intent = new Intent(this, PtMainActivity.class);
        else
            intent = new Intent(this, BsMainActivity.class);

        startActivity(intent);
        finish();
    }
//신규유저 로그인 유저 타입을 받기 위해 이동
    protected void redirectSignUp2Activity() {
        Intent intent = new Intent(this, SignUp2Activity.class);
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

    protected class getUserTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            server.loadUser(strings[0]);
            while (server.getUsers() == null && server.isUserExist()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Log.d("네이버 로그인", "테스트 "+ server.isUserExist());

            return server.isUserExist();
        }

        @Override
        protected void onPostExecute(Boolean isExist) {
            if(isExist){
                user = server.getUsers();
                Log.d("로그인", "메인으로 이동");
                redirectMainActivity(user.getType());
            }
            else {
                server.setUser(user);
                redirectSignUp2Activity();
            }
        }
    }


    class NaverProfileTask extends AsyncTask<String, Void, String> {
        String result;

        @Override
        protected String doInBackground(String... strings) {
            String token = strings[0];// 네이버 로그인 접근 토큰;
            String header = "Bearer " + token; // Bearer 다음에 공백 추가
            try {
                String apiURL = "https://openapi.naver.com/v1/nid/me";
                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("Authorization", header);
                int responseCode = con.getResponseCode();
                BufferedReader br;
                if (responseCode == 200) { // 정상 호출
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                } else {  // 에러 발생
                    br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                }
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                result = response.toString();
                br.close();
                System.out.println(response.toString());
            } catch (Exception e) {
                System.out.println(e);
            }
            //result 값은 JSONObject 형태로 넘어옵니다.
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                Log.d("네이버 로그인", "값 받아옴");

                //넘어온 result 값을 JSONObject 로 변환해주고, 값을 가져오면 되는데요.
                // result 를 Log에 찍어보면 어떻게 가져와야할 지 감이 오실거에요.
                JSONObject object = new JSONObject(result);
                Log.d("네이버 로그인", "값: "+ object.toString());
                Log.d("네이버 로그인", "테스트: "+ object.getString("resultcode"));
                Log.d("네이버 로그인", "테스트22: "+ object.getString("resultcode").equals("00"));


                if (object.getString("resultcode").equals("00")) {
                    Log.d("네이버 로그인", "테스트 333");

                    Log.d("네이버 로그인", "테스트 3.1: "+ object.getString("response"));
                    JSONObject jsonObject = new JSONObject(object.getString("response"));
                    Log.d("네이버 로그인", "테스트 3.2: "+ object.getString("response"));
                    user.setId(jsonObject.getString("id"));
                    user.setEmail(jsonObject.getString("email"));
                    user.setName(jsonObject.getString("name"));
                    Log.d("네이버 로그인", "테스트 3.5");
                    new getUserTask().execute(user.getId());//유저 체크하고 있으면 받아오고 없으면 타입 받는 것으로 이동
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
