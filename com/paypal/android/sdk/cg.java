package com.paypal.android.sdk;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

public final class cg extends SSLSocketFactory {
    private SSLSocketFactory delegate;

    public cg() {
        GeneralSecurityException e;
        try {
            SSLContext instance = SSLContext.getInstance("TLS");
            instance.init(null, null, null);
            this.delegate = instance.getSocketFactory();
        } catch (NoSuchAlgorithmException e2) {
            e = e2;
            throw new SSLException(e.getMessage());
        } catch (KeyManagementException e3) {
            e = e3;
            throw new SSLException(e.getMessage());
        }
    }

    public cg(InputStream inputStream) {
        try {
            KeyStore instance = KeyStore.getInstance(KeyStore.getDefaultType());
            instance.load(null, null);
            for (Certificate certificate : CertificateFactory.getInstance("X.509").generateCertificates(inputStream)) {
                if (certificate instanceof X509Certificate) {
                    instance.setCertificateEntry(((X509Certificate) certificate).getSubjectDN().getName(), certificate);
                }
            }
            TrustManagerFactory instance2 = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            instance2.init(instance);
            SSLContext instance3 = SSLContext.getInstance("TLS");
            instance3.init(null, instance2.getTrustManagers(), null);
            this.delegate = instance3.getSocketFactory();
            try {
                inputStream.close();
            } catch (IOException e) {
            } catch (NullPointerException e2) {
            }
        } catch (Exception e3) {
            throw new SSLException(e3.getMessage());
        } catch (Throwable th) {
            try {
                inputStream.close();
            } catch (IOException e4) {
            } catch (NullPointerException e5) {
            }
        }
    }

    private static Socket m178a(Socket socket) {
        if (socket instanceof SSLSocket) {
            ArrayList arrayList = new ArrayList(Arrays.asList(((SSLSocket) socket).getSupportedProtocols()));
            arrayList.retainAll(Arrays.asList(new String[]{"TLSv1.2", "TLSv1.1", "TLSv1"}));
            ((SSLSocket) socket).setEnabledProtocols((String[]) arrayList.toArray(new String[arrayList.size()]));
        }
        return socket;
    }

    public final Socket createSocket(String str, int i) {
        return m178a(this.delegate.createSocket(str, i));
    }

    public final Socket createSocket(String str, int i, InetAddress inetAddress, int i2) {
        return m178a(this.delegate.createSocket(str, i, inetAddress, i2));
    }

    public final Socket createSocket(InetAddress inetAddress, int i) {
        return m178a(this.delegate.createSocket(inetAddress, i));
    }

    public final Socket createSocket(InetAddress inetAddress, int i, InetAddress inetAddress2, int i2) {
        return m178a(this.delegate.createSocket(inetAddress, i, inetAddress2, i2));
    }

    public final Socket createSocket(Socket socket, String str, int i, boolean z) {
        return m178a(this.delegate.createSocket(socket, str, i, z));
    }

    public final String[] getDefaultCipherSuites() {
        return this.delegate.getDefaultCipherSuites();
    }

    public final String[] getSupportedCipherSuites() {
        return this.delegate.getSupportedCipherSuites();
    }
}
