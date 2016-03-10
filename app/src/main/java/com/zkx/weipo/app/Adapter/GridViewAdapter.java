package com.zkx.weipo.app.adapter;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import com.facebook.drawee.view.SimpleDraweeView;
import com.zkx.weipo.app.R;
import com.zkx.weipo.app.util.Tools;

import java.util.ArrayList;

public class GridViewAdapter extends BaseAdapter {

	Activity context;
	ArrayList<String> list;
	private int wh;

	public interface OnItemClickLitener
	{
		void onItemClick(View view, int position);
	}

	private OnItemClickLitener mOnItemClickLitener;

	public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
	{
		this.mOnItemClickLitener = mOnItemClickLitener;
	}

	public GridViewAdapter(Activity context, ArrayList<String> data) {
		this.context=context;
		this.list=data;
		this.wh= Tools.getWidth(context);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int position, View view, ViewGroup arg2) {
		Holder holder;
		if (null == view) {
			holder = new Holder();
			LayoutInflater mInflater = LayoutInflater.from(context);
			view = mInflater.inflate(R.layout.item_gridview, arg2, false);
            /*根据parent动态设置convertview的大小*/
			view.setLayoutParams(new AbsListView.LayoutParams((arg2.getWidth() / 3) - 1,(arg2.getHeight() / 2)));// 动态设置item的高度
			ViewGroup.LayoutParams params=view.getLayoutParams();
			params.width=wh;
			params.height=wh;
			view.setLayoutParams(params);
			holder.imageView = (SimpleDraweeView) view.findViewById(R.id.imageView);
			view.setTag(holder);
		} else {
			holder = (Holder) view.getTag();
		}
		final SimpleDraweeView draweeView = holder.imageView;
		Uri uri=Uri.parse(list.get(position));
		draweeView.setImageURI(uri);
		draweeView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mOnItemClickLitener.onItemClick(draweeView,position);
			}
		});
		return view;
	}

	class Holder{
		SimpleDraweeView imageView;
	}

}
