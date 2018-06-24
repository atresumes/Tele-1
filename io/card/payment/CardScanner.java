package io.card.payment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Build.VERSION;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.WindowManager;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

class CardScanner implements AutoFocusCallback, PreviewCallback, Callback {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static final String TAG = CardScanner.class.getSimpleName();
    private static boolean manualFallbackForError;
    private static boolean processingInProgress = false;
    private long captureStart;
    private Bitmap detectedBitmap;
    private int frameCount = 0;
    private boolean isSurfaceValid;
    private long mAutoFocusCompletedAt;
    private long mAutoFocusStartedAt;
    private Camera mCamera;
    private boolean mFirstPreviewFrame = true;
    private int mFrameOrientation = 1;
    private byte[] mPreviewBuffer;
    final int mPreviewHeight = 480;
    final int mPreviewWidth = 640;
    protected WeakReference<CardIOActivity> mScanActivityRef;
    private boolean mScanExpiry;
    private boolean mSuppressScan = false;
    private int numAutoRefocus;
    private int numFramesSkipped;
    private int numManualRefocus;
    private int numManualTorchChange;
    protected boolean useCamera = true;

    private native void nCleanup();

    private native void nGetGuideFrame(int i, int i2, int i3, Rect rect);

    private native int nGetNumFramesScanned();

    private native void nResetAnalytics();

    private native void nScanFrame(byte[] bArr, int i, int i2, int i3, DetectionInfo detectionInfo, Bitmap bitmap, boolean z);

    private native void nSetup(boolean z, float f);

    public static native boolean nUseNeon();

    public static native boolean nUseTegra();

    public static native boolean nUseX86();

    static {
        boolean z;
        if (CardScanner.class.desiredAssertionStatus()) {
            z = false;
        } else {
            z = true;
        }
        $assertionsDisabled = z;
        Log.i("card.io", "card.io 5.3.4 04/22/2016 15:06:12 -0500");
        try {
            System.loadLibrary("cardioDecider");
            Log.d("card.io", "Loaded card.io decider library.");
            Log.d("card.io", "    nUseNeon(): " + nUseNeon());
            Log.d("card.io", "    nUseTegra():" + nUseTegra());
            Log.d("card.io", "    nUseX86():  " + nUseX86());
            if (usesSupportedProcessorArch()) {
                System.loadLibrary("opencv_core");
                Log.d("card.io", "Loaded opencv core library");
                System.loadLibrary("opencv_imgproc");
                Log.d("card.io", "Loaded opencv imgproc library");
            }
            if (nUseNeon()) {
                System.loadLibrary("cardioRecognizer");
                Log.i("card.io", "Loaded card.io NEON library");
            } else if (nUseX86()) {
                System.loadLibrary("cardioRecognizer");
                Log.i("card.io", "Loaded card.io x86 library");
            } else if (nUseTegra()) {
                System.loadLibrary("cardioRecognizer_tegra2");
                Log.i("card.io", "Loaded card.io Tegra2 library");
            } else {
                Log.w("card.io", "unsupported processor - card.io scanning requires ARMv7 or x86 architecture");
                manualFallbackForError = true;
            }
        } catch (UnsatisfiedLinkError e) {
            Log.e("card.io", "Failed to load native library: " + e.getMessage());
            manualFallbackForError = true;
        }
    }

    private static boolean usesSupportedProcessorArch() {
        return nUseNeon() || nUseTegra() || nUseX86();
    }

    static boolean processorSupported() {
        return !manualFallbackForError && usesSupportedProcessorArch();
    }

    CardScanner(CardIOActivity scanActivity, int currentFrameOrientation) {
        boolean z = true;
        Intent scanIntent = scanActivity.getIntent();
        if (scanIntent != null) {
            this.mSuppressScan = scanIntent.getBooleanExtra(CardIOActivity.EXTRA_SUPPRESS_SCAN, false);
            if (!(scanIntent.getBooleanExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, false) && scanIntent.getBooleanExtra(CardIOActivity.EXTRA_SCAN_EXPIRY, true))) {
                z = false;
            }
            this.mScanExpiry = z;
        }
        this.mScanActivityRef = new WeakReference(scanActivity);
        this.mFrameOrientation = currentFrameOrientation;
        nSetup(this.mSuppressScan, 6.0f);
    }

    private Camera connectToCamera(int checkInterval, int maxTimeout) {
        long start = System.currentTimeMillis();
        if (this.useCamera) {
            do {
                try {
                    return Camera.open();
                } catch (RuntimeException e) {
                    try {
                        Log.w("card.io", "Wasn't able to connect to camera service. Waiting and trying again...");
                        Thread.sleep((long) checkInterval);
                    } catch (InterruptedException e1) {
                        Log.e("card.io", "Interrupted while waiting for camera", e1);
                    }
                } catch (Exception e2) {
                    Log.e("card.io", "Unexpected exception. Please report it to support@card.io", e2);
                    maxTimeout = 0;
                }
            } while (System.currentTimeMillis() - start >= ((long) maxTimeout));
            Log.w(TAG, "camera connect timeout");
            return null;
        }
        Log.w(TAG, "camera connect timeout");
        return null;
        if (System.currentTimeMillis() - start >= ((long) maxTimeout)) {
            Log.w(TAG, "camera connect timeout");
            return null;
        }
    }

    void prepareScanner() {
        Log.v(TAG, "prepareScanner()");
        this.mFirstPreviewFrame = true;
        this.mAutoFocusStartedAt = 0;
        this.mAutoFocusCompletedAt = 0;
        this.numManualRefocus = 0;
        this.numAutoRefocus = 0;
        this.numManualTorchChange = 0;
        this.numFramesSkipped = 0;
        if (this.useCamera && this.mCamera == null) {
            this.mCamera = connectToCamera(50, 5000);
            if (this.mCamera == null) {
                Log.e("card.io", "prepare scanner couldn't connect to camera!");
                return;
            }
            Log.v(TAG, "camera is connected");
            setCameraDisplayOrientation(this.mCamera);
            Parameters parameters = this.mCamera.getParameters();
            List<Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
            if (supportedPreviewSizes != null) {
                Size previewSize = null;
                for (Size s : supportedPreviewSizes) {
                    if (s.width == 640 && s.height == 480) {
                        previewSize = s;
                        break;
                    }
                }
                if (previewSize == null) {
                    Log.w("card.io", "Didn't find a supported 640x480 resolution, so forcing");
                    previewSize = (Size) supportedPreviewSizes.get(0);
                    previewSize.width = 640;
                    previewSize.height = 480;
                }
            }
            Log.d(TAG, "- parameters: " + parameters);
            parameters.setPreviewSize(640, 480);
            this.mCamera.setParameters(parameters);
        } else if (!this.useCamera) {
            Log.w(TAG, "useCamera is false!");
        } else if (this.mCamera != null) {
            Log.v(TAG, "we already have a camera instance: " + this.mCamera);
        }
        if (this.detectedBitmap == null) {
            this.detectedBitmap = Bitmap.createBitmap(428, 270, Config.ARGB_8888);
        }
    }

    boolean resumeScanning(SurfaceHolder holder) {
        Log.v(TAG, "resumeScanning(" + holder + ")");
        if (this.mCamera == null) {
            Log.v(TAG, "preparing the scanner...");
            prepareScanner();
            Log.v(TAG, "preparations complete");
        }
        if (this.useCamera && this.mCamera == null) {
            Log.i(TAG, "null camera. failure");
            return false;
        } else if ($assertionsDisabled || holder != null) {
            if (this.useCamera && this.mPreviewBuffer == null) {
                Log.v(TAG, "- mCamera:" + this.mCamera);
                int previewFormat = this.mCamera.getParameters().getPreviewFormat();
                Log.v(TAG, "- preview format: " + previewFormat);
                int bytesPerPixel = ImageFormat.getBitsPerPixel(previewFormat) / 8;
                Log.v(TAG, "- bytes per pixel: " + bytesPerPixel);
                int bufferSize = (307200 * bytesPerPixel) * 3;
                Log.v(TAG, "- buffer size: " + bufferSize);
                this.mPreviewBuffer = new byte[bufferSize];
                this.mCamera.addCallbackBuffer(this.mPreviewBuffer);
            }
            holder.addCallback(this);
            holder.setType(3);
            if (this.useCamera) {
                this.mCamera.setPreviewCallbackWithBuffer(this);
            }
            if (this.isSurfaceValid) {
                makePreviewGo(holder);
            }
            setFlashOn(false);
            this.captureStart = System.currentTimeMillis();
            nResetAnalytics();
            return true;
        } else {
            throw new AssertionError();
        }
    }

    public void pauseScanning() {
        setFlashOn(false);
        if (this.mCamera != null) {
            try {
                this.mCamera.stopPreview();
                this.mCamera.setPreviewDisplay(null);
            } catch (IOException e) {
                Log.w("card.io", "can't stop preview display", e);
            }
            this.mCamera.setPreviewCallback(null);
            this.mCamera.release();
            this.mPreviewBuffer = null;
            Log.d(TAG, "- released camera");
            this.mCamera = null;
        }
        Log.i(TAG, "scan paused");
    }

    public void endScanning() {
        if (this.mCamera != null) {
            pauseScanning();
        }
        nCleanup();
        this.mPreviewBuffer = null;
    }

    private boolean makePreviewGo(SurfaceHolder holder) {
        if (!$assertionsDisabled && holder == null) {
            throw new AssertionError();
        } else if ($assertionsDisabled || holder.getSurface() != null) {
            Log.d(TAG, "surfaceFrame: " + String.valueOf(holder.getSurfaceFrame()));
            this.mFirstPreviewFrame = true;
            if (this.useCamera) {
                try {
                    this.mCamera.setPreviewDisplay(holder);
                    try {
                        this.mCamera.startPreview();
                        this.mCamera.autoFocus(this);
                        Log.d(TAG, "startPreview success");
                    } catch (RuntimeException e) {
                        Log.e("card.io", "startPreview failed on camera. Error: ", e);
                        return false;
                    }
                } catch (IOException e2) {
                    Log.e("card.io", "can't set preview display", e2);
                    return false;
                }
            }
            return true;
        } else {
            throw new AssertionError();
        }
    }

    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "Preview.surfaceCreated()");
        if (this.mCamera == null && this.useCamera) {
            Log.wtf("card.io", "CardScanner.surfaceCreated() - camera is null!");
            return;
        }
        this.isSurfaceValid = true;
        makePreviewGo(holder);
        Log.d(TAG, "Preview.surfaceCreated(), surface is valid");
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        boolean z;
        String str = TAG;
        String str2 = "Preview.surfaceChanged(holder?:%b, f:%d, w:%d, h:%d )";
        Object[] objArr = new Object[4];
        if (holder != null) {
            z = true;
        } else {
            z = false;
        }
        objArr[0] = Boolean.valueOf(z);
        objArr[1] = Integer.valueOf(format);
        objArr[2] = Integer.valueOf(width);
        objArr[3] = Integer.valueOf(height);
        Log.d(str, String.format(str2, objArr));
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surfaceDestroyed()");
        if (this.mCamera != null) {
            try {
                this.mCamera.stopPreview();
            } catch (Exception e) {
                Log.e("card.io", "error stopping camera", e);
            }
        }
        this.isSurfaceValid = false;
    }

    public void onPreviewFrame(byte[] data, Camera camera) {
        boolean sufficientFocus = true;
        if (data == null) {
            Log.w(TAG, "frame is null! skipping");
        } else if (processingInProgress) {
            Log.e(TAG, "processing in progress.... dropping frame");
            this.numFramesSkipped++;
            if (camera != null) {
                camera.addCallbackBuffer(data);
            }
        } else {
            processingInProgress = true;
            if (this.mFirstPreviewFrame) {
                Log.d(TAG, "mFirstPreviewFrame");
                this.mFirstPreviewFrame = false;
                this.mFrameOrientation = 1;
                ((CardIOActivity) this.mScanActivityRef.get()).onFirstFrame(1);
            }
            DetectionInfo dInfo = new DetectionInfo();
            nScanFrame(data, 640, 480, this.mFrameOrientation, dInfo, this.detectedBitmap, this.mScanExpiry);
            if (dInfo.focusScore < 6.0f) {
                sufficientFocus = false;
            }
            if (!sufficientFocus) {
                triggerAutoFocus(false);
            } else if (dInfo.predicted() || (this.mSuppressScan && dInfo.detected())) {
                Log.d(TAG, "detected card: " + dInfo.creditCard());
                ((CardIOActivity) this.mScanActivityRef.get()).onCardDetected(this.detectedBitmap, dInfo);
            }
            if (camera != null) {
                camera.addCallbackBuffer(data);
            }
            processingInProgress = false;
        }
    }

    void onEdgeUpdate(DetectionInfo dInfo) {
        ((CardIOActivity) this.mScanActivityRef.get()).onEdgeUpdate(dInfo);
    }

    Rect getGuideFrame(int orientation, int previewWidth, int previewHeight) {
        if (!processorSupported()) {
            return null;
        }
        Rect r = new Rect();
        nGetGuideFrame(orientation, previewWidth, previewHeight, r);
        return r;
    }

    Rect getGuideFrame(int width, int height) {
        return getGuideFrame(this.mFrameOrientation, width, height);
    }

    void setDeviceOrientation(int orientation) {
        this.mFrameOrientation = orientation;
    }

    public void onAutoFocus(boolean success, Camera camera) {
        this.mAutoFocusCompletedAt = System.currentTimeMillis();
    }

    boolean isAutoFocusing() {
        return this.mAutoFocusCompletedAt < this.mAutoFocusStartedAt;
    }

    void triggerAutoFocus(boolean isManual) {
        if (this.useCamera && !isAutoFocusing()) {
            try {
                this.mAutoFocusStartedAt = System.currentTimeMillis();
                this.mCamera.autoFocus(this);
                if (isManual) {
                    this.numManualRefocus++;
                } else {
                    this.numAutoRefocus++;
                }
            } catch (RuntimeException e) {
                Log.w(TAG, "could not trigger auto focus: " + e);
            }
        }
    }

    public boolean isFlashOn() {
        if (this.useCamera) {
            return this.mCamera.getParameters().getFlashMode().equals("torch");
        }
        return false;
    }

    public boolean setFlashOn(boolean b) {
        if (this.mCamera != null) {
            Log.d(TAG, "setFlashOn: " + b);
            try {
                Parameters params = this.mCamera.getParameters();
                params.setFlashMode(b ? "torch" : "off");
                this.mCamera.setParameters(params);
                this.numManualTorchChange++;
                return true;
            } catch (RuntimeException e) {
                Log.w(TAG, "Could not set flash mode: " + e);
            }
        }
        return false;
    }

    private void setCameraDisplayOrientation(Camera mCamera) {
        int result;
        if (VERSION.SDK_INT >= 21) {
            CameraInfo info = new CameraInfo();
            Camera.getCameraInfo(0, info);
            result = ((info.orientation - getRotationalOffset()) + 360) % 360;
        } else {
            result = 90;
        }
        mCamera.setDisplayOrientation(result);
    }

    int getRotationalOffset() {
        int naturalOrientation = ((WindowManager) ((CardIOActivity) this.mScanActivityRef.get()).getSystemService("window")).getDefaultDisplay().getRotation();
        if (naturalOrientation == 0) {
            return 0;
        }
        if (naturalOrientation == 1) {
            return 90;
        }
        if (naturalOrientation == 2) {
            return 180;
        }
        if (naturalOrientation == 3) {
            return 270;
        }
        return 0;
    }
}
