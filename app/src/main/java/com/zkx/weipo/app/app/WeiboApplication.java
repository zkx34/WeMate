package com.zkx.weipo.app.app;

import android.app.Application;
import android.content.Context;
import cn.trinea.android.common.service.impl.ImageSDCardCache;
import cn.trinea.android.common.util.CacheManager;
import com.zkx.weipo.app.imgCache.LazyImageLoader;

/**
 * Created by Administrator on 2015/10/5.
 */
public class WeiboApplication extends Application {

    public static LazyImageLoader lazyImageLoader;
    public static Context context;
    public static final ImageSDCardCache IMAGE_CACHE= CacheManager.getImageSDCardCache();

    @Override
    public void onCreate() {
        super.onCreate();
        context=this.getApplicationContext();
        lazyImageLoader=new LazyImageLoader();
        //IMAGE_CACHE.initData(context,"weibo");
    }



}
