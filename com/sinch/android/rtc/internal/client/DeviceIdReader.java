package com.sinch.android.rtc.internal.client;

import android.content.Context;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

public class DeviceIdReader {
    private static final String ILLEGAL_ANDROID_ID_1 = "9774d56d682e549c";
    private static final String ILLEGAL_ANDROID_ID_2 = "undefined";

    public String getDeviceId(Context context) {
        String string = Secure.getString(context.getContentResolver(), "android_id");
        if (string != null && !ILLEGAL_ANDROID_ID_1.equals(string) && !ILLEGAL_ANDROID_ID_2.equals(string)) {
            return string;
        }
        if (context.checkCallingOrSelfPermission("android.permission.READ_PHONE_STATE") == 0) {
            string = Sha1Utils.sha1RawBytesAsString(((TelephonyManager) context.getSystemService("phone")).getDeviceId());
            if (string != null) {
                return string;
            }
        }
        return "unknown-deviceid";
    }
}
