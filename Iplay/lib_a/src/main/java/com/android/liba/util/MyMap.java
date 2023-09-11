package com.android.liba.util;

import java.util.ArrayList;
import java.util.List;

public class MyMap<K, V> {
    private final List<K> keyList = new ArrayList<>();
    private final List<V> valueList = new ArrayList<>();

    public synchronized void put(K k, V v) {
        if (keyList.contains(k)) {
            int index = keyList.indexOf(k);
            valueList.set(index, v);
        } else {
            keyList.add(k);
            valueList.add(v);
        }
    }

    public synchronized V get(K k) {
        if (keyList.contains(k)) {
            int index = keyList.indexOf(k);
            return valueList.get(index);
        } else {
            return null;
        }
    }

    public synchronized List<K> getKeyList() {
        return new ArrayList<>(keyList);
//        return keyList;
    }

    public synchronized List<V> getValueList() {
        return new ArrayList<>(valueList);
    }

    public synchronized boolean containsKey(K key) {
        return keyList.contains(key);
    }

    public synchronized void remove(K key) {
        if (keyList.contains(key)) {
            int index = keyList.indexOf(key);
            keyList.remove(key);
            valueList.remove(valueList.get(index));
        }
    }

    public synchronized int size() {
        return keyList.size();
    }

    public synchronized void clear() {
        keyList.clear();
        valueList.clear();
    }
}
