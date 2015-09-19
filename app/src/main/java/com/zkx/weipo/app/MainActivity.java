package com.zkx.weipo.app;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.zkx.weipo.app.openapi.StatusesAPI;
import com.zkx.weipo.app.openapi.UsersAPI;
import com.zkx.weipo.app.openapi.models.ErrorInfo;
import com.zkx.weipo.app.openapi.models.StatusList;
import com.zkx.weipo.app.openapi.models.User;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private NavigationView mNavigationView;
    private RecyclerView mRecyclerView;
    private MyRecyclerViewAdapter mRecyclerViewAdapter;
    private List<TestData> testDatas;
    private StatusList mStatusLists;
    /** 当前 Token 信息 */
    private Oauth2AccessToken mAccessToken;
    /** 用户信息接口 */
    private UsersAPI mUsersAPI;
    /** 用于获取微博信息流等操作的API */
    private StatusesAPI mStatusesAPI;

    private void initData(){
        testDatas=new ArrayList<TestData>();

    }

    private void initViews(){
        mToolbar=(Toolbar)findViewById(R.id.id_Toolbar);
        setSupportActionBar(mToolbar);

        mDrawerLayout=(DrawerLayout)findViewById(R.id.id_DrawerLayout);
        ActionBarDrawerToggle mActionBarDrawerToggle=new ActionBarDrawerToggle(this,mDrawerLayout,mToolbar,R.string.open,R.string.close);
        mActionBarDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);

        mNavigationView=(NavigationView)findViewById(R.id.id_NavigationView);
        mNavigationView.inflateHeaderView(R.layout.navi_header);
        mNavigationView.inflateMenu(R.menu.menu_nav);
        onNavigationViewMenuItemSelected(mNavigationView);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        // 获取当前已保存过的 Token
        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        // 获取用户信息接口
        mUsersAPI = new UsersAPI(this, Constants.APP_KEY, mAccessToken);
        mStatusesAPI = new StatusesAPI(this, Constants.APP_KEY, mAccessToken);
        getUserInfo();
        getStatus();
        initData();

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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }

    private void getUserInfo(){
        if (mAccessToken != null && mAccessToken.isSessionValid()) {
            long uid = Long.parseLong(mAccessToken.getUid());
            mUsersAPI.show(uid, mListener);
        }
    }

    private void getStatus(){
        mStatusesAPI.friendsTimeline(0L, 0L, 30, 1, false, 0, false, new RequestListener() {
            @Override
            public void onComplete(String s) {
                if (!TextUtils.isEmpty(s)){
                if (s.startsWith("{\"statuses\"")){
                    mStatusLists=StatusList.parse(s);
                    if (mStatusLists != null && mStatusLists.total_number > 0) {
                        Toast.makeText(MainActivity.this,
                                "获取微博信息流成功, 条数: " + mStatusLists.statusList.size(),
                                Toast.LENGTH_LONG).show();
                    }
                    for (int i=0;i<mStatusLists.statusList.size();i++){
                        testDatas.add(new TestData(mStatusLists.statusList.get(i).text,mStatusLists.statusList.get(i).user.name,mStatusLists.statusList.get(i).created_at,"来自: "+mStatusLists.statusList.get(i).getTextSource()));
                    }
                    initAdapter();
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
        mRecyclerView=(RecyclerView)findViewById(R.id.id_RecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        mRecyclerView.setHasFixedSize(true);//-------------------//
        mRecyclerViewAdapter=new MyRecyclerViewAdapter(testDatas);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
    }

    private RequestListener mListener=new RequestListener(){
        @Override
        public void onComplete(String s) {
            if (!TextUtils.isEmpty(s)){
                User user=User.parse(s);
                if (user!=null){
                    mToolbar.setTitle(user.screen_name);
                    TextView view=(TextView)findViewById(R.id.u_ID);
                    view.setText(user.screen_name);
                }
            }
        }
        @Override
        public void onWeiboException(WeiboException e) {
            ErrorInfo info = ErrorInfo.parse(e.getMessage());
            Toast.makeText(MainActivity.this, info.toString(), Toast.LENGTH_LONG).show();
        }
    };
}
