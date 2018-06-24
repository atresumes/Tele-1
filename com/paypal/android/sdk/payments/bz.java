package com.paypal.android.sdk.payments;

import java.util.ArrayList;

final class bz extends ArrayList {
    bz(by byVar) {
        add(PayPalConfiguration.ENVIRONMENT_PRODUCTION);
        add(PayPalConfiguration.ENVIRONMENT_SANDBOX);
        add(PayPalConfiguration.ENVIRONMENT_NO_NETWORK);
    }
}
