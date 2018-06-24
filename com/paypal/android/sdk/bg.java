package com.paypal.android.sdk;

import android.net.Uri;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;
import javax.net.ssl.HttpsURLConnection;

public class bg implements bb {
    private static int f977e = 60000;
    private static int f978f = 60000;
    private final bk f979a = new bk(C0441d.m255a());
    private byte[] f980b;
    private Uri f981c;
    private Map f982d;

    static {
        bg.class.getSimpleName();
    }

    public final int mo2152a(byte[] bArr) {
        HttpsURLConnection httpsURLConnection;
        Throwable th;
        Closeable closeable;
        Throwable th2;
        Closeable closeable2 = null;
        try {
            Closeable outputStream;
            HttpsURLConnection httpsURLConnection2 = (HttpsURLConnection) new URL(this.f981c.toString()).openConnection();
            try {
                httpsURLConnection2.setReadTimeout(f978f);
                httpsURLConnection2.setConnectTimeout(f977e);
                httpsURLConnection2.setRequestMethod("POST");
                httpsURLConnection2.setDoInput(true);
                httpsURLConnection2.setDoOutput(true);
                httpsURLConnection2.setSSLSocketFactory(this.f979a);
                for (Entry entry : this.f982d.entrySet()) {
                    httpsURLConnection2.setRequestProperty(entry.getKey().toString(), entry.getValue().toString());
                }
                httpsURLConnection2.setFixedLengthStreamingMode(bArr.length);
                outputStream = httpsURLConnection2.getOutputStream();
            } catch (Throwable th3) {
                httpsURLConnection = httpsURLConnection2;
                th = th3;
                closeable = null;
                C0441d.m260a(closeable2);
                C0441d.m260a(closeable);
                if (httpsURLConnection != null) {
                    httpsURLConnection.disconnect();
                }
                throw th;
            }
            try {
                outputStream.write(bArr);
                outputStream.flush();
                int responseCode = httpsURLConnection2.getResponseCode();
                if (responseCode == Callback.DEFAULT_DRAG_ANIMATION_DURATION) {
                    closeable = new BufferedInputStream(httpsURLConnection2.getInputStream());
                    try {
                        byte[] bArr2 = new byte[1024];
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        while (true) {
                            int read = closeable.read(bArr2);
                            if (read == -1) {
                                break;
                            }
                            byteArrayOutputStream.write(bArr2, 0, read);
                        }
                        this.f980b = byteArrayOutputStream.toByteArray();
                    } catch (Throwable th4) {
                        th2 = th4;
                        closeable2 = closeable;
                        closeable = outputStream;
                        httpsURLConnection = httpsURLConnection2;
                        th = th2;
                        C0441d.m260a(closeable2);
                        C0441d.m260a(closeable);
                        if (httpsURLConnection != null) {
                            httpsURLConnection.disconnect();
                        }
                        throw th;
                    }
                }
                this.f980b = new byte[0];
                closeable = null;
                C0441d.m260a(closeable);
                C0441d.m260a(outputStream);
                if (httpsURLConnection2 != null) {
                    httpsURLConnection2.disconnect();
                }
                return responseCode;
            } catch (Throwable th32) {
                th2 = th32;
                closeable = outputStream;
                httpsURLConnection = httpsURLConnection2;
                th = th2;
                C0441d.m260a(closeable2);
                C0441d.m260a(closeable);
                if (httpsURLConnection != null) {
                    httpsURLConnection.disconnect();
                }
                throw th;
            }
        } catch (Throwable th5) {
            th = th5;
            closeable = null;
            httpsURLConnection = null;
            C0441d.m260a(closeable2);
            C0441d.m260a(closeable);
            if (httpsURLConnection != null) {
                httpsURLConnection.disconnect();
            }
            throw th;
        }
    }

    public final void mo2153a(Uri uri) {
        this.f981c = uri;
    }

    public final void mo2154a(Map map) {
        this.f982d = map;
    }

    public final byte[] mo2155a() {
        return this.f980b;
    }
}
