package org.webrtc.sinch;

import org.webrtc.sinch.CameraVideoCapturer.CameraEventsHandler;

public interface CameraEnumerator {
    CameraVideoCapturer createCapturer(String str, CameraEventsHandler cameraEventsHandler);

    String[] getDeviceNames();

    boolean isBackFacing(String str);

    boolean isFrontFacing(String str);
}
