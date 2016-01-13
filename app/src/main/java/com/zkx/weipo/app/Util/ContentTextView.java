package com.zkx.weipo.app.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/1/13.
 */
public class ContentTextView extends TextView {

    public boolean linkHit;//内部链接是否被点击

    public ContentTextView(Context context) {
        super(context);
    }

    public ContentTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ContentTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean performClick() {
        if (linkHit){
            return true;
        }
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        linkHit=false;
        return super.onTouchEvent(event);
    }
}
