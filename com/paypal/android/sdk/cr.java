package com.paypal.android.sdk;

import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

final class cr implements Callback {
    private final cw f994a;

    private cr(cm cmVar, cw cwVar) {
        this.f994a = cwVar;
    }

    public final void onFailure(Call call, IOException iOException) {
        this.f994a.m195b(iOException.getMessage());
        cm.f1102a;
        new StringBuilder().append(this.f994a.m207n()).append(" failure: ").append(iOException.getMessage());
    }

    public final void onResponse(Call call, Response response) {
        this.f994a.m195b(response.body().string());
        cm.f1102a;
        new StringBuilder().append(this.f994a.m207n()).append(" success");
    }
}
