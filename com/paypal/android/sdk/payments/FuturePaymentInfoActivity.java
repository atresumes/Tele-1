package com.paypal.android.sdk.payments;

import android.app.Activity;
import android.os.Bundle;
import com.paypal.android.sdk.fu;
import com.paypal.android.sdk.fw;

public final class FuturePaymentInfoActivity extends Activity {
    private ah f594a;

    protected final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ag agVar = (ag) getIntent().getExtras().getSerializable("com.paypal.details.scope");
        setTheme(16973934);
        requestWindowFeature(8);
        this.f594a = new ah(this, agVar);
        setContentView(this.f594a.f763a);
        C0905d.m971a((Activity) this, this.f594a.f764b, null);
        this.f594a.f765c.setText(fu.m369a(fw.BACK_BUTTON));
        this.f594a.f765c.setOnClickListener(new af(this));
    }
}
