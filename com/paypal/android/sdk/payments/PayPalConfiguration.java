package com.paypal.android.sdk.payments;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import com.paypal.android.sdk.C0438a;
import com.paypal.android.sdk.C0441d;
import com.paypal.android.sdk.C0443e;
import com.paypal.android.sdk.bp;

public final class PayPalConfiguration implements Parcelable {
    public static final Creator CREATOR = new bj();
    public static final String ENVIRONMENT_NO_NETWORK = "mock";
    public static final String ENVIRONMENT_PRODUCTION = "live";
    public static final String ENVIRONMENT_SANDBOX = "sandbox";
    private static final String f617a = PayPalConfiguration.class.getSimpleName();
    private String f618b;
    private String f619c;
    private String f620d;
    private String f621e;
    private String f622f;
    private boolean f623g;
    private String f624h;
    private String f625i;
    private boolean f626j;
    private String f627k;
    private String f628l;
    private Uri f629m;
    private Uri f630n;
    private boolean f631o;

    public PayPalConfiguration() {
        this.f626j = C0905d.m979d();
        this.f631o = true;
    }

    private PayPalConfiguration(Parcel parcel) {
        boolean z = true;
        this.f626j = C0905d.m979d();
        this.f631o = true;
        this.f619c = parcel.readString();
        this.f618b = parcel.readString();
        this.f620d = parcel.readString();
        this.f621e = parcel.readString();
        this.f622f = parcel.readString();
        this.f623g = parcel.readByte() == (byte) 1;
        this.f624h = parcel.readString();
        this.f625i = parcel.readString();
        this.f626j = parcel.readByte() == (byte) 1;
        this.f627k = parcel.readString();
        this.f628l = parcel.readString();
        this.f629m = (Uri) parcel.readParcelable(Uri.class.getClassLoader());
        this.f630n = (Uri) parcel.readParcelable(Uri.class.getClassLoader());
        if (parcel.readByte() != (byte) 1) {
            z = false;
        }
        this.f631o = z;
    }

    private static void m479a(boolean z, String str) {
        if (!z) {
            Log.e(f617a, str + " is invalid.  Please see the docs.");
        }
    }

    public static final String getApplicationCorrelationId(Context context) {
        return getClientMetadataId(context);
    }

    public static final String getClientMetadataId(Context context) {
        C0905d c0905d = new C0905d();
        return C0443e.m304a(PayPalService.f668a, context, new C0438a(context, "AndroidBasePrefs", new C0441d()).m35e(), "2.14.2", null);
    }

    public static final String getLibraryVersion() {
        return "2.14.2";
    }

    final String m480a() {
        return this.f618b;
    }

    public final PayPalConfiguration acceptCreditCards(boolean z) {
        this.f626j = z;
        return this;
    }

    final String m481b() {
        if (C0441d.m262a(this.f619c)) {
            this.f619c = ENVIRONMENT_PRODUCTION;
            Log.w("paypal.sdk", "defaulting to production environment");
        }
        return this.f619c;
    }

    final String m482c() {
        return this.f620d;
    }

    public final PayPalConfiguration clientId(String str) {
        this.f627k = str;
        return this;
    }

    final String m483d() {
        return this.f621e;
    }

    public final PayPalConfiguration defaultUserEmail(String str) {
        this.f620d = str;
        return this;
    }

    public final PayPalConfiguration defaultUserPhone(String str) {
        this.f621e = str;
        return this;
    }

    public final PayPalConfiguration defaultUserPhoneCountryCode(String str) {
        this.f622f = str;
        return this;
    }

    public final int describeContents() {
        return 0;
    }

    final String m484e() {
        return this.f622f;
    }

    public final PayPalConfiguration environment(String str) {
        this.f619c = str;
        return this;
    }

    final boolean m485f() {
        return this.f623g;
    }

    public final PayPalConfiguration forceDefaultsOnSandbox(boolean z) {
        this.f623g = z;
        return this;
    }

    final String m486g() {
        return this.f624h;
    }

    final String m487h() {
        return this.f625i;
    }

    final boolean m488i() {
        return this.f626j;
    }

    final boolean m489j() {
        return this.f631o;
    }

    final String m490k() {
        return this.f627k;
    }

    final String m491l() {
        return this.f628l;
    }

    public final PayPalConfiguration languageOrLocale(String str) {
        this.f618b = str;
        return this;
    }

    final Uri m492m() {
        return this.f629m;
    }

    public final PayPalConfiguration merchantName(String str) {
        this.f628l = str;
        return this;
    }

    public final PayPalConfiguration merchantPrivacyPolicyUri(Uri uri) {
        this.f629m = uri;
        return this;
    }

    public final PayPalConfiguration merchantUserAgreementUri(Uri uri) {
        this.f630n = uri;
        return this;
    }

    final Uri m493n() {
        return this.f630n;
    }

    final boolean m494o() {
        boolean z;
        boolean a = C0441d.m263a(f617a, m481b(), "environment");
        m479a(a, "environment");
        if (!a) {
            z = false;
        } else if (bp.m160a(m481b())) {
            z = true;
        } else {
            z = C0441d.m263a(f617a, this.f627k, "clientId");
            m479a(z, "clientId");
        }
        return a && z;
    }

    public final PayPalConfiguration rememberUser(boolean z) {
        this.f631o = z;
        return this;
    }

    public final PayPalConfiguration sandboxUserPassword(String str) {
        this.f624h = str;
        return this;
    }

    public final PayPalConfiguration sandboxUserPin(String str) {
        this.f625i = str;
        return this;
    }

    public final String toString() {
        return String.format(PayPalConfiguration.class.getSimpleName() + ": {environment:%s, client_id:%s, languageOrLocale:%s}", new Object[]{this.f619c, this.f627k, this.f618b});
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int i2 = 1;
        parcel.writeString(this.f619c);
        parcel.writeString(this.f618b);
        parcel.writeString(this.f620d);
        parcel.writeString(this.f621e);
        parcel.writeString(this.f622f);
        parcel.writeByte((byte) (this.f623g ? 1 : 0));
        parcel.writeString(this.f624h);
        parcel.writeString(this.f625i);
        parcel.writeByte((byte) (this.f626j ? 1 : 0));
        parcel.writeString(this.f627k);
        parcel.writeString(this.f628l);
        parcel.writeParcelable(this.f629m, 0);
        parcel.writeParcelable(this.f630n, 0);
        if (!this.f631o) {
            i2 = 0;
        }
        parcel.writeByte((byte) i2);
    }
}
