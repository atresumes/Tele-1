package io.card.payment;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.drawable.shapes.RoundRectShape;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import com.android.volley.toolbox.ImageRequest;
import java.util.Arrays;

class Torch {
    private static final String TAG = Torch.class.getSimpleName();
    private float mHeight;
    private boolean mOn = false;
    private float mWidth;

    public Torch(float width, float height) {
        this.mWidth = width;
        this.mHeight = height;
    }

    public void draw(Canvas canvas) {
        canvas.save();
        canvas.translate((-this.mWidth) / ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT, (-this.mHeight) / ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT);
        Paint borderPaint = new Paint();
        borderPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        borderPaint.setStyle(Style.STROKE);
        borderPaint.setAntiAlias(true);
        borderPaint.setStrokeWidth(1.5f);
        Paint fillPaint = new Paint();
        fillPaint.setStyle(Style.FILL);
        fillPaint.setColor(-1);
        if (this.mOn) {
            fillPaint.setAlpha(192);
        } else {
            fillPaint.setAlpha(96);
        }
        float[] outerRadii = new float[8];
        Arrays.fill(outerRadii, 5.0f);
        RoundRectShape buttonShape = new RoundRectShape(outerRadii, null, null);
        buttonShape.resize(this.mWidth, this.mHeight);
        buttonShape.draw(canvas, fillPaint);
        buttonShape.draw(canvas, borderPaint);
        Paint boltPaint = new Paint();
        boltPaint.setStyle(Style.FILL_AND_STROKE);
        boltPaint.setAntiAlias(true);
        if (this.mOn) {
            boltPaint.setColor(-1);
        } else {
            boltPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        }
        Path boltPath = createBoltPath();
        Matrix m = new Matrix();
        float boltHeight = 0.8f * this.mHeight;
        m.postScale(boltHeight, boltHeight);
        boltPath.transform(m);
        canvas.translate(this.mWidth / ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT, this.mHeight / ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT);
        canvas.drawPath(boltPath, boltPaint);
        canvas.restore();
    }

    public void setOn(boolean on) {
        Log.d(TAG, "Torch " + (on ? "ON" : "OFF"));
        this.mOn = on;
    }

    private static Path createBoltPath() {
        Path p = new Path();
        p.moveTo(10.0f, 0.0f);
        p.lineTo(0.0f, 11.0f);
        p.lineTo(6.0f, 11.0f);
        p.lineTo(ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT, 20.0f);
        p.lineTo(13.0f, 8.0f);
        p.lineTo(7.0f, 8.0f);
        p.lineTo(10.0f, 0.0f);
        p.setLastPoint(10.0f, 0.0f);
        Matrix m = new Matrix();
        m.postTranslate(-6.5f, -10.0f);
        m.postScale(0.05f, 0.05f);
        p.transform(m);
        return p;
    }
}
