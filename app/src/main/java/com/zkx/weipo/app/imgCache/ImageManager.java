package com.zkx.weipo.app.imgCache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import com.zkx.weipo.app.R;
import com.zkx.weipo.app.Util.MD5Util;
import org.apache.http.HttpException;
import java.io.*;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/10/5.
 */
public class ImageManager {
    Map<String,SoftReference<Bitmap>> imgCache;
    public static Bitmap userDefualtHead;
    private Context context;
    public ImageManager(Context context) {
        this.context=context;
        imgCache=new HashMap<String,SoftReference<Bitmap>>();
       userDefualtHead =drawabltToBitmap(context.getResources().getDrawable(R.drawable.timeline_card_small_profile));
    }

    public boolean contains(String url){
        return imgCache.containsKey(url);
    }

    public Bitmap getFromCache(String url) {

        Bitmap bitmap;

        bitmap=this.getFromMapCache(url);

        if (null==bitmap){
            bitmap=getFromFile(url);
        }

        return bitmap;
    }

    /**
     * 从Map缓存中获取Bitmap
     * @param url
     * @return
     */
    public Bitmap getFromMapCache(String url) {

        Bitmap bitmap=null;

        SoftReference<Bitmap> ref=null;

        synchronized (this){
            ref=imgCache.get(url);
        }

        if (null!=ref){
            bitmap=ref.get();
        }
        return bitmap;
    }

    /**
     * 从文件中获取bitmap
     * @param url
     * @return
     */
    private Bitmap getFromFile(String url) {

        String fileName=this.getMD5(url);
        FileInputStream is=null;

        try {
            is=context.openFileInput(fileName);
            return BitmapFactory.decodeStream(is);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }finally {
            if (null!=is){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String writeToFile(String fileName,InputStream is){

        BufferedInputStream bis=null;
        BufferedOutputStream bos=null;

        try{
            bis=new BufferedInputStream(is);
            bos=new BufferedOutputStream(context.openFileOutput(fileName,Context.MODE_PRIVATE));
            byte[] buffer=new byte[1024];
            int length;
            while ((length=bis.read(buffer))!= -1){
                bos.write(buffer,0,length);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (null!=bis)
                    bis.close();
                if (null!=bos){
                    bos.flush();
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return context.getFilesDir()+"/"+fileName;
    }

    public Bitmap safeGet(String url) throws HttpException{
        Bitmap bitmap=this.getFromFile(url);
        if (null!=bitmap){
            synchronized (this){
                imgCache.put(url,new SoftReference<Bitmap>(bitmap));
            }
            return bitmap;
        }
        return downloadImg(url);
    }


    /**
     * 下载图片并保持到系统缓存
     * @param urlS
     * @return
     */
    public Bitmap downloadImg(String urlS) throws HttpException{
        try{
            URL url=new URL(urlS);
            HttpURLConnection connection=(HttpURLConnection)url.openConnection();
            String fileName=writeToFile(getMD5(urlS),connection.getInputStream());
            return BitmapFactory.decodeFile(fileName);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private String getMD5(String src){
        return MD5Util.getMD5String(src);
    }

    private Bitmap drawabltToBitmap(Drawable drawable)
    {

        BitmapDrawable tempDrawable = (BitmapDrawable)drawable;
        return tempDrawable.getBitmap();

    }
}
