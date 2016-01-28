package com.zkx.weipo.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.zkx.weipo.app.adapter.DetailPageListViewAdapter;
import com.zkx.weipo.app.adapter.HomePageListAdapater;
import com.zkx.weipo.app.api.Constants;
import com.zkx.weipo.app.app.WeiboApplication;
import com.zkx.weipo.app.openapi.CommentsAPI;
import com.zkx.weipo.app.openapi.models.CommentList;
import com.zkx.weipo.app.openapi.models.ErrorInfo;
import com.zkx.weipo.app.openapi.models.Status;
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
    private CommentList mCommentList;
    private TextView txt_retweet;
    private TextView txt_comment;
    private CommentsAPI mCommentsAPI;


    private void initView(){
        //获取微博ID
        long id=getIntent().getLongExtra("id",0);
        final Status list=(Status)getIntent().getSerializableExtra("sta");
        Oauth2AccessToken mAccessToken = AccessTokenKeeper.readAccessToken(this);
        // 获取用户信息接口
        CommentsAPI mCommentsAPI=new CommentsAPI(this,Constants.APP_KEY,mAccessToken);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.mipmap.ic_back_dark);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txt_retweet=(TextView)findViewById(R.id.txt_retweet);
        txt_comment=(TextView)findViewById(R.id.txt_comment);

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
        final ImageView de_verified=(ImageView)findViewById(R.id.de_verified);
        mListView=(MyListView)findViewById(R.id.de_list);
        ScrollView mSv = (ScrollView) findViewById(R.id.de_sv);
        mSv.smoothScrollTo(0,0);

        de_name.setText(list.user.name);
        de_content.setText(Tools.getContent(this,list.text,de_content));
        ImageLoader.getInstance().displayImage(list.user.avatar_large,profile,WeiboApplication.options);
        de_createdAt.setText(Tools.getTimeStr(Tools.strToDate(list.created_at), new Date()));
        de_source.setText("来自:"+list.getTextSource());

        //判断用户是否认证
        Tools.checkVerified(list.user,de_verified);

        //判断微博中是否有图片
        if (!StringUtil.isEmpty(list.thumbnail_pic)){
            ArrayList<String> list2=list.pic_urls;
            de_r14.setVisibility(View.VISIBLE);
            HomePageListAdapater.initInfoImages(de_images1,list2);
        }else {
            de_r14.setVisibility(View.GONE);
        }

        //转发内容是否为空
        if (list.retweeted_status!=null && list.retweeted_status.user!=null){
            de_retweet_content.setVisibility(View.VISIBLE);
            de_retweet_detail.setText(Tools.getContent(this,"@"+list.retweeted_status.user.name+
                    ":"+list.retweeted_status.text,de_retweet_detail));
            //转发图片是否有图片
            if (!StringUtil.isEmpty(list.retweeted_status.thumbnail_pic)){
                ArrayList<String> list2=list.retweeted_status.pic_urls;
                de_rl5.setVisibility(View.VISIBLE);
                HomePageListAdapater.initInfoImages(de_images2,list2);
            }else {
                de_rl5.setVisibility(View.GONE);
            }
        }else
        if (list.retweeted_status!=null && list.retweeted_status.user==null){
            de_retweet_content.setVisibility(View.VISIBLE);
            de_retweet_detail.setText(R.string.retweed_error);
        }else {
            de_retweet_content.setVisibility(View.GONE);
        }

        mCommentsAPI.show(id, 0, 0, 20, 1, 0, new RequestListener() {
            @Override
            public void onComplete(String s) {
                if (!TextUtils.isEmpty(s)){
                    mCommentList=CommentList.parse(s);
                    if (mCommentList != null) {
                        mAdapter = new DetailPageListViewAdapter(WeiboDetail.this, mCommentList.commentList);
                        mListView.setAdapter(mAdapter);
                    }
                }
            }

            @Override
            public void onWeiboException(WeiboException e) {
                ErrorInfo info = ErrorInfo.parse(e.getMessage());
                Toast.makeText(WeiboDetail.this, info != null ? info.toString() : null, Toast.LENGTH_LONG).show();
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
        txt_comment.performClick();
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
