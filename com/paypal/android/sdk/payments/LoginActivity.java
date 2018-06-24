package com.paypal.android.sdk.payments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ListView;
import com.payUMoney.sdk.SdkConstants;
import com.paypal.android.sdk.C0441d;
import com.paypal.android.sdk.bp;
import com.paypal.android.sdk.de;
import com.paypal.android.sdk.dm;
import com.paypal.android.sdk.ei;
import com.paypal.android.sdk.ep;
import com.paypal.android.sdk.et;
import com.paypal.android.sdk.eu;
import com.paypal.android.sdk.ev;
import com.paypal.android.sdk.fc;
import com.paypal.android.sdk.fu;
import com.paypal.android.sdk.fw;
import com.paypal.android.sdk.gd;
import com.paypal.android.sdk.gh;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class LoginActivity extends Activity {
    private final String f595a = LoginActivity.class.getSimpleName();
    private bg f596b;
    private String f597c;
    private String f598d;
    private String f599e;
    private String f600f;
    private String f601g;
    private String f602h;
    private ep f603i;
    private String f604j;
    private boolean f605k;
    private boolean f606l;
    private boolean f607m;
    private boolean f608n;
    private int f609o;
    private gd f610p;
    private boolean f611q;
    private PayPalService f612r;
    private final ServiceConnection f613s = new ap(this);

    private ep m442a(bg bgVar) {
        m467g();
        if (bgVar != bg.PIN) {
            return bgVar == bg.EMAIL ? new ep(this.f597c, this.f598d) : this.f603i;
        } else {
            eu a = de.m839a();
            return new ep(this.f600f == null ? new ev(a, this.f599e) : new ev(a, new ei(this.f600f), this.f599e), this.f601g);
        }
    }

    static void m444a(Activity activity, int i, dm dmVar, boolean z, boolean z2, String str, PayPalConfiguration payPalConfiguration) {
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.putExtras(activity.getIntent());
        intent.putExtra("com.paypal.android.sdk.payments.persistedLogin", dmVar);
        intent.putExtra("com.paypal.android.sdk.payments.useResponseTypeCode", z);
        intent.putExtra("com.paypal.android.sdk.payments.forceLogin", z2);
        intent.putExtra("com.paypal.android.sdk.payments.requestedScopes", str);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfiguration);
        activity.startActivityForResult(intent, 1);
    }

    static /* synthetic */ void m446a(LoginActivity loginActivity, View view) {
        ep a = loginActivity.m442a(loginActivity.f596b);
        if (loginActivity.f596b == bg.PIN) {
            loginActivity.f603i = new ep(a.m343d(), null);
            loginActivity.m454b(bg.PIN_LOGIN_IN_PROGRESS);
        } else {
            loginActivity.f603i = new ep(a.m341b(), null);
            loginActivity.m454b(bg.EMAIL_LOGIN_IN_PROGRESS);
        }
        loginActivity.f612r.m559a(a, loginActivity.f606l, loginActivity.m450b(), loginActivity.m457c(), loginActivity.f604j);
    }

    static /* synthetic */ void m448a(LoginActivity loginActivity, cf cfVar) {
        if (cfVar.m682b()) {
            loginActivity.m458d();
            return;
        }
        Object obj = (cfVar.m681a() && cfVar.f820b.equals("invalid_user")) ? 1 : null;
        if (obj != null) {
            loginActivity.m477o();
            C0905d.m973a((Activity) loginActivity, fu.m369a(fw.TWO_FACTOR_AUTH_INVALID_ONE_TIME_PASSWORD), 3);
        } else if (cfVar.m683c()) {
            loginActivity.m477o();
            C0905d.m973a((Activity) loginActivity, fu.m370a(cfVar.f820b), 3);
        } else if ("invalid_nonce".equals(cfVar.f820b)) {
            loginActivity.f602h = null;
            loginActivity.m477o();
            C0905d.m973a((Activity) loginActivity, fu.m369a(fw.SESSION_EXPIRED_MESSAGE), 5);
        } else {
            loginActivity.f602h = null;
            loginActivity.m477o();
            C0905d.m973a((Activity) loginActivity, fu.m370a(cfVar.f820b), 4);
        }
    }

    static /* synthetic */ void m449a(LoginActivity loginActivity, String str) {
        loginActivity.f598d = null;
        loginActivity.f601g = null;
        loginActivity.m477o();
        C0905d.m973a((Activity) loginActivity, fu.m370a(str), 1);
    }

    private String m450b() {
        return m457c() ? "code" : SdkConstants.TOKEN;
    }

    static /* synthetic */ void m452b(LoginActivity loginActivity, View view) {
        loginActivity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(ae.m675a())));
        loginActivity.f612r.m562a(fc.LoginForgotPassword, Boolean.valueOf(loginActivity.f606l));
    }

    static /* synthetic */ void m453b(LoginActivity loginActivity, String str) {
        loginActivity.m477o();
        if ("invalid_nonce".equals(str)) {
            C0905d.m973a((Activity) loginActivity, fu.m369a(fw.SESSION_EXPIRED_MESSAGE), 5);
        } else {
            C0905d.m973a((Activity) loginActivity, fu.m370a(str), 2);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void m454b(com.paypal.android.sdk.payments.bg r8) {
        /*
        r7 = this;
        r6 = 21;
        r5 = 20;
        r4 = 8;
        r2 = 1;
        r3 = 0;
        r0 = new java.lang.StringBuilder;
        r1 = "changeLoginState:";
        r0.<init>(r1);
        r0.append(r8);
        if (r8 == 0) goto L_0x003b;
    L_0x0014:
        r7.f596b = r8;
    L_0x0016:
        r0 = 20;
        r7.dismissDialog(r0);	 Catch:{ IllegalArgumentException -> 0x01e6 }
        r0 = 21;
        r7.dismissDialog(r0);	 Catch:{ IllegalArgumentException -> 0x01e6 }
    L_0x0020:
        r0 = com.paypal.android.sdk.payments.ay.f784a;
        r1 = r7.f596b;
        r1 = r1.ordinal();
        r0 = r0[r1];
        switch(r0) {
            case 1: goto L_0x0078;
            case 2: goto L_0x0097;
            case 3: goto L_0x00db;
            case 4: goto L_0x0111;
            case 5: goto L_0x018c;
            case 6: goto L_0x0048;
            case 7: goto L_0x0060;
            case 8: goto L_0x007b;
            case 9: goto L_0x009a;
            case 10: goto L_0x00a8;
            case 11: goto L_0x0114;
            case 12: goto L_0x0147;
            case 13: goto L_0x018f;
            default: goto L_0x002d;
        };
    L_0x002d:
        r0 = com.paypal.android.sdk.payments.ay.f784a;
        r1 = r7.f596b;
        r1 = r1.ordinal();
        r0 = r0[r1];
        switch(r0) {
            case 1: goto L_0x01c2;
            case 2: goto L_0x01c2;
            case 3: goto L_0x01ce;
            case 4: goto L_0x01ce;
            case 5: goto L_0x01da;
            default: goto L_0x003a;
        };
    L_0x003a:
        return;
    L_0x003b:
        r0 = new java.lang.StringBuilder;
        r1 = "null loginState, refreshing:";
        r0.<init>(r1);
        r1 = r7.f596b;
        r0.append(r1);
        goto L_0x0016;
    L_0x0048:
        r7.m472j();
        r7.m475m();
        r0 = r7.f610p;
        r0 = r0.f506b;
        r0.setEnabled(r2);
        r0 = r7.f610p;
        r0 = r0.f508d;
        r0.setEnabled(r2);
        r7.m469h();
        goto L_0x002d;
    L_0x0060:
        r7.m472j();
        r7.m476n();
        r0 = r7.f610p;
        r0 = r0.f506b;
        r0.setEnabled(r2);
        r0 = r7.f610p;
        r0 = r0.f508d;
        r0.setEnabled(r2);
        r7.m469h();
        goto L_0x002d;
    L_0x0078:
        r7.showDialog(r5);
    L_0x007b:
        r7.m472j();
        r7.m475m();
        r0 = r7.f610p;
        r0 = r0.f506b;
        r0.setEnabled(r3);
        r0 = r7.f610p;
        r0 = r0.f508d;
        r0.setEnabled(r3);
        r0 = r7.f610p;
        r0 = r0.f512h;
        r0.setEnabled(r3);
        goto L_0x002d;
    L_0x0097:
        r7.showDialog(r5);
    L_0x009a:
        r7.m472j();
        r7.m476n();
        r0 = r7.f610p;
        r0 = r0.f512h;
        r0.setEnabled(r3);
        goto L_0x002d;
    L_0x00a8:
        r7.m474l();
        r7.m473k();
        r0 = r7.f610p;
        r0 = r0.f519o;
        r0 = r0.f558c;
        r1 = com.paypal.android.sdk.fw.TWO_FACTOR_AUTH_SEND_SMS;
        r1 = com.paypal.android.sdk.fu.m369a(r1);
        r0.setText(r1);
        r0 = r7.f610p;
        r0 = r0.f516l;
        r0.setEnabled(r3);
        r0 = r7.f610p;
        r0 = r0.f516l;
        r0.setVisibility(r4);
        r0 = r7.f610p;
        r0 = r0.f517m;
        r0.setEnabled(r3);
        r0 = r7.f610p;
        r0 = r0.f517m;
        r0.setVisibility(r4);
        goto L_0x002d;
    L_0x00db:
        r7.showDialog(r6);
        r7.m474l();
        r7.m473k();
        r0 = r7.f610p;
        r0 = r0.f519o;
        r0 = r0.f558c;
        r1 = com.paypal.android.sdk.fw.TWO_FACTOR_AUTH_SEND_SMS_AGAIN;
        r1 = com.paypal.android.sdk.fu.m369a(r1);
        r0.setText(r1);
        r0 = r7.f610p;
        r0 = r0.f516l;
        r0.setEnabled(r3);
        r0 = r7.f610p;
        r0 = r0.f516l;
        r0.setVisibility(r4);
        r0 = r7.f610p;
        r0 = r0.f517m;
        r0.setEnabled(r3);
        r0 = r7.f610p;
        r0 = r0.f517m;
        r0.setVisibility(r4);
        goto L_0x002d;
    L_0x0111:
        r7.showDialog(r6);
    L_0x0114:
        r7.m474l();
        r7.m473k();
        r0 = r7.f610p;
        r0 = r0.f519o;
        r0 = r0.f558c;
        r1 = com.paypal.android.sdk.fw.TWO_FACTOR_AUTH_SEND_SMS_AGAIN;
        r1 = com.paypal.android.sdk.fu.m369a(r1);
        r0.setText(r1);
        r0 = r7.f610p;
        r0 = r0.f516l;
        r0.setEnabled(r3);
        r0 = r7.f610p;
        r0 = r0.f516l;
        r0.setVisibility(r3);
        r0 = r7.f610p;
        r0 = r0.f517m;
        r0.setEnabled(r3);
        r0 = r7.f610p;
        r0 = r0.f517m;
        r0.setVisibility(r3);
        goto L_0x002d;
    L_0x0147:
        r7.m474l();
        r7.m473k();
        r0 = r7.f610p;
        r0 = r0.f519o;
        r0 = r0.f558c;
        r1 = com.paypal.android.sdk.fw.TWO_FACTOR_AUTH_SEND_SMS_AGAIN;
        r1 = com.paypal.android.sdk.fu.m369a(r1);
        r0.setText(r1);
        r0 = r7.f610p;
        r0 = r0.f516l;
        r0.setEnabled(r2);
        r0 = r7.f610p;
        r0 = r0.f516l;
        r0.setVisibility(r3);
        r0 = r7.f610p;
        r0 = r0.f516l;
        r0.requestFocus();
        r1 = new android.os.Handler;
        r1.<init>();
        r2 = new com.paypal.android.sdk.payments.am;
        r2.<init>(r7, r0);
        r4 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        r1.postDelayed(r2, r4);
        r0 = r7.f610p;
        r0 = r0.f517m;
        r0.setVisibility(r3);
        r7.m471i();
        goto L_0x002d;
    L_0x018c:
        r7.showDialog(r5);
    L_0x018f:
        r7.m474l();
        r7.m473k();
        r0 = r7.f610p;
        r0 = r0.f519o;
        r0 = r0.f558c;
        r1 = com.paypal.android.sdk.fw.TWO_FACTOR_AUTH_SEND_SMS_AGAIN;
        r1 = com.paypal.android.sdk.fu.m369a(r1);
        r0.setText(r1);
        r0 = r7.f610p;
        r0 = r0.f516l;
        r0.setEnabled(r3);
        r0 = r7.f610p;
        r0 = r0.f516l;
        r0.setVisibility(r3);
        r0 = r7.f610p;
        r0 = r0.f517m;
        r0.setEnabled(r3);
        r0 = r7.f610p;
        r0 = r0.f517m;
        r0.setVisibility(r3);
        goto L_0x002d;
    L_0x01c2:
        r0 = r7.f612r;
        r1 = new com.paypal.android.sdk.payments.bf;
        r1.<init>(r7);
        r0.m566a(r1);
        goto L_0x003a;
    L_0x01ce:
        r0 = r7.f612r;
        r1 = new com.paypal.android.sdk.payments.ak;
        r1.<init>(r7);
        r0.m566a(r1);
        goto L_0x003a;
    L_0x01da:
        r0 = r7.f612r;
        r1 = new com.paypal.android.sdk.payments.al;
        r1.<init>(r7);
        r0.m566a(r1);
        goto L_0x003a;
    L_0x01e6:
        r0 = move-exception;
        goto L_0x0020;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.paypal.android.sdk.payments.LoginActivity.b(com.paypal.android.sdk.payments.bg):void");
    }

    static /* synthetic */ void m456c(LoginActivity loginActivity, View view) {
        loginActivity.m467g();
        if (loginActivity.f596b == bg.PIN) {
            loginActivity.m454b(bg.EMAIL);
        } else {
            loginActivity.m454b(bg.PIN);
        }
        loginActivity.m465f();
        loginActivity.f610p.m396a(loginActivity.f596b == bg.EMAIL);
    }

    private boolean m457c() {
        return getIntent().getBooleanExtra("com.paypal.android.sdk.payments.useResponseTypeCode", false);
    }

    private void m458d() {
        if (this.f612r.m575c().f299f.f350a.isEmpty()) {
            m477o();
            C0905d.m973a((Activity) this, fu.m369a(fw.TWO_FACTOR_AUTH_NO_ACTIVE_TOKENS_ERROR), 10);
            return;
        }
        m454b(bg.TWO_FA_SEND_FIRST_SMS);
    }

    static /* synthetic */ void m460d(LoginActivity loginActivity, View view) {
        loginActivity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(ea.m716a())));
        loginActivity.f612r.m562a(fc.SignUp, Boolean.valueOf(loginActivity.f606l));
    }

    private void m462e() {
        setResult(-1);
        finish();
    }

    static /* synthetic */ void m463e(LoginActivity loginActivity, View view) {
        if (loginActivity.f596b == bg.TWO_FA_ENTER_OTP) {
            loginActivity.m454b(bg.TWO_FA_SEND_ANOTHER_SMS_IN_PROGRESS);
        } else {
            loginActivity.m454b(bg.TWO_FA_SEND_FIRST_SMS_IN_PROGRESS);
        }
        loginActivity.f610p.f516l.setText("");
        loginActivity.f612r.m557a(loginActivity.f609o);
    }

    private void m465f() {
        C0905d.m974a(this.f610p.f507c.f487b, this.f612r.m577e());
        m454b(null);
    }

    static /* synthetic */ void m466f(LoginActivity loginActivity, View view) {
        loginActivity.m454b(bg.TWO_FA_LOGIN_OTP_IN_PROGRESS);
        loginActivity.f612r.m558a(loginActivity.m442a(loginActivity.f596b), loginActivity.f610p.f516l.getText().toString(), loginActivity.f606l, loginActivity.m450b(), loginActivity.m457c(), loginActivity.f604j);
    }

    private void m467g() {
        if (this.f596b == bg.PIN) {
            this.f599e = this.f610p.f506b.getText().toString();
            this.f601g = this.f610p.f508d.getText().toString();
            return;
        }
        this.f597c = this.f610p.f506b.getText().toString();
        this.f598d = this.f610p.f508d.getText().toString();
    }

    static /* synthetic */ void m468g(LoginActivity loginActivity) {
        switch (ay.f784a[loginActivity.f596b.ordinal()]) {
            case 8:
                loginActivity.m454b(bg.EMAIL);
                return;
            case 9:
                loginActivity.m454b(bg.PIN);
                return;
            case 11:
                loginActivity.m454b(bg.TWO_FA_ENTER_OTP);
                return;
            case 13:
                loginActivity.m454b(bg.TWO_FA_ENTER_OTP);
                return;
            default:
                new StringBuilder().append(loginActivity.f596b).append(" case not handled");
                return;
        }
    }

    private void m469h() {
        boolean z = true;
        String obj = this.f610p.f506b.getText().toString();
        String obj2 = this.f610p.f508d.getText().toString();
        if (this.f596b == bg.PIN) {
            if (!(et.m350d(obj) && et.m348b(obj2))) {
                z = false;
            }
        } else if (!(et.m347a(obj) && et.m349c(obj2))) {
            z = false;
        }
        this.f610p.f512h.setEnabled(z);
        this.f610p.f512h.setFocusable(z);
    }

    static /* synthetic */ void m470h(LoginActivity loginActivity) {
        if (loginActivity.f603i.m340a()) {
            loginActivity.m454b(bg.EMAIL);
        } else {
            loginActivity.m454b(bg.PIN);
        }
    }

    private void m471i() {
        this.f610p.f517m.setEnabled(6 == this.f610p.f516l.getText().toString().length());
    }

    private void m472j() {
        this.f610p.f519o.f556a.setVisibility(8);
        this.f610p.f515k.setEnabled(false);
        this.f610p.f515k.setVisibility(8);
        this.f610p.f519o.f558c.setVisibility(8);
        this.f610p.f517m.setEnabled(false);
        this.f610p.f517m.setVisibility(8);
        this.f610p.f516l.setEnabled(false);
        this.f610p.f516l.setVisibility(8);
    }

    private void m473k() {
        C0905d.m971a((Activity) this, null, fw.TWO_FACTOR_AUTH_TITLE);
        this.f610p.f515k.setEnabled(true);
        this.f610p.f515k.setVisibility(0);
        new StringBuilder("phone numbers: ").append(this.f612r.m575c().f299f.f350a);
        List arrayList = new ArrayList(this.f612r.m575c().f299f.f350a.values());
        this.f610p.f519o.m420a((String) arrayList.get(this.f609o));
        this.f610p.f519o.f556a.setVisibility(0);
        if (arrayList.size() > 1) {
            this.f610p.f519o.m421a(true);
            Object ghVar = new gh(this, arrayList, this.f609o);
            new ListView(this).setAdapter(ghVar);
            this.f610p.f519o.f557b.setOnClickListener(new an(this, ghVar, arrayList));
        } else {
            this.f610p.f519o.m421a(false);
        }
        this.f610p.f519o.f558c.setVisibility(0);
    }

    private void m474l() {
        this.f610p.f512h.setEnabled(false);
        this.f610p.f512h.setVisibility(8);
        this.f610p.f506b.setEnabled(false);
        this.f610p.f506b.setVisibility(8);
        this.f610p.f508d.setEnabled(false);
        this.f610p.f508d.setVisibility(8);
        this.f610p.f509e.setEnabled(false);
        this.f610p.f509e.setVisibility(8);
    }

    private void m475m() {
        C0905d.m971a((Activity) this, null, fw.LOG_IN_TO_PAYPAL);
        this.f610p.f506b.setVisibility(0);
        this.f610p.f506b.setText(this.f597c);
        this.f610p.f506b.setHint(fu.m369a(fw.EMAIL));
        this.f610p.f506b.setInputType(33);
        this.f610p.f508d.setVisibility(0);
        this.f610p.f508d.setText(this.f598d);
        this.f610p.f508d.setHint(fu.m369a(fw.PASSWORD));
        this.f610p.f508d.setInputType(129);
        if (this.f610p.f506b.getText().length() > 0 && this.f610p.f508d.getText().length() == 0) {
            this.f610p.f508d.requestFocus();
        }
        this.f610p.f506b.setContentDescription("Email");
        this.f610p.f508d.setContentDescription("Password");
        this.f610p.f512h.setVisibility(0);
        this.f610p.f509e.setVisibility(0);
        this.f610p.f510f.setVisibility(0);
        this.f610p.f511g.setVisibility(0);
        this.f610p.f514j.setText(fu.m369a(fw.LOGIN_WITH_PHONE));
    }

    private void m476n() {
        C0905d.m971a((Activity) this, null, fw.LOG_IN_TO_PAYPAL);
        this.f610p.f506b.setVisibility(0);
        this.f610p.f506b.setText(this.f599e);
        this.f610p.f506b.setHint(fu.m369a(fw.PHONE));
        this.f610p.f506b.setInputType(3);
        this.f610p.f508d.setVisibility(0);
        this.f610p.f508d.setText(this.f601g);
        this.f610p.f508d.setHint(fu.m369a(fw.PIN));
        this.f610p.f508d.setInputType(18);
        if (this.f610p.f506b.getText().length() > 0 && this.f610p.f508d.getText().length() == 0) {
            this.f610p.f508d.requestFocus();
        }
        this.f610p.f506b.setContentDescription("Phone");
        this.f610p.f508d.setContentDescription("Pin");
        this.f610p.f512h.setVisibility(0);
        this.f610p.f509e.setVisibility(0);
        this.f610p.f510f.setVisibility(0);
        this.f610p.f511g.setVisibility(4);
        this.f610p.f514j.setText(fu.m369a(fw.LOGIN_WITH_EMAIL));
    }

    private void m477o() {
        switch (ay.f784a[this.f596b.ordinal()]) {
            case 1:
                m454b(bg.EMAIL_LOGIN_FAILED);
                return;
            case 2:
                m454b(bg.PIN_LOGIN_FAILED);
                return;
            case 3:
            case 4:
                m454b(bg.TWO_FA_SEND_SMS_FAILED);
                return;
            case 5:
                m454b(bg.TWO_FA_LOGIN_OTP_FAILED);
                return;
            default:
                new StringBuilder().append(this.f596b).append(" case not handled");
                return;
        }
    }

    final void m478a() {
        PayPalConfiguration d = this.f612r.m576d();
        if (fu.f419a) {
            this.f610p.f508d.setGravity(5);
            this.f610p.f506b.setGravity(5);
            this.f610p.f516l.setGravity(5);
        }
        if (!(et.m352f(Locale.getDefault().getCountry().toLowerCase(Locale.US)) && this.f612r.m575c().f301h)) {
            this.f610p.f514j.setVisibility(4);
        }
        if (this.f607m) {
            this.f607m = false;
            this.f597c = d.m482c();
            String d2 = d.m483d();
            if (d2 != null) {
                this.f599e = d2;
            }
            d2 = d.m484e();
            if (d2 != null) {
                this.f600f = d2;
            }
            if (d.m485f() && !bp.m162c(d.m481b())) {
                this.f598d = d.m486g();
                this.f601g = d.m487h();
            }
        }
        if (getIntent().getBooleanExtra("com.paypal.android.sdk.payments.forceLogin", false) && !this.f608n) {
            this.f608n = true;
            this.f612r.m580h();
        }
        if (this.f612r.m582j()) {
            m462e();
            return;
        }
        if (!this.f605k) {
            this.f605k = true;
            this.f612r.m562a(fc.LoginWindow, Boolean.valueOf(this.f606l));
        }
        if (this.f596b == null) {
            dm dmVar = (dm) getIntent().getParcelableExtra("com.paypal.android.sdk.payments.persistedLogin");
            if (dmVar != null) {
                this.f606l = true;
                if (C0441d.m262a(this.f597c) && C0441d.m266b(dmVar.m296b())) {
                    this.f597c = dmVar.m296b();
                }
                if (this.f599e == null && dmVar.m292a() != null) {
                    this.f599e = dmVar.m292a().m360a(de.m839a());
                }
                switch (ay.f785b[dmVar.m297c().ordinal()]) {
                    case 1:
                        m454b(bg.EMAIL);
                        break;
                    case 2:
                        m454b(bg.PIN);
                        break;
                }
            }
            m454b(bg.EMAIL);
        }
        m465f();
    }

    public final void onBackPressed() {
        this.f612r.m562a(fc.LoginCancel, Boolean.valueOf(this.f606l));
        super.onBackPressed();
    }

    protected final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        new StringBuilder().append(getClass().getSimpleName()).append(".onCreate");
        this.f604j = getIntent().getExtras().getString("com.paypal.android.sdk.payments.requestedScopes");
        this.f611q = bindService(C0905d.m977b((Activity) this), this.f613s, 1);
        setTheme(16973934);
        requestWindowFeature(8);
        this.f610p = new gd(this);
        setContentView(this.f610p.f505a);
        this.f610p.f510f.setText(fu.m369a(fw.SIGN_UP));
        this.f610p.f511g.setText(fu.m369a(fw.FORGOT_PASSWORD));
        this.f610p.f513i.setText(fu.m369a(fw.LOG_IN));
        this.f610p.f513i.setHint(fu.m369a(fw.LOG_IN));
        this.f610p.f515k.setText(fu.m369a(fw.TWO_FACTOR_AUTH_SUBTITLE));
        this.f610p.f516l.setHint(fu.m369a(fw.TWO_FACTOR_AUTH_ENTER_SECURITY_CODE));
        this.f610p.f518n.setText(fu.m369a(fw.LOG_IN));
        this.f610p.f519o.m422b(fu.m369a(fw.TWO_FACTOR_AUTH_ENTER_MOBILE_NUMBER));
        TextWatcher ajVar = new aj(this);
        this.f610p.f506b.addTextChangedListener(ajVar);
        this.f610p.f508d.addTextChangedListener(ajVar);
        this.f610p.f512h.setOnClickListener(new aw(this));
        this.f610p.f511g.setOnClickListener(new az(this));
        this.f610p.f514j.setOnClickListener(new ba(this));
        this.f610p.f510f.setOnClickListener(new bb(this));
        this.f610p.f519o.f558c.setOnClickListener(new bc(this));
        this.f610p.f516l.addTextChangedListener(new bd(this));
        this.f610p.f517m.setOnClickListener(new be(this));
        if (bundle == null) {
            this.f605k = false;
            this.f607m = true;
        } else {
            this.f607m = false;
            this.f605k = bundle.getBoolean("PP_PageTrackingSent");
            this.f596b = (bg) bundle.getParcelable("PP_LoginType");
            this.f597c = bundle.getString("PP_SavedEmail");
            this.f599e = bundle.getString("PP_SavedPhone");
            this.f600f = bundle.getString("PP_savedPhoneCountryCode");
            this.f598d = bundle.getString("PP_SavedPassword");
            this.f601g = bundle.getString("PP_SavedPIN");
            this.f606l = bundle.getBoolean("PP_IsReturningUser");
            this.f608n = bundle.getBoolean("PP_IsClearedLogin");
            this.f604j = bundle.getString("PP_RequestedScopes");
            this.f602h = bundle.getString("PP_SavedOTP");
            this.f603i = (ep) bundle.getParcelable("PP_OriginalLoginData");
            this.f609o = bundle.getInt("PP_TwoFaSelectedPhoneNumberIndex");
        }
        this.f610p.f516l.setText(this.f602h);
    }

    protected final Dialog onCreateDialog(int i, Bundle bundle) {
        switch (i) {
            case 1:
                return C0905d.m962a((Activity) this, fw.LOGIN_FAILED_ALERT_TITLE, bundle, new ar(this));
            case 2:
                return C0905d.m962a((Activity) this, fw.WE_ARE_SORRY, bundle, new as(this));
            case 3:
                return C0905d.m962a((Activity) this, fw.LOGIN_FAILED_ALERT_TITLE, bundle, new at(this));
            case 4:
                return C0905d.m962a((Activity) this, fw.LOGIN_FAILED_ALERT_TITLE, bundle, new au(this));
            case 5:
                return C0905d.m962a((Activity) this, fw.SESSION_EXPIRED_TITLE, bundle, new av(this));
            case 10:
                return C0905d.m962a((Activity) this, fw.LOGIN_FAILED_ALERT_TITLE, bundle, new ax(this));
            case 20:
                return C0905d.m963a((Context) this, fw.AUTHENTICATING, fw.ONE_MOMENT);
            case 21:
                return C0905d.m963a((Context) this, fw.TWO_FACTOR_AUTH_SENDING_DIALOG, fw.ONE_MOMENT);
            default:
                return null;
        }
    }

    protected final void onDestroy() {
        new StringBuilder().append(getClass().getSimpleName()).append(".onDestroy");
        if (this.f612r != null) {
            this.f612r.m586n();
        }
        if (this.f611q) {
            unbindService(this.f613s);
            this.f611q = false;
        }
        super.onDestroy();
    }

    protected final void onResume() {
        super.onResume();
        new StringBuilder().append(getClass().getSimpleName()).append(".onResume");
        if (this.f612r != null) {
            m465f();
        }
    }

    protected final void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        m467g();
        bundle.putParcelable("PP_LoginType", this.f596b);
        bundle.putString("PP_SavedEmail", this.f597c);
        bundle.putString("PP_SavedPhone", this.f599e);
        bundle.putString("PP_savedPhoneCountryCode", this.f600f);
        bundle.putString("PP_SavedPassword", this.f598d);
        bundle.putString("PP_SavedPIN", this.f601g);
        bundle.putBoolean("PP_IsReturningUser", this.f606l);
        bundle.putBoolean("PP_PageTrackingSent", this.f605k);
        bundle.putBoolean("PP_IsClearedLogin", this.f608n);
        bundle.putString("PP_RequestedScopes", this.f604j);
        bundle.putString("PP_SavedOTP", this.f602h);
        bundle.putParcelable("PP_OriginalLoginData", this.f603i);
        bundle.putInt("PP_TwoFaSelectedPhoneNumberIndex", this.f609o);
    }
}
