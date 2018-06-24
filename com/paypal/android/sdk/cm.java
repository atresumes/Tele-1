package com.paypal.android.sdk;

import android.util.Log;
import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLException;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class cm extends ci {
    private static final String f1102a = cm.class.getSimpleName();
    private static final MediaType f1103b = MediaType.parse("charset=utf-8");
    private final C0438a f1104c;
    private final String f1105d;
    private final cx f1106e;
    private final OkHttpClient f1107f;
    private final OkHttpClient f1108g;
    private final C0439b f1109h;
    private final ScheduledExecutorService f1110i;
    private final ConcurrentLinkedQueue f1111j;

    public cm(C0438a c0438a, String str, C0439b c0439b, cx cxVar, int i, boolean z, List list) {
        boolean z2 = true;
        this.f1104c = c0438a;
        this.f1105d = str;
        this.f1109h = c0439b;
        this.f1106e = cxVar;
        boolean d = bp.m163d(str);
        boolean z3 = d && !z;
        if (d) {
            z2 = false;
        }
        Builder a = cc.m176a(90, z3, z2, c0439b.mo2210b(), this.f1106e.mo2160e());
        a.interceptors().addAll(list);
        a.interceptors().add(new cj());
        this.f1107f = a.build();
        Builder a2 = cc.m176a(90, z3, z2, c0439b.mo2210b(), this.f1106e.mo2160e());
        a2.interceptors().add(new cj());
        this.f1108g = a2.build();
        this.f1110i = Executors.newSingleThreadScheduledExecutor();
        this.f1111j = new ConcurrentLinkedQueue();
    }

    private static String m1047a(String str, String str2) {
        if (str2 == null) {
            return str;
        }
        if (!str.endsWith("/")) {
            str = str + "/";
        }
        return str + str2;
    }

    static /* synthetic */ void m1050a(cm cmVar, cw cwVar, Response response, IOException iOException) {
        new StringBuilder().append(cwVar.m207n()).append(" failure.");
        if (response != null) {
            Log.e("paypal.sdk", "request failure with http statusCode:" + response.code() + ",exception:" + response.message());
            ci.m833a(cwVar, response.code());
            if (cwVar.m210q()) {
                cwVar.m192a(bz.INTERNAL_SERVER_ERROR.toString(), response.code() + " http response received.  Response not parsable.", null);
            }
        } else if (iOException == null) {
            throw new RuntimeException("Both Response or Exception cannot be null");
        } else if ((iOException instanceof SSLException) && "Connection closed by peer".equals(iOException.getMessage())) {
            cwVar.m188a(new ca(bz.DEVICE_OS_TOO_OLD, (Exception) iOException));
        } else {
            cwVar.m188a(new ca(bz.SERVER_COMMUNICATION_ERROR, (Exception) iOException));
        }
        Log.e("paypal.sdk", "request failed with server response:" + cwVar.m201g());
        cmVar.f1106e.mo2157a(cwVar);
    }

    private void m1051a(cw cwVar, String str, OkHttpClient okHttpClient, Callback callback) {
        switch (cp.f221a[cwVar.m202h().mo2165b().ordinal()]) {
            case 1:
                okHttpClient.newCall(new Request.Builder().url(m1047a(str, cwVar.m200f())).headers(m1055c(cwVar)).build()).enqueue(callback);
                return;
            case 2:
                okHttpClient.newCall(new Request.Builder().url(str).post(RequestBody.create(f1103b, cwVar.m200f())).headers(m1055c(cwVar)).build()).enqueue(callback);
                return;
            case 3:
                okHttpClient.newCall(new Request.Builder().url(m1047a(str, cwVar.m200f())).headers(m1055c(cwVar)).delete().build()).enqueue(callback);
                return;
            default:
                throw new RuntimeException(cwVar.m202h().mo2165b() + " not supported.");
        }
    }

    private static Headers m1055c(cw cwVar) {
        Headers.Builder builder = new Headers.Builder();
        for (Entry entry : cwVar.m203i().entrySet()) {
            builder.add((String) entry.getKey(), (String) entry.getValue());
        }
        return builder.build();
    }

    public final void mo2935a() {
        this.f1107f.dispatcher().cancelAll();
        this.f1108g.dispatcher().cancelAll();
    }

    public final boolean mo2936b(cw cwVar) {
        if (this.f1104c.m29a()) {
            cw.m186k();
            String a = cwVar.mo2929a(cwVar.m202h());
            try {
                if (cwVar.mo2930a()) {
                    new StringBuilder().append(cwVar.m207n()).append(" endpoint: ").append(a);
                    new StringBuilder().append(cwVar.m207n()).append(" request: ").append(cwVar.m200f());
                    this.f1111j.offer(new cn(this, cwVar, a));
                    int nextInt = new Random().nextInt(190) + 10;
                    new StringBuilder("Delaying tracking execution for ").append(nextInt).append(" seconds");
                    this.f1110i.schedule(new co(this), (long) nextInt, TimeUnit.SECONDS);
                } else {
                    new StringBuilder().append(cwVar.m207n()).append(" endpoint: ").append(a);
                    new StringBuilder().append(cwVar.m207n()).append(" request: ").append(cwVar.m200f());
                    m1051a(cwVar, a, this.f1107f, new cq(this, cwVar));
                }
                return true;
            } catch (Exception e) {
                Log.e(f1102a, "encoding failure", e);
                cwVar.m188a(new ca(bz.INTERNAL_ERROR, e));
                return false;
            } catch (Exception e2) {
                Log.e(f1102a, "communication failure", e2);
                cwVar.m188a(new ca(bz.SERVER_COMMUNICATION_ERROR, e2));
                return false;
            }
        }
        cwVar.m188a(new ca(bz.SERVER_COMMUNICATION_ERROR.toString()));
        return false;
    }
}
