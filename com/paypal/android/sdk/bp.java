package com.paypal.android.sdk;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import java.util.Arrays;

public final class bp {
    static {
        Arrays.asList(new String[]{PayPalConfiguration.ENVIRONMENT_PRODUCTION, PayPalConfiguration.ENVIRONMENT_SANDBOX, PayPalConfiguration.ENVIRONMENT_NO_NETWORK});
    }

    public static boolean m160a(String str) {
        return str.equals(PayPalConfiguration.ENVIRONMENT_NO_NETWORK);
    }

    public static boolean m161b(String str) {
        return str.startsWith(PayPalConfiguration.ENVIRONMENT_SANDBOX);
    }

    public static boolean m162c(String str) {
        return str.equals(PayPalConfiguration.ENVIRONMENT_PRODUCTION);
    }

    public static boolean m163d(String str) {
        return (str.equals(PayPalConfiguration.ENVIRONMENT_PRODUCTION) || str.startsWith(PayPalConfiguration.ENVIRONMENT_SANDBOX) || str.equals(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)) ? false : true;
    }

    public static boolean m164e(String str) {
        return str.startsWith(PayPalConfiguration.ENVIRONMENT_SANDBOX) || str.equals(PayPalConfiguration.ENVIRONMENT_NO_NETWORK);
    }
}
