package com.android.liba.network;

public interface Secret {
    String encode(String src);
    String decode(String decode);
}
