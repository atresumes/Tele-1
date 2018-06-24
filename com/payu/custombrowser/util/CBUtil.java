package com.payu.custombrowser.util;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.TrafficStats;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build.VERSION;
import android.support.v4.app.NotificationCompat.Style;
import android.support.v4.media.TransportMediator;
import android.support.v7.app.NotificationCompat.Builder;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import com.bumptech.glide.load.Key;
import com.payUMoney.sdk.SdkConstants;
import com.payu.custombrowser.C0517R;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CBUtil {
    public static final String CB_PREFERENCE = "com.payu.custombrowser.payucustombrowser";
    private static SharedPreferences sharedPreferences;

    public String getLogMessage(Context context, String key, String value, String bank, String sdkMerchantKey, String trnxID, String pageType) {
        try {
            JSONObject eventAnalytics = new JSONObject();
            eventAnalytics.put(CBAnalyticsConstant.PAYU_ID, getCookie(CBConstant.PAYUID, context));
            eventAnalytics.put("txnid", trnxID);
            eventAnalytics.put("merchant_key", sdkMerchantKey);
            eventAnalytics.put(CBAnalyticsConstant.PAGE_TYPE, pageType);
            eventAnalytics.put(CBAnalyticsConstant.KEY, key);
            eventAnalytics.put(CBAnalyticsConstant.VALUE, URLEncoder.encode(value, Key.STRING_CHARSET_NAME));
            String str = "bank";
            if (bank == null) {
                bank = "";
            }
            eventAnalytics.put(str, bank);
            eventAnalytics.put("package_name", context.getPackageName());
            return eventAnalytics.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "{}";
        }
    }

    public static String decodeContents(FileInputStream fileInputStream) {
        StringBuilder decoded = new StringBuilder();
        int i = 0;
        while (true) {
            try {
                int c = fileInputStream.read();
                if (c == -1) {
                    break;
                }
                if (i % 2 == 0) {
                    decoded.append((char) (c - ((i % 5) + 1)));
                } else {
                    decoded.append((char) (((i % 5) + 1) + c));
                }
                i++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        fileInputStream.close();
        return decoded.toString();
    }

    public static void setAlpha(float alpha, View view) {
        if (VERSION.SDK_INT < 11) {
            AlphaAnimation animation = new AlphaAnimation(alpha, alpha);
            animation.setDuration(10);
            animation.setFillAfter(true);
            view.startAnimation(animation);
            return;
        }
        view.setAlpha(alpha);
    }

    public static String updateLastUrl(String lastUrl) {
        try {
            if (!lastUrl.contains(CBConstant.CB_DELIMITER)) {
                return lastUrl.length() > 128 ? lastUrl.substring(0, TransportMediator.KEYCODE_MEDIA_PAUSE) : lastUrl;
            } else {
                StringTokenizer st = new StringTokenizer(lastUrl, CBConstant.CB_DELIMITER);
                String firstURl = st.nextToken();
                String secondUrl = st.nextToken();
                if (firstURl.length() > 128) {
                    firstURl = firstURl.substring(0, 125);
                }
                if (secondUrl.length() > 128) {
                    secondUrl = secondUrl.substring(0, 125);
                }
                return firstURl + CBConstant.CB_DELIMITER + secondUrl;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void setVariableReflection(String className, String value, String varName) {
        if (value != null) {
            try {
                if (!value.trim().equals("")) {
                    Field field = Class.forName(className).getDeclaredField(varName);
                    field.setAccessible(true);
                    field.set(null, value);
                    field.setAccessible(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String filterSMS(JSONObject mBankJS, String msgBody, Context context) {
        String mPassword = null;
        if (msgBody != null) {
            try {
                if (Pattern.compile(mBankJS.getString(context.getString(C0517R.string.cb_detect_otp)), 2).matcher(msgBody).find()) {
                    Matcher match = Pattern.compile(mBankJS.getString(context.getString(C0517R.string.cb_find_otp)), 2).matcher(msgBody);
                    if (match.find()) {
                        mPassword = match.group(1).replaceAll("[^0-9]", "");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mPassword;
    }

    public HttpURLConnection getHttpsConn(String strURL, String postData) {
        try {
            return getHttpsConn(strURL, postData, -1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public HttpURLConnection getHttpsConn(String strURL, String postData, int timeout) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(strURL).openConnection();
            conn.setRequestMethod("POST");
            if (timeout != -1) {
                conn.setConnectTimeout(timeout);
            }
            conn.setRequestProperty("Content-Type", CBConstant.HTTP_URLENCODED);
            conn.setRequestProperty("Content-Length", String.valueOf(postData.length()));
            conn.setDoOutput(true);
            conn.getOutputStream().write(postData.getBytes());
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static HttpURLConnection getHttpsConn(String strURL) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(strURL).openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept-Charset", Key.STRING_CHARSET_NAME);
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static StringBuffer getStringBufferFromInputStream(InputStream responseInputStream) {
        try {
            StringBuffer stringBuffer = new StringBuffer();
            byte[] byteContainer = new byte[1024];
            while (true) {
                int i = responseInputStream.read(byteContainer);
                if (i == -1) {
                    return stringBuffer;
                }
                stringBuffer.append(new String(byteContainer, 0, i));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }

    public static List<String> updateRetryData(String data, Context context) {
        setRetryData(data, context);
        return processAndAddWhiteListedUrls(data);
    }

    private static void setRetryData(String data, Context context) {
        if (data == null) {
            SharedPreferenceUtil.addStringToSharedPreference(context, CBConstant.SP_RETRY_FILE_NAME, CBConstant.SP_RETRY_WHITELISTED_URLS, "");
        } else {
            SharedPreferenceUtil.addStringToSharedPreference(context, CBConstant.SP_RETRY_FILE_NAME, CBConstant.SP_RETRY_WHITELISTED_URLS, data);
        }
        C0533L.m760v("#### PAYU", "DATA UPDATED IN SHARED PREFERENCES");
    }

    public void clearCookie() {
        CookieManager cookieManager = CookieManager.getInstance();
        if (VERSION.SDK_INT >= 21) {
            cookieManager.removeSessionCookies(null);
        } else {
            cookieManager.removeSessionCookie();
        }
    }

    public static List<String> processAndAddWhiteListedUrls(String data) {
        if (!(data == null || data.equalsIgnoreCase(""))) {
            String[] urls = data.split("\\|");
            for (String url : urls) {
                C0533L.m760v("#### PAYU", "Split Url: " + url);
            }
            if (urls != null && urls.length > 0) {
                return Arrays.asList(urls);
            }
            C0533L.m760v("#### PAYU", "Whitelisted URLs from JS: " + data);
        }
        return new ArrayList();
    }

    public boolean getBooleanSharedPreference(String key, Context context) {
        sharedPreferences = context.getSharedPreferences(CB_PREFERENCE, 0);
        return sharedPreferences.getBoolean(key, false);
    }

    public boolean getBooleanSharedPreferenceDefaultTrue(String key, Context context) {
        sharedPreferences = context.getSharedPreferences(CB_PREFERENCE, 0);
        return sharedPreferences.getBoolean(key, true);
    }

    public void setBooleanSharedPreference(String key, boolean value, Context context) {
        Editor editor = context.getSharedPreferences(CB_PREFERENCE, 0).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public String getDeviceDensity(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.densityDpi + "";
    }

    private void getDownloadSpeed() {
        String[] testing = new String[2];
        long BeforeTime = System.currentTimeMillis();
        long TotalTxBeforeTest = TrafficStats.getTotalTxBytes();
        long TotalRxBeforeTest = TrafficStats.getTotalRxBytes();
        long TotalTxAfterTest = TrafficStats.getTotalTxBytes();
        double TimeDifference = (double) (System.currentTimeMillis() - BeforeTime);
        double rxDiff = (double) (TrafficStats.getTotalRxBytes() - TotalRxBeforeTest);
        double txDiff = (double) (TotalTxAfterTest - TotalTxBeforeTest);
        if (rxDiff == 0.0d || txDiff == 0.0d) {
            testing[0] = "No uploaded or downloaded bytes.";
            return;
        }
        double txBPS = txDiff / (TimeDifference / 1000.0d);
        testing[0] = String.valueOf(rxDiff / (TimeDifference / 1000.0d)) + "bps. Total rx = " + rxDiff;
        testing[1] = String.valueOf(txBPS) + "bps. Total tx = " + txDiff;
    }

    public String getNetworkStatus(Context context) {
        if (context != null) {
            try {
                NetworkInfo info = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
                if (info == null || !info.isConnected()) {
                    return "Not connected";
                }
                if (info.getType() == 1) {
                    return "WIFI";
                }
                if (info.getType() == 0) {
                    switch (info.getSubtype()) {
                        case 1:
                            return "GPRS";
                        case 2:
                            return "EDGE";
                        case 3:
                        case 5:
                        case 6:
                        case 8:
                        case 9:
                        case 10:
                            return "HSPA";
                        case 4:
                            return "CDMA";
                        case 7:
                        case 11:
                            return "2G";
                        case 12:
                        case 14:
                        case 15:
                            return "3G";
                        case 13:
                            return "4G";
                        default:
                            return "?";
                    }
                }
            } catch (Exception e) {
                return "?";
            }
        }
        return "?";
    }

    public NetworkInfo getNetWorkInfo(Context mContext) {
        int i = 0;
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService("connectivity");
        NetworkInfo network = null;
        int length;
        if (VERSION.SDK_INT >= 21) {
            Network[] networks = connectivityManager.getAllNetworks();
            length = networks.length;
            while (i < length) {
                NetworkInfo networkInfo = connectivityManager.getNetworkInfo(networks[i]);
                if (networkInfo.getState().equals(State.CONNECTED)) {
                    network = networkInfo;
                }
                i++;
            }
        } else {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null) {
                length = info.length;
                while (i < length) {
                    NetworkInfo anInfo = info[i];
                    if (anInfo.getState() == State.CONNECTED) {
                        network = anInfo;
                    }
                    i++;
                }
            }
        }
        return network;
    }

    public int getNetworkStrength(Context mContext) {
        NetworkInfo network = getNetWorkInfo(mContext);
        if (network == null) {
            return 0;
        }
        if (network.getTypeName().equalsIgnoreCase("MOBILE")) {
            return getMobileStrength(mContext, network);
        }
        if (!network.getTypeName().equalsIgnoreCase(SdkConstants.IS_WIFI) || !hasPermission(mContext, "android.permission.ACCESS_WIFI_STATE")) {
            return 0;
        }
        try {
            WifiInfo connectionInfo = ((WifiManager) mContext.getSystemService(SdkConstants.IS_WIFI)).getConnectionInfo();
            if (connectionInfo != null) {
                return WifiManager.calculateSignalLevel(connectionInfo.getRssi(), 5);
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private boolean hasPermission(Context context, String permission) {
        return context.checkCallingOrSelfPermission(permission) == 0;
    }

    private int getMobileStrength(Context context, NetworkInfo networkInfo) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
            int i = 0;
            if (VERSION.SDK_INT < 18) {
                return 0;
            }
            for (CellInfo info : telephonyManager.getAllCellInfo()) {
                if (info.isRegistered()) {
                    if (info instanceof CellInfoGsm) {
                        i = ((CellInfoGsm) info).getCellSignalStrength().getDbm();
                    } else if (info instanceof CellInfoCdma) {
                        i = ((CellInfoCdma) info).getCellSignalStrength().getDbm();
                    } else if (info instanceof CellInfoLte) {
                        i = ((CellInfoLte) info).getCellSignalStrength().getDbm();
                    } else if (info instanceof CellInfoWcdma) {
                        i = ((CellInfoWcdma) info).getCellSignalStrength().getDbm();
                    }
                }
            }
            return i;
        } catch (Exception e) {
            return 0;
        }
    }

    public void setStringSharedPreferenceLastURL(Context context, String key, String url) {
        String str = getStringSharedPreference(context, key);
        if (str.equalsIgnoreCase("")) {
            str = url;
        } else if (str.contains(CBConstant.CB_DELIMITER)) {
            StringTokenizer st = new StringTokenizer(str, CBConstant.CB_DELIMITER);
            st.nextToken();
            str = st.nextToken() + CBConstant.CB_DELIMITER + url;
        } else {
            str = str + CBConstant.CB_DELIMITER + url;
        }
        storeInSharedPreferences(context, key, str);
    }

    public String getStringSharedPreference(Context context, String key) {
        return context.getSharedPreferences(CB_PREFERENCE, 0).getString(key, "");
    }

    public void setStringSharedPreference(Context context, String key, String value) {
        Editor sharedPreferencesEditor = context.getSharedPreferences(CB_PREFERENCE, 0).edit();
        sharedPreferencesEditor.putString(key, value);
        sharedPreferencesEditor.commit();
    }

    public void deleteSharedPrefKey(Context context, String key) {
        try {
            Editor sharedPreferencesEditor = context.getSharedPreferences(CB_PREFERENCE, 0).edit();
            sharedPreferencesEditor.remove(key);
            sharedPreferencesEditor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void storeInSharedPreferences(Context context, String key, String value) {
        Editor editor = context.getSharedPreferences(CB_PREFERENCE, 0).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void removeFromSharedPreferences(Context context, String key) {
        Editor editor = context.getSharedPreferences(CB_PREFERENCE, 0).edit();
        editor.remove(key);
        editor.apply();
    }

    public Drawable getDrawableCB(Context context, int resID) {
        if (VERSION.SDK_INT < 21) {
            return context.getResources().getDrawable(resID);
        }
        return context.getResources().getDrawable(resID, context.getTheme());
    }

    public void cancelTimer(Timer timer) {
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
    }

    public String readFileInputStream(Context mContext, String fileName, int contextMode) {
        String temp = "";
        try {
            if (!new File(mContext.getFilesDir(), fileName).exists()) {
                mContext.openFileOutput(fileName, contextMode);
            }
            FileInputStream fileInputStream = mContext.openFileInput(fileName);
            while (true) {
                int c = fileInputStream.read();
                if (c == -1) {
                    break;
                }
                temp = temp + Character.toString((char) c);
            }
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        return temp;
    }

    public void writeFileOutputStream(InputStream inputStream, Context context, String fileName, int contextMode) {
        try {
            GZIPInputStream responseInputStream = new GZIPInputStream(inputStream);
            byte[] buf = new byte[1024];
            FileOutputStream outputStream = context.openFileOutput(fileName, contextMode);
            while (true) {
                int len = responseInputStream.read(buf);
                if (len > 0) {
                    outputStream.write(buf, 0, len);
                } else {
                    responseInputStream.close();
                    outputStream.close();
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public void resetPayuID() {
        clearCookie();
    }

    public String getCookie(String cookieName, Context context) {
        String cookieValue = "";
        try {
            String siteName = "https://secure.payu.in";
            CookieManager cookieManager = CookieManager.getInstance();
            if (VERSION.SDK_INT < 21) {
                CookieSyncManager.createInstance(context);
                CookieSyncManager.getInstance().sync();
            }
            String cookies = cookieManager.getCookie(siteName);
            if (cookies != null) {
                for (String ar1 : cookies.split(";")) {
                    if (ar1.contains(cookieName)) {
                        cookieValue = ar1.split("=")[1];
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cookieValue;
    }

    @Deprecated
    public String getDataFromPostData(String postData, String key) {
        for (String item : postData.split("&")) {
            String[] items = item.split("=");
            if (items.length >= 2 && items[0].equalsIgnoreCase(key)) {
                return items[1];
            }
        }
        return "";
    }

    public HashMap<String, String> getDataFromPostData(String postData) {
        HashMap<String, String> postParamsMap = new HashMap();
        if (postData != null) {
            StringTokenizer tokens = new StringTokenizer(postData, "&");
            while (tokens.hasMoreTokens()) {
                String[] keyValue = tokens.nextToken().split("=");
                if (!(keyValue == null || keyValue.length <= 0 || keyValue[0] == null)) {
                    postParamsMap.put(keyValue[0], keyValue.length > 1 ? keyValue[1] : "");
                }
            }
        }
        return postParamsMap;
    }

    public void showNotification(Context context, Intent intent, String title, String txt, int smallIcon, boolean autoCancel, Style style, int color, int ID) {
        Builder mBuilder = new Builder(context);
        mBuilder.setContentTitle(title).setContentText(txt).setSmallIcon(smallIcon).setPriority(1).setDefaults(2);
        if (autoCancel) {
            mBuilder.setAutoCancel(autoCancel);
        }
        if (style != null) {
            mBuilder.setStyle(style);
        }
        if (color != -1) {
            mBuilder.setColor(color);
        }
        mBuilder.setContentIntent(PendingIntent.getActivity(context, 0, intent, 134217728));
        ((NotificationManager) context.getSystemService("notification")).notify(ID, mBuilder.build());
    }

    public SnoozeConfigMap storeSnoozeConfigInSharedPref(Context context, String snoozeConfig) {
        SnoozeConfigMap snoozeConfigMap = new SnoozeConfigMap();
        try {
            JSONObject snoozeConfigObject = new JSONObject(snoozeConfig);
            SharedPreferenceUtil.removeAllFromSharedPref(context, CBConstant.SNOOZE_SHARED_PREF);
            snoozeConfigMap = storeSnoozeConfigInSharedPref(context, snoozeConfigObject.getJSONArray(CBAnalyticsConstant.DEFAULT), snoozeConfigMap);
            snoozeConfigObject.remove(CBAnalyticsConstant.DEFAULT);
            Iterator<String> snoozeConfigIterator = snoozeConfigObject.keys();
            if (snoozeConfigIterator.hasNext()) {
                snoozeConfigMap = storeSnoozeConfigInSharedPref(context, snoozeConfigObject.getJSONArray((String) snoozeConfigIterator.next()), snoozeConfigMap);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return snoozeConfigMap;
    }

    private SnoozeConfigMap storeSnoozeConfigInSharedPref(Context context, JSONArray configArray, SnoozeConfigMap snoozeConfigMap) {
        String urlCollections = "";
        String progressPercent = "";
        String timeOut = "";
        String url = "";
        try {
            int snoozeDefaultArrayLength = configArray.length();
            for (int i = 0; i < snoozeDefaultArrayLength; i++) {
                JSONObject currentSnoozeConfigObject = configArray.getJSONObject(i);
                urlCollections = currentSnoozeConfigObject.get(CBConstant.URL).toString();
                progressPercent = currentSnoozeConfigObject.get(CBConstant.PROGRESS_PERCENT).toString();
                timeOut = currentSnoozeConfigObject.get(CBConstant.TIME_OUT).toString();
                StringTokenizer snoozeTokenizer = new StringTokenizer(urlCollections, CBConstant.CB_DELIMITER);
                while (snoozeTokenizer.hasMoreTokens()) {
                    url = snoozeTokenizer.nextToken();
                    SharedPreferenceUtil.addStringToSharedPreference(context, CBConstant.SNOOZE_SHARED_PREF, url.contentEquals(CBConstant.DEFAULT_PAYMENT_URLS) ? CBConstant.DEFAULT_PAYMENT_URLS : url.trim(), progressPercent.trim() + CBConstant.CB_DELIMITER + timeOut.trim());
                    snoozeConfigMap.put(url.contentEquals(CBConstant.DEFAULT_PAYMENT_URLS) ? CBConstant.DEFAULT_PAYMENT_URLS : url.trim(), progressPercent.trim() + CBConstant.CB_DELIMITER + timeOut.trim());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return snoozeConfigMap;
    }

    public SnoozeConfigMap convertToSnoozeConfigMap(Map<String, ?> snoozeMap) {
        SnoozeConfigMap snoozeConfigMap = new SnoozeConfigMap();
        for (Entry<String, ?> entry : snoozeMap.entrySet()) {
            snoozeConfigMap.put(entry.getKey(), entry.getValue());
        }
        return snoozeConfigMap;
    }
}
