package com.paypal.android.sdk.payments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import com.paypal.android.sdk.C0441d;
import com.paypal.android.sdk.cz;
import com.paypal.android.sdk.de;
import com.paypal.android.sdk.dt;
import com.paypal.android.sdk.ek;
import com.paypal.android.sdk.fc;
import com.paypal.android.sdk.fu;
import com.paypal.android.sdk.fw;
import com.paypal.android.sdk.gf;
import java.util.Locale;
import java.util.Timer;

public final class PaymentMethodActivity extends Activity {
    private static final String f713a = PaymentMethodActivity.class.getSimpleName();
    private Timer f714b;
    private boolean f715c;
    private boolean f716d;
    private boolean f717e;
    private boolean f718f;
    private gf f719g;
    private cp f720h;
    private PayPalService f721i;
    private final ServiceConnection f722j = new dq(this);
    private boolean f723k;

    static void m646a(Activity activity, int i, PayPalConfiguration payPalConfiguration) {
        Intent intent = new Intent(activity, PaymentMethodActivity.class);
        intent.putExtras(activity.getIntent());
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfiguration);
        activity.startActivityForResult(intent, 1);
    }

    static /* synthetic */ void m647a(PaymentMethodActivity paymentMethodActivity) {
        paymentMethodActivity.f721i.m561a(fc.SelectPayPalPayment);
        PaymentConfirmActivity.m613a(paymentMethodActivity, 2, dh.PayPal, null, paymentMethodActivity.f721i.m576d(), true);
    }

    private void m649b() {
        if (this.f717e && !this.f716d) {
            this.f719g.f549m.setImageBitmap(cz.m251c("iVBORw0KGgoAAAANSUhEUgAAADcAAAAsCAYAAADByiAeAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAohJREFUeNrcWYGRgjAQJMwXQAl0IFbwWMFrBS8VvHYAFagVoBVIB2IFaAV8B08H/GUm/GA+CUouBL2ZTByEwCZ3m70LcRCsrusQutDBswranhBSOTYNgC1rM1ZA82yD+6nN2Urn21xNYAF0Jmf33Ro45DhDH9+1ObN3mMe84yVXTusd7ojjTds7iIzeoZtAU7mE1/E/5p536fj/yvbFb+VmDK2sn9c27b2RcKuVOs9vdJVnVN0QBsyHrjAQQ9RNMmhn5j5tC1g8zQ0A3AK4dbNqKbJ7nJjevIeYKN3HBpSOjy2h6DjznuxLQR4xpZvbYj4MX5+CO2S9aJvGCCEL+LnGEgBv2EEsWRXqIj4HJpeA3ML9FRq5IbhioEiHCsVz6V9s/H9WlwdiDHArSfycHpicpWSM0ia4UvJRRY+xlsiJcKwrnBPBtbSnLEt594b427O9cvisgL2cr6XobMobwbWdDXAitvvUTW8E5JTbAHcWXMOQUiHnHRcb4CrBXoYhBiYSjTooOH5GfSRB4I8BnI/xAV0eoTNxaOCUWfBjdh0DOFFsZAjgMkGtZnBCCTH3pIb2BewY2gDn8bkbU/q5xpgJ5t6pK7++BNcWHdUqmUV8GsQUj72irGD1KNvNHgQY8VJOIccGz+c8ybhxR9pyVOSCsW4+11S/asOZeMCIwWvdnyvuxygzJhgrh3pYiHiYqZ3P8XXI4t6SniTJ3WAWhjHBNUripKqNKNyQFoVXmB+DFXOqWDyw/tLEGItBCv6DpUkmTouSBlyJqOrHYlHjlocXA0Y9JGvAbSWpxrPajoaAy6mKVwBIDyHjG7ZkanyqKXxtu+IacEQ3bCmgZt8gixlhZdEBzK8AAwBIvuGtI5K/kgAAAABJRU5ErkJggg==", (Context) this));
            this.f719g.f549m.setVisibility(0);
            this.f719g.f549m.setContentDescription(fu.m369a(fw.SCAN_CARD_ICON_DESCRIPTION));
        }
    }

    private void m651c() {
        new StringBuilder().append(f713a).append(".refreshPayment");
        if (C0457k.m723a(this, this.f721i)) {
            this.f721i.m580h();
        }
        PayPalPayment a = this.f720h.m692a();
        CharSequence a2 = ek.m335a(Locale.getDefault(), de.m839a().mo2169c().m330a(), a.m510a().doubleValue(), a.m513d(), true);
        this.f719g.f539c.f570d.setText(a.m511b());
        this.f719g.f539c.f569c.setText(a2);
        if (this.f721i.m582j() && this.f721i.m575c().f300g.m855a()) {
            a2 = this.f721i.m590r();
            if (C0441d.m266b(a2)) {
                this.f719g.f540d.setText(a2);
                this.f719g.f540d.setVisibility(0);
                this.f719g.f538b.setVisibility(0);
            } else {
                this.f719g.f540d.setVisibility(8);
                this.f719g.f538b.setVisibility(8);
            }
        } else {
            this.f719g.f540d.setVisibility(8);
            this.f719g.f538b.setVisibility(8);
        }
        if (this.f721i.m576d().m488i()) {
            new ds().execute(new Void[0]);
            dt s = this.f721i.m591s();
            if (s == null || !s.m848b()) {
                this.f719g.f543g.setVisibility(8);
                this.f719g.f541e.setText(fu.m369a(fw.PAY_WITH_CARD));
                this.f719g.f547k.setVisibility(8);
            } else {
                this.f716d = true;
                this.f719g.f549m.setVisibility(8);
                this.f719g.f541e.setText(s.m850d());
                Enum a3 = C0905d.m966a(s);
                this.f719g.f543g.setImageBitmap(C0905d.m964a((Activity) this, a3));
                this.f719g.f543g.setContentDescription(a3.toString());
                this.f719g.f543g.setVisibility(0);
                this.f719g.f547k.setText(fu.m369a(fw.CLEAR_CREDIT_CARD_INFO));
                this.f719g.f547k.setVisibility(0);
                this.f719g.f544h.setVisibility(0);
                this.f719g.f547k.setVisibility(0);
            }
            m649b();
        } else {
            this.f719g.f544h.setVisibility(8);
            this.f719g.f547k.setVisibility(8);
        }
        C0905d.m974a(this.f719g.f545i.f487b, this.f721i.m576d().m481b());
    }

    static /* synthetic */ void m652c(PaymentMethodActivity paymentMethodActivity) {
        paymentMethodActivity.f721i.m561a(fc.SelectCreditCardPayment);
        dt s = paymentMethodActivity.f721i.m591s();
        if (s == null || !s.m848b()) {
            String a = paymentMethodActivity.f721i.m576d().m480a();
            Intent intent = new Intent(paymentMethodActivity, C0905d.m965a("io.card.payment.CardIOActivity"));
            intent.putExtra(C0905d.m970a("io.card.payment.CardIOActivity", "EXTRA_LANGUAGE_OR_LOCALE"), a);
            intent.putExtra(C0905d.m970a("io.card.payment.CardIOActivity", "EXTRA_REQUIRE_EXPIRY"), true);
            intent.putExtra(C0905d.m970a("io.card.payment.CardIOActivity", "EXTRA_REQUIRE_CVV"), true);
            new StringBuilder("startActivityForResult(").append(intent).append(", 1").append(")");
            paymentMethodActivity.startActivityForResult(intent, 1);
            return;
        }
        PaymentConfirmActivity.m612a(paymentMethodActivity, 2, dh.CreditCardToken, null, paymentMethodActivity.f721i.m576d());
    }

    private void m653d() {
        this.f723k = bindService(C0905d.m977b((Activity) this), this.f722j, 1);
    }

    static /* synthetic */ void m659i(PaymentMethodActivity paymentMethodActivity) {
        if (!paymentMethodActivity.f718f) {
            paymentMethodActivity.f718f = true;
            paymentMethodActivity.f721i.m561a(fc.PaymentMethodWindow);
        }
        boolean z = !paymentMethodActivity.f715c && (!paymentMethodActivity.f721i.m576d().m488i() || paymentMethodActivity.f721i.m591s() == null);
        new StringBuilder("autoAdvanceToPayPalConfirmIfLoggedIn: ").append(z);
        if (!C0457k.m723a(paymentMethodActivity, paymentMethodActivity.f721i) && (!(paymentMethodActivity.f721i.m576d().m488i() || paymentMethodActivity.f715c) || (z && paymentMethodActivity.f721i.m582j() && paymentMethodActivity.f721i.m575c().f300g.m855a()))) {
            paymentMethodActivity.showDialog(3);
            paymentMethodActivity.f715c = true;
            paymentMethodActivity.f714b = new Timer();
            paymentMethodActivity.f714b.schedule(new dn(paymentMethodActivity), 1000);
            paymentMethodActivity.f715c = true;
        }
        paymentMethodActivity.m651c();
    }

    protected final void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        new StringBuilder().append(f713a).append(".onActivityResult (requestCode: ").append(i).append(", resultCode: ").append(i2).append(")");
        switch (i) {
            case 1:
                if (intent != null && intent.hasExtra(C0905d.m970a("io.card.payment.CardIOActivity", "EXTRA_SCAN_RESULT"))) {
                    PaymentConfirmActivity.m613a(this, 2, dh.CreditCard, intent.getParcelableExtra(C0905d.m970a("io.card.payment.CardIOActivity", "EXTRA_SCAN_RESULT")), (PayPalConfiguration) getIntent().getParcelableExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION), true);
                    return;
                }
                return;
            case 2:
                if (i2 == -1) {
                    PaymentConfirmation paymentConfirmation = (PaymentConfirmation) intent.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                    Intent intent2 = new Intent();
                    intent2.putExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION, paymentConfirmation);
                    setResult(i2, intent2);
                    finish();
                    return;
                } else if (i2 == 0) {
                    this.f715c = true;
                    return;
                } else {
                    return;
                }
            default:
                return;
        }
    }

    public final void onBackPressed() {
        new StringBuilder().append(f713a).append(".onBackPressed");
        if (this.f721i != null) {
            this.f721i.m561a(fc.PaymentMethodCancel);
        }
        if (this.f714b != null) {
            this.f714b.cancel();
        }
        super.onBackPressed();
    }

    protected final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        new StringBuilder().append(f713a).append(".onCreate");
        setTheme(16973934);
        requestWindowFeature(8);
        m653d();
        this.f719g = new gf(this);
        this.f720h = new cp(getIntent());
        setContentView(this.f719g.f537a);
        C0905d.m971a((Activity) this, this.f719g.f548l, fw.YOUR_ORDER);
        this.f719g.f542f.setText(fu.m369a(fw.PAY_WITH));
        this.f719g.f538b.setText(fu.m369a(fw.LOG_OUT_BUTTON));
        this.f719g.f546j.setOnClickListener(new dj(this));
        this.f719g.f538b.setOnClickListener(new dk(this));
        this.f719g.f544h.setOnClickListener(new dl(this));
        this.f719g.f547k.setOnClickListener(new dm(this));
        if (bundle == null) {
            if (!C0905d.m975a((Activity) this)) {
                finish();
            }
            this.f718f = false;
        } else {
            this.f715c = bundle.getBoolean("PP_PreventAutoLogin");
            this.f718f = bundle.getBoolean("PP_PageTrackingSent");
        }
        this.f714b = null;
    }

    protected final Dialog onCreateDialog(int i, Bundle bundle) {
        switch (i) {
            case 1:
                return C0905d.m957a((Activity) this, fw.LOG_OUT, fw.CONFIRM_LOG_OUT, new C0451do(this));
            case 2:
                return C0905d.m957a((Activity) this, fw.CLEAR_CC_ALERT_TITLE, fw.CONFIRM_CLEAR_CREDIT_CARD_INFO, new dp(this));
            case 3:
                return C0905d.m963a((Context) this, fw.AUTHENTICATING, fw.ONE_MOMENT);
            default:
                return null;
        }
    }

    protected final void onDestroy() {
        new StringBuilder().append(f713a).append(".onDestroy");
        if (this.f723k) {
            unbindService(this.f722j);
            this.f723k = false;
        }
        super.onDestroy();
    }

    protected final void onRestart() {
        super.onRestart();
        m653d();
    }

    protected final void onResume() {
        super.onResume();
        new StringBuilder().append(f713a).append(".onResume");
        if (this.f721i != null) {
            m651c();
        }
    }

    protected final void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        new StringBuilder().append(f713a).append(".onSaveInstanceState");
        bundle.putBoolean("PP_PreventAutoLogin", this.f715c);
        bundle.putBoolean("PP_PageTrackingSent", this.f718f);
    }

    public final void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        this.f719g.f539c.m423a();
    }
}
