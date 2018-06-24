package com.paypal.android.sdk;

import android.os.Environment;
import com.bumptech.glide.load.Key;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public final class aq {
    private boolean f32a = false;
    private boolean f33b = false;
    private File f34c;

    public aq() {
        String externalStorageState = Environment.getExternalStorageState();
        boolean z = true;
        switch (externalStorageState.hashCode()) {
            case 1242932856:
                if (externalStorageState.equals("mounted")) {
                    z = false;
                    break;
                }
                break;
            case 1299749220:
                if (externalStorageState.equals("mounted_ro")) {
                    z = true;
                    break;
                }
                break;
        }
        switch (z) {
            case false:
                this.f33b = true;
                this.f32a = true;
                break;
            case true:
                this.f32a = true;
                this.f33b = false;
                break;
            default:
                this.f33b = false;
                this.f32a = false;
                break;
        }
        this.f34c = Environment.getExternalStorageDirectory();
    }

    public final void m65a(String str) {
        this.f34c = new File(str);
    }

    public final void m66a(String str, byte[] bArr) {
        Closeable fileOutputStream;
        Throwable th;
        if (this.f32a && this.f33b) {
            Closeable closeable = null;
            try {
                if (this.f34c.mkdirs() || this.f34c.isDirectory()) {
                    fileOutputStream = new FileOutputStream(new File(this.f34c, str));
                    try {
                        fileOutputStream.write(bArr);
                        closeable = fileOutputStream;
                    } catch (Throwable th2) {
                        th = th2;
                        C0441d.m260a(fileOutputStream);
                        throw th;
                    }
                }
                C0441d.m260a(closeable);
            } catch (Throwable th3) {
                Throwable th4 = th3;
                fileOutputStream = null;
                th = th4;
                C0441d.m260a(fileOutputStream);
                throw th;
            }
        }
    }

    public final String m67b(String str) {
        Closeable fileInputStream;
        String str2;
        Throwable th;
        byte[] bArr = new byte[1024];
        if (this.f33b) {
            try {
                fileInputStream = new FileInputStream(new File(this.f34c, str));
                try {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    for (int read = fileInputStream.read(bArr, 0, 1024); read != -1; read = fileInputStream.read(bArr, 0, 1024)) {
                        byteArrayOutputStream.write(bArr, 0, read);
                    }
                    bArr = byteArrayOutputStream.toByteArray();
                    C0441d.m260a(fileInputStream);
                } catch (IOException e) {
                    str2 = "";
                    C0441d.m260a(fileInputStream);
                    return str2;
                } catch (Throwable th2) {
                    th = th2;
                    C0441d.m260a(fileInputStream);
                    throw th;
                }
            } catch (IOException e2) {
                fileInputStream = null;
                str2 = "";
                C0441d.m260a(fileInputStream);
                return str2;
            } catch (Throwable th3) {
                th = th3;
                fileInputStream = null;
                C0441d.m260a(fileInputStream);
                throw th;
            }
        }
        return new String(bArr, Key.STRING_CHARSET_NAME);
    }
}
