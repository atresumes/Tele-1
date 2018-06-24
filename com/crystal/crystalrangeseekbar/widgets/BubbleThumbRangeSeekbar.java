package com.crystal.crystalrangeseekbar.widgets;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.animation.OvershootInterpolator;
import com.android.volley.toolbox.ImageRequest;
import com.example.crystalrangeseekbar.C0267R;

public class BubbleThumbRangeSeekbar extends CrystalRangeSeekbar {
    private boolean animate;
    private boolean isPressedLeftThumb;
    private boolean isPressedRightThumb;
    private BubbleRect thumbPressedRect;

    class C02591 implements AnimatorUpdateListener {
        C02591() {
        }

        public void onAnimationUpdate(ValueAnimator animation) {
            BubbleThumbRangeSeekbar.this.thumbPressedRect.left = ((Float) animation.getAnimatedValue("left")).floatValue();
            BubbleThumbRangeSeekbar.this.thumbPressedRect.right = ((Float) animation.getAnimatedValue("right")).floatValue();
            BubbleThumbRangeSeekbar.this.thumbPressedRect.top = ((Float) animation.getAnimatedValue("top")).floatValue();
            BubbleThumbRangeSeekbar.this.thumbPressedRect.bottom = ((Float) animation.getAnimatedValue("bottom")).floatValue();
            BubbleThumbRangeSeekbar.this.thumbPressedRect.imageWith = ((Float) animation.getAnimatedValue("width")).floatValue();
            BubbleThumbRangeSeekbar.this.thumbPressedRect.imageHeight = ((Float) animation.getAnimatedValue("height")).floatValue();
            BubbleThumbRangeSeekbar.this.invalidate();
        }
    }

    class C02602 implements Runnable {
        C02602() {
        }

        public void run() {
            BubbleThumbRangeSeekbar.this.animate = false;
        }
    }

    class C02613 implements AnimatorUpdateListener {
        C02613() {
        }

        public void onAnimationUpdate(ValueAnimator animation) {
            BubbleThumbRangeSeekbar.this.thumbPressedRect.left = ((Float) animation.getAnimatedValue("left")).floatValue();
            BubbleThumbRangeSeekbar.this.thumbPressedRect.right = ((Float) animation.getAnimatedValue("right")).floatValue();
            BubbleThumbRangeSeekbar.this.thumbPressedRect.top = ((Float) animation.getAnimatedValue("top")).floatValue();
            BubbleThumbRangeSeekbar.this.thumbPressedRect.bottom = ((Float) animation.getAnimatedValue("bottom")).floatValue();
            BubbleThumbRangeSeekbar.this.thumbPressedRect.imageWith = ((Float) animation.getAnimatedValue("width")).floatValue();
            BubbleThumbRangeSeekbar.this.thumbPressedRect.imageHeight = ((Float) animation.getAnimatedValue("height")).floatValue();
            BubbleThumbRangeSeekbar.this.invalidate();
        }
    }

    class C02624 implements Runnable {
        C02624() {
        }

        public void run() {
            BubbleThumbRangeSeekbar.this.animate = false;
            BubbleThumbRangeSeekbar.this.isPressedLeftThumb = false;
            BubbleThumbRangeSeekbar.this.isPressedRightThumb = false;
        }
    }

    private class BubbleRect {
        public float bottom;
        public float imageHeight;
        public float imageWith;
        public float left;
        public float right;
        public float top;

        private BubbleRect() {
        }
    }

    public BubbleThumbRangeSeekbar(Context context) {
        super(context);
    }

    public BubbleThumbRangeSeekbar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BubbleThumbRangeSeekbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void init() {
        this.thumbPressedRect = new BubbleRect();
        super.init();
    }

    protected void touchDown(float x, float y) {
        super.touchDown(x, y);
        this.animate = true;
        if (Thumb.MIN.equals(getPressedThumb())) {
            this.isPressedLeftThumb = true;
            startAnimationUp(Thumb.MIN);
        } else if (Thumb.MAX.equals(getPressedThumb())) {
            this.isPressedRightThumb = true;
            startAnimationUp(Thumb.MAX);
        }
    }

    protected void touchUp(float x, float y) {
        super.touchUp(x, y);
        this.animate = true;
        if (Thumb.MIN.equals(getPressedThumb())) {
            startAnimationDown(Thumb.MIN);
        } else {
            startAnimationDown(Thumb.MAX);
        }
    }

    protected void drawLeftThumbWithColor(Canvas canvas, Paint paint, RectF rect) {
        if (this.isPressedLeftThumb) {
            if (this.animate) {
                rect.left = this.thumbPressedRect.left;
                rect.right = this.thumbPressedRect.right;
                rect.top = this.thumbPressedRect.top;
                rect.bottom = this.thumbPressedRect.bottom;
            } else {
                rect.left -= (getBubbleWith() / ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT) - (getThumbWidth() / ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT);
                rect.right = rect.left + getBubbleWith();
                rect.top = getLeftThumbRect().top - ((getBubbleHeight() / ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT) - (getThumbHeight() / ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT));
                rect.bottom = getLeftThumbRect().bottom + ((getBubbleHeight() / ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT) - (getThumbHeight() / ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT));
            }
            canvas.drawOval(rect, paint);
            return;
        }
        canvas.drawOval(rect, paint);
    }

    protected void drawRightThumbWithColor(Canvas canvas, Paint paint, RectF rect) {
        if (this.isPressedRightThumb) {
            if (this.animate) {
                rect.left = this.thumbPressedRect.left;
                rect.right = this.thumbPressedRect.right;
                rect.top = this.thumbPressedRect.top;
                rect.bottom = this.thumbPressedRect.bottom;
            } else {
                rect.left -= (getBubbleWith() / ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT) - (getThumbWidth() / ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT);
                rect.right = rect.left + getBubbleWith();
                rect.top = getRightThumbRect().top - ((getBubbleHeight() / ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT) - (getThumbHeight() / ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT));
                rect.bottom = getRightThumbRect().bottom + ((getBubbleHeight() / ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT) - (getThumbHeight() / ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT));
            }
            canvas.drawOval(rect, paint);
            return;
        }
        canvas.drawOval(rect, paint);
    }

    protected void drawLeftThumbWithImage(Canvas canvas, Paint paint, RectF rect, Bitmap image) {
        if (this.isPressedLeftThumb) {
            if (this.animate) {
                image = resizeImage((int) this.thumbPressedRect.imageWith, (int) this.thumbPressedRect.imageHeight, image);
                rect.top = this.thumbPressedRect.top;
                rect.left = this.thumbPressedRect.left;
            } else {
                image = resizeImage((int) getBubbleWith(), (int) getBubbleHeight(), image);
                rect.top = getLeftThumbRect().top - ((getBubbleHeight() / ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT) - (getThumbHeight() / ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT));
                rect.left -= (getBubbleWith() / ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT) - (getThumbWidth() / ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT);
            }
            canvas.drawBitmap(image, rect.left, rect.top, paint);
            return;
        }
        canvas.drawBitmap(image, rect.left, rect.top, paint);
    }

    protected void drawRightThumbWithImage(Canvas canvas, Paint paint, RectF rect, Bitmap image) {
        if (this.isPressedRightThumb) {
            if (this.animate) {
                image = resizeImage((int) this.thumbPressedRect.imageWith, (int) this.thumbPressedRect.imageHeight, image);
                rect.top = this.thumbPressedRect.top;
                rect.left = this.thumbPressedRect.left;
            } else {
                image = resizeImage((int) getBubbleWith(), (int) getBubbleHeight(), image);
                rect.top = getRightThumbRect().top - ((getBubbleHeight() / ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT) - (getThumbHeight() / ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT));
                rect.left -= (getBubbleWith() / ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT) - (getThumbWidth() / ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT);
            }
            canvas.drawBitmap(image, rect.left, rect.top, paint);
            return;
        }
        canvas.drawBitmap(image, rect.left, rect.top, paint);
    }

    protected float getBubbleWith() {
        return getResources().getDimension(C0267R.dimen.bubble_thumb_width);
    }

    protected float getBubbleHeight() {
        return getResources().getDimension(C0267R.dimen.bubble_thumb_height);
    }

    protected void startAnimationUp(Thumb thumb) {
        RectF fromRect;
        BubbleRect toRect = new BubbleRect();
        if (Thumb.MIN.equals(thumb)) {
            fromRect = getLeftThumbRect();
        } else {
            fromRect = getRightThumbRect();
        }
        toRect.left = fromRect.left - ((getBubbleWith() / ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT) - (getThumbWidth() / ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT));
        toRect.right = toRect.left + getBubbleWith();
        toRect.top = fromRect.top - ((getBubbleHeight() / ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT) - (getThumbHeight() / ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT));
        toRect.bottom = fromRect.bottom + ((getBubbleHeight() / ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT) - (getThumbHeight() / ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT));
        PropertyValuesHolder leftValueHolder = PropertyValuesHolder.ofFloat("left", new float[]{fromRect.left, toRect.left});
        PropertyValuesHolder rightValueHolder = PropertyValuesHolder.ofFloat("right", new float[]{fromRect.right, toRect.right});
        PropertyValuesHolder topValueHolder = PropertyValuesHolder.ofFloat("top", new float[]{fromRect.top, toRect.top});
        PropertyValuesHolder bottomValueHolder = PropertyValuesHolder.ofFloat("bottom", new float[]{fromRect.bottom, toRect.bottom});
        PropertyValuesHolder imageWithValueHolder = PropertyValuesHolder.ofFloat("width", new float[]{getThumbWidth(), getBubbleWith()});
        PropertyValuesHolder imageHeightValueHolder = PropertyValuesHolder.ofFloat("height", new float[]{getThumbHeight(), getBubbleHeight()});
        ValueAnimator animation = ValueAnimator.ofPropertyValuesHolder(new PropertyValuesHolder[]{leftValueHolder, rightValueHolder, topValueHolder, bottomValueHolder, imageWithValueHolder, imageHeightValueHolder});
        animation.setDuration(200);
        animation.setInterpolator(new OvershootInterpolator(5.0f));
        animation.addUpdateListener(new C02591());
        animation.start();
        new Handler().postDelayed(new C02602(), 200);
    }

    protected void startAnimationDown(Thumb thumb) {
        RectF fromRect;
        RectF toRect = new RectF();
        if (Thumb.MIN.equals(thumb)) {
            fromRect = getLeftThumbRect();
        } else {
            fromRect = getRightThumbRect();
        }
        toRect.left = fromRect.left + ((getBubbleWith() / ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT) - (getThumbWidth() / ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT));
        toRect.right = toRect.left + getThumbWidth();
        toRect.top = 0.0f;
        toRect.bottom = getThumbHeight();
        PropertyValuesHolder leftValueHolder = PropertyValuesHolder.ofFloat("left", new float[]{fromRect.left, toRect.left});
        PropertyValuesHolder rightValueHolder = PropertyValuesHolder.ofFloat("right", new float[]{fromRect.right, toRect.right});
        PropertyValuesHolder topValueHolder = PropertyValuesHolder.ofFloat("top", new float[]{fromRect.top, toRect.top});
        PropertyValuesHolder bottomValueHolder = PropertyValuesHolder.ofFloat("bottom", new float[]{fromRect.bottom, toRect.bottom});
        PropertyValuesHolder imageWithValueHolder = PropertyValuesHolder.ofFloat("width", new float[]{getBubbleWith(), getThumbWidth()});
        PropertyValuesHolder imageHeightValueHolder = PropertyValuesHolder.ofFloat("height", new float[]{getBubbleHeight(), getThumbHeight()});
        ValueAnimator animation = ValueAnimator.ofPropertyValuesHolder(new PropertyValuesHolder[]{leftValueHolder, rightValueHolder, topValueHolder, bottomValueHolder, imageWithValueHolder, imageHeightValueHolder});
        animation.setDuration(300);
        animation.setInterpolator(new OvershootInterpolator(3.0f));
        animation.addUpdateListener(new C02613());
        animation.start();
        new Handler().postDelayed(new C02624(), 300);
    }

    private Bitmap resizeImage(int iconWidth, int iconHeight, Bitmap bmp) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        float scaleWidth = ((float) iconWidth) / ((float) width);
        float scaleHeight = ((float) iconHeight) / ((float) height);
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);
    }
}
