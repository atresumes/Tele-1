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
import java.util.Date;
import java.util.Timer;

public final class PayPalProfileSharingActivity extends Activity {
    public static final String EXTRA_REQUESTED_SCOPES = "com.paypal.android.sdk.requested_scopes";
    public static final String EXTRA_RESULT_AUTHORIZATION = "com.paypal.android.sdk.authorization";
    public static final int RESULT_EXTRAS_INVALID = 2;
    private static final String f662a = PayPalProfileSharingActivity.class.getSimpleName();
    private Date f663b;
    private Timer f664c;
    private PayPalService f665d;
    private final ServiceConnection f666e = new bu(this);
    private boolean f667f;

    public final void finish() {
        super.finish();
        new StringBuilder().append(getClass().getSimpleName()).append(".finish");
    }

    protected final void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        new StringBuilder().append(getClass().getSimpleName()).append(".onActivityResult");
        if (i == 1) {
            switch (i2) {
                case -1:
                    if (intent == null) {
                        Log.e(f662a, "result was OK, no intent data, oops");
                        break;
                    }
                    PayPalAuthorization payPalAuthorization = (PayPalAuthorization) intent.getParcelableExtra("com.paypal.android.sdk.authorization");
                    if (payPalAuthorization == null) {
                        Log.e(f662a, "result was OK, have data, but no authorization state in bundle, oops");
                        break;
                    }
                    Intent intent2 = new Intent();
                    intent2.putExtra("com.paypal.android.sdk.authorization", payPalAuthorization);
                    setResult(-1, intent2);
                    break;
                case 0:
                    break;
                default:
                    Log.wtf(f662a, "unexpected request code " + i + " call it a cancel");
                    break;
            }
        }
        finish();
    }

    protected final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        new StringBuilder().append(getClass().getSimpleName()).append(".onCreate");
        new gs(this).m437a();
        new gr(this).m435a();
        new gq(this).m434a(Arrays.asList(new String[]{PayPalProfileSharingActivity.class.getName(), LoginActivity.class.getName(), FuturePaymentInfoActivity.class.getName(), ProfileSharingConsentActivity.class.getName()}));
        this.f667f = bindService(C0905d.m977b((Activity) this), this.f666e, 1);
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
                return C0905d.m959a((Activity) this, new bt(this));
            case 3:
                return C0905d.m961a((Activity) this, fw.UNAUTHORIZED_MERCHANT_TITLE, bundle, i);
            default:
                return C0905d.m961a((Activity) this, fw.UNAUTHORIZED_DEVICE_TITLE, bundle, i);
        }
    }

    protected final void onDestroy() {
        new StringBuilder().append(getClass().getSimpleName()).append(".onDestroy");
        if (this.f665d != null) {
            this.f665d.m587o();
            this.f665d.m593u();
        }
        if (this.f667f) {
            unbindService(this.f666e);
            this.f667f = false;
        }
        super.onDestroy();
    }
}
