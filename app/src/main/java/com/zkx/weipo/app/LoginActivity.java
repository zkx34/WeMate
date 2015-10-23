package com.zkx.weipo.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.zkx.weipo.app.Util.AccessTokenKeeper;
import com.zkx.weipo.app.api.Constants;
import com.zkx.weipo.app.app.WeiboApplication;

/**
 * Created by Administrator on 2015/9/15.
 */
public class LoginActivity extends AppCompatActivity {

    private Button mLoginBt;
    private Button mRegistBt;
    private AuthInfo mAuthInfo;
    private Oauth2AccessToken mAccessToken;
    private SsoHandler mSsoHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if ((mAccessToken= AccessTokenKeeper.readAccessToken(this)).isSessionValid()){

            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }else {
            setContentView(R.layout.activity_login);
            initLoginView();

            WeiboApplication.getInstance();
            WeiboApplication.addActivity(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mSsoHandler!=null){
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
            if ((mAccessToken=AccessTokenKeeper.readAccessToken(this)).isSessionValid()){
                finish();
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
            }
        }
    }

    private void initLoginView(){
        mLoginBt=(Button)findViewById(R.id.loginButton);
        mRegistBt=(Button)findViewById(R.id.registButton);
        mAuthInfo=new AuthInfo(this, Constants.APP_KEY,Constants.REDIRECT_URL,Constants.SCOPE);
        mSsoHandler=new SsoHandler(LoginActivity.this,mAuthInfo);
        mLoginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSsoHandler.authorize(new AuthListener());
            }
        });
        mRegistBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(LoginActivity.this, RegistActivity.class);
                intent1.putExtra("url", getString(R.string.regist_address));
                startActivity(intent1);
            }
        });
    }

    class AuthListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle bundle) {
            mAccessToken= Oauth2AccessToken.parseAccessToken(bundle);
            if (mAccessToken.isSessionValid()){
                AccessTokenKeeper.writeAccessToken(LoginActivity.this,mAccessToken);
            }else {
                String code=bundle.getString("code");
                String message=getString(R.string.weibosdk_demo_toast_auth_failed);
                if (!TextUtils.isEmpty(code)){
                    message=message+"/nObtained the code:"+code;
                }
                Toast.makeText(LoginActivity.this,message,Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(LoginActivity.this,R.string.weibosdk_demo_toast_auth_canceled,Toast.LENGTH_SHORT).show();
        }
    }
}
