package com.sinch.android.rtc.video;

import java.nio.ByteBuffer;

public class VideoUtils {

    class NV21Frame implements VideoFrame {
        private final int mHeight;
        private final int mWidth;
        private final ByteBuffer[] mYuvPlanes;
        private final int[] mYuvStrides;

        public NV21Frame(int i, int i2, int[] iArr, ByteBuffer[] byteBufferArr) {
            this.mWidth = i;
            this.mHeight = i2;
            this.mYuvStrides = iArr;
            this.mYuvPlanes = byteBufferArr;
        }

        public int height() {
            return this.mHeight;
        }

        public void release() {
        }

        public int width() {
            return this.mWidth;
        }

        public ByteBuffer[] yuvPlanes() {
            return this.mYuvPlanes;
        }

        public int[] yuvStrides() {
            return this.mYuvStrides;
        }
    }

    public static VideoFrame I420toNV21Frame(VideoFrame videoFrame) {
        if (videoFrame.yuvPlanes().length != 3) {
            throw new IllegalArgumentException("The frame must have 3 planes to be converted from I420.");
        } else if (videoFrame.yuvStrides().length != 3) {
            throw new IllegalArgumentException("The frame must have 3 strides to be converted from I420.");
        } else {
            return new NV21Frame(videoFrame.width(), videoFrame.height(), new int[]{videoFrame.yuvStrides()[0], videoFrame.yuvStrides()[1] * 2}, new ByteBuffer[]{ByteBuffer.wrap(NV21Data(videoFrame))});
        }
    }

    private static byte[] NV21Data(VideoFrame videoFrame) {
        int i = 0;
        int width = videoFrame.width() * videoFrame.height();
        Object obj = new byte[((width / 2) + width)];
        Object obj2 = new byte[width];
        byte[] bArr = new byte[(width / 4)];
        byte[] bArr2 = new byte[(width / 4)];
        ByteBuffer byteBuffer = videoFrame.yuvPlanes()[0];
        ByteBuffer byteBuffer2 = videoFrame.yuvPlanes()[1];
        ByteBuffer byteBuffer3 = videoFrame.yuvPlanes()[2];
        byteBuffer.position(0);
        byteBuffer2.position(0);
        byteBuffer3.position(0);
        byteBuffer.get(obj2, 0, width);
        byteBuffer3.get(bArr2, 0, width / 4);
        byteBuffer2.get(bArr, 0, width / 4);
        System.arraycopy(obj2, 0, obj, 0, width);
        while (i < width / 4) {
            obj[(i * 2) + width] = bArr2[i];
            obj[((i * 2) + width) + 1] = bArr[i];
            i++;
        }
        return obj;
    }
}
