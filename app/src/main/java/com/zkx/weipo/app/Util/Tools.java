package com.zkx.weipo.app.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.zkx.weipo.app.ImagePagerActivity;
import com.zkx.weipo.app.R;
import com.zkx.weipo.app.WebView;
import com.zkx.weipo.app.adapter.GridViewAdapter;
import com.zkx.weipo.app.openapi.models.User;
import com.zkx.weipo.app.view.MyGridView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015/9/19.
 */
public class Tools  {
    /**
     * 判断字符串是否为空
     * @param s
     * @return
     */
    public static boolean isEmpty(String s){
        return null==s||"".equals(s);
    }
    /**
     * 识别文字中的@用户、话题、网址等内容
     * @param context
     * @param source
     * @param textView
     * @return
     */
    public static SpannableString getContent(final Context context, String source, TextView textView){
        SpannableString spannableString=new SpannableString(source);

        String REGEX="(@[\\u4e00-\\u9fa5a-zA-Z0-9_-]+)|(#[^#]+#)|(\\[[\\u4e00-\\u9fa5\\w]+\\])|(http://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|])";
        //设置正则
        Pattern pattern=Pattern.compile(REGEX);
        Matcher matcher=pattern.matcher(spannableString);

        if (matcher.find()){
            textView.setMovementMethod(LinkMovementMethod.getInstance());
            matcher.reset();
        }

        while (matcher.find()){
            final String at=matcher.group(1);
            final String topic=matcher.group(2);
            String emoji=matcher.group(3);
            final String url=matcher.group(4);

            if (at!=null){
                int start=matcher.start(1);
                int end=start+at.length();
                MyClickableSpan span=new MyClickableSpan(){
                    @Override
                    public void onClick(View widget) {
                        Toast.makeText(context,"点击用户",Toast.LENGTH_SHORT).show();
                    }
                };
                spannableString.setSpan(span,start,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            if (topic != null) {
                int start = matcher.start(2);
                int end = start + topic.length();
                MyClickableSpan clickableSpan = new MyClickableSpan() {

                    @Override
                    public void onClick(View widget) {
                        Toast.makeText(context, "点击了话题：" + topic, Toast.LENGTH_LONG).show();
                    }
                };
                spannableString.setSpan(clickableSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            if (emoji != null) {
                int start = matcher.start(3);
                int end = start + emoji.length();
                int ResId = EmotionUtils.getImgByName(emoji);
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), ResId);
                if (bitmap != null) {
                    // 获取字符的大小
                    //int size = (int) textView.getTextSize();
                    // 压缩Bitmap
                    bitmap = Bitmap.createScaledBitmap(bitmap, 48, 48 , true);
                    // 设置表情
                    ImageSpan imageSpan = new ImageSpan(context, bitmap);
                    spannableString.setSpan(imageSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }

            // 处理url地址
            if (url != null) {
                int start = matcher.start(4);
                int end = start + url.length();
                MyClickableSpan clickableSpan = new MyClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        Intent intent=new Intent(context,WebView.class);
                        intent.putExtra("URL",url);
                        context.startActivity(intent);
                        //Toast.makeText(context, "点击了网址：" + url, Toast.LENGTH_LONG).show();
                    }
                };
                spannableString.setSpan(clickableSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return spannableString;
    }

    /**
     * 将缩略图地址转为高清地址
     * @param arrayList
     * @return
     */
    public static ArrayList<String> getOriginalPicUrls(ArrayList<String> arrayList){

        ArrayList<String> arrayList2 = new ArrayList<String>();

        for (int i=0;i<arrayList.size();i++){
            String[] heightArray = (String[]) arrayList.toArray(new String[0]);
            String s2=heightArray[i].substring(32);
            StringBuffer stringBuffer=new StringBuffer("http://ww4.sinaimg.cn/large/");
            stringBuffer.append(s2);
            arrayList2.add(String.valueOf(stringBuffer));
        }
        return arrayList2;
    }

    /**
     * 将微博的日期字符串转换为Date对象
     * @param str
     * @return
     */
    public static Date strToDate(String str) {
        // sample：Tue May 31 17:46:55 +0800 2011
        // E：周 MMM：字符串形式的月，如果只有两个M，表示数值形式的月 Z表示时区（＋0800）
        SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy",
                Locale.US);
        Date result = null;
        try {
            result = sdf.parse(str);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return result;

    }

    /**
     * 获取微博发布日期
     * @param oldTime
     * @param currentDate
     * @return
     */
    public static String getTimeStr(Date oldTime, Date currentDate) {
        long time1 = currentDate.getTime();

        long time2 = oldTime.getTime();

        long year1=currentDate.getYear();

        long year2=oldTime.getYear();

        long time = (time1 - time2) / 1000;

        if (time >= 0 && time < 60) {
            return "刚才";
        } else if (time >= 60 && time < 3600) {
            return time / 60 + "分钟前";
        } else if (time >= 3600 && time < 3600 * 24) {
            return time / 3600 + "小时前";
        } else if (time >= 3600 * 24 && time < 3600 * 24 *365 && year1==year2){
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
            return sdf.format(oldTime);
        }else{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(oldTime);
        }
    }

    /**
     * 检查用户是否被认证
     * @param user
     * @param view
     */
    public static void checkVerified(User user, ImageView view){
        if (user.verified){
            switch (user.verified_type){
                case 0:
                    view.setImageResource(R.mipmap.avatar_vip);
                    view.setVisibility(View.VISIBLE);
                    break;
                case -1:
                    view.setVisibility(View.GONE);
                    break;
                default:
                    view.setImageResource(R.mipmap.avatar_enterprise_vip);
                    view.setVisibility(View.VISIBLE);
            }
        }else if (user.verified_type==200 || user.verified_type==220){
            view.setImageResource(R.mipmap.avatar_grassroot);
            view.setVisibility(View.VISIBLE);
        }else {
            view.setVisibility(View.GONE);
        }
    }

    /**
     * 九宫格图片查看
     * @param context 上下文
     * @param wh 屏幕宽度
     * @param gv_images Gridview
     * @param list 图片网络地址
     */
    public static void initInfoImages(final Activity context, int wh, MyGridView gv_images, final ArrayList<String> list){
        if(list!=null&&!list.equals("")){
            int w=0;
            switch (list.size()) {
                case 1:
                    w=wh;
                    gv_images.setNumColumns(1);
                    break;
                case 2:
                case 4:
                    w=2*wh+ Dp2Px(context, 2);
                    gv_images.setNumColumns(2);
                    break;
                case 3:
                case 5:
                case 6:
                    w=wh*3+Dp2Px(context, 2)*2;
                    gv_images.setNumColumns(3);
                    break;
                case 7:
                case 8:
                case 9:
                    w=wh*3+Dp2Px(context, 2)*2;
                    gv_images.setNumColumns(3);
                    break;
            }
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(w, RelativeLayout.LayoutParams.WRAP_CONTENT);
            gv_images.setLayoutParams(lp);
            GridViewAdapter nearByInfoImgsAdapter = new GridViewAdapter(context, list);
            gv_images.setAdapter(nearByInfoImgsAdapter);
            nearByInfoImgsAdapter.setOnItemClickLitener(new GridViewAdapter.OnItemClickLitener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent intent = new Intent(context, ImagePagerActivity.class);
                    // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
                    intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, Tools.getOriginalPicUrls(list));
                    intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
                    context.startActivity(intent);
                }
            });
        }
    }

    /**
     * 计算每行的单个图片宽度
     * @param context
     * @return
     */
    public static int getWidth(Activity context){
        return (getScreenWidth(context)- Dp2Px(context, 99))/3;
    }

    /**
     * DP转为PX
     * @param context
     * @param dp
     * @return
     */
    public static int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * 获取屏幕宽度
     * @param activity
     * @return
     */
    @SuppressWarnings("deprecation")
    public static int getScreenWidth(Activity activity){
        int width;
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        width=display.getWidth();
        return width;
    }
}
