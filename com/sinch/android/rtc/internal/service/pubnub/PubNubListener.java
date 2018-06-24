package com.sinch.android.rtc.internal.service.pubnub;

import com.sinch.android.rtc.internal.client.Sleepable;
import com.sinch.android.rtc.internal.natives.PubSubscriber;
import com.sinch.android.rtc.internal.service.pubnub.http.HttpRequester;
import com.sinch.gson.JsonArray;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;

public class PubNubListener {
    public static final int MAX_RETRIES = 6;
    public static final int SUBSCRIBE_TIME_OUT = 310000;
    private static final String TAG = PubNubListener.class.getSimpleName();
    private volatile boolean active;
    private String baseRequest;
    private final Executor callbackExecutor;
    private final Set<PubSubscriber> callbacks;
    private final List<String> channels;
    private int numRetries = 0;
    private final HttpRequester requester;
    private final RetryHoldback retryHoldback;
    private final int timeOffsetOnSubscribeInMs;
    private long timeToken;

    public PubNubListener(HttpRequester httpRequester, String str, String str2, List<String> list, String str3, int i, Set<PubSubscriber> set, Executor executor, Sleepable sleepable) {
        this.requester = httpRequester;
        this.timeOffsetOnSubscribeInMs = i;
        this.callbacks = set;
        this.callbackExecutor = executor;
        this.retryHoldback = new RetryHoldback(sleepable);
        this.channels = list;
        this.baseRequest = str + "/" + "subscribe" + "/" + str2 + "/" + getEncodedSubscribeChannels(list) + "/" + "0" + "/";
        try {
            this.timeToken = Long.parseLong(str3);
        } catch (NumberFormatException e) {
            this.timeToken = 0;
        }
        this.active = true;
    }

    private String getChannelForMessageNo(String[] strArr, int i) {
        String str = "";
        return this.channels.size() == 1 ? (String) this.channels.get(0) : (this.channels.size() <= 1 || strArr == null || strArr.length <= i) ? str : strArr[i];
    }

    private static String getCommaSeparatedString(List<String> list) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            stringBuilder.append((String) list.get(i));
            if (i != list.size() - 1) {
                stringBuilder.append(",");
            }
        }
        return stringBuilder.toString();
    }

    private String getEncodedSubscribeChannels(List<String> list) {
        String commaSeparatedString = getCommaSeparatedString(list);
        try {
            commaSeparatedString = URLEncoder.encode(commaSeparatedString, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Throwable th) {
        }
        return commaSeparatedString;
    }

    private void handleData(JsonArray jsonArray, String[] strArr) {
        for (int i = 0; i < jsonArray.size(); i++) {
            final String asString = jsonArray.get(i).getAsString();
            synchronized (this.callbacks) {
                for (final PubSubscriber pubSubscriber : this.callbacks) {
                    final String[] strArr2 = strArr;
                    this.callbackExecutor.execute(new Runnable() {
                        public void run() {
                            if (PubNubListener.this.active) {
                                pubSubscriber.handleData(asString, PubNubListener.this.getChannelForMessageNo(strArr2, i), String.valueOf(PubNubListener.this.timeToken));
                            }
                        }
                    });
                }
            }
        }
    }

    private void handleFailed(final String str) {
        synchronized (this.callbacks) {
            for (final PubSubscriber pubSubscriber : this.callbacks) {
                this.callbackExecutor.execute(new Runnable() {
                    public void run() {
                        if (PubNubListener.this.active) {
                            pubSubscriber.handleFailure(str);
                        }
                    }
                });
            }
        }
    }

    private void resetTimeToken() {
        this.timeToken = 0;
    }

    public void abort() {
        this.active = false;
    }

    public void readMessage(com.sinch.android.rtc.internal.service.pubnub.PubNubSubscriber.SubscribeThread r15) {
        /* JADX: method processing error */
/*
Error: java.util.NoSuchElementException
	at java.util.HashMap$HashIterator.nextNode(HashMap.java:1431)
	at java.util.HashMap$KeyIterator.next(HashMap.java:1453)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.applyRemove(BlockFinallyExtract.java:535)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.extractFinally(BlockFinallyExtract.java:175)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.processExceptionHandler(BlockFinallyExtract.java:79)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.visit(BlockFinallyExtract.java:51)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
        /*
        r14 = this;
        r12 = 0;
        r10 = 2;
        r9 = 1002; // 0x3ea float:1.404E-42 double:4.95E-321;
        r8 = 6;
        r0 = r14.active;	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        if (r0 != 0) goto L_0x0039;
    L_0x000a:
        r0 = r14.numRetries;
        if (r0 < r8) goto L_0x0038;
    L_0x000e:
        r0 = TAG;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Permanently failing subscribe after ";
        r1 = r1.append(r2);
        r2 = r14.numRetries;
        r1 = r1.append(r2);
        r2 = " attempts.";
        r1 = r1.append(r2);
        r1 = r1.toString();
        android.util.Log.e(r0, r1);
        r0 = java.lang.String.valueOf(r9);
        r14.handleFailed(r0);
        r15.stopSelf();
    L_0x0038:
        return;
    L_0x0039:
        r0 = r14.retryHoldback;	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        r1 = r14.numRetries;	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        r0 = r0.getCurrentHoldBack(r1);	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        if (r0 <= 0) goto L_0x004a;	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
    L_0x0043:
        r0 = r14.retryHoldback;	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        r1 = r14.numRetries;	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        r0.holdback(r1);	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
    L_0x004a:
        r0 = new java.lang.StringBuilder;	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        r0.<init>();	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        r1 = r14.baseRequest;	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        r0 = r0.append(r1);	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        r2 = r14.timeToken;	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        r0 = r0.append(r2);	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        r0 = r0.toString();	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        r1 = r14.requester;	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        r2 = 310000; // 0x4baf0 float:4.34403E-40 double:1.531604E-318;	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        r0 = r1.get(r0, r2);	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        if (r0 != 0) goto L_0x009c;	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
    L_0x006a:
        r14.resetTimeToken();	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        r0 = r14.numRetries;
        if (r0 < r8) goto L_0x0038;
    L_0x0071:
        r0 = TAG;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Permanently failing subscribe after ";
        r1 = r1.append(r2);
        r2 = r14.numRetries;
        r1 = r1.append(r2);
        r2 = " attempts.";
        r1 = r1.append(r2);
        r1 = r1.toString();
        android.util.Log.e(r0, r1);
        r0 = java.lang.String.valueOf(r9);
        r14.handleFailed(r0);
        r15.stopSelf();
        goto L_0x0038;
    L_0x009c:
        r1 = new com.sinch.gson.JsonParser;	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        r1.<init>();	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        r0 = r1.parse(r0);	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        r1 = r0.isJsonArray();	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        if (r1 != 0) goto L_0x00de;	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
    L_0x00ab:
        r14.resetTimeToken();	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        r0 = r14.numRetries;
        if (r0 < r8) goto L_0x0038;
    L_0x00b2:
        r0 = TAG;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Permanently failing subscribe after ";
        r1 = r1.append(r2);
        r2 = r14.numRetries;
        r1 = r1.append(r2);
        r2 = " attempts.";
        r1 = r1.append(r2);
        r1 = r1.toString();
        android.util.Log.e(r0, r1);
        r0 = java.lang.String.valueOf(r9);
        r14.handleFailed(r0);
        r15.stopSelf();
        goto L_0x0038;
    L_0x00de:
        r4 = r0.getAsJsonArray();	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        r0 = r4.size();	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        if (r0 >= r10) goto L_0x011b;	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
    L_0x00e8:
        r14.resetTimeToken();	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        r0 = r14.numRetries;
        if (r0 < r8) goto L_0x0038;
    L_0x00ef:
        r0 = TAG;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Permanently failing subscribe after ";
        r1 = r1.append(r2);
        r2 = r14.numRetries;
        r1 = r1.append(r2);
        r2 = " attempts.";
        r1 = r1.append(r2);
        r1 = r1.toString();
        android.util.Log.e(r0, r1);
        r0 = java.lang.String.valueOf(r9);
        r14.handleFailed(r0);
        r15.stopSelf();
        goto L_0x0038;
    L_0x011b:
        r0 = 1;
        r0 = r4.get(r0);	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        r2 = r0.getAsLong();	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        r0 = r14.timeToken;	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        r0 = (r0 > r12 ? 1 : (r0 == r12 ? 0 : -1));	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        if (r0 != 0) goto L_0x02f1;	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
    L_0x012a:
        r0 = r14.timeOffsetOnSubscribeInMs;	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        r0 = (long) r0;	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        r6 = 10000; // 0x2710 float:1.4013E-41 double:4.9407E-320;	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        r0 = r0 * r6;	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        r0 = r0 + r2;	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        r5 = (r0 > r12 ? 1 : (r0 == r12 ? 0 : -1));	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        if (r5 <= 0) goto L_0x02f1;	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
    L_0x0135:
        r14.timeToken = r0;	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        r0 = 0;	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        r0 = r4.get(r0);	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        r1 = r0.getAsJsonArray();	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        r0 = r4.size();	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        if (r0 <= r10) goto L_0x0192;	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
    L_0x0146:
        r0 = 2;	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        r0 = r4.get(r0);	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        r0 = r0.getAsString();	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        r2 = ",";	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        r0 = r0.split(r2);	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        r2 = r0.length;	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        r3 = r1.size();	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        if (r2 == r3) goto L_0x015c;	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
    L_0x015c:
        r14.handleData(r1, r0);	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        r0 = 0;	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        r14.numRetries = r0;	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        r0 = r14.numRetries;
        if (r0 < r8) goto L_0x0038;
    L_0x0166:
        r0 = TAG;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Permanently failing subscribe after ";
        r1 = r1.append(r2);
        r2 = r14.numRetries;
        r1 = r1.append(r2);
        r2 = " attempts.";
        r1 = r1.append(r2);
        r1 = r1.toString();
        android.util.Log.e(r0, r1);
        r0 = java.lang.String.valueOf(r9);
        r14.handleFailed(r0);
        r15.stopSelf();
        goto L_0x0038;
    L_0x0192:
        r0 = 0;
        goto L_0x015c;
    L_0x0194:
        r0 = move-exception;
        r0 = TAG;	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        r1 = "Got interrupt request";	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        android.util.Log.v(r0, r1);	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        r0 = r14.numRetries;
        if (r0 < r8) goto L_0x0038;
    L_0x01a0:
        r0 = TAG;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Permanently failing subscribe after ";
        r1 = r1.append(r2);
        r2 = r14.numRetries;
        r1 = r1.append(r2);
        r2 = " attempts.";
        r1 = r1.append(r2);
        r1 = r1.toString();
        android.util.Log.e(r0, r1);
        r0 = java.lang.String.valueOf(r9);
        r14.handleFailed(r0);
        r15.stopSelf();
        goto L_0x0038;
    L_0x01cc:
        r0 = move-exception;
        r0 = r14.numRetries;
        if (r0 < r8) goto L_0x0038;
    L_0x01d1:
        r0 = TAG;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Permanently failing subscribe after ";
        r1 = r1.append(r2);
        r2 = r14.numRetries;
        r1 = r1.append(r2);
        r2 = " attempts.";
        r1 = r1.append(r2);
        r1 = r1.toString();
        android.util.Log.e(r0, r1);
        r0 = java.lang.String.valueOf(r9);
        r14.handleFailed(r0);
        r15.stopSelf();
        goto L_0x0038;
    L_0x01fd:
        r0 = move-exception;
        r1 = TAG;	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        r2 = new java.lang.StringBuilder;	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        r2.<init>();	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        r3 = "Attempt ";	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        r2 = r2.append(r3);	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        r3 = r14.numRetries;	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        r2 = r2.append(r3);	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        r3 = " -> IO exception while subscribing for data. ";	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        r2 = r2.append(r3);	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        r0 = r0.getMessage();	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        r0 = r2.append(r0);	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        r0 = r0.toString();	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        android.util.Log.w(r1, r0);	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        r0 = r14.numRetries;	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        r0 = r0 + 1;	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        r14.numRetries = r0;	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        r0 = r14.numRetries;
        if (r0 < r8) goto L_0x0038;
    L_0x0230:
        r0 = TAG;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Permanently failing subscribe after ";
        r1 = r1.append(r2);
        r2 = r14.numRetries;
        r1 = r1.append(r2);
        r2 = " attempts.";
        r1 = r1.append(r2);
        r1 = r1.toString();
        android.util.Log.e(r0, r1);
        r0 = java.lang.String.valueOf(r9);
        r14.handleFailed(r0);
        r15.stopSelf();
        goto L_0x0038;
    L_0x025c:
        r0 = move-exception;
        r0 = r14.numRetries;
        if (r0 < r8) goto L_0x0038;
    L_0x0261:
        r0 = TAG;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Permanently failing subscribe after ";
        r1 = r1.append(r2);
        r2 = r14.numRetries;
        r1 = r1.append(r2);
        r2 = " attempts.";
        r1 = r1.append(r2);
        r1 = r1.toString();
        android.util.Log.e(r0, r1);
        r0 = java.lang.String.valueOf(r9);
        r14.handleFailed(r0);
        r15.stopSelf();
        goto L_0x0038;
    L_0x028d:
        r0 = move-exception;
        r14.resetTimeToken();	 Catch:{ InterruptedException -> 0x0194, SocketTimeoutException -> 0x01cc, IOException -> 0x01fd, IllegalArgumentException -> 0x025c, JsonSyntaxException -> 0x028d, all -> 0x02c1 }
        r0 = r14.numRetries;
        if (r0 < r8) goto L_0x0038;
    L_0x0295:
        r0 = TAG;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Permanently failing subscribe after ";
        r1 = r1.append(r2);
        r2 = r14.numRetries;
        r1 = r1.append(r2);
        r2 = " attempts.";
        r1 = r1.append(r2);
        r1 = r1.toString();
        android.util.Log.e(r0, r1);
        r0 = java.lang.String.valueOf(r9);
        r14.handleFailed(r0);
        r15.stopSelf();
        goto L_0x0038;
    L_0x02c1:
        r0 = move-exception;
        r1 = r14.numRetries;
        if (r1 < r8) goto L_0x02f0;
    L_0x02c6:
        r1 = TAG;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "Permanently failing subscribe after ";
        r2 = r2.append(r3);
        r3 = r14.numRetries;
        r2 = r2.append(r3);
        r3 = " attempts.";
        r2 = r2.append(r3);
        r2 = r2.toString();
        android.util.Log.e(r1, r2);
        r1 = java.lang.String.valueOf(r9);
        r14.handleFailed(r1);
        r15.stopSelf();
    L_0x02f0:
        throw r0;
    L_0x02f1:
        r0 = r2;
        goto L_0x0135;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sinch.android.rtc.internal.service.pubnub.PubNubListener.readMessage(com.sinch.android.rtc.internal.service.pubnub.PubNubSubscriber$SubscribeThread):void");
    }
}
