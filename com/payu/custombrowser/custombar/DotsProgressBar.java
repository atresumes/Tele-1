package com.payu.custombrowser.custombar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import com.android.volley.toolbox.ImageRequest;
import com.payu.custombrowser.C0517R;

public class DotsProgressBar extends View {
    private int heightSize;
    private boolean isDotProgressStopped;
    private int mDotCount = 5;
    private final Handler mHandler = new Handler();
    private int mIndex = 0;
    private float mOuterRadius;
    private final Paint mPaint = new Paint(1);
    private final Paint mPaintFill = new Paint(1);
    private float mRadius;
    private final Runnable mRunnable = new C05261();
    private final int margin = 10;
    private int step = 1;
    private int widthSize;

    class C05261 implements Runnable {
        C05261() {
        }

        public void run() {
            DotsProgressBar.this.mIndex = DotsProgressBar.this.mIndex + DotsProgressBar.this.step;
            if (DotsProgressBar.this.mIndex < 0) {
                DotsProgressBar.this.mIndex = 1;
                DotsProgressBar.this.step = 1;
            } else if (DotsProgressBar.this.mIndex > DotsProgressBar.this.mDotCount - 1) {
                DotsProgressBar.this.mIndex = 0;
                DotsProgressBar.this.step = 1;
            }
            if (!DotsProgressBar.this.isDotProgressStopped) {
                DotsProgressBar.this.invalidate();
                DotsProgressBar.this.mHandler.postDelayed(DotsProgressBar.this.mRunnable, 400);
            }
        }
    }

    public DotsProgressBar(Context context) {
        super(context);
        init(context);
    }

    public DotsProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DotsProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.mRadius = context.getResources().getDimension(C0517R.dimen.cb_circle_indicator_radius);
        this.mOuterRadius = context.getResources().getDimension(C0517R.dimen.cb_circle_indicator_outer_radius);
        this.mPaintFill.setStyle(Style.FILL);
        this.mPaintFill.setColor(context.getResources().getColor(C0517R.color.cb_payu_blue));
        this.mPaint.setStyle(Style.FILL);
        this.mPaint.setColor(855638016);
        start();
    }

    public void setDotsCount(int count) {
        this.mDotCount = count;
    }

    public void start() {
        this.mIndex = -1;
        this.isDotProgressStopped = false;
        this.mHandler.removeCallbacks(this.mRunnable);
        this.mHandler.post(this.mRunnable);
    }

    public void stop() {
        if (this.mHandler != null && this.mRunnable != null) {
            this.isDotProgressStopped = true;
            this.mHandler.removeCallbacks(this.mRunnable);
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.widthSize = (int) (((((this.mRadius * ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT) * ((float) this.mDotCount)) + ((float) (this.mDotCount * 10))) + 10.0f) + (this.mOuterRadius - this.mRadius));
        this.heightSize = ((((int) this.mRadius) * 2) + getPaddingBottom()) + getPaddingTop();
        setMeasuredDimension(this.widthSize, this.heightSize);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float dX = ((((float) this.widthSize) - ((((float) this.mDotCount) * this.mRadius) * ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT)) - ((float) ((this.mDotCount - 1) * 10))) / ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT;
        float dY = (float) (this.heightSize / 2);
        for (int i = 0; i < this.mDotCount; i++) {
            if (i == this.mIndex) {
                canvas.drawCircle(dX, dY, this.mOuterRadius, this.mPaintFill);
            } else {
                canvas.drawCircle(dX, dY, this.mRadius, this.mPaint);
            }
            dX += (this.mRadius * ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT) + 10.0f;
        }
    }
}
