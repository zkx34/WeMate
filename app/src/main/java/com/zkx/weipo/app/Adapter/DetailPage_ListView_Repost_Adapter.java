package com.zkx.weipo.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zkx.weipo.app.R;
import com.zkx.weipo.app.app.WeiboApplication;
import com.zkx.weipo.app.openapi.models.Reposts;
import com.zkx.weipo.app.util.ContentTextView;
import com.zkx.weipo.app.util.CustomLinkMovementMethod;
import com.zkx.weipo.app.util.Tools;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/3/12.
 */
public class DetailPage_ListView_Repost_Adapter extends BaseAdapter {


    private List<Reposts> mReposts;
    private LayoutInflater mInflater;
    private static Activity context;
    private OnItemClickLitener mOnItemClickLitener;

    public DetailPage_ListView_Repost_Adapter(Activity context,List<Reposts> mReposts) {

        this.context=context;
        this.mReposts = mReposts;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);;
    }

    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position,long id);
        void onItemLongClick(View view , int position,long id);
    }

    public void refresh(List<Reposts> reposts){
        mReposts.addAll(reposts);
        this.notifyDataSetChanged();
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public int getCount() {
        return mReposts==null?0:mReposts.size();
    }

    @Override
    public Object getItem(int position) {
        return mReposts==null?null:mReposts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(mReposts.get(position).idstr);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v;
        if (convertView==null){
            v=mInflater.inflate(R.layout.item_detail,null);
        }
        else {
            v=convertView;
        }
        final ViewHolder holder=new ViewHolder();
        holder.user_profile=(de.hdodenhof.circleimageview.CircleImageView)v.findViewById(R.id.user_profile);
        holder.tv_username=(TextView)v.findViewById(R.id.tv_username);
        holder.tv_createdAt=(TextView)v.findViewById(R.id.tv_createdAt);
        holder.de_detail=(ContentTextView)v.findViewById(R.id.de_detail);
        holder.cardview_item=(CardView)v.findViewById(R.id.cardview_item);
        if (mReposts!=null){
            holder.tv_username.setText(mReposts.get(position).user.name);
            ImageLoader.getInstance().displayImage(mReposts.get(position).user.profile_image_url,holder.user_profile, WeiboApplication.options);
            holder.tv_createdAt.setText(Tools.getTimeStr(Tools.strToDate(mReposts.get(position).created_at), new Date()));
            holder.de_detail.setText(Tools.getContent(context,mReposts.get(position).text,holder.de_detail));
            holder.de_detail.setMovementMethod(CustomLinkMovementMethod.getInstance());
        }

        holder.cardview_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id=getItemId(position);
                mOnItemClickLitener.onItemClick(holder.cardview_item,position,id);
            }
        });

        holder.cardview_item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                long id=getItemId(position);
                mOnItemClickLitener.onItemLongClick(holder.cardview_item,position,id);
                return false;
            }
        });

        holder.de_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id=getItemId(position);
                mOnItemClickLitener.onItemClick(holder.de_detail,position,id);
            }
        });

        holder.user_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id=getItemId(position);
                mOnItemClickLitener.onItemClick(holder.user_profile,position,id);
            }
        });

        return v;
    }

    private static class ViewHolder{
        de.hdodenhof.circleimageview.CircleImageView user_profile;
        TextView tv_username;
        TextView tv_createdAt;
        ContentTextView de_detail;
        CardView cardview_item;
    }
}
