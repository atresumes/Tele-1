package com.paypal.android.sdk.payments;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build.VERSION;
import android.os.IBinder;
import android.util.Log;
import com.paypal.android.sdk.C0438a;
import com.paypal.android.sdk.C0439b;
import com.paypal.android.sdk.C0441d;
import com.paypal.android.sdk.C0442do;
import com.paypal.android.sdk.C0443e;
import com.paypal.android.sdk.bp;
import com.paypal.android.sdk.bq;
import com.paypal.android.sdk.br;
import com.paypal.android.sdk.bu;
import com.paypal.android.sdk.cm;
import com.paypal.android.sdk.cs;
import com.paypal.android.sdk.cu;
import com.paypal.android.sdk.cw;
import com.paypal.android.sdk.db;
import com.paypal.android.sdk.dc;
import com.paypal.android.sdk.de;
import com.paypal.android.sdk.dg;
import com.paypal.android.sdk.dh;
import com.paypal.android.sdk.di;
import com.paypal.android.sdk.dl;
import com.paypal.android.sdk.dm;
import com.paypal.android.sdk.dt;
import com.paypal.android.sdk.dw;
import com.paypal.android.sdk.ef;
import com.paypal.android.sdk.eh;
import com.paypal.android.sdk.em;
import com.paypal.android.sdk.ep;
import com.paypal.android.sdk.er;
import com.paypal.android.sdk.fa;
import com.paypal.android.sdk.fc;
import com.paypal.android.sdk.fd;
import com.paypal.android.sdk.ff;
import com.paypal.android.sdk.fg;
import com.paypal.android.sdk.fh;
import com.paypal.android.sdk.fi;
import com.paypal.android.sdk.fj;
import com.paypal.android.sdk.fk;
import com.paypal.android.sdk.fl;
import com.paypal.android.sdk.fm;
import com.paypal.android.sdk.fo;
import com.paypal.android.sdk.fr;
import com.paypal.android.sdk.ft;
import com.paypal.android.sdk.fu;
import com.paypal.android.sdk.gs;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import org.json.JSONObject;

public final class PayPalService extends Service {
    public static final String EXTRA_PAYPAL_CONFIGURATION = "com.paypal.android.sdk.paypalConfiguration";
    static final ExecutorService f668a = db.m276a();
    private static final String f669c = PayPalService.class.getSimpleName();
    private static Intent f670t;
    dt f671b;
    private C0439b f672d;
    private dg f673e;
    private PayPalConfiguration f674f;
    private boolean f675g;
    private C0448a f676h = new C0448a();
    private C0448a f677i = new C0448a();
    private C0455i f678j = new C0906h(this);
    private String f679k;
    private br f680l;
    private ce f681m;
    private String f682n;
    private C0438a f683o;
    private dl f684p;
    private List f685q = new ArrayList();
    private boolean f686r = true;
    private boolean f687s = true;
    private final BroadcastReceiver f688u = new ca(this);
    private final IBinder f689v = new cd(this);

    private boolean m529A() {
        return (this.f674f == null || this.f673e == null) ? false : true;
    }

    private static dg m530B() {
        return new dg();
    }

    private void m531C() {
        m567a(new cb(this), false);
    }

    private static bq m532a(String str, String str2) {
        bq bqVar = new bq(str, str2);
        if (str2 != null) {
            if (str2.startsWith("https://")) {
                if (!str2.endsWith("/")) {
                    new StringBuilder().append(str2).append(" does not end with a slash, adding one.");
                    str2 = str2 + "/";
                }
                for (cu cuVar : dc.m835d()) {
                    bqVar.m167c().put(cuVar.mo2164a(), str2 + cuVar.mo2166c());
                }
            } else {
                throw new RuntimeException(str2 + " does not start with 'https://', ignoring " + str);
            }
        }
        return bqVar;
    }

    private void m535a(Intent intent) {
        f670t = intent;
        new StringBuilder("init:").append(m546b(intent));
        if (this.f674f == null) {
            this.f674f = (PayPalConfiguration) intent.getParcelableExtra(EXTRA_PAYPAL_CONFIGURATION);
            if (this.f674f == null) {
                throw new RuntimeException("Missing EXTRA_PAYPAL_CONFIGURATION. To avoid this error, set EXTRA_PAYPAL_CONFIGURATION in both PayPalService, and the initializing activity.");
            }
        }
        if (!this.f674f.m494o()) {
            throw new RuntimeException("Service extras invalid.  Please check the docs.");
        } else if (!this.f674f.m488i() || C0905d.m979d()) {
            String str;
            String b = this.f674f.m481b();
            if (bp.m162c(b)) {
                str = "https://api-m.paypal.com/v1/";
            } else if (bp.m161b(b)) {
                str = "https://api-m.sandbox.paypal.com/v1/";
            } else if (bp.m160a(b)) {
                str = null;
            } else if (m554z() && intent.hasExtra("com.paypal.android.sdk.baseEnvironmentUrl")) {
                str = intent.getStringExtra("com.paypal.android.sdk.baseEnvironmentUrl");
            } else {
                throw new RuntimeException("Invalid environment selected:" + b);
            }
            this.f684p = new dl(this.f683o, this.f674f.m481b());
            bq a = m532a(b, str);
            if (this.f680l == null) {
                int intExtra = (m554z() && intent.hasExtra("com.paypal.android.sdk.mockNetworkDelay")) ? intent.getIntExtra("com.paypal.android.sdk.mockNetworkDelay", 500) : 500;
                boolean booleanExtra = (m554z() && intent.hasExtra("com.paypal.android.sdk.mockEnable2fa")) ? intent.getBooleanExtra("com.paypal.android.sdk.mockEnable2fa", false) : false;
                int intExtra2 = (m554z() && intent.hasExtra("com.paypal.android.sdk.mock2faPhoneNumberCount")) ? intent.getIntExtra("com.paypal.android.sdk.mock2faPhoneNumberCount", 1) : 1;
                this.f686r = true;
                if (m554z() && intent.hasExtra("com.paypal.android.sdk.enableAuthenticator")) {
                    this.f686r = intent.getBooleanExtra("com.paypal.android.sdk.enableAuthenticator", true);
                }
                if (m554z() && intent.hasExtra("com.paypal.android.sdk.enableAuthenticatorSecurity")) {
                    this.f687s = intent.getBooleanExtra("com.paypal.android.sdk.enableAuthenticatorSecurity", true);
                }
                boolean booleanExtra2 = (m554z() && intent.hasExtra("com.paypal.android.sdk.enableStageSsl")) ? intent.getBooleanExtra("com.paypal.android.sdk.enableStageSsl", true) : true;
                this.f680l = new br(this.f683o, a, m555a());
                this.f680l.m824a(new ef(new cg()));
                this.f680l.m825a(new cs(this.f680l, bp.m160a(this.f674f.m481b()) ? new ft(this.f680l, intExtra, booleanExtra, intExtra2) : new cm(this.f683o, this.f674f.m481b(), m555a(), this.f680l, 90, booleanExtra2, Collections.singletonList(new dh(m555a().mo2211c())))));
            }
            fu.m371b(this.f674f.m480a());
            if (this.f673e == null) {
                this.f673e = m530B();
            }
            if (!this.f674f.m489j()) {
                clearAllUserData(this.f683o.m36f());
            }
            this.f679k = intent.getComponent().getPackageName();
            m561a(fc.PreConnect);
            m531C();
        } else {
            throw new RuntimeException("Credit Cards cannot be accepted without card.io dependency. Please check the docs.");
        }
    }

    private void m536a(cw cwVar) {
        this.f680l.m828b(cwVar);
    }

    private void m537a(fc fcVar, boolean z, String str, String str2, String str3) {
        this.f678j.m719a(fcVar, z, str, str2, str3);
    }

    static /* synthetic */ void m538a(PayPalService payPalService, cw cwVar) {
        payPalService.f673e.f295b = null;
        new StringBuilder().append(cwVar.m207n()).append(" request error");
        String b = cwVar.m209p().m173b();
        Log.e("paypal.sdk", b);
        payPalService.m547b(fc.DeviceCheck, b, cwVar.m204j());
        if (payPalService.f681m != null) {
            payPalService.f681m.mo2186a(payPalService.m544b(cwVar));
            payPalService.f681m = null;
        }
        payPalService.f675g = false;
    }

    private static boolean m540a(dw dwVar) {
        return dwVar != null && dwVar.m305b();
    }

    private fo[] m542a(PayPalItem[] payPalItemArr) {
        if (payPalItemArr == null) {
            return null;
        }
        fo[] foVarArr = new fo[payPalItemArr.length];
        int length = payPalItemArr.length;
        int i = 0;
        int i2 = 0;
        while (i < length) {
            PayPalItem payPalItem = payPalItemArr[i];
            int i3 = i2 + 1;
            foVarArr[i2] = new fo(payPalItem.getName(), payPalItem.getQuantity(), payPalItem.getPrice(), payPalItem.getCurrency(), payPalItem.getSku());
            i++;
            i2 = i3;
        }
        return foVarArr;
    }

    private cf m544b(cw cwVar) {
        return new cf(this, cwVar.m209p().m173b(), cwVar.m211r(), cwVar.m209p().m172a());
    }

    private static String m546b(Intent intent) {
        if (intent == null) {
            return "Intent = null";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Intent{");
        stringBuilder.append("action:" + intent.getAction());
        stringBuilder.append(", cmp:" + intent.getComponent() + ", ");
        if (intent.getExtras() == null) {
            stringBuilder.append("null extras");
        } else {
            stringBuilder.append("extras:");
            for (String str : intent.getExtras().keySet()) {
                stringBuilder.append("(" + str + ":" + intent.getExtras().get(str) + ")");
            }
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    private void m547b(fc fcVar, String str, String str2) {
        m537a(fcVar, false, str, str2, null);
    }

    static /* synthetic */ void m549c(PayPalService payPalService, cw cwVar) {
        String b = cwVar.m209p().m173b();
        Log.e("paypal.sdk", b);
        payPalService.m547b(fc.ConfirmPayment, b, cwVar.m204j());
        payPalService.f677i.m667a(payPalService.m544b(cwVar));
    }

    public static void clearAllUserData(Context context) {
        Log.w("paypal.sdk", "clearing user data");
        f668a.submit(new by(context));
    }

    private static boolean m554z() {
        return "partner".equals(BuildConfig.FLAVOR);
    }

    final C0439b m555a() {
        if (this.f672d == null) {
            this.f672d = new C0905d();
        }
        return this.f672d;
    }

    final String m556a(String str) {
        return this.f683o.m33c(str);
    }

    final void m557a(int i) {
        this.f680l.m828b(new fl(this.f680l, m555a(), this.f680l.mo2158c(), this.f674f.m490k(), this.f673e.f302i, (String) new ArrayList(this.f673e.f299f.f350a.keySet()).get(i)));
    }

    final void m558a(ep epVar, String str, boolean z, String str2, boolean z2, String str3) {
        this.f680l.m828b(new fm(this.f680l, m555a(), this.f680l.mo2158c(), this.f674f.m490k(), epVar, str, this.f673e.f302i, z, str2, z2, str3));
    }

    final void m559a(ep epVar, boolean z, String str, boolean z2, String str2) {
        this.f680l.m828b(new fm(this.f680l, m555a(), this.f680l.mo2158c(), this.f674f.m490k(), epVar, z, str, z2, str2));
    }

    final void m560a(er erVar, Map map, PayPalItem[] payPalItemArr, String str, boolean z, String str2, String str3, boolean z2, String str4, String str5, String str6, boolean z3) {
        this.f680l.m828b(new fg(this.f680l, m555a(), this.f673e.f300g.m306c(), this.f673e.m280b(), null, erVar, map, m542a(payPalItemArr), str, z, str2, this.f682n, str3, z2).m1109d(str4).m1111e(str5).m1113f(str6).m1106a(z3));
    }

    final void m561a(fc fcVar) {
        m537a(fcVar, false, null, null, null);
    }

    final void m562a(fc fcVar, Boolean bool) {
        m537a(fcVar, bool.booleanValue(), null, null, null);
    }

    final void m563a(fc fcVar, Boolean bool, String str) {
        m537a(fcVar, bool.booleanValue(), null, str, null);
    }

    final void m564a(fc fcVar, String str) {
        m537a(fcVar, false, null, str, null);
    }

    final void m565a(fc fcVar, String str, String str2) {
        m537a(fcVar, false, null, str, str2);
    }

    final void m566a(cc ccVar) {
        this.f676h.m666a(ccVar);
    }

    final void m567a(ce ceVar, boolean z) {
        if (z) {
            this.f673e.f295b = null;
        }
        this.f681m = ceVar;
        if (!this.f675g && !this.f673e.m281c()) {
            this.f675g = true;
            m561a(fc.DeviceCheck);
            this.f680l.m828b(new fr(this.f674f.m481b(), this.f680l, m555a(), this.f674f.m490k()));
        }
    }

    final void m568a(String str, String str2, er erVar, Map map, PayPalItem[] payPalItemArr, String str3, boolean z, String str4, String str5, String str6, String str7, String str8, String str9) {
        this.f680l.m828b(new fh(this.f680l, m555a(), this.f673e.f295b.m306c(), str, str2, str4, erVar, map, m542a(payPalItemArr), str3, z, str5, this.f682n, str6).m1148d(str7).m1149e(str8).m1151f(str9));
    }

    final void m569a(String str, String str2, String str3, String str4, int i, int i2, er erVar, Map map, PayPalItem[] payPalItemArr, String str5, boolean z, String str6, String str7, String str8, String str9, String str10) {
        String str11 = (str3.equalsIgnoreCase("4111111111111111") && bp.m161b(this.f674f.m481b())) ? "4444333322221111" : str3;
        this.f680l.m828b(new fh(this.f680l, m555a(), this.f673e.f295b.m306c(), str, str2, str11, str4, i, i2, null, erVar, map, m542a(payPalItemArr), str5, z, str6, this.f682n, str7).m1148d(str8).m1149e(str9).m1151f(str10));
    }

    final void m570a(List list) {
        this.f680l.m828b(new ff(this.f680l, m555a(), this.f680l.mo2158c(), this.f674f.m490k(), this.f673e.f298e.m301a(), this.f673e.f302i, list));
    }

    final void m571a(boolean z, String str, String str2, JSONObject jSONObject, JSONObject jSONObject2, String str3) {
        this.f680l.m828b(new fd(this.f680l, m555a(), this.f673e.f300g.m306c(), this.f673e.m280b(), z, str3, this.f682n, str, str2, jSONObject, jSONObject2));
    }

    protected final boolean m572a(ch chVar) {
        if (m529A()) {
            return true;
        }
        this.f685q.add(chVar);
        return false;
    }

    protected final br m573b() {
        return this.f680l;
    }

    final void m574b(cc ccVar) {
        this.f677i.m666a(ccVar);
    }

    protected final dg m575c() {
        return this.f673e;
    }

    final PayPalConfiguration m576d() {
        return this.f674f;
    }

    public final void doDeleteTokenizedCreditCard(String str, String str2) {
        m536a(new fi(this.f680l, m555a(), str, str2));
    }

    public final void doTrackingRequest(bu buVar) {
        m536a(new fj(this.f680l, m555a(), fa.m364a(buVar)));
    }

    protected final String m577e() {
        return this.f674f.m481b();
    }

    protected final String m578f() {
        return this.f674f.m490k();
    }

    final void m579g() {
        m592t();
        m580h();
        this.f684p.m290b();
        m593u();
    }

    final void m580h() {
        this.f673e.f300g = null;
        di.m284b(this.f674f.m481b());
        this.f673e.f297d = null;
        this.f673e.f296c = null;
    }

    final boolean m581i() {
        return this.f673e.m281c();
    }

    final boolean m582j() {
        dg dgVar = this.f673e;
        return dgVar.f300g != null && dgVar.f300g.m305b();
    }

    final boolean m583k() {
        return (this.f673e.f298e == null || this.f673e.f302i == null) ? false : true;
    }

    final void m584l() {
        dm a = this.f684p.m286a();
        if (a == null) {
            m580h();
            return;
        }
        dw dwVar = this.f673e.f300g;
        dw a2 = di.m282a(this.f674f.m481b());
        if (!m540a(dwVar) && m540a(a2)) {
            this.f673e.f300g = a2;
        }
        dg dgVar = this.f673e;
        String b = a.m298d() ? a.m297c().equals(C0442do.EMAIL) ? a.m296b() : a.m292a().m360a(de.m839a()) : null;
        dgVar.f296c = b;
    }

    final void m585m() {
        this.f677i.m669b();
    }

    final void m586n() {
        this.f676h.m669b();
    }

    final void m587o() {
        this.f681m = null;
    }

    public final IBinder onBind(Intent intent) {
        new StringBuilder("onBind(").append(m546b(intent)).append(")");
        if (!m529A()) {
            if (f670t == null) {
                m535a(intent);
            } else {
                m535a(f670t);
            }
        }
        return this.f689v;
    }

    public final void onCreate() {
        Log.w("paypal.sdk", PayPalService.class.getSimpleName() + " created. API:" + VERSION.SDK_INT + " " + m555a().mo2210b());
        C0905d c0905d = new C0905d();
        this.f683o = new C0438a(this, "AndroidBasePrefs", new C0441d());
        eh.m329a(this.f683o);
        em.m339a(this.f683o);
        this.f682n = C0443e.m304a(f668a, this, this.f683o.m35e(), "2.14.2", null);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.paypal.android.sdk.clearAllUserData");
        aa.m670a((Context) this).m673a(this.f688u, intentFilter);
    }

    public final void onDestroy() {
        if (this.f680l != null) {
            this.f680l.m823a();
            this.f680l.m827b();
            this.f680l = null;
        }
        try {
            aa.m670a((Context) this).m672a(this.f688u);
        } catch (Throwable th) {
            new StringBuilder("ignoring:").append(th.getMessage());
        }
        new StringBuilder("service destroyed: ").append(this);
    }

    public final void onRebind(Intent intent) {
        super.onRebind(intent);
        new StringBuilder("onRebind(").append(m546b(intent)).append(")");
    }

    public final int onStartCommand(Intent intent, int i, int i2) {
        new StringBuilder("onStartCommand(").append(m546b(intent)).append(", ").append(i).append(", ").append(i2).append(")");
        if (!m529A()) {
            new gs(this).m437a();
            if (intent == null || intent.getExtras() == null) {
                throw new RuntimeException("Service extras required. Please see the docs.");
            }
            m535a(intent);
        }
        if (this.f685q.size() > 0) {
            for (ch a : this.f685q) {
                a.mo2184a();
            }
            this.f685q.clear();
        }
        return 3;
    }

    public final boolean onUnbind(Intent intent) {
        new StringBuilder("onUnbind(").append(m546b(intent)).append(")");
        return true;
    }

    final void m588p() {
        this.f680l.m828b(new fk(this.f680l, m555a(), this.f680l.mo2158c(), this.f673e.f295b.m306c(), this.f674f.m490k()));
    }

    final dm m589q() {
        return this.f684p.m286a();
    }

    final String m590r() {
        return this.f673e.f296c;
    }

    final dt m591s() {
        return this.f684p.m287a(this.f674f.m490k());
    }

    final void m592t() {
        this.f671b = m591s();
        this.f684p.m291c();
        if (this.f671b != null && this.f673e.f295b != null) {
            doDeleteTokenizedCreditCard(this.f673e.f295b.m306c(), this.f671b.m851e());
            this.f671b = null;
        }
    }

    final void m593u() {
        if (this.f674f != null && this.f674f.m494o()) {
            this.f673e = m530B();
            m531C();
        }
    }

    protected final String m594v() {
        return this.f679k;
    }

    final boolean m595w() {
        return this.f686r;
    }

    final boolean m596x() {
        return this.f687s;
    }
}
