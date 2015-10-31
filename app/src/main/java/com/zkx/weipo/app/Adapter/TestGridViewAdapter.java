package com.zkx.weipo.app.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.zkx.weipo.app.R;
import com.zkx.weipo.app.Util.SysUtils;
import com.zkx.weipo.app.app.WeiboApplication;
import net.tsz.afinal.FinalBitmap;

import java.util.ArrayList;

public class TestGridViewAdapter extends BaseAdapter {

	Activity context;
	ArrayList<String> list;
	private FinalBitmap finalImageLoader;
	private int wh;

	public TestGridViewAdapter(Activity context,ArrayList<String> data) {
		this.context=context;
		this.wh=(SysUtils.getScreenWidth(context)-SysUtils.Dp2Px(context, 99))/3;
		this.list=data;
		//this.finalImageLoader=FinalBitmap.create(context);
		//this.finalImageLoader.configLoadingImage(R.drawable.surprise_picturebutton);//默认显示图片
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
			holder.imageView = (ImageView) view.findViewById(R.id.imageView);
			view.setTag(holder);
		} else {
			holder = (Holder) view.getTag();
		}
		// _Holder.btn_gv_item.setText(mLists.get(position));
		/*Holder holder;
		if (view==null) {
			view=LayoutInflater.from(context).inflate(R.layout.item_gridview, null);
			holder=new Holder();
			holder.imageView=(ImageView) view.findViewById(R.id.imageView);
			view.setTag(holder);
		}else {
			holder= (Holder) view.getTag();
		}*/
		//finalImageLoader.display(holder.imageView, list.get(position));
		WeiboApplication.IMAGE_CACHE.get(list.get(position),holder.imageView);
		return view;
	}

	class Holder{
		ImageView imageView;
	}

}
