package org.webrtc.sinch;

import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CameraEnumerationAndroid {
    private static final String TAG = "CameraEnumerationAndroid";

    public class CaptureFormat {
        public final FramerateRange framerate;
        public final int height;
        public final int imageFormat = 17;
        public final int width;

        public class FramerateRange {
            public int max;
            public int min;

            public FramerateRange(int i, int i2) {
                this.min = i;
                this.max = i2;
            }

            public boolean equals(Object obj) {
                if (!(obj instanceof FramerateRange)) {
                    return false;
                }
                FramerateRange framerateRange = (FramerateRange) obj;
                return this.min == framerateRange.min && this.max == framerateRange.max;
            }

            public int hashCode() {
                return ((65537 * this.min) + 1) + this.max;
            }

            public String toString() {
                return "[" + (((float) this.min) / 1000.0f) + ":" + (((float) this.max) / 1000.0f) + "]";
            }
        }

        public CaptureFormat(int i, int i2, int i3, int i4) {
            this.width = i;
            this.height = i2;
            this.framerate = new FramerateRange(i3, i4);
        }

        public CaptureFormat(int i, int i2, FramerateRange framerateRange) {
            this.width = i;
            this.height = i2;
            this.framerate = framerateRange;
        }

        public static int frameSize(int i, int i2, int i3) {
            if (i3 == 17) {
                return ((i * i2) * ImageFormat.getBitsPerPixel(i3)) / 8;
            }
            throw new UnsupportedOperationException("Don't know how to calculate the frame size of non-NV21 image formats.");
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof CaptureFormat)) {
                return false;
            }
            CaptureFormat captureFormat = (CaptureFormat) obj;
            return this.width == captureFormat.width && this.height == captureFormat.height && this.framerate.equals(captureFormat.framerate);
        }

        public int frameSize() {
            return frameSize(this.width, this.height, 17);
        }

        public int hashCode() {
            return ((((this.width * 65497) + this.height) * 251) + 1) + this.framerate.hashCode();
        }

        public String toString() {
            return this.width + "x" + this.height + "@" + this.framerate;
        }
    }

    abstract class ClosestComparator<T> implements Comparator<T> {
        private ClosestComparator() {
        }

        public int compare(T t, T t2) {
            return diff(t) - diff(t2);
        }

        abstract int diff(T t);
    }

    final class C11121 extends ClosestComparator<FramerateRange> {
        private static final int MAX_FPS_DIFF_THRESHOLD = 5000;
        private static final int MAX_FPS_HIGH_DIFF_WEIGHT = 3;
        private static final int MAX_FPS_LOW_DIFF_WEIGHT = 1;
        private static final int MIN_FPS_HIGH_VALUE_WEIGHT = 4;
        private static final int MIN_FPS_LOW_VALUE_WEIGHT = 1;
        private static final int MIN_FPS_THRESHOLD = 8000;
        final /* synthetic */ int val$requestedFps;

        C11121(int i) {
            this.val$requestedFps = i;
            super();
        }

        private int progressivePenalty(int i, int i2, int i3, int i4) {
            return i < i2 ? i * i3 : (i2 * i3) + ((i - i2) * i4);
        }

        int diff(FramerateRange framerateRange) {
            return progressivePenalty(framerateRange.min, MIN_FPS_THRESHOLD, 1, 4) + progressivePenalty(Math.abs((this.val$requestedFps * 1000) - framerateRange.max), 5000, 1, 3);
        }
    }

    final class C11132 extends ClosestComparator<Size> {
        final /* synthetic */ int val$requestedHeight;
        final /* synthetic */ int val$requestedWidth;

        C11132(int i, int i2) {
            this.val$requestedWidth = i;
            this.val$requestedHeight = i2;
            super();
        }

        int diff(Size size) {
            return Math.abs(this.val$requestedWidth - size.width) + Math.abs(this.val$requestedHeight - size.height);
        }
    }

    public static FramerateRange getClosestSupportedFramerateRange(List<FramerateRange> list, int i) {
        return (FramerateRange) Collections.min(list, new C11121(i));
    }

    public static Size getClosestSupportedSize(List<Size> list, int i, int i2) {
        return (Size) Collections.min(list, new C11132(i, i2));
    }

    @Deprecated
    public static int getDeviceCount() {
        return new Camera1Enumerator().getDeviceNames().length;
    }

    @Deprecated
    public static String getDeviceName(int i) {
        Camera1Enumerator camera1Enumerator = new Camera1Enumerator();
        return Camera1Enumerator.getDeviceName(i);
    }

    @Deprecated
    public static String[] getDeviceNames() {
        return new Camera1Enumerator().getDeviceNames();
    }

    @Deprecated
    public static String getNameOfBackFacingDevice() {
        return getNameOfDevice(0);
    }

    private static String getNameOfDevice(int i) {
        CameraInfo cameraInfo = new CameraInfo();
        int i2 = 0;
        while (i2 < Camera.getNumberOfCameras()) {
            try {
                Camera.getCameraInfo(i2, cameraInfo);
                if (cameraInfo.facing == i) {
                    return getDeviceName(i2);
                }
                i2++;
            } catch (Throwable e) {
                Logging.m778e(TAG, "getCameraInfo() failed on index " + i2, e);
            }
        }
        return null;
    }

    @Deprecated
    public static String getNameOfFrontFacingDevice() {
        return getNameOfDevice(1);
    }
}
