package com.android.liba.ui.base;

import android.os.Bundle;

public class CommonSetData {
    private int icon;
    private String content;
    private String subContent;

    public String getRoute() {
        return route;
    }

    public Bundle getBundle() {
        return bundle;
    }

    private String route;
    private Bundle bundle = null;

    public CommonSetData(int icon, String content, String subContent, String route) {
        this.icon = icon;
        this.content = content;
        this.subContent = subContent;
        this.route = route;
    }

    public CommonSetData(int icon, String content, String subContent) {
        this.icon = icon;
        this.content = content;
        this.subContent = subContent;
    }

    public CommonSetData addParamter(Bundle bundle) {
        this.bundle = bundle;
        return this;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSubContent() {
        return subContent;
    }

    public void setSubContent(String subContent) {
        this.subContent = subContent;
    }
}
