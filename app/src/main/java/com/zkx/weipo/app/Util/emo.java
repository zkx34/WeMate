package com.zkx.weipo.app.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zkx.weipo.app.openapi.models.emotions;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by Administrator on 2016/1/28.
 */
public class emo {
    public List<emotions> getJson(String json){
        Gson gson=new Gson();
        return gson.fromJson(json,new TypeToken<List<emotions>>(){}.getType());
    }

    public void makeImage(emotions bean, String filePath) {
        // 网络请求所需变量
        try {
            //获取输入流
            BufferedInputStream in = new BufferedInputStream(new URL(bean.getUrl()).openStream());
            //创建文件流
            File file = new File(filePath + bean.getPhrase()+".gif");
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
            //缓冲字节数组
            byte[] data = new byte[2048];
            int length = in.read(data);
            while (length != -1) {
                out.write(data, 0, data.length);
                length = in.read(data);
            }
            System.out.println("正在执行下载任务：当前正在下载图片" + bean.getPhrase() + ".gif");
            in.close();
            out.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
