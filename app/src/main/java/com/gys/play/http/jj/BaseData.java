package com.gys.play.http.jj;

import java.io.Serializable;

public class BaseData implements Serializable {
    private static final long serialVersionUID = 1217374310003390571L;
    private int ret;
    private String msg;
    private String data;

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public int getCode() {
        return ret;
    }

    public int getRet() {
        return ret;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return "BaseData{" +
                "ret=" + ret +
                ", msg='" + msg + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
