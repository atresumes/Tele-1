package com.paypal.android.sdk;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class aj {
    private static final String f12a = aj.class.getSimpleName();

    private static boolean m37a(PackageManager packageManager, String str, String str2, String str3, int i) {
        Signature[] signatureArr = packageManager.getPackageInfo(str, 64).signatures;
        int i2 = 0;
        while (i2 < signatureArr.length) {
            try {
                X509Certificate x509Certificate = (X509Certificate) CertificateFactory.getInstance("X509").generateCertificate(new ByteArrayInputStream(signatureArr[i2].toByteArray()));
                String name = x509Certificate.getSubjectX500Principal().getName();
                String name2 = x509Certificate.getIssuerX500Principal().getName();
                int hashCode = x509Certificate.getPublicKey().hashCode();
                new StringBuilder("Certificate subject: ").append(name);
                new StringBuilder("Certificate issuer: ").append(name2);
                new StringBuilder("Certificate public key hash code: ").append(hashCode);
                return str2.equals(name) && str3.equals(name2) && i == hashCode;
            } catch (CertificateException e) {
                i2++;
            }
        }
        return false;
    }

    protected final boolean m38a(Context context, boolean z, String str, String str2, String str3, int i) {
        boolean z2 = false;
        PackageManager packageManager = context.getPackageManager();
        try {
            packageManager.getApplicationInfo(str, 4224);
            if (!z || m37a(packageManager, str, str2, str3, 34172764)) {
                z2 = true;
            }
        } catch (NameNotFoundException e) {
            new StringBuilder().append(str).append(" not found.");
        }
        new StringBuilder("returning isValid:").append(z2);
        return z2;
    }
}
