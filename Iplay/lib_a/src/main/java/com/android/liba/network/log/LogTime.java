package com.android.liba.network.log;

import java.util.concurrent.TimeUnit;


public class LogTime {
    private final long startNs;

    public LogTime() {
        this.startNs = System.nanoTime();
    }

    public long tookMs() {
        return TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
    }
}
