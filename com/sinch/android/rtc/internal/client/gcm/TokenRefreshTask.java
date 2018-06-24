package com.sinch.android.rtc.internal.client.gcm;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import com.sinch.android.rtc.internal.client.gcm.InstanceIDTokenService.LocalBinder;

public class TokenRefreshTask {
    private static final String TAG = TokenRefreshTask.class.getSimpleName();
    private TokenRefreshCallback mCallback;
    private Context mContext;
    private ServiceConnection mInstanceIDServiceConnection;
    private InstanceIDTokenService mInstanceIdService;

    class C05521 implements ServiceConnection {
        C05521() {
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(TokenRefreshTask.TAG, "InstanceID service connected.");
            TokenRefreshTask.this.mInstanceIdService = ((LocalBinder) iBinder).getService();
            TokenRefreshTask.this.mInstanceIdService.setTokenRefreshCallback(TokenRefreshTask.this.mCallback);
        }

        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(TokenRefreshTask.TAG, "InstanceID service disconnected.");
            TokenRefreshTask.this.mInstanceIdService = null;
        }
    }

    public TokenRefreshTask(Context context, TokenRefreshCallback tokenRefreshCallback) {
        this.mContext = context;
        this.mCallback = tokenRefreshCallback;
    }

    public void start() {
        this.mInstanceIDServiceConnection = new C05521();
        this.mContext.bindService(new Intent(this.mContext, InstanceIDTokenService.class), this.mInstanceIDServiceConnection, 1);
    }

    public void stop() {
        if (this.mInstanceIdService != null) {
            this.mInstanceIdService.setTokenRefreshCallback(null);
            if (this.mInstanceIDServiceConnection != null) {
                this.mContext.unbindService(this.mInstanceIDServiceConnection);
                this.mInstanceIDServiceConnection = null;
            }
        }
    }
}
