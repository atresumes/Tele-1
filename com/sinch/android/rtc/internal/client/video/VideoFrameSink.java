package com.sinch.android.rtc.internal.client.video;

import com.sinch.android.rtc.video.VideoFrameListener;
import org.webrtc.sinch.VideoRenderer;
import org.webrtc.sinch.VideoRenderer.Callbacks;
import org.webrtc.sinch.VideoRenderer.I420Frame;

public class VideoFrameSink implements Callbacks {
    private Callbacks mCallbacks;
    private VideoFrameListener mListener;
    private String mSessionId = "";
    private VideoRenderer mVideoFrameSinkWrapper = new VideoRenderer(this);

    public void dispose() {
        if (this.mVideoFrameSinkWrapper != null) {
            this.mVideoFrameSinkWrapper.dispose();
        }
    }

    public long nativeRenderer() {
        return this.mVideoFrameSinkWrapper.nativeVideoRenderer;
    }

    public synchronized void renderFrame(I420Frame i420Frame) {
        if (this.mListener != null) {
            this.mListener.onFrame(this.mSessionId, new DefaultVideoFrame(VideoRenderer.copyFrameWithRotation(i420Frame)));
        }
        if (this.mCallbacks != null) {
            this.mCallbacks.renderFrame(i420Frame);
        }
    }

    synchronized void setSessionId(String str) {
        this.mSessionId = str;
    }

    synchronized void setVideoFrameListener(VideoFrameListener videoFrameListener) {
        this.mListener = videoFrameListener;
    }

    synchronized void setVideoRendererCallbacks(Callbacks callbacks) {
        this.mCallbacks = callbacks;
    }
}
