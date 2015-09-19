package com.zkx.weipo.app;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2015/9/19.
 */
public class Tools  {


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

    public static void dataTransfer(InputStream is, OutputStream os) {
        byte[] buffer = new byte[8192];
        int count = 0;
        try {
            while((count = is.read(buffer)) > -1) {
                os.write(buffer, 0, count);
            }
        } catch (Exception e) {

        }
    }

    public static void userVerified(ImageView imageView, int verifiedType) {
        if (verifiedType >= 0) {
            imageView.setVisibility(View.VISIBLE);
            switch (verifiedType) {
                case 0:
                case 220:
                    imageView.setImageLevel(verifiedType);
                    break;
                default:
                    imageView.setImageLevel(1);
                    break;
            }
        }
    }

   /* public static SpannableString changeTextToFace(Context context,
                                                   Spanned spanned) {
        String text = spanned.toString();
        SpannableString spannableString = new SpannableString(spanned);

        Pattern pattern = Pattern.compile("\\[[^\\]]+\\]");

        Matcher matcher = pattern.matcher(text);

        boolean b = true;

        while (b = matcher.find()) {
            String faceText = text.substring(matcher.start(), matcher.end());
            int resourceId = FaceMan.getResourceId(faceText);
            if (resourceId > 0) {
                Bitmap bitmap = BitmapFactory.decodeResource(
                        context.getResources(), resourceId);
                ImageSpan imageSpan = new ImageSpan(bitmap);

                spannableString.setSpan(imageSpan, matcher.start(),
                        matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return spannableString;
    }*/

    public static String atBlue(String s) {

        StringBuilder sb = new StringBuilder();
        int commonTextColor = Color.BLACK;
        int signColor = Color.BLUE;

        int state = 1;
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            switch (state) {
                case 1: // 普通字符状态
                    // 遇到@
                    if (s.charAt(i) == '@') {
                        state = 2;
                        str += s.charAt(i);
                    }
                    // 遇到#
                    else if (s.charAt(i) == '#') {
                        str += s.charAt(i);
                        state = 3;
                    }
                    // 添加普通字符
                    else {
                        if (commonTextColor == Color.BLACK)
                            sb.append(s.charAt(i));
                        else
                            sb.append("<font color='" + commonTextColor + "'>"
                                    + s.charAt(i) + "</font>");
                    }
                    break;
                case 2: // 处理遇到@的情况
                    // 处理@后面的普通字符
                    if (Character.isJavaIdentifierPart(s.charAt(i))) {
                        str += s.charAt(i);
                    }

                    else {
                        // 如果只有一个@，作为普通字符处理
                        if ("@".equals(str)) {
                            sb.append(str);
                        }
                        // 将@及后面的普通字符变成蓝色
                        else {
                            sb.append(setTextColor(str, String.valueOf(signColor)));
                        }
                        // @后面有#的情况，首先应将#添加到str里，这个值可能会变成蓝色，也可以作为普通字符，要看后面还有没有#了
                        if (s.charAt(i) == '#') {

                            str = String.valueOf(s.charAt(i));
                            state = 3;
                        }
                        // @后面还有个@的情况，和#类似
                        else if (s.charAt(i) == '@') {
                            str = String.valueOf(s.charAt(i));
                            state = 2;
                        }
                        // @后面有除了@、#的其他特殊字符。需要将这个字符作为普通字符处理
                        else {
                            if (commonTextColor == Color.BLACK)
                                sb.append(s.charAt(i));
                            else
                                sb.append("<font color='" + commonTextColor + "'>"
                                        + s.charAt(i) + "</font>");
                            state = 1;
                            str = "";
                        }
                    }
                    break;
                case 3: // 处理遇到#的情况
                    // 前面已经遇到一个#了，这里处理结束的#
                    if (s.charAt(i) == '#') {
                        str += s.charAt(i);
                        sb.append(setTextColor(str, String.valueOf(signColor)));
                        str = "";
                        state = 1;

                    }
                    // 如果#后面有@，那么看一下后面是否还有#，如果没有#，前面的#作废，按遇到@处理
                    else if (s.charAt(i) == '@') {
                        if (s.substring(i).indexOf("#") < 0) {
                            sb.append(str);
                            str = String.valueOf(s.charAt(i));
                            state = 2;

                        } else {
                            str += s.charAt(i);
                        }
                    }
                    // 处理#...#之间的普通字符
                    else {
                        str += s.charAt(i);
                    }
                    break;
            }

        }
        if (state == 1 || state == 3) {
            sb.append(str);
        } else if (state == 2) {
            if ("@".equals(str)) {
                sb.append(str);
            } else {
                sb.append(setTextColor(str, String.valueOf(signColor)));
            }

        }

        return sb.toString();

    }

    public static String setTextColor(String s, String color) {
        String result = "<font color='" + color + "'>" + s + "</font>";

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
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return sdf.format(oldTime);
        }
    }
}
