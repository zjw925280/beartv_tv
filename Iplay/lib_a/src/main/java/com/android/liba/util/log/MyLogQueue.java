package com.android.liba.util.log;

import android.text.TextUtils;
import android.util.Log;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class MyLogQueue {

    private static final LinkedBlockingQueue<Object> queue = new LinkedBlockingQueue<>(Integer.MAX_VALUE);
    private static boolean isPrinting;
    private static final String keyValueSp = "!##!";
    private static final String defaultKey = "LOG";

    public static synchronized void add(Object data) {
        if (data == null) {
            data = "null";
        }
        queue.add(data);
        print();
    }

    public static synchronized void add(Object... dataArray) {
        if (dataArray == null) {
            add("null");
            return;
        }
        queue.addAll(Arrays.asList(dataArray));
        print();
    }

    public static synchronized void add(List<Object> list) {
        if (list == null) {
            add("null");
            return;
        }
        queue.addAll(list);
        print();
    }

    public static synchronized void add(Object key, Object data) {
        if (data == null) {
            data = "null";
        }
        if (data instanceof String) {
            queue.add(getKey(key) + keyValueSp + data);
            print();
        } else if (data instanceof List) {
            List<Object> list = (List<Object>) data;
            for (Object o : list) {
                if (o == null) continue;
                queue.add(getKey(key) + keyValueSp + o);
            }
            print();
        } else {
            queue.add(getKey(key) + keyValueSp + getStr(data));
            print();
        }
    }

    public static synchronized Object getFirst() {
        return queue.poll();
    }

    private static synchronized void print() {
        if (isPrinting) return;
        Object first = getFirst();
        while (first != null) {
            String firstStr = first.toString();
            if (firstStr.contains(keyValueSp)) {
                String[] split = firstStr.split(keyValueSp);
                if (split.length == 1) {
                    Log.e(defaultKey, split[0]);
                } else if (split.length == 2) {
                    if (TextUtils.isEmpty(split[0])) {
                        split[0] = defaultKey;
                    }
                    Log.e(split[0], split[1]);
                } else {
                    Log.e(defaultKey, Arrays.toString(split));
                }
            } else {
                Log.e(defaultKey, firstStr);
            }

            first = getFirst();
            isPrinting = first != null;
        }
    }

    private static String getKey(Object key) {
        String keyStr;
        if (key == null) {
            keyStr = defaultKey;
        } else {
            if (key instanceof String) {
                keyStr = (String) key;
            } else {
                Class<?> aClass = key.getClass();
                keyStr = aClass.getSimpleName();
                if (TextUtils.isEmpty(keyStr)) {
                    //通过new 抽象类的方式，getSimpleName 获取到的是""，getName 有内容------比如getClass = class com.driving.test.dictionary.ui.activity.RealTestRoomActivity$e,getSimpleName = ,getName = com.driving.test.dictionary.ui.activity.RealTestRoomActivity$e
                    Class<?> superclass = aClass.getSuperclass();
                    if (superclass != null) {
                        keyStr = superclass.getSimpleName();
                    }
                    if (TextUtils.isEmpty(keyStr) || keyStr.length() == 1) {//被混淆后名字可能就1个字符，super-b，更不知道是啥
                        String name = aClass.getName();
                        if (name.contains(".")) {
                            keyStr = name.substring(name.lastIndexOf(".") + 1);
                        } else {
                            keyStr = name;
                        }
                    } else {
                        keyStr = "super-" + keyStr;
                    }
                }
            }
        }
        return keyStr;
    }

    private static String getStr(Object object) {
        if (object == null) {
            return "null";
        }
        if (!object.getClass().isArray()) {
            return object.toString();
        }
        if (object instanceof boolean[]) {
            return Arrays.toString((boolean[]) object);
        }
        if (object instanceof byte[]) {
            return Arrays.toString((byte[]) object);
        }
        if (object instanceof char[]) {
            return Arrays.toString((char[]) object);
        }
        if (object instanceof short[]) {
            return Arrays.toString((short[]) object);
        }
        if (object instanceof int[]) {
            return Arrays.toString((int[]) object);
        }
        if (object instanceof long[]) {
            return Arrays.toString((long[]) object);
        }
        if (object instanceof float[]) {
            return Arrays.toString((float[]) object);
        }
        if (object instanceof double[]) {
            return Arrays.toString((double[]) object);
        }
        if (object instanceof Object[]) {
            return Arrays.deepToString((Object[]) object);
        }
        return "Couldn't find a correct type for the object";
    }
} 
