package com.zkx.weipo.app.util;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Created by Administrator on 2015/12/21.
 */
public class MyClickableSpan extends ClickableSpan {
    @Override
    public void onClick(View widget) {

    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setColor(Color.BLUE);
        ds.setUnderlineText(false);
    }
}
