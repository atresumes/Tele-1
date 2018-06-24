package org.webrtc.sinch;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.ErrorCallback;
import android.hardware.Camera.PreviewCallback;
import android.os.Handler;
import android.os.SystemClock;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.WindowManager;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.webrtc.sinch.CameraEnumerationAndroid.CaptureFormat;
import org.webrtc.sinch.CameraVideoCapturer.CameraEventsHandler;
import org.webrtc.sinch.CameraVideoCapturer.CameraStatistics;
import org.webrtc.sinch.SurfaceTextureHelper.OnTextureFrameAvailableListener;
import org.webrtc.sinch.VideoCapturer.CapturerObserver;

public class VideoCapturerAndroid implements PreviewCallback, Callback, CameraVideoCapturer, OnTextureFrameAvailableListener {
    private static final int CAMERA_STOP_TIMEOUT_MS = 7000;
    private static final int MAX_OPEN_CAMERA_ATTEMPTS = 3;
    private static final int NUMBER_OF_CAPTURE_BUFFERS = 3;
    private static final int OPEN_CAMERA_DELAY_MS = 500;
    private static final String TAG = "VideoCapturerAndroid";
    private static SurfaceHolder localPreview;
    private Context applicationContext;
    private Camera camera;
    private final ErrorCallback cameraErrorCallback = new C07171();
    private final Object cameraIdLock = new Object();
    private CameraStatistics cameraStatistics;
    private Handler cameraThreadHandler;
    private CaptureFormat captureFormat;
    private final CameraEventsHandler eventsHandler;
    private boolean firstFrameReported;
    private CapturerObserver frameObserver = null;
    private final Object handlerLock = new Object();
    private int id;
    private CameraInfo info;
    private final boolean isCapturingToTexture;
    private int openCameraAttempts;
    private volatile boolean pendingCameraSwitch;
    private final Object pendingCameraSwitchLock = new Object();
    private final Set<byte[]> queuedBuffers = new HashSet();
    private int requestedFramerate;
    private int requestedHeight;
    private int requestedWidth;
    private SurfaceTextureHelper surfaceHelper;

    class C07171 implements ErrorCallback {
        C07171() {
        }

        public void onError(int i, Camera camera) {
            String str = i == 100 ? "Camera server died!" : "Camera error: " + i;
            VideoCapturerAndroid.this.camera = null;
            if (VideoCapturerAndroid.this.eventsHandler != null) {
                VideoCapturerAndroid.this.eventsHandler.onCameraError(str);
            }
        }
    }

    class C07269 implements Runnable {
        C07269() {
        }

        public void run() {
            if (VideoCapturerAndroid.this.camera != null) {
                VideoCapturerAndroid.this.camera.stopPreview();
                VideoCapturerAndroid.this.camera.setPreviewCallbackWithBuffer(null);
            }
            VideoCapturerAndroid.this.setPreviewDisplayOnCameraThread(null);
        }
    }

    class CameraEvents implements CameraEventsHandler {
        CameraEvents() {
        }

        public void onCameraClosed() {
            Logging.m776d(VideoCapturerAndroid.TAG, "Camera closed.");
        }

        public void onCameraError(String str) {
            Logging.m777e(VideoCapturerAndroid.TAG, "Camera error: " + str);
        }

        public void onCameraFreezed(String str) {
            Logging.m777e(VideoCapturerAndroid.TAG, "Camera freezed: " + str);
        }

        public void onCameraOpening(int i) {
            Logging.m776d(VideoCapturerAndroid.TAG, "Opening camera: " + i);
        }

        public void onFirstFrameAvailable() {
        }
    }

    public VideoCapturerAndroid(String str, CameraEventsHandler cameraEventsHandler, boolean z) {
        if (Camera.getNumberOfCameras() == 0) {
            throw new RuntimeException("No cameras available");
        }
        if (str == null || str.equals("")) {
            this.id = 0;
        } else {
            this.id = Camera1Enumerator.getCameraIndex(str);
        }
        this.eventsHandler = cameraEventsHandler;
        this.isCapturingToTexture = z;
        Logging.m776d(TAG, "VideoCapturerAndroid isCapturingToTexture : " + this.isCapturingToTexture);
    }

    private void checkIsOnCameraThread() {
        synchronized (this.handlerLock) {
            if (this.cameraThreadHandler == null) {
                Logging.m780w(TAG, "Camera is stopped - can't check thread.");
            } else if (Thread.currentThread() != this.cameraThreadHandler.getLooper().getThread()) {
                throw new IllegalStateException("Wrong thread");
            }
        }
    }

    public static VideoCapturerAndroid create(String str) {
        return create(str, new CameraEvents());
    }

    public static VideoCapturerAndroid create(String str, CameraEventsHandler cameraEventsHandler) {
        return create(str, cameraEventsHandler, false);
    }

    @Deprecated
    public static VideoCapturerAndroid create(String str, CameraEventsHandler cameraEventsHandler, boolean z) {
        try {
            return new VideoCapturerAndroid(str, cameraEventsHandler, z);
        } catch (Throwable e) {
            Logging.m778e(TAG, "Couldn't create camera.", e);
            return null;
        }
    }

    private int getCameraIdWithPosition(int i) {
        int i2 = 0;
        while (i2 < Camera.getNumberOfCameras()) {
            CameraInfo cameraInfo = new CameraInfo();
            try {
                Camera.getCameraInfo(i2, cameraInfo);
                if (cameraInfo.facing == i) {
                    return i2;
                }
                i2++;
            } catch (Throwable e) {
                Logging.m778e(TAG, "getCameraInfo failed on index " + i2, e);
            }
        }
        Logging.m777e(TAG, "Could not find camera with facing position " + i);
        return -1;
    }

    private int getCurrentCameraId() {
        int i;
        synchronized (this.cameraIdLock) {
            i = this.id;
        }
        return i;
    }

    private int getDeviceOrientation() {
        switch (((WindowManager) this.applicationContext.getSystemService("window")).getDefaultDisplay().getRotation()) {
            case 1:
                return 90;
            case 2:
                return 180;
            case 3:
                return 270;
            default:
                return 0;
        }
    }

    private int getFrameOrientation() {
        int deviceOrientation = getDeviceOrientation();
        if (this.info.facing == 0) {
            deviceOrientation = 360 - deviceOrientation;
        }
        return (deviceOrientation + this.info.orientation) % 360;
    }

    private boolean maybePostDelayedOnCameraThread(int i, Runnable runnable) {
        boolean z;
        synchronized (this.handlerLock) {
            z = this.cameraThreadHandler != null && this.cameraThreadHandler.postAtTime(runnable, this, SystemClock.uptimeMillis() + ((long) i));
        }
        return z;
    }

    private boolean maybePostOnCameraThread(Runnable runnable) {
        return maybePostDelayedOnCameraThread(0, runnable);
    }

    private void onOutputFormatRequestOnCameraThread(int i, int i2, int i3) {
        synchronized (this.handlerLock) {
            if (this.cameraThreadHandler == null || this.camera == null) {
                Logging.m777e(TAG, "onOutputFormatRequestOnCameraThread: Camera is stopped");
                return;
            }
            checkIsOnCameraThread();
            Logging.m776d(TAG, "onOutputFormatRequestOnCameraThread: " + i + "x" + i2 + "@" + i3);
            this.frameObserver.onOutputFormatRequest(i, i2, i3);
        }
    }

    private void setCameraPreviewOrientation(int i) {
        CameraInfo cameraInfo = new CameraInfo();
        Camera.getCameraInfo(i, cameraInfo);
        int frameOrientation = getFrameOrientation();
        if (cameraInfo.facing == 1) {
            frameOrientation = (360 - frameOrientation) % 360;
        }
        if (this.camera != null) {
            this.camera.setDisplayOrientation(frameOrientation);
        }
    }

    private void setCameraPreviewSizeOnMainThread(SurfaceHolder surfaceHolder, int i, int i2) {
        if (surfaceHolder == null) {
            Logging.m777e(TAG, "setCameraPreviewSizeOnMainThread: holder is null!");
        } else if (getDeviceOrientation() % 180 == 0) {
            surfaceHolder.setFixedSize(i2, i);
        } else {
            surfaceHolder.setFixedSize(i, i2);
        }
    }

    public static void setLocalPreview(SurfaceHolder surfaceHolder) {
        localPreview = surfaceHolder;
    }

    private void setPreviewDisplayOnCameraThread(SurfaceHolder surfaceHolder) {
        try {
            if (this.camera != null) {
                this.camera.setPreviewDisplay(surfaceHolder);
                synchronized (this.cameraIdLock) {
                    setCameraPreviewOrientation(this.id);
                }
            }
        } catch (IOException e) {
            Logging.m777e(TAG, "Could not set preview display on camera thread.");
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void startCaptureOnCameraThread(int r11, int r12, int r13, org.webrtc.sinch.VideoCapturer.CapturerObserver r14, android.content.Context r15) {
        /*
        r10 = this;
        r9 = 1;
        r8 = 0;
        r1 = r10.handlerLock;
        monitor-enter(r1);
        r0 = r10.cameraThreadHandler;	 Catch:{ all -> 0x0022 }
        if (r0 != 0) goto L_0x0012;
    L_0x0009:
        r0 = "VideoCapturerAndroid";
        r2 = "startCaptureOnCameraThread: Camera is stopped";
        org.webrtc.sinch.Logging.m777e(r0, r2);	 Catch:{ all -> 0x0022 }
        monitor-exit(r1);	 Catch:{ all -> 0x0022 }
    L_0x0011:
        return;
    L_0x0012:
        r10.checkIsOnCameraThread();	 Catch:{ all -> 0x0022 }
        monitor-exit(r1);	 Catch:{ all -> 0x0022 }
        r0 = r10.camera;
        if (r0 == 0) goto L_0x0025;
    L_0x001a:
        r0 = "VideoCapturerAndroid";
        r1 = "startCaptureOnCameraThread: Camera has already been started.";
        org.webrtc.sinch.Logging.m777e(r0, r1);
        goto L_0x0011;
    L_0x0022:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0022 }
        throw r0;
    L_0x0025:
        r10.applicationContext = r15;
        r10.frameObserver = r14;
        r10.firstFrameReported = r8;
        r1 = r10.cameraIdLock;	 Catch:{ RuntimeException -> 0x00fa }
        monitor-enter(r1);	 Catch:{ RuntimeException -> 0x00fa }
        r0 = "VideoCapturerAndroid";
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00f7 }
        r2.<init>();	 Catch:{ all -> 0x00f7 }
        r3 = "Opening camera ";
        r2 = r2.append(r3);	 Catch:{ all -> 0x00f7 }
        r3 = r10.id;	 Catch:{ all -> 0x00f7 }
        r2 = r2.append(r3);	 Catch:{ all -> 0x00f7 }
        r2 = r2.toString();	 Catch:{ all -> 0x00f7 }
        org.webrtc.sinch.Logging.m776d(r0, r2);	 Catch:{ all -> 0x00f7 }
        r0 = r10.eventsHandler;	 Catch:{ all -> 0x00f7 }
        if (r0 == 0) goto L_0x0053;
    L_0x004c:
        r0 = r10.eventsHandler;	 Catch:{ all -> 0x00f7 }
        r2 = r10.id;	 Catch:{ all -> 0x00f7 }
        r0.onCameraOpening(r2);	 Catch:{ all -> 0x00f7 }
    L_0x0053:
        r0 = r10.id;	 Catch:{ all -> 0x00f7 }
        r0 = android.hardware.Camera.open(r0);	 Catch:{ all -> 0x00f7 }
        r10.camera = r0;	 Catch:{ all -> 0x00f7 }
        r0 = new android.hardware.Camera$CameraInfo;	 Catch:{ all -> 0x00f7 }
        r0.<init>();	 Catch:{ all -> 0x00f7 }
        r10.info = r0;	 Catch:{ all -> 0x00f7 }
        r0 = r10.id;	 Catch:{ all -> 0x00f7 }
        r2 = r10.info;	 Catch:{ all -> 0x00f7 }
        android.hardware.Camera.getCameraInfo(r0, r2);	 Catch:{ all -> 0x00f7 }
        monitor-exit(r1);	 Catch:{ all -> 0x00f7 }
        r0 = localPreview;	 Catch:{ RuntimeException -> 0x00dc }
        if (r0 == 0) goto L_0x012e;
    L_0x006e:
        r0 = localPreview;	 Catch:{ IOException -> 0x0120 }
        r0.addCallback(r10);	 Catch:{ IOException -> 0x0120 }
        r0 = localPreview;	 Catch:{ IOException -> 0x0120 }
        r0 = r0.getSurface();	 Catch:{ IOException -> 0x0120 }
        if (r0 == 0) goto L_0x008e;
    L_0x007b:
        r0 = localPreview;	 Catch:{ IOException -> 0x0120 }
        r0 = r0.getSurface();	 Catch:{ IOException -> 0x0120 }
        r0 = r0.isValid();	 Catch:{ IOException -> 0x0120 }
        if (r0 == 0) goto L_0x008e;
    L_0x0087:
        r0 = r10.camera;	 Catch:{ IOException -> 0x0120 }
        r1 = localPreview;	 Catch:{ IOException -> 0x0120 }
        r0.setPreviewDisplay(r1);	 Catch:{ IOException -> 0x0120 }
    L_0x008e:
        r0 = "VideoCapturerAndroid";
        r1 = new java.lang.StringBuilder;	 Catch:{ RuntimeException -> 0x00dc }
        r1.<init>();	 Catch:{ RuntimeException -> 0x00dc }
        r2 = "Camera orientation: ";
        r1 = r1.append(r2);	 Catch:{ RuntimeException -> 0x00dc }
        r2 = r10.info;	 Catch:{ RuntimeException -> 0x00dc }
        r2 = r2.orientation;	 Catch:{ RuntimeException -> 0x00dc }
        r1 = r1.append(r2);	 Catch:{ RuntimeException -> 0x00dc }
        r2 = " .Device orientation: ";
        r1 = r1.append(r2);	 Catch:{ RuntimeException -> 0x00dc }
        r2 = r10.getDeviceOrientation();	 Catch:{ RuntimeException -> 0x00dc }
        r1 = r1.append(r2);	 Catch:{ RuntimeException -> 0x00dc }
        r1 = r1.toString();	 Catch:{ RuntimeException -> 0x00dc }
        org.webrtc.sinch.Logging.m776d(r0, r1);	 Catch:{ RuntimeException -> 0x00dc }
        r0 = r10.camera;	 Catch:{ RuntimeException -> 0x00dc }
        r1 = r10.cameraErrorCallback;	 Catch:{ RuntimeException -> 0x00dc }
        r0.setErrorCallback(r1);	 Catch:{ RuntimeException -> 0x00dc }
        r10.startPreviewOnCameraThread(r11, r12, r13);	 Catch:{ RuntimeException -> 0x00dc }
        r0 = 1;
        r14.onCapturerStarted(r0);	 Catch:{ RuntimeException -> 0x00dc }
        r0 = r10.isCapturingToTexture;	 Catch:{ RuntimeException -> 0x00dc }
        if (r0 == 0) goto L_0x00cf;
    L_0x00ca:
        r0 = r10.surfaceHelper;	 Catch:{ RuntimeException -> 0x00dc }
        r0.startListening(r10);	 Catch:{ RuntimeException -> 0x00dc }
    L_0x00cf:
        r0 = new org.webrtc.sinch.CameraVideoCapturer$CameraStatistics;	 Catch:{ RuntimeException -> 0x00dc }
        r1 = r10.surfaceHelper;	 Catch:{ RuntimeException -> 0x00dc }
        r2 = r10.eventsHandler;	 Catch:{ RuntimeException -> 0x00dc }
        r0.<init>(r1, r2);	 Catch:{ RuntimeException -> 0x00dc }
        r10.cameraStatistics = r0;	 Catch:{ RuntimeException -> 0x00dc }
        goto L_0x0011;
    L_0x00dc:
        r0 = move-exception;
        r1 = "VideoCapturerAndroid";
        r2 = "startCapture failed";
        org.webrtc.sinch.Logging.m778e(r1, r2, r0);
        r10.stopCaptureOnCameraThread(r9);
        r14.onCapturerStarted(r8);
        r0 = r10.eventsHandler;
        if (r0 == 0) goto L_0x0011;
    L_0x00ee:
        r0 = r10.eventsHandler;
        r1 = "Camera can not be started.";
        r0.onCameraError(r1);
        goto L_0x0011;
    L_0x00f7:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x00f7 }
        throw r0;	 Catch:{ RuntimeException -> 0x00fa }
    L_0x00fa:
        r0 = move-exception;
        r1 = r10.openCameraAttempts;	 Catch:{ RuntimeException -> 0x00dc }
        r1 = r1 + 1;
        r10.openCameraAttempts = r1;	 Catch:{ RuntimeException -> 0x00dc }
        r1 = r10.openCameraAttempts;	 Catch:{ RuntimeException -> 0x00dc }
        r2 = 3;
        if (r1 >= r2) goto L_0x011f;
    L_0x0106:
        r1 = "VideoCapturerAndroid";
        r2 = "Camera.open failed, retrying";
        org.webrtc.sinch.Logging.m778e(r1, r2, r0);	 Catch:{ RuntimeException -> 0x00dc }
        r7 = 500; // 0x1f4 float:7.0E-43 double:2.47E-321;
        r0 = new org.webrtc.sinch.VideoCapturerAndroid$6;	 Catch:{ RuntimeException -> 0x00dc }
        r1 = r10;
        r2 = r11;
        r3 = r12;
        r4 = r13;
        r5 = r14;
        r6 = r15;
        r0.<init>(r2, r3, r4, r5, r6);	 Catch:{ RuntimeException -> 0x00dc }
        r10.maybePostDelayedOnCameraThread(r7, r0);	 Catch:{ RuntimeException -> 0x00dc }
        goto L_0x0011;
    L_0x011f:
        throw r0;	 Catch:{ RuntimeException -> 0x00dc }
    L_0x0120:
        r0 = move-exception;
        r1 = "VideoCapturerAndroid";
        r2 = "setPreviewDisplay failed";
        org.webrtc.sinch.Logging.m778e(r1, r2, r0);	 Catch:{ RuntimeException -> 0x00dc }
        r1 = new java.lang.RuntimeException;	 Catch:{ RuntimeException -> 0x00dc }
        r1.<init>(r0);	 Catch:{ RuntimeException -> 0x00dc }
        throw r1;	 Catch:{ RuntimeException -> 0x00dc }
    L_0x012e:
        r0 = r10.camera;	 Catch:{ IOException -> 0x013b }
        r1 = r10.surfaceHelper;	 Catch:{ IOException -> 0x013b }
        r1 = r1.getSurfaceTexture();	 Catch:{ IOException -> 0x013b }
        r0.setPreviewTexture(r1);	 Catch:{ IOException -> 0x013b }
        goto L_0x008e;
    L_0x013b:
        r0 = move-exception;
        r1 = "VideoCapturerAndroid";
        r2 = "setPreviewTexture failed";
        org.webrtc.sinch.Logging.m778e(r1, r2, r0);	 Catch:{ RuntimeException -> 0x00dc }
        r1 = new java.lang.RuntimeException;	 Catch:{ RuntimeException -> 0x00dc }
        r1.<init>(r0);	 Catch:{ RuntimeException -> 0x00dc }
        throw r1;	 Catch:{ RuntimeException -> 0x00dc }
        */
        throw new UnsupportedOperationException("Method not decompiled: org.webrtc.sinch.VideoCapturerAndroid.startCaptureOnCameraThread(int, int, int, org.webrtc.sinch.VideoCapturer$CapturerObserver, android.content.Context):void");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void startPreviewOnCameraThread(int r7, int r8, int r9) {
        /*
        r6 = this;
        r1 = r6.handlerLock;
        monitor-enter(r1);
        r0 = r6.cameraThreadHandler;	 Catch:{ all -> 0x0160 }
        if (r0 == 0) goto L_0x000b;
    L_0x0007:
        r0 = r6.camera;	 Catch:{ all -> 0x0160 }
        if (r0 != 0) goto L_0x0014;
    L_0x000b:
        r0 = "VideoCapturerAndroid";
        r2 = "startPreviewOnCameraThread: Camera is stopped";
        org.webrtc.sinch.Logging.m777e(r0, r2);	 Catch:{ all -> 0x0160 }
        monitor-exit(r1);	 Catch:{ all -> 0x0160 }
    L_0x0013:
        return;
    L_0x0014:
        r6.checkIsOnCameraThread();	 Catch:{ all -> 0x0160 }
        monitor-exit(r1);	 Catch:{ all -> 0x0160 }
        r0 = "VideoCapturerAndroid";
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "startPreviewOnCameraThread requested: ";
        r1 = r1.append(r2);
        r1 = r1.append(r7);
        r2 = "x";
        r1 = r1.append(r2);
        r1 = r1.append(r8);
        r2 = "@";
        r1 = r1.append(r2);
        r1 = r1.append(r9);
        r1 = r1.toString();
        org.webrtc.sinch.Logging.m776d(r0, r1);
        r6.requestedWidth = r7;
        r6.requestedHeight = r8;
        r6.requestedFramerate = r9;
        r0 = r6.camera;
        r0 = r0.getParameters();
        r1 = r0.getSupportedPreviewFpsRange();
        r1 = org.webrtc.sinch.Camera1Enumerator.convertFramerates(r1);
        r2 = "VideoCapturerAndroid";
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "Available fps ranges: ";
        r3 = r3.append(r4);
        r3 = r3.append(r1);
        r3 = r3.toString();
        org.webrtc.sinch.Logging.m776d(r2, r3);
        r1 = org.webrtc.sinch.CameraEnumerationAndroid.getClosestSupportedFramerateRange(r1, r9);
        r2 = r0.getSupportedPreviewSizes();
        r2 = org.webrtc.sinch.Camera1Enumerator.convertSizes(r2);
        r2 = org.webrtc.sinch.CameraEnumerationAndroid.getClosestSupportedSize(r2, r7, r8);
        r3 = new org.webrtc.sinch.CameraEnumerationAndroid$CaptureFormat;
        r4 = r2.width;
        r5 = r2.height;
        r3.<init>(r4, r5, r1);
        r1 = r6.captureFormat;
        r1 = r3.equals(r1);
        if (r1 != 0) goto L_0x0013;
    L_0x0091:
        r1 = "VideoCapturerAndroid";
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "isVideoStabilizationSupported: ";
        r4 = r4.append(r5);
        r5 = r0.isVideoStabilizationSupported();
        r4 = r4.append(r5);
        r4 = r4.toString();
        org.webrtc.sinch.Logging.m776d(r1, r4);
        r1 = r0.isVideoStabilizationSupported();
        if (r1 == 0) goto L_0x00b7;
    L_0x00b3:
        r1 = 1;
        r0.setVideoStabilization(r1);
    L_0x00b7:
        r1 = r3.framerate;
        r1 = r1.max;
        if (r1 <= 0) goto L_0x00c8;
    L_0x00bd:
        r1 = r3.framerate;
        r1 = r1.min;
        r4 = r3.framerate;
        r4 = r4.max;
        r0.setPreviewFpsRange(r1, r4);
    L_0x00c8:
        r1 = r2.width;
        r2 = r2.height;
        r0.setPreviewSize(r1, r2);
        r1 = r6.isCapturingToTexture;
        if (r1 != 0) goto L_0x00db;
    L_0x00d3:
        r3.getClass();
        r1 = 17;
        r0.setPreviewFormat(r1);
    L_0x00db:
        r1 = r0.getSupportedPictureSizes();
        r1 = org.webrtc.sinch.Camera1Enumerator.convertSizes(r1);
        r1 = org.webrtc.sinch.CameraEnumerationAndroid.getClosestSupportedSize(r1, r7, r8);
        r2 = r1.width;
        r1 = r1.height;
        r0.setPictureSize(r2, r1);
        r1 = r6.captureFormat;
        if (r1 == 0) goto L_0x00fd;
    L_0x00f2:
        r1 = r6.camera;
        r1.stopPreview();
        r1 = r6.camera;
        r2 = 0;
        r1.setPreviewCallbackWithBuffer(r2);
    L_0x00fd:
        r1 = "VideoCapturerAndroid";
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r4 = "Start capturing: ";
        r2 = r2.append(r4);
        r2 = r2.append(r3);
        r2 = r2.toString();
        org.webrtc.sinch.Logging.m776d(r1, r2);
        r6.captureFormat = r3;
        r1 = r0.getSupportedFocusModes();
        r2 = "continuous-video";
        r1 = r1.contains(r2);
        if (r1 == 0) goto L_0x0128;
    L_0x0123:
        r1 = "continuous-video";
        r0.setFocusMode(r1);
    L_0x0128:
        r1 = r6.camera;
        r1.setParameters(r0);
        r1 = r6.cameraIdLock;
        monitor-enter(r1);
        r0 = r6.id;	 Catch:{ all -> 0x0163 }
        r6.setCameraPreviewOrientation(r0);	 Catch:{ all -> 0x0163 }
        monitor-exit(r1);	 Catch:{ all -> 0x0163 }
        r0 = r6.isCapturingToTexture;
        if (r0 != 0) goto L_0x016b;
    L_0x013a:
        r0 = r6.queuedBuffers;
        r0.clear();
        r1 = r3.frameSize();
        r0 = 0;
    L_0x0144:
        r2 = 3;
        if (r0 >= r2) goto L_0x0166;
    L_0x0147:
        r2 = java.nio.ByteBuffer.allocateDirect(r1);
        r3 = r6.queuedBuffers;
        r4 = r2.array();
        r3.add(r4);
        r3 = r6.camera;
        r2 = r2.array();
        r3.addCallbackBuffer(r2);
        r0 = r0 + 1;
        goto L_0x0144;
    L_0x0160:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0160 }
        throw r0;
    L_0x0163:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0163 }
        throw r0;
    L_0x0166:
        r0 = r6.camera;
        r0.setPreviewCallbackWithBuffer(r6);
    L_0x016b:
        r0 = r6.camera;
        r0.startPreview();
        goto L_0x0013;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.webrtc.sinch.VideoCapturerAndroid.startPreviewOnCameraThread(int, int, int):void");
    }

    private void stopCaptureOnCameraThread(boolean z) {
        synchronized (this.handlerLock) {
            if (this.cameraThreadHandler == null) {
                Logging.m777e(TAG, "stopCaptureOnCameraThread: Camera is stopped");
            } else {
                checkIsOnCameraThread();
            }
        }
        Logging.m776d(TAG, "stopCaptureOnCameraThread");
        if (this.surfaceHelper != null) {
            this.surfaceHelper.stopListening();
        }
        if (z) {
            synchronized (this.handlerLock) {
                if (this.cameraThreadHandler != null) {
                    this.cameraThreadHandler.removeCallbacksAndMessages(this);
                    this.cameraThreadHandler = null;
                }
                this.surfaceHelper = null;
            }
        }
        if (this.cameraStatistics != null) {
            this.cameraStatistics.release();
            this.cameraStatistics = null;
        }
        Logging.m776d(TAG, "Stop preview.");
        if (this.camera != null) {
            this.camera.stopPreview();
            this.camera.setPreviewCallbackWithBuffer(null);
        }
        this.queuedBuffers.clear();
        this.captureFormat = null;
        if (!(localPreview == null || this.camera == null)) {
            try {
                localPreview.removeCallback(this);
                this.camera.setPreviewDisplay(null);
            } catch (IOException e) {
                Logging.m777e(TAG, "Could not clear preview display.");
            }
        }
        Logging.m776d(TAG, "Release camera.");
        if (this.camera != null) {
            this.camera.release();
            this.camera = null;
        }
        if (this.eventsHandler != null) {
            this.eventsHandler.onCameraClosed();
        }
        Logging.m776d(TAG, "stopCaptureOnCameraThread done");
    }

    private void switchCameraOnCameraThread() {
        synchronized (this.handlerLock) {
            if (this.cameraThreadHandler == null) {
                Logging.m777e(TAG, "switchCameraOnCameraThread: Camera is stopped");
                return;
            }
            checkIsOnCameraThread();
            Logging.m776d(TAG, "switchCameraOnCameraThread");
            stopCaptureOnCameraThread(false);
            synchronized (this.cameraIdLock) {
                this.id = (this.id + 1) % Camera.getNumberOfCameras();
            }
            startCaptureOnCameraThread(this.requestedWidth, this.requestedHeight, this.requestedFramerate, this.frameObserver, this.applicationContext);
            Logging.m776d(TAG, "switchCameraOnCameraThread done");
        }
    }

    private void switchCameraOnCameraThreadToId(int i) {
        synchronized (this.handlerLock) {
            if (this.cameraThreadHandler == null) {
                Logging.m777e(TAG, "switchCameraOnCameraThread: Camera is stopped");
                return;
            }
            checkIsOnCameraThread();
            Logging.m776d(TAG, "switchCameraOnCameraThreadToId");
            stopCaptureOnCameraThread(false);
            synchronized (this.cameraIdLock) {
                this.id = i;
            }
            startCaptureOnCameraThread(this.requestedWidth, this.requestedHeight, this.requestedFramerate, this.frameObserver, this.applicationContext);
            Logging.m776d(TAG, "switchCameraOnCameraThreadToId done");
        }
    }

    private synchronized boolean switchToCameraId(final int i) {
        boolean z = false;
        synchronized (this) {
            if (Camera.getNumberOfCameras() >= 2) {
                synchronized (this.pendingCameraSwitchLock) {
                    if (this.pendingCameraSwitch) {
                        Logging.m780w(TAG, "Ignoring camera switch request.");
                    } else {
                        this.pendingCameraSwitch = true;
                        z = maybePostOnCameraThread(new Runnable() {
                            public void run() {
                                if (VideoCapturerAndroid.this.camera == null) {
                                    Logging.m780w(VideoCapturerAndroid.TAG, "Cannot switch, camera is stopped.");
                                    return;
                                }
                                VideoCapturerAndroid.this.switchCameraOnCameraThreadToId(i);
                                synchronized (VideoCapturerAndroid.this.pendingCameraSwitchLock) {
                                    VideoCapturerAndroid.this.pendingCameraSwitch = false;
                                }
                            }
                        });
                    }
                }
            }
        }
        return z;
    }

    public void changeCaptureFormat(final int i, final int i2, final int i3) {
        maybePostOnCameraThread(new Runnable() {
            public void run() {
                VideoCapturerAndroid.this.startPreviewOnCameraThread(i, i2, i3);
            }
        });
    }

    public void dispose() {
        Logging.m776d(TAG, "dispose");
    }

    Handler getCameraThreadHandler() {
        return this.cameraThreadHandler;
    }

    public List<CaptureFormat> getSupportedFormats() {
        return Camera1Enumerator.getSupportedFormats(getCurrentCameraId());
    }

    public boolean isCapturingToTexture() {
        return this.isCapturingToTexture;
    }

    public void onOutputFormatRequest(final int i, final int i2, final int i3) {
        maybePostOnCameraThread(new Runnable() {
            public void run() {
                VideoCapturerAndroid.this.onOutputFormatRequestOnCameraThread(i, i2, i3);
            }
        });
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onPreviewFrame(byte[] r9, android.hardware.Camera r10) {
        /*
        r8 = this;
        r1 = r8.handlerLock;
        monitor-enter(r1);
        r0 = r8.cameraThreadHandler;	 Catch:{ all -> 0x0028 }
        if (r0 != 0) goto L_0x0010;
    L_0x0007:
        r0 = "VideoCapturerAndroid";
        r2 = "onPreviewFrame: Camera is stopped";
        org.webrtc.sinch.Logging.m777e(r0, r2);	 Catch:{ all -> 0x0028 }
        monitor-exit(r1);	 Catch:{ all -> 0x0028 }
    L_0x000f:
        return;
    L_0x0010:
        r8.checkIsOnCameraThread();	 Catch:{ all -> 0x0028 }
        monitor-exit(r1);	 Catch:{ all -> 0x0028 }
        r0 = r8.queuedBuffers;
        r0 = r0.contains(r9);
        if (r0 == 0) goto L_0x000f;
    L_0x001c:
        r0 = r8.camera;
        if (r0 == r10) goto L_0x002b;
    L_0x0020:
        r0 = new java.lang.RuntimeException;
        r1 = "Unexpected camera in callback!";
        r0.<init>(r1);
        throw r0;
    L_0x0028:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0028 }
        throw r0;
    L_0x002b:
        r0 = java.util.concurrent.TimeUnit.MILLISECONDS;
        r2 = android.os.SystemClock.elapsedRealtime();
        r6 = r0.toNanos(r2);
        r0 = r8.eventsHandler;
        if (r0 == 0) goto L_0x0045;
    L_0x0039:
        r0 = r8.firstFrameReported;
        if (r0 != 0) goto L_0x0045;
    L_0x003d:
        r0 = r8.eventsHandler;
        r0.onFirstFrameAvailable();
        r0 = 1;
        r8.firstFrameReported = r0;
    L_0x0045:
        r0 = r8.cameraStatistics;
        r0.addFrame();
        r1 = r8.frameObserver;
        r0 = r8.captureFormat;
        r3 = r0.width;
        r0 = r8.captureFormat;
        r4 = r0.height;
        r5 = r8.getFrameOrientation();
        r2 = r9;
        r1.onByteBufferFrameCaptured(r2, r3, r4, r5, r6);
        r0 = r8.camera;
        r0.addCallbackBuffer(r9);
        goto L_0x000f;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.webrtc.sinch.VideoCapturerAndroid.onPreviewFrame(byte[], android.hardware.Camera):void");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onTextureFrameAvailable(int r10, float[] r11, long r12) {
        /*
        r9 = this;
        r2 = 1;
        r1 = r9.handlerLock;
        monitor-enter(r1);
        r0 = r9.cameraThreadHandler;	 Catch:{ all -> 0x0050 }
        if (r0 != 0) goto L_0x0016;
    L_0x0008:
        r0 = "VideoCapturerAndroid";
        r2 = "onTextureFrameAvailable: Camera is stopped";
        org.webrtc.sinch.Logging.m777e(r0, r2);	 Catch:{ all -> 0x0050 }
        r0 = r9.surfaceHelper;	 Catch:{ all -> 0x0050 }
        r0.returnTextureFrame();	 Catch:{ all -> 0x0050 }
        monitor-exit(r1);	 Catch:{ all -> 0x0050 }
    L_0x0015:
        return;
    L_0x0016:
        r9.checkIsOnCameraThread();	 Catch:{ all -> 0x0050 }
        monitor-exit(r1);	 Catch:{ all -> 0x0050 }
        r0 = r9.eventsHandler;
        if (r0 == 0) goto L_0x0029;
    L_0x001e:
        r0 = r9.firstFrameReported;
        if (r0 != 0) goto L_0x0029;
    L_0x0022:
        r0 = r9.eventsHandler;
        r0.onFirstFrameAvailable();
        r9.firstFrameReported = r2;
    L_0x0029:
        r5 = r9.getFrameOrientation();
        r0 = r9.info;
        r0 = r0.facing;
        if (r0 != r2) goto L_0x0053;
    L_0x0033:
        r0 = org.webrtc.sinch.RendererCommon.horizontalFlipMatrix();
        r4 = org.webrtc.sinch.RendererCommon.multiplyMatrices(r11, r0);
    L_0x003b:
        r0 = r9.cameraStatistics;
        r0.addFrame();
        r0 = r9.frameObserver;
        r1 = r9.captureFormat;
        r1 = r1.width;
        r2 = r9.captureFormat;
        r2 = r2.height;
        r3 = r10;
        r6 = r12;
        r0.onTextureFrameCaptured(r1, r2, r3, r4, r5, r6);
        goto L_0x0015;
    L_0x0050:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0050 }
        throw r0;
    L_0x0053:
        r4 = r11;
        goto L_0x003b;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.webrtc.sinch.VideoCapturerAndroid.onTextureFrameAvailable(int, float[], long):void");
    }

    public void printStackTrace() {
        Thread thread = null;
        synchronized (this.handlerLock) {
            if (this.cameraThreadHandler != null) {
                thread = this.cameraThreadHandler.getLooper().getThread();
            }
        }
        if (thread != null) {
            StackTraceElement[] stackTrace = thread.getStackTrace();
            if (stackTrace.length > 0) {
                Logging.m776d(TAG, "VideoCapturerAndroid stacks trace:");
                for (StackTraceElement stackTraceElement : stackTrace) {
                    Logging.m776d(TAG, stackTraceElement.toString());
                }
            }
        }
    }

    public void startCapture(int i, int i2, int i3, SurfaceTextureHelper surfaceTextureHelper, Context context, CapturerObserver capturerObserver) {
        Logging.m776d(TAG, "startCapture requested: " + i + "x" + i2 + "@" + i3);
        if (surfaceTextureHelper == null) {
            capturerObserver.onCapturerStarted(false);
            if (this.eventsHandler != null) {
                this.eventsHandler.onCameraError("No SurfaceTexture created.");
            }
        } else if (context == null) {
            throw new IllegalArgumentException("applicationContext not set.");
        } else if (capturerObserver == null) {
            throw new IllegalArgumentException("frameObserver not set.");
        } else {
            synchronized (this.handlerLock) {
                if (this.cameraThreadHandler != null) {
                    throw new RuntimeException("Camera has already been started.");
                }
                this.cameraThreadHandler = surfaceTextureHelper.getHandler();
                this.surfaceHelper = surfaceTextureHelper;
                final int i4 = i;
                final int i5 = i2;
                final int i6 = i3;
                final CapturerObserver capturerObserver2 = capturerObserver;
                final Context context2 = context;
                if (!maybePostOnCameraThread(new Runnable() {
                    public void run() {
                        VideoCapturerAndroid.this.openCameraAttempts = 0;
                        VideoCapturerAndroid.this.startCaptureOnCameraThread(i4, i5, i6, capturerObserver2, context2);
                    }
                })) {
                    capturerObserver.onCapturerStarted(false);
                    if (this.eventsHandler != null) {
                        this.eventsHandler.onCameraError("Could not post task to camera thread.");
                    }
                }
            }
        }
    }

    public void stopCapture() {
        Logging.m776d(TAG, "stopCapture");
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        if (maybePostOnCameraThread(new Runnable() {
            public void run() {
                VideoCapturerAndroid.this.stopCaptureOnCameraThread(true);
                countDownLatch.countDown();
            }
        })) {
            if (!countDownLatch.await(7000, TimeUnit.MILLISECONDS)) {
                Logging.m777e(TAG, "Camera stop timeout");
                printStackTrace();
                if (this.eventsHandler != null) {
                    this.eventsHandler.onCameraError("Camera stop timeout");
                }
            }
            Logging.m776d(TAG, "stopCapture done");
            return;
        }
        Logging.m777e(TAG, "Calling stopCapture() for already stopped camera.");
    }

    public synchronized void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        Logging.m776d(TAG, "VideoCapturerAndroid::surfaceChanged ignored: " + i + ": " + i2 + "x" + i3);
    }

    public synchronized void surfaceCreated(final SurfaceHolder surfaceHolder) {
        Logging.m776d(TAG, "VideoCapturerAndroid::surfaceCreated");
        if (this.cameraThreadHandler != null) {
            maybePostOnCameraThread(new Runnable() {
                public void run() {
                    if (VideoCapturerAndroid.this.captureFormat != null) {
                        final int i = VideoCapturerAndroid.this.captureFormat.width;
                        final int i2 = VideoCapturerAndroid.this.captureFormat.height;
                        new Handler(VideoCapturerAndroid.this.applicationContext.getMainLooper()).post(new Runnable() {
                            public void run() {
                                VideoCapturerAndroid.this.setCameraPreviewSizeOnMainThread(surfaceHolder, i, i2);
                            }
                        });
                    } else {
                        Logging.m777e(VideoCapturerAndroid.TAG, "Cannot set camera preview size: captureFormat is null.");
                    }
                    VideoCapturerAndroid.this.setPreviewDisplayOnCameraThread(surfaceHolder);
                    VideoCapturerAndroid.this.queuedBuffers.clear();
                    if (VideoCapturerAndroid.this.camera == null) {
                        Logging.m776d(VideoCapturerAndroid.TAG, "Camera is null, restarting.");
                        VideoCapturerAndroid.this.stopCaptureOnCameraThread(true);
                        VideoCapturerAndroid.this.startCaptureOnCameraThread(VideoCapturerAndroid.this.requestedWidth, VideoCapturerAndroid.this.requestedHeight, VideoCapturerAndroid.this.requestedFramerate, VideoCapturerAndroid.this.frameObserver, VideoCapturerAndroid.this.applicationContext);
                    }
                    if (VideoCapturerAndroid.this.camera != null) {
                        i2 = VideoCapturerAndroid.this.captureFormat.frameSize();
                        for (i = 0; i < 3; i++) {
                            ByteBuffer allocateDirect = ByteBuffer.allocateDirect(i2);
                            VideoCapturerAndroid.this.queuedBuffers.add(allocateDirect.array());
                            VideoCapturerAndroid.this.camera.addCallbackBuffer(allocateDirect.array());
                        }
                        VideoCapturerAndroid.this.camera.setPreviewCallbackWithBuffer(VideoCapturerAndroid.this);
                        VideoCapturerAndroid.this.camera.startPreview();
                        return;
                    }
                    Logging.m777e(VideoCapturerAndroid.TAG, "Cannot start camera preview after surface created: camera not initialized.");
                }
            });
        }
    }

    public synchronized void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Logging.m776d(TAG, "VideoCapturerAndroid::surfaceDestroyed");
        if (!(this.camera == null || this.cameraThreadHandler == null)) {
            maybePostOnCameraThread(new C07269());
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void switchCamera(final org.webrtc.sinch.CameraVideoCapturer.CameraSwitchHandler r4) {
        /*
        r3 = this;
        r0 = android.hardware.Camera.getNumberOfCameras();
        r1 = 2;
        if (r0 >= r1) goto L_0x000f;
    L_0x0007:
        if (r4 == 0) goto L_0x000e;
    L_0x0009:
        r0 = "No camera to switch to.";
        r4.onCameraSwitchError(r0);
    L_0x000e:
        return;
    L_0x000f:
        r1 = r3.pendingCameraSwitchLock;
        monitor-enter(r1);
        r0 = r3.pendingCameraSwitch;	 Catch:{ all -> 0x0026 }
        if (r0 == 0) goto L_0x0029;
    L_0x0016:
        r0 = "VideoCapturerAndroid";
        r2 = "Ignoring camera switch request.";
        org.webrtc.sinch.Logging.m780w(r0, r2);	 Catch:{ all -> 0x0026 }
        if (r4 == 0) goto L_0x0024;
    L_0x001f:
        r0 = "Pending camera switch already in progress.";
        r4.onCameraSwitchError(r0);	 Catch:{ all -> 0x0026 }
    L_0x0024:
        monitor-exit(r1);	 Catch:{ all -> 0x0026 }
        goto L_0x000e;
    L_0x0026:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0026 }
        throw r0;
    L_0x0029:
        r0 = 1;
        r3.pendingCameraSwitch = r0;	 Catch:{ all -> 0x0026 }
        monitor-exit(r1);	 Catch:{ all -> 0x0026 }
        r0 = new org.webrtc.sinch.VideoCapturerAndroid$2;
        r0.<init>(r4);
        r0 = r3.maybePostOnCameraThread(r0);
        if (r0 != 0) goto L_0x000e;
    L_0x0038:
        if (r4 == 0) goto L_0x000e;
    L_0x003a:
        r0 = "Camera is stopped.";
        r4.onCameraSwitchError(r0);
        goto L_0x000e;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.webrtc.sinch.VideoCapturerAndroid.switchCamera(org.webrtc.sinch.CameraVideoCapturer$CameraSwitchHandler):void");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void switchToCameraPosition(int r6) {
        /*
        r5 = this;
        r1 = r5.cameraIdLock;
        monitor-enter(r1);
        r0 = r5.getCameraIdWithPosition(r6);	 Catch:{ all -> 0x0030 }
        r2 = -1;
        if (r0 == r2) goto L_0x002e;
    L_0x000a:
        r2 = r5.id;	 Catch:{ all -> 0x0030 }
        if (r0 != r2) goto L_0x0010;
    L_0x000e:
        monitor-exit(r1);	 Catch:{ all -> 0x0030 }
    L_0x000f:
        return;
    L_0x0010:
        r2 = r5.switchToCameraId(r0);	 Catch:{ all -> 0x0030 }
        if (r2 != 0) goto L_0x002e;
    L_0x0016:
        r2 = "VideoCapturerAndroid";
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0030 }
        r3.<init>();	 Catch:{ all -> 0x0030 }
        r4 = "Could not switch to camera with id ";
        r3 = r3.append(r4);	 Catch:{ all -> 0x0030 }
        r0 = r3.append(r0);	 Catch:{ all -> 0x0030 }
        r0 = r0.toString();	 Catch:{ all -> 0x0030 }
        org.webrtc.sinch.Logging.m777e(r2, r0);	 Catch:{ all -> 0x0030 }
    L_0x002e:
        monitor-exit(r1);	 Catch:{ all -> 0x0030 }
        goto L_0x000f;
    L_0x0030:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0030 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.webrtc.sinch.VideoCapturerAndroid.switchToCameraPosition(int):void");
    }
}
