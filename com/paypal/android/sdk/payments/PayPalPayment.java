package com.paypal.android.sdk.payments;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import com.payUMoney.sdk.SdkConstants;
import com.paypal.android.sdk.C0441d;
import com.paypal.android.sdk.de;
import com.paypal.android.sdk.ek;
import java.math.BigDecimal;
import java.util.Locale;
import org.json.JSONObject;

public final class PayPalPayment implements Parcelable {
    public static final Creator CREATOR = new br();
    public static final String PAYMENT_INTENT_AUTHORIZE = "authorize";
    public static final String PAYMENT_INTENT_ORDER = "order";
    public static final String PAYMENT_INTENT_SALE = "sale";
    private static final String f645a = PayPalPayment.class.getSimpleName();
    private BigDecimal f646b;
    private String f647c;
    private String f648d;
    private String f649e;
    private PayPalPaymentDetails f650f;
    private String f651g;
    private PayPalItem[] f652h;
    private boolean f653i;
    private ShippingAddress f654j;
    private String f655k;
    private String f656l;
    private String f657m;

    private PayPalPayment(Parcel parcel) {
        this.f647c = parcel.readString();
        try {
            this.f646b = new BigDecimal(parcel.readString());
        } catch (NumberFormatException e) {
        }
        this.f648d = parcel.readString();
        this.f651g = parcel.readString();
        this.f649e = parcel.readString();
        this.f650f = (PayPalPaymentDetails) parcel.readParcelable(PayPalPaymentDetails.class.getClassLoader());
        int readInt = parcel.readInt();
        if (readInt > 0) {
            this.f652h = new PayPalItem[readInt];
            parcel.readTypedArray(this.f652h, PayPalItem.CREATOR);
        }
        this.f654j = (ShippingAddress) parcel.readParcelable(ShippingAddress.class.getClassLoader());
        this.f653i = parcel.readInt() == 1;
        this.f655k = parcel.readString();
        this.f656l = parcel.readString();
        this.f657m = parcel.readString();
    }

    public PayPalPayment(BigDecimal bigDecimal, String str, String str2, String str3) {
        this.f646b = bigDecimal;
        this.f647c = str;
        this.f648d = str2;
        this.f651g = str3;
        this.f650f = null;
        this.f649e = null;
        toString();
    }

    private static void m508a(boolean z, String str) {
        if (!z) {
            Log.e("paypal.sdk", str + " is invalid.  Please see the docs.");
        }
    }

    private static boolean m509a(String str, String str2, int i) {
        if (!C0441d.m269d(str) || str.length() <= i) {
            return true;
        }
        Log.e("paypal.sdk", str2 + " is too long (max " + i + ")");
        return false;
    }

    protected final BigDecimal m510a() {
        return this.f646b;
    }

    protected final String m511b() {
        return this.f648d;
    }

    public final PayPalPayment bnCode(String str) {
        this.f649e = str;
        return this;
    }

    protected final String m512c() {
        return this.f651g;
    }

    public final PayPalPayment custom(String str) {
        this.f656l = str;
        return this;
    }

    protected final String m513d() {
        return this.f647c;
    }

    public final int describeContents() {
        return 0;
    }

    protected final String m514e() {
        return this.f649e;
    }

    public final PayPalPayment enablePayPalShippingAddressesRetrieval(boolean z) {
        this.f653i = z;
        return this;
    }

    protected final PayPalPaymentDetails m515f() {
        return this.f650f;
    }

    protected final PayPalItem[] m516g() {
        return this.f652h;
    }

    public final String getAmountAsLocalizedString() {
        if (this.f646b == null) {
            return null;
        }
        return ek.m335a(Locale.getDefault(), de.m839a().mo2169c().m330a(), this.f646b.doubleValue(), this.f647c, true);
    }

    public final ShippingAddress getProvidedShippingAddress() {
        return this.f654j;
    }

    protected final String m517h() {
        return this.f655k;
    }

    protected final String m518i() {
        return this.f656l;
    }

    public final PayPalPayment invoiceNumber(String str) {
        this.f655k = str;
        return this;
    }

    public final boolean isEnablePayPalShippingAddressesRetrieval() {
        return this.f653i;
    }

    public final boolean isNoShipping() {
        return !this.f653i && this.f654j == null;
    }

    public final boolean isProcessable() {
        boolean z;
        boolean a = ek.m336a(this.f647c);
        boolean a2 = ek.m337a(this.f646b, this.f647c, true);
        boolean b = C0441d.m266b(this.f648d);
        boolean z2 = C0441d.m269d(this.f651g) && (this.f651g.equals(PAYMENT_INTENT_SALE) || this.f651g.equals(PAYMENT_INTENT_AUTHORIZE) || this.f651g.equals(PAYMENT_INTENT_ORDER));
        boolean isProcessable = this.f650f == null ? true : this.f650f.isProcessable();
        boolean c = C0441d.m267c(this.f649e) ? true : C0441d.m268c(this.f649e);
        if (this.f652h == null || this.f652h.length == 0) {
            z = true;
        } else {
            for (PayPalItem isValid : this.f652h) {
                if (!isValid.isValid()) {
                    z = false;
                    break;
                }
            }
            z = true;
        }
        boolean z3 = m509a(this.f655k, "invoiceNumber", 256);
        if (!m509a(this.f656l, "custom", 256)) {
            z3 = false;
        }
        if (!m509a(this.f657m, "softDescriptor", 22)) {
            z3 = false;
        }
        m508a(a, "currencyCode");
        m508a(a2, SdkConstants.AMOUNT);
        m508a(b, "shortDescription");
        m508a(z2, "paymentIntent");
        m508a(isProcessable, "details");
        m508a(c, "bnCode");
        m508a(z, "items");
        return a && a2 && b && isProcessable && z2 && c && z && z3;
    }

    public final PayPalPayment items(PayPalItem[] payPalItemArr) {
        this.f652h = payPalItemArr;
        return this;
    }

    protected final String m519j() {
        return this.f657m;
    }

    public final PayPalPayment paymentDetails(PayPalPaymentDetails payPalPaymentDetails) {
        this.f650f = payPalPaymentDetails;
        return this;
    }

    public final PayPalPayment providedShippingAddress(ShippingAddress shippingAddress) {
        this.f654j = shippingAddress;
        return this;
    }

    public final PayPalPayment softDescriptor(String str) {
        this.f657m = str;
        return this;
    }

    public final JSONObject toJSONObject() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(SdkConstants.AMOUNT, this.f646b.toPlainString());
            jSONObject.put("currency_code", this.f647c);
            if (this.f650f != null) {
                jSONObject.put("details", this.f650f.toJSONObject());
            }
            jSONObject.put("short_description", this.f648d);
            jSONObject.put("intent", this.f651g.toString());
            if (C0441d.m269d(this.f649e)) {
                jSONObject.put("bn_code", this.f649e);
            }
            if (this.f652h == null || this.f652h.length <= 0) {
                return jSONObject;
            }
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.accumulate("items", PayPalItem.toJSONArray(this.f652h));
            jSONObject.put("item_list", jSONObject2);
            return jSONObject;
        } catch (Throwable e) {
            Log.e("paypal.sdk", "error encoding JSON", e);
            return null;
        }
    }

    public final String toString() {
        String str = "PayPalPayment: {%s: $%s %s, %s}";
        Object[] objArr = new Object[4];
        objArr[0] = this.f648d;
        objArr[1] = this.f646b != null ? this.f646b.toString() : null;
        objArr[2] = this.f647c;
        objArr[3] = this.f651g;
        return String.format(str, objArr);
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int i2 = 0;
        parcel.writeString(this.f647c);
        parcel.writeString(this.f646b.toString());
        parcel.writeString(this.f648d);
        parcel.writeString(this.f651g);
        parcel.writeString(this.f649e);
        parcel.writeParcelable(this.f650f, 0);
        if (this.f652h != null) {
            parcel.writeInt(this.f652h.length);
            parcel.writeTypedArray(this.f652h, 0);
        } else {
            parcel.writeInt(0);
        }
        parcel.writeParcelable(this.f654j, 0);
        if (this.f653i) {
            i2 = 1;
        }
        parcel.writeInt(i2);
        parcel.writeString(this.f655k);
        parcel.writeString(this.f656l);
        parcel.writeString(this.f657m);
    }
}
