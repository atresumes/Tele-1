package com.sinch.android.rtc.internal.client.gcm;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class PersistedToken {
    static final String APP_VERSION = "AppVersion";
    static final String SHARED_PREF_KEY = "SinchManagedPush";
    private int mCurrentVersion = 0;
    private String mSenderId;
    private SharedPreferences mSharedPreferences;

    public PersistedToken(Context context, String str) {
        this.mSenderId = str;
        this.mSharedPreferences = context.getSharedPreferences(SHARED_PREF_KEY, 0);
        this.mCurrentVersion = getAppVersion(context);
    }

    static int getAppVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            Log.e("PersistedToken", "Failed to get package name, this should never happen!");
            return -1;
        }
    }

    public String getPersistedToken() {
        return this.mCurrentVersion == this.mSharedPreferences.getInt(APP_VERSION, Integer.MIN_VALUE) ? this.mSharedPreferences.getString(this.mSenderId, null) : null;
    }

    public void savePersistedToken(String str) {
        Editor edit = this.mSharedPreferences.edit();
        edit.putInt(APP_VERSION, this.mCurrentVersion);
        edit.putString(this.mSenderId, str);
        edit.commit();
    }
}
