package com.zkx.weipo.app;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015/9/12.
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    List<TestData> testDatas;

    public MyRecyclerViewAdapter(List<TestData> testDatas) {
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
        viewHolder.content.setText(testDatas.get(i).content);
        viewHolder.name.setText(testDatas.get(i).name);
        viewHolder.time.setText(Tools.getTimeStr(testDatas.get(i).time, new Date()));
        //Tools.getTimeStr(status.getCreatedAt(), new Date())
        viewHolder.source.setText(testDatas.get(i).source);
    }

    @Override
    public int getItemCount() {
        return testDatas==null?0:testDatas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView content;
        TextView name;
        TextView time;
        TextView source;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView=(CardView)itemView.findViewById(R.id.id_CardView);
            content=(TextView)itemView.findViewById(R.id.id_content);
            name=(TextView)itemView.findViewById(R.id.id_name);
            time=(TextView)itemView.findViewById(R.id.id_time);
            source=(TextView)itemView.findViewById(R.id.id_source);
        }
    }
}
