package com.sinch.android.rtc.internal.client.video;

import com.sinch.android.rtc.video.VideoFrame;
import java.nio.ByteBuffer;
import org.webrtc.sinch.VideoRenderer;
import org.webrtc.sinch.VideoRenderer.I420Frame;

public class DefaultVideoFrame implements VideoFrame {
    I420Frame mFrame;

    public DefaultVideoFrame(I420Frame i420Frame) {
        this.mFrame = i420Frame;
    }

    private void throwIfDisposed() {
        if (this.mFrame == null) {
            throw new IllegalStateException("The frame has already been released.");
        }
    }

    public int height() {
        throwIfDisposed();
        return this.mFrame.height;
    }

    public void release() {
        throwIfDisposed();
        VideoRenderer.renderFrameDone(this.mFrame);
        this.mFrame = null;
    }

    public int width() {
        throwIfDisposed();
        return this.mFrame.width;
    }

    public ByteBuffer[] yuvPlanes() {
        throwIfDisposed();
        return this.mFrame.yuvPlanes;
    }

    public int[] yuvStrides() {
        throwIfDisposed();
        return this.mFrame.yuvStrides;
    }
}
