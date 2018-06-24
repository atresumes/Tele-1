package com.payu.custombrowser.util;

import android.util.Log;

public class C0533L {
    public static final int DEBUG = 4;
    private static final String DEFAULT_LOG_TAG = "### PAYU ####";
    private static final String DEFAULT_TIMESTAMP_TAG = "PAYU-TIMESTAMP";
    public static final int ERROR = 6;
    public static final int INFO = 3;
    public static final int NONE = 7;
    public static final int VERBOSE = 2;
    public static final int WARN = 5;
    private static int sLogLevel = 7;

    public static synchronized void m756t(String message) {
        synchronized (C0533L.class) {
            if (sLogLevel <= 2) {
                Log.v(DEFAULT_TIMESTAMP_TAG, message);
            }
        }
    }

    public static synchronized void m760v(String tag, String message) {
        synchronized (C0533L.class) {
            if (sLogLevel <= 2) {
                Log.v(tag, message);
            }
        }
    }

    public static synchronized void m758v(String message) {
        synchronized (C0533L.class) {
            if (sLogLevel <= 2) {
                Log.v(DEFAULT_LOG_TAG, message);
            }
        }
    }

    public static synchronized void m757v(int message) {
        synchronized (C0533L.class) {
            if (sLogLevel <= 2) {
                C0533L.m760v(DEFAULT_LOG_TAG, message + "");
            }
        }
    }

    public static synchronized void m759v(String tag, int message) {
        synchronized (C0533L.class) {
            if (sLogLevel <= 2) {
                C0533L.m760v(tag, message + "");
            }
        }
    }

    public static synchronized void m753d(String tag, String message) {
        synchronized (C0533L.class) {
            if (sLogLevel <= 4) {
                Log.d(tag, message);
            }
        }
    }

    public static synchronized void m761w(String tag, String message) {
        synchronized (C0533L.class) {
            if (sLogLevel <= 5) {
                Log.w(tag, message);
            }
        }
    }

    public static synchronized void m755i(String tag, String message) {
        synchronized (C0533L.class) {
            if (sLogLevel <= 3) {
                Log.i(tag, message);
            }
        }
    }

    public static synchronized void m754e(String tag, String message) {
        synchronized (C0533L.class) {
            if (sLogLevel <= 6) {
                Log.e(tag, message);
            }
        }
    }
}
