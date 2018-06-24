package com.sinch.android.rtc.internal.client.gcm;

import android.content.Context;
import com.sinch.android.rtc.internal.InternalErrorCodes;

public class GetDeviceTokenTask implements GcmRegistrationCallback {
    static final int[] HOLDBACK_STEPS = new int[]{0, InternalErrorCodes.CapabilityUserNotFound, 5000, 15000, 30000};
    static final int MAX_RETRIES = 5;
    private int mAttempt = 0;
    private GcmRegistrationCallback mCallback;
    private Context mContext;
    private String mSenderId;
    private PersistedToken mStore;
    private GcmTask mTask;

    public GetDeviceTokenTask(Context context, PersistedToken persistedToken, String str, GcmRegistrationCallback gcmRegistrationCallback) {
        this.mContext = context;
        this.mStore = persistedToken;
        this.mSenderId = str;
        this.mCallback = gcmRegistrationCallback;
        this.mTask = new GcmTask(context, this.mStore, str, this, HOLDBACK_STEPS[this.mAttempt]);
    }

    public void execute() {
        this.mTask.execute(new Void[0]);
    }

    public void onGcmRegistrationSucceeded(PersistedToken persistedToken, String str, String str2) {
        if (str2 == null) {
            this.mAttempt++;
            if (this.mAttempt < 5) {
                this.mTask = new GcmTask(this.mContext, this.mStore, this.mSenderId, this, HOLDBACK_STEPS[this.mAttempt]);
                this.mTask.execute(new Void[0]);
                return;
            }
            return;
        }
        this.mCallback.onGcmRegistrationSucceeded(persistedToken, this.mSenderId, str2);
    }
}
