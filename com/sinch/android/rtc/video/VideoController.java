package com.sinch.android.rtc.video;

import android.view.View;

public interface VideoController {
    int getCaptureDevicePosition();

    View getLocalView();

    View getRemoteView();

    void setBorderColor(float f, float f2, float f3);

    void setCaptureDevicePosition(int i);

    void setResizeBehaviour(VideoScalingType videoScalingType);

    void setVideoFrameListener(VideoFrameListener videoFrameListener);

    void toggleCaptureDevicePosition();
}
