package com.crystal.crystalrangeseekbar.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import com.android.volley.toolbox.ImageRequest;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.example.crystalrangeseekbar.C0267R;

public class CrystalRangeSeekbar extends View {
    private static final int INVALID_POINTER_ID = 255;
    private final float NO_FIXED_GAP;
    private final float NO_STEP;
    private Paint _paint;
    private RectF _rect;
    private float absoluteMaxValue;
    private float absoluteMinValue;
    private int barColor;
    private float barHeight;
    private int barHighlightColor;
    private float barPadding;
    private float cornerRadius;
    private int dataType;
    private float fixGap;
    private float gap;
    private Drawable leftDrawable;
    private Drawable leftDrawablePressed;
    private Bitmap leftThumb;
    private int leftThumbColor;
    private int leftThumbColorNormal;
    private int leftThumbColorPressed;
    private Bitmap leftThumbPressed;
    private int mActivePointerId;
    private boolean mIsDragging;
    private float maxStartValue;
    private float maxValue;
    private float minStartValue;
    private float minValue;
    private double normalizedMaxValue;
    private double normalizedMinValue;
    private OnRangeSeekbarChangeListener onRangeSeekbarChangeListener;
    private OnRangeSeekbarFinalValueListener onRangeSeekbarFinalValueListener;
    private int pointerIndex;
    private Thumb pressedThumb;
    private RectF rectLeftThumb;
    private RectF rectRightThumb;
    private Drawable rightDrawable;
    private Drawable rightDrawablePressed;
    private Bitmap rightThumb;
    private int rightThumbColor;
    private int rightThumbColorNormal;
    private int rightThumbColorPressed;
    private Bitmap rightThumbPressed;
    private float steps;
    private float thumbHeight;
    private float thumbWidth;

    public static final class DataType {
        public static final int BYTE = 5;
        public static final int DOUBLE = 1;
        public static final int FLOAT = 3;
        public static final int INTEGER = 2;
        public static final int LONG = 0;
        public static final int SHORT = 4;
    }

    protected enum Thumb {
        MIN,
        MAX
    }

    public CrystalRangeSeekbar(Context context) {
        this(context, null);
    }

    public CrystalRangeSeekbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CrystalRangeSeekbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.NO_STEP = -1.0f;
        this.NO_FIXED_GAP = -1.0f;
        this.mActivePointerId = 255;
        this.normalizedMinValue = 0.0d;
        this.normalizedMaxValue = 100.0d;
        if (!isInEditMode()) {
            TypedArray array = context.obtainStyledAttributes(attrs, C0267R.styleable.CrystalRangeSeekbar);
            try {
                this.cornerRadius = getCornerRadius(array);
                this.minValue = getMinValue(array);
                this.maxValue = getMaxValue(array);
                this.minStartValue = getMinStartValue(array);
                this.maxStartValue = getMaxStartValue(array);
                this.steps = getSteps(array);
                this.gap = getGap(array);
                this.fixGap = getFixedGap(array);
                this.barColor = getBarColor(array);
                this.barHighlightColor = getBarHighlightColor(array);
                this.leftThumbColorNormal = getLeftThumbColor(array);
                this.rightThumbColorNormal = getRightThumbColor(array);
                this.leftThumbColorPressed = getLeftThumbColorPressed(array);
                this.rightThumbColorPressed = getRightThumbColorPressed(array);
                this.leftDrawable = getLeftDrawable(array);
                this.rightDrawable = getRightDrawable(array);
                this.leftDrawablePressed = getLeftDrawablePressed(array);
                this.rightDrawablePressed = getRightDrawablePressed(array);
                this.dataType = getDataType(array);
                init();
            } finally {
                array.recycle();
            }
        }
    }

    protected void init() {
        this.absoluteMinValue = this.minValue;
        this.absoluteMaxValue = this.maxValue;
        this.leftThumbColor = this.leftThumbColorNormal;
        this.rightThumbColor = this.rightThumbColorNormal;
        this.leftThumb = getBitmap(this.leftDrawable);
        this.rightThumb = getBitmap(this.rightDrawable);
        this.leftThumbPressed = getBitmap(this.leftDrawablePressed);
        this.rightThumbPressed = getBitmap(this.rightDrawablePressed);
        this.leftThumbPressed = this.leftThumbPressed == null ? this.leftThumb : this.leftThumbPressed;
        this.rightThumbPressed = this.rightThumbPressed == null ? this.rightThumb : this.rightThumbPressed;
        this.gap = Math.max(0.0f, Math.min(this.gap, this.absoluteMaxValue - this.absoluteMinValue));
        this.gap = (this.gap / (this.absoluteMaxValue - this.absoluteMinValue)) * 100.0f;
        if (this.fixGap != -1.0f) {
            this.fixGap = Math.min(this.fixGap, this.absoluteMaxValue);
            this.fixGap = (this.fixGap / (this.absoluteMaxValue - this.absoluteMinValue)) * 100.0f;
            addFixGap(true);
        }
        this.thumbWidth = getThumbWidth();
        this.thumbHeight = getThumbHeight();
        this.barHeight = getBarHeight();
        this.barPadding = getBarPadding();
        this._paint = new Paint(1);
        this._rect = new RectF();
        this.rectLeftThumb = new RectF();
        this.rectRightThumb = new RectF();
        this.pressedThumb = null;
        setMinStartValue();
        setMaxStartValue();
    }

    public CrystalRangeSeekbar setCornerRadius(float cornerRadius) {
        this.cornerRadius = cornerRadius;
        return this;
    }

    public CrystalRangeSeekbar setMinValue(float minValue) {
        this.minValue = minValue;
        this.absoluteMinValue = minValue;
        return this;
    }

    public CrystalRangeSeekbar setMaxValue(float maxValue) {
        this.maxValue = maxValue;
        this.absoluteMaxValue = maxValue;
        return this;
    }

    public CrystalRangeSeekbar setMinStartValue(float minStartValue) {
        this.minStartValue = minStartValue;
        return this;
    }

    public CrystalRangeSeekbar setMaxStartValue(float maxStartValue) {
        this.maxStartValue = maxStartValue;
        return this;
    }

    public CrystalRangeSeekbar setSteps(float steps) {
        this.steps = steps;
        return this;
    }

    public CrystalRangeSeekbar setGap(float gap) {
        this.gap = gap;
        return this;
    }

    public CrystalRangeSeekbar setFixGap(float fixGap) {
        this.fixGap = fixGap;
        return this;
    }

    public CrystalRangeSeekbar setBarColor(int barColor) {
        this.barColor = barColor;
        return this;
    }

    public CrystalRangeSeekbar setBarHighlightColor(int barHighlightColor) {
        this.barHighlightColor = barHighlightColor;
        return this;
    }

    public CrystalRangeSeekbar setLeftThumbColor(int leftThumbColorNormal) {
        this.leftThumbColorNormal = leftThumbColorNormal;
        return this;
    }

    public CrystalRangeSeekbar setLeftThumbHighlightColor(int leftThumbColorPressed) {
        this.leftThumbColorPressed = leftThumbColorPressed;
        return this;
    }

    public CrystalRangeSeekbar setLeftThumbDrawable(int resId) {
        setLeftThumbDrawable(ContextCompat.getDrawable(getContext(), resId));
        return this;
    }

    public CrystalRangeSeekbar setLeftThumbDrawable(Drawable drawable) {
        setLeftThumbBitmap(getBitmap(drawable));
        return this;
    }

    public CrystalRangeSeekbar setLeftThumbBitmap(Bitmap bitmap) {
        this.leftThumb = bitmap;
        return this;
    }

    public CrystalRangeSeekbar setLeftThumbHighlightDrawable(int resId) {
        setLeftThumbHighlightDrawable(ContextCompat.getDrawable(getContext(), resId));
        return this;
    }

    public CrystalRangeSeekbar setLeftThumbHighlightDrawable(Drawable drawable) {
        setLeftThumbHighlightBitmap(getBitmap(drawable));
        return this;
    }

    public CrystalRangeSeekbar setLeftThumbHighlightBitmap(Bitmap bitmap) {
        this.leftThumbPressed = bitmap;
        return this;
    }

    public CrystalRangeSeekbar setRightThumbColor(int rightThumbColorNormal) {
        this.rightThumbColorNormal = rightThumbColorNormal;
        return this;
    }

    public CrystalRangeSeekbar setRightThumbHighlightColor(int rightThumbColorPressed) {
        this.rightThumbColorPressed = rightThumbColorPressed;
        return this;
    }

    public CrystalRangeSeekbar setRightThumbDrawable(int resId) {
        setRightThumbDrawable(ContextCompat.getDrawable(getContext(), resId));
        return this;
    }

    public CrystalRangeSeekbar setRightThumbDrawable(Drawable drawable) {
        setRightThumbBitmap(getBitmap(drawable));
        return this;
    }

    public CrystalRangeSeekbar setRightThumbBitmap(Bitmap bitmap) {
        this.rightThumb = bitmap;
        return this;
    }

    public CrystalRangeSeekbar setRightThumbHighlightDrawable(int resId) {
        setRightThumbHighlightDrawable(ContextCompat.getDrawable(getContext(), resId));
        return this;
    }

    public CrystalRangeSeekbar setRightThumbHighlightDrawable(Drawable drawable) {
        setRightThumbHighlightBitmap(getBitmap(drawable));
        return this;
    }

    public CrystalRangeSeekbar setRightThumbHighlightBitmap(Bitmap bitmap) {
        this.rightThumbPressed = bitmap;
        return this;
    }

    public CrystalRangeSeekbar setDataType(int dataType) {
        this.dataType = dataType;
        return this;
    }

    public void setOnRangeSeekbarChangeListener(OnRangeSeekbarChangeListener onRangeSeekbarChangeListener) {
        this.onRangeSeekbarChangeListener = onRangeSeekbarChangeListener;
        if (this.onRangeSeekbarChangeListener != null) {
            this.onRangeSeekbarChangeListener.valueChanged(getSelectedMinValue(), getSelectedMaxValue());
        }
    }

    public void setOnRangeSeekbarFinalValueListener(OnRangeSeekbarFinalValueListener onRangeSeekbarFinalValueListener) {
        this.onRangeSeekbarFinalValueListener = onRangeSeekbarFinalValueListener;
    }

    public Number getSelectedMinValue() {
        double nv = this.normalizedMinValue;
        if (this.steps > 0.0f && this.steps <= this.absoluteMaxValue / ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT) {
            float stp = (this.steps / (this.absoluteMaxValue - this.absoluteMinValue)) * 100.0f;
            double mod = nv % ((double) stp);
            if (mod > ((double) (stp / ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT))) {
                nv = (nv - mod) + ((double) stp);
            } else {
                nv -= mod;
            }
        } else if (this.steps != -1.0f) {
            throw new IllegalStateException("steps out of range " + this.steps);
        }
        return formatValue(Double.valueOf(normalizedToValue(nv)));
    }

    public Number getSelectedMaxValue() {
        double nv = this.normalizedMaxValue;
        if (this.steps > 0.0f && this.steps <= this.absoluteMaxValue / ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT) {
            float stp = (this.steps / (this.absoluteMaxValue - this.absoluteMinValue)) * 100.0f;
            double mod = nv % ((double) stp);
            if (mod > ((double) (stp / ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT))) {
                nv = (nv - mod) + ((double) stp);
            } else {
                nv -= mod;
            }
        } else if (this.steps != -1.0f) {
            throw new IllegalStateException("steps out of range " + this.steps);
        }
        return formatValue(Double.valueOf(normalizedToValue(nv)));
    }

    public void apply() {
        float height;
        this.normalizedMinValue = 0.0d;
        this.normalizedMaxValue = 100.0d;
        this.gap = Math.max(0.0f, Math.min(this.gap, this.absoluteMaxValue - this.absoluteMinValue));
        this.gap = (this.gap / (this.absoluteMaxValue - this.absoluteMinValue)) * 100.0f;
        if (this.fixGap != -1.0f) {
            this.fixGap = Math.min(this.fixGap, this.absoluteMaxValue);
            this.fixGap = (this.fixGap / (this.absoluteMaxValue - this.absoluteMinValue)) * 100.0f;
            addFixGap(true);
        }
        this.thumbWidth = this.leftThumb != null ? (float) this.leftThumb.getWidth() : getResources().getDimension(C0267R.dimen.thumb_width);
        if (this.rightThumb != null) {
            height = (float) this.rightThumb.getHeight();
        } else {
            height = getResources().getDimension(C0267R.dimen.thumb_height);
        }
        this.thumbHeight = height;
        this.barHeight = (this.thumbHeight * 0.5f) * 0.3f;
        this.barPadding = this.thumbWidth * 0.5f;
        if (this.minStartValue <= this.minValue) {
            this.minStartValue = 0.0f;
            setNormalizedMinValue((double) this.minStartValue);
        } else if (this.minStartValue >= this.maxValue) {
            this.minStartValue = this.maxValue;
            setMinStartValue();
        } else {
            setMinStartValue();
        }
        if (this.maxStartValue <= this.minStartValue || this.maxStartValue <= this.minValue) {
            this.maxStartValue = 0.0f;
            setNormalizedMaxValue((double) this.maxStartValue);
        } else if (this.maxStartValue >= this.maxValue) {
            this.maxStartValue = this.maxValue;
            setMaxStartValue();
        } else {
            setMaxStartValue();
        }
        invalidate();
        if (this.onRangeSeekbarChangeListener != null) {
            this.onRangeSeekbarChangeListener.valueChanged(getSelectedMinValue(), getSelectedMaxValue());
        }
    }

    protected Thumb getPressedThumb() {
        return this.pressedThumb;
    }

    protected float getThumbWidth() {
        return this.leftThumb != null ? (float) this.leftThumb.getWidth() : getResources().getDimension(C0267R.dimen.thumb_width);
    }

    protected float getThumbHeight() {
        return this.leftThumb != null ? (float) this.leftThumb.getHeight() : getResources().getDimension(C0267R.dimen.thumb_height);
    }

    protected float getBarHeight() {
        return (this.thumbHeight * 0.5f) * 0.3f;
    }

    protected float getBarPadding() {
        return this.thumbWidth * 0.5f;
    }

    protected Bitmap getBitmap(Drawable drawable) {
        return drawable != null ? ((BitmapDrawable) drawable).getBitmap() : null;
    }

    protected float getCornerRadius(TypedArray typedArray) {
        return typedArray.getFloat(C0267R.styleable.CrystalRangeSeekbar_corner_radius, 0.0f);
    }

    protected float getMinValue(TypedArray typedArray) {
        return typedArray.getFloat(C0267R.styleable.CrystalRangeSeekbar_min_value, 0.0f);
    }

    protected float getMaxValue(TypedArray typedArray) {
        return typedArray.getFloat(C0267R.styleable.CrystalRangeSeekbar_max_value, 100.0f);
    }

    protected float getMinStartValue(TypedArray typedArray) {
        return typedArray.getFloat(C0267R.styleable.CrystalRangeSeekbar_min_start_value, this.minValue);
    }

    protected float getMaxStartValue(TypedArray typedArray) {
        return typedArray.getFloat(C0267R.styleable.CrystalRangeSeekbar_max_start_value, this.maxValue);
    }

    protected float getSteps(TypedArray typedArray) {
        return typedArray.getFloat(C0267R.styleable.CrystalRangeSeekbar_steps, -1.0f);
    }

    protected float getGap(TypedArray typedArray) {
        return typedArray.getFloat(C0267R.styleable.CrystalRangeSeekbar_gap, 0.0f);
    }

    protected float getFixedGap(TypedArray typedArray) {
        return typedArray.getFloat(C0267R.styleable.CrystalRangeSeekbar_fix_gap, -1.0f);
    }

    protected int getBarColor(TypedArray typedArray) {
        return typedArray.getColor(C0267R.styleable.CrystalRangeSeekbar_bar_color, -7829368);
    }

    protected int getBarHighlightColor(TypedArray typedArray) {
        return typedArray.getColor(C0267R.styleable.CrystalRangeSeekbar_bar_highlight_color, ViewCompat.MEASURED_STATE_MASK);
    }

    protected int getLeftThumbColor(TypedArray typedArray) {
        return typedArray.getColor(C0267R.styleable.CrystalRangeSeekbar_left_thumb_color, ViewCompat.MEASURED_STATE_MASK);
    }

    protected int getRightThumbColor(TypedArray typedArray) {
        return typedArray.getColor(C0267R.styleable.CrystalRangeSeekbar_right_thumb_color, ViewCompat.MEASURED_STATE_MASK);
    }

    protected int getLeftThumbColorPressed(TypedArray typedArray) {
        return typedArray.getColor(C0267R.styleable.CrystalRangeSeekbar_left_thumb_color_pressed, -12303292);
    }

    protected int getRightThumbColorPressed(TypedArray typedArray) {
        return typedArray.getColor(C0267R.styleable.CrystalRangeSeekbar_right_thumb_color_pressed, -12303292);
    }

    protected Drawable getLeftDrawable(TypedArray typedArray) {
        return typedArray.getDrawable(C0267R.styleable.CrystalRangeSeekbar_left_thumb_image);
    }

    protected Drawable getRightDrawable(TypedArray typedArray) {
        return typedArray.getDrawable(C0267R.styleable.CrystalRangeSeekbar_right_thumb_image);
    }

    protected Drawable getLeftDrawablePressed(TypedArray typedArray) {
        return typedArray.getDrawable(C0267R.styleable.CrystalRangeSeekbar_left_thumb_image_pressed);
    }

    protected Drawable getRightDrawablePressed(TypedArray typedArray) {
        return typedArray.getDrawable(C0267R.styleable.CrystalRangeSeekbar_right_thumb_image_pressed);
    }

    protected int getDataType(TypedArray typedArray) {
        return typedArray.getInt(C0267R.styleable.CrystalRangeSeekbar_data_type, 2);
    }

    protected RectF getLeftThumbRect() {
        return this.rectLeftThumb;
    }

    protected RectF getRightThumbRect() {
        return this.rectRightThumb;
    }

    protected void setupBar(Canvas canvas, Paint paint, RectF rect) {
        rect.left = this.barPadding;
        rect.top = (((float) getHeight()) - this.barHeight) * 0.5f;
        rect.right = ((float) getWidth()) - this.barPadding;
        rect.bottom = (((float) getHeight()) + this.barHeight) * 0.5f;
        paint.setStyle(Style.FILL);
        paint.setColor(this.barColor);
        paint.setAntiAlias(true);
        drawBar(canvas, paint, rect);
    }

    protected void drawBar(Canvas canvas, Paint paint, RectF rect) {
        canvas.drawRoundRect(rect, this.cornerRadius, this.cornerRadius, paint);
    }

    protected void setupHighlightBar(Canvas canvas, Paint paint, RectF rect) {
        rect.left = normalizedToScreen(this.normalizedMinValue) + (getThumbWidth() / ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT);
        rect.right = normalizedToScreen(this.normalizedMaxValue) + (getThumbWidth() / ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT);
        paint.setColor(this.barHighlightColor);
        drawHighlightBar(canvas, paint, rect);
    }

    protected void drawHighlightBar(Canvas canvas, Paint paint, RectF rect) {
        canvas.drawRoundRect(rect, this.cornerRadius, this.cornerRadius, paint);
    }

    protected void setupLeftThumb(Canvas canvas, Paint paint, RectF rect) {
        this.leftThumbColor = Thumb.MIN.equals(this.pressedThumb) ? this.leftThumbColorPressed : this.leftThumbColorNormal;
        paint.setColor(this.leftThumbColor);
        this.rectLeftThumb.left = normalizedToScreen(this.normalizedMinValue);
        this.rectLeftThumb.right = Math.min((this.rectLeftThumb.left + (getThumbWidth() / ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT)) + this.barPadding, (float) getWidth());
        this.rectLeftThumb.top = 0.0f;
        this.rectLeftThumb.bottom = this.thumbHeight;
        if (this.leftThumb != null) {
            drawLeftThumbWithImage(canvas, paint, this.rectLeftThumb, Thumb.MIN.equals(this.pressedThumb) ? this.leftThumbPressed : this.leftThumb);
        } else {
            drawLeftThumbWithColor(canvas, paint, this.rectLeftThumb);
        }
    }

    protected void drawLeftThumbWithColor(Canvas canvas, Paint paint, RectF rect) {
        canvas.drawOval(rect, paint);
    }

    protected void drawLeftThumbWithImage(Canvas canvas, Paint paint, RectF rect, Bitmap image) {
        canvas.drawBitmap(image, rect.left, rect.top, paint);
    }

    protected void setupRightThumb(Canvas canvas, Paint paint, RectF rect) {
        this.rightThumbColor = Thumb.MAX.equals(this.pressedThumb) ? this.rightThumbColorPressed : this.rightThumbColorNormal;
        paint.setColor(this.rightThumbColor);
        this.rectRightThumb.left = normalizedToScreen(this.normalizedMaxValue);
        this.rectRightThumb.right = Math.min((this.rectRightThumb.left + (getThumbWidth() / ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT)) + this.barPadding, (float) getWidth());
        this.rectRightThumb.top = 0.0f;
        this.rectRightThumb.bottom = this.thumbHeight;
        if (this.rightThumb != null) {
            drawRightThumbWithImage(canvas, paint, this.rectRightThumb, Thumb.MAX.equals(this.pressedThumb) ? this.rightThumbPressed : this.rightThumb);
        } else {
            drawRightThumbWithColor(canvas, paint, this.rectRightThumb);
        }
    }

    protected void drawRightThumbWithColor(Canvas canvas, Paint paint, RectF rect) {
        canvas.drawOval(rect, paint);
    }

    protected void drawRightThumbWithImage(Canvas canvas, Paint paint, RectF rect, Bitmap image) {
        canvas.drawBitmap(image, rect.left, rect.top, paint);
    }

    protected void trackTouchEvent(MotionEvent event) {
        try {
            float x = event.getX(event.findPointerIndex(this.mActivePointerId));
            if (Thumb.MIN.equals(this.pressedThumb)) {
                setNormalizedMinValue(screenToNormalized(x));
            } else if (Thumb.MAX.equals(this.pressedThumb)) {
                setNormalizedMaxValue(screenToNormalized(x));
            }
        } catch (Exception e) {
        }
    }

    protected void touchDown(float x, float y) {
    }

    protected void touchMove(float x, float y) {
    }

    protected void touchUp(float x, float y) {
    }

    protected int getMeasureSpecWith(int widthMeasureSpec) {
        if (MeasureSpec.getMode(widthMeasureSpec) != 0) {
            return MeasureSpec.getSize(widthMeasureSpec);
        }
        return Callback.DEFAULT_DRAG_ANIMATION_DURATION;
    }

    protected int getMeasureSpecHeight(int heightMeasureSpec) {
        int height = Math.round(this.thumbHeight);
        if (MeasureSpec.getMode(heightMeasureSpec) != 0) {
            return Math.min(height, MeasureSpec.getSize(heightMeasureSpec));
        }
        return height;
    }

    protected final void log(Object object) {
        Log.d("CRS=>", String.valueOf(object));
    }

    private void setMinStartValue() {
        if (this.minStartValue > this.minValue && this.minStartValue < this.maxValue) {
            this.minStartValue = Math.min(this.minStartValue, this.absoluteMaxValue);
            this.minStartValue -= this.absoluteMinValue;
            this.minStartValue = (this.minStartValue / (this.absoluteMaxValue - this.absoluteMinValue)) * 100.0f;
            setNormalizedMinValue((double) this.minStartValue);
        }
    }

    private void setMaxStartValue() {
        if (this.maxStartValue < this.maxValue && this.maxStartValue > this.minValue && this.maxStartValue > this.minStartValue) {
            this.maxStartValue = Math.max(this.maxStartValue, this.absoluteMinValue);
            this.maxStartValue -= this.absoluteMinValue;
            this.maxStartValue = (this.maxStartValue / (this.absoluteMaxValue - this.absoluteMinValue)) * 100.0f;
            setNormalizedMaxValue((double) this.maxStartValue);
        }
    }

    private Thumb evalPressedThumb(float touchX) {
        boolean minThumbPressed = isInThumbRange(touchX, this.normalizedMinValue);
        boolean maxThumbPressed = isInThumbRange(touchX, this.normalizedMaxValue);
        if (minThumbPressed && maxThumbPressed) {
            return touchX / ((float) getWidth()) > 0.5f ? Thumb.MIN : Thumb.MAX;
        } else {
            if (minThumbPressed) {
                return Thumb.MIN;
            }
            if (maxThumbPressed) {
                return Thumb.MAX;
            }
            return null;
        }
    }

    private boolean isInThumbRange(float touchX, double normalizedThumbValue) {
        float thumbPos = normalizedToScreen(normalizedThumbValue);
        float left = thumbPos - (getThumbWidth() / ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT);
        float right = thumbPos + (getThumbWidth() / ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT);
        float x = touchX - (getThumbWidth() / ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT);
        if (thumbPos > ((float) getWidth()) - this.thumbWidth) {
            x = touchX;
        }
        return x >= left && x <= right;
    }

    private void onStartTrackingTouch() {
        this.mIsDragging = true;
    }

    private void onStopTrackingTouch() {
        this.mIsDragging = false;
    }

    private float normalizedToScreen(double normalizedCoord) {
        return (((float) normalizedCoord) / 100.0f) * (((float) getWidth()) - (this.barPadding * ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT));
    }

    private double screenToNormalized(float screenCoord) {
        double width = (double) getWidth();
        if (width <= ((double) (this.barPadding * ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT))) {
            return 0.0d;
        }
        width -= (double) (this.barPadding * ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT);
        return Math.min(100.0d, Math.max(0.0d, ((((double) screenCoord) / width) * 100.0d) - ((((double) this.barPadding) / width) * 100.0d)));
    }

    private void setNormalizedMinValue(double value) {
        this.normalizedMinValue = Math.max(0.0d, Math.min(100.0d, Math.min(value, this.normalizedMaxValue)));
        if (this.fixGap == -1.0f || this.fixGap <= 0.0f) {
            addMinGap();
        } else {
            addFixGap(true);
        }
        invalidate();
    }

    private void setNormalizedMaxValue(double value) {
        this.normalizedMaxValue = Math.max(0.0d, Math.min(100.0d, Math.max(value, this.normalizedMinValue)));
        if (this.fixGap == -1.0f || this.fixGap <= 0.0f) {
            addMaxGap();
        } else {
            addFixGap(false);
        }
        invalidate();
    }

    private void addFixGap(boolean leftThumb) {
        if (leftThumb) {
            this.normalizedMaxValue = this.normalizedMinValue + ((double) this.fixGap);
            if (this.normalizedMaxValue >= 100.0d) {
                this.normalizedMaxValue = 100.0d;
                this.normalizedMinValue = this.normalizedMaxValue - ((double) this.fixGap);
                return;
            }
            return;
        }
        this.normalizedMinValue = this.normalizedMaxValue - ((double) this.fixGap);
        if (this.normalizedMinValue <= 0.0d) {
            this.normalizedMinValue = 0.0d;
            this.normalizedMaxValue = this.normalizedMinValue + ((double) this.fixGap);
        }
    }

    private void addMinGap() {
        if (this.normalizedMinValue + ((double) this.gap) > this.normalizedMaxValue) {
            double g = this.normalizedMinValue + ((double) this.gap);
            this.normalizedMaxValue = g;
            this.normalizedMaxValue = Math.max(0.0d, Math.min(100.0d, Math.max(g, this.normalizedMinValue)));
            if (this.normalizedMinValue >= this.normalizedMaxValue - ((double) this.gap)) {
                this.normalizedMinValue = this.normalizedMaxValue - ((double) this.gap);
            }
        }
    }

    private void addMaxGap() {
        if (this.normalizedMaxValue - ((double) this.gap) < this.normalizedMinValue) {
            double g = this.normalizedMaxValue - ((double) this.gap);
            this.normalizedMinValue = g;
            this.normalizedMinValue = Math.max(0.0d, Math.min(100.0d, Math.min(g, this.normalizedMaxValue)));
            if (this.normalizedMaxValue <= this.normalizedMinValue + ((double) this.gap)) {
                this.normalizedMaxValue = this.normalizedMinValue + ((double) this.gap);
            }
        }
    }

    private double normalizedToValue(double normalized) {
        return ((normalized / 100.0d) * ((double) (this.maxValue - this.minValue))) + ((double) this.minValue);
    }

    private void attemptClaimDrag() {
        if (getParent() != null) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
    }

    private <T extends Number> Number formatValue(T value) throws IllegalArgumentException {
        Double v = (Double) value;
        if (this.dataType == 0) {
            return Long.valueOf(v.longValue());
        }
        if (this.dataType == 1) {
            return v;
        }
        if (this.dataType == 2) {
            return Long.valueOf(Math.round(v.doubleValue()));
        }
        if (this.dataType == 3) {
            return Float.valueOf(v.floatValue());
        }
        if (this.dataType == 4) {
            return Short.valueOf(v.shortValue());
        }
        if (this.dataType == 5) {
            return Byte.valueOf(v.byteValue());
        }
        throw new IllegalArgumentException("Number class '" + value.getClass().getName() + "' is not supported");
    }

    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isInEditMode()) {
            setupBar(canvas, this._paint, this._rect);
            setupHighlightBar(canvas, this._paint, this._rect);
            setupLeftThumb(canvas, this._paint, this._rect);
            setupRightThumb(canvas, this._paint, this._rect);
        }
    }

    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getMeasureSpecWith(widthMeasureSpec), getMeasureSpecHeight(heightMeasureSpec));
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized boolean onTouchEvent(android.view.MotionEvent r7) {
        /*
        r6 = this;
        r3 = 1;
        r2 = 0;
        monitor-enter(r6);
        r4 = r6.isEnabled();	 Catch:{ all -> 0x005f }
        if (r4 != 0) goto L_0x000b;
    L_0x0009:
        monitor-exit(r6);
        return r2;
    L_0x000b:
        r0 = r7.getAction();	 Catch:{ all -> 0x005f }
        r2 = r0 & 255;
        switch(r2) {
            case 0: goto L_0x0016;
            case 1: goto L_0x008e;
            case 2: goto L_0x0062;
            case 3: goto L_0x00e4;
            case 4: goto L_0x0014;
            case 5: goto L_0x0014;
            case 6: goto L_0x00df;
            default: goto L_0x0014;
        };	 Catch:{ all -> 0x005f }
    L_0x0014:
        r2 = r3;
        goto L_0x0009;
    L_0x0016:
        r2 = r7.getPointerCount();	 Catch:{ all -> 0x005f }
        r2 = r2 + -1;
        r2 = r7.getPointerId(r2);	 Catch:{ all -> 0x005f }
        r6.mActivePointerId = r2;	 Catch:{ all -> 0x005f }
        r2 = r6.mActivePointerId;	 Catch:{ all -> 0x005f }
        r2 = r7.findPointerIndex(r2);	 Catch:{ all -> 0x005f }
        r6.pointerIndex = r2;	 Catch:{ all -> 0x005f }
        r2 = r6.pointerIndex;	 Catch:{ all -> 0x005f }
        r1 = r7.getX(r2);	 Catch:{ all -> 0x005f }
        r2 = r6.evalPressedThumb(r1);	 Catch:{ all -> 0x005f }
        r6.pressedThumb = r2;	 Catch:{ all -> 0x005f }
        r2 = r6.pressedThumb;	 Catch:{ all -> 0x005f }
        if (r2 != 0) goto L_0x003f;
    L_0x003a:
        r2 = super.onTouchEvent(r7);	 Catch:{ all -> 0x005f }
        goto L_0x0009;
    L_0x003f:
        r2 = r6.pointerIndex;	 Catch:{ all -> 0x005f }
        r2 = r7.getX(r2);	 Catch:{ all -> 0x005f }
        r4 = r6.pointerIndex;	 Catch:{ all -> 0x005f }
        r4 = r7.getY(r4);	 Catch:{ all -> 0x005f }
        r6.touchDown(r2, r4);	 Catch:{ all -> 0x005f }
        r2 = 1;
        r6.setPressed(r2);	 Catch:{ all -> 0x005f }
        r6.invalidate();	 Catch:{ all -> 0x005f }
        r6.onStartTrackingTouch();	 Catch:{ all -> 0x005f }
        r6.trackTouchEvent(r7);	 Catch:{ all -> 0x005f }
        r6.attemptClaimDrag();	 Catch:{ all -> 0x005f }
        goto L_0x0014;
    L_0x005f:
        r2 = move-exception;
        monitor-exit(r6);
        throw r2;
    L_0x0062:
        r2 = r6.pressedThumb;	 Catch:{ all -> 0x005f }
        if (r2 == 0) goto L_0x0014;
    L_0x0066:
        r2 = r6.mIsDragging;	 Catch:{ all -> 0x005f }
        if (r2 == 0) goto L_0x007c;
    L_0x006a:
        r2 = r6.pointerIndex;	 Catch:{ all -> 0x005f }
        r2 = r7.getX(r2);	 Catch:{ all -> 0x005f }
        r4 = r6.pointerIndex;	 Catch:{ all -> 0x005f }
        r4 = r7.getY(r4);	 Catch:{ all -> 0x005f }
        r6.touchMove(r2, r4);	 Catch:{ all -> 0x005f }
        r6.trackTouchEvent(r7);	 Catch:{ all -> 0x005f }
    L_0x007c:
        r2 = r6.onRangeSeekbarChangeListener;	 Catch:{ all -> 0x005f }
        if (r2 == 0) goto L_0x0014;
    L_0x0080:
        r2 = r6.onRangeSeekbarChangeListener;	 Catch:{ all -> 0x005f }
        r4 = r6.getSelectedMinValue();	 Catch:{ all -> 0x005f }
        r5 = r6.getSelectedMaxValue();	 Catch:{ all -> 0x005f }
        r2.valueChanged(r4, r5);	 Catch:{ all -> 0x005f }
        goto L_0x0014;
    L_0x008e:
        r2 = r6.mIsDragging;	 Catch:{ all -> 0x005f }
        if (r2 == 0) goto L_0x00d5;
    L_0x0092:
        r6.trackTouchEvent(r7);	 Catch:{ all -> 0x005f }
        r6.onStopTrackingTouch();	 Catch:{ all -> 0x005f }
        r2 = 0;
        r6.setPressed(r2);	 Catch:{ all -> 0x005f }
        r2 = r6.pointerIndex;	 Catch:{ all -> 0x005f }
        r2 = r7.getX(r2);	 Catch:{ all -> 0x005f }
        r4 = r6.pointerIndex;	 Catch:{ all -> 0x005f }
        r4 = r7.getY(r4);	 Catch:{ all -> 0x005f }
        r6.touchUp(r2, r4);	 Catch:{ all -> 0x005f }
        r2 = r6.onRangeSeekbarFinalValueListener;	 Catch:{ all -> 0x005f }
        if (r2 == 0) goto L_0x00bc;
    L_0x00af:
        r2 = r6.onRangeSeekbarFinalValueListener;	 Catch:{ all -> 0x005f }
        r4 = r6.getSelectedMinValue();	 Catch:{ all -> 0x005f }
        r5 = r6.getSelectedMaxValue();	 Catch:{ all -> 0x005f }
        r2.finalValue(r4, r5);	 Catch:{ all -> 0x005f }
    L_0x00bc:
        r2 = 0;
        r6.pressedThumb = r2;	 Catch:{ all -> 0x005f }
        r6.invalidate();	 Catch:{ all -> 0x005f }
        r2 = r6.onRangeSeekbarChangeListener;	 Catch:{ all -> 0x005f }
        if (r2 == 0) goto L_0x0014;
    L_0x00c6:
        r2 = r6.onRangeSeekbarChangeListener;	 Catch:{ all -> 0x005f }
        r4 = r6.getSelectedMinValue();	 Catch:{ all -> 0x005f }
        r5 = r6.getSelectedMaxValue();	 Catch:{ all -> 0x005f }
        r2.valueChanged(r4, r5);	 Catch:{ all -> 0x005f }
        goto L_0x0014;
    L_0x00d5:
        r6.onStartTrackingTouch();	 Catch:{ all -> 0x005f }
        r6.trackTouchEvent(r7);	 Catch:{ all -> 0x005f }
        r6.onStopTrackingTouch();	 Catch:{ all -> 0x005f }
        goto L_0x00bc;
    L_0x00df:
        r6.invalidate();	 Catch:{ all -> 0x005f }
        goto L_0x0014;
    L_0x00e4:
        r2 = r6.mIsDragging;	 Catch:{ all -> 0x005f }
        if (r2 == 0) goto L_0x00fe;
    L_0x00e8:
        r6.onStopTrackingTouch();	 Catch:{ all -> 0x005f }
        r2 = 0;
        r6.setPressed(r2);	 Catch:{ all -> 0x005f }
        r2 = r6.pointerIndex;	 Catch:{ all -> 0x005f }
        r2 = r7.getX(r2);	 Catch:{ all -> 0x005f }
        r4 = r6.pointerIndex;	 Catch:{ all -> 0x005f }
        r4 = r7.getY(r4);	 Catch:{ all -> 0x005f }
        r6.touchUp(r2, r4);	 Catch:{ all -> 0x005f }
    L_0x00fe:
        r6.invalidate();	 Catch:{ all -> 0x005f }
        goto L_0x0014;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar.onTouchEvent(android.view.MotionEvent):boolean");
    }
}
