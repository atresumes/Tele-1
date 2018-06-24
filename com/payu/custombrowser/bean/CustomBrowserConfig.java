package com.payu.custombrowser.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Size;
import com.payUMoney.sdk.SdkConstants;
import com.payu.custombrowser.Bank;
import com.payu.custombrowser.C0517R;
import com.payu.custombrowser.util.CBConstant;
import com.payu.custombrowser.util.CBUtil;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;

public class CustomBrowserConfig implements Parcelable {
    public static final Creator<CustomBrowserConfig> CREATOR = new C05251();
    public static final int FAIL_MODE = 2;
    public static final int STOREONECLICKHASH_MODE_NONE = 0;
    public static final int STOREONECLICKHASH_MODE_SERVER = 1;
    public static final int WARN_MODE = 1;
    private int autoApprove;
    private int autoSelectOTP;
    private int disableBackButtonDialog;
    private int enableSurePay;
    private int magicRetry = 1;
    private String merchantCheckoutActivityPath;
    private String merchantKey;
    private int merchantSMSPermission;
    private String payuPostData;
    private String postURL;
    private String sdkVersionName;
    private int showCustombrowser;
    private int storeOneClickHash;
    private int surePayBackgroundTTL;
    private int surePayMode;
    private String surePayNotificationGoodNetWorkBody;
    private String surePayNotificationGoodNetWorkHeader;
    private String surePayNotificationGoodNetworkTitle;
    private int surePayNotificationIcon;
    private String surePayNotificationPoorNetWorkBody;
    private String surePayNotificationPoorNetWorkHeader;
    private String surePayNotificationPoorNetWorkTitle;
    private String surePayNotificationTransactionNotVerifiedBody;
    private String surePayNotificationTransactionNotVerifiedHeader;
    private String surePayNotificationTransactionNotVerifiedTitle;
    private String surePayNotificationTransactionVerifiedBody;
    private String surePayNotificationTransactionVerifiedHeader;
    private String surePayNotificationTransactionVerifiedTitle;
    private String transactionID;
    private int viewPortWideEnable;

    static class C05251 implements Creator<CustomBrowserConfig> {
        C05251() {
        }

        public CustomBrowserConfig createFromParcel(Parcel in) {
            return new CustomBrowserConfig(in);
        }

        public CustomBrowserConfig[] newArray(int size) {
            return new CustomBrowserConfig[size];
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SnoozeMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface StoreOneClickHashMode {
    }

    public int getSurePayBackgroundTTL() {
        return this.surePayBackgroundTTL;
    }

    public void setSurePayBackgroundTTL(int surePayBackgroundTTL) {
        this.surePayBackgroundTTL = surePayBackgroundTTL;
    }

    private CustomBrowserConfig() {
    }

    protected CustomBrowserConfig(Parcel in) {
        this.viewPortWideEnable = in.readInt();
        this.autoApprove = in.readInt();
        this.autoSelectOTP = in.readInt();
        this.transactionID = in.readString();
        this.merchantKey = in.readString();
        this.sdkVersionName = in.readString();
        this.showCustombrowser = in.readInt();
        this.disableBackButtonDialog = in.readInt();
        this.storeOneClickHash = in.readInt();
        this.magicRetry = in.readInt();
        this.merchantSMSPermission = in.readInt();
        this.enableSurePay = in.readInt();
        this.merchantCheckoutActivityPath = in.readString();
        this.postURL = in.readString();
        this.payuPostData = in.readString();
        this.surePayNotificationIcon = in.readInt();
        this.surePayNotificationGoodNetworkTitle = in.readString();
        this.surePayNotificationGoodNetWorkHeader = in.readString();
        this.surePayNotificationGoodNetWorkBody = in.readString();
        this.surePayNotificationPoorNetWorkTitle = in.readString();
        this.surePayNotificationPoorNetWorkHeader = in.readString();
        this.surePayNotificationPoorNetWorkBody = in.readString();
        this.surePayNotificationTransactionVerifiedTitle = in.readString();
        this.surePayNotificationTransactionVerifiedHeader = in.readString();
        this.surePayNotificationTransactionVerifiedBody = in.readString();
        this.surePayNotificationTransactionNotVerifiedTitle = in.readString();
        this.surePayNotificationTransactionNotVerifiedHeader = in.readString();
        this.surePayNotificationTransactionNotVerifiedBody = in.readString();
        this.surePayMode = in.readInt();
        this.surePayBackgroundTTL = in.readInt();
    }

    public CustomBrowserConfig(@Size(max = 6, min = 6) @NonNull String merchantKey, @NonNull String transactionID) {
        this.transactionID = transactionID;
        this.merchantKey = merchantKey;
        this.surePayNotificationIcon = C0517R.drawable.surepay_logo;
        this.surePayNotificationGoodNetworkTitle = "Internet Restored";
        this.surePayNotificationGoodNetWorkHeader = "You can now resume the transaction";
        this.surePayNotificationPoorNetWorkTitle = "No Internet Found";
        this.surePayNotificationPoorNetWorkHeader = "We could not detect internet on your device";
        this.surePayNotificationTransactionVerifiedTitle = "Transaction Verified";
        this.surePayNotificationTransactionVerifiedHeader = "The bank has verified this transaction and we are good to go.";
        this.surePayNotificationTransactionNotVerifiedTitle = "Transaction Status Unknown";
        this.surePayNotificationTransactionNotVerifiedHeader = "The bank could not verify the transaction at this time.";
        this.enableSurePay = 0;
        this.surePayMode = 1;
        this.surePayBackgroundTTL = CBConstant.DEFAULT_SURE_PAY_TTL;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.viewPortWideEnable);
        dest.writeInt(this.autoApprove);
        dest.writeInt(this.autoSelectOTP);
        dest.writeString(this.transactionID);
        dest.writeString(this.merchantKey);
        dest.writeString(this.sdkVersionName);
        dest.writeInt(this.showCustombrowser);
        dest.writeInt(this.disableBackButtonDialog);
        dest.writeInt(this.storeOneClickHash);
        dest.writeInt(this.magicRetry);
        dest.writeInt(this.merchantSMSPermission);
        dest.writeInt(this.enableSurePay);
        dest.writeString(this.merchantCheckoutActivityPath);
        dest.writeString(this.postURL);
        dest.writeString(this.payuPostData);
        dest.writeInt(this.surePayNotificationIcon);
        dest.writeString(this.surePayNotificationGoodNetworkTitle);
        dest.writeString(this.surePayNotificationGoodNetWorkHeader);
        dest.writeString(this.surePayNotificationGoodNetWorkBody);
        dest.writeString(this.surePayNotificationPoorNetWorkTitle);
        dest.writeString(this.surePayNotificationPoorNetWorkHeader);
        dest.writeString(this.surePayNotificationPoorNetWorkBody);
        dest.writeString(this.surePayNotificationTransactionVerifiedTitle);
        dest.writeString(this.surePayNotificationTransactionVerifiedHeader);
        dest.writeString(this.surePayNotificationTransactionVerifiedBody);
        dest.writeString(this.surePayNotificationTransactionNotVerifiedTitle);
        dest.writeString(this.surePayNotificationTransactionNotVerifiedHeader);
        dest.writeString(this.surePayNotificationTransactionNotVerifiedBody);
        dest.writeInt(this.surePayMode);
        dest.writeInt(this.surePayBackgroundTTL);
    }

    public int describeContents() {
        return 0;
    }

    public String getPostURL() {
        return this.postURL;
    }

    public void setPostURL(String postURL) {
        this.postURL = postURL;
    }

    public String getPayuPostData() {
        return this.payuPostData;
    }

    public void setPayuPostData(String payuPostData) {
        this.payuPostData = payuPostData;
        HashMap<String, String> postParams = new CBUtil().getDataFromPostData(payuPostData);
        String defaultText = "Product info: " + ((String) postParams.get(SdkConstants.PRODUCT_INFO_STRING)) + "\nAmount: " + ((String) postParams.get(SdkConstants.AMOUNT));
        if (this.surePayNotificationGoodNetWorkBody == null) {
            setSurePayNotificationGoodNetWorkBody(defaultText);
        }
        if (this.surePayNotificationPoorNetWorkBody == null) {
            setSurePayNotificationPoorNetWorkBody(defaultText);
        }
        if (this.surePayNotificationTransactionVerifiedBody == null) {
            setSurePayNotificationTransactionVerifiedBody(defaultText);
        }
        if (this.surePayNotificationTransactionNotVerifiedBody == null) {
            setSurePayNotificationTransactionNotVerifiedBody(defaultText);
        }
        if (postParams.get("key") != null) {
            setMerchantKey(Bank.keyAnalytics == null ? (String) postParams.get("key") : Bank.keyAnalytics);
        }
    }

    public int getEnableSurePay() {
        return this.enableSurePay;
    }

    public void setEnableSurePay(@IntRange(from = 0, to = 3) int enableSurePay) {
        if (enableSurePay > 3) {
            enableSurePay = 3;
        }
        this.enableSurePay = enableSurePay;
        this.enableSurePay = 0;
    }

    public int getMerchantSMSPermission() {
        return this.merchantSMSPermission;
    }

    public void setMerchantSMSPermission(boolean merchantSMSPermission) {
        this.merchantSMSPermission = merchantSMSPermission ? 1 : 0;
    }

    public int getMagicretry() {
        return this.magicRetry;
    }

    public void setmagicRetry(boolean magicRetry) {
        this.magicRetry = magicRetry ? 1 : 0;
    }

    public int getStoreOneClickHash() {
        return this.storeOneClickHash;
    }

    public void setStoreOneClickHash(int storeOneClickHash) {
        this.storeOneClickHash = storeOneClickHash;
    }

    public String getMerchantCheckoutActivityPath() {
        return this.merchantCheckoutActivityPath;
    }

    public void setMerchantCheckoutActivityPath(String merchantCheckoutActivityPath) {
        this.merchantCheckoutActivityPath = merchantCheckoutActivityPath;
    }

    public int getDisableBackButtonDialog() {
        return this.disableBackButtonDialog;
    }

    public void setDisableBackButtonDialog(boolean disableBackButtonDialog) {
        this.disableBackButtonDialog = disableBackButtonDialog ? 1 : 0;
    }

    public int getViewPortWideEnable() {
        return this.viewPortWideEnable;
    }

    public void setViewPortWideEnable(boolean viewPortWideEnable) {
        this.viewPortWideEnable = viewPortWideEnable ? 1 : 0;
    }

    public int getAutoApprove() {
        return this.autoApprove;
    }

    public void setAutoApprove(boolean autoApprove) {
        this.autoApprove = autoApprove ? 1 : 0;
    }

    public String getTransactionID() {
        return this.transactionID;
    }

    public int getAutoSelectOTP() {
        return this.autoSelectOTP;
    }

    public void setAutoSelectOTP(boolean autoSelectOTP) {
        this.autoSelectOTP = autoSelectOTP ? 1 : 0;
    }

    public String getMerchantKey() {
        return this.merchantKey;
    }

    public void setMerchantKey(String merchantKey) {
        this.merchantKey = Bank.keyAnalytics;
        if (this.merchantKey == null || this.merchantKey.trim().length() < 1) {
            this.merchantKey = merchantKey;
            Bank.keyAnalytics = merchantKey;
        }
    }

    public String getSdkVersionName() {
        return this.sdkVersionName;
    }

    public void setSdkVersionName(String sdkVersionName) {
        this.sdkVersionName = sdkVersionName;
    }

    public int getShowCustombrowser() {
        return this.showCustombrowser;
    }

    public void setShowCustombrowser(boolean showCustombrowser) {
        this.showCustombrowser = showCustombrowser ? 1 : 0;
    }

    public String getSurePayNotificationGoodNetworkTitle() {
        return this.surePayNotificationGoodNetworkTitle;
    }

    public void setSurePayNotificationGoodNetworkTitle(String surePayNotificationGoodNetworkTitle) {
        this.surePayNotificationGoodNetworkTitle = surePayNotificationGoodNetworkTitle;
    }

    public String getSurePayNotificationGoodNetWorkHeader() {
        return this.surePayNotificationGoodNetWorkHeader;
    }

    public void setSurePayNotificationGoodNetWorkHeader(String surePayNotificationGoodNetWorkHeader) {
        this.surePayNotificationGoodNetWorkHeader = surePayNotificationGoodNetWorkHeader;
    }

    public String getSurePayNotificationGoodNetWorkBody() {
        return this.surePayNotificationGoodNetWorkBody;
    }

    public void setSurePayNotificationGoodNetWorkBody(String surePayNotificationGoodNetWorkBody) {
        this.surePayNotificationGoodNetWorkBody = surePayNotificationGoodNetWorkBody;
    }

    public String getSurePayNotificationPoorNetWorkTitle() {
        return this.surePayNotificationPoorNetWorkTitle;
    }

    public void setSurePayNotificationPoorNetWorkTitle(String surePayNotificationPoorNetWorkTitle) {
        this.surePayNotificationPoorNetWorkTitle = surePayNotificationPoorNetWorkTitle;
    }

    public String getSurePayNotificationPoorNetWorkHeader() {
        return this.surePayNotificationPoorNetWorkHeader;
    }

    public void setSurePayNotificationPoorNetWorkHeader(String surePayNotificationPoorNetWorkHeader) {
        this.surePayNotificationPoorNetWorkHeader = surePayNotificationPoorNetWorkHeader;
    }

    public String getSurePayNotificationPoorNetWorkBody() {
        return this.surePayNotificationPoorNetWorkBody;
    }

    public void setSurePayNotificationPoorNetWorkBody(String surePayNotificationPoorNetWorkBody) {
        this.surePayNotificationPoorNetWorkBody = surePayNotificationPoorNetWorkBody;
    }

    public String getSurePayNotificationTransactionVerifiedTitle() {
        return this.surePayNotificationTransactionVerifiedTitle;
    }

    public void setSurePayNotificationTransactionVerifiedTitle(String surePayNotificationTransactionVerifiedTitle) {
        this.surePayNotificationTransactionVerifiedTitle = surePayNotificationTransactionVerifiedTitle;
    }

    public String getSurePayNotificationTransactionVerifiedHeader() {
        return this.surePayNotificationTransactionVerifiedHeader;
    }

    public void setSurePayNotificationTransactionVerifiedHeader(String surePayNotificationTransactionVerifiedHeader) {
        this.surePayNotificationTransactionVerifiedHeader = surePayNotificationTransactionVerifiedHeader;
    }

    public String getSurePayNotificationTransactionVerifiedBody() {
        return this.surePayNotificationTransactionVerifiedBody;
    }

    public void setSurePayNotificationTransactionVerifiedBody(String surePayNotificationTransactionVerifiedBody) {
        this.surePayNotificationTransactionVerifiedBody = surePayNotificationTransactionVerifiedBody;
    }

    public String getSurePayNotificationTransactionNotVerifiedTitle() {
        return this.surePayNotificationTransactionNotVerifiedTitle;
    }

    public void setSurePayNotificationTransactionNotVerifiedTitle(String surePayNotificationTransactionNotVerifiedTitle) {
        this.surePayNotificationTransactionNotVerifiedTitle = surePayNotificationTransactionNotVerifiedTitle;
    }

    public String getSurePayNotificationTransactionNotVerifiedHeader() {
        return this.surePayNotificationTransactionNotVerifiedHeader;
    }

    public void setSurePayNotificationTransactionNotVerifiedHeader(String surePayNotificationTransactionNotVerifiedHeader) {
        this.surePayNotificationTransactionNotVerifiedHeader = surePayNotificationTransactionNotVerifiedHeader;
    }

    public String getSurePayNotificationTransactionNotVerifiedBody() {
        return this.surePayNotificationTransactionNotVerifiedBody;
    }

    public void setSurePayNotificationTransactionNotVerifiedBody(String surePayNotificationTransactionNotVerifiedBody) {
        this.surePayNotificationTransactionNotVerifiedBody = surePayNotificationTransactionNotVerifiedBody;
    }

    public int getSurePayNotificationIcon() {
        return this.surePayNotificationIcon;
    }

    public void setSurePayNotificationIcon(int surePayNotificationIcon) {
        this.surePayNotificationIcon = surePayNotificationIcon;
    }

    public int getSurePayMode() {
        return this.surePayMode;
    }

    public void setSurePayMode(int surePayMode) {
        this.surePayMode = surePayMode;
    }
}
