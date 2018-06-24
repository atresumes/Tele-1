package com.payu.magicretry.analytics;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MRAnalytics {
    private static MRAnalytics INSTANCE = null;
    private static final boolean IS_PRODUCTION = true;
    private static final String PRODUCTION_URL = "https://info.payu.in/merchant/postservice.php?form=2";
    private static final String TEST_URL = "https://sdktest.payu.in/merchant/postservice.php?form=2";
    private static final long TIMER_DELAY = 5000;
    private String ANALYTICS_URL = PRODUCTION_URL;
    private String fileName;
    private final Activity mActivity;
    private ArrayList<String> mBuffer;
    private boolean mIsLocked = false;
    private Timer mTimer;

    class C05443 extends TimerTask {
        public void run() {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Unreachable block: B:149:?
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.modifyBlocksTree(BlockProcessor.java:248)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.rerun(BlockProcessor.java:44)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.visit(BlockFinallyExtract.java:57)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r22 = this;
        L_0x0000:
            r0 = r22;
            r0 = com.payu.magicretry.analytics.MRAnalytics.this;
            r19 = r0;
            r19 = r19.mIsLocked;
            if (r19 != 0) goto L_0x0000;
        L_0x000c:
            r0 = r22;
            r0 = com.payu.magicretry.analytics.MRAnalytics.this;
            r19 = r0;
            r19.setLock();
            r0 = r22;
            r0 = com.payu.magicretry.analytics.MRAnalytics.this;
            r19 = r0;
            r19 = r19.isOnline();
            if (r19 == 0) goto L_0x0192;
        L_0x0021:
            r16 = "";
            r7 = new java.io.File;	 Catch:{ IOException -> 0x0228, all -> 0x0391 }
            r0 = r22;	 Catch:{ IOException -> 0x0228, all -> 0x0391 }
            r0 = com.payu.magicretry.analytics.MRAnalytics.this;	 Catch:{ IOException -> 0x0228, all -> 0x0391 }
            r19 = r0;	 Catch:{ IOException -> 0x0228, all -> 0x0391 }
            r19 = r19.mActivity;	 Catch:{ IOException -> 0x0228, all -> 0x0391 }
            r19 = r19.getFilesDir();	 Catch:{ IOException -> 0x0228, all -> 0x0391 }
            r0 = r22;	 Catch:{ IOException -> 0x0228, all -> 0x0391 }
            r0 = com.payu.magicretry.analytics.MRAnalytics.this;	 Catch:{ IOException -> 0x0228, all -> 0x0391 }
            r20 = r0;	 Catch:{ IOException -> 0x0228, all -> 0x0391 }
            r20 = r20.fileName;	 Catch:{ IOException -> 0x0228, all -> 0x0391 }
            r0 = r19;	 Catch:{ IOException -> 0x0228, all -> 0x0391 }
            r1 = r20;	 Catch:{ IOException -> 0x0228, all -> 0x0391 }
            r7.<init>(r0, r1);	 Catch:{ IOException -> 0x0228, all -> 0x0391 }
            r19 = r7.exists();	 Catch:{ IOException -> 0x0228, all -> 0x0391 }
            if (r19 != 0) goto L_0x0063;	 Catch:{ IOException -> 0x0228, all -> 0x0391 }
        L_0x004a:
            r0 = r22;
            r0 = com.payu.magicretry.analytics.MRAnalytics.this;	 Catch:{ IOException -> 0x0228, all -> 0x0391 }
            r19 = r0;	 Catch:{ IOException -> 0x0228, all -> 0x0391 }
            r19 = r19.mActivity;	 Catch:{ IOException -> 0x0228, all -> 0x0391 }
            r0 = r22;	 Catch:{ IOException -> 0x0228, all -> 0x0391 }
            r0 = com.payu.magicretry.analytics.MRAnalytics.this;	 Catch:{ IOException -> 0x0228, all -> 0x0391 }
            r20 = r0;	 Catch:{ IOException -> 0x0228, all -> 0x0391 }
            r20 = r20.fileName;	 Catch:{ IOException -> 0x0228, all -> 0x0391 }
            r21 = 0;	 Catch:{ IOException -> 0x0228, all -> 0x0391 }
            r19.openFileOutput(r20, r21);	 Catch:{ IOException -> 0x0228, all -> 0x0391 }
        L_0x0063:
            r0 = r22;	 Catch:{ IOException -> 0x0228, all -> 0x0391 }
            r0 = com.payu.magicretry.analytics.MRAnalytics.this;	 Catch:{ IOException -> 0x0228, all -> 0x0391 }
            r19 = r0;	 Catch:{ IOException -> 0x0228, all -> 0x0391 }
            r19 = r19.mActivity;	 Catch:{ IOException -> 0x0228, all -> 0x0391 }
            r0 = r22;	 Catch:{ IOException -> 0x0228, all -> 0x0391 }
            r0 = com.payu.magicretry.analytics.MRAnalytics.this;	 Catch:{ IOException -> 0x0228, all -> 0x0391 }
            r20 = r0;	 Catch:{ IOException -> 0x0228, all -> 0x0391 }
            r20 = r20.fileName;	 Catch:{ IOException -> 0x0228, all -> 0x0391 }
            r8 = r19.openFileInput(r20);	 Catch:{ IOException -> 0x0228, all -> 0x0391 }
        L_0x007b:
            r4 = r8.read();	 Catch:{ IOException -> 0x0228, all -> 0x0391 }
            r19 = -1;	 Catch:{ IOException -> 0x0228, all -> 0x0391 }
            r0 = r19;	 Catch:{ IOException -> 0x0228, all -> 0x0391 }
            if (r4 == r0) goto L_0x00a2;	 Catch:{ IOException -> 0x0228, all -> 0x0391 }
        L_0x0085:
            r19 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0228, all -> 0x0391 }
            r19.<init>();	 Catch:{ IOException -> 0x0228, all -> 0x0391 }
            r0 = r19;	 Catch:{ IOException -> 0x0228, all -> 0x0391 }
            r1 = r16;	 Catch:{ IOException -> 0x0228, all -> 0x0391 }
            r19 = r0.append(r1);	 Catch:{ IOException -> 0x0228, all -> 0x0391 }
            r0 = (char) r4;	 Catch:{ IOException -> 0x0228, all -> 0x0391 }
            r20 = r0;	 Catch:{ IOException -> 0x0228, all -> 0x0391 }
            r20 = java.lang.Character.toString(r20);	 Catch:{ IOException -> 0x0228, all -> 0x0391 }
            r19 = r19.append(r20);	 Catch:{ IOException -> 0x0228, all -> 0x0391 }
            r16 = r19.toString();	 Catch:{ IOException -> 0x0228, all -> 0x0391 }
            goto L_0x007b;	 Catch:{ IOException -> 0x0228, all -> 0x0391 }
        L_0x00a2:
            r8.close();	 Catch:{ IOException -> 0x0228, all -> 0x0391 }
            r17 = new org.json.JSONArray;	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r0 = r17;	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r1 = r16;	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r0.<init>(r1);	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r2 = new org.json.JSONArray;	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r2.<init>();	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r0 = r22;	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r0 = com.payu.magicretry.analytics.MRAnalytics.this;	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r19 = r0;	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r19 = r19.mBuffer;	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r19 = r19.size();	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            if (r19 <= 0) goto L_0x00f7;	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
        L_0x00c3:
            r9 = 0;	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
        L_0x00c4:
            r0 = r22;	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r0 = com.payu.magicretry.analytics.MRAnalytics.this;	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r19 = r0;	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r19 = r19.mBuffer;	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r19 = r19.size();	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r0 = r19;	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            if (r9 >= r0) goto L_0x00f7;	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
        L_0x00d6:
            r10 = new org.json.JSONObject;	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r0 = r22;	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r0 = com.payu.magicretry.analytics.MRAnalytics.this;	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r19 = r0;	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r19 = r19.mBuffer;	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r0 = r19;	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r19 = r0.get(r9);	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r19 = (java.lang.String) r19;	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r0 = r19;	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r10.<init>(r0);	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r0 = r17;	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r0.put(r10);	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r9 = r9 + 1;	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            goto L_0x00c4;	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
        L_0x00f7:
            r19 = r17.length();	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            if (r19 <= 0) goto L_0x0192;	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
        L_0x00fd:
            r19 = new java.lang.StringBuilder;	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r19.<init>();	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r20 = "command=sdkWsNew&var1=";	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r19 = r19.append(r20);	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r20 = r17.toString();	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r19 = r19.append(r20);	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r11 = r19.toString();	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r12 = r11.getBytes();	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r18 = new java.net.URL;	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r0 = r22;	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r0 = com.payu.magicretry.analytics.MRAnalytics.this;	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r19 = r0;	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r19 = r19.ANALYTICS_URL;	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r18.<init>(r19);	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r5 = r18.openConnection();	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r5 = (java.net.HttpURLConnection) r5;	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r19 = "POST";	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r0 = r19;	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r5.setRequestMethod(r0);	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r19 = "Content-Type";	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r20 = "application/x-www-form-urlencoded";	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r0 = r19;	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r1 = r20;	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r5.setRequestProperty(r0, r1);	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r19 = "Content-Length";	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r20 = r11.length();	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r20 = java.lang.String.valueOf(r20);	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r0 = r19;	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r1 = r20;	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r5.setRequestProperty(r0, r1);	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r19 = 1;	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r0 = r19;	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r5.setDoOutput(r0);	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r19 = r5.getOutputStream();	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r0 = r19;	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r0.write(r12);	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r13 = r5.getResponseCode();	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r14 = r5.getInputStream();	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r15 = new java.lang.StringBuffer;	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r15.<init>();	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r19 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r0 = r19;	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r3 = new byte[r0];	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
        L_0x0173:
            r9 = r14.read(r3);	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r19 = -1;	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r0 = r19;	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            if (r9 == r0) goto L_0x01b5;	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
        L_0x017d:
            r19 = new java.lang.String;	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r20 = 0;	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r0 = r19;	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r1 = r20;	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r0.<init>(r3, r1, r9);	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r0 = r19;	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            r15.append(r0);	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            goto L_0x0173;
        L_0x018e:
            r6 = move-exception;
            r6.printStackTrace();
        L_0x0192:
            r0 = r22;
            r0 = com.payu.magicretry.analytics.MRAnalytics.this;
            r19 = r0;
            r19 = r19.mBuffer;
            r19 = r19.size();
            if (r19 <= 0) goto L_0x01ab;
        L_0x01a2:
            r0 = r22;
            r0 = com.payu.magicretry.analytics.MRAnalytics.this;
            r19 = r0;
            r19.resetTimer();
        L_0x01ab:
            r0 = r22;
            r0 = com.payu.magicretry.analytics.MRAnalytics.this;
            r19 = r0;
            r19.releaseLock();
            return;
        L_0x01b5:
            r19 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
            r0 = r19;
            if (r13 != r0) goto L_0x0192;
        L_0x01bb:
            r10 = new org.json.JSONObject;	 Catch:{ Exception -> 0x0206, MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c }
            r19 = r15.toString();	 Catch:{ Exception -> 0x0206, MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c }
            r0 = r19;	 Catch:{ Exception -> 0x0206, MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c }
            r10.<init>(r0);	 Catch:{ Exception -> 0x0206, MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c }
            r19 = "status";	 Catch:{ Exception -> 0x0206, MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c }
            r0 = r19;	 Catch:{ Exception -> 0x0206, MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c }
            r19 = r10.has(r0);	 Catch:{ Exception -> 0x0206, MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c }
            if (r19 == 0) goto L_0x0192;	 Catch:{ Exception -> 0x0206, MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c }
        L_0x01d0:
            r19 = "status";	 Catch:{ Exception -> 0x0206, MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c }
            r0 = r19;	 Catch:{ Exception -> 0x0206, MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c }
            r19 = r10.getString(r0);	 Catch:{ Exception -> 0x0206, MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c }
            r20 = "1";	 Catch:{ Exception -> 0x0206, MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c }
            r19 = r19.equals(r20);	 Catch:{ Exception -> 0x0206, MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c }
            if (r19 == 0) goto L_0x0192;	 Catch:{ Exception -> 0x0206, MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c }
        L_0x01e0:
            r0 = r22;	 Catch:{ Exception -> 0x0206, MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c }
            r0 = com.payu.magicretry.analytics.MRAnalytics.this;	 Catch:{ Exception -> 0x0206, MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c }
            r19 = r0;	 Catch:{ Exception -> 0x0206, MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c }
            r19 = r19.mActivity;	 Catch:{ Exception -> 0x0206, MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c }
            r0 = r22;	 Catch:{ Exception -> 0x0206, MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c }
            r0 = com.payu.magicretry.analytics.MRAnalytics.this;	 Catch:{ Exception -> 0x0206, MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c }
            r20 = r0;	 Catch:{ Exception -> 0x0206, MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c }
            r20 = r20.fileName;	 Catch:{ Exception -> 0x0206, MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c }
            r19.deleteFile(r20);	 Catch:{ Exception -> 0x0206, MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c }
            r0 = r22;	 Catch:{ Exception -> 0x0206, MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c }
            r0 = com.payu.magicretry.analytics.MRAnalytics.this;	 Catch:{ Exception -> 0x0206, MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c }
            r19 = r0;	 Catch:{ Exception -> 0x0206, MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c }
            r20 = new java.util.ArrayList;	 Catch:{ Exception -> 0x0206, MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c }
            r20.<init>();	 Catch:{ Exception -> 0x0206, MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c }
            r19.mBuffer = r20;	 Catch:{ Exception -> 0x0206, MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c }
            goto L_0x0192;
        L_0x0206:
            r6 = move-exception;
            r6.printStackTrace();	 Catch:{ MalformedURLException -> 0x018e, ProtocolException -> 0x020b, UnsupportedEncodingException -> 0x0210, IOException -> 0x0216, JSONException -> 0x021c, Exception -> 0x0222 }
            goto L_0x0192;
        L_0x020b:
            r6 = move-exception;
            r6.printStackTrace();
            goto L_0x0192;
        L_0x0210:
            r6 = move-exception;
            r6.printStackTrace();
            goto L_0x0192;
        L_0x0216:
            r6 = move-exception;
            r6.printStackTrace();
            goto L_0x0192;
        L_0x021c:
            r6 = move-exception;
            r6.printStackTrace();
            goto L_0x0192;
        L_0x0222:
            r6 = move-exception;
            r6.printStackTrace();
            goto L_0x0192;
        L_0x0228:
            r6 = move-exception;
            r6.printStackTrace();	 Catch:{ IOException -> 0x0228, all -> 0x0391 }
            r17 = new org.json.JSONArray;	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r0 = r17;	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r1 = r16;	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r0.<init>(r1);	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r2 = new org.json.JSONArray;	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r2.<init>();	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r0 = r22;	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r0 = com.payu.magicretry.analytics.MRAnalytics.this;	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r19 = r0;	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r19 = r19.mBuffer;	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r19 = r19.size();	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            if (r19 <= 0) goto L_0x027e;	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
        L_0x024a:
            r9 = 0;	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
        L_0x024b:
            r0 = r22;	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r0 = com.payu.magicretry.analytics.MRAnalytics.this;	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r19 = r0;	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r19 = r19.mBuffer;	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r19 = r19.size();	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r0 = r19;	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            if (r9 >= r0) goto L_0x027e;	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
        L_0x025d:
            r10 = new org.json.JSONObject;	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r0 = r22;	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r0 = com.payu.magicretry.analytics.MRAnalytics.this;	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r19 = r0;	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r19 = r19.mBuffer;	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r0 = r19;	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r19 = r0.get(r9);	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r19 = (java.lang.String) r19;	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r0 = r19;	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r10.<init>(r0);	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r0 = r17;	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r0.put(r10);	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r9 = r9 + 1;	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            goto L_0x024b;	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
        L_0x027e:
            r19 = r17.length();	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            if (r19 <= 0) goto L_0x0192;	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
        L_0x0284:
            r19 = new java.lang.StringBuilder;	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r19.<init>();	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r20 = "command=sdkWsNew&var1=";	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r19 = r19.append(r20);	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r20 = r17.toString();	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r19 = r19.append(r20);	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r11 = r19.toString();	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r12 = r11.getBytes();	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r18 = new java.net.URL;	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r0 = r22;	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r0 = com.payu.magicretry.analytics.MRAnalytics.this;	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r19 = r0;	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r19 = r19.ANALYTICS_URL;	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r18.<init>(r19);	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r5 = r18.openConnection();	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r5 = (java.net.HttpURLConnection) r5;	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r19 = "POST";	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r0 = r19;	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r5.setRequestMethod(r0);	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r19 = "Content-Type";	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r20 = "application/x-www-form-urlencoded";	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r0 = r19;	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r1 = r20;	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r5.setRequestProperty(r0, r1);	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r19 = "Content-Length";	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r20 = r11.length();	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r20 = java.lang.String.valueOf(r20);	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r0 = r19;	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r1 = r20;	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r5.setRequestProperty(r0, r1);	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r19 = 1;	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r0 = r19;	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r5.setDoOutput(r0);	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r19 = r5.getOutputStream();	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r0 = r19;	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r0.write(r12);	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r13 = r5.getResponseCode();	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r14 = r5.getInputStream();	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r15 = new java.lang.StringBuffer;	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r15.<init>();	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r19 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r0 = r19;	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r3 = new byte[r0];	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
        L_0x02fa:
            r9 = r14.read(r3);	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r19 = -1;	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r0 = r19;	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            if (r9 == r0) goto L_0x031b;	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
        L_0x0304:
            r19 = new java.lang.String;	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r20 = 0;	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r0 = r19;	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r1 = r20;	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r0.<init>(r3, r1, r9);	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r0 = r19;	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            r15.append(r0);	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            goto L_0x02fa;
        L_0x0315:
            r6 = move-exception;
            r6.printStackTrace();
            goto L_0x0192;
        L_0x031b:
            r19 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
            r0 = r19;
            if (r13 != r0) goto L_0x0192;
        L_0x0321:
            r10 = new org.json.JSONObject;	 Catch:{ Exception -> 0x036d, MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385 }
            r19 = r15.toString();	 Catch:{ Exception -> 0x036d, MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385 }
            r0 = r19;	 Catch:{ Exception -> 0x036d, MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385 }
            r10.<init>(r0);	 Catch:{ Exception -> 0x036d, MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385 }
            r19 = "status";	 Catch:{ Exception -> 0x036d, MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385 }
            r0 = r19;	 Catch:{ Exception -> 0x036d, MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385 }
            r19 = r10.has(r0);	 Catch:{ Exception -> 0x036d, MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385 }
            if (r19 == 0) goto L_0x0192;	 Catch:{ Exception -> 0x036d, MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385 }
        L_0x0336:
            r19 = "status";	 Catch:{ Exception -> 0x036d, MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385 }
            r0 = r19;	 Catch:{ Exception -> 0x036d, MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385 }
            r19 = r10.getString(r0);	 Catch:{ Exception -> 0x036d, MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385 }
            r20 = "1";	 Catch:{ Exception -> 0x036d, MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385 }
            r19 = r19.equals(r20);	 Catch:{ Exception -> 0x036d, MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385 }
            if (r19 == 0) goto L_0x0192;	 Catch:{ Exception -> 0x036d, MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385 }
        L_0x0346:
            r0 = r22;	 Catch:{ Exception -> 0x036d, MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385 }
            r0 = com.payu.magicretry.analytics.MRAnalytics.this;	 Catch:{ Exception -> 0x036d, MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385 }
            r19 = r0;	 Catch:{ Exception -> 0x036d, MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385 }
            r19 = r19.mActivity;	 Catch:{ Exception -> 0x036d, MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385 }
            r0 = r22;	 Catch:{ Exception -> 0x036d, MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385 }
            r0 = com.payu.magicretry.analytics.MRAnalytics.this;	 Catch:{ Exception -> 0x036d, MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385 }
            r20 = r0;	 Catch:{ Exception -> 0x036d, MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385 }
            r20 = r20.fileName;	 Catch:{ Exception -> 0x036d, MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385 }
            r19.deleteFile(r20);	 Catch:{ Exception -> 0x036d, MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385 }
            r0 = r22;	 Catch:{ Exception -> 0x036d, MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385 }
            r0 = com.payu.magicretry.analytics.MRAnalytics.this;	 Catch:{ Exception -> 0x036d, MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385 }
            r19 = r0;	 Catch:{ Exception -> 0x036d, MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385 }
            r20 = new java.util.ArrayList;	 Catch:{ Exception -> 0x036d, MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385 }
            r20.<init>();	 Catch:{ Exception -> 0x036d, MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385 }
            r19.mBuffer = r20;	 Catch:{ Exception -> 0x036d, MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385 }
            goto L_0x0192;
        L_0x036d:
            r6 = move-exception;
            r6.printStackTrace();	 Catch:{ MalformedURLException -> 0x0315, ProtocolException -> 0x0373, UnsupportedEncodingException -> 0x0379, IOException -> 0x037f, JSONException -> 0x0385, Exception -> 0x038b }
            goto L_0x0192;
        L_0x0373:
            r6 = move-exception;
            r6.printStackTrace();
            goto L_0x0192;
        L_0x0379:
            r6 = move-exception;
            r6.printStackTrace();
            goto L_0x0192;
        L_0x037f:
            r6 = move-exception;
            r6.printStackTrace();
            goto L_0x0192;
        L_0x0385:
            r6 = move-exception;
            r6.printStackTrace();
            goto L_0x0192;
        L_0x038b:
            r6 = move-exception;
            r6.printStackTrace();
            goto L_0x0192;
        L_0x0391:
            r19 = move-exception;
            r20 = r19;
            r17 = new org.json.JSONArray;	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r0 = r17;	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r1 = r16;	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r0.<init>(r1);	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r2 = new org.json.JSONArray;	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r2.<init>();	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r0 = r22;	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r0 = com.payu.magicretry.analytics.MRAnalytics.this;	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r19 = r0;	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r19 = r19.mBuffer;	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r19 = r19.size();	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            if (r19 <= 0) goto L_0x03e6;	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
        L_0x03b2:
            r9 = 0;	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
        L_0x03b3:
            r0 = r22;	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r0 = com.payu.magicretry.analytics.MRAnalytics.this;	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r19 = r0;	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r19 = r19.mBuffer;	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r19 = r19.size();	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r0 = r19;	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            if (r9 >= r0) goto L_0x03e6;	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
        L_0x03c5:
            r10 = new org.json.JSONObject;	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r0 = r22;	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r0 = com.payu.magicretry.analytics.MRAnalytics.this;	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r19 = r0;	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r19 = r19.mBuffer;	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r0 = r19;	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r19 = r0.get(r9);	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r19 = (java.lang.String) r19;	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r0 = r19;	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r10.<init>(r0);	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r0 = r17;	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r0.put(r10);	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r9 = r9 + 1;	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            goto L_0x03b3;	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
        L_0x03e6:
            r19 = r17.length();	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            if (r19 <= 0) goto L_0x0489;	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
        L_0x03ec:
            r19 = new java.lang.StringBuilder;	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r19.<init>();	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r21 = "command=sdkWsNew&var1=";	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r0 = r19;	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r1 = r21;	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r19 = r0.append(r1);	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r21 = r17.toString();	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r0 = r19;	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r1 = r21;	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r19 = r0.append(r1);	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r11 = r19.toString();	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r12 = r11.getBytes();	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r18 = new java.net.URL;	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r0 = r22;	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r0 = com.payu.magicretry.analytics.MRAnalytics.this;	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r19 = r0;	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r19 = r19.ANALYTICS_URL;	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r18.<init>(r19);	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r5 = r18.openConnection();	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r5 = (java.net.HttpURLConnection) r5;	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r19 = "POST";	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r0 = r19;	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r5.setRequestMethod(r0);	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r19 = "Content-Type";	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r21 = "application/x-www-form-urlencoded";	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r0 = r19;	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r1 = r21;	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r5.setRequestProperty(r0, r1);	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r19 = "Content-Length";	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r21 = r11.length();	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r21 = java.lang.String.valueOf(r21);	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r0 = r19;	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r1 = r21;	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r5.setRequestProperty(r0, r1);	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r19 = 1;	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r0 = r19;	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r5.setDoOutput(r0);	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r19 = r5.getOutputStream();	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r0 = r19;	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r0.write(r12);	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r13 = r5.getResponseCode();	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r14 = r5.getInputStream();	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r15 = new java.lang.StringBuffer;	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r15.<init>();	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r19 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r0 = r19;	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r3 = new byte[r0];	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
        L_0x046a:
            r9 = r14.read(r3);	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r19 = -1;	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r0 = r19;	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            if (r9 == r0) goto L_0x048a;	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
        L_0x0474:
            r19 = new java.lang.String;	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r21 = 0;	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r0 = r19;	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r1 = r21;	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r0.<init>(r3, r1, r9);	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r0 = r19;	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            r15.append(r0);	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            goto L_0x046a;
        L_0x0485:
            r6 = move-exception;
            r6.printStackTrace();
        L_0x0489:
            throw r20;
        L_0x048a:
            r19 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
            r0 = r19;
            if (r13 != r0) goto L_0x0489;
        L_0x0490:
            r10 = new org.json.JSONObject;	 Catch:{ Exception -> 0x04e7, MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb }
            r19 = r15.toString();	 Catch:{ Exception -> 0x04e7, MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb }
            r0 = r19;	 Catch:{ Exception -> 0x04e7, MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb }
            r10.<init>(r0);	 Catch:{ Exception -> 0x04e7, MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb }
            r19 = "status";	 Catch:{ Exception -> 0x04e7, MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb }
            r0 = r19;	 Catch:{ Exception -> 0x04e7, MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb }
            r19 = r10.has(r0);	 Catch:{ Exception -> 0x04e7, MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb }
            if (r19 == 0) goto L_0x0489;	 Catch:{ Exception -> 0x04e7, MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb }
        L_0x04a5:
            r19 = "status";	 Catch:{ Exception -> 0x04e7, MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb }
            r0 = r19;	 Catch:{ Exception -> 0x04e7, MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb }
            r19 = r10.getString(r0);	 Catch:{ Exception -> 0x04e7, MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb }
            r21 = "1";	 Catch:{ Exception -> 0x04e7, MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb }
            r0 = r19;	 Catch:{ Exception -> 0x04e7, MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb }
            r1 = r21;	 Catch:{ Exception -> 0x04e7, MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb }
            r19 = r0.equals(r1);	 Catch:{ Exception -> 0x04e7, MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb }
            if (r19 == 0) goto L_0x0489;	 Catch:{ Exception -> 0x04e7, MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb }
        L_0x04b9:
            r0 = r22;	 Catch:{ Exception -> 0x04e7, MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb }
            r0 = com.payu.magicretry.analytics.MRAnalytics.this;	 Catch:{ Exception -> 0x04e7, MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb }
            r19 = r0;	 Catch:{ Exception -> 0x04e7, MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb }
            r19 = r19.mActivity;	 Catch:{ Exception -> 0x04e7, MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb }
            r0 = r22;	 Catch:{ Exception -> 0x04e7, MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb }
            r0 = com.payu.magicretry.analytics.MRAnalytics.this;	 Catch:{ Exception -> 0x04e7, MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb }
            r21 = r0;	 Catch:{ Exception -> 0x04e7, MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb }
            r21 = r21.fileName;	 Catch:{ Exception -> 0x04e7, MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb }
            r0 = r19;	 Catch:{ Exception -> 0x04e7, MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb }
            r1 = r21;	 Catch:{ Exception -> 0x04e7, MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb }
            r0.deleteFile(r1);	 Catch:{ Exception -> 0x04e7, MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb }
            r0 = r22;	 Catch:{ Exception -> 0x04e7, MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb }
            r0 = com.payu.magicretry.analytics.MRAnalytics.this;	 Catch:{ Exception -> 0x04e7, MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb }
            r19 = r0;	 Catch:{ Exception -> 0x04e7, MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb }
            r21 = new java.util.ArrayList;	 Catch:{ Exception -> 0x04e7, MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb }
            r21.<init>();	 Catch:{ Exception -> 0x04e7, MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb }
            r0 = r19;	 Catch:{ Exception -> 0x04e7, MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb }
            r1 = r21;	 Catch:{ Exception -> 0x04e7, MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb }
            r0.mBuffer = r1;	 Catch:{ Exception -> 0x04e7, MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb }
            goto L_0x0489;
        L_0x04e7:
            r6 = move-exception;
            r6.printStackTrace();	 Catch:{ MalformedURLException -> 0x0485, ProtocolException -> 0x04ec, UnsupportedEncodingException -> 0x04f1, IOException -> 0x04f6, JSONException -> 0x04fb, Exception -> 0x0500 }
            goto L_0x0489;
        L_0x04ec:
            r6 = move-exception;
            r6.printStackTrace();
            goto L_0x0489;
        L_0x04f1:
            r6 = move-exception;
            r6.printStackTrace();
            goto L_0x0489;
        L_0x04f6:
            r6 = move-exception;
            r6.printStackTrace();
            goto L_0x0489;
        L_0x04fb:
            r6 = move-exception;
            r6.printStackTrace();
            goto L_0x0489;
        L_0x0500:
            r6 = move-exception;
            r6.printStackTrace();
            goto L_0x0489;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.payu.magicretry.analytics.MRAnalytics.3.run():void");
        }

        C05443() {
        }
    }

    public MRAnalytics(Activity activity, String filename) {
        this.mActivity = activity;
        this.fileName = filename;
        this.mBuffer = new ArrayList();
        final UncaughtExceptionHandler defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            public void uncaughtException(Thread thread, Throwable ex) {
                do {
                } while (MRAnalytics.this.mIsLocked);
                MRAnalytics.this.setLock();
                try {
                    FileOutputStream fileOutputStream = MRAnalytics.this.mActivity.openFileOutput(MRAnalytics.this.fileName, 0);
                    int c = MRAnalytics.this.mBuffer.size();
                    if (c > 0) {
                        JSONArray jsonArray = new JSONArray();
                        for (int i = 0; i < c; i++) {
                            jsonArray.put(jsonArray.length(), new JSONObject((String) MRAnalytics.this.mBuffer.get(i)));
                        }
                        fileOutputStream.write(jsonArray.toString().getBytes());
                        MRAnalytics.this.mBuffer = new ArrayList();
                    }
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                MRAnalytics.this.releaseLock();
                defaultUEH.uncaughtException(thread, ex);
            }
        });
    }

    public static synchronized MRAnalytics getInstance(Activity activity, String fileName) {
        MRAnalytics mRAnalytics;
        synchronized (MRAnalytics.class) {
            if (INSTANCE == null) {
                INSTANCE = new MRAnalytics(activity, fileName);
            }
            mRAnalytics = INSTANCE;
        }
        return mRAnalytics;
    }

    public void log(final String msg) {
        resetTimer();
        if (this.mIsLocked) {
            try {
                this.mBuffer.add(msg);
                return;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        new AsyncTask<Void, Void, Void>() {
            protected Void doInBackground(Void... voids) {
                MRAnalytics.this.setLock();
                try {
                    JSONArray jsonArray;
                    JSONObject newobject = new JSONObject(msg);
                    String temp = "";
                    if (!new File(MRAnalytics.this.mActivity.getFilesDir(), MRAnalytics.this.fileName).exists()) {
                        MRAnalytics.this.mActivity.openFileOutput(MRAnalytics.this.fileName, 0);
                    }
                    FileInputStream fileInputStream = MRAnalytics.this.mActivity.openFileInput(MRAnalytics.this.fileName);
                    while (true) {
                        int c = fileInputStream.read();
                        if (c == -1) {
                            break;
                        }
                        temp = temp + Character.toString((char) c);
                    }
                    if (temp.equalsIgnoreCase("")) {
                        jsonArray = new JSONArray();
                    } else {
                        jsonArray = new JSONArray(temp);
                    }
                    fileInputStream.close();
                    FileOutputStream fileOutputStream = MRAnalytics.this.mActivity.openFileOutput(MRAnalytics.this.fileName, 0);
                    jsonArray.put(jsonArray.length(), newobject);
                    fileOutputStream.write(jsonArray.toString().getBytes());
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    MRAnalytics.this.mBuffer.add(msg);
                } catch (JSONException e2) {
                    e2.printStackTrace();
                    MRAnalytics.this.mBuffer.add(msg);
                } catch (Exception e3) {
                    e3.printStackTrace();
                    MRAnalytics.this.mBuffer.add(msg);
                }
                MRAnalytics.this.releaseLock();
                return null;
            }
        }.execute(new Void[]{null, null, null});
    }

    private void resetTimer() {
        if (this.mTimer != null) {
            this.mTimer.cancel();
        }
        this.mTimer = new Timer();
        this.mTimer.schedule(new C05443(), TIMER_DELAY);
    }

    private synchronized void setLock() {
        this.mIsLocked = IS_PRODUCTION;
    }

    private synchronized void releaseLock() {
        this.mIsLocked = false;
    }

    private boolean isOnline() {
        NetworkInfo netInfo = ((ConnectivityManager) this.mActivity.getSystemService("connectivity")).getActiveNetworkInfo();
        return (netInfo == null || !netInfo.isConnectedOrConnecting()) ? false : IS_PRODUCTION;
    }
}
