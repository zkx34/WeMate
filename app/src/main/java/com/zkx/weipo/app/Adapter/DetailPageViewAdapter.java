package com.zkx.weipo.app.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.zkx.weipo.app.R;
import com.zkx.weipo.app.app.WeiboApplication;
import com.zkx.weipo.app.openapi.models.Comment;
import com.zkx.weipo.app.openapi.models.CommentList;
import com.zkx.weipo.app.util.Tools;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015/11/8.
 */
public class DetailPageViewAdapter extends RecyclerView.Adapter<DetailPageViewAdapter.ViewHolder> {

    private CommentList mCommentList;
    // private StatusList mStatusLists;

    public DetailPageViewAdapter(CommentList mCommentList) {
        this.mCommentList = mCommentList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(ViewHolder mHolder, int position) {

        List<Comment> commentList=this.mCommentList.commentList;
        if (commentList!=null){
            mHolder.tv_username.setText(commentList.get(position).user.name);
            WeiboApplication.IMAGE_CACHE.get(commentList.get(position).user.profile_image_url,mHolder.user_profile);
            mHolder.tv_createdAt.setText(Tools.getTimeStr(Tools.strToDate(commentList.get(position).created_at), new Date()));
            mHolder.de_detail.setText(commentList.get(position).text);
        }
/*
        holder.de_name.setText(mStatusLists.statusList.get(0).user.name);
        holder.de_content.setText(Html.fromHtml(Tools.atBlue(mStatusLists.statusList.get(0).text)));
        WeiboApplication.IMAGE_CACHE.get(mStatusLists.statusList.get(0).user.profile_image_url,holder.profile);
        holder.de_createdAt.setText(Tools.getTimeStr(Tools.strToDate(mStatusLists.statusList.get(0).created_at), new Date()));
        holder.de_source.setText("来自:"+mStatusLists.statusList.get(0).getTextSource());

        //判断微博中是否有图片
        if (!StringUtil.isEmpty(mStatusLists.statusList.get(0).thumbnail_pic)){
            ArrayList<String> list=mStatusLists.statusList.get(0).pic_urls;
            holder.de_r14.setVisibility(View.VISIBLE);
            HomePageViewAdapter.initInfoImages(holder.de_images1,list);
        }else {
            holder.de_r14.setVisibility(View.GONE);
        }

        //转发内容是否为空
        if (mStatusLists.statusList.get(0).retweeted_status!=null
                &&mStatusLists.statusList.get(0).retweeted_status.user!=null){
            holder.de_retweet_content.setVisibility(View.VISIBLE);
            holder.de_retweet_detail.setText(Html.fromHtml(Tools.atBlue("@"+mStatusLists.statusList.get(0).retweeted_status.user.name+
                    ":"+mStatusLists.statusList.get(0).retweeted_status.text)));
            //转发图片是否有图片
            if (!StringUtil.isEmpty(mStatusLists.statusList.get(0).retweeted_status.thumbnail_pic)){
                ArrayList<String> list=mStatusLists.statusList.get(0).retweeted_status.pic_urls;
                holder.de_rl5.setVisibility(View.VISIBLE);
                HomePageViewAdapter.initInfoImages(holder.de_images2,list);
            }else {
                holder.de_rl5.setVisibility(View.GONE);
            }
        }else {
            holder.de_retweet_content.setVisibility(View.GONE);
        }

        if (mStatusLists.statusList.get(0).comments_count==0 && mStatusLists.statusList.get(0).reposts_count==0 ){
            holder.rLayout.setVisibility(View.VISIBLE);
        }else {
            holder.rLayout.setVisibility(View.GONE);
        }*/
    }

    @Override
    public int getItemCount() {
        return mCommentList==null?0:mCommentList.commentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        /*
                de.hdodenhof.circleimageview.CircleImageView profile;
                TextView de_name;
                TextView de_content;
                TextView de_createdAt;
                TextView de_source;
                RelativeLayout de_r14;
                MyGridView de_images1;
                TextView de_retweet_detail;
                LinearLayout de_retweet_content;
                RelativeLayout de_rl5;
                MyGridView de_images2;
                Button de_repeat;
                Button de_comment;
                RelativeLayout rLayout;
        */

        de.hdodenhof.circleimageview.CircleImageView user_profile;
        TextView tv_username;
        TextView tv_createdAt;
        TextView de_detail;

        public ViewHolder(View itemView) {
            super(itemView);
            user_profile=(de.hdodenhof.circleimageview.CircleImageView)itemView.findViewById(R.id.user_profile);
            tv_username=(TextView)itemView.findViewById(R.id.tv_username);
            tv_createdAt=(TextView)itemView.findViewById(R.id.tv_createdAt);
            de_detail=(TextView)itemView.findViewById(R.id.de_detail);
          /*  profile=(de.hdodenhof.circleimageview.CircleImageView)itemView.findViewById(R.id.de_profile);
            de_name=(TextView)itemView.findViewById(R.id.de_name);
            de_content=(TextView)itemView.findViewById(R.id.de_content);
            de_createdAt=(TextView)itemView.findViewById(R.id.de_createdAt);
            de_source=(TextView)itemView.findViewById(R.id.de_source);
            de_r14=(RelativeLayout)itemView.findViewById(R.id.de_rl4);
            de_images1=(MyGridView)itemView.findViewById(R.id.de_images1);
            de_retweet_detail=(TextView)itemView.findViewById(R.id.de_retweet_detail);
            de_retweet_content=(LinearLayout)itemView.findViewById(R.id.de_retweet_content);
            de_rl5=(RelativeLayout)itemView.findViewById(R.id.de_rl5);
            de_images2=(MyGridView)itemView.findViewById(R.id.de_images2);
            de_repeat=(Button)itemView.findViewById(R.id.de_repeat);
            de_comment=(Button)itemView.findViewById(R.id.de_comment);
            rLayout=(RelativeLayout)itemView.findViewById(R.id.rl_1);*/
        }
    }
}
