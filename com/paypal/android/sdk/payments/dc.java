package com.paypal.android.sdk.payments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import com.paypal.android.sdk.fg;
import com.paypal.android.sdk.fu;
import com.paypal.android.sdk.fw;

final class dc implements cc {
    private /* synthetic */ PaymentConfirmActivity f1061a;

    dc(PaymentConfirmActivity paymentConfirmActivity) {
        this.f1061a = paymentConfirmActivity;
    }

    public final void mo2182a(cf cfVar) {
        this.f1061a.f706k.m575c().m279a();
        this.f1061a.m641j();
        if (!cfVar.m681a() || "UNAUTHORIZED_PAYMENT".equals(cfVar.f820b)) {
            switch (cu.f830a[this.f1061a.f704i.ordinal()]) {
                case 1:
                    this.f1061a.f701f = false;
                    Bundle bundle = new Bundle();
                    bundle.putString("BUNDLE_ERROR_CODE", cfVar.f820b);
                    this.f1061a.showDialog(5, bundle);
                    return;
                case 2:
                case 3:
                    this.f1061a.f702g.m408b(true);
                    C0905d.m973a(this.f1061a, fu.m370a(cfVar.f820b), 1);
                    return;
                default:
                    return;
            }
        }
        switch (cu.f830a[this.f1061a.f704i.ordinal()]) {
            case 1:
                C0905d.m973a(this.f1061a, fu.m369a(fw.SESSION_EXPIRED_MESSAGE), 4);
                return;
            case 2:
            case 3:
                this.f1061a.showDialog(2);
                PaymentConfirmActivity.f696a;
                new StringBuilder("server thinks token is expired, get new one. AccessToken: ").append(this.f1061a.f706k.m575c().f295b);
                this.f1061a.f706k.m567a(this.f1061a.m637h(), true);
                return;
            default:
                return;
        }
    }

    public final void mo2183a(Object obj) {
        if (obj instanceof ProofOfPayment) {
            Parcelable paymentConfirmation = new PaymentConfirmation(this.f1061a.f706k.m577e(), this.f1061a.f703h.m692a(), (ProofOfPayment) obj);
            Intent intent = this.f1061a.getIntent();
            intent.putExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION, paymentConfirmation);
            this.f1061a.m628c();
            this.f1061a.setResult(-1, intent);
            this.f1061a.finish();
        } else if (obj instanceof fg) {
            this.f1061a.f701f = false;
            PaymentConfirmActivity.m616a(this.f1061a, (fg) obj);
        }
    }
}
