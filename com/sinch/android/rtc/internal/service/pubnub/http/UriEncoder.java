package com.sinch.android.rtc.internal.service.pubnub.http;

public final class UriEncoder {
    private UriEncoder() {
    }

    public static String encode(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            if (isUnsafe(charAt)) {
                stringBuilder.append('%');
                stringBuilder.append(toHex(charAt / 16));
                stringBuilder.append(toHex(charAt % 16));
            } else {
                stringBuilder.append(charAt);
            }
        }
        return stringBuilder.toString();
    }

    private static boolean isUnsafe(char c) {
        return " ~`!@#$%^&*()+=[]\\{}|;':\",./<>?".indexOf(c) >= 0;
    }

    private static char toHex(int i) {
        return (char) (i < 10 ? i + 48 : (i + 65) - 10);
    }
}
