package com.payUMoney.sdk.utils;

import android.util.Log;

public class SdkLogger {
    private static final boolean DETAIL_ENABLE = true;
    private static final boolean LOG_ENABLE = true;
    private static String LOG_PREFIX = "SDK_PAYU";
    private static final int LOG_PREFIX_LENGTH = LOG_PREFIX.length();
    private static final int MAX_LOG_TAG_LENGTH = 23;

    public static String makeLogTag(String str) {
        if (str.length() > 23 - LOG_PREFIX_LENGTH) {
            return LOG_PREFIX + str.substring(0, (23 - LOG_PREFIX_LENGTH) - 1);
        }
        return LOG_PREFIX + str;
    }

    public static String makeLogTag(Class cls) {
        return makeLogTag(cls.getSimpleName());
    }

    private SdkLogger() {
    }

    public static void setTAG(String TAG) {
        LOG_PREFIX = TAG;
    }

    private static String buildMsg(String msg) {
        StringBuilder buffer = new StringBuilder();
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[4];
        buffer.append("[ ");
        buffer.append(Thread.currentThread().getName());
        buffer.append(": ");
        buffer.append(stackTraceElement.getFileName());
        buffer.append(": ");
        buffer.append(stackTraceElement.getLineNumber());
        buffer.append(": ");
        buffer.append(stackTraceElement.getMethodName());
        buffer.append("() ] --> ");
        buffer.append(msg);
        return buffer.toString();
    }

    public static void m21v(String msg) {
        if (Log.isLoggable(LOG_PREFIX, 2)) {
            Log.v(LOG_PREFIX, buildMsg(msg));
        }
    }

    public static void m13d(String msg) {
        if (Log.isLoggable(LOG_PREFIX, 3)) {
            Log.d(LOG_PREFIX, buildMsg(msg));
        }
    }

    public static void m19i(String msg) {
        if (Log.isLoggable(LOG_PREFIX, 4)) {
            Log.i(LOG_PREFIX, buildMsg(msg));
        }
    }

    public static void m23w(String msg) {
        if (Log.isLoggable(LOG_PREFIX, 5)) {
            Log.w(LOG_PREFIX, buildMsg(msg));
        }
    }

    public static void m24w(String msg, Exception e) {
        if (Log.isLoggable(LOG_PREFIX, 5)) {
            Log.w(LOG_PREFIX, buildMsg(msg), e);
        }
    }

    public static void m15e(String msg) {
        if (Log.isLoggable(LOG_PREFIX, 6)) {
            Log.e(LOG_PREFIX, buildMsg(msg));
        }
    }

    public static void m16e(String msg, Exception e) {
        if (Log.isLoggable(LOG_PREFIX, 6)) {
            Log.e(LOG_PREFIX, buildMsg(msg), e);
        }
    }

    public static void m22v(String TAG, String msg) {
        if (Log.isLoggable(TAG, 2)) {
            Log.v(TAG, buildMsg(msg));
        }
    }

    public static void m14d(String TAG, String msg) {
        if (Log.isLoggable(TAG, 3)) {
            Log.d(TAG, buildMsg(msg));
        }
    }

    public static void m20i(String TAG, String msg) {
        if (Log.isLoggable(TAG, 4)) {
            Log.i(TAG, buildMsg(msg));
        }
    }

    public static void m25w(String TAG, String msg) {
        if (Log.isLoggable(TAG, 5)) {
            Log.w(TAG, buildMsg(msg));
        }
    }

    public static void m26w(String TAG, String msg, Exception e) {
        if (Log.isLoggable(TAG, 5)) {
            Log.w(TAG, buildMsg(msg), e);
        }
    }

    public static void m17e(String TAG, String msg) {
        if (Log.isLoggable(TAG, 6)) {
            Log.e(TAG, buildMsg(msg));
        }
    }

    public static void m18e(String TAG, String msg, Exception e) {
        if (Log.isLoggable(TAG, 6)) {
            Log.e(TAG, buildMsg(msg), e);
        }
    }
}
