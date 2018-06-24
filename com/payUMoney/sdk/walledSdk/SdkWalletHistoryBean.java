package com.payUMoney.sdk.walledSdk;

import java.util.Date;

public class SdkWalletHistoryBean {
    private String mAmount;
    private String mDescription;
    private String mExternalRefId;
    private String mMerchantId;
    private String mMerchantName;
    private String mMerchantTransactionId;
    private String mMode;
    private String mPaymentId;
    private String mPaymentType;
    private String mRefundToSource;
    private Date mTransactionDate;
    private String mTransactionStatus;
    private String mVaultAction;
    private String mVaultActionMessage;
    private String mVaultTransactionId;

    public String getVaultActionMessage() {
        return this.mVaultActionMessage;
    }

    public void setVaultActionMessage(String transactionStatus) {
        this.mVaultActionMessage = transactionStatus;
    }

    public String getDescription() {
        return this.mDescription;
    }

    public void setDescription(String transactionStatus) {
        this.mDescription = transactionStatus;
    }

    public String getTransactionStatus() {
        return this.mTransactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.mTransactionStatus = transactionStatus;
    }

    public String getPaymentType() {
        return this.mPaymentType;
    }

    public void setPaymentType(String name) {
        this.mPaymentType = name;
    }

    public String getExternalRefId() {
        return this.mExternalRefId;
    }

    public void setExternalRefId(String image) {
        this.mExternalRefId = image;
    }

    public String getVaultAction() {
        return this.mVaultAction;
    }

    public void setVaultAction(String type) {
        this.mVaultAction = type;
    }

    public String getVaultTransactionId() {
        return this.mVaultTransactionId;
    }

    public void setVaultTransactionId(String rate) {
        this.mVaultTransactionId = rate;
    }

    public String getRefundToSource() {
        return this.mRefundToSource;
    }

    public void setRefundToSource(String name) {
        this.mRefundToSource = name;
    }

    public String getAmount() {
        return this.mAmount;
    }

    public void setAmount(String image) {
        this.mAmount = image;
    }

    public String getMerchantName() {
        return this.mMerchantName;
    }

    public void setMerchantName(String type) {
        this.mMerchantName = type;
    }

    public String getMerchantTransactionId() {
        return this.mMerchantTransactionId;
    }

    public void setMerchantTransactionId(String rate) {
        this.mMerchantTransactionId = rate;
    }

    public String getMerchantId() {
        return this.mMerchantId;
    }

    public void setMerchantId(String name) {
        this.mMerchantId = name;
    }

    public String getPaymentId() {
        return this.mPaymentId;
    }

    public void setPaymentId(String image) {
        this.mPaymentId = image;
    }

    public Date getTransactionDate() {
        return this.mTransactionDate;
    }

    public void setTransactionDate(long type) {
        this.mTransactionDate = new Date(type);
    }

    public String getMode() {
        return this.mMode;
    }

    public void setMode(String rate) {
        this.mMode = rate;
    }
}
