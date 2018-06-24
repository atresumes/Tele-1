package com.paypal.android.sdk;

public enum df {
    FptiRequest(cb.POST, null),
    PreAuthRequest(cb.POST, "oauth2/token"),
    LoginRequest(cb.POST, "oauth2/login"),
    LoginChallengeRequest(cb.POST, "oauth2/login/challenge"),
    ConsentRequest(cb.POST, "oauth2/consent"),
    CreditCardPaymentRequest(cb.POST, "payments/payment"),
    PayPalPaymentRequest(cb.POST, "payments/payment"),
    CreateSfoPaymentRequest(cb.POST, "orchestration/msdk-create-sfo-payment"),
    ApproveAndExecuteSfoPaymentRequest(cb.POST, "orchestration/msdk-approve-and-execute-sfo-payment"),
    TokenizeCreditCardRequest(cb.POST, "vault/credit-card"),
    DeleteCreditCardRequest(cb.DELETE, "vault/credit-card"),
    GetAppInfoRequest(cb.GET, "apis/applications");
    
    private cb f292m;
    private String f293n;

    private df(cb cbVar, String str) {
        this.f292m = cbVar;
        this.f293n = str;
    }

    final cb m277a() {
        return this.f292m;
    }

    final String m278b() {
        return this.f293n;
    }
}
