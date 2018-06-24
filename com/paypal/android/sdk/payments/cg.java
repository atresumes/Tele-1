package com.paypal.android.sdk.payments;

import android.util.Log;
import com.paypal.android.sdk.C0442do;
import com.paypal.android.sdk.cw;
import com.paypal.android.sdk.de;
import com.paypal.android.sdk.di;
import com.paypal.android.sdk.dm;
import com.paypal.android.sdk.dt;
import com.paypal.android.sdk.dw;
import com.paypal.android.sdk.dy;
import com.paypal.android.sdk.ed;
import com.paypal.android.sdk.ee;
import com.paypal.android.sdk.ep;
import com.paypal.android.sdk.fc;
import com.paypal.android.sdk.fd;
import com.paypal.android.sdk.ff;
import com.paypal.android.sdk.fg;
import com.paypal.android.sdk.fh;
import com.paypal.android.sdk.fi;
import com.paypal.android.sdk.fk;
import com.paypal.android.sdk.fl;
import com.paypal.android.sdk.fm;
import com.paypal.android.sdk.fr;
import com.paypal.android.sdk.fs;
import java.util.UUID;

final class cg implements ee {
    private /* synthetic */ PayPalService f1056a;

    private cg(PayPalService payPalService) {
        this.f1056a = payPalService;
    }

    private static dw m928a(String str, String str2, long j) {
        return new dw(str, str2, System.currentTimeMillis() + (1000 * j), true);
    }

    public final void mo2189a() {
        PayPalService.f669c;
    }

    public final void mo2190a(fd fdVar) {
        PayPalService.f669c;
        this.f1056a.m565a(fc.PaymentSuccessful, fdVar.m204j(), fdVar.m1100t());
        if (fdVar.m1101u() && (this.f1056a.m575c().f300g == null || this.f1056a.m575c().f300g.m855a())) {
            di.m283a(this.f1056a.m575c().f300g, this.f1056a.m577e());
            if (this.f1056a.m575c().f297d != null) {
                this.f1056a.f684p.m288a(this.f1056a.m575c().f297d);
            }
        } else {
            this.f1056a.m580h();
        }
        this.f1056a.f677i.m668a(C0905d.m967a((cw) fdVar));
        this.f1056a.f677i.m665a();
    }

    public final void mo2191a(ff ffVar) {
        PayPalService.f669c;
        this.f1056a.m564a(fc.AuthorizationSuccessful, ffVar.m204j());
        this.f1056a.f677i.m668a(ffVar.f1122a);
        this.f1056a.f677i.m665a();
    }

    public final void mo2192a(fg fgVar) {
        PayPalService.f669c;
        this.f1056a.f677i.m668a((Object) fgVar);
        this.f1056a.f677i.m665a();
    }

    public final void mo2193a(fh fhVar) {
        PayPalService.f669c;
        this.f1056a.m565a(fc.PaymentSuccessful, fhVar.m204j(), fhVar.m1127D());
        this.f1056a.f677i.m668a(C0905d.m967a((cw) fhVar));
        Object obj = (fhVar.m1152t() == null || !fhVar.m1124A()) ? 1 : null;
        if (obj != null) {
            if (!fhVar.m1124A()) {
                this.f1056a.m592t();
            }
            this.f1056a.f677i.m665a();
            return;
        }
        this.f1056a.m573b().m828b(new fs(this.f1056a.m573b(), this.f1056a.m555a(), this.f1056a.m575c().f295b.m306c(), UUID.randomUUID().toString(), fhVar.m1153u(), fhVar.m1152t(), fhVar.m1154v(), fhVar.m1155w(), fhVar.m1156x()));
    }

    public final void mo2194a(fi fiVar) {
        String b = fiVar.m209p().m173b();
        PayPalService.f669c;
        new StringBuilder("DeleteCreditCardRequest Error:").append(b);
        Log.e("paypal.sdk", b);
    }

    public final void mo2195a(fk fkVar) {
        PayPalService.f669c;
        this.f1056a.f677i.m668a((Object) fkVar);
        this.f1056a.f677i.m665a();
    }

    public final void mo2196a(fl flVar) {
        PayPalService.f669c;
        this.f1056a.m575c().f302i = flVar.g;
        this.f1056a.f676h.m665a();
    }

    public final void mo2197a(fm fmVar) {
        long j = 840;
        PayPalService.f669c;
        long j2 = fmVar.f1132f;
        if (j2 <= 840) {
            j = j2;
        }
        if (fmVar.f1129c == null) {
            this.f1056a.m575c().f300g = m928a(fmVar.f1130d, fmVar.f1131e, j);
        } else {
            this.f1056a.m575c().f302i = fmVar.g;
            this.f1056a.m575c().f298e = new dy(fmVar.f1129c, fmVar.f1131e);
        }
        this.f1056a.m575c().f299f = null;
        dm dmVar = new dm();
        if (fmVar.f1127a.m340a()) {
            dmVar.m295a(fmVar.f1127a.m341b());
            dmVar.m293a(C0442do.EMAIL);
        } else {
            dmVar.m294a(fmVar.f1127a.m343d());
            dmVar.m293a(C0442do.PHONE);
        }
        this.f1056a.m575c().f297d = dmVar;
        if (fmVar.f1127a.m340a()) {
            this.f1056a.m575c().f296c = fmVar.f1127a.m341b();
            this.f1056a.m563a(fc.LoginPassword, Boolean.valueOf(fmVar.f1128b), fmVar.m204j());
        } else {
            this.f1056a.m575c().f296c = fmVar.f1127a.m343d().m360a(de.m839a());
            this.f1056a.m563a(fc.LoginPIN, Boolean.valueOf(fmVar.f1128b), fmVar.m204j());
        }
        this.f1056a.f676h.m665a();
    }

    public final void mo2198a(fr frVar) {
        PayPalService.f669c;
        this.f1056a.m564a(fc.DeviceCheck, frVar.m204j());
        this.f1056a.m575c().f295b = m928a(frVar.f1138a, frVar.f1139b, frVar.f1140c);
        this.f1056a.m575c().f301h = frVar.f1141d;
        this.f1056a.f675g = false;
        if (this.f1056a.f681m != null) {
            PayPalService.f669c;
            this.f1056a.f681m.mo2185a();
            this.f1056a.f681m = null;
        }
    }

    public final void mo2199a(fs fsVar) {
        PayPalService.f669c;
        this.f1056a.f684p.m289a(new dt(this.f1056a.f683o, fsVar.m1141t(), fsVar.f1181a, fsVar.m1143v(), fsVar.m1142u(), fsVar.m1144w(), fsVar.m1145x(), fsVar.m1146y()), this.f1056a.f674f.m490k());
        this.f1056a.f677i.m665a();
    }

    public final void mo2200b(fd fdVar) {
        String b = fdVar.m209p().m173b();
        PayPalService.f669c;
        new StringBuilder("ApproveAndExecuteSfoPaymentRequest Error: ").append(b);
        PayPalService.m549c(this.f1056a, fdVar);
    }

    public final void mo2201b(ff ffVar) {
        String b = ffVar.m209p().m173b();
        PayPalService.f669c;
        new StringBuilder("ConsentRequest Error:").append(b);
        Log.e("paypal.sdk", b);
        this.f1056a.f677i.m667a(this.f1056a.m544b((cw) ffVar));
    }

    public final void mo2202b(fg fgVar) {
        String b = fgVar.m209p().m173b();
        PayPalService.f669c;
        new StringBuilder("CreateSfoPaymentRequest Error: ").append(b);
        Log.e("paypal.sdk", b);
        this.f1056a.f677i.m667a(this.f1056a.m544b((cw) fgVar));
    }

    public final void mo2203b(fh fhVar) {
        String b = fhVar.m209p().m173b();
        PayPalService.f669c;
        new StringBuilder("CreditCardPaymentRequest Error:").append(b);
        PayPalService.m549c(this.f1056a, fhVar);
    }

    public final void mo2204b(fk fkVar) {
        String b = fkVar.m209p().m173b();
        PayPalService.f669c;
        new StringBuilder("GetAppInfoRequest Error: ").append(b);
        Log.e("paypal.sdk", b);
        this.f1056a.f677i.m667a(this.f1056a.m544b((cw) fkVar));
    }

    public final void mo2205b(fl flVar) {
        String b = flVar.m209p().m173b();
        PayPalService.f669c;
        new StringBuilder("LoginChallengeRequest Error:").append(b);
        Log.e("paypal.sdk", b);
        this.f1056a.f676h.m667a(this.f1056a.m544b((cw) flVar));
    }

    public final void mo2206b(fm fmVar) {
        String b = fmVar.m209p().m173b();
        PayPalService.f669c;
        new StringBuilder("LoginRequest Error: ").append(b);
        Log.e("paypal.sdk", b);
        this.f1056a.m580h();
        ep epVar = fmVar.f1127a;
        boolean z = fmVar.f1128b;
        if (epVar.m340a()) {
            this.f1056a.m537a(fc.LoginPassword, z, b, fmVar.m204j(), null);
        } else {
            this.f1056a.m537a(fc.LoginPIN, z, b, fmVar.m204j(), null);
        }
        this.f1056a.f684p.m290b();
        if (fmVar.h) {
            this.f1056a.m575c().f302i = fmVar.g;
            this.f1056a.m575c().f299f = new ed(fmVar.i);
        }
        this.f1056a.f676h.m667a(this.f1056a.m544b((cw) fmVar));
    }

    public final void mo2207b(fr frVar) {
        PayPalService.m538a(this.f1056a, (cw) frVar);
    }

    public final void mo2208b(fs fsVar) {
        String b = fsVar.m209p().m173b();
        PayPalService.f669c;
        new StringBuilder("TokenizeCreditCardRequest Error:").append(b);
        Log.e("paypal.sdk", b);
        this.f1056a.f677i.m665a();
    }
}
