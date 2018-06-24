package com.sinch.android.rtc.internal.client;

import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallListener;
import com.sinch.android.rtc.internal.natives.AccessNumber;
import com.sinch.android.rtc.internal.natives.ConnectionInfo;

public interface InternalCallListener extends CallListener {
    void onCallTransferFailed(Call call, SinchError sinchError);

    void onCallTransferSucceeded(Call call, AccessNumber accessNumber);

    void onCallTransferringOnRemoteEnd(Call call);

    void onConnectionInfo(Call call, ConnectionInfo connectionInfo);
}
