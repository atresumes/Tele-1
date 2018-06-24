package com.paypal.android.sdk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import java.util.List;

public class an extends aj {
    private static final String f961a = an.class.getSimpleName();

    protected static Intent m812a(String str, String str2) {
        Intent intent = new Intent(str);
        intent.setComponent(ComponentName.unflattenFromString(str2));
        intent.setPackage("com.paypal.android.p2pmobile");
        return intent;
    }

    public final boolean m813a(Context context, String str, String str2) {
        boolean z = false;
        List queryIntentActivities = context.getPackageManager().queryIntentActivities(m812a(str, str2), 0);
        if (queryIntentActivities != null && queryIntentActivities.size() > 0) {
            z = true;
        }
        if (!z) {
            new StringBuilder("PayPal wallet app will not accept intent to: [action:").append(str).append(", class:").append(str2).append("]");
        }
        return z;
    }

    public final boolean m814a(Context context, boolean z) {
        return m38a(context, z, "com.paypal.android.p2pmobile", "O=Paypal", "O=Paypal", 34172764);
    }
}
