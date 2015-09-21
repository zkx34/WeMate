package com.zkx.weipo.app;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
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
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.content.setText(Html.fromHtml(Tools.atBlue(testDatas.statusList.get(i).text)));
        viewHolder.name.setText(testDatas.statusList.get(i).user.name);
        viewHolder.time.setText(Tools.getTimeStr(Tools.strToDate(testDatas.statusList.get(i).created_at), new Date()));
        viewHolder.source.setText("来自:"+testDatas.statusList.get(i).getTextSource());
        if (testDatas.statusList.get(i).retweeted_status!=null
                &&testDatas.statusList.get(i).retweeted_status.user!=null){
            viewHolder.insideContent.setVisibility(View.VISIBLE);
            viewHolder.retweeted_detail.setText(Html.fromHtml(Tools.atBlue("@"+testDatas.statusList.get(i).retweeted_status.user.name+
                    ":"+testDatas.statusList.get(i).retweeted_status.text)));
        }else {
            viewHolder.insideContent.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return testDatas==null?0:testDatas.statusList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout insideContent;
        CardView cardView;
        TextView content;
        TextView name;
        TextView time;
        TextView source;
        TextView retweeted_detail;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView=(CardView)itemView.findViewById(R.id.id_CardView);
            content=(TextView)itemView.findViewById(R.id.id_content);
            name=(TextView)itemView.findViewById(R.id.id_name);
            time=(TextView)itemView.findViewById(R.id.id_time);
            source=(TextView)itemView.findViewById(R.id.id_source);
            insideContent =(LinearLayout)itemView.findViewById(R.id.inside_content);
            retweeted_detail=(TextView)itemView.findViewById(R.id.id_retweeted_detail);
        }
    }
}
