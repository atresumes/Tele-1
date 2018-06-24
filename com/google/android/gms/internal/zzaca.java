package com.google.android.gms.internal;

public abstract class zzaca<T> {
    private static String READ_PERMISSION = "com.google.android.providers.gsf.permission.READ_GSERVICES";
    private static zza zzaDC = null;
    private static int zzaDD = 0;
    private static final Object zztX = new Object();
    protected final String zzAX;
    protected final T zzAY;
    private T zzaDE = null;

    private interface zza {
    }

    class C08661 extends zzaca<Boolean> {
        C08661(String str, Boolean bool) {
            super(str, bool);
        }
    }

    class C08672 extends zzaca<Long> {
        C08672(String str, Long l) {
            super(str, l);
        }
    }

    class C08683 extends zzaca<Integer> {
        C08683(String str, Integer num) {
            super(str, num);
        }
    }

    class C08694 extends zzaca<Float> {
        C08694(String str, Float f) {
            super(str, f);
        }
    }

    class C08705 extends zzaca<String> {
        C08705(String str, String str2) {
            super(str, str2);
        }
    }

    protected zzaca(String str, T t) {
        this.zzAX = str;
        this.zzAY = t;
    }

    public static zzaca<String> zzB(String str, String str2) {
        return new C08705(str, str2);
    }

    public static zzaca<Float> zza(String str, Float f) {
        return new C08694(str, f);
    }

    public static zzaca<Integer> zza(String str, Integer num) {
        return new C08683(str, num);
    }

    public static zzaca<Long> zza(String str, Long l) {
        return new C08672(str, l);
    }

    public static zzaca<Boolean> zzj(String str, boolean z) {
        return new C08661(str, Boolean.valueOf(z));
    }
}
