package com.zkx.weipo.app.imgCache;

import android.graphics.Bitmap;
import android.widget.ImageView;
import com.zkx.weipo.app.app.WeiboApplication;

/**
 * Created by Administrator on 2015/10/5.
 */
public class SimpleImageLoader {

    public static void showImg(ImageView view,String url){

        view.setTag(url);
        view.setImageBitmap(
                WeiboApplication.lazyImageLoader.get(url,getCallback(url,view)));
    }

    private static ImageLoaderCallback getCallback(final String url,final ImageView view){
        return new ImageLoaderCallback() {
            @Override
            public void refresh(String url, Bitmap bitmap) {
                if (url.equals(view.getTag().toString())){
                    view.setImageBitmap(bitmap);
                }
            }
        };
    }
}
