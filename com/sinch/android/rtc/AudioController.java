package com.sinch.android.rtc;

public interface AudioController {
    void disableSpeaker();

    void enableSpeaker();

    void mute();

    void unmute();
}
