package com.paypal.android.sdk;

import android.content.Intent;
import android.os.Bundle;

public class dq extends an {
    private static final String f1112a = dq.class.getSimpleName();

    public final Intent m1060a(String str, dr drVar, ds dsVar, String str2) {
        Intent a = an.m812a("com.paypal.android.p2pmobile.Sdk", "com.paypal.android.lib.authenticator.activity.SdkActivity");
        Bundle bundle = new Bundle();
        bundle.putString("target_client_id", str);
        if (drVar != null) {
            bundle.putString("token_request_type", drVar.toString());
        }
        if (dsVar != null) {
            bundle.putString("response_type", dsVar.toString());
        }
        bundle.putString("app_guid", str2);
        new StringBuilder("launching authenticator with bundle:").append(bundle);
        a.putExtras(bundle);
        return a;
    }
}
