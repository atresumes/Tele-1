package org.webrtc.sinch;

import android.graphics.SurfaceTexture;
import android.view.Surface;

public abstract class EglBase {
    public static final int[] CONFIG_PIXEL_BUFFER = new int[]{12324, 8, 12323, 8, 12322, 8, 12352, 4, 12339, 1, 12344};
    public static final int[] CONFIG_PIXEL_RGBA_BUFFER = new int[]{12324, 8, 12323, 8, 12322, 8, 12321, 8, 12352, 4, 12339, 1, 12344};
    public static final int[] CONFIG_PLAIN = new int[]{12324, 8, 12323, 8, 12322, 8, 12352, 4, 12344};
    public static final int[] CONFIG_RECORDABLE = new int[]{12324, 8, 12323, 8, 12322, 8, 12352, 4, EGL_RECORDABLE_ANDROID, 1, 12344};
    public static final int[] CONFIG_RGBA = new int[]{12324, 8, 12323, 8, 12322, 8, 12321, 8, 12352, 4, 12344};
    private static final int EGL_OPENGL_ES2_BIT = 4;
    private static final int EGL_RECORDABLE_ANDROID = 12610;
    public static final Object lock = new Object();

    public class Context {
    }

    public static EglBase create() {
        return create(null, CONFIG_PLAIN);
    }

    public static EglBase create(Context context) {
        return create(context, CONFIG_PLAIN);
    }

    public static EglBase create(Context context, int[] iArr) {
        return (EglBase14.isEGL14Supported() && (context == null || (context instanceof org.webrtc.sinch.EglBase14.Context))) ? new EglBase14((org.webrtc.sinch.EglBase14.Context) context, iArr) : new EglBase10((org.webrtc.sinch.EglBase10.Context) context, iArr);
    }

    public abstract void createDummyPbufferSurface();

    public abstract void createPbufferSurface(int i, int i2);

    public abstract void createSurface(SurfaceTexture surfaceTexture);

    public abstract void createSurface(Surface surface);

    public abstract void detachCurrent();

    public abstract Context getEglBaseContext();

    public abstract boolean hasSurface();

    public abstract void makeCurrent();

    public abstract void release();

    public abstract void releaseSurface();

    public abstract int surfaceHeight();

    public abstract int surfaceWidth();

    public abstract void swapBuffers();
}
