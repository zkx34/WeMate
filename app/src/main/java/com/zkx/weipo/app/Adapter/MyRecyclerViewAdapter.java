package com.zkx.weipo.app.Adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.zkx.weipo.app.R;
import com.zkx.weipo.app.Util.StringUtil;
import com.zkx.weipo.app.Util.Tools;
import com.zkx.weipo.app.app.WeiboApplication;
import com.zkx.weipo.app.openapi.models.StatusList;

import java.util.Date;

/**
 * Created by Administrator on 2015/9/12.
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    StatusList testDatas;


    public MyRecyclerViewAdapter(StatusList testDatas) {
        this.testDatas = testDatas;
    }

    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);
        void onItemLongClick(View view , int position);
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
        //SimpleImageLoader.showImg(viewHolder.userhead, testDatas.statusList.get(i).user.profile_image_url);
        WeiboApplication.IMAGE_CACHE.get(testDatas.statusList.get(i).user.profile_image_url, viewHolder.userhead);

        if (mOnItemClickLitener!=null){
            viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=viewHolder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(viewHolder.cardView,pos);
                }
            });

            viewHolder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos=viewHolder.getLayoutPosition();
                    mOnItemClickLitener.onItemLongClick(viewHolder.cardView,pos);
                    return false;
                }
            });

            viewHolder.btn_repeat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=viewHolder.getLayoutPosition();
                    mOnItemClickLitener.onItemLongClick(viewHolder.btn_repeat,pos);
                }
            });

            viewHolder.btn_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=viewHolder.getLayoutPosition();
                    mOnItemClickLitener.onItemLongClick(viewHolder.btn_comment,pos);
                }
            });

            viewHolder.userhead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=viewHolder.getLayoutPosition();
                    mOnItemClickLitener.onItemLongClick(viewHolder.userhead,pos);
                }
            });
        }

        //判断微博中是否有图片
        if (!StringUtil.isEmpty(testDatas.statusList.get(i).thumbnail_pic)){
            viewHolder.content_img.setVisibility(View.VISIBLE);
            //SimpleImageLoader.showImg(viewHolder.content_img,testDatas.statusList.get(i).thumbnail_pic);
            WeiboApplication.IMAGE_CACHE.get(testDatas.statusList.get(i).thumbnail_pic,viewHolder.content_img);
        }else {
            viewHolder.content_img.setVisibility(View.GONE);
        }

        //转发内容是否为空
        if (testDatas.statusList.get(i).retweeted_status!=null
                &&testDatas.statusList.get(i).retweeted_status.user!=null){
            viewHolder.insideContent.setVisibility(View.VISIBLE);
            viewHolder.retweeted_detail.setText(Html.fromHtml(Tools.atBlue("@"+testDatas.statusList.get(i).retweeted_status.user.name+
                    ":"+testDatas.statusList.get(i).retweeted_status.text)));
            //转发图片是否有图片
            if (!StringUtil.isEmpty(testDatas.statusList.get(i).retweeted_status.thumbnail_pic)){
                viewHolder.retweeted_img.setVisibility(View.VISIBLE);
                //SimpleImageLoader.showImg(viewHolder.retweeted_img,testDatas.statusList.get(i).retweeted_status.thumbnail_pic);
                WeiboApplication.IMAGE_CACHE.get(testDatas.statusList.get(i).retweeted_status.thumbnail_pic,viewHolder.retweeted_img);
            }else {
                viewHolder.retweeted_img.setVisibility(View.GONE);
            }
        }else {
            viewHolder.insideContent.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return testDatas==null?0:testDatas.statusList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {

        LinearLayout insideContent;
        CardView cardView;
        TextView content;
        TextView name;
        TextView time;
        TextView source;
        TextView retweeted_detail;
        ImageView userhead;
        ImageView content_img;
        ImageView retweeted_img;
        Button btn_repeat;
        Button btn_comment;


        public ViewHolder(View itemView) {
            super(itemView);
            cardView=(CardView)itemView.findViewById(R.id.id_CardView);
            content=(TextView)itemView.findViewById(R.id.id_content);
            userhead=(ImageView)itemView.findViewById(R.id.user_headimg);
            name=(TextView)itemView.findViewById(R.id.id_name);
            time=(TextView)itemView.findViewById(R.id.id_time);
            source=(TextView)itemView.findViewById(R.id.id_source);
            insideContent =(LinearLayout)itemView.findViewById(R.id.inside_content);
            retweeted_detail=(TextView)itemView.findViewById(R.id.id_retweeted_detail);
            content_img=(ImageView)itemView.findViewById(R.id.content_img);
            retweeted_img=(ImageView)itemView.findViewById(R.id.retweeted_img);
            btn_repeat=(Button)itemView.findViewById(R.id.btn_repeat);
            btn_comment=(Button)itemView.findViewById(R.id.btn_comment);
        }
    }
}
