package android.support.v7.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.toolbox.ImageRequest;

@TargetApi(9)
@RequiresApi(9)
class CardViewGingerbread implements CardViewImpl {
    final RectF sCornerRect = new RectF();

    class C08231 implements RoundRectHelper {
        C08231() {
        }

        public void drawRoundRect(Canvas canvas, RectF bounds, float cornerRadius, Paint paint) {
            float twoRadius = cornerRadius * ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT;
            float innerWidth = (bounds.width() - twoRadius) - DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;
            float innerHeight = (bounds.height() - twoRadius) - DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;
            if (cornerRadius >= DefaultRetryPolicy.DEFAULT_BACKOFF_MULT) {
                float roundedCornerRadius = cornerRadius + 0.5f;
                CardViewGingerbread.this.sCornerRect.set(-roundedCornerRadius, -roundedCornerRadius, roundedCornerRadius, roundedCornerRadius);
                int saved = canvas.save();
                canvas.translate(bounds.left + roundedCornerRadius, bounds.top + roundedCornerRadius);
                canvas.drawArc(CardViewGingerbread.this.sCornerRect, 180.0f, 90.0f, true, paint);
                canvas.translate(innerWidth, 0.0f);
                canvas.rotate(90.0f);
                canvas.drawArc(CardViewGingerbread.this.sCornerRect, 180.0f, 90.0f, true, paint);
                canvas.translate(innerHeight, 0.0f);
                canvas.rotate(90.0f);
                canvas.drawArc(CardViewGingerbread.this.sCornerRect, 180.0f, 90.0f, true, paint);
                canvas.translate(innerWidth, 0.0f);
                canvas.rotate(90.0f);
                canvas.drawArc(CardViewGingerbread.this.sCornerRect, 180.0f, 90.0f, true, paint);
                canvas.restoreToCount(saved);
                canvas.drawRect((bounds.left + roundedCornerRadius) - DefaultRetryPolicy.DEFAULT_BACKOFF_MULT, bounds.top, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT + (bounds.right - roundedCornerRadius), bounds.top + roundedCornerRadius, paint);
                canvas.drawRect((bounds.left + roundedCornerRadius) - DefaultRetryPolicy.DEFAULT_BACKOFF_MULT, bounds.bottom - roundedCornerRadius, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT + (bounds.right - roundedCornerRadius), bounds.bottom, paint);
            }
            canvas.drawRect(bounds.left, bounds.top + cornerRadius, bounds.right, bounds.bottom - cornerRadius, paint);
        }
    }

    CardViewGingerbread() {
    }

    public void initStatic() {
        RoundRectDrawableWithShadow.sRoundRectHelper = new C08231();
    }

    public void initialize(CardViewDelegate cardView, Context context, ColorStateList backgroundColor, float radius, float elevation, float maxElevation) {
        RoundRectDrawableWithShadow background = createBackground(context, backgroundColor, radius, elevation, maxElevation);
        background.setAddPaddingForCorners(cardView.getPreventCornerOverlap());
        cardView.setCardBackground(background);
        updatePadding(cardView);
    }

    private RoundRectDrawableWithShadow createBackground(Context context, ColorStateList backgroundColor, float radius, float elevation, float maxElevation) {
        return new RoundRectDrawableWithShadow(context.getResources(), backgroundColor, radius, elevation, maxElevation);
    }

    public void updatePadding(CardViewDelegate cardView) {
        Rect shadowPadding = new Rect();
        getShadowBackground(cardView).getMaxShadowAndCornerPadding(shadowPadding);
        cardView.setMinWidthHeightInternal((int) Math.ceil((double) getMinWidth(cardView)), (int) Math.ceil((double) getMinHeight(cardView)));
        cardView.setShadowPadding(shadowPadding.left, shadowPadding.top, shadowPadding.right, shadowPadding.bottom);
    }

    public void onCompatPaddingChanged(CardViewDelegate cardView) {
    }

    public void onPreventCornerOverlapChanged(CardViewDelegate cardView) {
        getShadowBackground(cardView).setAddPaddingForCorners(cardView.getPreventCornerOverlap());
        updatePadding(cardView);
    }

    public void setBackgroundColor(CardViewDelegate cardView, @Nullable ColorStateList color) {
        getShadowBackground(cardView).setColor(color);
    }

    public ColorStateList getBackgroundColor(CardViewDelegate cardView) {
        return getShadowBackground(cardView).getColor();
    }

    public void setRadius(CardViewDelegate cardView, float radius) {
        getShadowBackground(cardView).setCornerRadius(radius);
        updatePadding(cardView);
    }

    public float getRadius(CardViewDelegate cardView) {
        return getShadowBackground(cardView).getCornerRadius();
    }

    public void setElevation(CardViewDelegate cardView, float elevation) {
        getShadowBackground(cardView).setShadowSize(elevation);
    }

    public float getElevation(CardViewDelegate cardView) {
        return getShadowBackground(cardView).getShadowSize();
    }

    public void setMaxElevation(CardViewDelegate cardView, float maxElevation) {
        getShadowBackground(cardView).setMaxShadowSize(maxElevation);
        updatePadding(cardView);
    }

    public float getMaxElevation(CardViewDelegate cardView) {
        return getShadowBackground(cardView).getMaxShadowSize();
    }

    public float getMinWidth(CardViewDelegate cardView) {
        return getShadowBackground(cardView).getMinWidth();
    }

    public float getMinHeight(CardViewDelegate cardView) {
        return getShadowBackground(cardView).getMinHeight();
    }

    private RoundRectDrawableWithShadow getShadowBackground(CardViewDelegate cardView) {
        return (RoundRectDrawableWithShadow) cardView.getCardBackground();
    }
}
