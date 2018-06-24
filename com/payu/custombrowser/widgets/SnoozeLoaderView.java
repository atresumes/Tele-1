package com.payu.custombrowser.widgets;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.util.AttributeSet;
import android.view.View;
import com.payu.custombrowser.C0517R;
import java.util.Timer;
import java.util.TimerTask;

public class SnoozeLoaderView extends View {
    private Paint activeBarPaint;
    private int blinkPosition = 0;
    private Rect firstBar;
    private Paint firstBarPaint;
    private Paint inActiveBarPaint;
    private int mActiveColor = Color.parseColor("#00adf2");
    Activity mActivity;
    private int mAnimationSpeed = Callback.DEFAULT_DRAG_ANIMATION_DURATION;
    private int mBarHeight = 120;
    private int mBarHeightRatio = (this.mBarHeight / 3);
    private int mBarSpace = 70;
    private int mBarWidth = 40;
    private int mInActiveColor = Color.parseColor("#b0eafc");
    private Rect secondBar;
    private Paint secondBarPaint;
    private boolean shouldStartAnimation = false;
    private Rect thirdBar;
    private Paint thirdBarPaint;
    private Timer timer;

    class C05341 implements Runnable {
        C05341() {
        }

        public void run() {
            SnoozeLoaderView.this.invalidate();
        }
    }

    class C05352 extends TimerTask {
        C05352() {
        }

        public void run() {
            if (SnoozeLoaderView.this.blinkPosition == 4) {
                SnoozeLoaderView.this.blinkPosition = 0;
            } else {
                SnoozeLoaderView.this.blinkPosition = SnoozeLoaderView.this.blinkPosition + 1;
            }
            if (SnoozeLoaderView.this.shouldStartAnimation) {
                SnoozeLoaderView.this.updateBar(SnoozeLoaderView.this.blinkPosition);
            } else {
                cancel();
            }
        }
    }

    public SnoozeLoaderView(Context context) {
        super(context);
        this.mActivity = (Activity) context;
        init();
    }

    public SnoozeLoaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mActivity = (Activity) context;
        TypedArray snoozeLoaderArray = context.getTheme().obtainStyledAttributes(attrs, C0517R.styleable.SnoozeLoaderView, 0, 0);
        try {
            this.shouldStartAnimation = snoozeLoaderArray.getBoolean(C0517R.styleable.SnoozeLoaderView_startAnimate, this.shouldStartAnimation);
            this.mActiveColor = snoozeLoaderArray.getColor(C0517R.styleable.SnoozeLoaderView_activeBarColor, this.mActiveColor);
            this.mInActiveColor = snoozeLoaderArray.getColor(C0517R.styleable.SnoozeLoaderView_inActiveBarColor, this.mInActiveColor);
            this.mBarWidth = snoozeLoaderArray.getDimensionPixelSize(C0517R.styleable.SnoozeLoaderView_barWidth, this.mBarWidth);
            this.mBarHeight = snoozeLoaderArray.getDimensionPixelSize(C0517R.styleable.SnoozeLoaderView_barHeight, this.mBarHeight);
            this.mBarHeightRatio = this.mBarHeight / 3;
            this.mBarSpace = snoozeLoaderArray.getDimensionPixelSize(C0517R.styleable.SnoozeLoaderView_barSpace, this.mBarSpace);
            this.mAnimationSpeed = snoozeLoaderArray.getInt(C0517R.styleable.SnoozeLoaderView_animationSpeed, this.mAnimationSpeed);
            init();
        } finally {
            snoozeLoaderArray.recycle();
        }
    }

    public SnoozeLoaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mActivity = (Activity) context;
        TypedArray snoozeLoaderArray = context.getTheme().obtainStyledAttributes(attrs, C0517R.styleable.SnoozeLoaderView, 0, 0);
        try {
            this.shouldStartAnimation = snoozeLoaderArray.getBoolean(C0517R.styleable.SnoozeLoaderView_startAnimate, this.shouldStartAnimation);
            this.mActiveColor = snoozeLoaderArray.getColor(C0517R.styleable.SnoozeLoaderView_activeBarColor, this.mActiveColor);
            this.mInActiveColor = snoozeLoaderArray.getColor(C0517R.styleable.SnoozeLoaderView_inActiveBarColor, this.mInActiveColor);
            this.mBarWidth = snoozeLoaderArray.getDimensionPixelSize(C0517R.styleable.SnoozeLoaderView_barWidth, this.mBarWidth);
            this.mBarHeight = snoozeLoaderArray.getDimensionPixelSize(C0517R.styleable.SnoozeLoaderView_barHeight, this.mBarHeight);
            this.mBarHeightRatio = this.mBarHeight / 3;
            this.mBarSpace = snoozeLoaderArray.getDimensionPixelSize(C0517R.styleable.SnoozeLoaderView_barSpace, this.mBarSpace);
            init();
        } finally {
            snoozeLoaderArray.recycle();
        }
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(this.firstBar, this.firstBarPaint);
        canvas.drawRect(this.secondBar, this.secondBarPaint);
        canvas.drawRect(this.thirdBar, this.thirdBarPaint);
    }

    public void updateBar(int position) {
        switch (position) {
            case 0:
                this.firstBarPaint = this.inActiveBarPaint;
                this.secondBarPaint = this.inActiveBarPaint;
                this.thirdBarPaint = this.inActiveBarPaint;
                break;
            case 1:
                this.firstBarPaint = this.activeBarPaint;
                this.secondBarPaint = this.inActiveBarPaint;
                this.thirdBarPaint = this.inActiveBarPaint;
                break;
            case 2:
                this.firstBarPaint = this.activeBarPaint;
                this.secondBarPaint = this.activeBarPaint;
                this.thirdBarPaint = this.inActiveBarPaint;
                break;
            case 3:
                this.firstBarPaint = this.activeBarPaint;
                this.secondBarPaint = this.activeBarPaint;
                this.thirdBarPaint = this.activeBarPaint;
                break;
            default:
                this.firstBarPaint = this.inActiveBarPaint;
                this.secondBarPaint = this.inActiveBarPaint;
                this.thirdBarPaint = this.inActiveBarPaint;
                break;
        }
        this.mActivity.runOnUiThread(new C05341());
    }

    private void init() {
        this.activeBarPaint = new Paint();
        this.activeBarPaint.setColor(this.mActiveColor);
        this.activeBarPaint.setStyle(Style.FILL);
        this.inActiveBarPaint = new Paint();
        this.inActiveBarPaint.setColor(this.mInActiveColor);
        this.inActiveBarPaint.setStyle(Style.FILL);
        this.firstBarPaint = this.inActiveBarPaint;
        this.secondBarPaint = this.inActiveBarPaint;
        this.thirdBarPaint = this.inActiveBarPaint;
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int centerX = w / 2;
        int centerY = h / 2;
        int secondBarLeft = centerX - (this.mBarWidth / 2);
        int secondBarTop = centerY - (this.mBarHeight / 2);
        int firstBarLeft = ((centerX - this.mBarWidth) - this.mBarSpace) - (this.mBarWidth / 2);
        int firstBarTop = (centerY - (this.mBarHeight / 2)) - this.mBarHeightRatio;
        int firstBarRight = firstBarLeft + this.mBarWidth;
        int firstBarBottom = ((this.mBarHeight + firstBarTop) + this.mBarHeightRatio) + this.mBarHeightRatio;
        int thirdBarLeft = ((this.mBarWidth / 2) + centerX) + this.mBarSpace;
        int thirdBarTop = (centerY - (this.mBarHeight / 2)) + this.mBarHeightRatio;
        int thirdBarRight = thirdBarLeft + this.mBarWidth;
        int thirdBarBottom = ((this.mBarHeight + thirdBarTop) - this.mBarHeightRatio) - this.mBarHeightRatio;
        this.secondBar = new Rect(secondBarLeft, secondBarTop, secondBarLeft + this.mBarWidth, secondBarTop + this.mBarHeight);
        this.firstBar = new Rect(firstBarLeft, firstBarTop, firstBarRight, firstBarBottom);
        this.thirdBar = new Rect(thirdBarLeft, thirdBarTop, thirdBarRight, thirdBarBottom);
        if (this.shouldStartAnimation) {
            startAnimation();
        }
    }

    public void startAnimation() {
        this.shouldStartAnimation = true;
        this.timer = new Timer();
        this.timer.schedule(new C05352(), 0, (long) this.mAnimationSpeed);
    }

    public void cancelAnimation() {
        this.shouldStartAnimation = false;
        if (this.timer != null) {
            this.timer.cancel();
            this.timer.purge();
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension((((this.mBarWidth * 3) + (this.mBarSpace * 2)) + getPaddingLeft()) + getPaddingRight(), ((this.mBarHeight + (this.mBarHeightRatio * 2)) + getPaddingTop()) + getPaddingBottom());
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.shouldStartAnimation = false;
    }
}
