package org.webrtc.sinch;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.graphics.Point;
import android.opengl.GLES20;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View.MeasureSpec;
import org.webrtc.sinch.RendererCommon.GlDrawer;
import org.webrtc.sinch.RendererCommon.RendererEvents;
import org.webrtc.sinch.RendererCommon.ScalingType;
import org.webrtc.sinch.RendererCommon.YuvUploader;
import org.webrtc.sinch.VideoRenderer.Callbacks;
import org.webrtc.sinch.VideoRenderer.I420Frame;

public class SurfaceViewRenderer extends SurfaceView implements Callback, Callbacks {
    private static final String TAG = "SurfaceViewRenderer";
    private Point desiredLayoutSize = new Point();
    private GlDrawer drawer;
    private EglBase eglBase;
    private long firstFrameTimeNs;
    private int frameHeight;
    private final Object frameLock = new Object();
    private int frameRotation;
    private int frameWidth;
    private int framesDropped;
    private int framesReceived;
    private int framesRendered;
    private final Object handlerLock = new Object();
    private boolean isSurfaceCreated;
    private final Object layoutLock = new Object();
    private final Point layoutSize = new Point();
    private final Runnable makeBlackRunnable = new C07102();
    private boolean mirror;
    private I420Frame pendingFrame;
    private final Runnable renderFrameRunnable = new C07091();
    private HandlerThread renderThread;
    private Handler renderThreadHandler;
    private long renderTimeNs;
    private RendererEvents rendererEvents;
    private ScalingType scalingType = ScalingType.SCALE_ASPECT_BALANCED;
    private final Object statisticsLock = new Object();
    private final Point surfaceSize = new Point();
    private int[] yuvTextures = null;
    private final YuvUploader yuvUploader = new YuvUploader();

    class C07091 implements Runnable {
        C07091() {
        }

        public void run() {
            SurfaceViewRenderer.this.renderFrameOnRenderThread();
        }
    }

    class C07102 implements Runnable {
        C07102() {
        }

        public void run() {
            SurfaceViewRenderer.this.makeBlack();
        }
    }

    class C07113 implements Runnable {
        C07113() {
        }

        public void run() {
            synchronized (SurfaceViewRenderer.this.layoutLock) {
                if (SurfaceViewRenderer.this.isSurfaceCreated && !SurfaceViewRenderer.this.eglBase.hasSurface()) {
                    SurfaceViewRenderer.this.eglBase.createSurface(SurfaceViewRenderer.this.getHolder().getSurface());
                    SurfaceViewRenderer.this.eglBase.makeCurrent();
                    GLES20.glPixelStorei(3317, 1);
                }
            }
        }
    }

    class C07135 implements Runnable {
        C07135() {
        }

        public void run() {
            SurfaceViewRenderer.this.eglBase.releaseSurface();
        }
    }

    class C07146 implements Runnable {
        C07146() {
        }

        public void run() {
            SurfaceViewRenderer.this.requestLayout();
        }
    }

    public SurfaceViewRenderer(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    public SurfaceViewRenderer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        getHolder().addCallback(this);
    }

    private boolean checkConsistentLayout() {
        if (Thread.currentThread() != this.renderThread) {
            throw new IllegalStateException(getResourceName() + "Wrong thread.");
        }
        boolean z;
        synchronized (this.layoutLock) {
            z = this.layoutSize.equals(this.desiredLayoutSize) && this.surfaceSize.equals(this.layoutSize);
        }
        return z;
    }

    private float frameAspectRatio() {
        float f;
        synchronized (this.layoutLock) {
            if (this.frameWidth == 0 || this.frameHeight == 0) {
                f = 0.0f;
            } else {
                f = this.frameRotation % 180 == 0 ? ((float) this.frameWidth) / ((float) this.frameHeight) : ((float) this.frameHeight) / ((float) this.frameWidth);
            }
        }
        return f;
    }

    private Point getDesiredLayoutSize(int i, int i2) {
        Point displaySize;
        synchronized (this.layoutLock) {
            int defaultSize = getDefaultSize(ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED, i);
            int defaultSize2 = getDefaultSize(ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED, i2);
            displaySize = RendererCommon.getDisplaySize(this.scalingType, frameAspectRatio(), defaultSize, defaultSize2);
            if (MeasureSpec.getMode(i) == 1073741824) {
                displaySize.x = defaultSize;
            }
            if (MeasureSpec.getMode(i2) == 1073741824) {
                displaySize.y = defaultSize2;
            }
        }
        return displaySize;
    }

    private String getResourceName() {
        try {
            return getResources().getResourceEntryName(getId()) + ": ";
        } catch (NotFoundException e) {
            return "";
        }
    }

    private void logStatistics() {
        synchronized (this.statisticsLock) {
            Logging.m776d(TAG, getResourceName() + "Frames received: " + this.framesReceived + ". Dropped: " + this.framesDropped + ". Rendered: " + this.framesRendered);
            if (this.framesReceived > 0 && this.framesRendered > 0) {
                long nanoTime = System.nanoTime() - this.firstFrameTimeNs;
                Logging.m776d(TAG, getResourceName() + "Duration: " + ((int) (((double) nanoTime) / 1000000.0d)) + " ms. FPS: " + ((((double) this.framesRendered) * 1.0E9d) / ((double) nanoTime)));
                Logging.m776d(TAG, getResourceName() + "Average render time: " + ((int) (this.renderTimeNs / ((long) (this.framesRendered * 1000)))) + " us.");
            }
        }
    }

    private void makeBlack() {
        if (Thread.currentThread() != this.renderThread) {
            throw new IllegalStateException(getResourceName() + "Wrong thread.");
        } else if (this.eglBase != null && this.eglBase.hasSurface()) {
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            GLES20.glClear(16384);
            this.eglBase.swapBuffers();
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void renderFrameOnRenderThread() {
        /*
        r14 = this;
        r7 = 3;
        r9 = 0;
        r0 = java.lang.Thread.currentThread();
        r1 = r14.renderThread;
        if (r0 == r1) goto L_0x0027;
    L_0x000a:
        r0 = new java.lang.IllegalStateException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = r14.getResourceName();
        r1 = r1.append(r2);
        r2 = "Wrong thread.";
        r1 = r1.append(r2);
        r1 = r1.toString();
        r0.<init>(r1);
        throw r0;
    L_0x0027:
        r1 = r14.frameLock;
        monitor-enter(r1);
        r0 = r14.pendingFrame;	 Catch:{ all -> 0x0062 }
        if (r0 != 0) goto L_0x0030;
    L_0x002e:
        monitor-exit(r1);	 Catch:{ all -> 0x0062 }
    L_0x002f:
        return;
    L_0x0030:
        r10 = r14.pendingFrame;	 Catch:{ all -> 0x0062 }
        r0 = 0;
        r14.pendingFrame = r0;	 Catch:{ all -> 0x0062 }
        monitor-exit(r1);	 Catch:{ all -> 0x0062 }
        r0 = r14.eglBase;
        if (r0 == 0) goto L_0x0042;
    L_0x003a:
        r0 = r14.eglBase;
        r0 = r0.hasSurface();
        if (r0 != 0) goto L_0x0065;
    L_0x0042:
        r0 = "SurfaceViewRenderer";
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = r14.getResourceName();
        r1 = r1.append(r2);
        r2 = "No surface to draw on";
        r1 = r1.append(r2);
        r1 = r1.toString();
        org.webrtc.sinch.Logging.m776d(r0, r1);
        org.webrtc.sinch.VideoRenderer.renderFrameDone(r10);
        goto L_0x002f;
    L_0x0062:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0062 }
        throw r0;
    L_0x0065:
        r0 = r14.checkConsistentLayout();
        if (r0 != 0) goto L_0x0072;
    L_0x006b:
        r14.makeBlack();
        org.webrtc.sinch.VideoRenderer.renderFrameDone(r10);
        goto L_0x002f;
    L_0x0072:
        r1 = r14.layoutLock;
        monitor-enter(r1);
        r0 = r14.eglBase;	 Catch:{ all -> 0x00dc }
        r0 = r0.surfaceWidth();	 Catch:{ all -> 0x00dc }
        r2 = r14.surfaceSize;	 Catch:{ all -> 0x00dc }
        r2 = r2.x;	 Catch:{ all -> 0x00dc }
        if (r0 != r2) goto L_0x008d;
    L_0x0081:
        r0 = r14.eglBase;	 Catch:{ all -> 0x00dc }
        r0 = r0.surfaceHeight();	 Catch:{ all -> 0x00dc }
        r2 = r14.surfaceSize;	 Catch:{ all -> 0x00dc }
        r2 = r2.y;	 Catch:{ all -> 0x00dc }
        if (r0 == r2) goto L_0x0090;
    L_0x008d:
        r14.makeBlack();	 Catch:{ all -> 0x00dc }
    L_0x0090:
        monitor-exit(r1);	 Catch:{ all -> 0x00dc }
        r12 = java.lang.System.nanoTime();
        r1 = r14.layoutLock;
        monitor-enter(r1);
        r0 = r10.samplingMatrix;	 Catch:{ all -> 0x00df }
        r2 = r10.rotationDegree;	 Catch:{ all -> 0x00df }
        r2 = (float) r2;	 Catch:{ all -> 0x00df }
        r0 = org.webrtc.sinch.RendererCommon.rotateTextureMatrix(r0, r2);	 Catch:{ all -> 0x00df }
        r2 = r14.mirror;	 Catch:{ all -> 0x00df }
        r3 = r14.frameAspectRatio();	 Catch:{ all -> 0x00df }
        r4 = r14.layoutSize;	 Catch:{ all -> 0x00df }
        r4 = r4.x;	 Catch:{ all -> 0x00df }
        r4 = (float) r4;	 Catch:{ all -> 0x00df }
        r5 = r14.layoutSize;	 Catch:{ all -> 0x00df }
        r5 = r5.y;	 Catch:{ all -> 0x00df }
        r5 = (float) r5;	 Catch:{ all -> 0x00df }
        r4 = r4 / r5;
        r2 = org.webrtc.sinch.RendererCommon.getLayoutMatrix(r2, r3, r4);	 Catch:{ all -> 0x00df }
        r6 = org.webrtc.sinch.RendererCommon.multiplyMatrices(r0, r2);	 Catch:{ all -> 0x00df }
        monitor-exit(r1);	 Catch:{ all -> 0x00df }
        r0 = 16384; // 0x4000 float:2.2959E-41 double:8.0948E-320;
        android.opengl.GLES20.glClear(r0);
        r0 = r10.yuvFrame;
        if (r0 == 0) goto L_0x0164;
    L_0x00c4:
        r0 = r14.yuvTextures;
        if (r0 != 0) goto L_0x00e2;
    L_0x00c8:
        r0 = new int[r7];
        r14.yuvTextures = r0;
        r0 = r9;
    L_0x00cd:
        if (r0 >= r7) goto L_0x00e2;
    L_0x00cf:
        r1 = r14.yuvTextures;
        r2 = 3553; // 0xde1 float:4.979E-42 double:1.7554E-320;
        r2 = org.webrtc.sinch.GlUtil.generateTexture(r2);
        r1[r0] = r2;
        r0 = r0 + 1;
        goto L_0x00cd;
    L_0x00dc:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x00dc }
        throw r0;
    L_0x00df:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x00df }
        throw r0;
    L_0x00e2:
        r0 = r14.yuvUploader;
        r1 = r14.yuvTextures;
        r2 = r10.width;
        r3 = r10.height;
        r4 = r10.yuvStrides;
        r5 = r10.yuvPlanes;
        r0.uploadYuvData(r1, r2, r3, r4, r5);
        r0 = r14.drawer;
        r1 = r14.yuvTextures;
        r3 = r10.rotatedWidth();
        r4 = r10.rotatedHeight();
        r2 = r14.surfaceSize;
        r7 = r2.x;
        r2 = r14.surfaceSize;
        r8 = r2.y;
        r2 = r6;
        r5 = r9;
        r6 = r9;
        r0.drawYuv(r1, r2, r3, r4, r5, r6, r7, r8);
    L_0x010b:
        r0 = r14.eglBase;
        r0.swapBuffers();
        org.webrtc.sinch.VideoRenderer.renderFrameDone(r10);
        r1 = r14.statisticsLock;
        monitor-enter(r1);
        r0 = r14.framesRendered;	 Catch:{ all -> 0x0161 }
        if (r0 != 0) goto L_0x0145;
    L_0x011a:
        r14.firstFrameTimeNs = r12;	 Catch:{ all -> 0x0161 }
        r2 = r14.layoutLock;	 Catch:{ all -> 0x0161 }
        monitor-enter(r2);	 Catch:{ all -> 0x0161 }
        r0 = "SurfaceViewRenderer";
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x017f }
        r3.<init>();	 Catch:{ all -> 0x017f }
        r4 = r14.getResourceName();	 Catch:{ all -> 0x017f }
        r3 = r3.append(r4);	 Catch:{ all -> 0x017f }
        r4 = "Reporting first rendered frame.";
        r3 = r3.append(r4);	 Catch:{ all -> 0x017f }
        r3 = r3.toString();	 Catch:{ all -> 0x017f }
        org.webrtc.sinch.Logging.m776d(r0, r3);	 Catch:{ all -> 0x017f }
        r0 = r14.rendererEvents;	 Catch:{ all -> 0x017f }
        if (r0 == 0) goto L_0x0144;
    L_0x013f:
        r0 = r14.rendererEvents;	 Catch:{ all -> 0x017f }
        r0.onFirstFrameRendered();	 Catch:{ all -> 0x017f }
    L_0x0144:
        monitor-exit(r2);	 Catch:{ all -> 0x017f }
    L_0x0145:
        r0 = r14.framesRendered;	 Catch:{ all -> 0x0161 }
        r0 = r0 + 1;
        r14.framesRendered = r0;	 Catch:{ all -> 0x0161 }
        r2 = r14.renderTimeNs;	 Catch:{ all -> 0x0161 }
        r4 = java.lang.System.nanoTime();	 Catch:{ all -> 0x0161 }
        r4 = r4 - r12;
        r2 = r2 + r4;
        r14.renderTimeNs = r2;	 Catch:{ all -> 0x0161 }
        r0 = r14.framesRendered;	 Catch:{ all -> 0x0161 }
        r0 = r0 % 300;
        if (r0 != 0) goto L_0x015e;
    L_0x015b:
        r14.logStatistics();	 Catch:{ all -> 0x0161 }
    L_0x015e:
        monitor-exit(r1);	 Catch:{ all -> 0x0161 }
        goto L_0x002f;
    L_0x0161:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0161 }
        throw r0;
    L_0x0164:
        r0 = r14.drawer;
        r1 = r10.textureId;
        r3 = r10.rotatedWidth();
        r4 = r10.rotatedHeight();
        r2 = r14.surfaceSize;
        r7 = r2.x;
        r2 = r14.surfaceSize;
        r8 = r2.y;
        r2 = r6;
        r5 = r9;
        r6 = r9;
        r0.drawOes(r1, r2, r3, r4, r5, r6, r7, r8);
        goto L_0x010b;
    L_0x017f:
        r0 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x017f }
        throw r0;	 Catch:{ all -> 0x0161 }
        */
        throw new UnsupportedOperationException("Method not decompiled: org.webrtc.sinch.SurfaceViewRenderer.renderFrameOnRenderThread():void");
    }

    private void runOnRenderThread(Runnable runnable) {
        synchronized (this.handlerLock) {
            if (this.renderThreadHandler != null) {
                this.renderThreadHandler.post(runnable);
            }
        }
    }

    private void updateFrameDimensionsAndReportEvents(I420Frame i420Frame) {
        synchronized (this.layoutLock) {
            if (!(this.frameWidth == i420Frame.width && this.frameHeight == i420Frame.height && this.frameRotation == i420Frame.rotationDegree)) {
                Logging.m776d(TAG, getResourceName() + "Reporting frame resolution changed to " + i420Frame.width + "x" + i420Frame.height + " with rotation " + i420Frame.rotationDegree);
                if (this.rendererEvents != null) {
                    this.rendererEvents.onFrameResolutionChanged(i420Frame.width, i420Frame.height, i420Frame.rotationDegree);
                }
                this.frameWidth = i420Frame.width;
                this.frameHeight = i420Frame.height;
                this.frameRotation = i420Frame.rotationDegree;
                post(new C07146());
            }
        }
    }

    public void init(EglBase.Context context, RendererEvents rendererEvents) {
        init(context, rendererEvents, EglBase.CONFIG_PLAIN, new GlRectDrawer());
    }

    public void init(EglBase.Context context, RendererEvents rendererEvents, int[] iArr, GlDrawer glDrawer) {
        synchronized (this.handlerLock) {
            if (this.renderThreadHandler != null) {
                throw new IllegalStateException(getResourceName() + "Already initialized");
            }
            Logging.m776d(TAG, getResourceName() + "Initializing.");
            this.rendererEvents = rendererEvents;
            this.drawer = glDrawer;
            this.renderThread = new HandlerThread(TAG);
            this.renderThread.start();
            this.eglBase = EglBase.create(context, iArr);
            this.renderThreadHandler = new Handler(this.renderThread.getLooper());
        }
        tryCreateEglSurface();
    }

    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        synchronized (this.layoutLock) {
            this.layoutSize.x = i3 - i;
            this.layoutSize.y = i4 - i2;
        }
        runOnRenderThread(this.renderFrameRunnable);
    }

    protected void onMeasure(int i, int i2) {
        synchronized (this.layoutLock) {
            if (this.frameWidth == 0 || this.frameHeight == 0) {
                super.onMeasure(i, i2);
                return;
            }
            this.desiredLayoutSize = getDesiredLayoutSize(i, i2);
            if (!(this.desiredLayoutSize.x == getMeasuredWidth() && this.desiredLayoutSize.y == getMeasuredHeight())) {
                synchronized (this.handlerLock) {
                    if (this.renderThreadHandler != null) {
                        this.renderThreadHandler.postAtFrontOfQueue(this.makeBlackRunnable);
                    }
                }
            }
            setMeasuredDimension(this.desiredLayoutSize.x, this.desiredLayoutSize.y);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void release() {
        /*
        r5 = this;
        r4 = 0;
        r0 = new java.util.concurrent.CountDownLatch;
        r1 = 1;
        r0.<init>(r1);
        r1 = r5.handlerLock;
        monitor-enter(r1);
        r2 = r5.renderThreadHandler;	 Catch:{ all -> 0x006d }
        if (r2 != 0) goto L_0x002c;
    L_0x000e:
        r0 = "SurfaceViewRenderer";
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x006d }
        r2.<init>();	 Catch:{ all -> 0x006d }
        r3 = r5.getResourceName();	 Catch:{ all -> 0x006d }
        r2 = r2.append(r3);	 Catch:{ all -> 0x006d }
        r3 = "Already released";
        r2 = r2.append(r3);	 Catch:{ all -> 0x006d }
        r2 = r2.toString();	 Catch:{ all -> 0x006d }
        org.webrtc.sinch.Logging.m776d(r0, r2);	 Catch:{ all -> 0x006d }
        monitor-exit(r1);	 Catch:{ all -> 0x006d }
    L_0x002b:
        return;
    L_0x002c:
        r2 = r5.renderThreadHandler;	 Catch:{ all -> 0x006d }
        r3 = new org.webrtc.sinch.SurfaceViewRenderer$4;	 Catch:{ all -> 0x006d }
        r3.<init>(r0);	 Catch:{ all -> 0x006d }
        r2.postAtFrontOfQueue(r3);	 Catch:{ all -> 0x006d }
        r2 = 0;
        r5.renderThreadHandler = r2;	 Catch:{ all -> 0x006d }
        monitor-exit(r1);	 Catch:{ all -> 0x006d }
        org.webrtc.sinch.ThreadUtils.awaitUninterruptibly(r0);
        r0 = r5.renderThread;
        r0.quit();
        r1 = r5.frameLock;
        monitor-enter(r1);
        r0 = r5.pendingFrame;	 Catch:{ all -> 0x0070 }
        if (r0 == 0) goto L_0x0051;
    L_0x0049:
        r0 = r5.pendingFrame;	 Catch:{ all -> 0x0070 }
        org.webrtc.sinch.VideoRenderer.renderFrameDone(r0);	 Catch:{ all -> 0x0070 }
        r0 = 0;
        r5.pendingFrame = r0;	 Catch:{ all -> 0x0070 }
    L_0x0051:
        monitor-exit(r1);	 Catch:{ all -> 0x0070 }
        r0 = r5.renderThread;
        org.webrtc.sinch.ThreadUtils.joinUninterruptibly(r0);
        r5.renderThread = r4;
        r1 = r5.layoutLock;
        monitor-enter(r1);
        r0 = 0;
        r5.frameWidth = r0;	 Catch:{ all -> 0x0073 }
        r0 = 0;
        r5.frameHeight = r0;	 Catch:{ all -> 0x0073 }
        r0 = 0;
        r5.frameRotation = r0;	 Catch:{ all -> 0x0073 }
        r0 = 0;
        r5.rendererEvents = r0;	 Catch:{ all -> 0x0073 }
        monitor-exit(r1);	 Catch:{ all -> 0x0073 }
        r5.resetStatistics();
        goto L_0x002b;
    L_0x006d:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x006d }
        throw r0;
    L_0x0070:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0070 }
        throw r0;
    L_0x0073:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0073 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.webrtc.sinch.SurfaceViewRenderer.release():void");
    }

    public void renderFrame(I420Frame i420Frame) {
        synchronized (this.statisticsLock) {
            this.framesReceived++;
        }
        synchronized (this.handlerLock) {
            if (this.renderThreadHandler == null) {
                Logging.m776d(TAG, getResourceName() + "Dropping frame - Not initialized or already released.");
                VideoRenderer.renderFrameDone(i420Frame);
                return;
            }
            synchronized (this.frameLock) {
                if (this.pendingFrame != null) {
                    synchronized (this.statisticsLock) {
                        this.framesDropped++;
                    }
                    VideoRenderer.renderFrameDone(this.pendingFrame);
                }
                this.pendingFrame = i420Frame;
                updateFrameDimensionsAndReportEvents(i420Frame);
                this.renderThreadHandler.post(this.renderFrameRunnable);
            }
        }
    }

    public void resetStatistics() {
        synchronized (this.statisticsLock) {
            this.framesReceived = 0;
            this.framesDropped = 0;
            this.framesRendered = 0;
            this.firstFrameTimeNs = 0;
            this.renderTimeNs = 0;
        }
    }

    public void setMirror(boolean z) {
        synchronized (this.layoutLock) {
            this.mirror = z;
        }
    }

    public void setScalingType(ScalingType scalingType) {
        synchronized (this.layoutLock) {
            this.scalingType = scalingType;
        }
    }

    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        Logging.m776d(TAG, getResourceName() + "Surface changed: " + i2 + "x" + i3);
        synchronized (this.layoutLock) {
            this.surfaceSize.x = i2;
            this.surfaceSize.y = i3;
        }
        runOnRenderThread(this.renderFrameRunnable);
    }

    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Logging.m776d(TAG, getResourceName() + "Surface created.");
        synchronized (this.layoutLock) {
            this.isSurfaceCreated = true;
        }
        tryCreateEglSurface();
    }

    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Logging.m776d(TAG, getResourceName() + "Surface destroyed.");
        synchronized (this.layoutLock) {
            this.isSurfaceCreated = false;
            this.surfaceSize.x = 0;
            this.surfaceSize.y = 0;
        }
        runOnRenderThread(new C07135());
    }

    public void tryCreateEglSurface() {
        runOnRenderThread(new C07113());
    }
}
