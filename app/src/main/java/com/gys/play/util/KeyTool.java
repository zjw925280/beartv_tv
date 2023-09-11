package com.gys.play.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;


public class KeyTool {
    private static final String TAG = "KeyTool";

    public static void logAll(Context context) {
        Log.e(TAG, "getKeyHash: " + getKeyHash(context));
        Log.e(TAG, "getSHA1: " + getSHA1(context));
        Log.e(TAG, "getMD5: " + getMD5(context));
        Log.e(TAG, "getSHA256: " + getSHA256(context));
    }

    public static String getKeyHash(Context context) {
        String s = "";
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);

            for (Signature data : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(data.toByteArray());
                s = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.e(TAG, "KeyHash: " + s);
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return s;
    }


    public static String getSHA1(Context context) {
        return getSign(context, "SHA1");
    }

    public static String getMD5(Context context) {
        return getSign(context, "MD5");
    }

    public static String getSHA256(Context context) {
        return getSign(context, "SHA256");
    }

    private static String getSign(Context context, String algorithm) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            return result.substring(0, result.length() - 1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

}
