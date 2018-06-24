package com.paypal.android.sdk;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class et {
    private static List f368a = Arrays.asList(new String[]{"AU", "BR", "CA", "ES", "FR", "GB", "IT", "MY", "SG", "US"});
    private static Pattern f369b = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,} *$");
    private static Pattern f370c = Pattern.compile("^[0-9]{4,8}$");
    private static Pattern f371d = Pattern.compile("^\\+?[0-9]{7,14}$");
    private static Pattern f372e = Pattern.compile("[ .\\-\\(\\)]*");
    private static Pattern f373f = Pattern.compile("^\\+?0+$");
    private static /* synthetic */ boolean f374g = (!et.class.desiredAssertionStatus());

    public static boolean m347a(String str) {
        if (f374g || str != null) {
            return f369b.matcher(str).matches();
        }
        throw new AssertionError();
    }

    public static boolean m348b(String str) {
        if (f374g || str != null) {
            return f370c.matcher(str).matches();
        }
        throw new AssertionError();
    }

    public static boolean m349c(String str) {
        return str.length() >= 8;
    }

    public static boolean m350d(String str) {
        if (f374g || str != null) {
            CharSequence replaceAll = f372e.matcher(str).replaceAll("");
            return f371d.matcher(replaceAll).matches() && !f373f.matcher(replaceAll).matches();
        } else {
            throw new AssertionError();
        }
    }

    public static String m351e(String str) {
        return f372e.matcher(str).replaceAll("");
    }

    public static boolean m352f(String str) {
        return C0441d.m267c((CharSequence) str) ? false : f368a.contains(str.toUpperCase());
    }
}
