package com.android.liba.util;


import com.android.liba.network.ApiManager;
import com.android.liba.network.Secret;

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESImpl implements Secret {

    IvParameterSpec iv;
    SecretKeySpec skeySpec;
    private static AESImpl instance = null;

    public static AESImpl getInstance() {
        return instance;
    }

    public static void init(ApiManager manager) {
        instance = new AESImpl(manager);
    }

    private AESImpl(ApiManager manager) {
        String doMain = manager.getNetProvider().getDominName().toLowerCase();
        String s = MD5.string2MD5(doMain);
        String key = s.substring(8, s.length() - 8);
        StringBuffer stringBuffer = new StringBuffer(key);
        String iV = stringBuffer.reverse().toString();
        try {
            byte[] raw = key.getBytes("ASCII");
            skeySpec = new SecretKeySpec(raw, "AES");
            iv = new IvParameterSpec(iV.getBytes());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        UIHelper.showLog(this, "解密初始化 doMain = " + doMain);
        UIHelper.showLog(this, "解密初始化 skeySpec = " + skeySpec);
        UIHelper.showLog(this, "解密初始化 iv = " + iv);
    }

    @Override
    public String encode(String src) {
        try {

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");// "算法/模式/补码方式"01
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            // 执行加密
            byte[] bytes = cipher.doFinal(src.getBytes("UTF-8"));
            return android.util.Base64.encodeToString(bytes, android.util.Base64.NO_WRAP);// 先用base64加密  NO_WRAP 可以避免使用平台特定的换行符。
        } catch (Exception e) {
            // 解析异常
            return "";
        }
    }

    @Override
    public String decode(String decode) {
//        UIHelper.showLog(this, "解密前：" + decode);
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 = android.util.Base64.decode(decode, android.util.Base64.DEFAULT);// 先用base64解密
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original, "utf-8");
//            UIHelper.showLog(this, "解密后：" + originalString);
            return originalString;
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }
}
