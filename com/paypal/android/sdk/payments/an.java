package com.paypal.android.sdk.payments;

import android.app.AlertDialog.Builder;
import android.view.View;
import android.view.View.OnClickListener;
import com.paypal.android.sdk.fu;
import com.paypal.android.sdk.fw;
import com.paypal.android.sdk.gh;
import java.util.List;

final class an implements OnClickListener {
    final /* synthetic */ gh f772a;
    final /* synthetic */ List f773b;
    final /* synthetic */ LoginActivity f774c;

    an(LoginActivity loginActivity, gh ghVar, List list) {
        this.f774c = loginActivity;
        this.f772a = ghVar;
        this.f773b = list;
    }

    public final void onClick(View view) {
        Builder builder = new Builder(view.getContext());
        builder.setTitle(fu.m369a(fw.TWO_FACTOR_AUTH_ENTER_MOBILE_NUMBER)).setAdapter(this.f772a, new ao(this));
        builder.create().show();
    }
}
