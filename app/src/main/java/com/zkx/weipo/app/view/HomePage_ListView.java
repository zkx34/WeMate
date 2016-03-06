package com.zkx.weipo.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zkx.weipo.app.R;

/**
 * Created by Administrator on 2015/11/21.
 */
public class HomePage_ListView extends ListView implements AbsListView.OnScrollListener{

    private View footerView=null;
    private Context context;
    private OnBottomListener listener;
    private ImageLoader imageLoader;
    private final boolean pauseOnScroll=true;
    private final boolean pauseOnFling=true;


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    public HomePage_ListView(Context context) {
        super(context);
        this.context=context;
        initListView();
    }

    public HomePage_ListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        initListView();
    }

    public HomePage_ListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        initListView();
    }

    public void initListView(){
        imageLoader=ImageLoader.getInstance();
        setOnScrollListener(this);
        setFooterDividersEnabled(false);
    }

    public void initFooterView(){
        if (footerView==null){
            footerView= LayoutInflater.from(this.context).inflate(R.layout.footerview_loadmore,null);
        }
        addFooterView(footerView);
    }

   /* public void showFooterView(){
        footerView.setVisibility(VISIBLE);
    }

    public void hideFooterView(){
        footerView.setVisibility(GONE);
    }
*/
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState){
            case 0:
                this.imageLoader.resume();
                break;
            case 1:
                if(pauseOnScroll) {
                    this.imageLoader.pause();
                }
                break;
            case 2:
                if(pauseOnFling) {
                    this.imageLoader.pause();
                }
        }
        // 当不滚动时
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
            // 判断是否滚动到底部
            if (view.getLastVisiblePosition() == view.getCount() - 1) {
                //加载更多功能的代码
               // showFooterView();
                listener.onBottom();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    }

    public void setOnBottomListener(OnBottomListener listener){
        this.listener=listener;
    }

    public interface OnBottomListener {
        void onBottom();
    }
}
