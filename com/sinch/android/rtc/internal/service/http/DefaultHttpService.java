package com.sinch.android.rtc.internal.service.http;

import android.os.AsyncTask;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.util.Log;
import com.bumptech.glide.load.Key;
import com.sinch.android.rtc.internal.natives.HttpRequest;
import com.sinch.android.rtc.internal.natives.HttpRequestCallback;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class DefaultHttpService implements HttpService {
    private static final int CONNECTION_TIMOUT = 10000;
    private static final int READ_TIMOUT = 10000;
    private static final String TAG = "DefaultHttpService";
    private List<AsyncHttpRequest> mAsyncRequests = new ArrayList();
    private boolean mCancelAllRequests = false;
    private SinchHttpServiceObserver mObserver = null;

    class AsyncHttpRequest extends AsyncTask<Void, Void, String> {
        private HttpURLConnection connection;
        private HttpRequestCallback mCallback;
        private Exception mException;
        private HttpRequest mRequest;
        private int mStatusCode = Callback.DEFAULT_DRAG_ANIMATION_DURATION;

        public AsyncHttpRequest(HttpRequest httpRequest, HttpRequestCallback httpRequestCallback) {
            this.mRequest = httpRequest;
            this.mCallback = httpRequestCallback;
        }

        private void closeQuietly(Closeable closeable) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                }
            }
        }

        private String readResponse(InputStream inputStream) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            for (String readLine = bufferedReader.readLine(); readLine != null; readLine = bufferedReader.readLine()) {
                stringBuilder.append(readLine);
            }
            return stringBuilder.toString();
        }

        private void sendBody(HttpURLConnection httpURLConnection, String str) {
            Throwable th;
            if (str != null) {
                Closeable dataOutputStream;
                try {
                    dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                    try {
                        dataOutputStream.writeBytes(str);
                        dataOutputStream.flush();
                        closeQuietly(dataOutputStream);
                    } catch (Throwable th2) {
                        th = th2;
                        closeQuietly(dataOutputStream);
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    dataOutputStream = null;
                    closeQuietly(dataOutputStream);
                    throw th;
                }
            }
        }

        private boolean shouldWrite(HttpRequest httpRequest) {
            return ((httpRequest.getMethod() != 2 && httpRequest.getMethod() != 0 && httpRequest.getMethod() != 3) || httpRequest.getBody() == null || httpRequest.getBody().isEmpty()) ? false : true;
        }

        protected String doInBackground(Void... voidArr) {
            String str = "";
            if (isCancelled()) {
                return str;
            }
            String readResponse;
            try {
                this.connection = (HttpURLConnection) new URL(this.mRequest.getUrl()).openConnection();
                this.connection.setConnectTimeout(10000);
                this.connection.setReadTimeout(10000);
                this.connection.setRequestMethod(this.mRequest.getMethodString());
                for (Entry entry : this.mRequest.getHeaders().entrySet()) {
                    this.connection.setRequestProperty((String) entry.getKey(), (String) entry.getValue());
                }
                if (shouldWrite(this.mRequest)) {
                    this.connection.setDoOutput(true);
                    sendBody(this.connection, this.mRequest.getBody());
                } else {
                    this.connection.connect();
                }
                if (isCancelled()) {
                    this.connection.disconnect();
                    return str;
                }
                this.mStatusCode = this.connection.getResponseCode();
                readResponse = this.mStatusCode == Callback.DEFAULT_DRAG_ANIMATION_DURATION ? readResponse(this.connection.getInputStream()) : readResponse(this.connection.getErrorStream());
                this.connection.disconnect();
                Log.d(DefaultHttpService.TAG, "Http response: " + readResponse);
                return readResponse;
            } catch (Exception e) {
                this.mException = e;
                readResponse = str;
                Log.d(DefaultHttpService.TAG, "Http response: " + readResponse);
                return readResponse;
            } finally {
                this.connection.disconnect();
            }
        }

        protected void onPostExecute(String str) {
            super.onPostExecute(str);
            DefaultHttpService.this.mAsyncRequests.remove(this);
            if (!isCancelled()) {
                if (this.mException != null) {
                    Log.e(DefaultHttpService.TAG, "Error while executing http request: ", this.mException);
                    this.mCallback.exception("DefaultHttpService: " + this.mException.getMessage());
                    return;
                }
                String headerField = this.connection.getHeaderField(null);
                HttpRequestCallback httpRequestCallback = this.mCallback;
                int i = this.mStatusCode;
                if (headerField == null) {
                    headerField = "";
                }
                httpRequestCallback.completed(i, headerField, this.connection.getContentLength(), this.connection.getContentType(), str);
            }
        }

        protected void onPreExecute() {
            super.onPreExecute();
            DefaultHttpService.this.mAsyncRequests.add(this);
        }
    }

    public synchronized void cancelAllRequests() {
        this.mCancelAllRequests = true;
        for (AsyncHttpRequest cancel : this.mAsyncRequests) {
            cancel.cancel(true);
        }
        this.mAsyncRequests.clear();
    }

    public HttpRequest createHttpRequest(String str, int i, String str2, Map<String, String> map) {
        return new HttpRequest(str, i, str2, map);
    }

    public void sendRequest(HttpRequest httpRequest, HttpRequestCallback httpRequestCallback) {
        Log.d(TAG, "Http request to " + httpRequest.getUrl());
        if (this.mCancelAllRequests) {
            Log.d(TAG, "Ignoring http request, since we've got cancelAllRequests");
            return;
        }
        try {
            if (this.mObserver != null) {
                this.mObserver.onHttpRequestSent(httpRequest.getUrl(), httpRequest.getMethodString(), httpRequest.getBody().getBytes(Key.STRING_CHARSET_NAME));
            }
            new AsyncHttpRequest(httpRequest, httpRequestCallback).execute(new Void[0]);
        } catch (UnsupportedEncodingException e) {
            httpRequestCallback.exception(e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    public void setObserver(SinchHttpServiceObserver sinchHttpServiceObserver) {
        this.mObserver = sinchHttpServiceObserver;
    }
}
