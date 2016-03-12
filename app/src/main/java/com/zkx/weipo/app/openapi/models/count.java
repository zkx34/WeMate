package com.zkx.weipo.app.openapi.models;

/**
 * Created by Administrator on 2016/3/12.
 */
public class count{
    private long id;
    private int comments;
    private int reposts;
    private int attitudes;

    public int getAttitudes() {
        return attitudes;
    }

    public void setAttitudes(int attitudes) {
        this.attitudes = attitudes;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getReposts() {
        return reposts;
    }

    public void setReposts(int reposts) {
        this.reposts = reposts;
    }

    @Override
    public String toString() {
        return "count{" +
                "id=" + id +
                ", comments=" + comments +
                ", reposts=" + reposts +
                ", attitudes=" + attitudes +
                '}';
    }
}
