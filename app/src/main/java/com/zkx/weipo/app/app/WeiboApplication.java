package com.zkx.weipo.app.app;

import android.app.Application;
import android.content.Context;
import com.zkx.weipo.app.imgCache.LazyImageLoader;

/**
 * Created by Administrator on 2015/10/5.
 */
public class WeiboApplication extends Application {

    public static LazyImageLoader lazyImageLoader;
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context=this.getApplicationContext();
        lazyImageLoader=new LazyImageLoader();
    }
}
