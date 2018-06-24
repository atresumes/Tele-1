package com.paypal.android.sdk;

import android.util.Log;

public abstract class ci implements ct {
    private static final String f990a = ci.class.getSimpleName();

    protected static void m832a(cw cwVar) {
        try {
            new StringBuilder("parsing success response\n:").append(cwVar.m201g());
            cwVar.mo2932c();
        } catch (Exception e) {
            Log.e("paypal.sdk", "Exception parsing server response", e);
            cwVar.m188a(new ca(bz.PARSE_RESPONSE_ERROR, e));
        }
    }

    protected static void m833a(cw cwVar, int i) {
        cwVar.m189a(Integer.valueOf(i));
        try {
            new StringBuilder("parsing error response:\n").append(cwVar.m201g());
            cwVar.mo2933d();
        } catch (Throwable e) {
            Log.e("paypal.sdk", "Exception parsing server response", e);
            cwVar.m192a(bz.INTERNAL_SERVER_ERROR.toString(), i + " http response received.  Response not parsable: " + e.getMessage(), null);
        }
    }
}
