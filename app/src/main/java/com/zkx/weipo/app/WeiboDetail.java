package com.zkx.weipo.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.zkx.weipo.app.Util.AccessTokenKeeper;
import com.zkx.weipo.app.api.Constants;
import com.zkx.weipo.app.app.WeiboApplication;
import com.zkx.weipo.app.openapi.legacy.StatusesAPI;

/**
 * Created by Administrator on 2015/10/14.
 */
public class WeiboDetail extends AppCompatActivity {

    private void initView(){

        Toolbar mToolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.toolbar_leftarrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        long id=getIntent().getLongExtra("id",0);

        de.hdodenhof.circleimageview.CircleImageView profile=(de.hdodenhof.circleimageview.CircleImageView)findViewById(R.id.de_profile);
        TextView de_name=(TextView)findViewById(R.id.de_name);
        TextView de_content=(TextView)findViewById(R.id.de_content);

        Oauth2AccessToken mAccessToken = AccessTokenKeeper.readAccessToken(this);
        // 获取用户信息接口
        StatusesAPI mStatusesAPI = new StatusesAPI(this, Constants.APP_KEY, mAccessToken);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weibo_detail);
        WeiboApplication.getInstance();
        WeiboApplication.addActivity(this);
        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_navigationbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_retweet:
                Toast.makeText(WeiboDetail.this,"转发",Toast.LENGTH_SHORT).show();
                break;
            case R.id.item_comment:
                Toast.makeText(WeiboDetail.this,"评论",Toast.LENGTH_SHORT).show();
                break;
            case R.id.item_collect:
                Toast.makeText(WeiboDetail.this,"收藏",Toast.LENGTH_SHORT).show();
                break;
            case R.id.item_share:
                Toast.makeText(WeiboDetail.this,"分享",Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
