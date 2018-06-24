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
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarFinalValueListener;
import com.example.crystalrangeseekbar.C0267R;

public class CrystalSeekbar extends View {
    private static final int INVALID_POINTER_ID = 255;
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
    private Drawable leftDrawable;
    private Drawable leftDrawablePressed;
    private Bitmap leftThumb;
    private int leftThumbColor;
    private int leftThumbColorNormal;
    private int leftThumbColorPressed;
    private Bitmap leftThumbPressed;
    private int mActivePointerId;
    private boolean mIsDragging;
    private float maxValue;
    private float minStartValue;
    private float minValue;
    private int nextPosition;
    private double normalizedMaxValue;
    private double normalizedMinValue;
    private OnSeekbarChangeListener onSeekbarChangeListener;
    private OnSeekbarFinalValueListener onSeekbarFinalValueListener;
    private int pointerIndex;
    private int position;
    private Thumb pressedThumb;
    private RectF rectLeftThumb;
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

    public static final class Position {
        public static final int LEFT = 0;
        public static final int RIGHT = 1;
    }

    protected enum Thumb {
        MIN
    }

    public CrystalSeekbar(Context context) {
        this(context, null);
    }

    public CrystalSeekbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CrystalSeekbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.NO_STEP = -1.0f;
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
                this.steps = getSteps(array);
                this.barColor = getBarColor(array);
                this.barHighlightColor = getBarHighlightColor(array);
                this.leftThumbColorNormal = getLeftThumbColor(array);
                this.leftThumbColorPressed = getLeftThumbColorPressed(array);
                this.leftDrawable = getLeftDrawable(array);
                this.leftDrawablePressed = getLeftDrawablePressed(array);
                this.dataType = getDataType(array);
                this.position = getPosition(array);
                this.nextPosition = this.position;
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
        this.leftThumb = getBitmap(this.leftDrawable);
        this.leftThumbPressed = getBitmap(this.leftDrawablePressed);
        this.leftThumbPressed = this.leftThumbPressed == null ? this.leftThumb : this.leftThumbPressed;
        this.thumbWidth = getThumbWidth();
        this.thumbHeight = getThumbHeight();
        this.barHeight = getBarHeight();
        this.barPadding = getBarPadding();
        this._paint = new Paint(1);
        this._rect = new RectF();
        this.rectLeftThumb = new RectF();
        this.pressedThumb = null;
        setMinStartValue();
    }

    public CrystalSeekbar setCornerRadius(float cornerRadius) {
        this.cornerRadius = cornerRadius;
        return this;
    }

    public CrystalSeekbar setMinValue(float minValue) {
        this.minValue = minValue;
        this.absoluteMinValue = minValue;
        return this;
    }

    public CrystalSeekbar setMaxValue(float maxValue) {
        this.maxValue = maxValue;
        this.absoluteMaxValue = maxValue;
        return this;
    }

    public CrystalSeekbar setMinStartValue(float minStartValue) {
        this.minStartValue = minStartValue;
        return this;
    }

    public CrystalSeekbar setSteps(float steps) {
        this.steps = steps;
        return this;
    }

    public CrystalSeekbar setBarColor(int barColor) {
        this.barColor = barColor;
        return this;
    }

    public CrystalSeekbar setBarHighlightColor(int barHighlightColor) {
        this.barHighlightColor = barHighlightColor;
        return this;
    }

    public CrystalSeekbar setLeftThumbColor(int leftThumbColorNormal) {
        this.leftThumbColorNormal = leftThumbColorNormal;
        return this;
    }

    public CrystalSeekbar setLeftThumbHighlightColor(int leftThumbColorPressed) {
        this.leftThumbColorPressed = leftThumbColorPressed;
        return this;
    }

    public CrystalSeekbar setLeftThumbDrawable(int resId) {
        setLeftThumbDrawable(ContextCompat.getDrawable(getContext(), resId));
        return this;
    }

    public CrystalSeekbar setLeftThumbDrawable(Drawable drawable) {
        setLeftThumbBitmap(getBitmap(drawable));
        return this;
    }

    public CrystalSeekbar setLeftThumbBitmap(Bitmap bitmap) {
        this.leftThumb = bitmap;
        return this;
    }

    public CrystalSeekbar setLeftThumbHighlightDrawable(int resId) {
        setLeftThumbHighlightDrawable(ContextCompat.getDrawable(getContext(), resId));
        return this;
    }

    public CrystalSeekbar setLeftThumbHighlightDrawable(Drawable drawable) {
        setLeftThumbHighlightBitmap(getBitmap(drawable));
        return this;
    }

    public CrystalSeekbar setLeftThumbHighlightBitmap(Bitmap bitmap) {
        this.leftThumbPressed = bitmap;
        return this;
    }

    public CrystalSeekbar setDataType(int dataType) {
        this.dataType = dataType;
        return this;
    }

    public CrystalSeekbar setPosition(int pos) {
        this.nextPosition = pos;
        return this;
    }

    public void setOnSeekbarChangeListener(OnSeekbarChangeListener onSeekbarChangeListener) {
        this.onSeekbarChangeListener = onSeekbarChangeListener;
        if (this.onSeekbarChangeListener != null) {
            this.onSeekbarChangeListener.valueChanged(getSelectedMinValue());
        }
    }

    public void setOnSeekbarFinalValueListener(OnSeekbarFinalValueListener onSeekbarFinalValueListener) {
        this.onSeekbarFinalValueListener = onSeekbarFinalValueListener;
    }

    public Thumb getPressedThumb() {
        return this.pressedThumb;
    }

    public RectF getLeftThumbRect() {
        return this.rectLeftThumb;
    }

    public float getCornerRadius() {
        return this.cornerRadius;
    }

    public float getMinValue() {
        return this.minValue;
    }

    public float getMaxValue() {
        return this.maxValue;
    }

    public float getMinStartValue() {
        return this.minStartValue;
    }

    public float getSteps() {
        return this.steps;
    }

    public int getBarColor() {
        return this.barColor;
    }

    public int getBarHighlightColor() {
        return this.barHighlightColor;
    }

    public int getLeftThumbColor() {
        return this.leftThumbColor;
    }

    public int getLeftThumbColorPressed() {
        return this.leftThumbColorPressed;
    }

    public Drawable getLeftDrawable() {
        return this.leftDrawable;
    }

    public Drawable getLeftDrawablePressed() {
        return this.leftDrawablePressed;
    }

    public int getDataType() {
        return this.dataType;
    }

    public int getPosition() {
        return this.position;
    }

    public float getThumbWidth() {
        return this.leftThumb != null ? (float) this.leftThumb.getWidth() : getResources().getDimension(C0267R.dimen.thumb_width);
    }

    public float getThumbHeight() {
        return this.leftThumb != null ? (float) this.leftThumb.getHeight() : getResources().getDimension(C0267R.dimen.thumb_height);
    }

    public float getBarHeight() {
        return (this.thumbHeight * 0.5f) * 0.3f;
    }

    public float getBarPadding() {
        return this.thumbWidth * 0.5f;
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
        if (this.position != 0) {
            nv = Math.abs(nv - ((double) this.maxValue));
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
        this.thumbWidth = this.leftThumb != null ? (float) this.leftThumb.getWidth() : getResources().getDimension(C0267R.dimen.thumb_width);
        this.thumbHeight = this.leftThumb != null ? (float) this.leftThumb.getHeight() : getResources().getDimension(C0267R.dimen.thumb_height);
        this.barHeight = (this.thumbHeight * 0.5f) * 0.3f;
        this.barPadding = this.thumbWidth * 0.5f;
        if (this.minStartValue < this.minValue) {
            this.minStartValue = 0.0f;
            setNormalizedMinValue((double) this.minStartValue);
        } else if (this.minStartValue > this.maxValue) {
            this.minStartValue = this.maxValue;
            setNormalizedMinValue((double) this.minStartValue);
        } else {
            if (this.nextPosition != this.position) {
                this.minStartValue = (float) Math.abs(this.normalizedMaxValue - this.normalizedMinValue);
            }
            if (this.minStartValue > this.minValue) {
                this.minStartValue = Math.min(this.minStartValue, this.absoluteMaxValue);
                this.minStartValue -= this.absoluteMinValue;
                this.minStartValue = (this.minStartValue / (this.absoluteMaxValue - this.absoluteMinValue)) * 100.0f;
            }
            setNormalizedMinValue((double) this.minStartValue);
            this.position = this.nextPosition;
        }
        invalidate();
        if (this.onSeekbarChangeListener != null) {
            this.onSeekbarChangeListener.valueChanged(getSelectedMinValue());
        }
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

    protected float getSteps(TypedArray typedArray) {
        return typedArray.getFloat(C0267R.styleable.CrystalRangeSeekbar_steps, -1.0f);
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

    protected int getLeftThumbColorPressed(TypedArray typedArray) {
        return typedArray.getColor(C0267R.styleable.CrystalRangeSeekbar_left_thumb_color_pressed, -12303292);
    }

    protected Drawable getLeftDrawable(TypedArray typedArray) {
        return typedArray.getDrawable(C0267R.styleable.CrystalRangeSeekbar_left_thumb_image);
    }

    protected Drawable getLeftDrawablePressed(TypedArray typedArray) {
        return typedArray.getDrawable(C0267R.styleable.CrystalRangeSeekbar_left_thumb_image_pressed);
    }

    protected int getDataType(TypedArray typedArray) {
        return typedArray.getInt(C0267R.styleable.CrystalRangeSeekbar_data_type, 2);
    }

    protected final int getPosition(TypedArray typedArray) {
        int pos = typedArray.getInt(C0267R.styleable.CrystalRangeSeekbar_position, 0);
        this.normalizedMinValue = pos == 0 ? this.normalizedMinValue : this.normalizedMaxValue;
        return pos;
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
        if (this.position == 1) {
            rect.left = normalizedToScreen(this.normalizedMinValue) + (getThumbWidth() / ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT);
            rect.right = ((float) getWidth()) - (getThumbWidth() / ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT);
        } else {
            rect.left = getThumbWidth() / ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT;
            rect.right = normalizedToScreen(this.normalizedMinValue) + (getThumbWidth() / ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT);
        }
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

    protected void trackTouchEvent(MotionEvent event) {
        try {
            float x = event.getX(event.findPointerIndex(this.mActivePointerId));
            if (Thumb.MIN.equals(this.pressedThumb)) {
                setNormalizedMinValue(screenToNormalized(x));
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

    private Thumb evalPressedThumb(float touchX) {
        if (isInThumbRange(touchX, this.normalizedMinValue)) {
            return Thumb.MIN;
        }
        return null;
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
        invalidate();
    }

    private void setNormalizedMaxValue(double value) {
        this.normalizedMaxValue = Math.max(0.0d, Math.min(100.0d, Math.max(value, this.normalizedMinValue)));
        invalidate();
    }

    private double normalizedToValue(double normalized) {
        double val = (normalized / 100.0d) * ((double) (this.maxValue - this.minValue));
        if (this.position == 0) {
            return val + ((double) this.minValue);
        }
        return val;
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
        }
    }

    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getMeasureSpecWith(widthMeasureSpec), getMeasureSpecHeight(heightMeasureSpec));
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized boolean onTouchEvent(android.view.MotionEvent r6) {
        /*
        r5 = this;
        r3 = 1;
        r2 = 0;
        monitor-enter(r5);
        r4 = r5.isEnabled();	 Catch:{ all -> 0x005f }
        if (r4 != 0) goto L_0x000b;
    L_0x0009:
        monitor-exit(r5);
        return r2;
    L_0x000b:
        r0 = r6.getAction();	 Catch:{ all -> 0x005f }
        r2 = r0 & 255;
        switch(r2) {
            case 0: goto L_0x0016;
            case 1: goto L_0x008a;
            case 2: goto L_0x0062;
            case 3: goto L_0x00d8;
            case 4: goto L_0x0014;
            case 5: goto L_0x0014;
            case 6: goto L_0x00d3;
            default: goto L_0x0014;
        };	 Catch:{ all -> 0x005f }
    L_0x0014:
        r2 = r3;
        goto L_0x0009;
    L_0x0016:
        r2 = r6.getPointerCount();	 Catch:{ all -> 0x005f }
        r2 = r2 + -1;
        r2 = r6.getPointerId(r2);	 Catch:{ all -> 0x005f }
        r5.mActivePointerId = r2;	 Catch:{ all -> 0x005f }
        r2 = r5.mActivePointerId;	 Catch:{ all -> 0x005f }
        r2 = r6.findPointerIndex(r2);	 Catch:{ all -> 0x005f }
        r5.pointerIndex = r2;	 Catch:{ all -> 0x005f }
        r2 = r5.pointerIndex;	 Catch:{ all -> 0x005f }
        r1 = r6.getX(r2);	 Catch:{ all -> 0x005f }
        r2 = r5.evalPressedThumb(r1);	 Catch:{ all -> 0x005f }
        r5.pressedThumb = r2;	 Catch:{ all -> 0x005f }
        r2 = r5.pressedThumb;	 Catch:{ all -> 0x005f }
        if (r2 != 0) goto L_0x003f;
    L_0x003a:
        r2 = super.onTouchEvent(r6);	 Catch:{ all -> 0x005f }
        goto L_0x0009;
    L_0x003f:
        r2 = r5.pointerIndex;	 Catch:{ all -> 0x005f }
        r2 = r6.getX(r2);	 Catch:{ all -> 0x005f }
        r4 = r5.pointerIndex;	 Catch:{ all -> 0x005f }
        r4 = r6.getY(r4);	 Catch:{ all -> 0x005f }
        r5.touchDown(r2, r4);	 Catch:{ all -> 0x005f }
        r2 = 1;
        r5.setPressed(r2);	 Catch:{ all -> 0x005f }
        r5.invalidate();	 Catch:{ all -> 0x005f }
        r5.onStartTrackingTouch();	 Catch:{ all -> 0x005f }
        r5.trackTouchEvent(r6);	 Catch:{ all -> 0x005f }
        r5.attemptClaimDrag();	 Catch:{ all -> 0x005f }
        goto L_0x0014;
    L_0x005f:
        r2 = move-exception;
        monitor-exit(r5);
        throw r2;
    L_0x0062:
        r2 = r5.pressedThumb;	 Catch:{ all -> 0x005f }
        if (r2 == 0) goto L_0x0014;
    L_0x0066:
        r2 = r5.mIsDragging;	 Catch:{ all -> 0x005f }
        if (r2 == 0) goto L_0x007c;
    L_0x006a:
        r2 = r5.pointerIndex;	 Catch:{ all -> 0x005f }
        r2 = r6.getX(r2);	 Catch:{ all -> 0x005f }
        r4 = r5.pointerIndex;	 Catch:{ all -> 0x005f }
        r4 = r6.getY(r4);	 Catch:{ all -> 0x005f }
        r5.touchMove(r2, r4);	 Catch:{ all -> 0x005f }
        r5.trackTouchEvent(r6);	 Catch:{ all -> 0x005f }
    L_0x007c:
        r2 = r5.onSeekbarChangeListener;	 Catch:{ all -> 0x005f }
        if (r2 == 0) goto L_0x0014;
    L_0x0080:
        r2 = r5.onSeekbarChangeListener;	 Catch:{ all -> 0x005f }
        r4 = r5.getSelectedMinValue();	 Catch:{ all -> 0x005f }
        r2.valueChanged(r4);	 Catch:{ all -> 0x005f }
        goto L_0x0014;
    L_0x008a:
        r2 = r5.mIsDragging;	 Catch:{ all -> 0x005f }
        if (r2 == 0) goto L_0x00c9;
    L_0x008e:
        r5.trackTouchEvent(r6);	 Catch:{ all -> 0x005f }
        r5.onStopTrackingTouch();	 Catch:{ all -> 0x005f }
        r2 = 0;
        r5.setPressed(r2);	 Catch:{ all -> 0x005f }
        r2 = r5.pointerIndex;	 Catch:{ all -> 0x005f }
        r2 = r6.getX(r2);	 Catch:{ all -> 0x005f }
        r4 = r5.pointerIndex;	 Catch:{ all -> 0x005f }
        r4 = r6.getY(r4);	 Catch:{ all -> 0x005f }
        r5.touchUp(r2, r4);	 Catch:{ all -> 0x005f }
        r2 = r5.onSeekbarFinalValueListener;	 Catch:{ all -> 0x005f }
        if (r2 == 0) goto L_0x00b4;
    L_0x00ab:
        r2 = r5.onSeekbarFinalValueListener;	 Catch:{ all -> 0x005f }
        r4 = r5.getSelectedMinValue();	 Catch:{ all -> 0x005f }
        r2.finalValue(r4);	 Catch:{ all -> 0x005f }
    L_0x00b4:
        r2 = 0;
        r5.pressedThumb = r2;	 Catch:{ all -> 0x005f }
        r5.invalidate();	 Catch:{ all -> 0x005f }
        r2 = r5.onSeekbarChangeListener;	 Catch:{ all -> 0x005f }
        if (r2 == 0) goto L_0x0014;
    L_0x00be:
        r2 = r5.onSeekbarChangeListener;	 Catch:{ all -> 0x005f }
        r4 = r5.getSelectedMinValue();	 Catch:{ all -> 0x005f }
        r2.valueChanged(r4);	 Catch:{ all -> 0x005f }
        goto L_0x0014;
    L_0x00c9:
        r5.onStartTrackingTouch();	 Catch:{ all -> 0x005f }
        r5.trackTouchEvent(r6);	 Catch:{ all -> 0x005f }
        r5.onStopTrackingTouch();	 Catch:{ all -> 0x005f }
        goto L_0x00b4;
    L_0x00d3:
        r5.invalidate();	 Catch:{ all -> 0x005f }
        goto L_0x0014;
    L_0x00d8:
        r2 = r5.mIsDragging;	 Catch:{ all -> 0x005f }
        if (r2 == 0) goto L_0x00f2;
    L_0x00dc:
        r5.onStopTrackingTouch();	 Catch:{ all -> 0x005f }
        r2 = 0;
        r5.setPressed(r2);	 Catch:{ all -> 0x005f }
        r2 = r5.pointerIndex;	 Catch:{ all -> 0x005f }
        r2 = r6.getX(r2);	 Catch:{ all -> 0x005f }
        r4 = r5.pointerIndex;	 Catch:{ all -> 0x005f }
        r4 = r6.getY(r4);	 Catch:{ all -> 0x005f }
        r5.touchUp(r2, r4);	 Catch:{ all -> 0x005f }
    L_0x00f2:
        r5.invalidate();	 Catch:{ all -> 0x005f }
        goto L_0x0014;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar.onTouchEvent(android.view.MotionEvent):boolean");
    }
}
