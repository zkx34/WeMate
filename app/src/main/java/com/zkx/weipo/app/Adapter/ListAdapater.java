package com.zkx.weipo.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.zkx.weipo.app.R;
import com.zkx.weipo.app.app.WeiboApplication;
import com.zkx.weipo.app.openapi.models.Comment;
import com.zkx.weipo.app.openapi.models.CommentList;
import com.zkx.weipo.app.util.Tools;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015/11/11.
 */
public class ListAdapater extends BaseAdapter {

    private CommentList mCommentList;
    private LayoutInflater mInflater;

    private static class ViewHolder{
        de.hdodenhof.circleimageview.CircleImageView user_profile;
        TextView tv_username;
        TextView tv_createdAt;
        TextView de_detail;
    }

    private void bindView(int position,View view){

        List<Comment> commentList=this.mCommentList.commentList;
        ViewHolder mHolder=new ViewHolder();
        mHolder.user_profile=(de.hdodenhof.circleimageview.CircleImageView)view.findViewById(R.id.user_profile);
        mHolder.tv_username=(TextView)view.findViewById(R.id.tv_username);
        mHolder.tv_createdAt=(TextView)view.findViewById(R.id.tv_createdAt);
        mHolder.de_detail=(TextView)view.findViewById(R.id.de_detail);

        mHolder.tv_username.setText(commentList.get(position).user.name);
        WeiboApplication.IMAGE_CACHE.get(commentList.get(position).user.profile_image_url,mHolder.user_profile);
        mHolder.tv_createdAt.setText(Tools.getTimeStr(Tools.strToDate(commentList.get(position).created_at), new Date()));
        mHolder.de_detail.setText(commentList.get(position).text);

    }

    private View createViewFromResource(int position,View convertView){
        View v;
        if (convertView == null) {
            v = mInflater.inflate(R.layout.item_detail, null);
        } else {
            v = convertView;
        }
        bindView(position, v);
        return v;
    }

    public ListAdapater(CommentList mStatusLists) {
        this.mCommentList = mStatusLists;
    }

    @Override
    public int getCount() {
        return mCommentList ==null?0: mCommentList.commentList.size();
    }

    @Override
    public Object getItem(int position) {
        return  mCommentList.commentList==null?null: mCommentList.commentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong((mCommentList.commentList.get(position).id));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position,convertView);
    }

}
