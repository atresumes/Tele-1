package com.sinch.android.rtc.calling;

import com.sinch.android.rtc.PushPair;
import java.util.List;

public interface CallListener {
    void onCallEnded(Call call);

    void onCallEstablished(Call call);

    void onCallProgressing(Call call);

    void onShouldSendPushNotification(Call call, List<PushPair> list);
}
