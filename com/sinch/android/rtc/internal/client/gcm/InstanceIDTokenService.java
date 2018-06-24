package com.sinch.android.rtc.internal.client.gcm;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import com.google.android.gms.iid.InstanceIDListenerService;

public class InstanceIDTokenService extends InstanceIDListenerService {
    private static final String TAG = InstanceIDTokenService.class.getSimpleName();
    private final IBinder mBinder = new LocalBinder();
    private TokenRefreshCallback mCallback;

    public class LocalBinder extends Binder {
        public InstanceIDTokenService getService() {
            return InstanceIDTokenService.this;
        }
    }

    public IBinder onBind(Intent intent) {
        return this.mBinder;
    }

    public void onTokenRefresh() {
        super.onTokenRefresh();
        Log.d(TAG, "onTokenRefresh.");
        if (this.mCallback != null) {
            this.mCallback.onTokenRefreshNeeded();
        }
    }

    public void setTokenRefreshCallback(TokenRefreshCallback tokenRefreshCallback) {
        this.mCallback = tokenRefreshCallback;
    }
}
