package com.sinch.android.rtc.internal.service.crypto;

public interface CryptoService {
    String decrypt(int i, byte[] bArr, String str);

    String encrypt(int i, byte[] bArr, String str);

    String sign(int i, byte[] bArr, String str);
}
