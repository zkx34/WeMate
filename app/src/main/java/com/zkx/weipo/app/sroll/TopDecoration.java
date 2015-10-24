package com.zkx.weipo.app.sroll;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2015/10/24.
 */
public class TopDecoration extends RecyclerView.ItemDecoration {

    private View mTopView = null;

    public TopDecoration (View topView) {
        mTopView = topView;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.set(0, mTopView.getHeight(), 0, 0);
        }
    }
}
