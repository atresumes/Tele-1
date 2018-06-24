package com.paypal.android.sdk;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class db {
    private static final int f276a;
    private static int f277b;
    private static int f278c = ((f276a << 1) + 1);

    static {
        db dbVar = new db();
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        f276a = availableProcessors;
        f277b = availableProcessors + 1;
    }

    private db() {
        dd ddVar = new dd();
    }

    public static ThreadPoolExecutor m276a() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(f277b, f278c, 1, TimeUnit.SECONDS, new LinkedBlockingQueue());
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        return threadPoolExecutor;
    }
}
