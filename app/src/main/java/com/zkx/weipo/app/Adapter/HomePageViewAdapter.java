package com.zkx.weipo.app.adapter;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.zkx.weipo.app.R;
import com.zkx.weipo.app.util.StringUtil;
import com.zkx.weipo.app.util.SysUtils;
import com.zkx.weipo.app.util.Tools;
import com.zkx.weipo.app.app.WeiboApplication;
import com.zkx.weipo.app.openapi.models.Status;
import com.zkx.weipo.app.openapi.models.StatusList;
import com.zkx.weipo.app.view.MyGridView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015/9/12.
 */
public class HomePageViewAdapter extends RecyclerView.Adapter<HomePageViewAdapter.ViewHolder> {

    StatusList testDatas;
    private static Activity context;
    private static int wh;

    public HomePageViewAdapter(Activity context, StatusList testDatas) {
        this.testDatas = testDatas;
        this.context = context;
        this.wh=(SysUtils.getScreenWidth(context)- SysUtils.Dp2Px(context, 99))/3;
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
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {

        viewHolder.content.setText(Html.fromHtml(Tools.atBlue(testDatas.statusList.get(i).text)));
        viewHolder.name.setText(testDatas.statusList.get(i).user.name);
        viewHolder.time.setText(Tools.getTimeStr(Tools.strToDate(testDatas.statusList.get(i).created_at), new Date()));
        viewHolder.source.setText("来自:"+testDatas.statusList.get(i).getTextSource());
        WeiboApplication.IMAGE_CACHE.get(testDatas.statusList.get(i).user.profile_image_url, viewHolder.userhead);

        if (mOnItemClickLitener!=null){

            viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=viewHolder.getLayoutPosition();
                    long id=getId(pos);
                    mOnItemClickLitener.onItemClick(viewHolder.cardView,pos,id);
                }
            });

            viewHolder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos=viewHolder.getLayoutPosition();
                    long id=getId(pos);
                    mOnItemClickLitener.onItemLongClick(viewHolder.cardView,pos,id);
                    return false;
                }
            });

            viewHolder.btn_repeat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=viewHolder.getLayoutPosition();
                    long id=getId(pos);
                    mOnItemClickLitener.onItemLongClick(viewHolder.btn_repeat,pos,id);
                }
            });

            viewHolder.btn_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=viewHolder.getLayoutPosition();
                    long id=getId(pos);
                    mOnItemClickLitener.onItemLongClick(viewHolder.btn_comment,pos,id);
                }
            });

            viewHolder.userhead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=viewHolder.getLayoutPosition();
                    long id=getId(pos);
                    mOnItemClickLitener.onItemLongClick(viewHolder.userhead,pos,id);
                }
            });
        }

        //判断微博中是否有图片
        if (!StringUtil.isEmpty(testDatas.statusList.get(i).thumbnail_pic)){
            ArrayList<String> list=testDatas.statusList.get(i).pic_urls;
            viewHolder.rl4.setVisibility(View.VISIBLE);
            initInfoImages(viewHolder.gv_images,list);
        }else {
            viewHolder.rl4.setVisibility(View.GONE);
        }

        //转发内容是否为空
        if (testDatas.statusList.get(i).retweeted_status!=null
                &&testDatas.statusList.get(i).retweeted_status.user!=null){
            viewHolder.insideContent.setVisibility(View.VISIBLE);
            viewHolder.retweeted_detail.setText(Html.fromHtml(Tools.atBlue("@"+testDatas.statusList.get(i).retweeted_status.user.name+
                    ":"+testDatas.statusList.get(i).retweeted_status.text)));
            //转发图片是否有图片
            if (!StringUtil.isEmpty(testDatas.statusList.get(i).retweeted_status.thumbnail_pic)){
                ArrayList<String> list=testDatas.statusList.get(i).retweeted_status.pic_urls;
                viewHolder.rl5.setVisibility(View.VISIBLE);
                initInfoImages(viewHolder.re_images,list);
            }else {
                viewHolder.rl5.setVisibility(View.GONE);
            }
        }else {
            viewHolder.insideContent.setVisibility(View.GONE);
        }
    }

    public void refresh(List<Status> newStatus){
        testDatas.statusList.addAll(newStatus);
        this.notifyDataSetChanged();
    }

    public long getId(int position){
        return Long.parseLong((testDatas.statusList.get(position).id));
    }

    @Override
    public int getItemCount() {
        return testDatas==null?0:testDatas.statusList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {

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

        public ViewHolder(View itemView) {
            super(itemView);
            rl4=(RelativeLayout)itemView.findViewById(R.id.rl4);
            rl5=(RelativeLayout)itemView.findViewById(R.id.rl5);
            cardView=(CardView)itemView.findViewById(R.id.id_CardView);
            content=(TextView)itemView.findViewById(R.id.id_content);
            userhead=(de.hdodenhof.circleimageview.CircleImageView)itemView.findViewById(R.id.user_headimg);
            name=(TextView)itemView.findViewById(R.id.id_name);
            time=(TextView)itemView.findViewById(R.id.id_time);
            source=(TextView)itemView.findViewById(R.id.id_source);
            insideContent =(LinearLayout)itemView.findViewById(R.id.inside_content);
            retweeted_detail=(TextView)itemView.findViewById(R.id.id_retweeted_detail);
            btn_repeat=(Button)itemView.findViewById(R.id.btn_repeat);
            btn_comment=(Button)itemView.findViewById(R.id.btn_comment);
            gv_images=(MyGridView)itemView.findViewById(R.id.gv_images);
            re_images=(MyGridView)itemView.findViewById(R.id.re_images);
        }
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
                    w=2*wh+SysUtils.Dp2Px(context, 2);
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
                    Toast.makeText(context, "点击了第"+(arg2+1)+"张图片", Toast.LENGTH_LONG).show();
                }
            });
        }

    }

}
