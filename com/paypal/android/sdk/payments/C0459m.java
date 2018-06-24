package com.paypal.android.sdk.payments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.BulletSpan;
import android.text.style.URLSpan;
import android.util.Log;
import com.paypal.android.sdk.C0441d;
import com.paypal.android.sdk.ak;
import com.paypal.android.sdk.dj;
import com.paypal.android.sdk.dq;
import com.paypal.android.sdk.dr;
import com.paypal.android.sdk.ds;
import com.paypal.android.sdk.fc;
import com.paypal.android.sdk.fk;
import com.paypal.android.sdk.fu;
import com.paypal.android.sdk.fw;
import com.paypal.android.sdk.fx;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

abstract class C0459m extends Activity {
    private static final String f884d = C0459m.class.getSimpleName();
    private static final Map f885l = new C0460o();
    protected PayPalService f886a;
    protected PayPalOAuthScopes f887b;
    protected fx f888c;
    private boolean f889e;
    private C0456j f890f;
    private boolean f891g;
    private boolean f892h;
    private boolean f893i;
    private du f894j;
    private final ServiceConnection f895k = new C0462u(this);

    C0459m() {
    }

    private void m725a(int i, PayPalAuthorization payPalAuthorization) {
        Intent intent = new Intent();
        intent.putExtra("com.paypal.android.sdk.authorization", payPalAuthorization);
        setResult(i, intent);
    }

    private void m726a(int i, String str, String str2, ag agVar) {
        SpannableString spannableString = new SpannableString(Html.fromHtml(str2 + str));
        if (agVar != null) {
            URLSpan[] uRLSpanArr = (URLSpan[]) spannableString.getSpans(0, spannableString.length(), URLSpan.class);
            if (uRLSpanArr.length > 0) {
                URLSpan uRLSpan = uRLSpanArr[0];
                spannableString.setSpan(new C0449b(uRLSpan, this, FuturePaymentInfoActivity.class, new C0907n(this), agVar), spannableString.getSpanStart(uRLSpan), spannableString.getSpanEnd(uRLSpan), 33);
                spannableString.removeSpan(uRLSpan);
            }
        } else {
            m727a(spannableString);
        }
        spannableString.setSpan(new BulletSpan(15), 0, spannableString.length(), 0);
        this.f888c.f476c[i].setVisibility(0);
        this.f888c.f476c[i].setFocusable(true);
        this.f888c.f476c[i].setNextFocusLeftId((i + 100) - 1);
        this.f888c.f476c[i].setNextFocusRightId((i + 100) + 1);
        this.f888c.f476c[i].setText(spannableString);
    }

    private void m727a(SpannableString spannableString) {
        int i = 0;
        URLSpan[] uRLSpanArr = (URLSpan[]) spannableString.getSpans(0, spannableString.length(), URLSpan.class);
        int length = uRLSpanArr.length;
        while (i < length) {
            URLSpan uRLSpan = uRLSpanArr[i];
            spannableString.setSpan(new eb(uRLSpan, new C0908p(this)), spannableString.getSpanStart(uRLSpan), spannableString.getSpanEnd(uRLSpan), 33);
            spannableString.removeSpan(uRLSpan);
            i++;
        }
    }

    private void m728a(C0456j c0456j) {
        this.f886a.m575c().f302i = c0456j.f879a;
        this.f886a.m575c().f298e = c0456j.f880b;
        this.f886a.m575c().f296c = c0456j.f881c;
        m746j();
    }

    static /* synthetic */ void m731a(C0459m c0459m, fk fkVar) {
        c0459m.f894j = new du(fkVar);
        c0459m.getIntent().putExtra("com.paypal.android.sdk.payments.ppAppInfo", c0459m.f894j);
        c0459m.m741f();
        c0459m.m747k();
    }

    private void m733b(SpannableString spannableString) {
        int i = 0;
        URLSpan[] uRLSpanArr = (URLSpan[]) spannableString.getSpans(0, spannableString.length(), URLSpan.class);
        int length = uRLSpanArr.length;
        while (i < length) {
            URLSpan uRLSpan = uRLSpanArr[i];
            spannableString.setSpan(new eb(uRLSpan, new C0909q(this)), spannableString.getSpanStart(uRLSpan), spannableString.getSpanEnd(uRLSpan), 33);
            spannableString.removeSpan(uRLSpan);
            i++;
        }
    }

    private void m735c() {
        if (this.f886a != null) {
            showDialog(2);
            if (this.f886a.m581i()) {
                this.f886a.m588p();
                return;
            }
            new StringBuilder("token is expired, get new one. AccessToken: ").append(this.f886a.m575c().f295b);
            this.f886a.m567a(new C0910r(this), true);
        }
    }

    private void m737d() {
        this.f889e = bindService(C0905d.m977b((Activity) this), this.f895k, 1);
    }

    static /* synthetic */ void m738d(C0459m c0459m) {
        boolean z;
        new StringBuilder().append(f884d).append(".postBindSetup()");
        new StringBuilder().append(f884d).append(".startLoginIfNeeded (access token: ").append(c0459m.f886a.m575c().f300g).append(")");
        if (c0459m.f886a.m582j() || c0459m.f892h) {
            z = false;
        } else {
            new StringBuilder().append(f884d).append(" -- doing the login...");
            c0459m.f892h = true;
            c0459m.m739e();
            z = true;
        }
        fx fxVar = c0459m.f888c;
        if (c0459m.f893i) {
            c0459m.f893i = false;
            c0459m.m746j();
        }
        if (!c0459m.f891g) {
            c0459m.f891g = true;
            c0459m.f886a.m561a(fc.ConsentWindow);
        }
        C0905d.m974a(fxVar.f479f.f487b, c0459m.f886a.m577e());
        c0459m.f886a.m574b(new C0911s(c0459m));
        c0459m.m741f();
        if (!z && c0459m.f894j == null) {
            c0459m.m735c();
        }
    }

    private void m739e() {
        new StringBuilder().append(f884d).append(".doLogin");
        if (C0457k.m723a(this, this.f886a)) {
            Intent a = new dq().m1060a(this.f886a.m576d().m490k(), dr.PROMPT_LOGIN, ds.code, this.f886a.m573b().mo2159d().m35e());
            new StringBuilder("startActivityForResult(").append(a).append(", 2").append(")");
            Log.w("paypal.sdk", "requesting code with scope=null from Authenticator.");
            startActivityForResult(a, 2);
            return;
        }
        LoginActivity.m444a(this, 1, null, true, false, this.f887b.m507b(), this.f886a.m576d());
    }

    static /* synthetic */ void m740e(C0459m c0459m) {
        c0459m.f886a.m561a(fc.ConsentCancel);
        c0459m.finish();
    }

    private void m741f() {
        if (this.f887b != null && this.f894j != null && this.f886a != null) {
            int i;
            String l = this.f886a.m576d().m491l();
            if (this.f894j.m698d() != null) {
                l = this.f894j.m698d();
            }
            String uri = this.f886a.m576d().m492m().toString();
            if (this.f894j.m696b() != null) {
                uri = this.f894j.m696b();
            }
            String uri2 = this.f886a.m576d().m493n().toString();
            if (this.f894j.m697c() != null) {
                uri2 = this.f894j.m697c();
            }
            String format = String.format(fu.m369a(fw.CONSENT_AGREEMENT_INTRO), new Object[]{"<b>" + l + "</b>"});
            String str = fu.f419a ? "‏" : "";
            this.f888c.f476c[0].setText(Html.fromHtml(str + format));
            if (fu.f419a) {
                this.f888c.f476c[0].setGravity(5);
            }
            this.f888c.f476c[0].setVisibility(0);
            Collection a = this.f887b.m506a();
            if (a.contains(ak.FUTURE_PAYMENTS.m40a()) || a.contains(dj.PAYMENT_CODE.m285a()) || a.contains(dj.MIS_CUSTOMER.m285a())) {
                m726a(1, String.format(fu.m369a(fw.CONSENT_AGREEMENT_FUTURE_PAYMENTS), new Object[]{"future-payment-consent", "<b>" + l + "</b>", "<b>" + l + "</b>"}), str, ag.FUTURE_PAYMENTS);
                i = 2;
            } else {
                i = 1;
            }
            if (a.contains(dj.GET_FUNDING_OPTIONS.m285a())) {
                m726a(i, fu.m369a(fw.CONSENT_AGREEMENT_FUNDING_OPTIONS), str, null);
                i++;
            }
            if (a.contains(dj.FINANCIAL_INSTRUMENTS.m285a())) {
                m726a(i, fu.m369a(fw.CONSENT_AGREEMENT_FINANCIAL_INSTRUMENTS), str, ag.FINANCIAL_INSTRUMENTS);
                i++;
            }
            if (a.contains(dj.SEND_MONEY.m285a())) {
                m726a(i, String.format(fu.m369a(fw.CONSENT_AGREEMENT_SEND_MONEY), new Object[]{"", l}), str, ag.SEND_MONEY);
                i++;
            }
            if (a.contains(dj.REQUEST_MONEY.m285a())) {
                m726a(i, String.format(fu.m369a(fw.CONSENT_AGREEMENT_REQUEST_MONEY), new Object[]{"", l}), str, ag.REQUEST_MONEY);
                i++;
            }
            if (a.contains(dj.LOYALTY.m285a())) {
                m726a(i, fu.m369a(fw.CONSENT_AGREEMENT_LOYALTY_CARD), str, null);
                i++;
            }
            if (a.contains(dj.EXPRESS_CHECKOUT.m285a())) {
                m726a(i, fu.m369a(fw.CONSENT_AGREEMENT_EXPRESS_CHECKOUT), str, null);
                i++;
            }
            if (!Collections.disjoint(a, ak.f20h)) {
                if (m743g().size() > 0) {
                    m726a(i, String.format(fu.m369a(fw.CONSENT_AGREEMENT_ATTRIBUTES), new Object[]{m744h()}), str, null);
                    i++;
                }
            }
            m726a(i, String.format(fu.m369a(fw.CONSENT_AGREEMENT_MERCHANT_PRIVACY_POLICY), new Object[]{"<b>" + l + "</b>", uri, uri2}), str, null);
            this.f888c.f476c[i].setNextFocusRightId(2);
            int i2 = i + 1;
            uri2 = fu.m369a(fw.PRIVACY);
            Object[] objArr = new Object[1];
            CharSequence toLowerCase = Locale.getDefault().getCountry().toLowerCase(Locale.US);
            if (C0441d.m267c(toLowerCase)) {
                toLowerCase = "us";
            }
            objArr[0] = String.format("https://www.paypal.com/%s/cgi-bin/webscr?cmd=p/gen/ua/policy_privacy-outside", new Object[]{toLowerCase});
            SpannableString spannableString = new SpannableString(Html.fromHtml(str + String.format(uri2, objArr)));
            m733b(spannableString);
            this.f888c.f477d.setText(spannableString);
            this.f888c.f477d.setMovementMethod(LinkMovementMethod.getInstance());
            this.f888c.f477d.setNextFocusLeftId((i2 + 100) - 1);
            this.f888c.f477d.setNextFocusRightId(1);
            toLowerCase = C0441d.m264b(this.f886a.m576d().m480a());
            if (toLowerCase != null) {
                this.f888c.f478e.setText(toLowerCase);
                this.f888c.f478e.setVisibility(0);
            }
            this.f888c.f482i.setText(fu.m369a(fw.CONSENT_AGREEMENT_AGREE));
            this.f888c.f480g.setOnClickListener(new C0463w(this));
            this.f888c.f481h.setOnClickListener(new C0464x(this));
            this.f888c.f481h.setEnabled(true);
            if (this.f890f != null) {
                m728a(this.f890f);
                this.f890f = null;
            }
        }
    }

    static /* synthetic */ void m742f(C0459m c0459m) {
        c0459m.f886a.m561a(fc.ConsentAgree);
        if (c0459m.f886a.m583k()) {
            c0459m.showDialog(2);
            c0459m.f886a.m570a(c0459m.f887b.m506a());
            return;
        }
        new StringBuilder("code/nonce invalid.  code:").append(c0459m.f886a.m575c().f298e).append(", nonce:").append(c0459m.f886a.m575c().f302i);
        C0905d.m973a((Activity) c0459m, fu.m369a(fw.SESSION_EXPIRED_MESSAGE), 4);
    }

    private Set m743g() {
        Collection a = this.f887b.m506a();
        Set linkedHashSet = new LinkedHashSet();
        for (C0465y c0465y : C0465y.values()) {
            if (this.f894j.m695a().contains(c0465y.name()) && a.contains(((ak) f885l.get(c0465y)).m40a())) {
                Object a2 = c0465y == C0465y.openid_connect ? null : c0465y == C0465y.oauth_account_creation_date ? fu.m369a(fw.CONSENT_AGREEMENT_ATTRIBUTE_ACCOUNT_CREATION_DATE) : c0465y == C0465y.oauth_account_verified ? fu.m369a(fw.CONSENT_AGREEMENT_ATTRIBUTE_ACCOUNT_STATUS) : c0465y == C0465y.oauth_account_type ? fu.m369a(fw.CONSENT_AGREEMENT_ATTRIBUTE_ACCOUNT_TYPE) : (c0465y == C0465y.oauth_street_address1 || c0465y == C0465y.oauth_street_address2 || c0465y == C0465y.oauth_city || c0465y == C0465y.oauth_state || c0465y == C0465y.oauth_country || c0465y == C0465y.oauth_zip) ? fu.m369a(fw.CONSENT_AGREEMENT_ATTRIBUTE_ADDRESS) : c0465y == C0465y.oauth_age_range ? fu.m369a(fw.CONSENT_AGREEMENT_ATTRIBUTE_AGE_RANGE) : c0465y == C0465y.oauth_date_of_birth ? fu.m369a(fw.CONSENT_AGREEMENT_ATTRIBUTE_DATE_OF_BIRTH) : c0465y == C0465y.oauth_email ? fu.m369a(fw.CONSENT_AGREEMENT_ATTRIBUTE_EMAIL_ADDRESS) : c0465y == C0465y.oauth_fullname ? fu.m369a(fw.CONSENT_AGREEMENT_ATTRIBUTE_FULL_NAME) : c0465y == C0465y.oauth_gender ? fu.m369a(fw.CONSENT_AGREEMENT_ATTRIBUTE_GENDER) : c0465y == C0465y.oauth_language ? fu.m369a(fw.CONSENT_AGREEMENT_ATTRIBUTE_LANGUAGE) : c0465y == C0465y.oauth_locale ? fu.m369a(fw.CONSENT_AGREEMENT_ATTRIBUTE_LOCALE) : c0465y == C0465y.oauth_phone_number ? fu.m369a(fw.CONSENT_AGREEMENT_ATTRIBUTE_PHONE) : c0465y == C0465y.oauth_timezone ? fu.m369a(fw.CONSENT_AGREEMENT_ATTRIBUTE_TIME_ZONE) : c0465y.name();
                if (a2 != null) {
                    linkedHashSet.add(a2);
                }
            }
        }
        return linkedHashSet;
    }

    private String m744h() {
        StringBuilder stringBuilder = new StringBuilder();
        Object obj = 1;
        for (String str : m743g()) {
            if (obj == null) {
                stringBuilder.append(", ");
            } else {
                obj = null;
            }
            stringBuilder.append(str);
        }
        return stringBuilder.toString();
    }

    private void m745i() {
        m725a(-1, new PayPalAuthorization(this.f886a.m577e(), this.f886a.m575c().f298e.m301a(), this.f886a.m575c().f296c));
        finish();
    }

    private void m746j() {
        String b = this.f886a.m575c().f298e.m302b();
        if (b == null || !Arrays.asList(b.split(" ")).containsAll(this.f887b.m506a())) {
            m735c();
        } else {
            m745i();
        }
    }

    private void m747k() {
        try {
            dismissDialog(2);
        } catch (IllegalArgumentException e) {
        }
    }

    protected abstract void mo2179a();

    public void finish() {
        super.finish();
        new StringBuilder().append(f884d).append(".finish");
    }

    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        new StringBuilder().append(f884d).append(".onActivityResult(").append(i).append(",").append(i2).append(",").append(intent).append(")");
        switch (i) {
            case 1:
                if (i2 != -1) {
                    m725a(i2, null);
                    finish();
                    return;
                } else if (this.f886a == null) {
                    this.f893i = true;
                    return;
                } else {
                    m746j();
                    return;
                }
            case 2:
                if (i2 == -1) {
                    C0456j a = C0458l.m724a(intent.getExtras());
                    if (this.f886a == null) {
                        this.f890f = a;
                        return;
                    } else {
                        m728a(a);
                        return;
                    }
                }
                m725a(i2, null);
                finish();
                return;
            default:
                Log.e(f884d, "unhandled requestCode " + i);
                return;
        }
    }

    public void onBackPressed() {
        this.f886a.m561a(fc.ConsentCancel);
        super.onBackPressed();
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        new StringBuilder().append(f884d).append(".onCreate");
        if (bundle == null) {
            if (!C0905d.m975a((Activity) this)) {
                finish();
            }
            this.f891g = false;
        } else {
            this.f891g = bundle.getBoolean("pageTrackingSent");
            this.f892h = bundle.getBoolean("isLoginActivityStarted");
        }
        mo2179a();
        this.f894j = (du) getIntent().getParcelableExtra("com.paypal.android.sdk.payments.ppAppInfo");
        m737d();
        setTheme(16973934);
        requestWindowFeature(8);
        this.f888c = new fx(this);
        setContentView(this.f888c.f474a);
        C0905d.m971a((Activity) this, this.f888c.f475b, null);
        this.f888c.f480g.setText(fu.m369a(fw.CANCEL));
        this.f888c.f480g.setVisibility(0);
        m741f();
    }

    protected Dialog onCreateDialog(int i, Bundle bundle) {
        switch (i) {
            case 1:
                return C0905d.m960a((Activity) this, fw.CONSENT_FAILED_ALERT_TITLE, bundle);
            case 2:
                return C0905d.m963a((Context) this, fw.PROCESSING, fw.ONE_MOMENT);
            case 3:
                return C0905d.m961a((Activity) this, fw.INTERNAL_ERROR, bundle, i);
            case 4:
                return C0905d.m962a((Activity) this, fw.SESSION_EXPIRED_TITLE, bundle, new C0461t(this));
            default:
                return null;
        }
    }

    protected void onDestroy() {
        new StringBuilder().append(f884d).append(".onDestroy");
        if (this.f886a != null) {
            this.f886a.m585m();
        }
        if (this.f889e) {
            unbindService(this.f895k);
            this.f889e = false;
        }
        super.onDestroy();
    }

    protected void onRestart() {
        super.onRestart();
        m737d();
    }

    protected void onResume() {
        super.onResume();
        new StringBuilder().append(f884d).append(".onResume");
    }

    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("pageTrackingSent", this.f891g);
        bundle.putBoolean("isLoginActivityStarted", this.f892h);
    }
}
