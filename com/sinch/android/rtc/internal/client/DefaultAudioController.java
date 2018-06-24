package com.sinch.android.rtc.internal.client;

import android.content.Context;
import android.media.AudioManager;
import com.sinch.android.rtc.AudioController;
import com.sinch.android.rtc.internal.natives.jni.AsyncAudioController;

public class DefaultAudioController implements AudioController {
    protected final DefaultSinchClient client;
    protected final Context context;
    protected final AsyncAudioController mAsyncAudioController;

    DefaultAudioController(Context context, DefaultSinchClient defaultSinchClient, AsyncAudioController asyncAudioController) {
        this.context = context.getApplicationContext();
        this.client = defaultSinchClient;
        this.mAsyncAudioController = asyncAudioController;
    }

    protected void checkState() {
        if (this.client.isDisposed()) {
            throw new IllegalStateException("Associated SinchClient is disposed");
        } else if (!this.client.isStarted()) {
            throw new IllegalStateException("Associated SinchClient is stopped");
        }
    }

    public void disableSpeaker() {
        checkState();
        ((AudioManager) this.context.getSystemService("audio")).setSpeakerphoneOn(false);
        this.mAsyncAudioController.onAudioRouteChangedToEarpiece();
    }

    public void enableSpeaker() {
        checkState();
        ((AudioManager) this.context.getSystemService("audio")).setSpeakerphoneOn(true);
        this.mAsyncAudioController.onAudioRouteChangedToSpeaker();
    }

    public void mute() {
        checkState();
        this.mAsyncAudioController.muteInput(true);
    }

    public void unmute() {
        checkState();
        this.mAsyncAudioController.muteInput(false);
    }
}
