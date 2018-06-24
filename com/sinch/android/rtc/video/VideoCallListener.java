package com.sinch.android.rtc.video;

import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallListener;

public interface VideoCallListener extends CallListener {
    void onVideoTrackAdded(Call call);

    void onVideoTrackPaused(Call call);

    void onVideoTrackResumed(Call call);
}
