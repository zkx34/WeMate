package com.zkx.weipo.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.zkx.weipo.app.adapter.DetailPageListViewAdapter;
import com.zkx.weipo.app.adapter.HomePageListAdapater;
import com.zkx.weipo.app.api.Constants;
import com.zkx.weipo.app.app.WeiboApplication;
import com.zkx.weipo.app.openapi.CommentsAPI;
import com.zkx.weipo.app.openapi.legacy.StatusesAPI;
import com.zkx.weipo.app.openapi.models.CommentList;
import com.zkx.weipo.app.openapi.models.ErrorInfo;
import com.zkx.weipo.app.openapi.models.StatusList;
import com.zkx.weipo.app.util.AccessTokenKeeper;
import com.zkx.weipo.app.util.StringUtil;
import com.zkx.weipo.app.util.Tools;
import com.zkx.weipo.app.view.MyGridView;
import com.zkx.weipo.app.view.MyListView;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2015/10/14.
 */
public class WeiboDetail extends AppCompatActivity {

    private DetailPageListViewAdapter mAdapter;
    private MyListView mListView;
    private StatusList mStatusLists;
    private CommentList mCommentList;
    private ScrollView mSv;
    private void initView(){
        //获取微博ID
        long id=getIntent().getLongExtra("id",0);
        Oauth2AccessToken mAccessToken = AccessTokenKeeper.readAccessToken(this);
        // 获取用户信息接口
        StatusesAPI mStatusesAPI = new StatusesAPI(this, Constants.APP_KEY, mAccessToken);
        CommentsAPI mCommentsAPI=new CommentsAPI(this,Constants.APP_KEY,mAccessToken);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.toolbar_leftarrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final de.hdodenhof.circleimageview.CircleImageView profile=(de.hdodenhof.circleimageview.CircleImageView)findViewById(R.id.de_profile);
        final TextView de_name=(TextView)findViewById(R.id.de_name);
        final TextView de_content=(TextView)findViewById(R.id.de_content);
        final TextView de_createdAt=(TextView)findViewById(R.id.de_createdAt);
        final TextView de_source=(TextView)findViewById(R.id.de_source);
        final RelativeLayout de_r14=(RelativeLayout)findViewById(R.id.de_rl4);
        final MyGridView de_images1=(MyGridView)findViewById(R.id.de_images1);
        final TextView de_retweet_detail=(TextView)findViewById(R.id.de_retweet_detail);
        final LinearLayout de_retweet_content=(LinearLayout)findViewById(R.id.de_retweet_content);
        final RelativeLayout de_rl5=(RelativeLayout)findViewById(R.id.de_rl5);
        final MyGridView de_images2=(MyGridView)findViewById(R.id.de_images2);
        mSv=(ScrollView)findViewById(R.id.de_sv);
        mSv.smoothScrollTo(0,0);
        mListView=(MyListView)findViewById(R.id.de_listview);

        mStatusesAPI.friendsTimeline(0, id, 1, 1, false, 0, false, new RequestListener() {
            @Override
            public void onComplete(String s) {
                if (!TextUtils.isEmpty(s)){

                    mStatusLists = StatusList.parse(s);
                    de_name.setText(mStatusLists.statusList.get(0).user.name);
                    de_content.setText(Html.fromHtml(Tools.atBlue(mStatusLists.statusList.get(0).text)));
                    WeiboApplication.IMAGE_CACHE.get(mStatusLists.statusList.get(0).user.profile_image_url,profile);
                    de_createdAt.setText(Tools.getTimeStr(Tools.strToDate(mStatusLists.statusList.get(0).created_at), new Date()));
                    de_source.setText("来自:"+mStatusLists.statusList.get(0).getTextSource());

                    //判断微博中是否有图片
                    if (!StringUtil.isEmpty(mStatusLists.statusList.get(0).thumbnail_pic)){
                        ArrayList<String> list=mStatusLists.statusList.get(0).pic_urls;
                        de_r14.setVisibility(View.VISIBLE);
                        HomePageListAdapater.initInfoImages(de_images1,list);
                    }else {
                        de_r14.setVisibility(View.GONE);
                    }

                    //转发内容是否为空
                    if (mStatusLists.statusList.get(0).retweeted_status!=null
                            &&mStatusLists.statusList.get(0).retweeted_status.user!=null){
                        de_retweet_content.setVisibility(View.VISIBLE);
                        de_retweet_detail.setText(Html.fromHtml(Tools.atBlue("@"+mStatusLists.statusList.get(0).retweeted_status.user.name+
                                ":"+mStatusLists.statusList.get(0).retweeted_status.text)));
                        //转发图片是否有图片
                        if (!StringUtil.isEmpty(mStatusLists.statusList.get(0).retweeted_status.thumbnail_pic)){
                            ArrayList<String> list=mStatusLists.statusList.get(0).retweeted_status.pic_urls;
                            de_rl5.setVisibility(View.VISIBLE);
                            HomePageListAdapater.initInfoImages(de_images2,list);
                        }else {
                            de_rl5.setVisibility(View.GONE);
                        }
                    }else {
                        de_retweet_content.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onWeiboException(WeiboException e) {
                ErrorInfo info = ErrorInfo.parse(e.getMessage());
                Toast.makeText(WeiboDetail.this, info.toString(), Toast.LENGTH_LONG).show();
            }
        });

        mCommentsAPI.show(id, 0, 0, 20, 1, 0, new RequestListener() {
            @Override
            public void onComplete(String s) {
                if (!TextUtils.isEmpty(s)){
                    mCommentList=CommentList.parse(s);
                    if (mCommentList != null) {
                        mAdapter = new DetailPageListViewAdapter(WeiboDetail.this, mCommentList);
                        mListView.setAdapter(mAdapter);
                    }
                }
            }

            @Override
            public void onWeiboException(WeiboException e) {
                ErrorInfo info = ErrorInfo.parse(e.getMessage());
                Toast.makeText(WeiboDetail.this, info.toString(), Toast.LENGTH_LONG).show();
            }
        });

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
