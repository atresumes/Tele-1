package org.webrtc.sinch;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Build.VERSION;
import android.os.SystemClock;
import android.util.Range;
import android.util.Size;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.webrtc.sinch.CameraEnumerationAndroid.CaptureFormat;

@TargetApi(21)
public class Camera2Enumerator {
    private static final double NANO_SECONDS_PER_SECOND = 1.0E9d;
    private static final String TAG = "Camera2Enumerator";
    private static final Map<String, List<CaptureFormat>> cachedSupportedFormats = new HashMap();

    public static List<CaptureFormat> getSupportedFormats(Context context, String str) {
        return getSupportedFormats((CameraManager) context.getSystemService("camera"), str);
    }

    public static List<CaptureFormat> getSupportedFormats(CameraManager cameraManager, String str) {
        synchronized (cachedSupportedFormats) {
            if (cachedSupportedFormats.containsKey(str)) {
                return (List) cachedSupportedFormats.get(str);
            }
            Logging.m776d(TAG, "Get supported formats for camera index " + str + ".");
            long elapsedRealtime = SystemClock.elapsedRealtime();
            try {
                CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(str);
                int i = 0;
                for (Range upper : (Range[]) cameraCharacteristics.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES)) {
                    i = Math.max(i, ((Integer) upper.getUpper()).intValue());
                }
                StreamConfigurationMap streamConfigurationMap = (StreamConfigurationMap) cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                Size[] outputSizes = streamConfigurationMap.getOutputSizes(35);
                if (outputSizes == null) {
                    throw new RuntimeException("ImageFormat.YUV_420_888 not supported.");
                }
                List<CaptureFormat> arrayList = new ArrayList();
                for (Size size : outputSizes) {
                    long j = 0;
                    try {
                        j = streamConfigurationMap.getOutputMinFrameDuration(35, size);
                    } catch (Exception e) {
                    }
                    arrayList.add(new CaptureFormat(size.getWidth(), size.getHeight(), 0, (j == 0 ? i : (int) Math.round(NANO_SECONDS_PER_SECOND / ((double) j))) * 1000));
                }
                cachedSupportedFormats.put(str, arrayList);
                Logging.m776d(TAG, "Get supported formats for camera index " + str + " done." + " Time spent: " + (SystemClock.elapsedRealtime() - elapsedRealtime) + " ms.");
                return arrayList;
            } catch (Exception e2) {
                Logging.m777e(TAG, "getCameraCharacteristics(): " + e2);
                return new ArrayList();
            }
        }
    }

    public static boolean isSupported() {
        return VERSION.SDK_INT >= 21;
    }
}
