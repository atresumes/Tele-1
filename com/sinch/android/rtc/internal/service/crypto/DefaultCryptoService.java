package com.sinch.android.rtc.internal.service.crypto;

import android.util.Base64;
import com.bumptech.glide.load.Key;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DefaultCryptoService implements CryptoService {
    private static final int IV_LENGTH = 16;
    private static final int SUPPORTED_PROTOCOL_VERSION = 10;
    private static final String TAG = "CryptoService";
    private static final int UTF8_BOM_LENGTH = 3;

    private String concatBytesAndEncodeBase64String(byte[] bArr, byte[] bArr2) {
        Object obj = new byte[(bArr2.length + 16)];
        System.arraycopy(bArr, 0, obj, 0, bArr.length);
        System.arraycopy(bArr2, 0, obj, bArr.length, bArr2.length);
        return Base64.encodeToString(obj, 0, obj.length, 2);
    }

    private boolean containsBomAtMessageStart(byte[] bArr) {
        return bArr[16] == (byte) -17 && bArr[17] == (byte) -69 && bArr[18] == (byte) -65;
    }

    private String createMessageString(byte[] bArr) {
        return new String(bArr, 16, bArr.length - 16, Key.STRING_CHARSET_NAME);
    }

    private String createMessageStringFilterOutBom(byte[] bArr) {
        return new String(bArr, 19, (bArr.length - 16) - 3, Key.STRING_CHARSET_NAME);
    }

    public String calculateSignature(String str) {
        try {
            return new String(Base64.encode(MessageDigest.getInstance("SHA-1").digest(str.getBytes(Key.STRING_CHARSET_NAME)), 2), Key.STRING_CHARSET_NAME);
        } catch (NoSuchAlgorithmException e) {
            return null;
        } catch (UnsupportedEncodingException e2) {
            return null;
        }
    }

    public String decrypt(int i, byte[] bArr, String str) {
        int i2 = 16;
        if (i != 10) {
            throw new IllegalArgumentException("protocol version not supported");
        }
        try {
            Object decode = Base64.decode(str, 0);
            Object obj = new byte[16];
            if (decode.length < 16) {
                i2 = decode.length;
            }
            System.arraycopy(decode, 0, obj, 0, i2);
            try {
                java.security.Key secretKeySpec = new SecretKeySpec(bArr, "AES");
                AlgorithmParameterSpec ivParameterSpec = new IvParameterSpec(obj);
                Cipher instance = Cipher.getInstance("AES/CBC/PKCS5Padding");
                instance.init(2, secretKeySpec, ivParameterSpec);
                byte[] doFinal = instance.doFinal(decode);
                return containsBomAtMessageStart(doFinal) ? createMessageStringFilterOutBom(doFinal) : createMessageString(doFinal);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e2) {
            return null;
        }
    }

    public String encrypt(int i, byte[] bArr, String str) {
        Exception e;
        if (i != 10) {
            throw new IllegalArgumentException("protocol version not supported");
        }
        String concatBytesAndEncodeBase64String;
        try {
            byte[] bytes = str.getBytes(Key.STRING_CHARSET_NAME);
            byte[] bArr2 = new byte[16];
            new SecureRandom().nextBytes(bArr2);
            java.security.Key secretKeySpec = new SecretKeySpec(bArr, "AES");
            AlgorithmParameterSpec ivParameterSpec = new IvParameterSpec(bArr2);
            Cipher instance = Cipher.getInstance("AES/CBC/PKCS5Padding");
            instance.init(1, secretKeySpec, ivParameterSpec);
            byte[] bArr3 = new byte[instance.getOutputSize(bytes.length)];
            int update = instance.update(bytes, 0, bytes.length, bArr3, 0);
            int doFinal = instance.doFinal(bArr3, update) + update;
            concatBytesAndEncodeBase64String = concatBytesAndEncodeBase64String(bArr2, bArr3);
            try {
                return concatBytesAndEncodeBase64String.length() % 4 != 0 ? concatBytesAndEncodeBase64String : concatBytesAndEncodeBase64String;
            } catch (Exception e2) {
                e = e2;
                e.printStackTrace();
            }
        } catch (Exception e3) {
            e = e3;
            concatBytesAndEncodeBase64String = null;
            e.printStackTrace();
        }
    }

    public String sign(int i, byte[] bArr, String str) {
        return calculateSignature(str + Base64.encodeToString(bArr, 2));
    }
}
