package com.payu.custombrowser.util;

import android.text.TextUtils;
import java.util.HashMap;

public class SnoozeConfigMap extends HashMap {
    public Object get(Object key) {
        return super.get(key);
    }

    public int[] getPercentageAndTimeout(String url) {
        if (get(url) == null) {
            url = CBConstant.DEFAULT_PAYMENT_URLS;
        }
        String[] percentageAndTimeout = get(url) == null ? new String[0] : get(url).toString().split("\\|\\|");
        int[] iArr = new int[2];
        int parseInt = (percentageAndTimeout.length <= 0 || percentageAndTimeout[0].length() <= 0 || !TextUtils.isDigitsOnly(percentageAndTimeout[0])) ? 20 : Integer.parseInt(percentageAndTimeout[0]);
        iArr[0] = parseInt;
        if (percentageAndTimeout.length <= 1 || percentageAndTimeout[1].length() <= 0 || !TextUtils.isDigitsOnly(percentageAndTimeout[1])) {
            parseInt = 5000;
        } else {
            parseInt = Integer.parseInt(percentageAndTimeout[1]);
        }
        iArr[1] = parseInt;
        return iArr;
    }
}
