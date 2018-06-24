package com.sinch.android.rtc.calling;

import java.util.Map;

public interface Call {
    void addCallListener(CallListener callListener);

    void answer();

    String getCallId();

    CallDetails getDetails();

    CallDirection getDirection();

    Map<String, String> getHeaders();

    String getRemoteUserId();

    CallState getState();

    void hangup();

    void pauseVideo();

    void removeCallListener(CallListener callListener);

    void resumeVideo();

    void sendDTMF(String str);
}
