package com.paypal.android.sdk.payments;

import com.paypal.android.sdk.ak;
import java.util.HashMap;

final class C0460o extends HashMap {
    C0460o() {
        put(C0465y.openid_connect, ak.OPENID);
        put(C0465y.oauth_fullname, ak.PROFILE);
        put(C0465y.oauth_gender, ak.PROFILE);
        put(C0465y.oauth_date_of_birth, ak.PROFILE);
        put(C0465y.oauth_timezone, ak.PROFILE);
        put(C0465y.oauth_locale, ak.PROFILE);
        put(C0465y.oauth_language, ak.PROFILE);
        put(C0465y.oauth_age_range, ak.PAYPAL_ATTRIBUTES);
        put(C0465y.oauth_account_verified, ak.PAYPAL_ATTRIBUTES);
        put(C0465y.oauth_account_type, ak.PAYPAL_ATTRIBUTES);
        put(C0465y.oauth_account_creation_date, ak.PAYPAL_ATTRIBUTES);
        put(C0465y.oauth_email, ak.EMAIL);
        put(C0465y.oauth_street_address1, ak.ADDRESS);
        put(C0465y.oauth_street_address2, ak.ADDRESS);
        put(C0465y.oauth_city, ak.ADDRESS);
        put(C0465y.oauth_state, ak.ADDRESS);
        put(C0465y.oauth_country, ak.ADDRESS);
        put(C0465y.oauth_zip, ak.ADDRESS);
        put(C0465y.oauth_phone_number, ak.PHONE);
    }
}
