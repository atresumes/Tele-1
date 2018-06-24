package org.webrtc.sinch;

import android.content.Context;

public class WebRTCDriver {
    public static native void deinit();

    public static native void init(Context context);
}
