package android.support.design.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build.VERSION;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewCompat;
import com.android.volley.DefaultRetryPolicy;

@TargetApi(14)
@RequiresApi(14)
class FloatingActionButtonIcs extends FloatingActionButtonGingerbread {
    private float mRotation = this.mView.getRotation();

    FloatingActionButtonIcs(VisibilityAwareImageButton view, ShadowViewDelegate shadowViewDelegate, Creator animatorCreator) {
        super(view, shadowViewDelegate, animatorCreator);
    }

    boolean requirePreDrawListener() {
        return true;
    }

    void onPreDraw() {
        float rotation = this.mView.getRotation();
        if (this.mRotation != rotation) {
            this.mRotation = rotation;
            updateFromViewRotation();
        }
    }

    void hide(@Nullable final InternalVisibilityChangedListener listener, final boolean fromUser) {
        if (!isOrWillBeHidden()) {
            this.mView.animate().cancel();
            if (shouldAnimateVisibilityChange()) {
                this.mAnimState = 1;
                this.mView.animate().scaleX(0.0f).scaleY(0.0f).alpha(0.0f).setDuration(200).setInterpolator(AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR).setListener(new AnimatorListenerAdapter() {
                    private boolean mCancelled;

                    public void onAnimationStart(Animator animation) {
                        FloatingActionButtonIcs.this.mView.internalSetVisibility(0, fromUser);
                        this.mCancelled = false;
                    }

                    public void onAnimationCancel(Animator animation) {
                        this.mCancelled = true;
                    }

                    public void onAnimationEnd(Animator animation) {
                        FloatingActionButtonIcs.this.mAnimState = 0;
                        if (!this.mCancelled) {
                            FloatingActionButtonIcs.this.mView.internalSetVisibility(fromUser ? 8 : 4, fromUser);
                            if (listener != null) {
                                listener.onHidden();
                            }
                        }
                    }
                });
                return;
            }
            this.mView.internalSetVisibility(fromUser ? 8 : 4, fromUser);
            if (listener != null) {
                listener.onHidden();
            }
        }
    }

    void show(@Nullable final InternalVisibilityChangedListener listener, final boolean fromUser) {
        if (!isOrWillBeShown()) {
            this.mView.animate().cancel();
            if (shouldAnimateVisibilityChange()) {
                this.mAnimState = 2;
                if (this.mView.getVisibility() != 0) {
                    this.mView.setAlpha(0.0f);
                    this.mView.setScaleY(0.0f);
                    this.mView.setScaleX(0.0f);
                }
                this.mView.animate().scaleX(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT).scaleY(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT).alpha(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT).setDuration(200).setInterpolator(AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR).setListener(new AnimatorListenerAdapter() {
                    public void onAnimationStart(Animator animation) {
                        FloatingActionButtonIcs.this.mView.internalSetVisibility(0, fromUser);
                    }

                    public void onAnimationEnd(Animator animation) {
                        FloatingActionButtonIcs.this.mAnimState = 0;
                        if (listener != null) {
                            listener.onShown();
                        }
                    }
                });
                return;
            }
            this.mView.internalSetVisibility(0, fromUser);
            this.mView.setAlpha(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            this.mView.setScaleY(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            this.mView.setScaleX(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            if (listener != null) {
                listener.onShown();
            }
        }
    }

    private boolean shouldAnimateVisibilityChange() {
        return ViewCompat.isLaidOut(this.mView) && !this.mView.isInEditMode();
    }

    private void updateFromViewRotation() {
        if (VERSION.SDK_INT == 19) {
            if (this.mRotation % 90.0f != 0.0f) {
                if (this.mView.getLayerType() != 1) {
                    this.mView.setLayerType(1, null);
                }
            } else if (this.mView.getLayerType() != 0) {
                this.mView.setLayerType(0, null);
            }
        }
        if (this.mShadowDrawable != null) {
            this.mShadowDrawable.setRotation(-this.mRotation);
        }
        if (this.mBorderDrawable != null) {
            this.mBorderDrawable.setRotation(-this.mRotation);
        }
    }
}
