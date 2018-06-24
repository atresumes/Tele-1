package com.payUMoney.sdk.dialog;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import com.payUMoney.sdk.C0360R;

public class SdkOtpProgressDialog extends FragmentActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0360R.layout.mainprogress);
        showDialog(this, findViewById(C0360R.id.progress));
    }

    public static void showDialog(Context mContext, View view) {
        view.setVisibility(0);
        showProgressDialog(mContext, view);
    }

    public static void removeDialog(View view) {
        view.setVisibility(8);
    }

    public static void showProgressDialog(Context mContext, View view) {
        final Handler handler = new Handler();
        final LinearLayout firstInnerCircle = (LinearLayout) view.findViewById(C0360R.id.firstOuterCircle);
        final LinearLayout secondInnerCircle = (LinearLayout) view.findViewById(C0360R.id.secondOuterCircle);
        final LinearLayout thirdInnerCircle = (LinearLayout) view.findViewById(C0360R.id.thirdOuterCircle);
        final LinearLayout forthInnerCircle = (LinearLayout) view.findViewById(C0360R.id.forthOuterCircle);
        final LinearLayout fifthInnerCircle = (LinearLayout) view.findViewById(C0360R.id.fifthOuterCircle);
        firstInnerCircle.setBackgroundResource(C0360R.drawable.inner_circle);
        final Animation animation = AnimationUtils.loadAnimation(mContext, C0360R.anim.cb_anim);
        firstInnerCircle.startAnimation(animation);
        final View view2 = view;
        handler.postDelayed(new Runnable() {
            boolean forthImage = false;

            class C03881 implements Runnable {
                C03881() {
                }

                public void run() {
                    firstInnerCircle.startAnimation(animation);
                    firstInnerCircle.setBackgroundResource(C0360R.drawable.inner_circle);
                }
            }

            public void run() {
                if (view2.getVisibility() == 0) {
                    if (firstInnerCircle.getAnimation() != null) {
                        firstInnerCircle.setBackgroundResource(C0360R.drawable.sdk_outer_circle);
                        firstInnerCircle.clearAnimation();
                        secondInnerCircle.startAnimation(animation);
                        secondInnerCircle.setBackgroundResource(C0360R.drawable.inner_circle);
                    } else if (secondInnerCircle.getAnimation() != null) {
                        secondInnerCircle.setBackgroundResource(C0360R.drawable.sdk_outer_circle);
                        secondInnerCircle.clearAnimation();
                        thirdInnerCircle.startAnimation(animation);
                        thirdInnerCircle.setBackgroundResource(C0360R.drawable.inner_circle);
                    } else if (thirdInnerCircle.getAnimation() != null) {
                        thirdInnerCircle.setBackgroundResource(C0360R.drawable.sdk_outer_circle);
                        thirdInnerCircle.clearAnimation();
                        forthInnerCircle.startAnimation(animation);
                        forthInnerCircle.setBackgroundResource(C0360R.drawable.inner_circle);
                    } else if (forthInnerCircle.getAnimation() != null) {
                        forthInnerCircle.setBackgroundResource(C0360R.drawable.sdk_outer_circle);
                        forthInnerCircle.clearAnimation();
                        fifthInnerCircle.startAnimation(animation);
                        fifthInnerCircle.setBackgroundResource(C0360R.drawable.inner_circle);
                    } else if (fifthInnerCircle.getAnimation() != null) {
                        fifthInnerCircle.setBackgroundResource(C0360R.drawable.sdk_outer_circle);
                        fifthInnerCircle.clearAnimation();
                        this.forthImage = true;
                        new Handler().postDelayed(new C03881(), 200);
                    }
                    if (this.forthImage) {
                        this.forthImage = false;
                        handler.postDelayed(this, 400);
                        return;
                    }
                    handler.postDelayed(this, 200);
                }
            }
        }, 200);
    }
}
