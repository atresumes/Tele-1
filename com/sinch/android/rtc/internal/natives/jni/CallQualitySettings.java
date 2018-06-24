package com.sinch.android.rtc.internal.natives.jni;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

public class CallQualitySettings {
    private static final String TAG = "CallQualitySettings";
    private final AudioManager mAudioManager;
    private boolean mAudioModeNeedsRestore = false;
    private int mSavedAudioMode = -2;

    CallQualitySettings(Context context, AudioManager audioManager) {
        this.mAudioManager = audioManager;
    }

    public CallQualitySettings(Object obj) {
        this.mAudioManager = (AudioManager) ((Context) obj).getSystemService("audio");
    }

    public void setCommunicationMode(boolean z) {
        if (z) {
            if (this.mAudioManager.getMode() != 3) {
                this.mSavedAudioMode = this.mAudioManager.getMode();
                try {
                    this.mAudioManager.setMode(3);
                } catch (SecurityException e) {
                    Log.e(TAG, "Could not change AudioManager mode to MODE_IN_COMMUNICATION: " + e);
                }
                this.mAudioModeNeedsRestore = true;
            }
        } else if (this.mAudioModeNeedsRestore) {
            try {
                this.mAudioManager.setMode(this.mSavedAudioMode);
            } catch (SecurityException e2) {
                Log.e(TAG, "Could not change AudioManager mode to " + this.mSavedAudioMode + ": " + e2);
            }
            this.mAudioModeNeedsRestore = false;
        }
    }
}
