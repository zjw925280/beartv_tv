package com.gys.play.entity;

import java.io.Serializable;

public class WowDetailBean implements Serializable {

    private String id;
    private String cover;
    private String url;
    private String title;
    private String user_name;
    private String avatar;
    private String like_num;
    private String comment_num;
    private String play_num;
    private boolean isLoadLike;  //是否在家过like详情

    private boolean isLoadDetail;

    public boolean isLoadDetail() {
        return isLoadDetail;
    }

    public void setLoadDetail(boolean loadDetail) {
        isLoadDetail = loadDetail;
    }

    private boolean isLike;

    private boolean isSub;  //是否订阅

    public boolean isSub() {
        return isSub;
    }

    public void setSub(boolean sub) {
        isSub = sub;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public String getUrl() {
        return url;
    }

    public String getId() {
        return id;
    }

    public String getCover() {
        return cover;
    }

    public String getTitle() {
        return title;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getLike_num() {
        return like_num;
    }

    public void setLike_num(String like_num) {
        this.like_num = like_num;
    }

    public String getComment_num() {
        return comment_num;
    }

}
