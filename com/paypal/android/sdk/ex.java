package com.paypal.android.sdk;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ex extends SimpleDateFormat {
    private static final String f378a = ex.class.getSimpleName();
    private static final long serialVersionUID = 5709634976027470847L;

    public ex() {
        this(TimeZone.getTimeZone("UTC"));
    }

    private ex(TimeZone timeZone) {
        super("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);
        setTimeZone(timeZone);
    }

    public static Date m363a(String str) {
        if (str == null) {
            return null;
        }
        String[] strArr = new String[]{"yyyy-MM-dd'T'HH:mm:ssZ", "yyyy-MM-dd'T'HH:mm:ss.SSSZ", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "yyyy-MM-dd'T'HH:mm:ss'Z'"};
        int i = 0;
        while (i < 4) {
            String str2 = strArr[i];
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(str2, Locale.US);
            simpleDateFormat.setLenient(true);
            try {
                Date parse = simpleDateFormat.parse(str);
                if (parse != null) {
                    return parse;
                }
                i++;
            } catch (ParseException e) {
                new StringBuilder("unsuccessful attempt to parse date '").append(str).append("': ").append(e.getMessage()).append(" while using format:'").append(str2).append("'");
            }
        }
        new StringBuilder("couldn't parse '").append(str).append("'");
        return null;
    }
}
