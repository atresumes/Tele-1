package io.card.payment;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import io.card.payment.i18n.LocalizedStrings;
import io.card.payment.i18n.StringKey;
import io.card.payment.ui.ActivityHelper;
import io.card.payment.ui.ViewUtil;
import java.io.ByteArrayInputStream;
import java.util.Date;

public final class CardIOActivity extends Activity {
    static final /* synthetic */ boolean $assertionsDisabled;
    public static final String EXTRA_CAPTURED_CARD_IMAGE = "io.card.payment.capturedCardImage";
    public static final String EXTRA_GUIDE_COLOR = "io.card.payment.guideColor";
    public static final String EXTRA_HIDE_CARDIO_LOGO = "io.card.payment.hideLogo";
    public static final String EXTRA_KEEP_APPLICATION_THEME = "io.card.payment.keepApplicationTheme";
    public static final String EXTRA_LANGUAGE_OR_LOCALE = "io.card.payment.languageOrLocale";
    public static final String EXTRA_NO_CAMERA = "io.card.payment.noCamera";
    public static final String EXTRA_REQUIRE_CARDHOLDER_NAME = "io.card.payment.requireCardholderName";
    public static final String EXTRA_REQUIRE_CVV = "io.card.payment.requireCVV";
    public static final String EXTRA_REQUIRE_EXPIRY = "io.card.payment.requireExpiry";
    public static final String EXTRA_REQUIRE_POSTAL_CODE = "io.card.payment.requirePostalCode";
    public static final String EXTRA_RESTRICT_POSTAL_CODE_TO_NUMERIC_ONLY = "io.card.payment.restrictPostalCodeToNumericOnly";
    public static final String EXTRA_RETURN_CARD_IMAGE = "io.card.payment.returnCardImage";
    public static final String EXTRA_SCAN_EXPIRY = "io.card.payment.scanExpiry";
    public static final String EXTRA_SCAN_INSTRUCTIONS = "io.card.payment.scanInstructions";
    public static final String EXTRA_SCAN_OVERLAY_LAYOUT_ID = "io.card.payment.scanOverlayLayoutId";
    public static final String EXTRA_SCAN_RESULT = "io.card.payment.scanResult";
    public static final String EXTRA_SUPPRESS_CONFIRMATION = "io.card.payment.suppressConfirmation";
    public static final String EXTRA_SUPPRESS_MANUAL_ENTRY = "io.card.payment.suppressManual";
    public static final String EXTRA_SUPPRESS_SCAN = "io.card.payment.suppressScan";
    public static final String EXTRA_USE_CARDIO_LOGO = "io.card.payment.useCardIOLogo";
    public static final String EXTRA_USE_PAYPAL_ACTIONBAR_ICON = "io.card.payment.intentSenderIsPayPal";
    public static final int RESULT_CARD_INFO;
    public static final int RESULT_CONFIRMATION_SUPPRESSED;
    public static final int RESULT_ENTRY_CANCELED;
    public static final int RESULT_SCAN_NOT_AVAILABLE;
    public static final int RESULT_SCAN_SUPPRESSED;
    private static final String TAG = CardIOActivity.class.getSimpleName();
    private static final long[] VIBRATE_PATTERN = new long[]{0, 70, 10, 40};
    private static int lastResult;
    static Bitmap markedCardImage = null;
    private static int numActivityAllocations;
    private LinearLayout customOverlayLayout;
    private CardScanner mCardScanner;
    private boolean mDetectOnly;
    private CreditCard mDetectedCard;
    private int mFrameOrientation;
    private Rect mGuideFrame;
    private int mLastDegrees;
    private FrameLayout mMainLayout;
    private OverlayView mOverlay;
    Preview mPreview;
    private RelativeLayout mUIBar;
    private boolean manualEntryFallbackOrForced = false;
    private OrientationEventListener orientationListener;
    private boolean suppressManualEntry;
    private boolean useApplicationTheme;
    private boolean waitingForPermission;

    class C06703 implements OnClickListener {
        C06703() {
        }

        public void onClick(View v) {
            CardIOActivity.this.nextActivity();
        }
    }

    static {
        boolean z;
        if (CardIOActivity.class.desiredAssertionStatus()) {
            z = false;
        } else {
            z = true;
        }
        $assertionsDisabled = z;
        lastResult = 13274384;
        int i = lastResult;
        lastResult = i + 1;
        RESULT_CARD_INFO = i;
        i = lastResult;
        lastResult = i + 1;
        RESULT_ENTRY_CANCELED = i;
        i = lastResult;
        lastResult = i + 1;
        RESULT_SCAN_NOT_AVAILABLE = i;
        i = lastResult;
        lastResult = i + 1;
        RESULT_SCAN_SUPPRESSED = i;
        i = lastResult;
        lastResult = i + 1;
        RESULT_CONFIRMATION_SUPPRESSED = i;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate()");
        numActivityAllocations++;
        if (numActivityAllocations != 1) {
            Log.i(TAG, String.format("INTERNAL WARNING: There are %d (not 1) CardIOActivity allocations!", new Object[]{Integer.valueOf(numActivityAllocations)}));
        }
        Intent clientData = getIntent();
        this.useApplicationTheme = getIntent().getBooleanExtra(EXTRA_KEEP_APPLICATION_THEME, false);
        ActivityHelper.setActivityTheme(this, this.useApplicationTheme);
        LocalizedStrings.setLanguage(clientData);
        this.mDetectOnly = clientData.getBooleanExtra(EXTRA_SUPPRESS_SCAN, false);
        String errorMsg = Util.manifestHasConfigChange(getPackageManager().resolveActivity(clientData, 65536), CardIOActivity.class);
        if (errorMsg != null) {
            throw new RuntimeException(errorMsg);
        }
        this.suppressManualEntry = clientData.getBooleanExtra(EXTRA_SUPPRESS_MANUAL_ENTRY, false);
        if (savedInstanceState != null) {
            this.waitingForPermission = savedInstanceState.getBoolean("io.card.payment.waitingForPermission");
        }
        if (clientData.getBooleanExtra(EXTRA_NO_CAMERA, false)) {
            Log.i("card.io", "EXTRA_NO_CAMERA set to true. Skipping camera.");
            this.manualEntryFallbackOrForced = true;
        } else if (CardScanner.processorSupported()) {
            try {
                if (VERSION.SDK_INT < 23) {
                    checkCamera();
                    android22AndBelowHandleCamera();
                } else if (!this.waitingForPermission) {
                    if (checkSelfPermission("android.permission.CAMERA") == -1) {
                        Log.d(TAG, "permission denied to camera - requesting it");
                        String[] permissions = new String[]{"android.permission.CAMERA"};
                        this.waitingForPermission = true;
                        requestPermissions(permissions, 11);
                        return;
                    }
                    checkCamera();
                    android23AndAboveHandleCamera();
                }
            } catch (Exception e) {
                handleGeneralExceptionError(e);
            }
        } else {
            Log.i("card.io", "Processor not Supported. Skipping camera.");
            this.manualEntryFallbackOrForced = true;
        }
    }

    private void android23AndAboveHandleCamera() {
        if (this.manualEntryFallbackOrForced) {
            finishIfSuppressManualEntry();
        } else {
            showCameraScannerOverlay();
        }
    }

    private void android22AndBelowHandleCamera() {
        if (this.manualEntryFallbackOrForced) {
            finishIfSuppressManualEntry();
            return;
        }
        requestWindowFeature(1);
        showCameraScannerOverlay();
    }

    private void finishIfSuppressManualEntry() {
        if (this.suppressManualEntry) {
            Log.i("card.io", "Camera not available and manual entry suppressed.");
            setResultAndFinish(RESULT_SCAN_NOT_AVAILABLE, null);
        }
    }

    private void checkCamera() {
        StringKey errorKey;
        try {
            if (!Util.hardwareSupported()) {
                errorKey = StringKey.ERROR_NO_DEVICE_SUPPORT;
                Log.w("card.io", errorKey + ": " + LocalizedStrings.getString(errorKey));
                this.manualEntryFallbackOrForced = true;
            }
        } catch (CameraUnavailableException e) {
            errorKey = StringKey.ERROR_CAMERA_CONNECT_FAIL;
            String localizedError = LocalizedStrings.getString(errorKey);
            Log.e("card.io", errorKey + ": " + localizedError);
            Toast toast = Toast.makeText(this, localizedError, 1);
            toast.setGravity(17, 0, -75);
            toast.show();
            this.manualEntryFallbackOrForced = true;
        }
    }

    private void showCameraScannerOverlay() {
        if (VERSION.SDK_INT >= 16) {
            getWindow().getDecorView().setSystemUiVisibility(4);
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
        }
        try {
            this.mGuideFrame = new Rect();
            this.mFrameOrientation = 1;
            if (!getIntent().getBooleanExtra("io.card.payment.cameraBypassTestMode", false)) {
                this.mCardScanner = new CardScanner(this, this.mFrameOrientation);
            } else if (getPackageName().contentEquals("io.card.development")) {
                this.mCardScanner = (CardScanner) Class.forName("io.card.payment.CardScannerTester").getConstructor(new Class[]{getClass(), Integer.TYPE}).newInstance(new Object[]{this, Integer.valueOf(this.mFrameOrientation)});
            } else {
                Log.e(TAG, getPackageName() + " is not correct");
                throw new IllegalStateException("illegal access of private extra");
            }
            this.mCardScanner.prepareScanner();
            setPreviewLayout();
            this.orientationListener = new OrientationEventListener(this, 2) {
                public void onOrientationChanged(int orientation) {
                    CardIOActivity.this.doOrientationChange(orientation);
                }
            };
        } catch (Exception e) {
            handleGeneralExceptionError(e);
        }
    }

    private void handleGeneralExceptionError(Exception e) {
        String localizedError = LocalizedStrings.getString(StringKey.ERROR_CAMERA_UNEXPECTED_FAIL);
        Log.e("card.io", "Unknown exception - please send the stack trace to support@card.io", e);
        Toast toast = Toast.makeText(this, localizedError, 1);
        toast.setGravity(17, 0, -75);
        toast.show();
        this.manualEntryFallbackOrForced = true;
    }

    private void doOrientationChange(int orientation) {
        if (orientation >= 0 && this.mCardScanner != null) {
            orientation += this.mCardScanner.getRotationalOffset();
            if (orientation > 360) {
                orientation -= 360;
            }
            int degrees = -1;
            if (orientation < 15 || orientation > 345) {
                degrees = 0;
                this.mFrameOrientation = 1;
            } else if (orientation > 75 && orientation < 105) {
                degrees = 90;
                this.mFrameOrientation = 4;
            } else if (orientation > 165 && orientation < 195) {
                degrees = 180;
                this.mFrameOrientation = 2;
            } else if (orientation > 255 && orientation < 285) {
                degrees = 270;
                this.mFrameOrientation = 3;
            }
            if (degrees >= 0 && degrees != this.mLastDegrees) {
                Log.d(TAG, "onOrientationChanged(" + degrees + ") calling setDeviceOrientation(" + this.mFrameOrientation + ")");
                this.mCardScanner.setDeviceOrientation(this.mFrameOrientation);
                setDeviceDegrees(degrees);
                if (degrees == 90) {
                    rotateCustomOverlay(270.0f);
                } else if (degrees == 270) {
                    rotateCustomOverlay(90.0f);
                } else {
                    rotateCustomOverlay((float) degrees);
                }
            }
        }
    }

    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume()");
        if (!this.waitingForPermission) {
            if (this.manualEntryFallbackOrForced) {
                nextActivity();
                return;
            }
            Util.logNativeMemoryStats();
            getWindow().addFlags(1024);
            getWindow().addFlags(128);
            ActivityHelper.setFlagSecure(this);
            setRequestedOrientation(1);
            this.orientationListener.enable();
            if (restartPreview()) {
                setFlashOn(false);
            } else {
                Log.e(TAG, "Could not connect to camera.");
                showErrorMessage(LocalizedStrings.getString(StringKey.ERROR_CAMERA_UNEXPECTED_FAIL));
                nextActivity();
            }
            doOrientationChange(this.mLastDegrees);
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("io.card.payment.waitingForPermission", this.waitingForPermission);
    }

    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause()");
        if (this.orientationListener != null) {
            this.orientationListener.disable();
        }
        setFlashOn(false);
        if (this.mCardScanner != null) {
            this.mCardScanner.pauseScanning();
        }
    }

    protected void onDestroy() {
        Log.d(TAG, "onDestroy()");
        this.mOverlay = null;
        numActivityAllocations--;
        if (this.orientationListener != null) {
            this.orientationListener.disable();
        }
        setFlashOn(false);
        if (this.mCardScanner != null) {
            this.mCardScanner.endScanning();
            this.mCardScanner = null;
        }
        super.onDestroy();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 11:
                this.waitingForPermission = false;
                if (grantResults.length <= 0 || grantResults[0] != 0) {
                    this.manualEntryFallbackOrForced = true;
                } else {
                    showCameraScannerOverlay();
                }
                onResume();
                return;
            default:
                return;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, String.format("onActivityResult(requestCode:%d, resultCode:%d, ...", new Object[]{Integer.valueOf(requestCode), Integer.valueOf(resultCode)}));
        switch (requestCode) {
            case 10:
                if (resultCode == 0) {
                    Log.d(TAG, "ignoring onActivityResult(RESULT_CANCELED) caused only when Camera Permissions are Denied in Android 23");
                    return;
                } else if (resultCode == RESULT_CARD_INFO || resultCode == RESULT_ENTRY_CANCELED || this.manualEntryFallbackOrForced) {
                    if (data == null || !data.hasExtra(EXTRA_SCAN_RESULT)) {
                        Log.d(TAG, "no data in EXTRA_SCAN_RESULT");
                    } else {
                        Log.v(TAG, "EXTRA_SCAN_RESULT: " + data.getParcelableExtra(EXTRA_SCAN_RESULT));
                    }
                    setResultAndFinish(resultCode, data);
                    return;
                } else if (this.mUIBar != null) {
                    this.mUIBar.setVisibility(0);
                    return;
                } else {
                    return;
                }
            default:
                return;
        }
    }

    public void onBackPressed() {
        Log.d(TAG, "onBackPressed()");
        if (!this.manualEntryFallbackOrForced && this.mOverlay.isAnimating()) {
            try {
                restartPreview();
            } catch (RuntimeException re) {
                Log.w(TAG, "*** could not return to preview: " + re);
            }
        } else if (this.mCardScanner != null) {
            super.onBackPressed();
        }
    }

    public static boolean canReadCardWithCamera() {
        boolean z = false;
        try {
            z = Util.hardwareSupported();
        } catch (CameraUnavailableException e) {
        } catch (RuntimeException e2) {
            Log.w(TAG, "RuntimeException accessing Util.hardwareSupported()");
        }
        return z;
    }

    public static String sdkVersion() {
        return BuildConfig.PRODUCT_VERSION;
    }

    public static Date sdkBuildDate() {
        return new Date(BuildConfig.BUILD_TIME);
    }

    public static Bitmap getCapturedCardImage(Intent intent) {
        if (intent == null || !intent.hasExtra(EXTRA_CAPTURED_CARD_IMAGE)) {
            return null;
        }
        return BitmapFactory.decodeStream(new ByteArrayInputStream(intent.getByteArrayExtra(EXTRA_CAPTURED_CARD_IMAGE)), null, new Options());
    }

    void onFirstFrame(int orientation) {
        Log.d(TAG, "onFirstFrame(" + orientation + ")");
        SurfaceView sv = this.mPreview.getSurfaceView();
        if (this.mOverlay != null) {
            this.mOverlay.setCameraPreviewRect(new Rect(sv.getLeft(), sv.getTop(), sv.getRight(), sv.getBottom()));
        }
        this.mFrameOrientation = 1;
        setDeviceDegrees(0);
        if (orientation != this.mFrameOrientation) {
            Log.wtf("card.io", "the orientation of the scanner doesn't match the orientation of the activity");
        }
        onEdgeUpdate(new DetectionInfo());
    }

    void onEdgeUpdate(DetectionInfo dInfo) {
        this.mOverlay.setDetectionInfo(dInfo);
    }

    void onCardDetected(Bitmap detectedBitmap, DetectionInfo dInfo) {
        float sf;
        Log.d(TAG, "onCardDetected()");
        try {
            ((Vibrator) getSystemService("vibrator")).vibrate(VIBRATE_PATTERN, -1);
        } catch (SecurityException e) {
            Log.e("card.io", "Could not activate vibration feedback. Please add <uses-permission android:name=\"android.permission.VIBRATE\" /> to your application's manifest.");
        } catch (Exception e2) {
            Log.w("card.io", "Exception while attempting to vibrate: ", e2);
        }
        this.mCardScanner.pauseScanning();
        this.mUIBar.setVisibility(4);
        if (dInfo.predicted()) {
            this.mDetectedCard = dInfo.creditCard();
            this.mOverlay.setDetectedCard(this.mDetectedCard);
        }
        if (this.mFrameOrientation == 1 || this.mFrameOrientation == 2) {
            sf = (((float) this.mGuideFrame.right) / 428.0f) * 0.95f;
        } else {
            sf = (((float) this.mGuideFrame.right) / 428.0f) * 1.15f;
        }
        Matrix m = new Matrix();
        Log.d(TAG, "Scale factor: " + sf);
        m.postScale(sf, sf);
        this.mOverlay.setBitmap(Bitmap.createBitmap(detectedBitmap, 0, 0, detectedBitmap.getWidth(), detectedBitmap.getHeight(), m, false));
        if (this.mDetectOnly) {
            Intent dataIntent = new Intent();
            Util.writeCapturedCardImageIfNecessary(getIntent(), dataIntent, this.mOverlay);
            setResultAndFinish(RESULT_SCAN_SUPPRESSED, dataIntent);
            return;
        }
        nextActivity();
    }

    private void nextActivity() {
        Log.d(TAG, "nextActivity()");
        final Intent origIntent = getIntent();
        if (origIntent == null || !origIntent.getBooleanExtra(EXTRA_SUPPRESS_CONFIRMATION, false)) {
            new Handler().post(new Runnable() {
                public void run() {
                    Log.d(CardIOActivity.TAG, "post(Runnable)");
                    CardIOActivity.this.getWindow().clearFlags(1024);
                    CardIOActivity.this.getWindow().addFlags(512);
                    Intent dataIntent = new Intent(CardIOActivity.this, DataEntryActivity.class);
                    Util.writeCapturedCardImageIfNecessary(origIntent, dataIntent, CardIOActivity.this.mOverlay);
                    if (CardIOActivity.this.mOverlay != null) {
                        CardIOActivity.this.mOverlay.markupCard();
                        if (!(CardIOActivity.markedCardImage == null || CardIOActivity.markedCardImage.isRecycled())) {
                            CardIOActivity.markedCardImage.recycle();
                        }
                        CardIOActivity.markedCardImage = CardIOActivity.this.mOverlay.getCardImage();
                    }
                    if (CardIOActivity.this.mDetectedCard != null) {
                        dataIntent.putExtra(CardIOActivity.EXTRA_SCAN_RESULT, CardIOActivity.this.mDetectedCard);
                        CardIOActivity.this.mDetectedCard = null;
                    } else {
                        dataIntent.putExtra("io.card.payment.manualEntryScanResult", true);
                    }
                    dataIntent.putExtras(CardIOActivity.this.getIntent());
                    dataIntent.addFlags(1082195968);
                    CardIOActivity.this.startActivityForResult(dataIntent, 10);
                }
            });
            return;
        }
        Intent dataIntent = new Intent(this, DataEntryActivity.class);
        if (this.mDetectedCard != null) {
            dataIntent.putExtra(EXTRA_SCAN_RESULT, this.mDetectedCard);
            this.mDetectedCard = null;
        }
        Util.writeCapturedCardImageIfNecessary(origIntent, dataIntent, this.mOverlay);
        setResultAndFinish(RESULT_CONFIRMATION_SUPPRESSED, dataIntent);
    }

    private void showErrorMessage(String msgStr) {
        Log.e("card.io", "error display: " + msgStr);
        Toast.makeText(this, msgStr, 1).show();
    }

    private boolean restartPreview() {
        Log.d(TAG, "restartPreview()");
        this.mDetectedCard = null;
        if ($assertionsDisabled || this.mPreview != null) {
            boolean success = this.mCardScanner.resumeScanning(this.mPreview.getSurfaceHolder());
            if (success) {
                this.mUIBar.setVisibility(0);
            }
            return success;
        }
        throw new AssertionError();
    }

    private void setDeviceDegrees(int degrees) {
        View sv = this.mPreview.getSurfaceView();
        if (sv == null) {
            Log.wtf("card.io", "surface view is null.. recovering... rotation might be weird.");
            return;
        }
        this.mGuideFrame = this.mCardScanner.getGuideFrame(sv.getWidth(), sv.getHeight());
        Rect rect = this.mGuideFrame;
        rect.top += sv.getTop();
        rect = this.mGuideFrame;
        rect.bottom += sv.getTop();
        this.mOverlay.setGuideAndRotation(this.mGuideFrame, degrees);
        this.mLastDegrees = degrees;
    }

    void toggleFlash() {
        setFlashOn(!this.mCardScanner.isFlashOn());
    }

    void setFlashOn(boolean b) {
        boolean success = (this.mPreview == null || this.mOverlay == null || !this.mCardScanner.setFlashOn(b)) ? false : true;
        if (success) {
            this.mOverlay.setTorchOn(b);
        }
    }

    void triggerAutoFocus() {
        this.mCardScanner.triggerAutoFocus(true);
    }

    private void setPreviewLayout() {
        this.mMainLayout = new FrameLayout(this);
        this.mMainLayout.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
        this.mMainLayout.setLayoutParams(new LayoutParams(-1, -1));
        FrameLayout previewFrame = new FrameLayout(this);
        previewFrame.setId(1);
        this.mCardScanner.getClass();
        this.mCardScanner.getClass();
        this.mPreview = new Preview(this, null, 640, 480);
        this.mPreview.setLayoutParams(new FrameLayout.LayoutParams(-1, -1, 48));
        previewFrame.addView(this.mPreview);
        this.mOverlay = new OverlayView(this, null, Util.deviceSupportsTorch(this));
        this.mOverlay.setLayoutParams(new LayoutParams(-1, -1));
        if (getIntent() != null) {
            this.mOverlay.setUseCardIOLogo(getIntent().getBooleanExtra(EXTRA_USE_CARDIO_LOGO, false));
            int color = getIntent().getIntExtra(EXTRA_GUIDE_COLOR, 0);
            if (color != 0) {
                int alphaRemovedColor = color | ViewCompat.MEASURED_STATE_MASK;
                if (color != alphaRemovedColor) {
                    Log.w("card.io", "Removing transparency from provided guide color.");
                }
                this.mOverlay.setGuideColor(alphaRemovedColor);
            } else {
                this.mOverlay.setGuideColor(-16711936);
            }
            this.mOverlay.setHideCardIOLogo(getIntent().getBooleanExtra(EXTRA_HIDE_CARDIO_LOGO, false));
            String scanInstructions = getIntent().getStringExtra(EXTRA_SCAN_INSTRUCTIONS);
            if (scanInstructions != null) {
                this.mOverlay.setScanInstructions(scanInstructions);
            }
        }
        previewFrame.addView(this.mOverlay);
        RelativeLayout.LayoutParams previewParams = new RelativeLayout.LayoutParams(-1, -1);
        previewParams.addRule(10);
        previewParams.addRule(2, 2);
        this.mMainLayout.addView(previewFrame, previewParams);
        this.mUIBar = new RelativeLayout(this);
        this.mUIBar.setGravity(80);
        RelativeLayout.LayoutParams mUIBarParams = new RelativeLayout.LayoutParams(-1, -2);
        previewParams.addRule(12);
        this.mUIBar.setLayoutParams(mUIBarParams);
        this.mUIBar.setId(2);
        this.mUIBar.setGravity(85);
        if (!this.suppressManualEntry) {
            Button keyboardBtn = new Button(this);
            keyboardBtn.setId(3);
            keyboardBtn.setText(LocalizedStrings.getString(StringKey.KEYBOARD));
            keyboardBtn.setOnClickListener(new C06703());
            this.mUIBar.addView(keyboardBtn);
            ViewUtil.styleAsButton(keyboardBtn, false, this, this.useApplicationTheme);
            if (!this.useApplicationTheme) {
                keyboardBtn.setTextSize(14.0f);
            }
            keyboardBtn.setMinimumHeight(ViewUtil.typedDimensionValueToPixelsInt("42dip", this));
            RelativeLayout.LayoutParams keyboardParams = (RelativeLayout.LayoutParams) keyboardBtn.getLayoutParams();
            keyboardParams.width = -2;
            keyboardParams.height = -2;
            keyboardParams.addRule(12);
            ViewUtil.setPadding(keyboardBtn, "16dip", null, "16dip", null);
            ViewUtil.setMargins(keyboardBtn, "4dip", "4dip", "4dip", "4dip");
        }
        LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -2);
        layoutParams.addRule(12);
        int uiBarMarginPx = (int) ((15.0f * getResources().getDisplayMetrics().density) + 0.5f);
        layoutParams.setMargins(0, uiBarMarginPx, 0, uiBarMarginPx);
        this.mMainLayout.addView(this.mUIBar, layoutParams);
        if (getIntent() != null) {
            if (this.customOverlayLayout != null) {
                this.mMainLayout.removeView(this.customOverlayLayout);
                this.customOverlayLayout = null;
            }
            int resourceId = getIntent().getIntExtra(EXTRA_SCAN_OVERLAY_LAYOUT_ID, -1);
            if (resourceId != -1) {
                this.customOverlayLayout = new LinearLayout(this);
                this.customOverlayLayout.setLayoutParams(new LayoutParams(-1, -1));
                getLayoutInflater().inflate(resourceId, this.customOverlayLayout);
                this.mMainLayout.addView(this.customOverlayLayout);
            }
        }
        setContentView(this.mMainLayout);
    }

    private void rotateCustomOverlay(float degrees) {
        if (this.customOverlayLayout != null) {
            Animation an = new RotateAnimation(0.0f, degrees, (float) (this.customOverlayLayout.getWidth() / 2), (float) (this.customOverlayLayout.getHeight() / 2));
            an.setDuration(0);
            an.setRepeatCount(0);
            an.setFillAfter(true);
            this.customOverlayLayout.setAnimation(an);
        }
    }

    private void setResultAndFinish(int resultCode, Intent data) {
        setResult(resultCode, data);
        markedCardImage = null;
        finish();
    }

    public Rect getTorchRect() {
        if (this.mOverlay == null) {
            return null;
        }
        return this.mOverlay.getTorchRect();
    }
}
