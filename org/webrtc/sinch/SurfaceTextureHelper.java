package org.webrtc.sinch;

import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.opengl.GLES20;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.toolbox.ImageRequest;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import org.webrtc.sinch.EglBase.Context;

class SurfaceTextureHelper {
    private static final String TAG = "SurfaceTextureHelper";
    private final EglBase eglBase;
    private final Handler handler;
    private boolean hasPendingTexture;
    private boolean isQuitting;
    private volatile boolean isTextureInUse;
    private OnTextureFrameAvailableListener listener;
    private final int oesTextureId;
    private OnTextureFrameAvailableListener pendingListener;
    final Runnable setListenerRunnable;
    private final SurfaceTexture surfaceTexture;
    private YuvConverter yuvConverter;

    final class C07031 implements Callable<SurfaceTextureHelper> {
        final /* synthetic */ Handler val$handler;
        final /* synthetic */ Context val$sharedContext;
        final /* synthetic */ String val$threadName;

        C07031(Context context, Handler handler, String str) {
            this.val$sharedContext = context;
            this.val$handler = handler;
            this.val$threadName = str;
        }

        public SurfaceTextureHelper call() {
            try {
                return new SurfaceTextureHelper(this.val$sharedContext, this.val$handler);
            } catch (Throwable e) {
                Logging.m778e(SurfaceTextureHelper.TAG, this.val$threadName + " create failure", e);
                return null;
            }
        }
    }

    class C07042 implements Runnable {
        C07042() {
        }

        public void run() {
            Logging.m776d(SurfaceTextureHelper.TAG, "Setting listener to " + SurfaceTextureHelper.this.pendingListener);
            SurfaceTextureHelper.this.listener = SurfaceTextureHelper.this.pendingListener;
            SurfaceTextureHelper.this.pendingListener = null;
            if (SurfaceTextureHelper.this.hasPendingTexture) {
                SurfaceTextureHelper.this.updateTexImage();
                SurfaceTextureHelper.this.hasPendingTexture = false;
            }
        }
    }

    class C07053 implements OnFrameAvailableListener {
        C07053() {
        }

        public void onFrameAvailable(SurfaceTexture surfaceTexture) {
            SurfaceTextureHelper.this.hasPendingTexture = true;
            SurfaceTextureHelper.this.tryDeliverTextureFrame();
        }
    }

    class C07064 implements Runnable {
        C07064() {
        }

        public void run() {
            SurfaceTextureHelper.this.listener = null;
            SurfaceTextureHelper.this.pendingListener = null;
        }
    }

    class C07075 implements Runnable {
        C07075() {
        }

        public void run() {
            SurfaceTextureHelper.this.isTextureInUse = false;
            if (SurfaceTextureHelper.this.isQuitting) {
                SurfaceTextureHelper.this.release();
            } else {
                SurfaceTextureHelper.this.tryDeliverTextureFrame();
            }
        }
    }

    class C07086 implements Runnable {
        C07086() {
        }

        public void run() {
            SurfaceTextureHelper.this.isQuitting = true;
            if (!SurfaceTextureHelper.this.isTextureInUse) {
                SurfaceTextureHelper.this.release();
            }
        }
    }

    public interface OnTextureFrameAvailableListener {
        void onTextureFrameAvailable(int i, float[] fArr, long j);
    }

    class YuvConverter {
        private static final FloatBuffer DEVICE_RECTANGLE = GlUtil.createFloatBuffer(new float[]{-1.0f, -1.0f, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT, -1.0f, -1.0f, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT});
        private static final String FRAGMENT_SHADER = "#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nvarying vec2 interp_tc;\n\nuniform samplerExternalOES oesTex;\nuniform vec2 xUnit;\nuniform vec4 coeffs;\n\nvoid main() {\n  gl_FragColor.r = coeffs.a + dot(coeffs.rgb,\n      texture2D(oesTex, interp_tc - 1.5 * xUnit).rgb);\n  gl_FragColor.g = coeffs.a + dot(coeffs.rgb,\n      texture2D(oesTex, interp_tc - 0.5 * xUnit).rgb);\n  gl_FragColor.b = coeffs.a + dot(coeffs.rgb,\n      texture2D(oesTex, interp_tc + 0.5 * xUnit).rgb);\n  gl_FragColor.a = coeffs.a + dot(coeffs.rgb,\n      texture2D(oesTex, interp_tc + 1.5 * xUnit).rgb);\n}\n";
        private static final FloatBuffer TEXTURE_RECTANGLE = GlUtil.createFloatBuffer(new float[]{0.0f, 0.0f, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT, 0.0f, 0.0f, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT});
        private static final String VERTEX_SHADER = "varying vec2 interp_tc;\nattribute vec4 in_pos;\nattribute vec4 in_tc;\n\nuniform mat4 texMatrix;\n\nvoid main() {\n    gl_Position = in_pos;\n    interp_tc = (texMatrix * in_tc).xy;\n}\n";
        private int coeffsLoc;
        private final EglBase eglBase;
        private boolean released = false;
        private final GlShader shader;
        private int texMatrixLoc;
        private int xUnitLoc;

        YuvConverter(Context context) {
            this.eglBase = EglBase.create(context, EglBase.CONFIG_PIXEL_RGBA_BUFFER);
            this.eglBase.createDummyPbufferSurface();
            this.eglBase.makeCurrent();
            this.shader = new GlShader(VERTEX_SHADER, FRAGMENT_SHADER);
            this.shader.useProgram();
            this.texMatrixLoc = this.shader.getUniformLocation("texMatrix");
            this.xUnitLoc = this.shader.getUniformLocation("xUnit");
            this.coeffsLoc = this.shader.getUniformLocation("coeffs");
            GLES20.glUniform1i(this.shader.getUniformLocation("oesTex"), 0);
            GlUtil.checkNoGLES2Error("Initialize fragment shader uniform values.");
            this.shader.setVertexAttribArray("in_pos", 2, DEVICE_RECTANGLE);
            this.shader.setVertexAttribArray("in_tc", 2, TEXTURE_RECTANGLE);
            this.eglBase.detachCurrent();
        }

        synchronized void convert(ByteBuffer byteBuffer, int i, int i2, int i3, int i4, float[] fArr) {
            if (this.released) {
                throw new IllegalStateException("YuvConverter.convert called on released object");
            } else if (i3 % 8 != 0) {
                throw new IllegalArgumentException("Invalid stride, must be a multiple of 8");
            } else if (i3 < i) {
                throw new IllegalArgumentException("Invalid stride, must >= width");
            } else {
                int i5 = (i + 3) / 4;
                int i6 = (i + 7) / 8;
                int i7 = (i2 + 1) / 2;
                int i8 = i2 + i7;
                if (byteBuffer.capacity() < i3 * i8) {
                    throw new IllegalArgumentException("YuvConverter.convert called with too small buffer");
                }
                float[] multiplyMatrices = RendererCommon.multiplyMatrices(fArr, RendererCommon.verticalFlipMatrix());
                if (!this.eglBase.hasSurface()) {
                    this.eglBase.createPbufferSurface(i3 / 4, i8);
                } else if (!(this.eglBase.surfaceWidth() == i3 / 4 && this.eglBase.surfaceHeight() == i8)) {
                    this.eglBase.releaseSurface();
                    this.eglBase.createPbufferSurface(i3 / 4, i8);
                }
                this.eglBase.makeCurrent();
                GLES20.glActiveTexture(33984);
                GLES20.glBindTexture(36197, i4);
                GLES20.glUniformMatrix4fv(this.texMatrixLoc, 1, false, multiplyMatrices, 0);
                GLES20.glViewport(0, 0, i5, i2);
                GLES20.glUniform2f(this.xUnitLoc, multiplyMatrices[0] / ((float) i), multiplyMatrices[1] / ((float) i));
                GLES20.glUniform4f(this.coeffsLoc, 0.299f, 0.587f, 0.114f, 0.0f);
                GLES20.glDrawArrays(5, 0, 4);
                GLES20.glViewport(0, i2, i6, i7);
                GLES20.glUniform2f(this.xUnitLoc, (ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT * multiplyMatrices[0]) / ((float) i), (multiplyMatrices[1] * ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT) / ((float) i));
                GLES20.glUniform4f(this.coeffsLoc, -0.169f, -0.331f, 0.499f, 0.5f);
                GLES20.glDrawArrays(5, 0, 4);
                GLES20.glViewport(i3 / 8, i2, i6, i7);
                GLES20.glUniform4f(this.coeffsLoc, 0.499f, -0.418f, -0.0813f, 0.5f);
                GLES20.glDrawArrays(5, 0, 4);
                GLES20.glReadPixels(0, 0, i3 / 4, i8, 6408, 5121, byteBuffer);
                GlUtil.checkNoGLES2Error("YuvConverter.convert");
                GLES20.glBindTexture(36197, 0);
                this.eglBase.detachCurrent();
            }
        }

        synchronized void release() {
            this.released = true;
            this.eglBase.makeCurrent();
            this.shader.release();
            this.eglBase.release();
        }
    }

    private SurfaceTextureHelper(Context context, Handler handler) {
        this.hasPendingTexture = false;
        this.isTextureInUse = false;
        this.isQuitting = false;
        this.setListenerRunnable = new C07042();
        if (handler.getLooper().getThread() != Thread.currentThread()) {
            throw new IllegalStateException("SurfaceTextureHelper must be created on the handler thread");
        }
        this.handler = handler;
        this.eglBase = EglBase.create(context, EglBase.CONFIG_PIXEL_BUFFER);
        try {
            this.eglBase.createDummyPbufferSurface();
            this.eglBase.makeCurrent();
            this.oesTextureId = GlUtil.generateTexture(36197);
            this.surfaceTexture = new SurfaceTexture(this.oesTextureId);
            this.surfaceTexture.setOnFrameAvailableListener(new C07053());
        } catch (RuntimeException e) {
            this.eglBase.release();
            handler.getLooper().quit();
            throw e;
        }
    }

    public static SurfaceTextureHelper create(String str, Context context) {
        HandlerThread handlerThread = new HandlerThread(str);
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        return (SurfaceTextureHelper) ThreadUtils.invokeAtFrontUninterruptibly(handler, new C07031(context, handler, str));
    }

    private YuvConverter getYuvConverter() {
        if (this.yuvConverter != null) {
            return this.yuvConverter;
        }
        YuvConverter yuvConverter;
        synchronized (this) {
            if (this.yuvConverter == null) {
                this.yuvConverter = new YuvConverter(this.eglBase.getEglBaseContext());
            }
            yuvConverter = this.yuvConverter;
        }
        return yuvConverter;
    }

    private void release() {
        if (this.handler.getLooper().getThread() != Thread.currentThread()) {
            throw new IllegalStateException("Wrong thread.");
        } else if (this.isTextureInUse || !this.isQuitting) {
            throw new IllegalStateException("Unexpected release.");
        } else {
            synchronized (this) {
                if (this.yuvConverter != null) {
                    this.yuvConverter.release();
                }
            }
            GLES20.glDeleteTextures(1, new int[]{this.oesTextureId}, 0);
            this.surfaceTexture.release();
            this.eglBase.release();
            this.handler.getLooper().quit();
        }
    }

    private void tryDeliverTextureFrame() {
        if (this.handler.getLooper().getThread() != Thread.currentThread()) {
            throw new IllegalStateException("Wrong thread.");
        } else if (!this.isQuitting && this.hasPendingTexture && !this.isTextureInUse && this.listener != null) {
            this.isTextureInUse = true;
            this.hasPendingTexture = false;
            updateTexImage();
            float[] fArr = new float[16];
            this.surfaceTexture.getTransformMatrix(fArr);
            this.listener.onTextureFrameAvailable(this.oesTextureId, fArr, VERSION.SDK_INT >= 14 ? this.surfaceTexture.getTimestamp() : TimeUnit.MILLISECONDS.toNanos(SystemClock.elapsedRealtime()));
        }
    }

    private void updateTexImage() {
        synchronized (EglBase.lock) {
            this.surfaceTexture.updateTexImage();
        }
    }

    public void dispose() {
        Logging.m776d(TAG, "dispose()");
        ThreadUtils.invokeAtFrontUninterruptibly(this.handler, new C07086());
    }

    public Handler getHandler() {
        return this.handler;
    }

    public SurfaceTexture getSurfaceTexture() {
        return this.surfaceTexture;
    }

    public boolean isTextureInUse() {
        return this.isTextureInUse;
    }

    public void returnTextureFrame() {
        this.handler.post(new C07075());
    }

    public void startListening(OnTextureFrameAvailableListener onTextureFrameAvailableListener) {
        if (this.listener == null && this.pendingListener == null) {
            this.pendingListener = onTextureFrameAvailableListener;
            this.handler.post(this.setListenerRunnable);
            return;
        }
        throw new IllegalStateException("SurfaceTextureHelper listener has already been set.");
    }

    public void stopListening() {
        Logging.m776d(TAG, "stopListening()");
        this.handler.removeCallbacks(this.setListenerRunnable);
        ThreadUtils.invokeAtFrontUninterruptibly(this.handler, new C07064());
    }

    public void textureToYUV(ByteBuffer byteBuffer, int i, int i2, int i3, int i4, float[] fArr) {
        if (i4 != this.oesTextureId) {
            throw new IllegalStateException("textureToByteBuffer called with unexpected textureId");
        }
        getYuvConverter().convert(byteBuffer, i, i2, i3, i4, fArr);
    }
}
