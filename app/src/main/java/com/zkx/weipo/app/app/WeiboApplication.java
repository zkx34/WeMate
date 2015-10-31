package com.zkx.weipo.app.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import cn.trinea.android.common.service.impl.ImageCache;
import cn.trinea.android.common.util.CacheManager;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/10/5.
 */
public class WeiboApplication extends Application {

    public static Context context;
    public static final ImageCache IMAGE_CACHE= CacheManager.getImageCache();
    private static ArrayList<Activity> appActivities = new ArrayList<Activity>();

    @Override
    public void onCreate() {
        super.onCreate();
        context=this.getApplicationContext();
        IMAGE_CACHE.initData(context,"weibo");
    }

    private static WeiboApplication instance;

    /**
     * 单例模式中获取唯一的MyApplication实例
     *
     * @return
     */
    public static WeiboApplication getInstance() {
        if (null == instance) {
            instance = new WeiboApplication();
        }
        return instance;

    }

    public static void addActivity(Activity activity)
    {

        if(!appActivities.isEmpty())
        {

            for (Activity at : appActivities)
            {
                if(at.getClass().getName().equals(activity.getClass().getName()))
                {
                    appActivities.remove(at);
                    break;
                }
            }
        }
        appActivities.add(activity);
    }

    public static void appExit(Context context)
    {
        IMAGE_CACHE.saveDataToDb(context,"weibo");

        //Finish 所有的Activity
        for (Activity activity : appActivities)
        {
            if(!activity.isFinishing())
                activity.finish();
        }

    }


}
