package com.zkx.weipo.app.openapi.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/3/12.
 */
public class Reposts {

    /** 微博创建时间 */
    public String created_at;
    /** 微博MID */
    public long mid;
    /** 字符串型的微博ID */
    public String idstr;
    /** 微博信息内容 */
    public String text;
    /** 微博来源 */
    public String source;
    /** 是否已收藏，true：是，false：否  */
    public boolean favorited;
    /** 是否被截断，true：是，false：否 */
    public boolean truncated;
    /**（暂未支持）回复ID */
    public String in_reply_to_status_id;
    /**（暂未支持）回复人UID */
    public String in_reply_to_user_id;
    /**（暂未支持）回复人昵称 */
    public String in_reply_to_screen_name;
    /** 缩略图片地址（小图），没有时不返回此字段 */
    public String thumbnail_pic;
    /** 中等尺寸图片地址（中图），没有时不返回此字段 */
    public String bmiddle_pic;
    /** 原始图片地址（原图），没有时不返回此字段 */
    public String original_pic;
    /** 地理信息字段 */
    public Geo geo;
    /** 微博作者的用户信息字段 */
    public User user;
    /** 转发数 */
    public int reposts_count;
    /** 评论数 */
    public int comments_count;
    /** 表态数 */
    public int attitudes_count;

    public Status retweeted_status;

    public static Reposts parse(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            return Reposts.parse(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Reposts parse(JSONObject jsonObject) {
        if (null == jsonObject) {
            return null;
        }

        Reposts reposts = new Reposts();
        reposts.created_at       = jsonObject.optString("created_at");
        reposts.mid              = jsonObject.optLong("mid");
        reposts.idstr            = jsonObject.optString("idstr");
        reposts.text             = jsonObject.optString("text");
        reposts.source           = jsonObject.optString("source");
        reposts.favorited        = jsonObject.optBoolean("favorited", false);
        reposts.truncated        = jsonObject.optBoolean("truncated", false);
        // Have NOT supported
        reposts.in_reply_to_status_id   = jsonObject.optString("in_reply_to_status_id");
        reposts.in_reply_to_user_id     = jsonObject.optString("in_reply_to_user_id");
        reposts.in_reply_to_screen_name = jsonObject.optString("in_reply_to_screen_name");
        reposts.thumbnail_pic    = jsonObject.optString("thumbnail_pic");
        reposts.bmiddle_pic      = jsonObject.optString("bmiddle_pic");
        reposts.original_pic     = jsonObject.optString("original_pic");
        reposts.geo              = Geo.parse(jsonObject.optJSONObject("geo"));
        reposts.user             = User.parse(jsonObject.optJSONObject("user"));
        reposts.retweeted_status = Status.parse(jsonObject.optJSONObject("retweeted_status"));
        reposts.reposts_count    = jsonObject.optInt("reposts_count");
        reposts.comments_count   = jsonObject.optInt("comments_count");
        reposts.attitudes_count  = jsonObject.optInt("attitudes_count");

        //status.ad = jsonObject.optString("ad", "");

        return reposts;
    }

}
