package com.paypal.android.sdk;

import android.util.Log;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public final class ek {
    private static List f353a = Arrays.asList(new String[]{"AUD", "BRL", "CAD", "CHF", "CZK", "DKK", "EUR", "GBP", "HKD", "HUF", "ILS", "JPY", "MXN", "MYR", "NOK", "NZD", "PHP", "PLN", "RUB", "SEK", "SGD", "THB", "TWD", "TRY", "USD"});
    private static String f354b = "JPY, HUF, TWD";
    private static final Locale f355c = Locale.US;
    private static final Locale f356d = Locale.GERMANY;
    private static List f357e = null;
    private static NumberFormat f358f = null;

    public static String m331a(double d, String str) {
        return m332a(d, str, (DecimalFormat) NumberFormat.getInstance(f355c));
    }

    private static String m332a(double d, String str, DecimalFormat decimalFormat) {
        String str2 = "#######0";
        if ((f354b.indexOf(str.toUpperCase(Locale.US)) == -1 ? 1 : null) != null) {
            str2 = "#####0.00";
        }
        decimalFormat.applyPattern(str2);
        return decimalFormat.format(d);
    }

    public static String m333a(double d, Currency currency) {
        DecimalFormat decimalFormat = m334a(currency).equals(",") ? (DecimalFormat) NumberFormat.getInstance(f356d) : (DecimalFormat) NumberFormat.getInstance(f355c);
        String str = "#######0";
        if ((f354b.indexOf(currency.getCurrencyCode().toUpperCase(Locale.US)) == -1 ? 1 : null) != null) {
            str = "#####0.00";
        }
        decimalFormat.applyPattern(str);
        return decimalFormat.format(d);
    }

    private static String m334a(Currency currency) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        decimalFormat.setCurrency(currency);
        return decimalFormat.format(1.56d).indexOf(".") > 0 ? "." : ",";
    }

    public static String m335a(Locale locale, String str, double d, String str2, boolean z) {
        String str3;
        Object obj = null;
        String symbol = Currency.getInstance(str2).getSymbol();
        String str4 = " ";
        Currency instance = Currency.getInstance(str2);
        if (f358f == null) {
            f358f = NumberFormat.getCurrencyInstance(locale);
        }
        f358f.setCurrency(instance);
        if (f358f.format(1234.56d).indexOf("1") != 0) {
            Object obj2 = null;
        } else {
            int i = 1;
        }
        if (obj2 == null) {
            obj = 1;
        }
        StringBuilder append = new StringBuilder().append(obj != null ? symbol + str4 : "");
        if (str.equalsIgnoreCase("AU")) {
            str3 = "AUD";
        } else if (str.equalsIgnoreCase("GB")) {
            str3 = "GBP";
        } else if (str.equalsIgnoreCase("UK")) {
            str3 = "GBP";
        } else if (str.equalsIgnoreCase("CA")) {
            str3 = "CAD";
        } else if (str.equalsIgnoreCase("AT")) {
            str3 = "EUR";
        } else if (str.equalsIgnoreCase("CZ")) {
            str3 = "CZK";
        } else if (str.equalsIgnoreCase("DK")) {
            str3 = "DKK";
        } else if (str.equalsIgnoreCase("FR")) {
            str3 = "EUR";
        } else if (str.equalsIgnoreCase("DE")) {
            str3 = "EUR";
        } else if (str.equalsIgnoreCase("HU")) {
            str3 = "HUF";
        } else if (str.equalsIgnoreCase("IE")) {
            str3 = "EUR";
        } else if (str.equalsIgnoreCase("IT")) {
            str3 = "EUR";
        } else if (str.equalsIgnoreCase("NL")) {
            str3 = "EUR";
        } else if (str.equalsIgnoreCase("PL")) {
            str3 = "PLN";
        } else if (str.equalsIgnoreCase("PT")) {
            str3 = "EUR";
        } else if (str.equalsIgnoreCase("ES")) {
            str3 = "EUR";
        } else if (str.equalsIgnoreCase("SE")) {
            str3 = "SEK";
        } else {
            if (!str.equalsIgnoreCase("ZA")) {
                if (str.equalsIgnoreCase("NZ")) {
                    str3 = "NZD";
                } else if (str.equalsIgnoreCase("LT")) {
                    str3 = "EUR";
                } else if (str.equalsIgnoreCase("JP")) {
                    str3 = "JPY";
                } else if (str.equalsIgnoreCase("BR")) {
                    str3 = "BRL";
                } else if (str.equalsIgnoreCase("MY")) {
                    str3 = "MYR";
                } else if (str.equalsIgnoreCase("MX")) {
                    str3 = "MXN";
                } else if (str.equalsIgnoreCase("RU")) {
                    str3 = "RUB";
                }
            }
            str3 = "USD";
        }
        return append.append(m332a(d, str2, m334a(Currency.getInstance(str3)).equals(",") ? (DecimalFormat) NumberFormat.getInstance(f356d) : (DecimalFormat) NumberFormat.getInstance(f355c))).append(obj == null ? str4 + symbol : "").toString();
    }

    public static boolean m336a(String str) {
        if (str == null) {
            return false;
        }
        try {
            Currency instance = Currency.getInstance(str);
            if (f357e == null) {
                f357e = new ArrayList();
                for (String instance2 : f353a) {
                    f357e.add(Currency.getInstance(instance2));
                }
                Collections.sort(f357e, new el());
            }
            return f357e.contains(instance);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static boolean m337a(BigDecimal bigDecimal, String str, boolean z) {
        if (bigDecimal == null) {
            Log.e("paypal.sdk", "The specified amount is null.");
            return false;
        } else if (!z || BigDecimal.ZERO.compareTo(bigDecimal) == -1) {
            if (m336a(str)) {
                if (Arrays.asList(new String[]{"HUF", "JPY", "TWD"}).contains(str) && bigDecimal.scale() > 0) {
                    Log.e("paypal.sdk", "The specified currency (" + str + ") does not support fractional amounts.");
                    return false;
                }
            }
            return true;
        } else {
            Log.e("paypal.sdk", "The specified amount must be greater than zero.");
            return false;
        }
    }
}
