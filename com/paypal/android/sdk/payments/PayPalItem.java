package com.paypal.android.sdk.payments;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import com.paypal.android.sdk.C0441d;
import com.paypal.android.sdk.ek;
import java.math.BigDecimal;
import org.json.JSONArray;
import org.json.JSONObject;

public final class PayPalItem implements Parcelable {
    public static final Creator CREATOR = new bp();
    private static final String f638a = PayPalItem.class.getSimpleName();
    private final String f639b;
    private final Integer f640c;
    private final BigDecimal f641d;
    private final String f642e;
    private final String f643f;

    private PayPalItem(Parcel parcel) {
        this.f639b = parcel.readString();
        this.f640c = Integer.valueOf(parcel.readInt());
        try {
            this.f641d = new BigDecimal(parcel.readString());
            this.f642e = parcel.readString();
            this.f643f = parcel.readString();
        } catch (Throwable e) {
            Log.e(f638a, "bad price", e);
            throw new RuntimeException(e);
        }
    }

    public PayPalItem(String str, Integer num, BigDecimal bigDecimal, String str2, String str3) {
        this.f639b = str;
        this.f640c = num;
        this.f641d = bigDecimal;
        this.f642e = str2;
        this.f643f = str3;
    }

    public static BigDecimal getItemTotal(PayPalItem[] payPalItemArr) {
        BigDecimal bigDecimal = new BigDecimal("0.00");
        for (PayPalItem payPalItem : payPalItemArr) {
            bigDecimal = bigDecimal.add(payPalItem.f641d.multiply(BigDecimal.valueOf((long) payPalItem.f640c.intValue())));
        }
        return bigDecimal;
    }

    public static JSONArray toJSONArray(PayPalItem[] payPalItemArr) {
        JSONArray jSONArray = new JSONArray();
        for (PayPalItem payPalItem : payPalItemArr) {
            JSONObject jSONObject = new JSONObject();
            jSONObject.accumulate(Param.QUANTITY, Integer.toString(payPalItem.f640c.intValue()));
            jSONObject.accumulate("name", payPalItem.f639b);
            jSONObject.accumulate(Param.PRICE, payPalItem.f641d.toString());
            jSONObject.accumulate(Param.CURRENCY, payPalItem.f642e);
            if (payPalItem.f643f != null) {
                jSONObject.accumulate("sku", payPalItem.f643f);
            }
            jSONArray.put(jSONObject);
        }
        return jSONArray;
    }

    public final int describeContents() {
        return 0;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof PayPalItem)) {
            return false;
        }
        PayPalItem payPalItem = (PayPalItem) obj;
        String name = getName();
        String name2 = payPalItem.getName();
        if (name != null ? !name.equals(name2) : name2 != null) {
            return false;
        }
        Integer quantity = getQuantity();
        Integer quantity2 = payPalItem.getQuantity();
        if (quantity != null ? !quantity.equals(quantity2) : quantity2 != null) {
            return false;
        }
        BigDecimal price = getPrice();
        BigDecimal price2 = payPalItem.getPrice();
        if (price != null ? !price.equals(price2) : price2 != null) {
            return false;
        }
        name = getCurrency();
        name2 = payPalItem.getCurrency();
        if (name != null ? !name.equals(name2) : name2 != null) {
            return false;
        }
        name = getSku();
        name2 = payPalItem.getSku();
        if (name == null) {
            if (name2 == null) {
                return true;
            }
        } else if (name.equals(name2)) {
            return true;
        }
        return false;
    }

    public final String getCurrency() {
        return this.f642e;
    }

    public final String getName() {
        return this.f639b;
    }

    public final BigDecimal getPrice() {
        return this.f641d;
    }

    public final Integer getQuantity() {
        return this.f640c;
    }

    public final String getSku() {
        return this.f643f;
    }

    public final int hashCode() {
        int i = 43;
        String name = getName();
        int hashCode = (name == null ? 43 : name.hashCode()) + 59;
        Integer quantity = getQuantity();
        hashCode = (quantity == null ? 43 : quantity.hashCode()) + (hashCode * 59);
        BigDecimal price = getPrice();
        hashCode = (price == null ? 43 : price.hashCode()) + (hashCode * 59);
        String currency = getCurrency();
        hashCode = (currency == null ? 43 : currency.hashCode()) + (hashCode * 59);
        currency = getSku();
        hashCode *= 59;
        if (currency != null) {
            i = currency.hashCode();
        }
        return hashCode + i;
    }

    public final boolean isValid() {
        if (this.f640c.intValue() <= 0) {
            Log.e("paypal.sdk", "item.quantity must be a positive integer.");
            return false;
        } else if (!ek.m336a(this.f642e)) {
            Log.e("paypal.sdk", "item.currency field is required, and must be a supported currency.");
            return false;
        } else if (C0441d.m267c(this.f639b)) {
            Log.e("paypal.sdk", "item.name field is required.");
            return false;
        } else if (ek.m337a(this.f641d, this.f642e, false)) {
            return true;
        } else {
            Log.e("paypal.sdk", "item.price field is required.");
            return false;
        }
    }

    public final String toString() {
        return "PayPalItem(name=" + getName() + ", quantity=" + getQuantity() + ", price=" + getPrice() + ", currency=" + getCurrency() + ", sku=" + getSku() + ")";
    }

    public final void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.f639b);
        parcel.writeInt(this.f640c.intValue());
        parcel.writeString(this.f641d.toString());
        parcel.writeString(this.f642e);
        parcel.writeString(this.f643f);
    }
}
