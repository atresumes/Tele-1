package org.webrtc.sinch;

import android.annotation.SuppressLint;
import android.graphics.Point;
import android.graphics.Rect;
import android.opengl.EGL14;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.opengles.GL10;
import org.webrtc.sinch.EglBase.Context;
import org.webrtc.sinch.RendererCommon.GlDrawer;
import org.webrtc.sinch.RendererCommon.RendererEvents;
import org.webrtc.sinch.RendererCommon.ScalingType;
import org.webrtc.sinch.RendererCommon.YuvUploader;
import org.webrtc.sinch.VideoRenderer.Callbacks;
import org.webrtc.sinch.VideoRenderer.I420Frame;

public class VideoRendererGui implements Renderer {
    private static final String TAG = "VideoRendererGui";
    static float blue = 0.15f;
    private static Thread drawThread;
    private static Context eglContext = null;
    private static Runnable eglContextReady = null;
    static float green = 0.15f;
    private static VideoRendererGui instance = null;
    static float red = 0.15f;
    private static Thread renderFrameThread;
    private boolean onSurfaceCreatedCalled;
    private int screenHeight;
    private int screenWidth;
    private GLSurfaceView surface;
    private final ArrayList<YuvImageRenderer> yuvImageRenderers = new ArrayList();

    final class C07271 implements Runnable {
        final /* synthetic */ CountDownLatch val$countDownLatch;
        final /* synthetic */ YuvImageRenderer val$yuvImageRenderer;

        C07271(YuvImageRenderer yuvImageRenderer, CountDownLatch countDownLatch) {
            this.val$yuvImageRenderer = yuvImageRenderer;
            this.val$countDownLatch = countDownLatch;
        }

        public void run() {
            this.val$yuvImageRenderer.createTextures();
            this.val$yuvImageRenderer.setScreenSize(VideoRendererGui.instance.screenWidth, VideoRendererGui.instance.screenHeight);
            this.val$countDownLatch.countDown();
        }
    }

    class YuvImageRenderer implements Callbacks {
        private long copyTimeNs;
        private final Rect displayLayout;
        private long drawTimeNs;
        private final GlDrawer drawer;
        private int framesDropped;
        private int framesReceived;
        private int framesRendered;
        private int id;
        private final Rect layoutInPercentage;
        private float[] layoutMatrix;
        private boolean mirror;
        private I420Frame pendingFrame;
        private final Object pendingFrameLock;
        private RendererEvents rendererEvents;
        private RendererType rendererType;
        private float[] rotatedSamplingMatrix;
        private int rotationDegree;
        private ScalingType scalingType;
        private int screenHeight;
        private int screenWidth;
        boolean seenFrame;
        private long startTimeNs;
        private GLSurfaceView surface;
        private GlTextureFrameBuffer textureCopy;
        private final Object updateLayoutLock;
        private boolean updateLayoutProperties;
        private int videoHeight;
        private int videoWidth;
        private int[] yuvTextures;
        private final YuvUploader yuvUploader;

        enum RendererType {
            RENDERER_YUV,
            RENDERER_TEXTURE
        }

        private YuvImageRenderer(GLSurfaceView gLSurfaceView, int i, int i2, int i3, int i4, int i5, ScalingType scalingType, boolean z, GlDrawer glDrawer) {
            this.yuvTextures = new int[]{0, 0, 0};
            this.yuvUploader = new YuvUploader();
            this.pendingFrameLock = new Object();
            this.startTimeNs = -1;
            this.displayLayout = new Rect();
            this.updateLayoutLock = new Object();
            Logging.m776d(VideoRendererGui.TAG, "YuvImageRenderer.Create id: " + i);
            this.surface = gLSurfaceView;
            this.id = i;
            this.scalingType = scalingType;
            this.mirror = z;
            this.drawer = glDrawer;
            this.layoutInPercentage = new Rect(i2, i3, Math.min(100, i2 + i4), Math.min(100, i3 + i5));
            this.updateLayoutProperties = false;
            this.rotationDegree = 0;
        }

        private void createTextures() {
            Logging.m776d(VideoRendererGui.TAG, "  YuvImageRenderer.createTextures " + this.id + " on GL thread:" + Thread.currentThread().getId());
            for (int i = 0; i < 3; i++) {
                this.yuvTextures[i] = GlUtil.generateTexture(3553);
            }
            this.textureCopy = new GlTextureFrameBuffer(6407);
        }

        private void draw() {
            if (this.seenFrame) {
                Object obj;
                long nanoTime = System.nanoTime();
                synchronized (this.pendingFrameLock) {
                    obj = this.pendingFrame != null ? 1 : null;
                    if (obj != null && this.startTimeNs == -1) {
                        this.startTimeNs = nanoTime;
                    }
                    if (obj != null) {
                        this.rotatedSamplingMatrix = RendererCommon.rotateTextureMatrix(this.pendingFrame.samplingMatrix, (float) this.pendingFrame.rotationDegree);
                        if (this.pendingFrame.yuvFrame) {
                            this.rendererType = RendererType.RENDERER_YUV;
                            this.yuvUploader.uploadYuvData(this.yuvTextures, this.pendingFrame.width, this.pendingFrame.height, this.pendingFrame.yuvStrides, this.pendingFrame.yuvPlanes);
                        } else {
                            this.rendererType = RendererType.RENDERER_TEXTURE;
                            this.textureCopy.setSize(this.pendingFrame.rotatedWidth(), this.pendingFrame.rotatedHeight());
                            GLES20.glBindFramebuffer(36160, this.textureCopy.getFrameBufferId());
                            GlUtil.checkNoGLES2Error("glBindFramebuffer");
                            this.drawer.drawOes(this.pendingFrame.textureId, this.rotatedSamplingMatrix, this.textureCopy.getWidth(), this.textureCopy.getHeight(), 0, 0, this.textureCopy.getWidth(), this.textureCopy.getHeight());
                            this.rotatedSamplingMatrix = RendererCommon.identityMatrix();
                            GLES20.glBindFramebuffer(36160, 0);
                            GLES20.glFinish();
                        }
                        this.copyTimeNs += System.nanoTime() - nanoTime;
                        VideoRenderer.renderFrameDone(this.pendingFrame);
                        this.pendingFrame = null;
                    }
                }
                updateLayoutMatrix();
                float[] multiplyMatrices = RendererCommon.multiplyMatrices(this.rotatedSamplingMatrix, this.layoutMatrix);
                int i = this.screenHeight - this.displayLayout.bottom;
                if (this.rendererType == RendererType.RENDERER_YUV) {
                    this.drawer.drawYuv(this.yuvTextures, multiplyMatrices, this.videoWidth, this.videoHeight, this.displayLayout.left, i, this.displayLayout.width(), this.displayLayout.height());
                } else {
                    this.drawer.drawRgb(this.textureCopy.getTextureId(), multiplyMatrices, this.videoWidth, this.videoHeight, this.displayLayout.left, i, this.displayLayout.width(), this.displayLayout.height());
                }
                if (obj != null) {
                    this.framesRendered++;
                    this.drawTimeNs += System.nanoTime() - nanoTime;
                    if (this.framesRendered % 300 == 0) {
                        logStatistics();
                    }
                }
            }
        }

        private void logStatistics() {
            long nanoTime = System.nanoTime() - this.startTimeNs;
            Logging.m776d(VideoRendererGui.TAG, "ID: " + this.id + ". Type: " + this.rendererType + ". Frames received: " + this.framesReceived + ". Dropped: " + this.framesDropped + ". Rendered: " + this.framesRendered);
            if (this.framesReceived > 0 && this.framesRendered > 0) {
                Logging.m776d(VideoRendererGui.TAG, "Duration: " + ((int) (((double) nanoTime) / 1000000.0d)) + " ms. FPS: " + ((((double) this.framesRendered) * 1.0E9d) / ((double) nanoTime)));
                Logging.m776d(VideoRendererGui.TAG, "Draw time: " + ((int) (this.drawTimeNs / ((long) (this.framesRendered * 1000)))) + " us. Copy time: " + ((int) (this.copyTimeNs / ((long) (this.framesReceived * 1000)))) + " us");
            }
        }

        private synchronized void release() {
            this.surface = null;
            this.drawer.release();
            synchronized (this.pendingFrameLock) {
                if (this.pendingFrame != null) {
                    VideoRenderer.renderFrameDone(this.pendingFrame);
                    this.pendingFrame = null;
                }
            }
        }

        private void setSize(int i, int i2, int i3) {
            if (i != this.videoWidth || i2 != this.videoHeight || i3 != this.rotationDegree) {
                if (this.rendererEvents != null) {
                    Logging.m776d(VideoRendererGui.TAG, "ID: " + this.id + ". Reporting frame resolution changed to " + i + " x " + i2);
                    this.rendererEvents.onFrameResolutionChanged(i, i2, i3);
                }
                synchronized (this.updateLayoutLock) {
                    Logging.m776d(VideoRendererGui.TAG, "ID: " + this.id + ". YuvImageRenderer.setSize: " + i + " x " + i2 + " rotation " + i3);
                    this.videoWidth = i;
                    this.videoHeight = i2;
                    this.rotationDegree = i3;
                    this.updateLayoutProperties = true;
                    Logging.m776d(VideoRendererGui.TAG, "  YuvImageRenderer.setSize done.");
                }
            }
        }

        private void updateLayoutMatrix() {
            synchronized (this.updateLayoutLock) {
                if (this.updateLayoutProperties) {
                    this.displayLayout.set(((this.screenWidth * this.layoutInPercentage.left) + 99) / 100, ((this.screenHeight * this.layoutInPercentage.top) + 99) / 100, (this.screenWidth * this.layoutInPercentage.right) / 100, (this.screenHeight * this.layoutInPercentage.bottom) / 100);
                    Logging.m776d(VideoRendererGui.TAG, "ID: " + this.id + ". AdjustTextureCoords. Allowed display size: " + this.displayLayout.width() + " x " + this.displayLayout.height() + ". Video: " + this.videoWidth + " x " + this.videoHeight + ". Rotation: " + this.rotationDegree + ". Mirror: " + this.mirror);
                    float f = this.rotationDegree % 180 == 0 ? ((float) this.videoWidth) / ((float) this.videoHeight) : ((float) this.videoHeight) / ((float) this.videoWidth);
                    Point displaySize = RendererCommon.getDisplaySize(this.scalingType, f, this.displayLayout.width(), this.displayLayout.height());
                    this.displayLayout.inset((this.displayLayout.width() - displaySize.x) / 2, (this.displayLayout.height() - displaySize.y) / 2);
                    Logging.m776d(VideoRendererGui.TAG, "  Adjusted display size: " + this.displayLayout.width() + " x " + this.displayLayout.height());
                    this.layoutMatrix = RendererCommon.getLayoutMatrix(this.mirror, f, ((float) this.displayLayout.width()) / ((float) this.displayLayout.height()));
                    this.updateLayoutProperties = false;
                    Logging.m776d(VideoRendererGui.TAG, "  AdjustTextureCoords done");
                    return;
                }
            }
        }

        public synchronized void renderFrame(I420Frame i420Frame) {
            if (this.surface == null) {
                VideoRenderer.renderFrameDone(i420Frame);
            } else {
                if (VideoRendererGui.renderFrameThread == null) {
                    VideoRendererGui.renderFrameThread = Thread.currentThread();
                }
                if (!(this.seenFrame || this.rendererEvents == null)) {
                    Logging.m776d(VideoRendererGui.TAG, "ID: " + this.id + ". Reporting first rendered frame.");
                    this.rendererEvents.onFirstFrameRendered();
                }
                this.framesReceived++;
                synchronized (this.pendingFrameLock) {
                    if (i420Frame.yuvFrame && (i420Frame.yuvStrides[0] < i420Frame.width || i420Frame.yuvStrides[1] < i420Frame.width / 2 || i420Frame.yuvStrides[2] < i420Frame.width / 2)) {
                        Logging.m777e(VideoRendererGui.TAG, "Incorrect strides " + i420Frame.yuvStrides[0] + ", " + i420Frame.yuvStrides[1] + ", " + i420Frame.yuvStrides[2]);
                        VideoRenderer.renderFrameDone(i420Frame);
                    } else if (this.pendingFrame != null) {
                        this.framesDropped++;
                        VideoRenderer.renderFrameDone(i420Frame);
                        this.seenFrame = true;
                    } else {
                        this.pendingFrame = i420Frame;
                        setSize(i420Frame.width, i420Frame.height, i420Frame.rotationDegree);
                        this.seenFrame = true;
                        this.surface.requestRender();
                    }
                }
            }
        }

        public synchronized void reset() {
            this.seenFrame = false;
        }

        public synchronized void resetDrawer() {
            this.drawer.release();
        }

        public void setPosition(int i, int i2, int i3, int i4, ScalingType scalingType, boolean z) {
            Rect rect = new Rect(i, i2, Math.min(100, i + i3), Math.min(100, i2 + i4));
            synchronized (this.updateLayoutLock) {
                if (rect.equals(this.layoutInPercentage) && scalingType == this.scalingType && z == this.mirror) {
                    return;
                }
                Logging.m776d(VideoRendererGui.TAG, "ID: " + this.id + ". YuvImageRenderer.setPosition: (" + i + ", " + i2 + ") " + i3 + " x " + i4 + ". Scaling: " + scalingType + ". Mirror: " + z);
                this.layoutInPercentage.set(rect);
                this.scalingType = scalingType;
                this.mirror = z;
                this.updateLayoutProperties = true;
            }
        }

        public void setScreenSize(int i, int i2) {
            synchronized (this.updateLayoutLock) {
                if (i == this.screenWidth && i2 == this.screenHeight) {
                    return;
                }
                Logging.m776d(VideoRendererGui.TAG, "ID: " + this.id + ". YuvImageRenderer.setScreenSize: " + i + " x " + i2);
                this.screenWidth = i;
                this.screenHeight = i2;
                this.updateLayoutProperties = true;
            }
        }
    }

    private VideoRendererGui(GLSurfaceView gLSurfaceView) {
        this.surface = gLSurfaceView;
        gLSurfaceView.setPreserveEGLContextOnPause(true);
        gLSurfaceView.setEGLContextClientVersion(2);
        gLSurfaceView.setRenderer(this);
        gLSurfaceView.setRenderMode(0);
    }

    public static synchronized YuvImageRenderer create(int i, int i2, int i3, int i4, ScalingType scalingType, boolean z) {
        YuvImageRenderer create;
        synchronized (VideoRendererGui.class) {
            create = create(i, i2, i3, i4, scalingType, z, new GlRectDrawer());
        }
        return create;
    }

    public static synchronized YuvImageRenderer create(int i, int i2, int i3, int i4, ScalingType scalingType, boolean z, GlDrawer glDrawer) {
        YuvImageRenderer yuvImageRenderer;
        synchronized (VideoRendererGui.class) {
            if (i < 0 || i > 100 || i2 < 0 || i2 > 100 || i3 < 0 || i3 > 100 || i4 < 0 || i4 > 100 || i + i3 > 100 || i2 + i4 > 100) {
                throw new RuntimeException("Incorrect window parameters.");
            } else if (instance == null) {
                throw new RuntimeException("Attempt to create yuv renderer before setting GLSurfaceView");
            } else {
                yuvImageRenderer = new YuvImageRenderer(instance.surface, instance.yuvImageRenderers.size(), i, i2, i3, i4, scalingType, z, glDrawer);
                synchronized (instance.yuvImageRenderers) {
                    if (instance.onSurfaceCreatedCalled) {
                        CountDownLatch countDownLatch = new CountDownLatch(1);
                        instance.surface.queueEvent(new C07271(yuvImageRenderer, countDownLatch));
                        try {
                            countDownLatch.await();
                        } catch (Throwable e) {
                            throw new RuntimeException(e);
                        }
                    }
                    instance.yuvImageRenderers.add(yuvImageRenderer);
                }
            }
        }
        return yuvImageRenderer;
    }

    public static VideoRenderer createGui(int i, int i2, int i3, int i4, ScalingType scalingType, boolean z) {
        return new VideoRenderer(create(i, i2, i3, i4, scalingType, z));
    }

    public static Callbacks createGuiRenderer(int i, int i2, int i3, int i4, ScalingType scalingType, boolean z) {
        return create(i, i2, i3, i4, scalingType, z);
    }

    public static synchronized void dispose() {
        synchronized (VideoRendererGui.class) {
            if (instance != null) {
                Logging.m776d(TAG, "VideoRendererGui.dispose");
                synchronized (instance.yuvImageRenderers) {
                    Iterator it = instance.yuvImageRenderers.iterator();
                    while (it.hasNext()) {
                        ((YuvImageRenderer) it.next()).release();
                    }
                    instance.yuvImageRenderers.clear();
                }
                renderFrameThread = null;
                drawThread = null;
                instance.surface = null;
                eglContext = null;
                eglContextReady = null;
                instance = null;
            }
        }
    }

    public static synchronized Context getEglBaseContext() {
        Context context;
        synchronized (VideoRendererGui.class) {
            context = eglContext;
        }
        return context;
    }

    private static void printStackTrace(Thread thread, String str) {
        if (thread != null) {
            StackTraceElement[] stackTrace = thread.getStackTrace();
            if (stackTrace.length > 0) {
                Logging.m776d(TAG, str + " stacks trace:");
                for (StackTraceElement stackTraceElement : stackTrace) {
                    Logging.m776d(TAG, stackTraceElement.toString());
                }
            }
        }
    }

    public static synchronized void printStackTraces() {
        synchronized (VideoRendererGui.class) {
            if (instance != null) {
                printStackTrace(renderFrameThread, "Render frame thread");
                printStackTrace(drawThread, "Draw thread");
            }
        }
    }

    public static synchronized void remove(Callbacks callbacks) {
        synchronized (VideoRendererGui.class) {
            Logging.m776d(TAG, "VideoRendererGui.remove");
            if (instance == null) {
                throw new RuntimeException("Attempt to remove renderer before setting GLSurfaceView");
            }
            synchronized (instance.yuvImageRenderers) {
                int indexOf = instance.yuvImageRenderers.indexOf(callbacks);
                if (indexOf == -1) {
                    Logging.m780w(TAG, "Couldn't remove renderer (not present in current list)");
                } else {
                    ((YuvImageRenderer) instance.yuvImageRenderers.remove(indexOf)).release();
                }
            }
        }
    }

    public static synchronized void reset(Callbacks callbacks) {
        synchronized (VideoRendererGui.class) {
            Logging.m776d(TAG, "VideoRendererGui.reset");
            if (instance == null) {
                throw new RuntimeException("Attempt to reset renderer before setting GLSurfaceView");
            }
            synchronized (instance.yuvImageRenderers) {
                Iterator it = instance.yuvImageRenderers.iterator();
                while (it.hasNext()) {
                    Callbacks callbacks2 = (YuvImageRenderer) it.next();
                    if (callbacks2 == callbacks) {
                        callbacks2.reset();
                    }
                }
            }
        }
    }

    public static void setClearColor(float f, float f2, float f3) {
        red = f;
        green = f2;
        blue = f3;
    }

    public static synchronized void setRendererEvents(Callbacks callbacks, RendererEvents rendererEvents) {
        synchronized (VideoRendererGui.class) {
            Logging.m776d(TAG, "VideoRendererGui.setRendererEvents");
            if (instance == null) {
                throw new RuntimeException("Attempt to set renderer events before setting GLSurfaceView");
            }
            synchronized (instance.yuvImageRenderers) {
                Iterator it = instance.yuvImageRenderers.iterator();
                while (it.hasNext()) {
                    Callbacks callbacks2 = (YuvImageRenderer) it.next();
                    if (callbacks2 == callbacks) {
                        callbacks2.rendererEvents = rendererEvents;
                    }
                }
            }
        }
    }

    public static synchronized void setView(GLSurfaceView gLSurfaceView, Runnable runnable) {
        synchronized (VideoRendererGui.class) {
            Logging.m776d(TAG, "VideoRendererGui.setView");
            instance = new VideoRendererGui(gLSurfaceView);
            eglContextReady = runnable;
        }
    }

    public static synchronized void update(Callbacks callbacks, int i, int i2, int i3, int i4, ScalingType scalingType, boolean z) {
        synchronized (VideoRendererGui.class) {
            Logging.m776d(TAG, "VideoRendererGui.update");
            if (instance == null) {
                throw new RuntimeException("Attempt to update yuv renderer before setting GLSurfaceView");
            }
            synchronized (instance.yuvImageRenderers) {
                Iterator it = instance.yuvImageRenderers.iterator();
                while (it.hasNext()) {
                    Callbacks callbacks2 = (YuvImageRenderer) it.next();
                    if (callbacks2 == callbacks) {
                        callbacks2.setPosition(i, i2, i3, i4, scalingType, z);
                    }
                }
            }
        }
    }

    public void onDrawFrame(GL10 gl10) {
        if (drawThread == null) {
            drawThread = Thread.currentThread();
        }
        GLES20.glViewport(0, 0, this.screenWidth, this.screenHeight);
        GLES20.glClear(16384);
        synchronized (this.yuvImageRenderers) {
            Iterator it = this.yuvImageRenderers.iterator();
            while (it.hasNext()) {
                ((YuvImageRenderer) it.next()).draw();
            }
        }
    }

    public void onSurfaceChanged(GL10 gl10, int i, int i2) {
        Logging.m776d(TAG, "VideoRendererGui.onSurfaceChanged: " + i + " x " + i2 + "  ");
        this.screenWidth = i;
        this.screenHeight = i2;
        synchronized (this.yuvImageRenderers) {
            Iterator it = this.yuvImageRenderers.iterator();
            while (it.hasNext()) {
                ((YuvImageRenderer) it.next()).setScreenSize(this.screenWidth, this.screenHeight);
            }
        }
    }

    @SuppressLint({"NewApi"})
    public void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
        Logging.m776d(TAG, "VideoRendererGui.onSurfaceCreated");
        synchronized (VideoRendererGui.class) {
            if (EglBase14.isEGL14Supported()) {
                eglContext = new EglBase14.Context(EGL14.eglGetCurrentContext());
            } else {
                eglContext = new EglBase10.Context(((EGL10) EGLContext.getEGL()).eglGetCurrentContext());
            }
            Logging.m776d(TAG, "VideoRendererGui EGL Context: " + eglContext);
        }
        synchronized (this.yuvImageRenderers) {
            Iterator it = this.yuvImageRenderers.iterator();
            while (it.hasNext()) {
                YuvImageRenderer yuvImageRenderer = (YuvImageRenderer) it.next();
                yuvImageRenderer.resetDrawer();
                yuvImageRenderer.createTextures();
            }
            this.onSurfaceCreatedCalled = true;
        }
        GlUtil.checkNoGLES2Error("onSurfaceCreated done");
        GLES20.glPixelStorei(3317, 1);
        GLES20.glClearColor(red, green, blue, 0.0f);
        synchronized (VideoRendererGui.class) {
            if (eglContextReady != null) {
                eglContextReady.run();
            }
        }
    }
}
