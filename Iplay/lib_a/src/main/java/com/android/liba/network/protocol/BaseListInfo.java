package com.android.liba.network.protocol;

import java.util.ArrayList;
import java.util.List;

public class BaseListInfo<T> implements BaseType {

    /**
     * lastid : 2
     * items : [{"id":1,"url":"http://adadmin.88765.com/uploads/preview/060051755115471735518973337.jpg","title":"开机图"},{"id":2,"url":"http://adadmin.88765.com/uploads/preview/060051757415471735749052666.jpg","title":"Banner"}]
     */
    private int page;
    private int total;
    private int sign_days;
    private int lastid;
    private int is_sign_today; //1-已签到 0-未签到
    private String title;
    private int is_score;
    private int count;
    private List<T> items;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getLastid() {
        return lastid;
    }

    public void setLastid(int lastid) {
        this.lastid = lastid;
    }

    public List<T> getItems() {
        if (items == null) items = new ArrayList<>();
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getIs_score() {
        return is_score;
    }

    public void setIs_score(int is_score) {
        this.is_score = is_score;
    }

    public int getSign_days() {
        return sign_days;
    }

    public void setSign_days(int sign_days) {
        this.sign_days = sign_days;
    }

    public int getIs_sign_today() {
        return is_sign_today;
    }

    public void setIs_sign_today(int is_sign_today) {
        this.is_sign_today = is_sign_today;
    }

    public Class<?> getType() {
        if (items != null)
            return items.get(0).getClass();
        throw new IllegalStateException("can not get list info");
    }

    public Class<?> getThisType() {
        return getClass();
    }
}
