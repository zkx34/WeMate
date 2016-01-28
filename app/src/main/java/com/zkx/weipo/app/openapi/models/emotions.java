package com.zkx.weipo.app.openapi.models;

/**
 * Created by Administrator on 2016/1/28.
 */
public class emotions {

    private String phrase;
    private String type;
    private String url;
    private Boolean hot;
    private Boolean common;
    private String category;
    private String icon;
    private String value;
    private String picid;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getCommon() {
        return common;
    }

    public void setCommon(Boolean common) {
        this.common = common;
    }

    public Boolean getHot() {
        return hot;
    }

    public void setHot(Boolean hot) {
        this.hot = hot;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    public String getPicid() {
        return picid;
    }

    public void setPicid(String picid) {
        this.picid = picid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "emotions{" +
                "category='" + category + '\'' +
                ", phrase='" + phrase + '\'' +
                ", type='" + type + '\'' +
                ", url='" + url + '\'' +
                ", hot=" + hot +
                ", common=" + common +
                ", icon='" + icon + '\'' +
                ", value='" + value + '\'' +
                ", picid='" + picid + '\'' +
                '}';
    }
}
