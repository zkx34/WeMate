package com.zkx.weipo.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.afollestad.materialdialogs.MaterialDialog;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.zkx.weipo.app.adapter.HomePageViewAdapter;
import com.zkx.weipo.app.openapi.legacy.FavoritesAPI;
import com.zkx.weipo.app.util.AccessTokenKeeper;
import com.zkx.weipo.app.api.Constants;
import com.zkx.weipo.app.app.WeiboApplication;
import com.zkx.weipo.app.openapi.OnRcvScrollListener;
import com.zkx.weipo.app.openapi.StatusesAPI;
import com.zkx.weipo.app.openapi.UsersAPI;
import com.zkx.weipo.app.openapi.models.ErrorInfo;
import com.zkx.weipo.app.openapi.models.StatusList;
import com.zkx.weipo.app.sroll.BottomTrackListener;
import com.zkx.weipo.app.sroll.TopDecoration;
import com.zkx.weipo.app.sroll.TopTrackListener;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static Boolean isRefreshing=false;
    private DrawerLayout mDrawerLayout;
    private RecyclerView mRecyclerView;
    private StatusList testDatas;
    private StatusList mStatusLists;
    private SwipeRefreshLayout mRefreshLayout;
    /** 用户信息接口 */
    private UsersAPI mUsersAPI;
    /** 用于获取微博信息流等操作的API */
    private StatusesAPI mStatusesAPI;
    private FavoritesAPI mFavoritesAPI;
    private HomePageViewAdapter mAdapter;
    private long maxId=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        // 获取当前已保存过的 Token
        /* 当前 Token 信息 */
        Oauth2AccessToken mAccessToken = AccessTokenKeeper.readAccessToken(this);
        // 获取用户信息接口
        mUsersAPI = new UsersAPI(this, Constants.APP_KEY, mAccessToken);
        mStatusesAPI = new StatusesAPI(this, Constants.APP_KEY, mAccessToken);
        mFavoritesAPI =new FavoritesAPI(this,Constants.APP_KEY,mAccessToken);
        //getUserInfo();
        getStatus();
        initData();
        WeiboApplication.getInstance();
        WeiboApplication.addActivity(this);
    }

    private void initData(){
        testDatas=new StatusList();
    }

    private void initViews(){

        mRefreshLayout =(SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setColorSchemeResources(R.color.red, R.color.orange, R.color.yellow, R.color.green);
        mRefreshLayout.setProgressViewEndTarget(false,220);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.id_Toolbar);
        setSupportActionBar(mToolbar);
        mDrawerLayout=(DrawerLayout)findViewById(R.id.id_DrawerLayout);
        ActionBarDrawerToggle mActionBarDrawerToggle=new ActionBarDrawerToggle(this,mDrawerLayout, mToolbar,R.string.open,R.string.close);
        mActionBarDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);

        NavigationView mNavigationView = (NavigationView) findViewById(R.id.id_NavigationView);
        mNavigationView.inflateHeaderView(R.layout.navi_header);
        mNavigationView.inflateMenu(R.menu.menu_nav);
        onNavigationViewMenuItemSelected(mNavigationView);

        mRecyclerView=(RecyclerView)findViewById(R.id.id_RecyclerView);
        LinearLayoutManager mLayoutManage = new LinearLayoutManager(MainActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManage);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new TopDecoration(findViewById(R.id.id_Toolbar)));
        mRecyclerView.addOnScrollListener(new TopTrackListener(findViewById(R.id.id_Toolbar)));
        mRecyclerView.addOnScrollListener(new BottomTrackListener(findViewById(R.id.floatbutton)));
    }

    /* private void getUserInfo(){
         if (mAccessToken != null && mAccessToken.isSessionValid()) {
             long uid = Long.parseLong(mAccessToken.getUid());
             mUsersAPI.show(uid, mListener);
         }
     }*/

    private void onNavigationViewMenuItemSelected(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    default:
                        mDrawerLayout.closeDrawers();
                        break;
                }
                return true;
            }
        });
    }

    private void getStatus(){
        mStatusesAPI.friendsTimeline(0L, maxId, 20, 1, false, 0, false, new RequestListener(){
            @Override
            public void onComplete(String s) {
                if (!TextUtils.isEmpty(s)){
                    if (s.startsWith("{\"statuses\"")){
                        mStatusLists=StatusList.parse(s);
                        if (mStatusLists != null && mStatusLists.total_number > 0) {
                            testDatas=mStatusLists;
                            //Long.parseLong(status.get(status.size() -1).getMid())-1;
                            if (mAdapter==null){
                                initAdapter();
                                maxId=Long.parseLong(mStatusLists.statusList.get(mStatusLists.statusList.size() - 1).mid)-1;
                            }else {
                                mAdapter.refresh(testDatas.statusList);
                                maxId=Long.parseLong(mStatusLists.statusList.get(mStatusLists.statusList.size() - 1).mid)-1;
                            }
                        }
                    }
                }
            }
            @Override
            public void onWeiboException(WeiboException e) {
                ErrorInfo info = ErrorInfo.parse(e.getMessage());
                Toast.makeText(MainActivity.this, info.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initAdapter(){

        mAdapter=new HomePageViewAdapter(MainActivity.this,testDatas);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setOnScrollListener(new OnRcvScrollListener(){
            @Override
            public void onBottom() {
                super.onBottom();
                if (!isRefreshing){
                    Toast.makeText(MainActivity.this,"加载更多微博",Toast.LENGTH_SHORT).show();
                    loadMore();
                    isRefreshing=true;
                }else {
                    isRefreshing=false;
                }
            }
        });

        mAdapter.setOnItemClickLitener(new HomePageViewAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position, long id) {
                switch (view.getId()) {

                    case R.id.id_CardView:
                        Intent intent=new Intent(MainActivity.this,WeiboDetail.class);
                        intent.putExtra("id",id);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onItemLongClick(View view, int position, final long id) {
                switch (view.getId()) {
                    case R.id.id_CardView:
                        new MaterialDialog.Builder(MainActivity.this)
                                .items(R.array.defualt_)
                                .itemsCallback(new MaterialDialog.ListCallback() {
                                    @Override
                                    public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                                        switch (i){
                                            case 0:
                                                Toast.makeText(MainActivity.this, "0",
                                                        Toast.LENGTH_SHORT).show();
                                                break;
                                            case 1:
                                                Toast.makeText(MainActivity.this, "1",
                                                        Toast.LENGTH_SHORT).show();
                                                break;
                                            case 2:
                                                mFavoritesAPI.create(id, new RequestListener() {
                                                    @Override
                                                    public void onComplete(String s) {
                                                        Toast.makeText(MainActivity.this, "收藏成功",
                                                                Toast.LENGTH_SHORT).show();
                                                    }

                                                    @Override
                                                    public void onWeiboException(WeiboException e) {
                                                        ErrorInfo info = ErrorInfo.parse(e.getMessage());
                                                        Toast.makeText(MainActivity.this, info.toString(), Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                                break;
                                        }
                                    }
                                })
                                .show();
                        break;
                    case R.id.btn_repeat:
                        new MaterialDialog.Builder(MainActivity.this)
                                .title(R.string.retweet_title)
                                .input(R.string.input_hint, R.string.input_prefill, new MaterialDialog.InputCallback() {
                                    @Override
                                    public void onInput(MaterialDialog materialDialog, CharSequence charSequence) {
                                        Toast.makeText(MainActivity.this, "转发",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .show();
                        break;
                    case R.id.btn_comment:
                        new MaterialDialog.Builder(MainActivity.this)
                                .title(R.string.com_title)
                                .input(R.string.com_hint, R.string.input_prefill, new MaterialDialog.InputCallback() {
                                    @Override
                                    public void onInput(MaterialDialog materialDialog, CharSequence charSequence) {
                                        Toast.makeText(MainActivity.this, "评论",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .show();
                        break;
                    case R.id.user_headimg:
                        Toast.makeText(MainActivity.this, position + " 用户头像被点击",
                                Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 加载更多微博
     */
    public void loadMore()
    {
        getStatus();
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        mStatusesAPI.friendsTimeline(0L, 0L, 20, 1, false, 0, false, new RequestListener() {
            @Override
            public void onComplete(String s) {
                if (!TextUtils.isEmpty(s)){
                    if (s.startsWith("{\"statuses\"")){
                        mStatusLists=StatusList.parse(s);
                        if (mStatusLists != null && mStatusLists.total_number > 0) {
                            testDatas=mStatusLists;
                            initAdapter();
                            mRefreshLayout.setRefreshing(false);
                        }
                    }
                }
            }

            @Override
            public void onWeiboException(WeiboException e) {
                ErrorInfo info = ErrorInfo.parse(e.getMessage());
                Toast.makeText(MainActivity.this, info.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_exit) {

            WeiboApplication.getInstance();
            WeiboApplication.appExit(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }

}
