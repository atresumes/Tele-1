package com.paypal.android.sdk;

import java.util.Locale;

public final class ft extends ck {
    private final boolean f1142a;
    private final int f1143b;

    public ft(cx cxVar, int i, boolean z, int i2) {
        super(i, cxVar);
        this.f1142a = z;
        this.f1143b = i2;
    }

    protected final String mo3080b() {
        int i = this.f1143b;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i2 = 0; i2 < i; i2++) {
            if (i2 != 0) {
                stringBuilder.append(",\n");
            }
            String substring = new String(new char[4]).replace("\u0000", i2).substring(0, 4);
            stringBuilder.append(String.format(Locale.US, "    {\n        \"type\":\"sms_otp\",\n        \"token_identifier\":\"mock_token_id_%s\",\n        \"token_identifier_display\":\"xxx-xxx-%s\"\n    }\n", new Object[]{Integer.valueOf(i2), substring}));
        }
        return String.format(Locale.US, "{\n    \"nonce\":\"mock-login-nonce\",\n    \"error\":\"2fa_required\",\n    \"error_description\":\"Unable to authenticate the user. 2fa flow completion is necessary for successful login.\",\n    \"visitor_id\":\"mock-visitor_id\",\n    \"2fa_enabled\":\"true\",\n    \"2fa_token_identifier\":[\n%s    ]\n}", new Object[]{stringBuilder.toString()});
    }

    protected final int mo3081c() {
        return 401;
    }

    protected final boolean mo3082c(cw cwVar) {
        return this.f1142a && (cwVar instanceof fm) && !((fm) cwVar).m1085t();
    }
}
