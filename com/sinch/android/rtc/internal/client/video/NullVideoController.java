package com.sinch.android.rtc.internal.client.video;

import android.view.View;
import com.sinch.android.rtc.video.VideoFrameListener;
import com.sinch.android.rtc.video.VideoScalingType;

public class NullVideoController implements VideoControllerInternal, VideoTrackListenerInternal {
    public void dispose() {
    }

    public int getCaptureDevicePosition() {
        return 0;
    }

    public View getLocalView() {
        return null;
    }

    public View getRemoteView() {
        return null;
    }

    public long getVideoFrameSink() {
        return 0;
    }

    public void onVideoTrackAddedInternal(String str) {
    }

    public void setBorderColor(float f, float f2, float f3) {
    }

    public void setCaptureDevicePosition(int i) {
    }

    public void setResizeBehaviour(VideoScalingType videoScalingType) {
    }

    public void setVideoFrameListener(VideoFrameListener videoFrameListener) {
    }

    public void toggleCaptureDevicePosition() {
    }
}
