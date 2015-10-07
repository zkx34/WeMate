package com.zkx.weipo.app.imgCache;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2015/10/5.
 */
public interface ImageLoaderCallback {

    void refresh(String url,Bitmap bitmap);
}
