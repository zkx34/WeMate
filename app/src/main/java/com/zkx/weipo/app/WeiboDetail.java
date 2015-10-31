package com.zkx.weipo.app;

import android.app.Activity;
import android.os.Bundle;
import com.zkx.weipo.app.app.WeiboApplication;

/**
 * Created by Administrator on 2015/10/14.
 */
public class WeiboDetail extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WeiboApplication.getInstance();
        WeiboApplication.addActivity(this);
    }
}
