package com.zkx.weipo.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Administrator on 2015/9/14.
 */
public class RegistActivity extends AppCompatActivity {


    private WebView mWebview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view_regist);
        mWebview=(WebView)findViewById(R.id.web_view);
        Intent intent=getIntent();
        String url=intent.getStringExtra("url");
        mWebview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.loadUrl(url);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode==KeyEvent.KEYCODE_BACK)&&mWebview.canGoBack()){
            mWebview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }
}
