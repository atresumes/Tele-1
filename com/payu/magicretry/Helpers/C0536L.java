package com.payu.magicretry.Helpers;

import android.util.Log;

public class C0536L {
    public static final int DEBUG = 4;
    private static final String DEFAULT_LOG_TAG = "### PAYU ####";
    private static final String DEFAULT_TIMESTAMP_TAG = "PAYU-TIMESTAMP";
    public static final int ERROR = 6;
    public static final int INFO = 3;
    public static final int NONE = 7;
    public static final int VERBOSE = 2;
    public static final int WARN = 5;
    private static int sLogLevel = 7;

    public static synchronized void m765t(String message) {
        synchronized (C0536L.class) {
            if (sLogLevel <= 2) {
                Log.v(DEFAULT_TIMESTAMP_TAG, message);
            }
        }
    }

    public static synchronized void m769v(String tag, String message) {
        synchronized (C0536L.class) {
            if (sLogLevel <= 2) {
                Log.v(tag, message);
            }
        }
    }

    public static synchronized void m767v(String message) {
        synchronized (C0536L.class) {
            if (sLogLevel <= 2) {
                Log.v(DEFAULT_LOG_TAG, message);
            }
        }
    }

    public static synchronized void m766v(int message) {
        synchronized (C0536L.class) {
            if (sLogLevel <= 2) {
                C0536L.m769v(DEFAULT_LOG_TAG, message + "");
            }
        }
    }

    public static synchronized void m768v(String tag, int message) {
        synchronized (C0536L.class) {
            if (sLogLevel <= 2) {
                C0536L.m769v(tag, message + "");
            }
        }
    }

    public static synchronized void m762d(String tag, String message) {
        synchronized (C0536L.class) {
            if (sLogLevel <= 4) {
                Log.d(tag, message);
            }
        }
    }

    public static synchronized void m770w(String tag, String message) {
        synchronized (C0536L.class) {
            if (sLogLevel <= 5) {
                Log.w(tag, message);
            }
        }
    }

    public static synchronized void m764i(String tag, String message) {
        synchronized (C0536L.class) {
            if (sLogLevel <= 3) {
                Log.i(tag, message);
            }
        }
    }

    public static synchronized void m763e(String tag, String message) {
        synchronized (C0536L.class) {
            if (sLogLevel <= 6) {
                Log.e(tag, message);
            }
        }
    }
}
