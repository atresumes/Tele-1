package com.paypal.android.sdk;

import com.google.firebase.analytics.FirebaseAnalytics.Event;
import com.payUMoney.sdk.SdkConstants;

public enum fc {
    PreConnect("preconnect", "", false, false),
    DeviceCheck("devicecheck", "", false, false),
    PaymentMethodWindow("selectpaymentmethod", "", false, false),
    PaymentMethodCancel("selectpaymentmethod", SdkConstants.CANCEL_STRING, false, true),
    SelectPayPalPayment("selectpaymentmethod", "paypal", false, true),
    SelectCreditCardPayment("selectpaymentmethod", "card", false, true),
    ConfirmPaymentWindow("confirmpayment", "", false, false),
    ConfirmPayment("confirmpayment", "confirm", false, false),
    ConfirmPaymentCancel("confirmpayment", SdkConstants.CANCEL_STRING, false, true),
    PaymentSuccessful("paymentsuccessful", "", false, false),
    LoginWindow(Event.LOGIN, SdkConstants.PASSWORD, true, false),
    LoginPassword(Event.LOGIN, SdkConstants.PASSWORD, true, true),
    LoginPIN(Event.LOGIN, "PIN", true, true),
    SignUp(Event.LOGIN, SdkConstants.PASSWORD, true, true),
    LoginForgotPassword(Event.LOGIN, SdkConstants.PASSWORD, true, true),
    LoginCancel(Event.LOGIN, SdkConstants.CANCEL_STRING, true, true),
    ConsentWindow("authorizationconsent", "", false, false),
    ConsentAgree("authorizationconsent", "agree", false, true),
    ConsentCancel("authorizationconsent", SdkConstants.CANCEL_STRING, false, true),
    ConsentMerchantUrl("authorizationconsent", "merchanturl", false, true),
    ConsentPayPalPrivacyUrl("authorizationconsent", "privacy", false, true),
    AuthorizationSuccessful("authorizationsuccessful", "", false, false),
    LegalTextWindow("legaltext", "", false, false);
    
    private boolean f410A;
    private String f411x;
    private String f412y;
    private boolean f413z;

    private fc(String str, String str2, boolean z, boolean z2) {
        this.f411x = str;
        this.f412y = str2;
        this.f413z = z;
        this.f410A = z2;
    }

    public final String m365a() {
        return this.f411x + "::" + this.f412y;
    }

    public final String m366a(String str, boolean z) {
        String str2 = this.f413z ? z ? "returnuser" : "newuser" : "";
        return fb.f382a + ":" + str + ":" + str2;
    }

    public final boolean m367b() {
        return this.f410A;
    }
}
