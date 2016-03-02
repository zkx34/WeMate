package com.zkx.weipo.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.zkx.weipo.app.adapter.HomePageListAdapater;
import com.zkx.weipo.app.api.Constants;
import com.zkx.weipo.app.openapi.legacy.StatusesAPI;
import com.zkx.weipo.app.openapi.models.ErrorInfo;
import com.zkx.weipo.app.openapi.models.StatusList;
import com.zkx.weipo.app.util.AccessTokenKeeper;

public class user_main_page extends AppCompatActivity {

    private HomePageListAdapater mAdapter;
    private Oauth2AccessToken mAccessToken;
    private StatusesAPI mStatusesAPI;//获取微博信息接口
    private StatusList mStatusLists;
    private long statusMaxId =0;
    private ListView mListview;

    private void initViews(){
        //Toolbar
        Toolbar mToolbar=(Toolbar)findViewById(R.id.user_main_page_toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.mipmap.ic_back_dark);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //listview
        mListview=(ListView)findViewById(R.id.user_page_listview);
        mListview.setDivider(null);
    }

    private void initAdapter(){
        mAdapter=new HomePageListAdapater(mStatusLists,this);
        mListview.setAdapter(mAdapter);
    }

    private void getStatus(){
        mStatusesAPI.userTimeline(0, statusMaxId, 50, 1, false, 0, false, new RequestListener() {
            @Override
            public void onComplete(String s) {
                mStatusLists=StatusList.parse(s);
                if (mStatusLists != null && mStatusLists.total_number > 0) {
                    //Long.parseLong(status.get(status.size() -1).getMid())-1;
                    if (mAdapter==null){
                        initAdapter();
                        Toast.makeText(user_main_page.this,"加载",Toast.LENGTH_SHORT).show();
                        statusMaxId =Long.parseLong(mStatusLists.statusList.get(mStatusLists.statusList.size() - 1).mid)-1;
                    }else {
                        //mListView.hideFooterView();
                        //loading.setVisibility(View.GONE);
                        mAdapter.refresh(mStatusLists.statusList);
                        statusMaxId =Long.parseLong(mStatusLists.statusList.get(mStatusLists.statusList.size() - 1).mid)-1;
                    }
                }else {
                    Toast.makeText(user_main_page.this,"加载失败",Toast.LENGTH_SHORT).show();
                    //加载完150条微博后
                   // loading.setVisibility(View.GONE);
                   // done.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onWeiboException(WeiboException e) {
                ErrorInfo info = ErrorInfo.parse(e.getMessage());
                Toast.makeText(user_main_page.this, info != null ? info.toString() : null, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_main_page);
        mAccessToken= AccessTokenKeeper.readAccessToken(this);
        mStatusesAPI=new StatusesAPI(this, Constants.APP_KEY,mAccessToken);
        initViews();
        getStatus();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_main_page, menu);
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
}
