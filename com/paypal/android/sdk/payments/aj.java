package com.paypal.android.sdk.payments;

import android.text.Editable;
import android.text.TextWatcher;

final class aj implements TextWatcher {
    private /* synthetic */ LoginActivity f770a;

    aj(LoginActivity loginActivity) {
        this.f770a = loginActivity;
    }

    public final void afterTextChanged(Editable editable) {
        this.f770a.m469h();
    }

    public final void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    public final void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }
}
