package com.sinch.android.rtc.internal;

import com.sinch.android.rtc.SinchError;
import java.util.List;

public interface UserCapabilitiesCallback {
    void onFetchFailed(String str, SinchError sinchError);

    void onFetchSuccess(String str, List<Capability> list);
}
