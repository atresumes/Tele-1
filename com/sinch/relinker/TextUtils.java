package com.sinch.relinker;

final class TextUtils {
    TextUtils() {
    }

    public static boolean isEmpty(CharSequence charSequence) {
        return charSequence == null || charSequence.length() == 0;
    }
}
