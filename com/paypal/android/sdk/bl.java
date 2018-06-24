package com.paypal.android.sdk;

public class bl {
    public static final bl f130a = new bl();
    private static final String f131b = bl.class.getSimpleName();
    private byte[] f132c;

    private bl() {
        this.f132c = null;
        this.f132c = null;
    }

    public bl(String str) {
        this.f132c = null;
        try {
            this.f132c = new byte[((str.length() + 1) / 2)];
            int i = 0;
            int length = str.length() - 1;
            while (length >= 0) {
                int i2 = length - 1;
                if (i2 < 0) {
                    i2 = 0;
                }
                this.f132c[i] = (byte) Integer.parseInt(str.substring(i2, length + 1), 16);
                length -= 2;
                i++;
            }
        } catch (Throwable e) {
            bn.m142a(f131b, "PPRiskDataBitSet initialize failed", e);
            this.f132c = null;
        }
    }

    public final boolean m127a(bm bmVar) {
        int a = bmVar.m128a() / 8;
        if (this.f132c == null) {
            return true;
        }
        if (a >= this.f132c.length) {
            return false;
        }
        byte a2 = (byte) (1 << (bmVar.m128a() % 8));
        return (this.f132c[a] & a2) == a2;
    }
}
