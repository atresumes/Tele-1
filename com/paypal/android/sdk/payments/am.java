package com.paypal.android.sdk.payments;

import android.os.SystemClock;
import android.view.MotionEvent;
import android.widget.EditText;

final class am implements Runnable {
    private /* synthetic */ EditText f771a;

    am(LoginActivity loginActivity, EditText editText) {
        this.f771a = editText;
    }

    public final void run() {
        this.f771a.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), 0, 9999.0f, 0.0f, 0));
        this.f771a.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), 1, 9999.0f, 0.0f, 0));
    }
}
