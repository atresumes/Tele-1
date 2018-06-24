package com.sinch.android.rtc.internal.natives.jni;

public class AsyncAudioController extends NativeProxy {
    public AsyncAudioController(long j) {
        super(j);
    }

    private static synchronized AsyncAudioController createInstance(long j) {
        AsyncAudioController asyncAudioController;
        synchronized (AsyncAudioController.class) {
            asyncAudioController = (AsyncAudioController) NativeProxy.get(j, AsyncAudioController.class);
            if (asyncAudioController == null) {
                asyncAudioController = new AsyncAudioController(j);
                NativeProxy.put(j, asyncAudioController);
            }
        }
        return asyncAudioController;
    }

    public native void muteInput(boolean z);

    public native void onAudioRouteChangedToEarpiece();

    public native void onAudioRouteChangedToSpeaker();
}
