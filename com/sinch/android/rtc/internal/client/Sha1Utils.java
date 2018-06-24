package com.sinch.android.rtc.internal.client;

import com.bumptech.glide.load.Key;
import java.security.MessageDigest;

class Sha1Utils {
    protected static final char[] hexArray = "0123456789ABCDEF".toCharArray();

    Sha1Utils() {
    }

    public static String bytesToHex(byte[] bArr) {
        char[] cArr = new char[(bArr.length * 2)];
        for (int i = 0; i < bArr.length; i++) {
            int i2 = bArr[i] & 255;
            cArr[i * 2] = hexArray[i2 >>> 4];
            cArr[(i * 2) + 1] = hexArray[i2 & 15];
        }
        return new String(cArr);
    }

    public static byte[] sha1(String str) {
        byte[] bArr = null;
        if (str != null) {
            try {
                bArr = MessageDigest.getInstance("SHA-1").digest(str.getBytes(Key.STRING_CHARSET_NAME));
            } catch (Exception e) {
            }
        }
        return bArr;
    }

    public static String sha1RawBytesAsString(String str) {
        return new String(sha1(str));
    }
}
