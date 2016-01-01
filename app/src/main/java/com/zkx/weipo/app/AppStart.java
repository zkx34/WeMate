package com.zkx.weipo.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.Toast;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.zkx.weipo.app.util.AccessTokenKeeper;
import com.zkx.weipo.app.api.Constants;
import com.zkx.weipo.app.app.WeiboApplication;

/**
 * Created by Administrator on 2015/10/14.
 */
public class AppStart extends Activity {

    private Oauth2AccessToken mAccessToken;
    private SsoHandler mSsoHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.start);
        AuthInfo mAuthInfo = new AuthInfo(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
        mSsoHandler=new SsoHandler(AppStart.this, mAuthInfo);
        WeiboApplication.getInstance();
        WeiboApplication.addActivity(this);
        ImageView imageView=(ImageView)findViewById(R.id.start_img);
        //渐变展示启动屏
        AlphaAnimation aa = new AlphaAnimation(0.0f,1.0f);
        aa.setDuration(3000);
        imageView.setAnimation(aa);
        aa.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationEnd(Animation arg0) {
                redirectTo();
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
            @Override
            public void onAnimationStart(Animation animation) {}

        });
    }

    class AuthListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle bundle) {
            mAccessToken= Oauth2AccessToken.parseAccessToken(bundle);
            if (mAccessToken.isSessionValid()){
                AccessTokenKeeper.writeAccessToken(AppStart.this, mAccessToken);
                startActivity(new Intent(AppStart.this,MainActivity.class));
                finish();
            }else {
                String code=bundle.getString("code");
                String message=getString(R.string.weibosdk_demo_toast_auth_failed);
                if (!TextUtils.isEmpty(code)){
                    message=message+"/nObtained the code:"+code;
                }
                Toast.makeText(AppStart.this, message, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(AppStart.this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(AppStart.this,R.string.weibosdk_demo_toast_auth_canceled,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mSsoHandler!=null){
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
            if ((mAccessToken=AccessTokenKeeper.readAccessToken(this)).isSessionValid()){
                startActivity(new Intent(AppStart.this,MainActivity.class));
                finish();
            }
        }
    }

    /**
     * 跳转到...
     */
    private void redirectTo(){
        if ((mAccessToken= AccessTokenKeeper.readAccessToken(this)).isSessionValid()){
            startActivity(new Intent(AppStart.this,MainActivity.class));
            finish();
        }else {
            mSsoHandler.authorize(new AuthListener());
        }
    }
}
