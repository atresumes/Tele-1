package com.paypal.android.sdk;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.payUMoney.sdk.SdkConstants;
import java.util.Random;

public final class ey extends ea implements Parcelable {
    public static final Creator CREATOR = new ez();

    public ey() {
        long currentTimeMillis = System.currentTimeMillis();
        if (this.a == null) {
            this.b = currentTimeMillis;
        }
        if (this.b + SdkConstants.DEFAULT_SESSION_UPDATE_TIME > currentTimeMillis) {
            this.b = currentTimeMillis + SdkConstants.DEFAULT_SESSION_UPDATE_TIME;
            Random random = new Random(this.b);
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < 8; i++) {
                stringBuilder.append((char) ((Math.abs(random.nextInt()) % 10) + 48));
            }
            this.a = stringBuilder.toString();
        }
    }

    public ey(Parcel parcel) {
        super(parcel);
    }

    public final int describeContents() {
        return 0;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.a);
        parcel.writeLong(this.b);
    }
}
