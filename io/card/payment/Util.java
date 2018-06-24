package io.card.payment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Build;
import android.os.Debug;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.util.List;

class Util {
    private static final String TAG = Util.class.getSimpleName();
    private static final boolean TORCH_BLACK_LISTED = Build.MODEL.equals("DROID2");
    private static Boolean sHardwareSupported;

    Util() {
    }

    public static boolean deviceSupportsTorch(Context context) {
        return !TORCH_BLACK_LISTED && context.getPackageManager().hasSystemFeature("android.hardware.camera.flash");
    }

    public static String manifestHasConfigChange(ResolveInfo resolveInfo, Class activityClass) {
        String error = null;
        if (resolveInfo == null) {
            error = String.format("Didn't find %s in the AndroidManifest.xml", new Object[]{activityClass.getName()});
        } else if (!hasConfigFlag(resolveInfo.activityInfo.configChanges, 128)) {
            error = activityClass.getName() + " requires attribute android:configChanges=\"orientation\"";
        }
        if (error != null) {
            Log.e("card.io", error);
        }
        return error;
    }

    public static boolean hasConfigFlag(int config, int configFlag) {
        return (config & configFlag) == configFlag;
    }

    public static boolean hardwareSupported() {
        if (sHardwareSupported == null) {
            sHardwareSupported = Boolean.valueOf(hardwareSupportCheck());
        }
        return sHardwareSupported.booleanValue();
    }

    private static boolean hardwareSupportCheck() {
        Log.i("card.io", "Checking hardware support...");
        if (CardScanner.processorSupported()) {
            try {
                Camera c = Camera.open();
                if (c == null) {
                    Log.w("card.io", "- No camera found");
                    return false;
                }
                List<Size> list = c.getParameters().getSupportedPreviewSizes();
                c.release();
                boolean supportsVGA = false;
                for (Size s : list) {
                    if (s.width == 640 && s.height == 480) {
                        supportsVGA = true;
                        break;
                    }
                }
                if (supportsVGA) {
                    return true;
                }
                Log.w("card.io", "- Camera resolution is insufficient");
                return false;
            } catch (RuntimeException e) {
                Log.w("card.io", "- Error opening camera: " + e);
                throw new CameraUnavailableException();
            }
        }
        Log.w("card.io", "- Processor type is not supported");
        return false;
    }

    public static String getNativeMemoryStats() {
        return "(free/alloc'd/total)" + Debug.getNativeHeapFreeSize() + "/" + Debug.getNativeHeapAllocatedSize() + "/" + Debug.getNativeHeapSize();
    }

    public static void logNativeMemoryStats() {
        Log.d("MEMORY", "Native memory stats: " + getNativeMemoryStats());
    }

    public static Rect rectGivenCenter(Point center, int width, int height) {
        return new Rect(center.x - (width / 2), center.y - (height / 2), center.x + (width / 2), center.y + (height / 2));
    }

    public static void setupTextPaintStyle(Paint paint) {
        paint.setColor(-1);
        paint.setStyle(Style.FILL);
        paint.setTypeface(Typeface.create(Typeface.SANS_SERIF, 1));
        paint.setAntiAlias(true);
        paint.setShadowLayer(1.5f, 0.5f, 0.0f, Color.HSVToColor(Callback.DEFAULT_DRAG_ANIMATION_DURATION, new float[]{0.0f, 0.0f, 0.0f}));
    }

    static void writeCapturedCardImageIfNecessary(Intent origIntent, Intent dataIntent, OverlayView mOverlay) {
        if (origIntent.getBooleanExtra(CardIOActivity.EXTRA_RETURN_CARD_IMAGE, false) && mOverlay != null && mOverlay.getBitmap() != null) {
            ByteArrayOutputStream scaledCardBytes = new ByteArrayOutputStream();
            mOverlay.getBitmap().compress(CompressFormat.JPEG, 80, scaledCardBytes);
            dataIntent.putExtra(CardIOActivity.EXTRA_CAPTURED_CARD_IMAGE, scaledCardBytes.toByteArray());
        }
    }
}
