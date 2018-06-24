package com.payu.custombrowser.analytics;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import com.payu.custombrowser.util.CBUtil;
import com.sinch.android.rtc.internal.InternalErrorCodes;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.HttpURLConnection;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONArray;
import org.json.JSONObject;

public class CBAnalytics {
    public static final String ANALYTICS_URL = "https://info.payu.in/merchant/MobileAnalytics";
    private static CBAnalytics INSTANCE = null;
    private static final String PRODUCTION_URL = "https://info.payu.in/merchant/MobileAnalytics";
    private static final String TEST_URL = "http://10.50.23.170:6543/MobileAnalytics";
    private static final long TIMER_DELAY = 5000;
    private String ANALYTICS_BUFFER_KEY = "analytics_buffer_key";
    private CBUtil cbUtil;
    private String fileName;
    private volatile boolean mBufferLock;
    private Timer mTimer;
    private volatile boolean mainFileLocked = false;
    private final Context mcontext;

    class C05214 extends TimerTask {
        C05214() {
        }

        public void run() {
            do {
            } while (CBAnalytics.this.mainFileLocked);
            CBAnalytics.this.setLock();
            if (CBAnalytics.this.isOnline()) {
                String temp = "";
                JSONArray tempJsonArray;
                HttpURLConnection conn;
                StringBuffer responseStringBuffer;
                try {
                    temp = CBAnalytics.this.cbUtil.readFileInputStream(CBAnalytics.this.mcontext, CBAnalytics.this.fileName, 0);
                    if (temp != null) {
                        try {
                            if (!temp.equalsIgnoreCase("")) {
                                tempJsonArray = new JSONArray(temp);
                                if (CBAnalytics.this.cbUtil.getStringSharedPreference(CBAnalytics.this.mcontext, CBAnalytics.this.ANALYTICS_BUFFER_KEY).length() > 1) {
                                    CBAnalytics.this.mBufferLock = true;
                                    tempJsonArray = CBAnalytics.this.copyBufferToFile(tempJsonArray, new JSONArray(CBAnalytics.this.cbUtil.getStringSharedPreference(CBAnalytics.this.mcontext, CBAnalytics.this.ANALYTICS_BUFFER_KEY)));
                                }
                                if (tempJsonArray.length() > 0) {
                                    conn = CBAnalytics.this.cbUtil.getHttpsConn("https://info.payu.in/merchant/MobileAnalytics", "command=EventAnalytics&data=" + tempJsonArray.toString(), InternalErrorCodes.CapabilityUserNotFound);
                                    if (!(conn == null || conn.getResponseCode() != Callback.DEFAULT_DRAG_ANIMATION_DURATION || conn.getInputStream() == null)) {
                                        responseStringBuffer = CBUtil.getStringBufferFromInputStream(conn.getInputStream());
                                        if (responseStringBuffer != null && new JSONObject(responseStringBuffer.toString()).has("status")) {
                                            CBAnalytics.this.mcontext.deleteFile(CBAnalytics.this.fileName);
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    tempJsonArray = new JSONArray();
                    if (CBAnalytics.this.cbUtil.getStringSharedPreference(CBAnalytics.this.mcontext, CBAnalytics.this.ANALYTICS_BUFFER_KEY).length() > 1) {
                        CBAnalytics.this.mBufferLock = true;
                        tempJsonArray = CBAnalytics.this.copyBufferToFile(tempJsonArray, new JSONArray(CBAnalytics.this.cbUtil.getStringSharedPreference(CBAnalytics.this.mcontext, CBAnalytics.this.ANALYTICS_BUFFER_KEY)));
                    }
                    if (tempJsonArray.length() > 0) {
                        conn = CBAnalytics.this.cbUtil.getHttpsConn("https://info.payu.in/merchant/MobileAnalytics", "command=EventAnalytics&data=" + tempJsonArray.toString(), InternalErrorCodes.CapabilityUserNotFound);
                        responseStringBuffer = CBUtil.getStringBufferFromInputStream(conn.getInputStream());
                        CBAnalytics.this.mcontext.deleteFile(CBAnalytics.this.fileName);
                    }
                } catch (Throwable th) {
                    if (temp != null) {
                        try {
                            if (!temp.equalsIgnoreCase("")) {
                                tempJsonArray = new JSONArray(temp);
                                if (CBAnalytics.this.cbUtil.getStringSharedPreference(CBAnalytics.this.mcontext, CBAnalytics.this.ANALYTICS_BUFFER_KEY).length() > 1) {
                                    CBAnalytics.this.mBufferLock = true;
                                    tempJsonArray = CBAnalytics.this.copyBufferToFile(tempJsonArray, new JSONArray(CBAnalytics.this.cbUtil.getStringSharedPreference(CBAnalytics.this.mcontext, CBAnalytics.this.ANALYTICS_BUFFER_KEY)));
                                }
                                if (tempJsonArray.length() > 0) {
                                    conn = CBAnalytics.this.cbUtil.getHttpsConn("https://info.payu.in/merchant/MobileAnalytics", "command=EventAnalytics&data=" + tempJsonArray.toString(), InternalErrorCodes.CapabilityUserNotFound);
                                    if (!(conn == null || conn.getResponseCode() != Callback.DEFAULT_DRAG_ANIMATION_DURATION || conn.getInputStream() == null)) {
                                        responseStringBuffer = CBUtil.getStringBufferFromInputStream(conn.getInputStream());
                                        if (responseStringBuffer != null && new JSONObject(responseStringBuffer.toString()).has("status")) {
                                            CBAnalytics.this.mcontext.deleteFile(CBAnalytics.this.fileName);
                                        }
                                    }
                                }
                            }
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                    tempJsonArray = new JSONArray();
                    if (CBAnalytics.this.cbUtil.getStringSharedPreference(CBAnalytics.this.mcontext, CBAnalytics.this.ANALYTICS_BUFFER_KEY).length() > 1) {
                        CBAnalytics.this.mBufferLock = true;
                        tempJsonArray = CBAnalytics.this.copyBufferToFile(tempJsonArray, new JSONArray(CBAnalytics.this.cbUtil.getStringSharedPreference(CBAnalytics.this.mcontext, CBAnalytics.this.ANALYTICS_BUFFER_KEY)));
                    }
                    if (tempJsonArray.length() > 0) {
                        conn = CBAnalytics.this.cbUtil.getHttpsConn("https://info.payu.in/merchant/MobileAnalytics", "command=EventAnalytics&data=" + tempJsonArray.toString(), InternalErrorCodes.CapabilityUserNotFound);
                        responseStringBuffer = CBUtil.getStringBufferFromInputStream(conn.getInputStream());
                        CBAnalytics.this.mcontext.deleteFile(CBAnalytics.this.fileName);
                    }
                }
            }
            CBAnalytics.this.releaseLock();
            if (CBAnalytics.this.cbUtil.getStringSharedPreference(CBAnalytics.this.mcontext, CBAnalytics.this.ANALYTICS_BUFFER_KEY).length() > 1) {
                CBAnalytics.this.resetTimer();
            }
        }
    }

    private CBAnalytics(final Context context, String filename) {
        this.mcontext = context;
        this.fileName = filename;
        this.cbUtil = new CBUtil();
        final UncaughtExceptionHandler defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            public void uncaughtException(Thread thread, Throwable ex) {
                do {
                } while (CBAnalytics.this.mainFileLocked);
                CBAnalytics.this.setLock();
                try {
                    FileOutputStream fileOutputStream = CBAnalytics.this.mcontext.openFileOutput(CBAnalytics.this.fileName, 0);
                    if (CBAnalytics.this.cbUtil.getStringSharedPreference(CBAnalytics.this.mcontext, CBAnalytics.this.ANALYTICS_BUFFER_KEY).length() > 0) {
                        JSONArray jsonArray = new JSONArray();
                        JSONArray bufferJsonArray = new JSONArray(CBAnalytics.this.cbUtil.getStringSharedPreference(CBAnalytics.this.mcontext, CBAnalytics.this.ANALYTICS_BUFFER_KEY).toString());
                        for (int i = 0; i < bufferJsonArray.length(); i++) {
                            jsonArray.put(jsonArray.length(), bufferJsonArray.getJSONObject(i));
                        }
                        fileOutputStream.write(jsonArray.toString().getBytes());
                        CBAnalytics.this.cbUtil.deleteSharedPrefKey(context, CBAnalytics.this.ANALYTICS_BUFFER_KEY);
                    }
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                CBAnalytics.this.releaseLock();
                defaultUEH.uncaughtException(thread, ex);
            }
        });
    }

    public static CBAnalytics getInstance(Context context, String fileName) {
        if (INSTANCE == null) {
            synchronized (CBAnalytics.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CBAnalytics(context, fileName);
                }
            }
        }
        return INSTANCE;
    }

    public void log(final String msg) {
        if (isOnline()) {
            resetTimer();
        }
        if (this.mainFileLocked) {
            new Thread(new Runnable() {
                public void run() {
                    JSONArray jsonArray;
                    do {
                        try {
                        } catch (Exception e) {
                            e.printStackTrace();
                            return;
                        }
                    } while (CBAnalytics.this.mBufferLock);
                    String str = CBAnalytics.this.cbUtil.getStringSharedPreference(CBAnalytics.this.mcontext, CBAnalytics.this.ANALYTICS_BUFFER_KEY);
                    if (str == null || str.equalsIgnoreCase("")) {
                        jsonArray = new JSONArray();
                    } else {
                        jsonArray = new JSONArray(str);
                    }
                    jsonArray.put(new JSONObject(msg));
                    CBAnalytics.this.cbUtil.setStringSharedPreference(CBAnalytics.this.mcontext, CBAnalytics.this.ANALYTICS_BUFFER_KEY, jsonArray.toString());
                }
            }).start();
        } else {
            new Thread(new Runnable() {
                public void run() {
                    /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxOverflowException: Regions stack size limit reached
	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:37)
	at jadx.core.utils.ErrorsCounter.methodError(ErrorsCounter.java:61)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                    /*
                    r9 = this;
                L_0x0000:
                    r5 = com.payu.custombrowser.analytics.CBAnalytics.this;
                    r5 = r5.mainFileLocked;
                    if (r5 != 0) goto L_0x0000;
                L_0x0008:
                    r5 = com.payu.custombrowser.analytics.CBAnalytics.this;
                    r5.setLock();
                    r3 = new org.json.JSONObject;	 Catch:{ IOException -> 0x006c, JSONException -> 0x0076, Exception -> 0x0080 }
                    r5 = r3;	 Catch:{ IOException -> 0x006c, JSONException -> 0x0076, Exception -> 0x0080 }
                    r3.<init>(r5);	 Catch:{ IOException -> 0x006c, JSONException -> 0x0076, Exception -> 0x0080 }
                    r5 = com.payu.custombrowser.analytics.CBAnalytics.this;	 Catch:{ IOException -> 0x006c, JSONException -> 0x0076, Exception -> 0x0080 }
                    r5 = r5.cbUtil;	 Catch:{ IOException -> 0x006c, JSONException -> 0x0076, Exception -> 0x0080 }
                    r6 = com.payu.custombrowser.analytics.CBAnalytics.this;	 Catch:{ IOException -> 0x006c, JSONException -> 0x0076, Exception -> 0x0080 }
                    r6 = r6.mcontext;	 Catch:{ IOException -> 0x006c, JSONException -> 0x0076, Exception -> 0x0080 }
                    r7 = com.payu.custombrowser.analytics.CBAnalytics.this;	 Catch:{ IOException -> 0x006c, JSONException -> 0x0076, Exception -> 0x0080 }
                    r7 = r7.fileName;	 Catch:{ IOException -> 0x006c, JSONException -> 0x0076, Exception -> 0x0080 }
                    r8 = 0;	 Catch:{ IOException -> 0x006c, JSONException -> 0x0076, Exception -> 0x0080 }
                    r4 = r5.readFileInputStream(r6, r7, r8);	 Catch:{ IOException -> 0x006c, JSONException -> 0x0076, Exception -> 0x0080 }
                    if (r4 == 0) goto L_0x0035;	 Catch:{ IOException -> 0x006c, JSONException -> 0x0076, Exception -> 0x0080 }
                L_0x002d:
                    r5 = "";	 Catch:{ IOException -> 0x006c, JSONException -> 0x0076, Exception -> 0x0080 }
                    r5 = r4.equalsIgnoreCase(r5);	 Catch:{ IOException -> 0x006c, JSONException -> 0x0076, Exception -> 0x0080 }
                    if (r5 == 0) goto L_0x0066;	 Catch:{ IOException -> 0x006c, JSONException -> 0x0076, Exception -> 0x0080 }
                L_0x0035:
                    r2 = new org.json.JSONArray;	 Catch:{ IOException -> 0x006c, JSONException -> 0x0076, Exception -> 0x0080 }
                    r2.<init>();	 Catch:{ IOException -> 0x006c, JSONException -> 0x0076, Exception -> 0x0080 }
                L_0x003a:
                    r5 = com.payu.custombrowser.analytics.CBAnalytics.this;	 Catch:{ IOException -> 0x006c, JSONException -> 0x0076, Exception -> 0x0080 }
                    r5 = r5.mcontext;	 Catch:{ IOException -> 0x006c, JSONException -> 0x0076, Exception -> 0x0080 }
                    r6 = com.payu.custombrowser.analytics.CBAnalytics.this;	 Catch:{ IOException -> 0x006c, JSONException -> 0x0076, Exception -> 0x0080 }
                    r6 = r6.fileName;	 Catch:{ IOException -> 0x006c, JSONException -> 0x0076, Exception -> 0x0080 }
                    r7 = 0;	 Catch:{ IOException -> 0x006c, JSONException -> 0x0076, Exception -> 0x0080 }
                    r1 = r5.openFileOutput(r6, r7);	 Catch:{ IOException -> 0x006c, JSONException -> 0x0076, Exception -> 0x0080 }
                    r5 = r2.length();	 Catch:{ IOException -> 0x006c, JSONException -> 0x0076, Exception -> 0x0080 }
                    r2.put(r5, r3);	 Catch:{ IOException -> 0x006c, JSONException -> 0x0076, Exception -> 0x0080 }
                    r5 = r2.toString();	 Catch:{ IOException -> 0x006c, JSONException -> 0x0076, Exception -> 0x0080 }
                    r5 = r5.getBytes();	 Catch:{ IOException -> 0x006c, JSONException -> 0x0076, Exception -> 0x0080 }
                    r1.write(r5);	 Catch:{ IOException -> 0x006c, JSONException -> 0x0076, Exception -> 0x0080 }
                    r1.close();	 Catch:{ IOException -> 0x006c, JSONException -> 0x0076, Exception -> 0x0080 }
                    r5 = com.payu.custombrowser.analytics.CBAnalytics.this;
                    r5.releaseLock();
                L_0x0065:
                    return;
                L_0x0066:
                    r2 = new org.json.JSONArray;	 Catch:{ IOException -> 0x006c, JSONException -> 0x0076, Exception -> 0x0080 }
                    r2.<init>(r4);	 Catch:{ IOException -> 0x006c, JSONException -> 0x0076, Exception -> 0x0080 }
                    goto L_0x003a;
                L_0x006c:
                    r0 = move-exception;
                    r0.printStackTrace();	 Catch:{ all -> 0x008a }
                    r5 = com.payu.custombrowser.analytics.CBAnalytics.this;
                    r5.releaseLock();
                    goto L_0x0065;
                L_0x0076:
                    r0 = move-exception;
                    r0.printStackTrace();	 Catch:{ all -> 0x008a }
                    r5 = com.payu.custombrowser.analytics.CBAnalytics.this;
                    r5.releaseLock();
                    goto L_0x0065;
                L_0x0080:
                    r0 = move-exception;
                    r0.printStackTrace();	 Catch:{ all -> 0x008a }
                    r5 = com.payu.custombrowser.analytics.CBAnalytics.this;
                    r5.releaseLock();
                    goto L_0x0065;
                L_0x008a:
                    r5 = move-exception;
                    r6 = com.payu.custombrowser.analytics.CBAnalytics.this;
                    r6.releaseLock();
                    throw r5;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.payu.custombrowser.analytics.CBAnalytics.3.run():void");
                }
            }).start();
        }
    }

    private void resetTimer() {
        if (this.mTimer != null) {
            this.mTimer.cancel();
        }
        this.mTimer = new Timer();
        this.mTimer.schedule(new C05214(), TIMER_DELAY);
    }

    private synchronized void setLock() {
        do {
        } while (this.mainFileLocked);
        this.mainFileLocked = true;
    }

    private void releaseLock() {
        this.mainFileLocked = false;
    }

    private boolean isOnline() {
        NetworkInfo netInfo = ((ConnectivityManager) this.mcontext.getSystemService("connectivity")).getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public Timer getmTimer() {
        return this.mTimer;
    }

    private JSONArray copyBufferToFile(JSONArray fileJsonArray, JSONArray bufferJsonArray) {
        FileOutputStream fileOutputStream = null;
        try {
            JSONArray mergeJsonArray = new JSONArray(fileJsonArray.toString());
            for (int i = 0; i < bufferJsonArray.length(); i++) {
                mergeJsonArray.put(bufferJsonArray.getJSONObject(i));
            }
            fileOutputStream = this.mcontext.openFileOutput(this.fileName, 0);
            fileOutputStream.write(mergeJsonArray.toString().getBytes());
            this.cbUtil.deleteSharedPrefKey(this.mcontext, this.ANALYTICS_BUFFER_KEY);
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            this.mBufferLock = false;
            return mergeJsonArray;
        } catch (Exception e2) {
            e2.printStackTrace();
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
            this.mBufferLock = false;
            return fileJsonArray;
        } catch (Throwable th) {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e32) {
                    e32.printStackTrace();
                }
            }
            this.mBufferLock = false;
        }
    }
}
