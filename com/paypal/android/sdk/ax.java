package com.paypal.android.sdk;

public final class ax {
    private static String f116a = null;
    private static long f117b = 0;
    private static long f118c = 0;

    public static synchronized void m104a() {
        synchronized (ax.class) {
            f116a = bn.m151b(Boolean.TRUE.booleanValue());
            f117b = System.currentTimeMillis();
        }
    }

    public static synchronized void m105a(long j) {
        synchronized (ax.class) {
            f118c = j;
        }
    }

    public static synchronized String m106b() {
        String str;
        synchronized (ax.class) {
            if (f116a == null) {
                m104a();
            }
            str = f116a;
        }
        return str;
    }

    public static synchronized boolean m107c() {
        boolean z;
        synchronized (ax.class) {
            z = System.currentTimeMillis() - m108d() <= f118c;
        }
        return z;
    }

    private static synchronized long m108d() {
        long j;
        synchronized (ax.class) {
            if (f117b == 0) {
                m104a();
            }
            j = f117b;
        }
        return j;
    }
}
