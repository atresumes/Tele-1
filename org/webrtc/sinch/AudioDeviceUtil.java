package org.webrtc.sinch;

import android.os.Build.VERSION;
import android.util.Log;

public class AudioDeviceUtil {
    private static int _audioMode = 0;
    private static int _audioSource = 1;
    private static boolean _overrideAudioMode = false;
    static final String logTag = "Sinch-AudioDeviceUtil";

    public static void LogException(String str, Exception exception) {
        Log.d(str, "Unexpected exception: ", exception);
    }

    public static synchronized int getAudioMode() {
        int i;
        synchronized (AudioDeviceUtil.class) {
            i = _audioMode;
        }
        return i;
    }

    public static synchronized int getAudioSource() {
        int i;
        synchronized (AudioDeviceUtil.class) {
            i = _audioSource;
        }
        return i;
    }

    public static synchronized void setAudioMode(int i) {
        synchronized (AudioDeviceUtil.class) {
            if (i >= 3) {
                if (VERSION.SDK_INT < 11) {
                    Log.d(logTag, "Ignoring incompatible setAudioMode call: audioMode: " + i + " but running on Android SDK " + VERSION.SDK_INT);
                }
            }
            _audioMode = i;
        }
    }

    public static synchronized void setAudioSource(int i) {
        synchronized (AudioDeviceUtil.class) {
            if (i >= 8) {
                if (VERSION.SDK_INT < 11) {
                    Log.d(logTag, "Ignoring incompatible setAudioSource call: audioSource: " + i + " but running on Android SDK " + VERSION.SDK_INT);
                }
            }
            _audioSource = i;
        }
    }

    public static synchronized void setOverrideAudioMode(boolean z) {
        synchronized (AudioDeviceUtil.class) {
            _overrideAudioMode = z;
        }
    }

    public static synchronized boolean shouldOverrideAudioMode() {
        boolean z;
        synchronized (AudioDeviceUtil.class) {
            z = _overrideAudioMode;
        }
        return z;
    }
}
