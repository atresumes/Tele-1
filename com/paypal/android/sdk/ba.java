package com.paypal.android.sdk;

import android.os.Handler;
import android.os.Message;
import com.payUMoney.sdk.SdkConstants;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ba extends bh {
    private static final String f962a = ba.class.getSimpleName();
    private Handler f963b;
    private String f964c;
    private String f965d;
    private String f966e;
    private ao f967f;

    public ba(String str, String str2, String str3, ao aoVar, Handler handler) {
        this.f963b = handler;
        this.f964c = str;
        this.f965d = str2;
        this.f966e = str3;
        this.f967f = aoVar;
    }

    private String m815a(String str) {
        Closeable bufferedReader;
        String str2;
        Throwable th;
        Object e;
        HttpURLConnection httpURLConnection = null;
        String str3 = "";
        try {
            URL url = new URL(str);
            HttpURLConnection httpURLConnection2 = (HttpURLConnection) url.openConnection();
            try {
                httpURLConnection2.setReadTimeout(60000);
                httpURLConnection2.setConnectTimeout(60000);
                httpURLConnection2.setRequestMethod("GET");
                httpURLConnection2.setRequestProperty(SdkConstants.USER_AGENT, String.format("%s/%s/%s/%s/Android", new Object[]{this.f967f.m41a(), this.f967f.m43b(), this.f966e, this.f965d}));
                httpURLConnection2.setRequestProperty("Accept-Language", "en-us");
                int responseCode = httpURLConnection2.getResponseCode();
                bn.m141a(f962a, "\nSending 'GET' request to URL : " + url);
                bn.m141a(f962a, "Response Code : " + responseCode);
                bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection2.getInputStream()));
                String str4 = str3;
                while (true) {
                    try {
                        String readLine = bufferedReader.readLine();
                        if (readLine == null) {
                            break;
                        }
                        str4 = str4 + readLine;
                    } catch (Exception e2) {
                        Exception exception = e2;
                        httpURLConnection = httpURLConnection2;
                        str2 = str4;
                        Exception exception2 = exception;
                    } catch (Throwable th2) {
                        httpURLConnection = httpURLConnection2;
                        th = th2;
                    }
                }
                bn.m141a(f962a, str4.toString());
                C0441d.m260a(bufferedReader);
                if (httpURLConnection2 == null) {
                    return str4;
                }
                httpURLConnection2.disconnect();
                return str4;
            } catch (Exception e3) {
                e = e3;
                bufferedReader = null;
                httpURLConnection = httpURLConnection2;
                str2 = str3;
                try {
                    this.f963b.sendMessage(Message.obtain(this.f963b, 1, e));
                    C0441d.m260a(bufferedReader);
                    if (httpURLConnection != null) {
                        return str2;
                    }
                    httpURLConnection.disconnect();
                    return str2;
                } catch (Throwable th3) {
                    th = th3;
                    C0441d.m260a(bufferedReader);
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                    throw th;
                }
            } catch (Throwable th22) {
                bufferedReader = null;
                httpURLConnection = httpURLConnection2;
                th = th22;
                C0441d.m260a(bufferedReader);
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                throw th;
            }
        } catch (Exception e4) {
            e = e4;
            bufferedReader = null;
            str2 = str3;
            this.f963b.sendMessage(Message.obtain(this.f963b, 1, e));
            C0441d.m260a(bufferedReader);
            if (httpURLConnection != null) {
                return str2;
            }
            httpURLConnection.disconnect();
            return str2;
        } catch (Throwable th4) {
            th = th4;
            bufferedReader = null;
            C0441d.m260a(bufferedReader);
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            throw th;
        }
    }

    public void run() {
        if (this.f963b != null) {
            try {
                this.f963b.sendMessage(Message.obtain(this.f963b, 20, this.f964c));
                String a = m815a(this.f964c);
                bn.m141a(f962a, String.format("%s/%s/%s/%s/Android", new Object[]{this.f967f.m41a(), this.f967f.m43b(), this.f966e, this.f965d}));
                this.f963b.sendMessage(Message.obtain(this.f963b, 22, a.toString()));
            } catch (Exception e) {
                this.f963b.sendMessage(Message.obtain(this.f963b, 21, e));
            } finally {
                bi.m121a().m124b(this);
            }
        }
    }
}
