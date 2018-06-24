package com.sinch.android.rtc.internal.service.pubnub.http;

import com.bumptech.glide.load.Key;
import com.payUMoney.sdk.SdkConstants;
import com.sinch.gson.JsonObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.GZIPInputStream;

public class DefaultHttpRequester implements HttpRequester {
    private static final boolean ALLOW_GZIP = true;
    public static final int CONNECT_TIME_OUT = 5000;
    private HttpURLConnection urlConnection;

    private String readInput(URLConnection uRLConnection) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader("gzip".equals(uRLConnection.getContentEncoding()) ? new BufferedInputStream(new GZIPInputStream(uRLConnection.getInputStream())) : new BufferedInputStream(uRLConnection.getInputStream()), Key.STRING_CHARSET_NAME));
            StringBuilder stringBuilder = new StringBuilder();
            try {
                for (String readLine = bufferedReader.readLine(); readLine != null; readLine = bufferedReader.readLine()) {
                    stringBuilder.append(readLine);
                }
                return stringBuilder.toString();
            } catch (Throwable e) {
                throw new IOException(e);
            } catch (Throwable e2) {
                throw new IOException(e2);
            }
        } catch (IllegalStateException e3) {
            throw new IOException("IllegalStateException rethrown as IOException due to Android API change");
        } catch (Throwable e22) {
            throw new IOException(e22);
        } catch (Throwable e222) {
            throw new IOException(e222);
        }
    }

    public String get(String str, int i) {
        if (this.urlConnection != null) {
            throw new IllegalStateException("Request already active");
        }
        try {
            try {
                this.urlConnection = (HttpURLConnection) new URL(str).openConnection();
                this.urlConnection.setConnectTimeout(5000);
                System.setProperty("http.keepAlive", SdkConstants.FALSE_STRING);
                this.urlConnection.setReadTimeout(i);
                String readInput = readInput(this.urlConnection);
                this.urlConnection = null;
                return readInput;
            } finally {
                this.urlConnection.disconnect();
                this.urlConnection = null;
            }
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Request is not a URL");
        }
    }

    public String postJson(String str, JsonObject jsonObject, int i) {
        if (this.urlConnection != null) {
            throw new IllegalStateException("Request already active");
        }
        try {
            this.urlConnection = (HttpURLConnection) new URL(str).openConnection();
            String jsonObject2 = jsonObject.toString();
            try {
                this.urlConnection.setDoOutput(ALLOW_GZIP);
                this.urlConnection.setConnectTimeout(5000);
                this.urlConnection.setReadTimeout(i);
                this.urlConnection.setRequestProperty("Accept-Encoding", "gzip");
                int length = jsonObject2.getBytes().length;
                this.urlConnection.setRequestMethod("POST");
                this.urlConnection.setRequestProperty("Content-Type", "application/json");
                this.urlConnection.setFixedLengthStreamingMode(length);
                PrintWriter printWriter = new PrintWriter(this.urlConnection.getOutputStream());
                printWriter.print(jsonObject2);
                printWriter.close();
                jsonObject2 = readInput(this.urlConnection);
                this.urlConnection = null;
                return jsonObject2;
            } finally {
                this.urlConnection.disconnect();
                this.urlConnection = null;
            }
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Request is not a URL");
        }
    }
}
