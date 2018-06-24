package org.webrtc.sinch;

import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Size;
import java.util.ArrayList;
import java.util.List;
import org.webrtc.sinch.CameraEnumerationAndroid.CaptureFormat;
import org.webrtc.sinch.CameraEnumerationAndroid.CaptureFormat.FramerateRange;
import org.webrtc.sinch.CameraVideoCapturer.CameraEventsHandler;

public class Camera1Enumerator implements CameraEnumerator {
    private static final String TAG = "Camera1Enumerator";
    private static List<List<CaptureFormat>> cachedSupportedFormats;

    public static List<FramerateRange> convertFramerates(List<int[]> list) {
        List<FramerateRange> arrayList = new ArrayList();
        for (int[] iArr : list) {
            arrayList.add(new FramerateRange(iArr[0], iArr[1]));
        }
        return arrayList;
    }

    public static List<Size> convertSizes(List<Size> list) {
        List<Size> arrayList = new ArrayList();
        for (Size size : list) {
            arrayList.add(new Size(size.width, size.height));
        }
        return arrayList;
    }

    private static java.util.List<org.webrtc.sinch.CameraEnumerationAndroid.CaptureFormat> enumerateFormats(int r9) {
        /* JADX: method processing error */
/*
Error: java.util.NoSuchElementException
	at java.util.HashMap$HashIterator.nextNode(HashMap.java:1431)
	at java.util.HashMap$KeyIterator.next(HashMap.java:1453)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.applyRemove(BlockFinallyExtract.java:535)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.extractFinally(BlockFinallyExtract.java:175)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.processExceptionHandler(BlockFinallyExtract.java:79)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.visit(BlockFinallyExtract.java:51)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
        /*
        r0 = 0;
        r1 = "Camera1Enumerator";
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "Get supported formats for camera index ";
        r2 = r2.append(r3);
        r2 = r2.append(r9);
        r3 = ".";
        r2 = r2.append(r3);
        r2 = r2.toString();
        org.webrtc.sinch.Logging.m776d(r1, r2);
        r4 = android.os.SystemClock.elapsedRealtime();
        r1 = 0;
        r2 = "Camera1Enumerator";	 Catch:{ RuntimeException -> 0x00d7, all -> 0x00fb }
        r3 = new java.lang.StringBuilder;	 Catch:{ RuntimeException -> 0x00d7, all -> 0x00fb }
        r3.<init>();	 Catch:{ RuntimeException -> 0x00d7, all -> 0x00fb }
        r6 = "Opening camera with index ";	 Catch:{ RuntimeException -> 0x00d7, all -> 0x00fb }
        r3 = r3.append(r6);	 Catch:{ RuntimeException -> 0x00d7, all -> 0x00fb }
        r3 = r3.append(r9);	 Catch:{ RuntimeException -> 0x00d7, all -> 0x00fb }
        r3 = r3.toString();	 Catch:{ RuntimeException -> 0x00d7, all -> 0x00fb }
        org.webrtc.sinch.Logging.m776d(r2, r3);	 Catch:{ RuntimeException -> 0x00d7, all -> 0x00fb }
        r1 = android.hardware.Camera.open(r9);	 Catch:{ RuntimeException -> 0x00d7, all -> 0x00fb }
        r6 = r1.getParameters();	 Catch:{ RuntimeException -> 0x00d7, all -> 0x00fb }
        if (r1 == 0) goto L_0x0049;
    L_0x0046:
        r1.release();
    L_0x0049:
        r1 = new java.util.ArrayList;
        r1.<init>();
        r2 = r6.getSupportedPreviewFpsRange();	 Catch:{ Exception -> 0x0089 }
        if (r2 == 0) goto L_0x0102;	 Catch:{ Exception -> 0x0089 }
    L_0x0054:
        r0 = r2.size();	 Catch:{ Exception -> 0x0089 }
        r0 = r0 + -1;	 Catch:{ Exception -> 0x0089 }
        r0 = r2.get(r0);	 Catch:{ Exception -> 0x0089 }
        r0 = (int[]) r0;	 Catch:{ Exception -> 0x0089 }
        r2 = 0;	 Catch:{ Exception -> 0x0089 }
        r2 = r0[r2];	 Catch:{ Exception -> 0x0089 }
        r3 = 1;	 Catch:{ Exception -> 0x0089 }
        r0 = r0[r3];	 Catch:{ Exception -> 0x0089 }
        r3 = r2;	 Catch:{ Exception -> 0x0089 }
        r2 = r0;	 Catch:{ Exception -> 0x0089 }
    L_0x0068:
        r0 = r6.getSupportedPreviewSizes();	 Catch:{ Exception -> 0x0089 }
        r6 = r0.iterator();	 Catch:{ Exception -> 0x0089 }
    L_0x0070:
        r0 = r6.hasNext();	 Catch:{ Exception -> 0x0089 }
        if (r0 == 0) goto L_0x00a2;	 Catch:{ Exception -> 0x0089 }
    L_0x0076:
        r0 = r6.next();	 Catch:{ Exception -> 0x0089 }
        r0 = (android.hardware.Camera.Size) r0;	 Catch:{ Exception -> 0x0089 }
        r7 = new org.webrtc.sinch.CameraEnumerationAndroid$CaptureFormat;	 Catch:{ Exception -> 0x0089 }
        r8 = r0.width;	 Catch:{ Exception -> 0x0089 }
        r0 = r0.height;	 Catch:{ Exception -> 0x0089 }
        r7.<init>(r8, r0, r3, r2);	 Catch:{ Exception -> 0x0089 }
        r1.add(r7);	 Catch:{ Exception -> 0x0089 }
        goto L_0x0070;
    L_0x0089:
        r0 = move-exception;
        r2 = "Camera1Enumerator";
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r6 = "getSupportedFormats() failed on camera index ";
        r3 = r3.append(r6);
        r3 = r3.append(r9);
        r3 = r3.toString();
        org.webrtc.sinch.Logging.m778e(r2, r3, r0);
    L_0x00a2:
        r2 = android.os.SystemClock.elapsedRealtime();
        r0 = "Camera1Enumerator";
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r7 = "Get supported formats for camera index ";
        r6 = r6.append(r7);
        r6 = r6.append(r9);
        r7 = " done.";
        r6 = r6.append(r7);
        r7 = " Time spent: ";
        r6 = r6.append(r7);
        r2 = r2 - r4;
        r2 = r6.append(r2);
        r3 = " ms.";
        r2 = r2.append(r3);
        r2 = r2.toString();
        org.webrtc.sinch.Logging.m776d(r0, r2);
        r0 = r1;
    L_0x00d6:
        return r0;
    L_0x00d7:
        r0 = move-exception;
        r2 = "Camera1Enumerator";	 Catch:{ RuntimeException -> 0x00d7, all -> 0x00fb }
        r3 = new java.lang.StringBuilder;	 Catch:{ RuntimeException -> 0x00d7, all -> 0x00fb }
        r3.<init>();	 Catch:{ RuntimeException -> 0x00d7, all -> 0x00fb }
        r4 = "Open camera failed on camera index ";	 Catch:{ RuntimeException -> 0x00d7, all -> 0x00fb }
        r3 = r3.append(r4);	 Catch:{ RuntimeException -> 0x00d7, all -> 0x00fb }
        r3 = r3.append(r9);	 Catch:{ RuntimeException -> 0x00d7, all -> 0x00fb }
        r3 = r3.toString();	 Catch:{ RuntimeException -> 0x00d7, all -> 0x00fb }
        org.webrtc.sinch.Logging.m778e(r2, r3, r0);	 Catch:{ RuntimeException -> 0x00d7, all -> 0x00fb }
        r0 = new java.util.ArrayList;	 Catch:{ RuntimeException -> 0x00d7, all -> 0x00fb }
        r0.<init>();	 Catch:{ RuntimeException -> 0x00d7, all -> 0x00fb }
        if (r1 == 0) goto L_0x00d6;
    L_0x00f7:
        r1.release();
        goto L_0x00d6;
    L_0x00fb:
        r0 = move-exception;
        if (r1 == 0) goto L_0x0101;
    L_0x00fe:
        r1.release();
    L_0x0101:
        throw r0;
    L_0x0102:
        r2 = r0;
        r3 = r0;
        goto L_0x0068;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.webrtc.sinch.Camera1Enumerator.enumerateFormats(int):java.util.List<org.webrtc.sinch.CameraEnumerationAndroid$CaptureFormat>");
    }

    static int getCameraIndex(String str) {
        Logging.m776d(TAG, "getCameraIndex: " + str);
        for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
            if (str.equals(CameraEnumerationAndroid.getDeviceName(i))) {
                return i;
            }
        }
        throw new IllegalArgumentException("No such camera: " + str);
    }

    private static CameraInfo getCameraInfo(int i) {
        CameraInfo cameraInfo = new CameraInfo();
        try {
            Camera.getCameraInfo(i, cameraInfo);
            return cameraInfo;
        } catch (Throwable e) {
            Logging.m778e(TAG, "getCameraInfo failed on index " + i, e);
            return null;
        }
    }

    static String getDeviceName(int i) {
        CameraInfo cameraInfo = getCameraInfo(i);
        return "Camera " + i + ", Facing " + (cameraInfo.facing == 1 ? "front" : "back") + ", Orientation " + cameraInfo.orientation;
    }

    static synchronized List<CaptureFormat> getSupportedFormats(int i) {
        List<CaptureFormat> list;
        synchronized (Camera1Enumerator.class) {
            if (cachedSupportedFormats == null) {
                cachedSupportedFormats = new ArrayList();
                for (int i2 = 0; i2 < CameraEnumerationAndroid.getDeviceCount(); i2++) {
                    cachedSupportedFormats.add(enumerateFormats(i2));
                }
            }
            list = (List) cachedSupportedFormats.get(i);
        }
        return list;
    }

    public CameraVideoCapturer createCapturer(String str, CameraEventsHandler cameraEventsHandler) {
        return new VideoCapturerAndroid(str, cameraEventsHandler, true);
    }

    public String[] getDeviceNames() {
        String[] strArr = new String[Camera.getNumberOfCameras()];
        for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
            strArr[i] = getDeviceName(i);
        }
        return strArr;
    }

    public boolean isBackFacing(String str) {
        return getCameraInfo(getCameraIndex(str)).facing == 0;
    }

    public boolean isFrontFacing(String str) {
        return getCameraInfo(getCameraIndex(str)).facing == 1;
    }
}
