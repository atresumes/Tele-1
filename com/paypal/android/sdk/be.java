package com.paypal.android.sdk;

import android.os.Handler;
import java.util.HashMap;
import java.util.Map;

public final class be extends bh {
    private String f972a;
    private HashMap f973b;
    private Map f974c = new HashMap();
    private Handler f975d;
    private boolean f976e;

    static {
        at.class.getSimpleName();
    }

    public be(String str, HashMap hashMap, Handler handler, boolean z) {
        this.f972a = str;
        this.f973b = hashMap;
        this.f975d = handler;
        this.f976e = z;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void run() {
        /*
        r8 = this;
        r1 = 1;
        r3 = 0;
        r0 = r8.f975d;
        if (r0 != 0) goto L_0x0007;
    L_0x0006:
        return;
    L_0x0007:
        r0 = r8.f975d;	 Catch:{ Exception -> 0x00c6 }
        r2 = r8.f975d;	 Catch:{ Exception -> 0x00c6 }
        r4 = 0;
        r5 = r8.f972a;	 Catch:{ Exception -> 0x00c6 }
        r2 = android.os.Message.obtain(r2, r4, r5);	 Catch:{ Exception -> 0x00c6 }
        r0.sendMessage(r2);	 Catch:{ Exception -> 0x00c6 }
        r0 = r8.f976e;	 Catch:{ Exception -> 0x00c6 }
        if (r0 != 0) goto L_0x0022;
    L_0x0019:
        r0 = r8.f974c;	 Catch:{ Exception -> 0x00c6 }
        r2 = "CLIENT-AUTH";
        r4 = "No cert";
        r0.put(r2, r4);	 Catch:{ Exception -> 0x00c6 }
    L_0x0022:
        r0 = r8.f974c;	 Catch:{ Exception -> 0x00c6 }
        r2 = "X-PAYPAL-RESPONSE-DATA-FORMAT";
        r4 = "NV";
        r0.put(r2, r4);	 Catch:{ Exception -> 0x00c6 }
        r0 = r8.f974c;	 Catch:{ Exception -> 0x00c6 }
        r2 = "X-PAYPAL-REQUEST-DATA-FORMAT";
        r4 = "NV";
        r0.put(r2, r4);	 Catch:{ Exception -> 0x00c6 }
        r0 = r8.f974c;	 Catch:{ Exception -> 0x00c6 }
        r2 = "X-PAYPAL-SERVICE-VERSION";
        r4 = "1.0.0";
        r0.put(r2, r4);	 Catch:{ Exception -> 0x00c6 }
        r0 = r8.f976e;	 Catch:{ Exception -> 0x00c6 }
        if (r0 == 0) goto L_0x00fc;
    L_0x0041:
        r0 = com.paypal.android.sdk.at.f87a;	 Catch:{ Exception -> 0x00c6 }
        r4 = r0.mo2151a();	 Catch:{ Exception -> 0x00c6 }
        r0 = r8.f972a;	 Catch:{ Exception -> 0x00c6 }
        r0 = android.net.Uri.parse(r0);	 Catch:{ Exception -> 0x00c6 }
        r4.mo2153a(r0);	 Catch:{ Exception -> 0x00c6 }
        r0 = r8.f974c;	 Catch:{ Exception -> 0x00c6 }
        r4.mo2154a(r0);	 Catch:{ Exception -> 0x00c6 }
        r0 = r8.f973b;	 Catch:{ Exception -> 0x00c6 }
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00c6 }
        r5.<init>();	 Catch:{ Exception -> 0x00c6 }
        r0 = r0.entrySet();	 Catch:{ Exception -> 0x00c6 }
        r6 = r0.iterator();	 Catch:{ Exception -> 0x00c6 }
    L_0x0064:
        r0 = r6.hasNext();	 Catch:{ Exception -> 0x00c6 }
        if (r0 == 0) goto L_0x009f;
    L_0x006a:
        r0 = r6.next();	 Catch:{ Exception -> 0x00c6 }
        r0 = (java.util.Map.Entry) r0;	 Catch:{ Exception -> 0x00c6 }
        if (r1 == 0) goto L_0x0098;
    L_0x0072:
        r2 = r3;
    L_0x0073:
        r1 = r0.getKey();	 Catch:{ Exception -> 0x00c6 }
        r1 = (java.lang.String) r1;	 Catch:{ Exception -> 0x00c6 }
        r7 = "UTF-8";
        r1 = java.net.URLEncoder.encode(r1, r7);	 Catch:{ Exception -> 0x00c6 }
        r5.append(r1);	 Catch:{ Exception -> 0x00c6 }
        r1 = "=";
        r5.append(r1);	 Catch:{ Exception -> 0x00c6 }
        r0 = r0.getValue();	 Catch:{ Exception -> 0x00c6 }
        r0 = (java.lang.String) r0;	 Catch:{ Exception -> 0x00c6 }
        r1 = "UTF-8";
        r0 = java.net.URLEncoder.encode(r0, r1);	 Catch:{ Exception -> 0x00c6 }
        r5.append(r0);	 Catch:{ Exception -> 0x00c6 }
        r1 = r2;
        goto L_0x0064;
    L_0x0098:
        r2 = "&";
        r5.append(r2);	 Catch:{ Exception -> 0x00c6 }
        r2 = r1;
        goto L_0x0073;
    L_0x009f:
        r0 = r5.toString();	 Catch:{ Exception -> 0x00c6 }
        r1 = "UTF-8";
        r0 = r0.getBytes(r1);	 Catch:{ Exception -> 0x00c6 }
        r0 = r4.mo2152a(r0);	 Catch:{ Exception -> 0x00c6 }
        r1 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r0 == r1) goto L_0x00dc;
    L_0x00b1:
        r1 = new java.lang.Exception;	 Catch:{ Exception -> 0x00c6 }
        r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00c6 }
        r3 = "Network Connection Error with wrong http code: ";
        r2.<init>(r3);	 Catch:{ Exception -> 0x00c6 }
        r0 = r2.append(r0);	 Catch:{ Exception -> 0x00c6 }
        r0 = r0.toString();	 Catch:{ Exception -> 0x00c6 }
        r1.<init>(r0);	 Catch:{ Exception -> 0x00c6 }
        throw r1;	 Catch:{ Exception -> 0x00c6 }
    L_0x00c6:
        r0 = move-exception;
        r1 = r8.f975d;	 Catch:{ all -> 0x010b }
        r2 = r8.f975d;	 Catch:{ all -> 0x010b }
        r3 = 1;
        r0 = android.os.Message.obtain(r2, r3, r0);	 Catch:{ all -> 0x010b }
        r1.sendMessage(r0);	 Catch:{ all -> 0x010b }
        r0 = com.paypal.android.sdk.bi.m121a();
        r0.m124b(r8);
        goto L_0x0006;
    L_0x00dc:
        r0 = new java.lang.String;	 Catch:{ Exception -> 0x00c6 }
        r1 = r4.mo2155a();	 Catch:{ Exception -> 0x00c6 }
        r2 = "UTF-8";
        r0.<init>(r1, r2);	 Catch:{ Exception -> 0x00c6 }
        r1 = r8.f975d;	 Catch:{ Exception -> 0x00c6 }
        r2 = r8.f975d;	 Catch:{ Exception -> 0x00c6 }
        r3 = 2;
        r0 = android.os.Message.obtain(r2, r3, r0);	 Catch:{ Exception -> 0x00c6 }
        r1.sendMessage(r0);	 Catch:{ Exception -> 0x00c6 }
    L_0x00f3:
        r0 = com.paypal.android.sdk.bi.m121a();
        r0.m124b(r8);
        goto L_0x0006;
    L_0x00fc:
        r0 = r8.f975d;	 Catch:{ Exception -> 0x00c6 }
        r1 = r8.f975d;	 Catch:{ Exception -> 0x00c6 }
        r2 = 2;
        r3 = "not supported";
        r1 = android.os.Message.obtain(r1, r2, r3);	 Catch:{ Exception -> 0x00c6 }
        r0.sendMessage(r1);	 Catch:{ Exception -> 0x00c6 }
        goto L_0x00f3;
    L_0x010b:
        r0 = move-exception;
        r1 = com.paypal.android.sdk.bi.m121a();
        r1.m124b(r8);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.paypal.android.sdk.be.run():void");
    }
}
