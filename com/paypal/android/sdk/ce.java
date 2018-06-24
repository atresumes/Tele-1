package com.paypal.android.sdk;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

final class ce implements HostnameVerifier {
    ce() {
    }

    public final boolean verify(String str, SSLSession sSLSession) {
        return true;
    }
}
