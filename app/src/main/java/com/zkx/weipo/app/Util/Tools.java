package com.zkx.weipo.app.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
                    bitmap = Bitmap.createScaledBitmap(bitmap, 32, 32, true);
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
                        Toast.makeText(context, "点击了网址：" + url, Toast.LENGTH_LONG).show();
                    }
                };
                spannableString.setSpan(clickableSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return spannableString;
    }

    //将缩略图地址转为高清地址
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

    // 将微博的日期字符串转换为Date对象
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

    public static String getTimeStr(Date oldTime, Date currentDate) {
        long time1 = currentDate.getTime();

        long time2 = oldTime.getTime();

        long time = (time1 - time2) / 1000;

        if (time >= 0 && time < 60) {
            return "刚才";
        } else if (time >= 60 && time < 3600) {
            return time / 60 + "分钟前";
        } else if (time >= 3600 && time < 3600 * 24) {
            return time / 3600 + "小时前";
        } else if (time >= 3600 * 24 && time < 3600 * 24 *365){
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
            return sdf.format(oldTime);
        }else {
            SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
            return sdf.format(oldTime);
        }
    }
}
