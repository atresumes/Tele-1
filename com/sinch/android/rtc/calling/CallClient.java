package com.sinch.android.rtc.calling;

import java.util.Map;

public interface CallClient {
    void addCallClientListener(CallClientListener callClientListener);

    Call callConference(String str);

    Call callConference(String str, Map<String, String> map);

    Call callPhoneNumber(String str);

    Call callPhoneNumber(String str, Map<String, String> map);

    Call callSip(String str);

    Call callSip(String str, Map<String, String> map);

    Call callUser(String str);

    Call callUser(String str, Map<String, String> map);

    Call callUserVideo(String str);

    Call callUserVideo(String str, Map<String, String> map);

    Call getCall(String str);

    void removeCallClientListener(CallClientListener callClientListener);

    void setRespectNativeCalls(boolean z);
}
