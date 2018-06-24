package com.payu.custombrowser;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import com.payu.custombrowser.bean.CustomBrowserConfig;
import com.payu.custombrowser.bean.CustomBrowserData;
import com.payu.custombrowser.util.CBConstant;

public class CustomBrowser {
    public void addCustomBrowser(Activity activity, @NonNull CustomBrowserConfig cbCustomBrowserConfig, @NonNull PayUCustomBrowserCallback cbPayUCustomBrowserCallback) {
        CustomBrowserData.SINGLETON.setPayuCustomBrowserCallback(cbPayUCustomBrowserCallback);
        if (cbCustomBrowserConfig.getPayuPostData() != null && cbCustomBrowserConfig.getEnableSurePay() > 0 && (cbCustomBrowserConfig.getPostURL().contentEquals("https://secure.payu.in/_payment") || cbCustomBrowserConfig.getPostURL().contentEquals("https://mobiletest.payu.in/_payment") || cbCustomBrowserConfig.getPostURL().contentEquals(CBConstant.MOBILE_TEST_PAYMENT_URL_SEAMLESS) || cbCustomBrowserConfig.getPostURL().contentEquals(CBConstant.PRODUCTION_PAYMENT_URL_SEAMLESS))) {
            if (cbCustomBrowserConfig.getPayuPostData().trim().endsWith("&")) {
                cbCustomBrowserConfig.setPayuPostData(cbCustomBrowserConfig.getPayuPostData().substring(0, cbCustomBrowserConfig.getPayuPostData().length() - 1));
            }
            cbCustomBrowserConfig.setPayuPostData(cbCustomBrowserConfig.getPayuPostData() + "&snooze=" + cbCustomBrowserConfig.getEnableSurePay());
        }
        Intent intent = new Intent(activity, CBActivity.class);
        intent.putExtra(CBConstant.CB_CONFIG, cbCustomBrowserConfig);
        activity.startActivity(intent);
    }
}
