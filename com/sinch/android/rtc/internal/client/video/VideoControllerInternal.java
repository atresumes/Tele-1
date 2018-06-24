package com.sinch.android.rtc.internal.client.video;

import com.sinch.android.rtc.video.VideoController;

public interface VideoControllerInternal extends VideoController {
    void dispose();

    long getVideoFrameSink();
}
