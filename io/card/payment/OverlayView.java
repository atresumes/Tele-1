package io.card.payment;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.toolbox.ImageRequest;
import io.card.payment.i18n.LocalizedStrings;
import io.card.payment.i18n.StringKey;
import java.lang.ref.WeakReference;

class OverlayView extends View {
    private static final Orientation[] GRADIENT_ORIENTATIONS = new Orientation[]{Orientation.TOP_BOTTOM, Orientation.LEFT_RIGHT, Orientation.BOTTOM_TOP, Orientation.RIGHT_LEFT};
    private static final String TAG = OverlayView.class.getSimpleName();
    private int guideColor;
    private boolean hideCardIOLogo;
    private Bitmap mBitmap;
    private Rect mCameraPreviewRect;
    private DetectionInfo mDInfo;
    private CreditCard mDetectedCard;
    private GradientDrawable mGradientDrawable;
    private Rect mGuide;
    private final Paint mGuidePaint;
    private final Paint mLockedBackgroundPaint;
    private Path mLockedBackgroundPath;
    private final Logo mLogo;
    private Rect mLogoRect;
    private int mRotation;
    private int mRotationFlip;
    private float mScale = DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;
    private final WeakReference<CardIOActivity> mScanActivityRef;
    private final boolean mShowTorch;
    private int mState;
    private final Torch mTorch;
    private Rect mTorchRect;
    private String scanInstructions;

    public OverlayView(CardIOActivity captureActivity, AttributeSet attributeSet, boolean showTorch) {
        super(captureActivity, attributeSet);
        this.mShowTorch = showTorch;
        this.mScanActivityRef = new WeakReference(captureActivity);
        this.mRotationFlip = 1;
        this.mScale = getResources().getDisplayMetrics().density / 1.5f;
        this.mTorch = new Torch(70.0f * this.mScale, 50.0f * this.mScale);
        this.mLogo = new Logo(captureActivity);
        this.mGuidePaint = new Paint(1);
        this.mLockedBackgroundPaint = new Paint(1);
        this.mLockedBackgroundPaint.clearShadowLayer();
        this.mLockedBackgroundPaint.setStyle(Style.FILL);
        this.mLockedBackgroundPaint.setColor(-1157627904);
        this.scanInstructions = LocalizedStrings.getString(StringKey.SCAN_GUIDE);
    }

    public void setGuideColor(int color) {
        this.guideColor = color;
    }

    public void setHideCardIOLogo(boolean hide) {
        this.hideCardIOLogo = hide;
    }

    public void setScanInstructions(String scanInstructions) {
        this.scanInstructions = scanInstructions;
    }

    public void setGuideAndRotation(Rect rect, int rotation) {
        Point topEdgeUIOffset;
        Log.d(TAG, "setGuideAndRotation: " + rect + ", " + rotation);
        this.mRotation = rotation;
        this.mGuide = rect;
        invalidate();
        if (this.mRotation % 180 != 0) {
            topEdgeUIOffset = new Point((int) (this.mScale * 40.0f), (int) (this.mScale * 60.0f));
            this.mRotationFlip = -1;
        } else {
            topEdgeUIOffset = new Point((int) (this.mScale * 60.0f), (int) (this.mScale * 40.0f));
            this.mRotationFlip = 1;
        }
        if (this.mCameraPreviewRect != null) {
            Log.d(TAG, "" + this.mCameraPreviewRect + ", " + topEdgeUIOffset + ", " + this.mCameraPreviewRect + ", " + topEdgeUIOffset);
            this.mTorchRect = Util.rectGivenCenter(new Point(this.mCameraPreviewRect.left + topEdgeUIOffset.x, this.mCameraPreviewRect.top + topEdgeUIOffset.y), (int) (70.0f * this.mScale), (int) (this.mScale * 50.0f));
            this.mLogoRect = Util.rectGivenCenter(new Point(this.mCameraPreviewRect.right - topEdgeUIOffset.x, this.mCameraPreviewRect.top + topEdgeUIOffset.y), (int) (100.0f * this.mScale), (int) (this.mScale * 50.0f));
            this.mGradientDrawable = new GradientDrawable(GRADIENT_ORIENTATIONS[(this.mRotation / 90) % 4], new int[]{-1, ViewCompat.MEASURED_STATE_MASK});
            this.mGradientDrawable.setGradientType(0);
            this.mGradientDrawable.setBounds(this.mGuide);
            this.mGradientDrawable.setAlpha(50);
            this.mLockedBackgroundPath = new Path();
            this.mLockedBackgroundPath.addRect(new RectF(this.mCameraPreviewRect), Direction.CW);
            this.mLockedBackgroundPath.addRect(new RectF(this.mGuide), Direction.CCW);
        }
    }

    public void setBitmap(Bitmap bitmap) {
        if (this.mBitmap != null) {
            this.mBitmap.recycle();
        }
        this.mBitmap = bitmap;
        if (this.mBitmap != null) {
            decorateBitmap();
        }
    }

    public Bitmap getBitmap() {
        return this.mBitmap;
    }

    public void setDetectionInfo(DetectionInfo dinfo) {
        if (!(this.mDInfo == null || this.mDInfo.sameEdgesAs(dinfo))) {
            invalidate();
        }
        this.mDInfo = dinfo;
    }

    public Bitmap getCardImage() {
        if (this.mBitmap == null || this.mBitmap.isRecycled()) {
            return null;
        }
        return Bitmap.createBitmap(this.mBitmap, 0, 0, this.mBitmap.getWidth(), this.mBitmap.getHeight());
    }

    private Rect guideStrokeRect(int x1, int y1, int x2, int y2) {
        int t2 = (int) (8.0f * this.mScale);
        Rect r = new Rect();
        r.left = Math.min(x1, x2) - t2;
        r.right = Math.max(x1, x2) + t2;
        r.top = Math.min(y1, y2) - t2;
        r.bottom = Math.max(y1, y2) + t2;
        return r;
    }

    public void onDraw(Canvas canvas) {
        if (this.mGuide != null && this.mCameraPreviewRect != null) {
            int tickLength;
            canvas.save();
            this.mGradientDrawable.draw(canvas);
            if (this.mRotation == 0 || this.mRotation == 180) {
                tickLength = (this.mGuide.bottom - this.mGuide.top) / 4;
            } else {
                tickLength = (this.mGuide.right - this.mGuide.left) / 4;
            }
            if (this.mDInfo != null && this.mDInfo.numVisibleEdges() == 4) {
                canvas.drawPath(this.mLockedBackgroundPath, this.mLockedBackgroundPaint);
            }
            this.mGuidePaint.clearShadowLayer();
            this.mGuidePaint.setStyle(Style.FILL);
            this.mGuidePaint.setColor(this.guideColor);
            canvas.drawRect(guideStrokeRect(this.mGuide.left, this.mGuide.top, this.mGuide.left + tickLength, this.mGuide.top), this.mGuidePaint);
            canvas.drawRect(guideStrokeRect(this.mGuide.left, this.mGuide.top, this.mGuide.left, this.mGuide.top + tickLength), this.mGuidePaint);
            canvas.drawRect(guideStrokeRect(this.mGuide.right, this.mGuide.top, this.mGuide.right - tickLength, this.mGuide.top), this.mGuidePaint);
            canvas.drawRect(guideStrokeRect(this.mGuide.right, this.mGuide.top, this.mGuide.right, this.mGuide.top + tickLength), this.mGuidePaint);
            canvas.drawRect(guideStrokeRect(this.mGuide.left, this.mGuide.bottom, this.mGuide.left + tickLength, this.mGuide.bottom), this.mGuidePaint);
            canvas.drawRect(guideStrokeRect(this.mGuide.left, this.mGuide.bottom, this.mGuide.left, this.mGuide.bottom - tickLength), this.mGuidePaint);
            canvas.drawRect(guideStrokeRect(this.mGuide.right, this.mGuide.bottom, this.mGuide.right - tickLength, this.mGuide.bottom), this.mGuidePaint);
            canvas.drawRect(guideStrokeRect(this.mGuide.right, this.mGuide.bottom, this.mGuide.right, this.mGuide.bottom - tickLength), this.mGuidePaint);
            if (this.mDInfo != null) {
                if (this.mDInfo.topEdge) {
                    canvas.drawRect(guideStrokeRect(this.mGuide.left, this.mGuide.top, this.mGuide.right, this.mGuide.top), this.mGuidePaint);
                }
                if (this.mDInfo.bottomEdge) {
                    canvas.drawRect(guideStrokeRect(this.mGuide.left, this.mGuide.bottom, this.mGuide.right, this.mGuide.bottom), this.mGuidePaint);
                }
                if (this.mDInfo.leftEdge) {
                    canvas.drawRect(guideStrokeRect(this.mGuide.left, this.mGuide.top, this.mGuide.left, this.mGuide.bottom), this.mGuidePaint);
                }
                if (this.mDInfo.rightEdge) {
                    canvas.drawRect(guideStrokeRect(this.mGuide.right, this.mGuide.top, this.mGuide.right, this.mGuide.bottom), this.mGuidePaint);
                }
                if (this.mDInfo.numVisibleEdges() < 3) {
                    float guideHeight = 34.0f * this.mScale;
                    float guideFontSize = 26.0f * this.mScale;
                    Util.setupTextPaintStyle(this.mGuidePaint);
                    this.mGuidePaint.setTextAlign(Align.CENTER);
                    this.mGuidePaint.setTextSize(guideFontSize);
                    canvas.translate((float) (this.mGuide.left + (this.mGuide.width() / 2)), (float) (this.mGuide.top + (this.mGuide.height() / 2)));
                    canvas.rotate((float) (this.mRotationFlip * this.mRotation));
                    if (!(this.scanInstructions == null || this.scanInstructions == "")) {
                        String[] lines = this.scanInstructions.split("\n");
                        float y = (-(((((float) (lines.length - 1)) * guideHeight) - guideFontSize) / ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT)) - 3.0f;
                        for (String drawText : lines) {
                            canvas.drawText(drawText, 0.0f, y, this.mGuidePaint);
                            y += guideHeight;
                        }
                    }
                }
            }
            canvas.restore();
            if (!this.hideCardIOLogo) {
                canvas.save();
                canvas.translate(this.mLogoRect.exactCenterX(), this.mLogoRect.exactCenterY());
                canvas.rotate((float) (this.mRotationFlip * this.mRotation));
                this.mLogo.draw(canvas, 100.0f * this.mScale, 50.0f * this.mScale);
                canvas.restore();
            }
            if (this.mShowTorch) {
                canvas.save();
                canvas.translate(this.mTorchRect.exactCenterX(), this.mTorchRect.exactCenterY());
                canvas.rotate((float) (this.mRotationFlip * this.mRotation));
                this.mTorch.draw(canvas);
                canvas.restore();
            }
        }
    }

    public void setDetectedCard(CreditCard creditCard) {
        this.mDetectedCard = creditCard;
    }

    public boolean onTouchEvent(MotionEvent event) {
        try {
            if ((event.getAction() & 255) == 0) {
                Point p = new Point((int) event.getX(), (int) event.getY());
                Rect r = Util.rectGivenCenter(p, 20, 20);
                Log.d(TAG, "onTouchEvent: " + p);
                if (this.mShowTorch && this.mTorchRect != null && Rect.intersects(this.mTorchRect, r)) {
                    Log.d(TAG, "torch touched");
                    ((CardIOActivity) this.mScanActivityRef.get()).toggleFlash();
                } else if (this.mLogoRect == null || !Rect.intersects(this.mLogoRect, r)) {
                    ((CardIOActivity) this.mScanActivityRef.get()).triggerAutoFocus();
                } else {
                    Log.d(TAG, "logo touched");
                }
            }
        } catch (NullPointerException e) {
            Log.d(TAG, "NullPointerException caught in onTouchEvent method");
        }
        return false;
    }

    private void decorateBitmap() {
        RectF roundedRect = new RectF(ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT, ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT, (float) (this.mBitmap.getWidth() - 2), (float) (this.mBitmap.getHeight() - 2));
        float cornerRadius = ((float) this.mBitmap.getHeight()) * 0.06666667f;
        Bitmap maskBitmap = Bitmap.createBitmap(this.mBitmap.getWidth(), this.mBitmap.getHeight(), Config.ARGB_8888);
        Canvas maskCanvas = new Canvas(maskBitmap);
        maskCanvas.drawColor(0);
        Paint maskPaint = new Paint(1);
        maskPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        maskPaint.setStyle(Style.FILL);
        maskCanvas.drawRoundRect(roundedRect, cornerRadius, cornerRadius, maskPaint);
        Paint paint = new Paint();
        paint.setFilterBitmap(false);
        Canvas canvas = new Canvas(this.mBitmap);
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        canvas.drawBitmap(maskBitmap, 0.0f, 0.0f, paint);
        paint.setXfermode(null);
        maskBitmap.recycle();
    }

    public void markupCard() {
        if (this.mBitmap != null) {
            if (this.mDetectedCard.flipped) {
                Matrix m = new Matrix();
                m.setRotate(180.0f, (float) (this.mBitmap.getWidth() / 2), (float) (this.mBitmap.getHeight() / 2));
                this.mBitmap = Bitmap.createBitmap(this.mBitmap, 0, 0, this.mBitmap.getWidth(), this.mBitmap.getHeight(), m, false);
            }
            Canvas bc = new Canvas(this.mBitmap);
            Paint paint = new Paint();
            Util.setupTextPaintStyle(paint);
            paint.setTextSize(28.0f * this.mScale);
            int len = this.mDetectedCard.cardNumber.length();
            float sf = ((float) this.mBitmap.getWidth()) / 428.0f;
            int yOffset = (int) ((((float) this.mDetectedCard.yoff) * sf) - 6.0f);
            for (int i = 0; i < len; i++) {
                bc.drawText("" + this.mDetectedCard.cardNumber.charAt(i), (float) ((int) (((float) this.mDetectedCard.xoff[i]) * sf)), (float) yOffset, paint);
            }
        }
    }

    public boolean isAnimating() {
        return this.mState != 0;
    }

    public void setCameraPreviewRect(Rect rect) {
        this.mCameraPreviewRect = rect;
    }

    public void setTorchOn(boolean b) {
        this.mTorch.setOn(b);
        invalidate();
    }

    public void setUseCardIOLogo(boolean useCardIOLogo) {
        this.mLogo.loadLogo(useCardIOLogo);
    }

    public Rect getTorchRect() {
        return this.mTorchRect;
    }
}
