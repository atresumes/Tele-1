package com.paypal.android.sdk;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.TrustManager;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient.Builder;

public class cc {
    private static final String f214a = cc.class.getSimpleName();

    public static Builder m176a(int i, boolean z, boolean z2, String str, String str2) {
        new StringBuilder("Creating okhttp client.  networkTimeout=").append(i).append(", isTrustAll=").append(z).append(", useSslPinning=").append(z2).append(", userAgent=").append(str).append(", baseUrl=").append(str2);
        Builder connectionSpecs = new Builder().connectionSpecs(Arrays.asList(new ConnectionSpec[]{ConnectionSpec.MODERN_TLS}));
        connectionSpecs.connectTimeout(Integer.valueOf(i).longValue(), TimeUnit.SECONDS);
        connectionSpecs.readTimeout(Integer.valueOf(i).longValue(), TimeUnit.SECONDS);
        connectionSpecs.interceptors().add(new ch(str));
        if (z) {
            try {
                TrustManager[] trustManagerArr = new TrustManager[]{new cd()};
                SSLContext instance = SSLContext.getInstance("TLSv1");
                instance.init(null, trustManagerArr, null);
                connectionSpecs.sslSocketFactory(instance.getSocketFactory());
                connectionSpecs.hostnameVerifier(new ce());
            } catch (NoSuchAlgorithmException e) {
                Throwable e2 = e;
                throw new RuntimeException(e2);
            } catch (KeyManagementException e3) {
                e2 = e3;
                throw new RuntimeException(e2);
            } catch (SSLException e4) {
                e2 = e4;
                throw new RuntimeException(e2);
            }
        } else if (z2) {
            connectionSpecs.sslSocketFactory(new cg(cf.m177a()));
        } else {
            connectionSpecs.sslSocketFactory(new cg());
        }
        return connectionSpecs;
    }
}
