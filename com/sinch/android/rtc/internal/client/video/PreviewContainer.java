package com.sinch.android.rtc.internal.client.video;

import android.content.Context;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

class PreviewContainer extends FrameLayout {
    private static final String TAG = PreviewContainer.class.getSimpleName();
    private SurfaceView mCameraPreview;

    class C05541 implements Callback {
        C05541() {
        }

        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
            Size fitFrame = PreviewContainer.fitFrame(new Size(i2, i3), new Size(PreviewContainer.this.getWidth(), PreviewContainer.this.getHeight()));
            final LayoutParams layoutParams = new LayoutParams(fitFrame.width, fitFrame.height);
            layoutParams.gravity = 17;
            PreviewContainer.this.mCameraPreview.post(new Runnable() {
                public void run() {
                    PreviewContainer.this.mCameraPreview.setLayoutParams(layoutParams);
                }
            });
        }

        public void surfaceCreated(SurfaceHolder surfaceHolder) {
        }

        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        }
    }

    class Size {
        public final int height;
        public final int width;

        public Size(int i, int i2) {
            this.width = i;
            this.height = i2;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Size size = (Size) obj;
            return this.width == size.width && this.height == size.height;
        }

        public int hashCode() {
            return (this.width * 31) + this.height;
        }

        public String toString() {
            return "width:" + this.width + " height:" + this.height;
        }
    }

    public PreviewContainer(Context context) {
        super(context);
        this.mCameraPreview = new SurfaceView(context);
        setLayoutParams(new LayoutParams(-1, -1));
        getPreviewHolder().addCallback(new C05541());
        addView(this.mCameraPreview);
    }

    static Size fillFrame(Size size, Size size2) {
        double d = size.height == 0 ? 0.0d : ((double) size.width) / ((double) size.height);
        double d2 = size2.height == 0 ? 0.0d : ((double) size2.width) / ((double) size2.height);
        if (d == 0.0d || d2 == 0.0d) {
            Log.e(TAG, "Zero aspect ration when filling preview.");
            return new Size(0, 0);
        }
        int i;
        int i2;
        int i3;
        if (d2 > d) {
            i3 = size2.width;
            i = (int) (((double) i3) / d);
            i2 = i3;
        } else {
            i3 = size2.height;
            i2 = (int) (d * ((double) i3));
            i = i3;
        }
        return new Size(i2, i);
    }

    static Size fitFrame(Size size, Size size2) {
        double d = size.height == 0 ? 0.0d : ((double) size.width) / ((double) size.height);
        double d2 = size2.height == 0 ? 0.0d : ((double) size2.width) / ((double) size2.height);
        if (d == 0.0d || d2 == 0.0d) {
            Log.e(TAG, "Zero aspect ratio when fitting preview.");
            return new Size(0, 0);
        }
        int i;
        int i2;
        int i3;
        if (d2 > d) {
            i3 = size2.height;
            i = (int) (d * ((double) i3));
            i2 = i3;
        } else {
            i3 = size2.width;
            i2 = (int) (((double) i3) / d);
            i = i3;
        }
        return new Size(i, i2);
    }

    public SurfaceHolder getPreviewHolder() {
        return this.mCameraPreview.getHolder();
    }
}
