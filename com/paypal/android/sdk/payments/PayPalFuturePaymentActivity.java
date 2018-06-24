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

public final class PayPalFuturePaymentActivity extends Activity {
    public static final String EXTRA_RESULT_AUTHORIZATION = "com.paypal.android.sdk.authorization";
    public static final int RESULT_EXTRAS_INVALID = 2;
    private static final String f632a = PayPalFuturePaymentActivity.class.getSimpleName();
    private Date f633b;
    private Timer f634c;
    private PayPalService f635d;
    private final ServiceConnection f636e = new bl(this);
    private boolean f637f;

    private void m500b() {
        FuturePaymentConsentActivity.m907a(this, 1, this.f635d.m576d());
    }

    private ce m501c() {
        return new bn(this);
    }

    static /* synthetic */ void m502c(PayPalFuturePaymentActivity payPalFuturePaymentActivity) {
        if (payPalFuturePaymentActivity.f635d.m576d() == null) {
            Log.e(f632a, "Service state invalid.  Did you start the PayPalService?");
            payPalFuturePaymentActivity.setResult(2);
            payPalFuturePaymentActivity.finish();
            return;
        }
        bx bxVar = new bx(payPalFuturePaymentActivity.getIntent(), payPalFuturePaymentActivity.f635d.m576d(), false);
        if (!bxVar.m751b()) {
            Log.e(f632a, "Service extras invalid.  Please see the docs.");
            payPalFuturePaymentActivity.setResult(2);
            payPalFuturePaymentActivity.finish();
        } else if (!bxVar.mo2188c()) {
            Log.e(f632a, "Extras invalid.  Please see the docs.");
            payPalFuturePaymentActivity.setResult(2);
            payPalFuturePaymentActivity.finish();
        } else if (payPalFuturePaymentActivity.f635d.m581i()) {
            payPalFuturePaymentActivity.m500b();
        } else {
            Calendar instance = Calendar.getInstance();
            instance.add(13, 1);
            payPalFuturePaymentActivity.f633b = instance.getTime();
            payPalFuturePaymentActivity.f635d.m567a(payPalFuturePaymentActivity.m501c(), false);
        }
    }

    public final void finish() {
        super.finish();
        new StringBuilder().append(f632a).append(".finish");
    }

    protected final void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        new StringBuilder().append(f632a).append(".onActivityResult");
        if (i == 1) {
            switch (i2) {
                case -1:
                    if (intent == null) {
                        Log.e(f632a, "result was OK, no intent data, oops");
                        break;
                    }
                    PayPalAuthorization payPalAuthorization = (PayPalAuthorization) intent.getParcelableExtra("com.paypal.android.sdk.authorization");
                    if (payPalAuthorization == null) {
                        Log.e(f632a, "result was OK, have data, but no authorization state in bundle, oops");
                        break;
                    }
                    Intent intent2 = new Intent();
                    intent2.putExtra("com.paypal.android.sdk.authorization", payPalAuthorization);
                    setResult(-1, intent2);
                    break;
                case 0:
                    break;
                default:
                    Log.wtf(f632a, "unexpected request code " + i + " call it a cancel");
                    break;
            }
        }
        finish();
    }

    protected final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        new StringBuilder().append(f632a).append(".onCreate");
        new gs(this).m437a();
        new gr(this).m435a();
        new gq(this).m434a(Arrays.asList(new String[]{PayPalFuturePaymentActivity.class.getName(), LoginActivity.class.getName(), FuturePaymentInfoActivity.class.getName(), FuturePaymentConsentActivity.class.getName()}));
        this.f637f = bindService(C0905d.m977b((Activity) this), this.f636e, 1);
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
                return C0905d.m959a((Activity) this, new bk(this));
            case 3:
                return C0905d.m961a((Activity) this, fw.UNAUTHORIZED_MERCHANT_TITLE, bundle, i);
            default:
                return C0905d.m961a((Activity) this, fw.UNAUTHORIZED_DEVICE_TITLE, bundle, i);
        }
    }

    protected final void onDestroy() {
        new StringBuilder().append(f632a).append(".onDestroy");
        if (this.f635d != null) {
            this.f635d.m587o();
            this.f635d.m593u();
        }
        if (this.f637f) {
            unbindService(this.f636e);
            this.f637f = false;
        }
        super.onDestroy();
    }
}
