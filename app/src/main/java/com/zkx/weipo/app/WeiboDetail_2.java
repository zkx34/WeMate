package com.zkx.weipo.app;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.zkx.weipo.app.adapter.DetailPageViewAdapter;
import com.zkx.weipo.app.api.Constants;
import com.zkx.weipo.app.app.WeiboApplication;
import com.zkx.weipo.app.openapi.legacy.StatusesAPI;
import com.zkx.weipo.app.openapi.models.ErrorInfo;
import com.zkx.weipo.app.openapi.models.StatusList;
import com.zkx.weipo.app.sroll.TopDecoration;
import com.zkx.weipo.app.sroll.TopTrackListener;
import com.zkx.weipo.app.util.AccessTokenKeeper;

/**
 * Created by Administrator on 2015/11/8.
 */
public class WeiboDetail_2 extends AppCompatActivity {

    private DetailPageViewAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private StatusList mStatusLists;
    private SwipeRefreshLayout mRefreshLayout;

    private void initView(){

        long id=getIntent().getLongExtra("id",0);
        Oauth2AccessToken mAccessToken = AccessTokenKeeper.readAccessToken(this);
        // 获取用户信息接口
        StatusesAPI mStatusesAPI = new StatusesAPI(this, Constants.APP_KEY, mAccessToken);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.de_Toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.toolbar_leftarrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mRecyclerView= (RecyclerView) findViewById(R.id.de_RecyclerView);
        LinearLayoutManager mLayoutManage = new LinearLayoutManager(WeiboDetail_2.this);
        mRecyclerView.setLayoutManager(mLayoutManage);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new TopDecoration(findViewById(R.id.de_Toolbar)));
        mRecyclerView.addOnScrollListener(new TopTrackListener(findViewById(R.id.de_Toolbar)));

        mStatusesAPI.friendsTimeline(0, id, 1, 1, false, 0, false,new RequestListener() {
            @Override
            public void onComplete(String s) {
                if (!TextUtils.isEmpty(s)){
                    mStatusLists = StatusList.parse(s);
                    initAdapter();

                }
            }

            @Override
            public void onWeiboException(WeiboException e) {
                ErrorInfo info = ErrorInfo.parse(e.getMessage());
                Toast.makeText(WeiboDetail_2.this, info.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initAdapter(){
        mAdapter= new DetailPageViewAdapter(mStatusLists );
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weibo_detail_2);
        WeiboApplication.getInstance();
        WeiboApplication.addActivity(this);
        initView();
    }

}
