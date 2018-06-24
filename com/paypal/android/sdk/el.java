package com.paypal.android.sdk;

import java.util.Comparator;
import java.util.Currency;

final class el implements Comparator {
    el() {
    }

    public final /* synthetic */ int compare(Object obj, Object obj2) {
        return ((Currency) obj).getCurrencyCode().compareTo(((Currency) obj2).getCurrencyCode());
    }
}
