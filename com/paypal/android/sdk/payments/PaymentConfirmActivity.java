package com.paypal.android.sdk.payments;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ListView;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import com.paypal.android.sdk.C0441d;
import com.paypal.android.sdk.de;
import com.paypal.android.sdk.dq;
import com.paypal.android.sdk.dr;
import com.paypal.android.sdk.ds;
import com.paypal.android.sdk.dt;
import com.paypal.android.sdk.dw;
import com.paypal.android.sdk.ek;
import com.paypal.android.sdk.er;
import com.paypal.android.sdk.fc;
import com.paypal.android.sdk.fg;
import com.paypal.android.sdk.fu;
import com.paypal.android.sdk.fw;
import com.paypal.android.sdk.fz;
import com.paypal.android.sdk.ga;
import com.paypal.android.sdk.ge;
import com.paypal.android.sdk.gl;
import com.paypal.android.sdk.gm;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.json.JSONObject;

public final class PaymentConfirmActivity extends Activity {
    private static final String f696a = PaymentConfirmActivity.class.getSimpleName();
    private dg f697b;
    private dx f698c;
    private boolean f699d;
    private boolean f700e;
    private boolean f701f;
    private ge f702g;
    private cp f703h;
    private dh f704i;
    private Parcelable f705j;
    private PayPalService f706k;
    private final ServiceConnection f707l = new cw(this);
    private boolean f708m;

    private static er m608a(PayPalPayment payPalPayment) {
        return new er(new BigDecimal(ek.m331a(payPalPayment.m510a().doubleValue(), payPalPayment.m513d()).trim()), payPalPayment.m513d());
    }

    private void m611a(int i) {
        setResult(i, new Intent());
    }

    static void m612a(Activity activity, int i, dh dhVar, Parcelable parcelable, PayPalConfiguration payPalConfiguration) {
        m613a(activity, 2, dhVar, null, payPalConfiguration, false);
    }

    static void m613a(Activity activity, int i, dh dhVar, Parcelable parcelable, PayPalConfiguration payPalConfiguration, boolean z) {
        Intent intent = new Intent(activity, PaymentConfirmActivity.class);
        intent.putExtras(activity.getIntent());
        intent.putExtra("com.paypal.android.sdk.payments.PaymentConfirmActivity.EXTRA_PAYMENT_KIND", dhVar);
        intent.putExtra("com.paypal.android.sdk.payments.PaymentConfirmActivity.EXTRA_CREDIT_CARD", parcelable);
        intent.putExtra("com.paypal.android.sdk.payments.PaymentConfirmActivity.EXTRA_RESET_PP_REQUEST_ID", z);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfiguration);
        activity.startActivityForResult(intent, i);
    }

    private void m614a(Bundle bundle) {
        String string = bundle.getString("authAccount");
        String string2 = bundle.getString("authtoken");
        String string3 = bundle.getString("scope");
        long j = bundle.getLong("valid_until");
        for (String str : bundle.keySet()) {
            if (bundle.get(str) == null) {
                String.format("%s:null", new Object[]{(String) r1.next()});
            } else {
                String.format("%s:%s (%s)", new Object[]{(String) r1.next(), bundle.get(str).toString(), bundle.get(str).getClass().getName()});
            }
        }
        dw dwVar = new dw(string2, string3, j, false);
        if (this.f706k == null) {
            this.f697b = new dg(this, string, dwVar);
        } else {
            m619a(string, dwVar);
        }
    }

    static /* synthetic */ void m616a(PaymentConfirmActivity paymentConfirmActivity, fg fgVar) {
        paymentConfirmActivity.f698c = new dx(fgVar, paymentConfirmActivity.f703h.m692a().getProvidedShippingAddress());
        paymentConfirmActivity.getIntent().putExtra("com.paypal.android.sdk.payments.PaymentConfirmActivity.EXTRA_PAYMENT_INFO", paymentConfirmActivity.f698c);
        paymentConfirmActivity.m623b();
        paymentConfirmActivity.m641j();
    }

    static /* synthetic */ void m617a(PaymentConfirmActivity paymentConfirmActivity, List list, int i) {
        paymentConfirmActivity.f703h.m693b().m702a(i);
        paymentConfirmActivity.f702g.m398a((Context) paymentConfirmActivity, (fz) list.get(i));
    }

    private void m618a(String str) {
        this.f702g.m402a(str);
    }

    private void m619a(String str, dw dwVar) {
        this.f706k.m575c().f296c = str;
        m618a(str);
        this.f706k.m575c().f300g = dwVar;
        if (this.f704i != dh.PayPal) {
            this.f702g.m408b(true);
        }
    }

    private void m620a(boolean z) {
        new StringBuilder().append(f696a).append(".doLogin");
        if (C0457k.m723a(this, this.f706k)) {
            Intent a = new dq().m1060a(this.f706k.m576d().m490k(), z ? dr.PROMPT_LOGIN : dr.USER_REQUIRED, ds.token, this.f706k.m573b().mo2159d().m35e());
            a.putExtra("scope", "https://uri.paypal.com/services/payments/basic");
            new StringBuilder("startActivityForResult(").append(a).append(", 2").append(")");
            Log.w("paypal.sdk", "requesting " + a.getStringExtra("response_type") + " with scope={" + a.getStringExtra("scope") + "} from Authenticator.");
            startActivityForResult(a, 2);
            return;
        }
        LoginActivity.m444a(this, 1, this.f706k.m589q(), false, z, "https://uri.paypal.com/services/payments/basic", this.f706k.m576d());
    }

    private static Map m622b(PayPalPayment payPalPayment) {
        if (payPalPayment != null) {
            Map hashMap = new HashMap();
            PayPalPaymentDetails f = payPalPayment.m515f();
            if (f != null) {
                if (f.getShipping() != null) {
                    hashMap.put(Param.SHIPPING, ek.m331a(f.getShipping().doubleValue(), payPalPayment.m513d()));
                }
                if (f.getSubtotal() != null) {
                    hashMap.put("subtotal", ek.m331a(f.getSubtotal().doubleValue(), payPalPayment.m513d()));
                }
                if (f.getTax() != null) {
                    hashMap.put(Param.TAX, ek.m331a(f.getTax().doubleValue(), payPalPayment.m513d()));
                }
            }
            if (!hashMap.isEmpty()) {
                return hashMap;
            }
        }
        return null;
    }

    private void m623b() {
        if (this.f698c != null) {
            Object gmVar;
            JSONObject jSONObject = null;
            if (this.f698c.m703b() != null) {
                jSONObject = this.f698c.m703b().toJSONObject();
            }
            int h = this.f698c.m710h();
            ArrayList a = gl.m869a(jSONObject, this.f698c.m701a(), this.f698c.m711i());
            if (this.f703h.m692a().isNoShipping() || a == null || a.size() <= 0) {
                this.f702g.m414f().setClickable(false);
                this.f702g.m414f().setVisibility(8);
            } else {
                this.f702g.m414f().setVisibility(0);
                this.f702g.m414f().setClickable(true);
                this.f702g.m399a(getApplicationContext(), (gl) a.get(h));
                gmVar = new gm(this, a, h);
                new ListView(this).setAdapter(gmVar);
                this.f702g.m412d(new da(this, gmVar, a));
            }
            h = this.f698c.m709g();
            a = fz.m860a(this.f698c.m705c(), this.f698c.m706d());
            if (a == null || a.size() <= 0) {
                this.f702g.m413e().setClickable(false);
                this.f702g.m413e().setVisibility(8);
            } else {
                this.f702g.m413e().setVisibility(0);
                this.f702g.m413e().setClickable(true);
                this.f702g.m398a(getApplicationContext(), (fz) a.get(h));
                gmVar = new ga(this, a, h);
                new ListView(this).setAdapter(gmVar);
                this.f702g.m410c(new cy(this, gmVar, a));
            }
            this.f702g.m408b(true);
        }
    }

    static /* synthetic */ void m624b(PaymentConfirmActivity paymentConfirmActivity) {
        new StringBuilder().append(f696a).append(".postBindSetup()");
        if (paymentConfirmActivity.f704i.equals(dh.PayPal)) {
            paymentConfirmActivity.f702g.m400a(C0441d.m264b(paymentConfirmActivity.f706k.m576d().m480a()));
        } else {
            paymentConfirmActivity.f702g.m400a(null);
        }
        if (paymentConfirmActivity.f697b != null) {
            paymentConfirmActivity.m619a(paymentConfirmActivity.f697b.f844a, paymentConfirmActivity.f697b.f845b);
            paymentConfirmActivity.f697b = null;
        }
        if (paymentConfirmActivity.getIntent().getBooleanExtra("com.paypal.android.sdk.payments.PaymentConfirmActivity.EXTRA_RESET_PP_REQUEST_ID", false)) {
            paymentConfirmActivity.f706k.m575c().m279a();
        }
        boolean e = paymentConfirmActivity.m632e();
        if (!paymentConfirmActivity.f699d) {
            paymentConfirmActivity.f699d = true;
            paymentConfirmActivity.f706k.m561a(fc.ConfirmPaymentWindow);
        }
        paymentConfirmActivity.m633f();
        paymentConfirmActivity.f706k.m574b(new dc(paymentConfirmActivity));
        if (dh.PayPal == paymentConfirmActivity.f704i && !e && !paymentConfirmActivity.f701f && paymentConfirmActivity.f698c == null) {
            paymentConfirmActivity.m640i();
        }
    }

    static /* synthetic */ void m625b(PaymentConfirmActivity paymentConfirmActivity, List list, int i) {
        paymentConfirmActivity.f703h.m693b().m704b(i);
        paymentConfirmActivity.f702g.m399a((Context) paymentConfirmActivity, (gl) list.get(i));
    }

    private void m628c() {
        if (this.f706k.m575c().f300g != null && !this.f706k.m575c().f300g.m855a()) {
            this.f706k.m575c().f300g = null;
            this.f706k.m575c().f296c = null;
        }
    }

    private void m630d() {
        this.f708m = bindService(C0905d.m977b((Activity) this), this.f707l, 1);
    }

    private boolean m632e() {
        if (!this.f704i.equals(dh.PayPal) || this.f706k.m582j() || this.f700e) {
            return false;
        }
        this.f700e = true;
        m620a(false);
        return true;
    }

    private void m633f() {
        PayPalPayment a = this.f703h.m692a();
        this.f702g.m404a(a.m511b(), ek.m335a(Locale.getDefault(), de.m839a().mo2169c().m330a(), a.m510a().doubleValue(), a.m513d(), true));
        if (this.f704i == dh.PayPal) {
            this.f702g.m405a(true);
            m618a(this.f706k.m590r());
        } else if (this.f704i == dh.CreditCard || this.f704i == dh.CreditCardToken) {
            String a2;
            int b;
            int b2;
            Enum b3;
            this.f702g.m405a(false);
            if (this.f704i == dh.CreditCard) {
                a2 = dt.m845a(C0905d.m968a(this.f705j));
                b = C0905d.m976b(this.f705j, "expiryMonth");
                b2 = C0905d.m976b(this.f705j, "expiryYear");
                b3 = C0905d.m978b(this.f705j);
            } else {
                dt s = this.f706k.m591s();
                a2 = s.m850d();
                b = s.m852f();
                b2 = s.m853g();
                b3 = C0905d.m966a(s);
            }
            this.f702g.m403a(a2, C0905d.m964a((Activity) this, b3), String.format(Locale.getDefault(), "%02d / %04d", new Object[]{Integer.valueOf(b), Integer.valueOf(b2)}));
        } else {
            Log.wtf(f696a, "Unknown payment type: " + this.f704i.toString());
            C0905d.m973a((Activity) this, "The payment is not a valid type. Please try again.", 3);
        }
        C0905d.m974a(this.f702g.m411d(), this.f706k.m577e());
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void m636g() {
        /*
        r27 = this;
        r1 = com.paypal.android.sdk.payments.cu.f830a;
        r0 = r27;
        r2 = r0.f704i;
        r2 = r2.ordinal();
        r1 = r1[r2];
        switch(r1) {
            case 1: goto L_0x0013;
            case 2: goto L_0x001d;
            case 3: goto L_0x001d;
            default: goto L_0x000f;
        };
    L_0x000f:
        r1 = 1;
    L_0x0010:
        if (r1 != 0) goto L_0x004f;
    L_0x0012:
        return;
    L_0x0013:
        r1 = r27.m632e();
        if (r1 != 0) goto L_0x001b;
    L_0x0019:
        r1 = 1;
        goto L_0x0010;
    L_0x001b:
        r1 = 0;
        goto L_0x0010;
    L_0x001d:
        r0 = r27;
        r1 = r0.f706k;
        r1 = r1.m581i();
        if (r1 != 0) goto L_0x000f;
    L_0x0027:
        r1 = 2;
        r0 = r27;
        r0.showDialog(r1);
        r1 = new java.lang.StringBuilder;
        r2 = "token is expired, get new one. AccessToken: ";
        r1.<init>(r2);
        r0 = r27;
        r2 = r0.f706k;
        r2 = r2.m575c();
        r2 = r2.f295b;
        r1.append(r2);
        r0 = r27;
        r1 = r0.f706k;
        r2 = r27.m637h();
        r3 = 1;
        r1.m567a(r2, r3);
        r1 = 0;
        goto L_0x0010;
    L_0x004f:
        r1 = 2;
        r0 = r27;
        r0.showDialog(r1);
        r0 = r27;
        r1 = r0.f703h;
        if (r1 == 0) goto L_0x0077;
    L_0x005b:
        r0 = r27;
        r1 = r0.f703h;
        r1 = r1.m692a();
        if (r1 == 0) goto L_0x0077;
    L_0x0065:
        r0 = r27;
        r1 = r0.f704i;
        r2 = com.paypal.android.sdk.payments.dh.PayPal;
        if (r1 != r2) goto L_0x007b;
    L_0x006d:
        r0 = r27;
        r1 = r0.f703h;
        r1 = r1.m693b();
        if (r1 != 0) goto L_0x007b;
    L_0x0077:
        r27.onBackPressed();
        goto L_0x0012;
    L_0x007b:
        r0 = r27;
        r1 = r0.f703h;
        r17 = r1.m692a();
        r6 = m608a(r17);
        r7 = m622b(r17);
        r9 = r17.m511b();
        r0 = r27;
        r1 = r0.f706k;
        r1 = r1.m576d();
        r2 = r1.m489j();
        r1 = com.paypal.android.sdk.payments.cu.f830a;
        r0 = r27;
        r3 = r0.f704i;
        r3 = r3.ordinal();
        r1 = r1[r3];
        switch(r1) {
            case 1: goto L_0x00ac;
            case 2: goto L_0x00e1;
            case 3: goto L_0x012b;
            default: goto L_0x00aa;
        };
    L_0x00aa:
        goto L_0x0012;
    L_0x00ac:
        r0 = r27;
        r1 = r0.f703h;
        r6 = r1.m693b();
        r0 = r27;
        r1 = r0.f706k;
        r3 = r6.m707e();
        r4 = r6.m708f();
        r5 = r6.m713k();
        if (r5 == 0) goto L_0x00dd;
    L_0x00c6:
        r5 = r6.m715m();
    L_0x00ca:
        r7 = r6.m712j();
        if (r7 == 0) goto L_0x00df;
    L_0x00d0:
        r6 = r6.m714l();
    L_0x00d4:
        r7 = r17.m514e();
        r1.m571a(r2, r3, r4, r5, r6, r7);
        goto L_0x0012;
    L_0x00dd:
        r5 = 0;
        goto L_0x00ca;
    L_0x00df:
        r6 = 0;
        goto L_0x00d4;
    L_0x00e1:
        r0 = r27;
        r1 = r0.f706k;
        r1 = r1.m591s();
        r0 = r27;
        r3 = r0.f706k;
        r4 = r1.m299a();
        r11 = r3.m556a(r4);
        r0 = r27;
        r3 = r0.f706k;
        r0 = r27;
        r4 = r0.f706k;
        r4 = r4.m575c();
        r4 = r4.m280b();
        r5 = r1.m851e();
        r8 = r17.m516g();
        r12 = r17.m514e();
        r1 = r17.m512c();
        r13 = r1.toString();
        r14 = r17.m517h();
        r15 = r17.m518i();
        r16 = r17.m519j();
        r10 = r2;
        r3.m568a(r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16);
        goto L_0x0012;
    L_0x012b:
        r0 = r27;
        r10 = r0.f706k;
        r0 = r27;
        r1 = r0.f706k;
        r1 = r1.m575c();
        r11 = r1.m280b();
        r0 = r27;
        r1 = r0.f705j;
        r1 = com.paypal.android.sdk.payments.C0905d.m978b(r1);
        r1 = r1.name();
        r3 = java.util.Locale.US;
        r12 = r1.toLowerCase(r3);
        r0 = r27;
        r1 = r0.f705j;
        r3 = "cardNumber";
        r13 = com.paypal.android.sdk.payments.C0905d.m969a(r1, r3);
        r0 = r27;
        r1 = r0.f705j;
        r3 = "cvv";
        r14 = com.paypal.android.sdk.payments.C0905d.m969a(r1, r3);
        r0 = r27;
        r1 = r0.f705j;
        r3 = "expiryMonth";
        r15 = com.paypal.android.sdk.payments.C0905d.m976b(r1, r3);
        r0 = r27;
        r1 = r0.f705j;
        r3 = "expiryYear";
        r16 = com.paypal.android.sdk.payments.C0905d.m976b(r1, r3);
        r19 = r17.m516g();
        r22 = r17.m514e();
        r1 = r17.m512c();
        r23 = r1.toString();
        r24 = r17.m517h();
        r25 = r17.m518i();
        r26 = r17.m519j();
        r17 = r6;
        r18 = r7;
        r20 = r9;
        r21 = r2;
        r10.m569a(r11, r12, r13, r14, r15, r16, r17, r18, r19, r20, r21, r22, r23, r24, r25, r26);
        goto L_0x0012;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.paypal.android.sdk.payments.PaymentConfirmActivity.g():void");
    }

    private ce m637h() {
        return new ct(this);
    }

    private void m640i() {
        if (this.f706k != null && this.f706k.m575c().f300g != null) {
            showDialog(2);
            PayPalPayment a = this.f703h.m692a();
            this.f706k.m560a(m608a(a), m622b(a), a.m516g(), a.m511b(), this.f706k.m576d().m489j(), a.m514e(), a.m512c().toString(), a.isEnablePayPalShippingAddressesRetrieval(), a.m517h(), a.m518i(), a.m519j(), a.isNoShipping());
            this.f701f = true;
            m618a(this.f706k.m590r());
        }
    }

    private void m641j() {
        try {
            dismissDialog(2);
        } catch (IllegalArgumentException e) {
        }
    }

    public final void finish() {
        super.finish();
        new StringBuilder().append(f696a).append(".finish");
    }

    protected final void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        new StringBuilder().append(f696a).append(".onActivityResult(requestCode:").append(i).append(", resultCode:").append(i2).append(", data:").append(intent).append(")");
        switch (i) {
            case 1:
                this.f700e = false;
                if (i2 == -1) {
                    if (this.f702g != null) {
                        this.f702g.m408b(false);
                    }
                    if (this.f706k != null) {
                        m640i();
                        return;
                    }
                    return;
                }
                m611a(i2);
                finish();
                return;
            case 2:
                this.f700e = false;
                if (i2 == -1) {
                    this.f702g.m408b(true);
                    m614a(intent.getExtras());
                    if (this.f706k != null) {
                        m640i();
                        return;
                    }
                    return;
                }
                m611a(i2);
                finish();
                return;
            default:
                Log.e(f696a, "unhandled requestCode " + i);
                return;
        }
    }

    public final void onBackPressed() {
        this.f706k.m561a(fc.ConfirmPaymentCancel);
        m628c();
        super.onBackPressed();
    }

    protected final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        new StringBuilder().append(f696a).append(".onCreate");
        m630d();
        if (bundle == null) {
            if (!C0905d.m975a((Activity) this)) {
                finish();
            }
            this.f699d = false;
        } else {
            this.f699d = bundle.getBoolean("pageTrackingSent");
            this.f700e = bundle.getBoolean("isLoginActivityInProgress");
            this.f701f = bundle.getBoolean("isSFOPaymentRequestInProgress");
        }
        if (getIntent().getExtras() == null) {
            onBackPressed();
            return;
        }
        this.f704i = (dh) getIntent().getSerializableExtra("com.paypal.android.sdk.payments.PaymentConfirmActivity.EXTRA_PAYMENT_KIND");
        this.f705j = getIntent().getParcelableExtra("com.paypal.android.sdk.payments.PaymentConfirmActivity.EXTRA_CREDIT_CARD");
        this.f703h = new cp(getIntent());
        setTheme(16973934);
        requestWindowFeature(8);
        this.f702g = new ge(this, this.f704i == dh.PayPal);
        setContentView(this.f702g.m397a());
        C0905d.m971a((Activity) this, this.f702g.m406b(), fw.CONFIRM);
        this.f702g.m407b(new cq(this));
        this.f702g.m401a(new cv(this));
        if (dh.PayPal == this.f704i) {
            this.f698c = (dx) getIntent().getParcelableExtra("com.paypal.android.sdk.payments.PaymentConfirmActivity.EXTRA_PAYMENT_INFO");
            m623b();
        }
    }

    protected final Dialog onCreateDialog(int i, Bundle bundle) {
        switch (i) {
            case 1:
                return C0905d.m960a((Activity) this, fw.PAY_FAILED_ALERT_TITLE, bundle);
            case 2:
                return C0905d.m963a((Context) this, fw.PROCESSING, fw.ONE_MOMENT);
            case 3:
                return C0905d.m961a((Activity) this, fw.INTERNAL_ERROR, bundle, i);
            case 4:
                return C0905d.m962a((Activity) this, fw.SESSION_EXPIRED_TITLE, bundle, new dd(this));
            case 5:
                fu.m369a(fw.UNEXPECTED_PAYMENT_FLOW);
                fw fwVar;
                fw fwVar2;
                fw fwVar3;
                OnClickListener crVar;
                if (bundle == null || !C0441d.m269d(bundle.getString("BUNDLE_ERROR_CODE"))) {
                    fw fwVar4 = fw.WE_ARE_SORRY;
                    fwVar = fw.UNEXPECTED_PAYMENT_FLOW;
                    fwVar2 = fw.TRY_AGAIN;
                    fwVar3 = fw.CANCEL;
                    crVar = new cr(this);
                    return new Builder(this).setIcon(17301543).setTitle(fu.m369a(fwVar4)).setMessage(fu.m369a(fwVar)).setPositiveButton(fu.m369a(fwVar2), crVar).setNegativeButton(fu.m369a(fwVar3), new cs(this)).create();
                }
                String string = bundle.getString("BUNDLE_ERROR_CODE");
                fwVar = fw.WE_ARE_SORRY;
                CharSequence a = fu.m370a(string);
                fwVar2 = fw.TRY_AGAIN;
                fwVar3 = fw.CANCEL;
                crVar = new de(this);
                return new Builder(this).setIcon(17301543).setTitle(fu.m369a(fwVar)).setMessage(a).setPositiveButton(fu.m369a(fwVar2), crVar).setNegativeButton(fu.m369a(fwVar3), new df(this)).create();
            default:
                return null;
        }
    }

    protected final void onDestroy() {
        new StringBuilder().append(f696a).append(".onDestroy");
        if (this.f706k != null) {
            this.f706k.m585m();
        }
        if (this.f708m) {
            unbindService(this.f707l);
            this.f708m = false;
        }
        super.onDestroy();
    }

    protected final void onRestart() {
        super.onRestart();
        m630d();
    }

    protected final void onResume() {
        super.onResume();
        new StringBuilder().append(f696a).append(".onResume");
        if (this.f706k != null) {
            m633f();
        }
    }

    protected final void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("pageTrackingSent", this.f699d);
        bundle.putBoolean("isLoginActivityInProgress", this.f700e);
        bundle.putBoolean("isSFOPaymentRequestInProgress", this.f701f);
    }

    public final void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        new StringBuilder().append(f696a).append(".onWindowFocusChanged");
        this.f702g.m409c();
    }
}
