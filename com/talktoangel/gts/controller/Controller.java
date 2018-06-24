package com.talktoangel.gts.controller;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.Volley;

public class Controller extends Application {
    public static final String TAG = Controller.class.getSimpleName();
    private static Controller mInstance;
    private RequestQueue mRequestQueue;

    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized Controller getInstance() {
        Controller controller;
        synchronized (Controller.class) {
            controller = mInstance;
        }
        return controller;
    }

    public RequestQueue getRequestQueue() {
        if (this.mRequestQueue == null) {
            this.mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return this.mRequestQueue;
    }

    public RequestQueue getRequestQueueSSL(HttpStack stack) {
        if (this.mRequestQueue == null) {
            this.mRequestQueue = Volley.newRequestQueue(getApplicationContext(), stack);
        }
        return this.mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        if (TextUtils.isEmpty(tag)) {
            tag = TAG;
        }
        req.setTag(tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req, HttpStack stack) {
        req.setTag(TAG);
        getRequestQueueSSL(stack).add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (this.mRequestQueue != null) {
            this.mRequestQueue.cancelAll(tag);
            Log.e("request", tag + "");
        }
    }
}
