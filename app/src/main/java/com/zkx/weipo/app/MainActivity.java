package com.zkx.weipo.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.afollestad.materialdialogs.MaterialDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.zkx.weipo.app.adapter.HomePageListAdapater;
import com.zkx.weipo.app.api.Constants;
import com.zkx.weipo.app.app.WeiboApplication;
import com.zkx.weipo.app.openapi.CommentsAPI;
import com.zkx.weipo.app.openapi.UsersAPI;
import com.zkx.weipo.app.openapi.legacy.AccountAPI;
import com.zkx.weipo.app.openapi.legacy.FavoritesAPI;
import com.zkx.weipo.app.openapi.legacy.StatusesAPI;
import com.zkx.weipo.app.openapi.models.ErrorInfo;
import com.zkx.weipo.app.openapi.models.Status;
import com.zkx.weipo.app.openapi.models.StatusList;
import com.zkx.weipo.app.openapi.models.User;
import com.zkx.weipo.app.util.AccessTokenKeeper;
import com.zkx.weipo.app.util.Tools;
import de.hdodenhof.circleimageview.CircleImageView;
import me.maxwin.view.XListView;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements XListView.IXListViewListener {

    private DrawerLayout mDrawerLayout;
    private StatusList mStatusLists;
    /** 用户信息接口 */
    private UsersAPI mUsersAPI;
    private User mUser;
    /** 用于获取微博信息流等操作的API */
    private CommentsAPI mCommentsAPI;
    private AccountAPI mAccountAPI;
    private StatusesAPI mStatusesAPI;
    private FavoritesAPI mFavoritesAPI;
    private XListView mListView;
    private HomePageListAdapater mAdapter;
    private long statusMaxId =0;
    private CircleImageView mUserProfile;
    private TextView mUserName;
    private TextView mUserDesc;
    private ImageView mVerified;
    private boolean comment_ori=false;
    private static  int TYPE=0;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initAPI();
        initViews();
        getUid();
        getStatus();
        WeiboApplication.getInstance();
        WeiboApplication.addActivity(this);
    }

    private void initAPI(){
        // 获取当前已保存过的 Token信息
        Oauth2AccessToken mAccessToken = AccessTokenKeeper.readAccessToken(this);
        // 获取用户信息接口
        mUsersAPI = new UsersAPI(this, Constants.APP_KEY, mAccessToken);
        mStatusesAPI = new StatusesAPI(this, Constants.APP_KEY, mAccessToken);
        mFavoritesAPI =new FavoritesAPI(this,Constants.APP_KEY,mAccessToken);
        mAccountAPI=new AccountAPI(this,Constants.APP_KEY,mAccessToken);
        mCommentsAPI=new CommentsAPI(this,Constants.APP_KEY,mAccessToken);
    }

    private void initViews(){

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

        mProgressBar=(ProgressBar)findViewById(R.id.main_progress_bar);
        mProgressBar.setVisibility(View.VISIBLE);

        mListView=(XListView) findViewById(R.id.home_listview);
        mListView.setPullLoadEnable(true);
        mListView.setPullRefreshEnable(true);
        mListView.setDivider(null);
        mListView.setAdapter(null);
        mListView.setXListViewListener(this);

        mUserProfile=(CircleImageView)findViewById(R.id.user_head_large);
        mUserName=(TextView)findViewById(R.id.user_name);
        mUserDesc=(TextView)findViewById(R.id.user_description);
        mVerified=(ImageView)findViewById(R.id.user_verified);

        mUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=mAdapter.getScreenName(0);
                Intent intent=new Intent(MainActivity.this,user_main_page.class);
                //intent.putExtra("name",name);
                startActivity(intent);
            }
        });
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

    private void getStatus(){
        TYPE=3;
        mStatusesAPI.homeTimeline(0L, statusMaxId, 20, 1, false, 0, false, mListener);
    }

    private void initAdapter(){
        mAdapter=new HomePageListAdapater(mStatusLists,MainActivity.this);
        mListView.setAdapter(mAdapter);
        mAdapter.setOnItemClickLitener(new HomePageListAdapater.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position, long id) {
                switch (view.getId()) {
                    case R.id.id_content:
                    case R.id.id_CardView:
                        Status list= (Status) mAdapter.getItem(position);
                        Intent intent=new Intent(MainActivity.this,WeiboDetail.class);
                        intent.putExtra("sta", list);
                        intent.putExtra("id",id);
                        startActivity(intent);
                        break;
                    case R.id.id_retweeted_detail:
                        Status list1=(Status)mAdapter.getRetweetedItem(position);
                        Intent intent1=new Intent(MainActivity.this,WeiboDetail.class);
                        intent1.putExtra("sta",list1);
                        intent1.putExtra("id",id);
                        startActivity(intent1);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onItemLongClick(View view, int position, final long id) {
                switch (view.getId()){
                    case R.id.id_CardView:
                        new MaterialDialog.Builder(MainActivity.this)
                                .items(R.array.defualt_)
                                .itemsCallback(new MaterialDialog.ListCallback() {
                                    @Override
                                    public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                                        switch (i){
                                            case 0:
                                                TYPE=2;
                                                new MaterialDialog.Builder(MainActivity.this)
                                                        .title(R.string.retweet_title)
                                                        .input(R.string.input_hint, R.string.input_prefill, new MaterialDialog.InputCallback() {
                                                            @Override
                                                            public void onInput(MaterialDialog materialDialog, CharSequence charSequence) {
                                                                TYPE=2;
                                                                if (charSequence.length() > 140){
                                                                    Toast.makeText(MainActivity.this,"转发内容超出限制",Toast.LENGTH_SHORT).show();
                                                                    return;
                                                                }
                                                                if (charSequence==""){
                                                                    charSequence="转发微博";
                                                                }
                                                                mStatusesAPI.repost(id, String.valueOf(charSequence),0,mListener);
                                                            }
                                                        })
                                                        .show();
                                                break;
                                            case 1:
                                                TYPE=1;
                                                new MaterialDialog.Builder(MainActivity.this)
                                                        .title(R.string.com_title)
                                                        .input(R.string.com_hint, R.string.input_prefill, new MaterialDialog.InputCallback() {
                                                            @Override
                                                            public void onInput(MaterialDialog materialDialog, CharSequence charSequence) {
                                                                mCommentsAPI.create(String.valueOf(charSequence),id,comment_ori,mListener);
                                                            }
                                                        })
                                                        .show();
                                                break;
                                            case 2:
                                                TYPE=4;
                                                mFavoritesAPI.create(id,mListener);
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
                                        TYPE=2;
                                        if (charSequence.length() > 140){
                                            Toast.makeText(MainActivity.this,"转发内容超出限制",Toast.LENGTH_SHORT).show();
                                            return;
                                        }else if (charSequence==""){
                                            charSequence="转发微博";
                                        }
                                        mStatusesAPI.repost(id, String.valueOf(charSequence),0,mListener);
                                    }
                                })
                                .show();
                        break;
                    case R.id.btn_comment:
                        TYPE=1;
                        new MaterialDialog.Builder(MainActivity.this)
                                .title(R.string.com_title)
                                .input(R.string.com_hint, R.string.input_prefill, new MaterialDialog.InputCallback() {
                                    @Override
                                    public void onInput(MaterialDialog materialDialog, CharSequence charSequence) {
                                        mCommentsAPI.create(String.valueOf(charSequence),id,comment_ori,mListener);
                                    }
                                })
                                .show();
                        break;
                    case R.id.user_headimg:
                        Toast.makeText(MainActivity.this,"点击头像",Toast.LENGTH_SHORT).show();
                        /*Intent intent=new Intent(MainActivity.this,user_main_page.class);
                        intent.putExtra("screen_name",
                                mAdapter.getScreenName(position));
                        startActivity(intent);*/
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void getUid(){
        mAccountAPI.getUid(new RequestListener() {
            @Override
            public void onComplete(String s) {
                try {
                    JSONObject jsonObject=new JSONObject(s);
                    long uid =jsonObject.getLong("uid");
                    mUsersAPI.show(uid, new RequestListener() {
                        @Override
                        public void onComplete(String s) {
                            mUser=User.parse(s);
                            if (mUser!=null){
                                ImageLoader.getInstance().displayImage(mUser.avatar_large,mUserProfile,WeiboApplication.options);
                                mUserName.setText(mUser.name);
                                mUserDesc.setText(mUser.description);
                                Tools.checkVerified(mUser,mVerified);
                            }
                        }

                        @Override
                        public void onWeiboException(WeiboException e) {
                            ErrorInfo info = ErrorInfo.parse(e.getMessage());
                            Toast.makeText(MainActivity.this, info != null ? info.toString() : null, Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onWeiboException(WeiboException e) {
                ErrorInfo info = ErrorInfo.parse(e.getMessage());
                Toast.makeText(MainActivity.this, info != null ? info.toString() : null, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode){
            case 1:
                onRefresh();
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        TYPE=5;
        mStatusesAPI.homeTimeline(0L, 0L, 20, 1, false, 0, false, mListener);
    }

    /**
     * 上滑加载更多微博
     */
    @Override
    public void onLoadMore() {
        getStatus();
    }

    private void onLoad(){
        mListView.stopRefresh();
        mListView.stopLoadMore();
        mListView.setRefreshTime("刚刚");
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
        switch (id){
            case R.id.action_exit:
                WeiboApplication.getInstance();
                WeiboApplication.appExit(this);
                break;
            case R.id.new_weibo:
                startActivityForResult(new Intent(MainActivity.this,NewWeibo.class),0);
                break;
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

    private RequestListener mListener=new RequestListener() {
        @Override
        public void onComplete(String s) {
            switch (TYPE){
                case 1:/**评论微博的处理*/
                    if (!TextUtils.isEmpty(s)){
                        Toast.makeText(MainActivity.this, "评论成功",
                                Toast.LENGTH_SHORT).show();
                    }
                    TYPE=0;
                    break;
                case 2:/**转发微博的处理，3是获取微博信息流,4是收藏,5是刷新主页*/
                    if (!TextUtils.isEmpty(s)){
                        Toast.makeText(MainActivity.this, "转发成功",
                                Toast.LENGTH_SHORT).show();
                        TYPE=5;
                        onRefresh();
                    }
                    break;
                case 3:/**获取微博信息流的处理*/
                    mStatusLists=StatusList.parse(s);
                    if ((mStatusLists != null ? mStatusLists.statusList : null) != null && mStatusLists.total_number > 0) {
                        if (mAdapter==null){
                            initAdapter();
                            onLoad();
                            mProgressBar.setVisibility(View.GONE);
                            statusMaxId =Long.parseLong(mStatusLists.statusList.get(mStatusLists.statusList.size() - 1).mid)-1;
                        }else {
                            onLoad();
                            mAdapter.refresh(mStatusLists.statusList);
                            statusMaxId =Long.parseLong(mStatusLists.statusList.get(mStatusLists.statusList.size() - 1).mid)-1;
                        }
                    }else {
                        //加载完150条微博后
                        onLoad();
                    }
                    TYPE=0;
                    break;
                case 4:/**收藏微博的处理*/
                    if (!TextUtils.isEmpty(s)){
                        Toast.makeText(MainActivity.this, "收藏成功",
                                Toast.LENGTH_SHORT).show();
                    }
                    TYPE=0;
                    break;
                case 5:/**刷新主页的处理*/
                    mStatusLists=StatusList.parse(s);
                    if (mStatusLists != null && mStatusLists.total_number > 0) {
                        statusMaxId =Long.parseLong(mStatusLists.statusList.get(mStatusLists.statusList.size() - 1).mid)-1;
                        onLoad();
                        initAdapter();
                    }
                    TYPE=0;
                    break;
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            ErrorInfo info = ErrorInfo.parse(e.getMessage());
            Toast.makeText(MainActivity.this, info != null ? info.toString() : null, Toast.LENGTH_LONG).show();
        }
    };
}
