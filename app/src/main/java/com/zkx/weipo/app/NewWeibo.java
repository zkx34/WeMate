package com.zkx.weipo.app;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.zkx.weipo.app.api.Constants;
import com.zkx.weipo.app.app.WeiboApplication;
import com.zkx.weipo.app.openapi.legacy.StatusesAPI;
import com.zkx.weipo.app.openapi.models.ErrorInfo;
import com.zkx.weipo.app.util.AccessTokenKeeper;

/**
 * Created by Administrator on 2015/12/20.
 */
public class NewWeibo extends AppCompatActivity {

    private EditText edit_text;
    private TextView limit_text;
    private static final int MAX_LIMIT=140;
    private StatusesAPI mStatusesAPI;

    private void initViews(){
        //Toolbar声明
        Toolbar mToolBar=(Toolbar)findViewById(R.id.new_weibo_toolbar);
        mToolBar.setTitle("新的微博");
        setSupportActionBar(mToolBar);
        mToolBar.setNavigationIcon(R.mipmap.ic_back_dark);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //EditText声明
        edit_text=(EditText)findViewById(R.id.edit_text);
        edit_text.setFocusable(true);
        edit_text.requestFocus();
        onFocusChanged(edit_text.isFocused());
        limit_text=(TextView)findViewById(R.id.limit_text);
        edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                limit_text.setText(MAX_LIMIT-s.length()+"/140");
            }
        });
    }

    /**
     * 自动获取焦点并弹出输入法
     * @param hasFocus
     */
    private void onFocusChanged(boolean hasFocus){
        final Boolean isFocus=hasFocus;
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm=(InputMethodManager)edit_text.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (isFocus){
                    imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
                }
                else {
                    imm.hideSoftInputFromWindow(edit_text.getWindowToken(),0);
                }
            }
        },100);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_weibo);
        initViews();
        Oauth2AccessToken mAccessToken = AccessTokenKeeper.readAccessToken(this);
        mStatusesAPI=new StatusesAPI(this, Constants.APP_KEY, mAccessToken);
        WeiboApplication.getInstance();
        WeiboApplication.addActivity(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.new_Weibo_new:
                if (!TextUtils.isEmpty(edit_text.getText()) && edit_text.getText().length() <= 140){
                    mStatusesAPI.update(edit_text.getText().toString(),"0.0" ,"0.0" , new RequestListener() {
                        @Override
                        public void onComplete(String s) {
                            if (!TextUtils.isEmpty(s)){
                                Toast.makeText(NewWeibo.this, R.string.success_new,Toast.LENGTH_SHORT).show();
                                NewWeibo.this.setResult(1);
                                finish();
                            }
                        }

                        @Override
                        public void onWeiboException(WeiboException e) {
                            ErrorInfo info = ErrorInfo.parse(e.getMessage());
                            Toast.makeText(NewWeibo.this, info != null ? info.toString() : null, Toast.LENGTH_LONG).show();
                        }
                    });
                }else {
                    Toast.makeText(this, R.string.alert,Toast.LENGTH_SHORT).show();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_weibo,menu);
        return true;
    }
}
