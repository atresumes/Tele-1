package com.paypal.android.sdk;

import com.payUMoney.sdk.SdkConstants;
import okhttp3.Interceptor;
import okhttp3.Interceptor.Chain;
import okhttp3.Response;

public final class ch implements Interceptor {
    private final String f989a;

    public ch(String str) {
        this.f989a = str == null ? null : str.replaceAll("[^\\x00-\\x7F]", "");
    }

    public final Response intercept(Chain chain) {
        return chain.proceed(chain.request().newBuilder().removeHeader(SdkConstants.USER_AGENT).addHeader(SdkConstants.USER_AGENT, this.f989a).build());
    }
}
