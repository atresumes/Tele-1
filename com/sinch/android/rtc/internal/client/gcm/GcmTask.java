package com.sinch.android.rtc.internal.client.gcm;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.google.android.gms.iid.InstanceID;
import java.io.IOException;

class GcmTask extends AsyncTask<Void, Void, String> {
    private static final String SCOPE = "GCM";
    private static final String TAG = "Sinch-GCMTask";
    private GcmRegistrationCallback mCallback;
    private Context mContext;
    private int mHoldback;
    private String mSenderId;
    private PersistedToken mStore;

    public GcmTask(Context context, PersistedToken persistedToken, String str, GcmRegistrationCallback gcmRegistrationCallback, int i) {
        this.mContext = context;
        this.mStore = persistedToken;
        this.mSenderId = str;
        this.mCallback = gcmRegistrationCallback;
        this.mHoldback = i;
    }

    protected String doInBackground(Void... voidArr) {
        if (this.mHoldback > 0) {
            try {
                Thread.sleep((long) this.mHoldback);
            } catch (InterruptedException e) {
            }
        }
        String str = null;
        try {
            str = InstanceID.getInstance(this.mContext).getToken(this.mSenderId, SCOPE);
        } catch (IOException e2) {
        }
        if (str == null) {
            Log.w(TAG, "Failed to register with GCM.");
        }
        return str;
    }

    protected void onPostExecute(String str) {
        this.mCallback.onGcmRegistrationSucceeded(this.mStore, this.mSenderId, str);
    }
}
