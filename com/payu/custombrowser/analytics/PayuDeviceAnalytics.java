package com.payu.custombrowser.analytics;

import android.content.Context;
import android.os.AsyncTask;
import com.payu.custombrowser.util.CBUtil;
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

public class PayuDeviceAnalytics {
    private static final String PRODUCTION_URL_DEVICE_ANALYTICS = "https://info.payu.in/merchant/MobileAnalytics";
    private static final String TEST_URL_DEVICE_ANALYTICS = "http://10.50.23.170:6543/MobileAnalytics";
    private long TIMER_DELAY = 0;
    private CBUtil cbUtil;
    private final Context context;
    private String file_name = "cb_local_cache_device";
    private boolean isTimerCancelled;
    private ArrayList<String> mBuffer;
    private boolean mIsLocked = false;
    private Timer mTimer;

    class C05232 extends TimerTask {
        C05232() {
        }

        public void run() {
            do {
            } while (PayuDeviceAnalytics.this.mIsLocked);
            PayuDeviceAnalytics.this.TIMER_DELAY = 5000;
            PayuDeviceAnalytics.this.setLock();
            String temp = "";
            int c;
            try {
                if (!new File(PayuDeviceAnalytics.this.context.getFilesDir(), PayuDeviceAnalytics.this.file_name).exists()) {
                    PayuDeviceAnalytics.this.context.openFileOutput(PayuDeviceAnalytics.this.file_name, 0);
                }
                FileInputStream fileInputStream = PayuDeviceAnalytics.this.context.openFileInput(PayuDeviceAnalytics.this.file_name);
                while (true) {
                    c = fileInputStream.read();
                    if (c == -1) {
                        break;
                    }
                    temp = temp + Character.toString((char) c);
                }
                fileInputStream.close();
                c = PayuDeviceAnalytics.this.mBuffer.size();
                while (c > 0) {
                    c--;
                    temp = temp + ((String) PayuDeviceAnalytics.this.mBuffer.get(c)) + "\r\n";
                    if (c >= 0 && PayuDeviceAnalytics.this.mBuffer.size() > c) {
                        PayuDeviceAnalytics.this.mBuffer.remove(c);
                    }
                }
                temp = temp.trim();
                if (temp.length() > 0) {
                    new UploadData(temp).execute(new String[]{temp});
                } else {
                    PayuDeviceAnalytics.this.mTimer.cancel();
                }
            } catch (IOException e) {
                e.printStackTrace();
                c = PayuDeviceAnalytics.this.mBuffer.size();
                while (c > 0) {
                    c--;
                    temp = temp + ((String) PayuDeviceAnalytics.this.mBuffer.get(c)) + "\r\n";
                    if (c >= 0 && PayuDeviceAnalytics.this.mBuffer.size() > c) {
                        PayuDeviceAnalytics.this.mBuffer.remove(c);
                    }
                }
                temp = temp.trim();
                if (temp.length() > 0) {
                    new UploadData(temp).execute(new String[]{temp});
                } else {
                    PayuDeviceAnalytics.this.mTimer.cancel();
                }
            } catch (Throwable th) {
                Throwable th2 = th;
                c = PayuDeviceAnalytics.this.mBuffer.size();
                while (c > 0) {
                    c--;
                    temp = temp + ((String) PayuDeviceAnalytics.this.mBuffer.get(c)) + "\r\n";
                    if (c >= 0 && PayuDeviceAnalytics.this.mBuffer.size() > c) {
                        PayuDeviceAnalytics.this.mBuffer.remove(c);
                    }
                }
                temp = temp.trim();
                if (temp.length() > 0) {
                    new UploadData(temp).execute(new String[]{temp});
                } else {
                    PayuDeviceAnalytics.this.mTimer.cancel();
                }
            }
            if (PayuDeviceAnalytics.this.mBuffer.size() > 0) {
                PayuDeviceAnalytics.this.resetTimer();
            }
            PayuDeviceAnalytics.this.releaseLock();
        }
    }

    public class UploadData extends AsyncTask<String, Void, String> {
        private String temp;

        UploadData(String temp) {
            this.temp = temp;
        }

        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        protected java.lang.String doInBackground(java.lang.String... r16) {
            /*
            r15 = this;
            r11 = com.payu.custombrowser.analytics.PayuDeviceAnalytics.this;	 Catch:{ MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r11 = r11.context;	 Catch:{ MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            if (r11 == 0) goto L_0x0117;
        L_0x0008:
            r11 = com.payu.custombrowser.analytics.PayuDeviceAnalytics.this;	 Catch:{ MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r11 = r11.isTimerCancelled;	 Catch:{ MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            if (r11 != 0) goto L_0x0117;
        L_0x0010:
            r5 = new org.json.JSONArray;	 Catch:{ MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r11 = 0;
            r11 = r16[r11];	 Catch:{ MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r5.<init>(r11);	 Catch:{ MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r3 = r5;
            r4 = 0;
        L_0x001a:
            r11 = r3.length();	 Catch:{ MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            if (r4 >= r11) goto L_0x006a;
        L_0x0020:
            r11 = com.payu.custombrowser.analytics.PayuDeviceAnalytics.this;	 Catch:{ MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r12 = r11.cbUtil;	 Catch:{ MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r13 = new java.lang.StringBuilder;	 Catch:{ MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r13.<init>();	 Catch:{ MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r11 = r3.get(r4);	 Catch:{ MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r11 = (org.json.JSONObject) r11;	 Catch:{ MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r14 = "merchant_key";
            r11 = r11.getString(r14);	 Catch:{ MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r11 = r13.append(r11);	 Catch:{ MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r13 = "|";
            r13 = r11.append(r13);	 Catch:{ MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r11 = r3.get(r4);	 Catch:{ MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r11 = (org.json.JSONObject) r11;	 Catch:{ MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r14 = "txnid";
            r11 = r11.getString(r14);	 Catch:{ MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r11 = r13.append(r11);	 Catch:{ MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r11 = r11.toString();	 Catch:{ MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r13 = com.payu.custombrowser.analytics.PayuDeviceAnalytics.this;	 Catch:{ MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r13 = r13.context;	 Catch:{ MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r11 = r12.getBooleanSharedPreference(r11, r13);	 Catch:{ MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            if (r11 == 0) goto L_0x0067;
        L_0x0061:
            r11 = com.payu.custombrowser.analytics.PayuDeviceAnalytics.this;	 Catch:{ MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r5 = r11.removeJsonObjectAtJsonArrayIndex(r3, r4);	 Catch:{ MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
        L_0x0067:
            r4 = r4 + 1;
            goto L_0x001a;
        L_0x006a:
            r11 = r5.length();	 Catch:{ MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            if (r11 <= 0) goto L_0x0117;
        L_0x0070:
            r11 = new java.lang.StringBuilder;	 Catch:{ MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r11.<init>();	 Catch:{ MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r12 = "command=DeviceAnalytics&data=";
            r11 = r11.append(r12);	 Catch:{ MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r12 = r5.toString();	 Catch:{ MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r11 = r11.append(r12);	 Catch:{ MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r7 = r11.toString();	 Catch:{ MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r0 = "https://info.payu.in/merchant/MobileAnalytics";
            r11 = com.payu.custombrowser.analytics.PayuDeviceAnalytics.this;	 Catch:{ MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r11 = r11.cbUtil;	 Catch:{ MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r1 = r11.getHttpsConn(r0, r7);	 Catch:{ MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            if (r1 == 0) goto L_0x0138;
        L_0x0095:
            r8 = r1.getResponseCode();	 Catch:{ MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r11 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
            if (r8 != r11) goto L_0x012d;
        L_0x009d:
            r9 = r1.getInputStream();	 Catch:{ Exception -> 0x0119, MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r10 = com.payu.custombrowser.util.CBUtil.getStringBufferFromInputStream(r9);	 Catch:{ Exception -> 0x0119, MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            if (r10 == 0) goto L_0x0117;
        L_0x00a7:
            r6 = new org.json.JSONObject;	 Catch:{ Exception -> 0x0119, MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r11 = r10.toString();	 Catch:{ Exception -> 0x0119, MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r6.<init>(r11);	 Catch:{ Exception -> 0x0119, MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r11 = "status";
            r11 = r6.has(r11);	 Catch:{ Exception -> 0x0119, MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            if (r11 == 0) goto L_0x0110;
        L_0x00b8:
            r11 = com.payu.custombrowser.analytics.PayuDeviceAnalytics.this;	 Catch:{ Exception -> 0x0119, MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r11 = r11.context;	 Catch:{ Exception -> 0x0119, MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r12 = com.payu.custombrowser.analytics.PayuDeviceAnalytics.this;	 Catch:{ Exception -> 0x0119, MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r12 = r12.file_name;	 Catch:{ Exception -> 0x0119, MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r11.deleteFile(r12);	 Catch:{ Exception -> 0x0119, MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r4 = 0;
        L_0x00c8:
            r11 = r5.length();	 Catch:{ Exception -> 0x0119, MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            if (r4 >= r11) goto L_0x0117;
        L_0x00ce:
            r11 = com.payu.custombrowser.analytics.PayuDeviceAnalytics.this;	 Catch:{ Exception -> 0x0119, MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r12 = r11.cbUtil;	 Catch:{ Exception -> 0x0119, MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r13 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0119, MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r13.<init>();	 Catch:{ Exception -> 0x0119, MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r11 = r5.get(r4);	 Catch:{ Exception -> 0x0119, MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r11 = (org.json.JSONObject) r11;	 Catch:{ Exception -> 0x0119, MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r14 = "merchant_key";
            r11 = r11.getString(r14);	 Catch:{ Exception -> 0x0119, MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r11 = r13.append(r11);	 Catch:{ Exception -> 0x0119, MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r13 = "|";
            r13 = r11.append(r13);	 Catch:{ Exception -> 0x0119, MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r11 = r5.get(r4);	 Catch:{ Exception -> 0x0119, MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r11 = (org.json.JSONObject) r11;	 Catch:{ Exception -> 0x0119, MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r14 = "txnid";
            r11 = r11.getString(r14);	 Catch:{ Exception -> 0x0119, MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r11 = r13.append(r11);	 Catch:{ Exception -> 0x0119, MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r11 = r11.toString();	 Catch:{ Exception -> 0x0119, MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r13 = 1;
            r14 = com.payu.custombrowser.analytics.PayuDeviceAnalytics.this;	 Catch:{ Exception -> 0x0119, MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r14 = r14.context;	 Catch:{ Exception -> 0x0119, MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r12.setBooleanSharedPreference(r11, r13, r14);	 Catch:{ Exception -> 0x0119, MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r4 = r4 + 1;
            goto L_0x00c8;
        L_0x0110:
            r11 = com.payu.custombrowser.analytics.PayuDeviceAnalytics.this;	 Catch:{ Exception -> 0x0119, MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r12 = r15.temp;	 Catch:{ Exception -> 0x0119, MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r11.updateFile(r12);	 Catch:{ Exception -> 0x0119, MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
        L_0x0117:
            r11 = 0;
            return r11;
        L_0x0119:
            r2 = move-exception;
            r11 = com.payu.custombrowser.analytics.PayuDeviceAnalytics.this;	 Catch:{ MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r12 = r15.temp;	 Catch:{ MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r11.updateFile(r12);	 Catch:{ MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            goto L_0x0117;
        L_0x0122:
            r11 = move-exception;
            r2 = r11;
        L_0x0124:
            r2.printStackTrace();	 Catch:{ Exception -> 0x0128 }
            goto L_0x0117;
        L_0x0128:
            r2 = move-exception;
            r2.printStackTrace();
            goto L_0x0117;
        L_0x012d:
            r11 = com.payu.custombrowser.analytics.PayuDeviceAnalytics.this;	 Catch:{ MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r12 = r15.temp;	 Catch:{ MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r11.updateFile(r12);	 Catch:{ MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            goto L_0x0117;
        L_0x0135:
            r11 = move-exception;
            r2 = r11;
            goto L_0x0124;
        L_0x0138:
            r11 = com.payu.custombrowser.analytics.PayuDeviceAnalytics.this;	 Catch:{ MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r11 = r11.context;	 Catch:{ MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r12 = com.payu.custombrowser.analytics.PayuDeviceAnalytics.this;	 Catch:{ MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r12 = r12.file_name;	 Catch:{ MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            r11.deleteFile(r12);	 Catch:{ MalformedURLException -> 0x0122, ProtocolException -> 0x0135, IOException -> 0x0148 }
            goto L_0x0117;
        L_0x0148:
            r2 = move-exception;
            r11 = com.payu.custombrowser.analytics.PayuDeviceAnalytics.this;	 Catch:{ Exception -> 0x0128 }
            r11.resetTimer();	 Catch:{ Exception -> 0x0128 }
            r2.printStackTrace();	 Catch:{ Exception -> 0x0128 }
            goto L_0x0117;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.payu.custombrowser.analytics.PayuDeviceAnalytics.UploadData.doInBackground(java.lang.String[]):java.lang.String");
        }
    }

    public PayuDeviceAnalytics(Context context, final String fileName) {
        this.context = context;
        this.file_name = fileName;
        this.mBuffer = new ArrayList();
        this.cbUtil = new CBUtil();
        final UncaughtExceptionHandler defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            public void uncaughtException(Thread thread, Throwable ex) {
                do {
                } while (PayuDeviceAnalytics.this.mIsLocked);
                PayuDeviceAnalytics.this.setLock();
                try {
                    FileOutputStream fileOutputStream = PayuDeviceAnalytics.this.context.openFileOutput(fileName, 0);
                    int c = PayuDeviceAnalytics.this.mBuffer.size();
                    for (int i = 0; i < c; i++) {
                        fileOutputStream.write((((String) PayuDeviceAnalytics.this.mBuffer.get(i)) + "\r\n").getBytes());
                    }
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                PayuDeviceAnalytics.this.releaseLock();
                defaultUEH.uncaughtException(thread, ex);
            }
        });
    }

    public void log(String msg) {
        if (this.mIsLocked) {
            this.mBuffer.add(msg);
        } else {
            setLock();
            try {
                JSONArray jsonArray;
                JSONObject newobject = new JSONObject(msg);
                String temp = "";
                if (!new File(this.context.getFilesDir(), this.file_name).exists()) {
                    this.context.openFileOutput(this.file_name, 0);
                }
                FileInputStream fileInputStream = this.context.openFileInput(this.file_name);
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
                FileOutputStream fileOutputStream = this.context.openFileOutput(this.file_name, 0);
                jsonArray.put(jsonArray.length(), newobject);
                fileOutputStream.write(jsonArray.toString().getBytes());
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                this.mBuffer.add(msg);
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
            releaseLock();
        }
        resetTimer();
    }

    private void resetTimer() {
        if (this.mTimer != null) {
            this.mTimer.cancel();
        }
        this.mTimer = new Timer();
        this.mTimer.schedule(new C05232(), this.TIMER_DELAY);
    }

    private synchronized void setLock() {
        this.mIsLocked = true;
    }

    private synchronized void releaseLock() {
        this.mIsLocked = false;
    }

    private JSONArray removeJsonObjectAtJsonArrayIndex(JSONArray source, int index) throws JSONException {
        if (index < 0 || index > source.length() - 1) {
            throw new IndexOutOfBoundsException();
        }
        JSONArray copy = new JSONArray();
        int count = source.length();
        for (int i = 0; i < count; i++) {
            if (i != index) {
                copy.put(source.get(i));
            }
        }
        return copy;
    }

    private void updateFile(String temp) {
        try {
            FileOutputStream fileOutputStream = this.context.openFileOutput(this.file_name, 0);
            fileOutputStream.write(temp.getBytes());
            fileOutputStream.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public Timer getmTimer() {
        this.isTimerCancelled = true;
        return this.mTimer;
    }
}
