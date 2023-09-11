package com.gys.play.entity;

import java.util.List;

public class VideoDetailBean {

    private String category_name;
    private String cover;
    private String title;
    private String play_num;
    private String finish_txt;
    private String tag;
    private String score;
    private String intro; //简介

    private int pay_type;

    private List<VideoDetailChapterBean> chapter_list;


    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlay_num() {
        return play_num;
    }

    public void setPlay_num(String play_num) {
        this.play_num = play_num;
    }

    public String getFinish_txt() {
        return finish_txt;
    }

    public void setFinish_txt(String finish_txt) {
        this.finish_txt = finish_txt;
    }

    public String getTag() {
        return tag.replaceAll(",", " ");
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public int getPay_type() {
        return pay_type;
    }

    public void setPay_type(int pay_type) {
        this.pay_type = pay_type;
    }

    public List<VideoDetailChapterBean> getChapter_list() {
        return chapter_list;
    }

    public void setChapter_list(List<VideoDetailChapterBean> chapter_list) {
        this.chapter_list = chapter_list;
    }

    public static class VideoDetailChapterBean {

        private String title;
        private String coins;
        private int is_buy;
        private String url;
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCoins() {
            return coins;
        }

        public void setCoins(String coins) {
            this.coins = coins;
        }

        public int getIs_buy() {
            return is_buy;
        }

        public void setIs_buy(int is_buy) {
            this.is_buy = is_buy;
        }
    }
}
