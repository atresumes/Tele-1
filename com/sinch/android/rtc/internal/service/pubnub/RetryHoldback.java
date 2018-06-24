package com.sinch.android.rtc.internal.service.pubnub;

import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import com.sinch.android.rtc.internal.InternalErrorCodes;
import com.sinch.android.rtc.internal.client.Sleepable;

public class RetryHoldback {
    public static final int[] HOLDBACK_STEPS = new int[]{0, Callback.DEFAULT_DRAG_ANIMATION_DURATION, 1000, InternalErrorCodes.CapabilityUserNotFound, InternalErrorCodes.ApiApiCallFailed, 6000, 10000, 20000};
    private Sleepable sleepable;

    public RetryHoldback(Sleepable sleepable) {
        this.sleepable = sleepable;
    }

    public int getCurrentHoldBack(int i) {
        return HOLDBACK_STEPS[Math.min(HOLDBACK_STEPS.length - 1, i)];
    }

    public void holdback(int i) {
        this.sleepable.sleep((long) getCurrentHoldBack(i));
    }
}
