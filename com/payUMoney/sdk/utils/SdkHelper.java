package com.payUMoney.sdk.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.payUMoney.sdk.C0360R;
import com.payUMoney.sdk.SdkConstants;
import org.json.JSONException;
import org.json.JSONObject;

public class SdkHelper {
    private static long mLastClickTime = 0;
    private static ProgressDialog progressDialog;

    public static boolean checkNetwork(Context c) {
        ConnectivityManager cm = (ConnectivityManager) c.getSystemService("connectivity");
        if (cm == null) {
            return false;
        }
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork == null || !activeNetwork.isConnectedOrConnecting()) {
            return false;
        }
        return true;
    }

    public static void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog = null;
    }

    public static void showProgressDialog(Context mActivity, String strMessage) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(mActivity);
        } else if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        ProgressDialog progressDialog = progressDialog;
        if (strMessage.equals(null)) {
            strMessage = "Loading...";
        }
        progressDialog.setMessage(strMessage);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        try {
            progressDialog.show();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public static boolean setStringPreference(Context context, String key, String value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences == null || TextUtils.isEmpty(key)) {
            return false;
        }
        Editor editor = preferences.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public static String getStringPreference(Context context, String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences != null) {
            return preferences.getString(key, null);
        }
        return null;
    }

    public static void showProgressDialog(Context mActivity, String strMessage, boolean isCancellable) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(mActivity);
        } else if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog = new ProgressDialog(mActivity);
        ProgressDialog progressDialog = progressDialog;
        if (strMessage.equals(null)) {
            strMessage = "Loading...";
        }
        progressDialog.setMessage(strMessage);
        progressDialog.setCancelable(isCancellable);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    public static boolean isValidClick() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return false;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        return true;
    }

    public static void showToastMessage(Activity mActivity, String strMessage, boolean warningMessage) {
        View layout = mActivity.getLayoutInflater().inflate(C0360R.layout.sdk_toast_layout, (ViewGroup) mActivity.findViewById(C0360R.id.toast_layout_root));
        ((TextView) layout.findViewById(C0360R.id.toast_textView)).setText(strMessage);
        layout.findViewById(C0360R.id.toast_layout_root).setBackgroundColor(mActivity.getApplicationContext().getResources().getColor(warningMessage ? 17170454 : C0360R.color.primary_green));
        Toast toast = new Toast(mActivity.getApplicationContext());
        toast.setGravity(80, 0, 30);
        toast.setDuration(1);
        toast.setView(layout);
        toast.show();
    }

    public static String getAndroidID(Context context) {
        if (context == null) {
            return "";
        }
        return Secure.getString(context.getContentResolver(), "android_id");
    }

    public static String getAppVersion(Context mContext) {
        try {
            return mContext.getApplicationContext().getPackageManager().getPackageInfo(mContext.getApplicationContext().getPackageName(), 0).versionName;
        } catch (Exception e) {
            return "";
        }
    }

    public static String getUserCookieSessionId(Context c) {
        if (c == null) {
            return "";
        }
        long currentSessionTimeStamp;
        long lastUsedSessionTimeStamp = getLongPreference(c, "LAST_USED_SESSION_TIMESTAMP");
        if (SdkConstants.DEFAULT_SESSION_UPDATE_TIME + lastUsedSessionTimeStamp < System.currentTimeMillis()) {
            currentSessionTimeStamp = System.currentTimeMillis();
            setLongPreference(c, "LAST_USED_SESSION_TIMESTAMP", currentSessionTimeStamp);
        } else {
            currentSessionTimeStamp = lastUsedSessionTimeStamp;
        }
        return "UserSessionCookie = " + getAndroidID(c) + "_" + c.getPackageName() + "_" + currentSessionTimeStamp;
    }

    public static long getLongPreference(Context context, String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences != null) {
            return preferences.getLong(key, 0);
        }
        return 0;
    }

    public static boolean setLongPreference(Context context, String key, long value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences == null || TextUtils.isEmpty(key)) {
            return false;
        }
        Editor editor = preferences.edit();
        editor.putLong(key, value);
        return editor.commit();
    }

    public static boolean checkForValidString(String inputString) {
        if (inputString == null || inputString.isEmpty() || SdkConstants.NULL_STRING.equals(inputString)) {
            return false;
        }
        return true;
    }

    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static boolean hasNFC(Context context) {
        if (context != null) {
            return context.getPackageManager().hasSystemFeature("android.hardware.nfc");
        }
        return false;
    }

    public static boolean hasTelephony(Context context) {
        if (context != null) {
            return context.getPackageManager().hasSystemFeature("android.hardware.telephony");
        }
        return false;
    }

    public static String getDeviceCustomPropertyJsonString(Context c) {
        JSONObject j = new JSONObject();
        try {
            j.put(SdkConstants.WIDTH, getScreenWidth(c));
            j.put(SdkConstants.HEIGHT, getScreenHeight(c));
            j.put(SdkConstants.IS_WIFI, isConnectedWifi(c));
            j.put(SdkConstants.NFC, hasNFC(c));
            j.put(SdkConstants.TELEPHONE, hasTelephony(c));
            j.put(SdkConstants.DEVICE_ID, getAndroidID(c));
            j.put(SdkConstants.DEVICE_NAME, getDeviceName());
            j.put(SdkConstants.OS_NAME, SdkConstants.OS_NAME_VALUE);
            j.put(SdkConstants.OS_VERSION, getAndroidOSVersion());
            j.put(SdkConstants.BR_VERSION, SdkConstants.BR_VERSION_VALUE);
            return j.toString();
        } catch (JSONException e) {
            return "";
        }
    }

    private static String getAndroidOSVersion() {
        return VERSION.RELEASE + "";
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model == null || !model.startsWith(manufacturer)) {
            return capitalize(manufacturer) + ":" + model;
        }
        return capitalize(model);
    }

    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        return !Character.isUpperCase(first) ? Character.toUpperCase(first) + s.substring(1) : s;
    }

    public static boolean isUpdateSessionRequired(Context c) {
        if (c == null || SdkConstants.DEFAULT_SESSION_SEND_MAX_TIME + getLongPreference(c, SdkConstants.LAST_USED_SESSION_SEND_TIMESTAMP) >= System.currentTimeMillis()) {
            return false;
        }
        setLongPreference(c, SdkConstants.LAST_USED_SESSION_SEND_TIMESTAMP, System.currentTimeMillis());
        return true;
    }

    public static void resetSessionUpdateTimeStamp(Context c) {
        if (c != null) {
            setLongPreference(c, SdkConstants.LAST_USED_SESSION_SEND_TIMESTAMP, 0);
        }
    }

    public static synchronized String getUserSessionId(Context c) {
        String str;
        synchronized (SdkHelper.class) {
            if (c == null) {
                str = "";
            } else {
                str = "";
                if (SdkConstants.DEFAULT_SESSION_UPDATE_TIME + getLongPreference(c, SdkConstants.LAST_USED_SESSION_TIMESTAMP) < System.currentTimeMillis()) {
                    str = getAndroidID(c) + "_" + c.getPackageName() + "_" + System.currentTimeMillis();
                    setStringPreference(c, SdkConstants.LAST_SESSION_ID, str);
                } else {
                    str = getStringPreference(c, SdkConstants.LAST_SESSION_ID);
                    if (TextUtils.isEmpty(str)) {
                        str = getAndroidID(c) + "_" + c.getPackageName() + "_" + System.currentTimeMillis();
                        setStringPreference(c, SdkConstants.LAST_SESSION_ID, str);
                    }
                }
                setLongPreference(c, SdkConstants.LAST_USED_SESSION_TIMESTAMP, System.currentTimeMillis());
            }
        }
        return str;
    }

    public static NetworkInfo getNetworkInfo(Context context) {
        return ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
    }

    public static boolean isConnectedWifi(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        if (info != null && info.isConnected() && info.getType() == 1) {
            return true;
        }
        return false;
    }
}
