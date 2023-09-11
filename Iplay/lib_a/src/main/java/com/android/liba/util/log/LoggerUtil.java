package com.android.liba.util.log;

public class LoggerUtil {

    private static LoggerUtil loggerUtil;
    private boolean isShowLog;

    public static LoggerUtil instance() {
        if (loggerUtil == null) {
            loggerUtil = new LoggerUtil();
        }
        return loggerUtil;
    }

    private LoggerUtil() {
    }

    public void setShowLog(boolean showLog) {
        isShowLog = showLog;
    }

    public synchronized void addLog(Object object) {
        if (!isShowLog) {
            return;
        }
        MyLogQueue.add(object);
    }

    public synchronized void addLog(Object... objects) {
        if (!isShowLog) {
            return;
        }
        if (objects == null) {
            MyLogQueue.add("null");
        } else {
            MyLogQueue.add(objects);
        }
    }

    public synchronized void addLog(Object key, Object object) {
        MyLogQueue.add(key, object);
    }

}
