package com.sinch.android.rtc.internal.natives;

public class LogLevel {
    public static final int CRITICAL = 3;
    public static final int INFO = 1;
    public static final int TRACE = 0;
    public static final int WARN = 2;

    public static int nativeToAndroid(int i) {
        switch (i) {
            case 0:
                return 2;
            case 2:
                return 5;
            case 3:
                return 6;
            default:
                return 4;
        }
    }
}
