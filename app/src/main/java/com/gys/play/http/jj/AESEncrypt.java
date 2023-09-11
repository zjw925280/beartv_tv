package com.gys.play.http.jj;


import com.android.liba.util.UIHelper;

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES 加密、解密
 */
class AESEncrypt {
    private static final String transformation = "AES/CBC/PKCS5Padding";
    private static IvParameterSpec iv;
    private static SecretKeySpec secretKeySpec;

    static {
        String[] split = HttpMethods.INSTANCE.getBaseUrl().split("/");
        String doMain = split[2].trim();
        String s = UIHelper.md5(doMain);
        String key = s.substring(8, s.length() - 8);
        StringBuilder stringBuffer = new StringBuilder(key);
        String iV = stringBuffer.reverse().toString();
        try {
            byte[] raw = key.getBytes("ASCII");
            secretKeySpec = new SecretKeySpec(raw, "AES");
            iv = new IvParameterSpec(iV.getBytes());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加密
     */
    public static String encode(String str) {
        try {
            Cipher cipher = Cipher.getInstance(transformation);// "算法/模式/补码方式"01
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, iv);
            // 执行加密
            byte[] bytes = cipher.doFinal(str.getBytes("UTF-8"));
            return android.util.Base64.encodeToString(bytes, android.util.Base64.NO_WRAP);// 先用base64加密  NO_WRAP 可以避免使用平台特定的换行符。
        } catch (Exception e) {
            // 解析异常
            return "";
        }
    }

    /**
     * 解密
     */
    public static String decodeReal(String decode) {
        try {
            Cipher cipher = Cipher.getInstance(transformation);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, iv);
            byte[] encrypted1 = android.util.Base64.decode(decode, android.util.Base64.DEFAULT);// 先用base64解密
            byte[] original = cipher.doFinal(encrypted1);
            return new String(original, "utf-8");
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

}
