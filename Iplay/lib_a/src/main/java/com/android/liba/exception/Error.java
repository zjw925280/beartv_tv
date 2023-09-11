package com.android.liba.exception;


import com.android.liba.network.protocol.ResponseHead;

import java.io.IOException;

public class Error extends IOException {
    //接口返回码
    public static final int CODE_SUCCESS = 200;



    public int code;
    public Error(String msg) {
        super(msg);
    }
    public Error(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public Error(ResponseHead response) {
        super(response.msg);
        this.code = response.ret;
    }




}
