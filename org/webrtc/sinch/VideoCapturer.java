package org.webrtc.sinch;

import android.content.Context;
import java.util.List;
import org.webrtc.sinch.CameraEnumerationAndroid.CaptureFormat;

public interface VideoCapturer {

    public interface CapturerObserver {
        void onByteBufferFrameCaptured(byte[] bArr, int i, int i2, int i3, long j);

        void onCapturerStarted(boolean z);

        void onOutputFormatRequest(int i, int i2, int i3);

        void onTextureFrameCaptured(int i, int i2, int i3, float[] fArr, int i4, long j);
    }

    public class NativeObserver implements CapturerObserver {
        private final long nativeCapturer;

        public NativeObserver(long j) {
            this.nativeCapturer = j;
        }

        private native void nativeCapturerStarted(long j, boolean z);

        private native void nativeOnByteBufferFrameCaptured(long j, byte[] bArr, int i, int i2, int i3, int i4, long j2);

        private native void nativeOnOutputFormatRequest(long j, int i, int i2, int i3);

        private native void nativeOnTextureFrameCaptured(long j, int i, int i2, int i3, float[] fArr, int i4, long j2);

        public void onByteBufferFrameCaptured(byte[] bArr, int i, int i2, int i3, long j) {
            nativeOnByteBufferFrameCaptured(this.nativeCapturer, bArr, bArr.length, i, i2, i3, j);
        }

        public void onCapturerStarted(boolean z) {
            nativeCapturerStarted(this.nativeCapturer, z);
        }

        public void onOutputFormatRequest(int i, int i2, int i3) {
            nativeOnOutputFormatRequest(this.nativeCapturer, i, i2, i3);
        }

        public void onTextureFrameCaptured(int i, int i2, int i3, float[] fArr, int i4, long j) {
            nativeOnTextureFrameCaptured(this.nativeCapturer, i, i2, i3, fArr, i4, j);
        }
    }

    void dispose();

    List<CaptureFormat> getSupportedFormats();

    void startCapture(int i, int i2, int i3, SurfaceTextureHelper surfaceTextureHelper, Context context, CapturerObserver capturerObserver);

    void stopCapture();
}
