package com.sinch.android.rtc.video;

import java.nio.ByteBuffer;

public interface VideoFrame {
    int height();

    void release();

    int width();

    ByteBuffer[] yuvPlanes();

    int[] yuvStrides();
}
