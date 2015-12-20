package com.zkx.weipo.app.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zkx.weipo.app.ImagePagerActivity;
import com.zkx.weipo.app.R;
import com.zkx.weipo.app.app.WeiboApplication;
import com.zkx.weipo.app.openapi.models.Status;
import com.zkx.weipo.app.openapi.models.StatusList;
import com.zkx.weipo.app.util.StringUtil;
import com.zkx.weipo.app.util.SysUtils;
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
    private StatusList statusList;
    private LayoutInflater mInflater;
    private static Activity context;
    private static int wh;

    public HomePageListAdapater(StatusList mStatuslist, Activity context) {
        this.statusList=mStatuslist;
        this.mStatuslist = mStatuslist.statusList;
        mInflater= LayoutInflater.from(context);
        HomePageListAdapater.context = context;
        wh=(SysUtils.getScreenWidth(context)- SysUtils.Dp2Px(context, 99))/3;
    }

    private static class ViewHolder{

        ImageView verified;
        RelativeLayout rl4;
        RelativeLayout rl5;
        LinearLayout insideContent;
        CardView cardView;
        TextView content;
        TextView name;
        TextView time;
        TextView source;
        TextView retweeted_detail;
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
            v=mInflater.inflate(R.layout.card_view,null);
        }
        else {
            v=convertView;
        }
        final ViewHolder holder=new ViewHolder();

        holder.rl4=(RelativeLayout)v.findViewById(R.id.rl4);
        holder.rl5=(RelativeLayout)v.findViewById(R.id.rl5);
        holder.cardView=(CardView)v.findViewById(R.id.id_CardView);
        holder.content=(TextView)v.findViewById(R.id.id_content);
        holder.userhead=(de.hdodenhof.circleimageview.CircleImageView)v.findViewById(R.id.user_headimg);
        holder.name=(TextView)v.findViewById(R.id.id_name);
        holder.time=(TextView)v.findViewById(R.id.id_time);
        holder.source=(TextView)v.findViewById(R.id.id_source);
        holder.insideContent =(LinearLayout)v.findViewById(R.id.inside_content);
        holder.retweeted_detail=(TextView)v.findViewById(R.id.id_retweeted_detail);
        holder.btn_repeat=(Button)v.findViewById(R.id.btn_repeat);
        holder.btn_comment=(Button)v.findViewById(R.id.btn_comment);
        holder.gv_images=(MyGridView)v.findViewById(R.id.gv_images);
        holder.re_images=(MyGridView)v.findViewById(R.id.re_images);
        holder.verified=(ImageView)v.findViewById(R.id.verified);
        holder.content.setText(Html.fromHtml(Tools.atBlue(mStatuslist.get(i).text)));
        holder.name.setText(mStatuslist.get(i).user.name);
        holder.time.setText(Tools.getTimeStr(Tools.strToDate(mStatuslist.get(i).created_at), new Date()));
        holder.source.setText("来自:"+mStatuslist.get(i).getTextSource());
        ImageLoader.getInstance().displayImage(mStatuslist.get(i).user.avatar_large, holder.userhead, WeiboApplication.options);

        //判断用户是否认证
        if (mStatuslist.get(i).user.verified){
            switch (mStatuslist.get(i).user.verified_type){
                case 0:
                    holder.verified.setImageResource(R.mipmap.avatar_vip);
                    holder.verified.setVisibility(View.VISIBLE);
                    break;
                case -1:
                    holder.verified.setVisibility(View.GONE);
                    break;
                default:
                    holder.verified.setImageResource(R.mipmap.avatar_enterprise_vip);
                    holder.verified.setVisibility(View.VISIBLE);
            }
        }else if (mStatuslist.get(i).user.verified_type==200 || mStatuslist.get(i).user.verified_type==220){
            holder.verified.setImageResource(R.mipmap.avatar_grassroot);
            holder.verified.setVisibility(View.VISIBLE);
        }else {
            holder.verified.setVisibility(View.GONE);
        }

        //判断微博中是否有图片
        if (!StringUtil.isEmpty(mStatuslist.get(i).thumbnail_pic)){
            ArrayList<String> list=mStatuslist.get(i).pic_urls;
            holder.rl4.setVisibility(View.VISIBLE);
            initInfoImages(holder.gv_images,list);
        }else {
            holder.rl4.setVisibility(View.GONE);
        }

        //转发内容是否为空
        if (mStatuslist.get(i).retweeted_status!=null
                &&mStatuslist.get(i).retweeted_status.user!=null){
            holder.insideContent.setVisibility(View.VISIBLE);
            holder.retweeted_detail.setText(Html.fromHtml(Tools.atBlue("@"+mStatuslist.get(i).retweeted_status.user.name+
                    ":"+mStatuslist.get(i).retweeted_status.text)));
            //转发图片是否有图片
            if (!StringUtil.isEmpty(mStatuslist.get(i).retweeted_status.thumbnail_pic)){
                ArrayList<String> list=mStatuslist.get(i).retweeted_status.pic_urls;
                holder.rl5.setVisibility(View.VISIBLE);
                initInfoImages(holder.re_images,list);
            }else {
                holder.rl5.setVisibility(View.GONE);
            }
        }else {
            holder.insideContent.setVisibility(View.GONE);
        }
        //为Item项设置点击事件
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

    public static void initInfoImages(MyGridView gv_images, final ArrayList<String> list){
        if(list!=null&&!list.equals("")){
            int w=0;
            switch (list.size()) {
                case 1:
                    w=wh;
                    gv_images.setNumColumns(1);
                    break;
                case 2:
                case 4:
                    w=2*wh+ SysUtils.Dp2Px(context, 2);
                    gv_images.setNumColumns(2);
                    break;
                case 3:
                case 5:
                case 6:
                    w=wh*3+SysUtils.Dp2Px(context, 2)*2;
                    gv_images.setNumColumns(3);
                    break;
                case 7:
                case 8:
                case 9:
                    w=wh*3+SysUtils.Dp2Px(context, 2)*2;
                    gv_images.setNumColumns(3);
                    break;
            }
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(w, RelativeLayout.LayoutParams.WRAP_CONTENT);
            gv_images.setLayoutParams(lp);
            GridViewAdapter nearByInfoImgsAdapter = new GridViewAdapter(context, list);
            gv_images.setAdapter(nearByInfoImgsAdapter);
            gv_images.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
                    imageBrower(arg2,Tools.getOriginalPicUrls(list));
                }
            });
        }
    }

    protected static void imageBrower(int position, ArrayList<String> urls2) {
        Intent intent = new Intent(context, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        context.startActivity(intent);
    }
}
