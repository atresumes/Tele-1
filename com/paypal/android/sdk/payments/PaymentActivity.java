package com.paypal.android.sdk.payments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.util.Log;
import com.paypal.android.sdk.fu;
import com.paypal.android.sdk.fw;
import com.paypal.android.sdk.gn;
import com.paypal.android.sdk.gq;
import com.paypal.android.sdk.gr;
import com.paypal.android.sdk.gs;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

public final class PaymentActivity extends Activity {
    public static final String EXTRA_PAYMENT = "com.paypal.android.sdk.payment";
    public static final String EXTRA_RESULT_CONFIRMATION = "com.paypal.android.sdk.paymentConfirmation";
    public static final int RESULT_EXTRAS_INVALID = 2;
    private static final String f690a = PaymentActivity.class.getSimpleName();
    private Timer f691b;
    private Date f692c;
    private PayPalService f693d;
    private final ServiceConnection f694e = new ck(this);
    private boolean f695f;

    private void m602b() {
        PaymentMethodActivity.m646a(this, 1, this.f693d.m576d());
    }

    private ce m603c() {
        return new cm(this);
    }

    static /* synthetic */ void m604c(PaymentActivity paymentActivity) {
        if (paymentActivity.f693d.m576d() == null) {
            Log.e(f690a, "Service state invalid.  Did you start the PayPalService?");
            paymentActivity.setResult(2);
            paymentActivity.finish();
            return;
        }
        co coVar = new co(paymentActivity.getIntent(), paymentActivity.f693d.m576d());
        if (!coVar.m751b()) {
            Log.e(f690a, "Service extras invalid.  Please see the docs.");
            paymentActivity.setResult(2);
            paymentActivity.finish();
        } else if (coVar.mo2188c()) {
            paymentActivity.f693d.m584l();
            paymentActivity.f693d.m575c().m279a();
            if (paymentActivity.f693d.m581i()) {
                paymentActivity.m602b();
                return;
            }
            Calendar instance = Calendar.getInstance();
            instance.add(13, 1);
            paymentActivity.f692c = instance.getTime();
            paymentActivity.f693d.m567a(paymentActivity.m603c(), false);
        } else {
            Log.e(f690a, "Extras invalid.  Please see the docs.");
            paymentActivity.setResult(2);
            paymentActivity.finish();
        }
    }

    public final void finish() {
        super.finish();
        new StringBuilder().append(f690a).append(".finish");
    }

    protected final void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        new StringBuilder().append(f690a).append(".onActivityResult");
        if (i == 1) {
            switch (i2) {
                case -1:
                    if (intent == null) {
                        Log.e(f690a, "result was OK, no intent data, oops");
                        break;
                    }
                    PaymentConfirmation paymentConfirmation = (PaymentConfirmation) intent.getParcelableExtra(EXTRA_RESULT_CONFIRMATION);
                    if (paymentConfirmation == null) {
                        Log.e(f690a, "result was OK, have data, but no payment state in bundle, oops");
                        break;
                    }
                    Intent intent2 = new Intent();
                    intent2.putExtra(EXTRA_RESULT_CONFIRMATION, paymentConfirmation);
                    setResult(-1, intent2);
                    break;
                case 0:
                    break;
                default:
                    Log.wtf("paypal.sdk", "unexpected request code " + i + " call it a cancel");
                    break;
            }
        }
        finish();
    }

    protected final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        new StringBuilder().append(f690a).append(".onCreate");
        new gs(this).m437a();
        new gr(this).m435a();
        new gq(this).m434a(Arrays.asList(new String[]{PaymentActivity.class.getName(), LoginActivity.class.getName(), PaymentMethodActivity.class.getName(), PaymentConfirmActivity.class.getName()}));
        this.f695f = bindService(C0905d.m977b((Activity) this), this.f694e, 1);
        setTheme(16973934);
        requestWindowFeature(8);
        gn gnVar = new gn(this);
        setContentView(gnVar.f574a);
        gnVar.f575b.setText(fu.m369a(fw.CHECKING_DEVICE));
        C0905d.m971a((Activity) this, null, fw.CHECKING_DEVICE);
    }

    protected final Dialog onCreateDialog(int i, Bundle bundle) {
        switch (i) {
            case 2:
                return C0905d.m959a((Activity) this, new cj(this));
            case 3:
                return C0905d.m961a((Activity) this, fw.UNAUTHORIZED_MERCHANT_TITLE, bundle, i);
            default:
                return C0905d.m961a((Activity) this, fw.UNAUTHORIZED_DEVICE_TITLE, bundle, i);
        }
    }

    protected final void onDestroy() {
        new StringBuilder().append(f690a).append(".onDestroy");
        if (this.f693d != null) {
            this.f693d.m587o();
            this.f693d.m593u();
        }
        if (this.f695f) {
            unbindService(this.f694e);
            this.f695f = false;
        }
        super.onDestroy();
    }
}
