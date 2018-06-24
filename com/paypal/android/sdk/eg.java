package com.paypal.android.sdk;

import android.util.Base64;
import android.util.Log;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public final class eg implements C0440c {
    private static final String f1008a = eg.class.getSimpleName();
    private final String f1009b;

    public eg(String str) {
        this.f1009b = str;
    }

    private static String m857a(Exception exception) {
        Log.e(f1008a, exception.getMessage());
        return null;
    }

    public final String mo2172a(String str) {
        Exception e;
        if (str == null) {
            return null;
        }
        try {
            Key generateSecret = SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(this.f1009b.getBytes("UTF8")));
            byte[] bytes = str.getBytes("UTF8");
            Cipher instance = Cipher.getInstance("DES");
            instance.init(1, generateSecret);
            return Base64.encodeToString(instance.doFinal(bytes), 0);
        } catch (InvalidKeyException e2) {
            e = e2;
            return m857a(e);
        } catch (UnsupportedEncodingException e3) {
            e = e3;
            return m857a(e);
        } catch (InvalidKeySpecException e4) {
            e = e4;
            return m857a(e);
        } catch (NoSuchAlgorithmException e5) {
            e = e5;
            return m857a(e);
        } catch (BadPaddingException e6) {
            e = e6;
            return m857a(e);
        } catch (NoSuchPaddingException e7) {
            e = e7;
            return m857a(e);
        } catch (IllegalBlockSizeException e8) {
            e = e8;
            return m857a(e);
        }
    }

    public final String mo2173b(String str) {
        Exception e;
        if (str == null) {
            return null;
        }
        try {
            Key generateSecret = SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(this.f1009b.getBytes("UTF8")));
            byte[] decode = Base64.decode(str, 0);
            Cipher instance = Cipher.getInstance("DES");
            instance.init(2, generateSecret);
            return new String(instance.doFinal(decode));
        } catch (InvalidKeyException e2) {
            e = e2;
            return m857a(e);
        } catch (UnsupportedEncodingException e3) {
            e = e3;
            return m857a(e);
        } catch (InvalidKeySpecException e4) {
            e = e4;
            return m857a(e);
        } catch (NoSuchAlgorithmException e5) {
            e = e5;
            return m857a(e);
        } catch (BadPaddingException e6) {
            e = e6;
            return m857a(e);
        } catch (NoSuchPaddingException e7) {
            e = e7;
            return m857a(e);
        } catch (IllegalBlockSizeException e8) {
            e = e8;
            return m857a(e);
        } catch (IllegalArgumentException e9) {
            e = e9;
            return m857a(e);
        }
    }
}
