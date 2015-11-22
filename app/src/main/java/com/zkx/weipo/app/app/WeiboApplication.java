package com.zkx.weipo.app.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.zkx.weipo.app.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/10/5.
 */
public class WeiboApplication extends Application {

    public static Context context;
    private static ArrayList<Activity> appActivities = new ArrayList<Activity>();
    public static final DisplayImageOptions options=new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.avatar_default_small)
            .cacheInMemory(true)
            .cacheOnDisk(false)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .build();

    @Override
    public void onCreate() {
        super.onCreate();
        context=this.getApplicationContext();
        ImageLoaderConfiguration configuration=ImageLoaderConfiguration.createDefault(this);
        ImageLoader.getInstance().init(configuration);
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
        //Finish 所有的Activity
        for (Activity activity : appActivities)
        {
            if(!activity.isFinishing())
                activity.finish();
        }

    }


}
