package com.paypal.android.sdk;

import android.text.TextUtils;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Interceptor.Chain;
import okhttp3.Request;
import okhttp3.Response;

public class dh implements Interceptor {
    private static final String f997a = dh.class.getSimpleName();
    private final String f998b;

    public dh(String str) {
        this.f998b = str;
    }

    private static String m844a(String str, String str2) {
        Mac instance = Mac.getInstance("HmacSHA1");
        instance.init(new SecretKeySpec(str.getBytes(), "HmacSHA1"));
        instance.update(str2.getBytes());
        byte[] doFinal = instance.doFinal();
        StringBuilder stringBuilder = new StringBuilder();
        int length = doFinal.length;
        for (int i = 0; i < length; i++) {
            stringBuilder.append(String.format("%02x", new Object[]{Byte.valueOf(doFinal[i])}));
        }
        return stringBuilder.toString();
    }

    public Response intercept(Chain chain) {
        int i = 0;
        Request request = chain.request();
        String format = String.format("Trace: [%s] %s, %s", new Object[]{this.f998b, "\"%08.8x: Operation = %80s Duration: %8.2f Iterations: %+4d\"", "memorySize * 8 + offset"});
        try {
            Headers headers = request.headers();
            List arrayList = new ArrayList();
            while (i < headers.size()) {
                arrayList.add(headers.name(i) + ": " + headers.value(i));
                i++;
            }
            Collections.sort(arrayList);
            return chain.proceed(request.newBuilder().removeHeader("PayPal-Item-Id").addHeader("PayPal-Item-Id", m844a(format, TextUtils.join(";", arrayList.toArray()) + request.body())).build());
        } catch (InvalidKeyException e) {
            return chain.proceed(request);
        } catch (NoSuchAlgorithmException e2) {
            return chain.proceed(request);
        }
    }
}
