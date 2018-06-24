package com.paypal.android.sdk;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Properties;

public final class ar {
    public static String m68a(String str) {
        Properties properties = new Properties();
        InputStream byteArrayInputStream = new ByteArrayInputStream("suFileName=/system/xbin/su\nsuperUserApk=/system/app/Superuser.apk\nemptyIp=0.0.0.0".getBytes());
        try {
            properties.load(byteArrayInputStream);
            return properties.getProperty(str);
        } finally {
            byteArrayInputStream.close();
        }
    }
}
