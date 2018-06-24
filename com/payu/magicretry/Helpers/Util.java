package com.payu.magicretry.Helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Util {
    public static final int HIDE_FRAGMENT = 0;
    public static final int SHOW_FRAGMENT = 1;

    public static boolean isNetworkAvailable(Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        for (NetworkInfo ni : ((ConnectivityManager) context.getSystemService("connectivity")).getAllNetworkInfo()) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI") && ni.isConnected()) {
                haveConnectedWifi = true;
            }
            if (ni.getTypeName().equalsIgnoreCase("MOBILE") && ni.isConnected()) {
                haveConnectedMobile = true;
            }
        }
        if (haveConnectedWifi || haveConnectedMobile) {
            return true;
        }
        return false;
    }

    public static boolean isWifiConnected(Context context) {
        boolean isWifiConnected = false;
        for (NetworkInfo ni : ((ConnectivityManager) context.getSystemService("connectivity")).getAllNetworkInfo()) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI") && ni.isConnected()) {
                isWifiConnected = true;
            }
        }
        return isWifiConnected;
    }

    private boolean isMobileDataConnected(Context context) {
        boolean isMobileDataConnected = false;
        for (NetworkInfo ni : ((ConnectivityManager) context.getSystemService("connectivity")).getAllNetworkInfo()) {
            if (ni.getTypeName().equalsIgnoreCase("MOBILE") && ni.isConnected()) {
                isMobileDataConnected = true;
            }
        }
        return isMobileDataConnected;
    }

    public static void showNetworkNotAvailableError(Context context) {
    }
}
