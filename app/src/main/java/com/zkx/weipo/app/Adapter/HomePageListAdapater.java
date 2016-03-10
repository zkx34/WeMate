package com.zkx.weipo.app.adapter;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zkx.weipo.app.R;
import com.zkx.weipo.app.app.WeiboApplication;
import com.zkx.weipo.app.openapi.models.Status;
import com.zkx.weipo.app.openapi.models.StatusList;
import com.zkx.weipo.app.util.ContentTextView;
import com.zkx.weipo.app.util.CustomLinkMovementMethod;
import com.zkx.weipo.app.util.Tools;
import com.zkx.weipo.app.view.MyGridView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015/11/11.
 */
public class HomePageListAdapater extends BaseAdapter {

    private List<Status> mStatuslist;
    private LayoutInflater mInflater;
    private static Activity context;

    public HomePageListAdapater(StatusList mStatuslist, Activity context) {
        this.mStatuslist = mStatuslist.statusList;
        mInflater= LayoutInflater.from(context);
        HomePageListAdapater.context = context;
    }

    private static class ViewHolder{

        ImageView verified;
        RelativeLayout rl4;
        RelativeLayout rl5;
        LinearLayout insideContent;
        CardView cardView;
        ContentTextView content;
        TextView name;
        TextView time;
        TextView source;
        ContentTextView retweeted_detail;
        de.hdodenhof.circleimageview.CircleImageView userhead;
        Button btn_repeat;
        Button btn_comment;
        MyGridView gv_images;
        MyGridView re_images;
    }

    public void refresh(List<Status> newStatus){
        mStatuslist.addAll(newStatus);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mStatuslist ==null?0: mStatuslist.size();
    }

    @Override
    public Object getItem(int position) {
        return  mStatuslist==null?null: mStatuslist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong((mStatuslist.get(position).id));
    }

    public Object getRetweetedItem(int position){
        return mStatuslist.get(position).retweeted_status==null?null:mStatuslist.get(position).retweeted_status;
    }

    public long getRetweetedItemId(int position){
        return mStatuslist.get(position).retweeted_status==null?null:Long.parseLong((mStatuslist.get(position).retweeted_status.id));
    }

    public String getScreenName(int position){
        return mStatuslist.get(position).user.screen_name;
    }

    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position,long id);
        void onItemLongClick(View view , int position,long id);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup parent) {

        View v;
        if (convertView==null){
            v=mInflater.inflate(R.layout.card_view_main,null);
        }
        else {
            v=convertView;
        }
        final ViewHolder holder=new ViewHolder();
        Status list=mStatuslist.get(i);

        holder.rl4=(RelativeLayout)v.findViewById(R.id.rl4);
        holder.rl5=(RelativeLayout)v.findViewById(R.id.rl5);
        holder.cardView=(CardView)v.findViewById(R.id.id_CardView);
        holder.content=(ContentTextView)v.findViewById(R.id.id_content);
        holder.userhead=(de.hdodenhof.circleimageview.CircleImageView)v.findViewById(R.id.user_headimg);
        holder.name=(TextView)v.findViewById(R.id.id_name);
        holder.time=(TextView)v.findViewById(R.id.id_time);
        holder.source=(TextView)v.findViewById(R.id.id_source);
        holder.insideContent =(LinearLayout)v.findViewById(R.id.inside_content);
        holder.retweeted_detail=(ContentTextView)v.findViewById(R.id.id_retweeted_detail);
        holder.btn_repeat=(Button)v.findViewById(R.id.btn_repeat);
        holder.btn_comment=(Button)v.findViewById(R.id.btn_comment);
        holder.gv_images=(MyGridView)v.findViewById(R.id.gv_images);
        holder.re_images=(MyGridView)v.findViewById(R.id.re_images);
        holder.verified=(ImageView)v.findViewById(R.id.verified);
        holder.content.setText(
                Tools.getContent(context,list.text,holder.content));
        holder.content.setMovementMethod(CustomLinkMovementMethod.getInstance());
        holder.name.setText(list.user.name);
        holder.time.setText(Tools.getTimeStr(Tools.strToDate(list.created_at), new Date()));
        holder.source.setText("来自:"+list.getTextSource());
        ImageLoader.getInstance().displayImage(list.user.avatar_large, holder.userhead, WeiboApplication.options);

        //判断用户是否认证
        Tools.checkVerified(list.user,holder.verified);

        //判断微博中是否有图片
        if (!Tools.isEmpty(list.thumbnail_pic)){
            ArrayList<String> list1=list.pic_urls;
            holder.rl4.setVisibility(View.VISIBLE);
            Tools.initInfoImages(context,Tools.getWidth(context),holder.gv_images,list1);
        }else {
            holder.rl4.setVisibility(View.GONE);
        }

        //转发内容是否为空
        if (list.retweeted_status!=null
                &&list.retweeted_status.user!=null){
            holder.insideContent.setVisibility(View.VISIBLE);
            holder.retweeted_detail.setText(
                    Tools.getContent(context,"@"+list.retweeted_status.user.name+
                            ":"+list.retweeted_status.text,holder.retweeted_detail));
            holder.retweeted_detail.setMovementMethod(CustomLinkMovementMethod.getInstance());
            //转发图片是否有图片
            if (!Tools.isEmpty(list.retweeted_status.thumbnail_pic)){
                ArrayList<String> list2=list.retweeted_status.pic_urls;
                holder.rl5.setVisibility(View.VISIBLE);
                Tools.initInfoImages(context,Tools.getWidth(context),holder.re_images,list2);
            }else {
                holder.rl5.setVisibility(View.GONE);
            }
        }else
        if (list.retweeted_status!=null && list.retweeted_status.user==null){
            holder.rl5.setVisibility(View.GONE);
            holder.insideContent.setVisibility(View.VISIBLE);
            holder.retweeted_detail.setText(R.string.retweed_error);
        }else {
            holder.insideContent.setVisibility(View.GONE);
        }
        //为Item项设置点击事件

        holder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id=getItemId(i);
                mOnItemClickLitener.onItemClick(holder.content,i,id);
            }
        });

        holder.retweeted_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id=getRetweetedItemId(i);
                mOnItemClickLitener.onItemClick(holder.retweeted_detail,i,id);
            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id=getItemId(i);
                mOnItemClickLitener.onItemClick(holder.cardView,i,id);
            }
        });

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                long id=getItemId(i);
                mOnItemClickLitener.onItemLongClick(holder.cardView,i,id);
                return false;
            }
        });

        holder.btn_repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id=getItemId(i);
                mOnItemClickLitener.onItemLongClick(holder.btn_repeat,i,id);
            }
        });

        holder.btn_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id=getItemId(i);
                mOnItemClickLitener.onItemLongClick(holder.btn_comment,i,id);
            }
        });

        holder.userhead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id=getItemId(i);
                mOnItemClickLitener.onItemLongClick(holder.userhead,i,id);
            }
        });

        return v;
    }
}
