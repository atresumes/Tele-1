package com.paypal.android.sdk;

import java.util.List;
import java.util.Map.Entry;
import okhttp3.Interceptor;
import okhttp3.Interceptor.Chain;
import okhttp3.Request;
import okhttp3.Response;

public class cj implements Interceptor {
    private static final String f991a = cj.class.getSimpleName();

    public Response intercept(Chain chain) {
        Request request = chain.request();
        for (Entry entry : request.headers().toMultimap().entrySet()) {
            for (String append : (List) entry.getValue()) {
                new StringBuilder().append((String) entry.getKey()).append("=").append(append);
            }
        }
        return chain.proceed(request);
    }
}
