package com.zkx.weipo.app.imgCache;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.zkx.weipo.app.app.WeiboApplication;

import java.lang.Thread.State;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
/**
 * Created by Administrator on 2015/10/5.
 */
public class LazyImageLoader {

    private static final int MESSAGE_ID=1;

    private static final String IMG_URL="img_url";

    private static final String IMG="img";

    private CallbackManager callbackManager=new CallbackManager();

    private ImageManager imageManager=new ImageManager(WeiboApplication.context);

    private BlockingQueue<String> urlQueue=new ArrayBlockingQueue<String>(50);

    private DownloadImgThread downloadImgThread=new DownloadImgThread();

    public Bitmap get(String url,ImageLoaderCallback callback){

        Bitmap bitmap=null;

        if (imageManager.contains(url)){

            bitmap=imageManager.getFromCache(url);
            return bitmap;
        }
        else {
            callbackManager.put(url,callback);
            startDownloadThread(url);
        }
        return bitmap;
    }

    private void startDownloadThread(String url){
        putUrlToQueue(url);
        State state=downloadImgThread.getState();
        if ( state== State.NEW){
            downloadImgThread.start();
        }else if ( state== State.TERMINATED){
            downloadImgThread=new DownloadImgThread();
            downloadImgThread.start();
        }
    }

    private void putUrlToQueue(String url){
        if (!urlQueue.contains(url)){
            try {
                urlQueue.put(url);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class DownloadImgThread extends Thread{

        private boolean isRun=true;

        public void shutDown(){
            isRun=false;
        }

        @Override
        public void run() {
            try{
                while (isRun){
                    String url=urlQueue.poll();
                    if (url==null)
                        break;
                    Bitmap bitmap=imageManager.safeGet(url);
                    Message msg=handler.obtainMessage(MESSAGE_ID);
                    Bundle bundle=msg.getData();
                    bundle.putSerializable(IMG_URL,url);
                    bundle.putParcelable(IMG,bitmap);
                    handler.sendMessage(msg);
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                shutDown();
            }
        }
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MESSAGE_ID:
                {
                    Bundle bundle=msg.getData();
                    String url=bundle.getString(IMG_URL);
                    Bitmap bitmap=bundle.getParcelable(IMG);
                    break;
                }
            }
        }
    };
}
