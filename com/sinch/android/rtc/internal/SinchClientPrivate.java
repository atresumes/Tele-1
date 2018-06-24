package com.sinch.android.rtc.internal;

import com.sinch.android.rtc.internal.service.http.SinchHttpServiceObserver;

public interface SinchClientPrivate {
    void setHttpServiceObserver(SinchHttpServiceObserver sinchHttpServiceObserver);
}
