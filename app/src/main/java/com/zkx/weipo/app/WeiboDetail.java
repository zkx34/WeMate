package com.zkx.weipo.app;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.zkx.weipo.app.adapter.DetailPageListViewAdapter;
import com.zkx.weipo.app.api.Constants;
import com.zkx.weipo.app.app.WeiboApplication;
import com.zkx.weipo.app.openapi.CommentsAPI;
import com.zkx.weipo.app.openapi.models.CommentList;
import com.zkx.weipo.app.openapi.models.ErrorInfo;
import com.zkx.weipo.app.openapi.models.Status;
import com.zkx.weipo.app.util.*;
import com.zkx.weipo.app.view.MyGridView;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2015/10/14.
 */
public class WeiboDetail extends AppCompatActivity {

    private DetailPageListViewAdapter mAdapter;
    private ListView mListView;
    private CommentList mCommentList;
    private Button retweet;
    private TextView load_more;
    private CommentsAPI mCommentsAPI;
    private long id;
    private long MAX_ID=0;
    private LinearLayout done;

    private void bindView(){
        //获取微博ID
        id=getIntent().getLongExtra("id",0);
        final Status list=(Status)getIntent().getSerializableExtra("sta");
        Toolbar mToolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.mipmap.ic_back_dark);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mListView=(ListView) findViewById(R.id.de_list);
        LayoutInflater inflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View Header=inflater.inflate(R.layout.card_view_detail,null);
        View Footer=View.inflate(this,R.layout.footer,null);
        mListView.addHeaderView(Header);
        mListView.addFooterView(Footer,null,true);
        mListView.setAdapter(null);
        retweet=(Button) Header.findViewById(R.id.de_repeat);
        load_more=(TextView)Footer.findViewById(R.id.load_more);
        done=(LinearLayout)Footer.findViewById(R.id.done);
        Footer.setOnClickListener(new loadMoreOnClickListener());
        retweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {Toast.makeText(WeiboDetail.this, "点击头像短", Toast.LENGTH_LONG).show();}
        });

        final de.hdodenhof.circleimageview.CircleImageView profile=(de.hdodenhof.circleimageview.CircleImageView)Header.findViewById(R.id.de_profile);
        final TextView de_name=(TextView)Header.findViewById(R.id.de_name);
        final TextView de_content=(TextView)Header.findViewById(R.id.de_content);
        final TextView de_createdAt=(TextView)Header.findViewById(R.id.de_createdAt);
        final TextView de_source=(TextView)Header.findViewById(R.id.de_source);
        final RelativeLayout de_r14=(RelativeLayout)Header.findViewById(R.id.de_rl4);
        final MyGridView de_images1=(MyGridView)Header.findViewById(R.id.de_images1);
        final TextView de_retweet_detail=(TextView)Header.findViewById(R.id.de_retweet_detail);
        final LinearLayout de_retweet_content=(LinearLayout)Header.findViewById(R.id.de_retweet_content);
        final RelativeLayout de_rl5=(RelativeLayout)Header.findViewById(R.id.de_rl5);
        final MyGridView de_images2=(MyGridView)Header.findViewById(R.id.de_images2);
        final ImageView de_verified=(ImageView)Header.findViewById(R.id.de_verified);

        de_name.setText(list.user.name);
        de_content.setText(Tools.getContent(this,list.text,de_content));
        de_content.setMovementMethod(CustomLinkMovementMethod.getInstance());
        ImageLoader.getInstance().displayImage(list.user.avatar_large,profile,WeiboApplication.options);
        de_createdAt.setText(Tools.getTimeStr(Tools.strToDate(list.created_at), new Date()));
        de_source.setText("来自:"+list.getTextSource());
        //判断用户是否认证
        Tools.checkVerified(list.user,de_verified);
        //判断微博中是否有图片
        if (!Tools.isEmpty(list.thumbnail_pic)){
            final ArrayList<String> list2=list.pic_urls;
            de_r14.setVisibility(View.VISIBLE);
            Tools.initInfoImages(WeiboDetail.this,Tools.getWidth(WeiboDetail.this),de_images1,list2);
        }else {
            de_r14.setVisibility(View.GONE);
        }
        //转发内容是否为空
        if (list.retweeted_status!=null && list.retweeted_status.user!=null){
            de_retweet_content.setVisibility(View.VISIBLE);
            de_retweet_detail.setText(Tools.getContent(this,"@"+list.retweeted_status.user.name+
                    ":"+list.retweeted_status.text,de_retweet_detail));
            //转发图片是否有图片
            if (!Tools.isEmpty(list.retweeted_status.thumbnail_pic)){
                ArrayList<String> list2=list.retweeted_status.pic_urls;
                de_rl5.setVisibility(View.VISIBLE);
                Tools.initInfoImages(WeiboDetail.this,Tools.getWidth(WeiboDetail.this),de_images2,list2);
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

    }

    private void setAdapter(){
        mAdapter = new DetailPageListViewAdapter(WeiboDetail.this, mCommentList.commentList);
        mListView.setAdapter(mAdapter);
        mAdapter.setOnItemClickLitener(new DetailPageListViewAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position, long id) {
                switch (view.getId()){
                    case R.id.user_profile:
                        Toast.makeText(WeiboDetail.this, "点击头像短", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.de_detail:
                    case R.id.cardview_item:
                        Toast.makeText(WeiboDetail.this,"点击文章短1" , Toast.LENGTH_LONG).show();
                        break;
                }
            }

            @Override
            public void onItemLongClick(View view, int position, long id) {
                switch (view.getId()){
                    case R.id.cardview_item:
                        Toast.makeText(WeiboDetail.this,"点击文章长1" , Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
    }

    private void getComments(){
        mCommentsAPI.show(id, 0, MAX_ID, 20, 1, 0, new RequestListener() {
            @Override
            public void onComplete(String s) {
                if (!TextUtils.isEmpty(s)){
                    mCommentList=CommentList.parse(s);
                    if (mCommentList != null && mCommentList.total_number != 0) {
                        if (mAdapter == null){
                            setAdapter();
                            MAX_ID =Long.parseLong(mCommentList.commentList.get(mCommentList.commentList.size() - 1).mid)-1;
                        }else {
                            if (mAdapter.getCount() < mCommentList.total_number){
                                mAdapter.refresh(mCommentList.commentList);
                                visibleControl(false);
                                MAX_ID =Long.parseLong(mCommentList.commentList.get(mCommentList.commentList.size() - 1).mid)-1;
                            }else if (mAdapter.getCount() == mCommentList.total_number){
                                visibleControl(false);
                                load_more.setText("已无更多评论");
                            }
                        }
                    }else {
                        visibleControl(false);
                        load_more.setText("暂时无人评论");
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
        Oauth2AccessToken mAccessToken = AccessTokenKeeper.readAccessToken(this);
        // 获取用户信息接口
        mCommentsAPI=new CommentsAPI(this, Constants.APP_KEY,mAccessToken);
        WeiboApplication.getInstance();
        WeiboApplication.addActivity(this);
        bindView();
        getComments();
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

    private void visibleControl(boolean isShow){
        load_more.setVisibility(isShow?View.GONE:View.VISIBLE);
        done.setVisibility(isShow?View.VISIBLE:View.GONE);
    }

    public void loadMore(){
        getComments();
    }

    final class loadMoreOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            visibleControl(true);
            loadMore();
        }
    }
}
