package com.paypal.android.sdk.payments;

import android.text.Editable;
import android.text.TextWatcher;

final class bd implements TextWatcher {
    private /* synthetic */ LoginActivity f794a;

    bd(LoginActivity loginActivity) {
        this.f794a = loginActivity;
    }

    public final void afterTextChanged(Editable editable) {
        this.f794a.m471i();
    }

    public final void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    public final void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }
}
