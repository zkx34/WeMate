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


    // ��΢���������ַ���ת��ΪDate����
    public static Date strToDate(String str) {
        // sample��Tue May 31 17:46:55 +0800 2011
        // E���� MMM���ַ�����ʽ���£����ֻ������M����ʾ��ֵ��ʽ���� Z��ʾʱ������0800��
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
                case 1: // ��ͨ�ַ�״̬
                    // ����@
                    if (s.charAt(i) == '@') {
                        state = 2;
                        str += s.charAt(i);
                    }
                    // ����#
                    else if (s.charAt(i) == '#') {
                        str += s.charAt(i);
                        state = 3;
                    }
                    // �����ͨ�ַ�
                    else {
                        if (commonTextColor == Color.BLACK)
                            sb.append(s.charAt(i));
                        else
                            sb.append("<font color='" + commonTextColor + "'>"
                                    + s.charAt(i) + "</font>");
                    }
                    break;
                case 2: // ��������@�����
                    // ����@�������ͨ�ַ�
                    if (Character.isJavaIdentifierPart(s.charAt(i))) {
                        str += s.charAt(i);
                    }

                    else {
                        // ���ֻ��һ��@����Ϊ��ͨ�ַ�����
                        if ("@".equals(str)) {
                            sb.append(str);
                        }
                        // ��@���������ͨ�ַ������ɫ
                        else {
                            sb.append(setTextColor(str, String.valueOf(signColor)));
                        }
                        // @������#�����������Ӧ��#��ӵ�str����ֵ���ܻ�����ɫ��Ҳ������Ϊ��ͨ�ַ���Ҫ�����滹��û��#��
                        if (s.charAt(i) == '#') {

                            str = String.valueOf(s.charAt(i));
                            state = 3;
                        }
                        // @���滹�и�@���������#����
                        else if (s.charAt(i) == '@') {
                            str = String.valueOf(s.charAt(i));
                            state = 2;
                        }
                        // @�����г���@��#�����������ַ�����Ҫ������ַ���Ϊ��ͨ�ַ�����
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
                case 3: // ��������#�����
                    // ǰ���Ѿ�����һ��#�ˣ����ﴦ�������#
                    if (s.charAt(i) == '#') {
                        str += s.charAt(i);
                        sb.append(setTextColor(str, String.valueOf(signColor)));
                        str = "";
                        state = 1;

                    }
                    // ���#������@����ô��һ�º����Ƿ���#�����û��#��ǰ���#���ϣ�������@����
                    else if (s.charAt(i) == '@') {
                        if (s.substring(i).indexOf("#") < 0) {
                            sb.append(str);
                            str = String.valueOf(s.charAt(i));
                            state = 2;

                        } else {
                            str += s.charAt(i);
                        }
                    }
                    // ����#...#֮�����ͨ�ַ�
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
            return "�ղ�";
        } else if (time >= 60 && time < 3600) {
            return time / 60 + "����ǰ";
        } else if (time >= 3600 && time < 3600 * 24) {
            return time / 3600 + "Сʱǰ";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return sdf.format(oldTime);
        }
    }
}
