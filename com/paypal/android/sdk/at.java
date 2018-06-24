package com.paypal.android.sdk;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import com.payUMoney.sdk.SdkConstants;
import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import org.json.JSONObject;

public class at implements LocationListener {
    public static bc f87a = null;
    private static final String f88b = at.class.getSimpleName();
    private static ar f89x = new ar();
    private static final Object f90y = new Object();
    private static at f91z;
    private Context f92c;
    private String f93d;
    private long f94e;
    private long f95f;
    private int f96g;
    private int f97h;
    private long f98i;
    private String f99j;
    private ap f100k;
    private as f101l;
    private as f102m;
    private String f103n;
    private Map f104o;
    private Location f105p;
    private Timer f106q;
    private Handler f107r;
    private ay f108s;
    private String f109t;
    private String f110u;
    private boolean f111v;
    private String f112w;

    private at() {
    }

    private static long m74a(Context context) {
        if (context == null) {
            return 0;
        }
        try {
            if (VERSION.SDK_INT > 8) {
                return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).firstInstallTime;
            }
            String str = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0).sourceDir;
            return str != null ? new File(str).lastModified() : 0;
        } catch (NameNotFoundException e) {
            return 0;
        }
    }

    public static at m75a() {
        at atVar;
        synchronized (f90y) {
            if (f91z == null) {
                f91z = new at();
            }
            atVar = f91z;
        }
        return atVar;
    }

    private static String m76a(TelephonyManager telephonyManager) {
        try {
            return telephonyManager.getSimOperatorName();
        } catch (Throwable e) {
            bn.m142a(f88b, "Known SecurityException on some devices", e);
            return null;
        }
    }

    private String m77a(String str, Map map) {
        this.f104o = null;
        if (str != null && this.f110u != null && str.equals(this.f110u)) {
            return str;
        }
        String i;
        if (str == null || str.trim().length() == 0) {
            i = m91i();
        } else {
            i = str.trim();
            bn.m139a(3, "PRD", "Using custom pairing id");
        }
        this.f110u = i;
        m101e();
        m92j();
        return i;
    }

    private void m78a(ap apVar) {
        this.f100k = apVar;
        bn.m141a(f88b, "Configuration loaded");
        bn.m141a(f88b, "URL:     " + this.f100k.m56a());
        bn.m141a(f88b, "Version: " + this.f100k.m57b());
        m95k();
        this.f106q = new Timer();
        long c = this.f100k.m58c();
        long d = this.f100k.m59d();
        long e = this.f100k.m60e();
        bn.m141a(f88b, "Sending logRiskMetadata every " + c + " seconds.");
        bn.m141a(f88b, "sessionTimeout set to " + d + " seconds.");
        bn.m141a(f88b, "compTimeout set to    " + e + " seconds.");
        this.f94e = c * 1000;
        this.f95f = e * 1000;
        ax.m105a(d * 1000);
        if (this.f100k != null && this.f111v) {
            m95k();
            this.f106q = new Timer();
            bn.m141a(f88b, "Starting LogRiskMetadataTask");
            this.f106q.scheduleAtFixedRate(new au(this), 0, this.f94e);
        }
    }

    private void m79a(as asVar, as asVar2) {
        boolean z = true;
        if (asVar != null) {
            asVar.ah = this.f104o;
            JSONObject a = asVar2 != null ? asVar.m72a(asVar2) : asVar.m71a();
            HashMap hashMap = new HashMap();
            hashMap.put("appGuid", this.f93d);
            hashMap.put("libraryVersion", m84d());
            hashMap.put("additionalData", a.toString());
            bn.m141a(f88b, "Dyson Risk Data " + a.toString());
            if (this.f100k != null) {
                String g = this.f100k.m62g();
                boolean h = this.f100k.m63h();
                bn.m141a(f88b, "new LogRiskMetadataRequest to: " + g);
                bn.m141a(f88b, "endpointIsStage: " + h + " (using SSL: " + (!h) + ")");
                Handler handler = this.f107r;
                if (h) {
                    z = false;
                }
                bi.m121a().m123a(new be(g, hashMap, handler, z));
            }
        }
    }

    private static long m81b(Context context) {
        if (context == null) {
            return 0;
        }
        try {
            if (VERSION.SDK_INT > 8) {
                return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).lastUpdateTime;
            }
            String str = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0).sourceDir;
            return str != null ? new File(str).lastModified() : 0;
        } catch (NameNotFoundException e) {
            return 0;
        }
    }

    static /* synthetic */ boolean m82c(at atVar) {
        return System.currentTimeMillis() - atVar.f98i > atVar.f95f;
    }

    public static String m84d() {
        return String.format(Locale.US, "Dyson/%S (%S %S)", new Object[]{"3.5.4.release", SdkConstants.OS_NAME_VALUE, VERSION.RELEASE});
    }

    static /* synthetic */ void m86f(at atVar) {
        if (atVar.f102m != null) {
            bn.m141a(f88b, atVar.f103n + " update not sent correctly, retrying...");
            if ("full".equals(atVar.f103n)) {
                atVar.m79a(atVar.f102m, null);
                return;
            }
            atVar.m79a(atVar.f102m, atVar.m96l());
        } else if (!ax.m107c() || atVar.f101l == null) {
            ax.m104a();
            atVar.f103n = "full";
            r0 = atVar.m96l();
            atVar.m79a(r0, null);
            atVar.f102m = r0;
        } else {
            atVar.f103n = "incremental";
            r0 = atVar.m96l();
            atVar.m79a(atVar.f101l, r0);
            atVar.f102m = r0;
        }
    }

    private static String m91i() {
        return bn.m151b(Boolean.FALSE.booleanValue());
    }

    private String m92j() {
        StringBuilder stringBuilder = new StringBuilder("https://b.stats.paypal.com/counter.cgi?p=");
        if (this.f108s == null || this.f108s == ay.UNKNOWN) {
            return "Beacon not recognize host app";
        }
        int a = this.f108s.m109a();
        if (this.f110u == null) {
            return "Beacon pairing id empty";
        }
        stringBuilder.append(this.f110u).append("&i=");
        String b = bn.m147b();
        if (b.equals("")) {
            try {
                stringBuilder.append(ar.m68a("emptyIp")).append("&t=");
            } catch (Throwable e) {
                bn.m142a(f88b, "error reading property file", e);
            }
        } else {
            stringBuilder.append(b).append("&t=");
        }
        stringBuilder.append(String.valueOf(System.currentTimeMillis() / 1000)).append("&a=").append(a);
        bn.m141a(f88b, "Beacon Request URL " + stringBuilder.toString());
        bi.m121a().m123a(new ba(stringBuilder.toString(), this.f93d, this.f109t, bn.m130a(this.f92c), this.f107r));
        return stringBuilder.toString();
    }

    private void m95k() {
        if (this.f106q != null) {
            this.f106q.cancel();
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.paypal.android.sdk.as m96l() {
        /*
        r14 = this;
        r0 = r14.f92c;
        if (r0 != 0) goto L_0x0006;
    L_0x0004:
        r0 = 0;
    L_0x0005:
        return r0;
    L_0x0006:
        r3 = new com.paypal.android.sdk.as;
        r3.<init>();
        r0 = r14.f100k;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r8 = r0.m64i();	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r0 = r14.f92c;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = "phone";
        r0 = r0.getSystemService(r1);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r0 = (android.telephony.TelephonyManager) r0;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = r14.f92c;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r2 = "wifi";
        r1 = r1.getSystemService(r2);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = (android.net.wifi.WifiManager) r1;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r2 = r14.f92c;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r4 = "connectivity";
        r2 = r2.getSystemService(r4);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r2 = (android.net.ConnectivityManager) r2;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r5 = 0;
        r4 = 0;
        r6 = r14.f92c;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r7 = "android.permission.ACCESS_WIFI_STATE";
        r6 = com.paypal.android.sdk.bn.m144a(r6, r7);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r6 == 0) goto L_0x04a1;
    L_0x003b:
        r1 = r1.getConnectionInfo();	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r7 = r1;
    L_0x0040:
        r1 = r14.f92c;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r6 = "android.permission.ACCESS_NETWORK_STATE";
        r1 = com.paypal.android.sdk.bn.m144a(r1, r6);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r1 == 0) goto L_0x04a5;
    L_0x004a:
        r1 = r2.getActiveNetworkInfo();	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r6 = r1;
    L_0x004f:
        r1 = r14.f92c;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r2 = "android.permission.ACCESS_COARSE_LOCATION";
        r1 = com.paypal.android.sdk.bn.m144a(r1, r2);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r1 != 0) goto L_0x0063;
    L_0x0059:
        r1 = r14.f92c;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r2 = "android.permission.ACCESS_FINE_LOCATION";
        r1 = com.paypal.android.sdk.bn.m144a(r1, r2);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r1 == 0) goto L_0x04a9;
    L_0x0063:
        r1 = 1;
    L_0x0064:
        r2 = r14.f92c;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r9 = "android.permission.READ_PHONE_STATE";
        r9 = com.paypal.android.sdk.bn.m144a(r2, r9);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r10 = new java.util.Date;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r10.<init>();	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r2 = r0.getPhoneType();	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        switch(r2) {
            case 0: goto L_0x04ac;
            case 1: goto L_0x04b4;
            case 2: goto L_0x04cc;
            default: goto L_0x0078;
        };	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x0078:
        r1 = new java.lang.StringBuilder;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r2 = "unknown (";
        r1.<init>(r2);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r2 = r0.getPhoneType();	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = r1.append(r2);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r2 = ")";
        r1 = r1.append(r2);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = r1.toString();	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r3.f35A = r1;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r2 = r4;
        r4 = r5;
    L_0x0095:
        r1 = com.paypal.android.sdk.bm.PPRiskDataPhoneType;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = r8.m127a(r1);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r1 != 0) goto L_0x00a0;
    L_0x009d:
        r1 = 0;
        r3.f35A = r1;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x00a0:
        r1 = com.paypal.android.sdk.bm.PPRiskDataAppGuid;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = r8.m127a(r1);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r1 == 0) goto L_0x00ac;
    L_0x00a8:
        r1 = r14.f93d;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r3.f61a = r1;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x00ac:
        r1 = com.paypal.android.sdk.bm.PPRiskDataPairingId;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = r8.m127a(r1);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r1 == 0) goto L_0x00b8;
    L_0x00b4:
        r1 = r14.f110u;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r3.f55U = r1;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x00b8:
        r1 = com.paypal.android.sdk.bm.PPRiskDataSourceApp;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = r8.m127a(r1);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r1 == 0) goto L_0x00cc;
    L_0x00c0:
        r1 = r14.f108s;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r1 != 0) goto L_0x04e4;
    L_0x00c4:
        r1 = com.paypal.android.sdk.ay.UNKNOWN;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = r1.m109a();	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r3.f51Q = r1;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x00cc:
        r1 = com.paypal.android.sdk.bm.PPRiskDataSourceAppVersion;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = r8.m127a(r1);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r1 == 0) goto L_0x00d8;
    L_0x00d4:
        r1 = r14.f109t;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r3.f52R = r1;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x00d8:
        r1 = com.paypal.android.sdk.bm.PPRiskDataNotifToken;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = r8.m127a(r1);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r1 == 0) goto L_0x00e4;
    L_0x00e0:
        r1 = r14.f112w;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r3.f60Z = r1;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x00e4:
        r1 = com.paypal.android.sdk.bm.PPRiskDataAndroidId;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = r8.m127a(r1);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r1 == 0) goto L_0x00fa;
    L_0x00ec:
        r1 = r14.f92c;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = r1.getContentResolver();	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r5 = "android_id";
        r1 = android.provider.Settings.Secure.getString(r1, r5);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r3.f58X = r1;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x00fa:
        r1 = r14.f92c;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = com.paypal.android.sdk.bn.m130a(r1);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r5 = com.paypal.android.sdk.bm.PPRiskDataAppId;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r5 = r8.m127a(r5);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r5 == 0) goto L_0x010e;
    L_0x0108:
        r5 = r1.m41a();	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r3.f62b = r5;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x010e:
        r5 = com.paypal.android.sdk.bm.PPRiskDataAppVersion;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r5 = r8.m127a(r5);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r5 == 0) goto L_0x011c;
    L_0x0116:
        r1 = r1.m43b();	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r3.f63c = r1;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x011c:
        r1 = com.paypal.android.sdk.bm.PPRiskDataBaseStationId;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = r8.m127a(r1);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r1 == 0) goto L_0x0129;
    L_0x0124:
        if (r2 != 0) goto L_0x04f0;
    L_0x0126:
        r1 = -1;
    L_0x0127:
        r3.f64d = r1;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x0129:
        r1 = com.paypal.android.sdk.bm.PPRiskDataCdmaNetworkId;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = r8.m127a(r1);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r1 == 0) goto L_0x0136;
    L_0x0131:
        if (r2 != 0) goto L_0x04f6;
    L_0x0133:
        r1 = -1;
    L_0x0134:
        r3.f49O = r1;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x0136:
        r1 = com.paypal.android.sdk.bm.PPRiskDataCdmaSystemId;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = r8.m127a(r1);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r1 == 0) goto L_0x0143;
    L_0x013e:
        if (r2 != 0) goto L_0x04fc;
    L_0x0140:
        r1 = -1;
    L_0x0141:
        r3.f48N = r1;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x0143:
        r1 = com.paypal.android.sdk.bm.PPRiskDataBssid;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = r8.m127a(r1);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r1 == 0) goto L_0x0150;
    L_0x014b:
        if (r7 != 0) goto L_0x0502;
    L_0x014d:
        r1 = 0;
    L_0x014e:
        r3.f65e = r1;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x0150:
        r1 = com.paypal.android.sdk.bm.PPRiskDataCellId;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = r8.m127a(r1);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r1 == 0) goto L_0x015d;
    L_0x0158:
        if (r4 != 0) goto L_0x0508;
    L_0x015a:
        r1 = -1;
    L_0x015b:
        r3.f66f = r1;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x015d:
        r1 = com.paypal.android.sdk.bm.PPRiskDataNetworkOperator;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = r8.m127a(r1);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r1 == 0) goto L_0x016b;
    L_0x0165:
        r1 = r0.getNetworkOperator();	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r3.f50P = r1;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x016b:
        r1 = "3.5.4.release";
        r3.f67g = r1;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = r14.f99j;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r3.f68h = r1;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = r14.f100k;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r1 != 0) goto L_0x050e;
    L_0x0177:
        r1 = 0;
    L_0x0178:
        r3.f69i = r1;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = com.paypal.android.sdk.bm.PPRiskDataConnType;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = r8.m127a(r1);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r1 == 0) goto L_0x0187;
    L_0x0182:
        if (r6 != 0) goto L_0x0516;
    L_0x0184:
        r1 = 0;
    L_0x0185:
        r3.f70j = r1;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x0187:
        r1 = com.paypal.android.sdk.bm.PPRiskDataDeviceId;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = r8.m127a(r1);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r1 == 0) goto L_0x0197;
    L_0x018f:
        if (r9 == 0) goto L_0x051c;
    L_0x0191:
        r1 = r0.getDeviceId();	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x0195:
        r3.f71k = r1;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x0197:
        r1 = com.paypal.android.sdk.bm.PPRiskDataDeviceModel;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = r8.m127a(r1);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r1 == 0) goto L_0x01a3;
    L_0x019f:
        r1 = android.os.Build.MODEL;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r3.f72l = r1;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x01a3:
        r1 = com.paypal.android.sdk.bm.PPRiskDataDeviceName;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = r8.m127a(r1);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r1 == 0) goto L_0x01af;
    L_0x01ab:
        r1 = android.os.Build.DEVICE;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r3.f73m = r1;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x01af:
        r1 = com.paypal.android.sdk.bm.PPRiskDataDeviceUptime;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = r8.m127a(r1);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r1 == 0) goto L_0x01bd;
    L_0x01b7:
        r12 = android.os.SystemClock.uptimeMillis();	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r3.f74n = r12;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x01bd:
        r1 = com.paypal.android.sdk.bm.PPRiskDataIpAddrs;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = r8.m127a(r1);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r1 == 0) goto L_0x01cb;
    L_0x01c5:
        r1 = com.paypal.android.sdk.bn.m147b();	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r3.f75o = r1;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x01cb:
        r1 = com.paypal.android.sdk.bm.PPRiskDataIpAddrs;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = r8.m127a(r1);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r1 == 0) goto L_0x01da;
    L_0x01d3:
        r1 = 1;
        r1 = com.paypal.android.sdk.bn.m138a(r1);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r3.f76p = r1;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x01da:
        r1 = com.paypal.android.sdk.bm.PPRiskDataLine1Number;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = r8.m127a(r1);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r1 == 0) goto L_0x01ea;
    L_0x01e2:
        if (r9 == 0) goto L_0x051f;
    L_0x01e4:
        r1 = r0.getLine1Number();	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x01e8:
        r3.f78r = r1;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x01ea:
        r1 = com.paypal.android.sdk.bm.PPRiskDataLinkerId;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = r8.m127a(r1);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r1 == 0) goto L_0x01f8;
    L_0x01f2:
        r1 = com.paypal.android.sdk.bn.m133a();	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r3.f79s = r1;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x01f8:
        r1 = com.paypal.android.sdk.bm.PPRiskDataLocaleCountry;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = r8.m127a(r1);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r1 == 0) goto L_0x020a;
    L_0x0200:
        r1 = java.util.Locale.getDefault();	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = r1.getCountry();	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r3.f80t = r1;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x020a:
        r1 = com.paypal.android.sdk.bm.PPRiskDataLocaleLang;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = r8.m127a(r1);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r1 == 0) goto L_0x021c;
    L_0x0212:
        r1 = java.util.Locale.getDefault();	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = r1.getLanguage();	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r3.f81u = r1;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x021c:
        r1 = com.paypal.android.sdk.bm.PPRiskDataLocation;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = r8.m127a(r1);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r1 == 0) goto L_0x022b;
    L_0x0224:
        r1 = r14.f105p;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r1 != 0) goto L_0x0522;
    L_0x0228:
        r1 = 0;
    L_0x0229:
        r3.f82v = r1;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x022b:
        r1 = com.paypal.android.sdk.bm.PPRiskDataLocationAreaCode;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = r8.m127a(r1);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r1 == 0) goto L_0x0238;
    L_0x0233:
        if (r4 != 0) goto L_0x0535;
    L_0x0235:
        r1 = -1;
    L_0x0236:
        r3.f83w = r1;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x0238:
        r1 = com.paypal.android.sdk.bm.PPRiskDataMacAddrs;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = r8.m127a(r1);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r1 == 0) goto L_0x0245;
    L_0x0240:
        if (r7 != 0) goto L_0x053b;
    L_0x0242:
        r1 = 0;
    L_0x0243:
        r3.f84x = r1;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x0245:
        r1 = com.paypal.android.sdk.bm.PPRiskDataOsType;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = r8.m127a(r1);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r1 == 0) goto L_0x0251;
    L_0x024d:
        r1 = android.os.Build.VERSION.RELEASE;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r3.f86z = r1;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x0251:
        r1 = com.paypal.android.sdk.bm.PPRiskDataRiskCompSessionId;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = r8.m127a(r1);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r1 == 0) goto L_0x025f;
    L_0x0259:
        r1 = com.paypal.android.sdk.ax.m106b();	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r3.f36B = r1;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x025f:
        r1 = com.paypal.android.sdk.bm.PPRiskDataRoaming;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = r8.m127a(r1);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r1 == 0) goto L_0x0276;
    L_0x0267:
        r1 = new android.telephony.ServiceState;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1.<init>();	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = r1.getRoaming();	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = java.lang.Boolean.valueOf(r1);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r3.f37C = r1;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x0276:
        r1 = com.paypal.android.sdk.bm.PPRiskDataSimOperatorName;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = r8.m127a(r1);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r1 == 0) goto L_0x0284;
    L_0x027e:
        r1 = m76a(r0);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r3.f38D = r1;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x0284:
        r1 = com.paypal.android.sdk.bm.PPRiskDataSerialNumber;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = r8.m127a(r1);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r1 == 0) goto L_0x0294;
    L_0x028c:
        if (r9 == 0) goto L_0x0541;
    L_0x028e:
        r1 = r0.getSimSerialNumber();	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x0292:
        r3.f39E = r1;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x0294:
        r1 = android.os.Build.VERSION.SDK_INT;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r2 = 9;
        if (r1 < r2) goto L_0x029e;
    L_0x029a:
        r1 = android.os.Build.SERIAL;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r3.aa = r1;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x029e:
        r1 = com.paypal.android.sdk.bm.PPRiskDataSmsEnabled;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = r8.m127a(r1);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r1 == 0) goto L_0x02b8;
    L_0x02a6:
        r1 = r14.f92c;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = r1.getPackageManager();	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r2 = "android.hardware.telephony";
        r1 = r1.hasSystemFeature(r2);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = java.lang.Boolean.valueOf(r1);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r3.f40F = r1;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x02b8:
        r1 = com.paypal.android.sdk.bm.PPRiskDataSsid;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = r8.m127a(r1);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r1 == 0) goto L_0x02c5;
    L_0x02c0:
        if (r7 != 0) goto L_0x0544;
    L_0x02c2:
        r1 = 0;
    L_0x02c3:
        r3.f41G = r1;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x02c5:
        r1 = com.paypal.android.sdk.bm.PPRiskDataSubscriberId;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = r8.m127a(r1);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r1 == 0) goto L_0x02d5;
    L_0x02cd:
        if (r9 == 0) goto L_0x054a;
    L_0x02cf:
        r0 = r0.getSubscriberId();	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x02d3:
        r3.f42H = r0;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x02d5:
        r0 = com.paypal.android.sdk.bm.PPRiskDataTimestamp;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r0 = r8.m127a(r0);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r0 == 0) goto L_0x02e3;
    L_0x02dd:
        r0 = java.lang.System.currentTimeMillis();	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r3.f43I = r0;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x02e3:
        r0 = com.paypal.android.sdk.bm.PPRiskDataTotalStorageSpace;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r0 = r8.m127a(r0);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r0 == 0) goto L_0x02f1;
    L_0x02eb:
        r0 = com.paypal.android.sdk.bn.m153c();	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r3.f44J = r0;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x02f1:
        r0 = com.paypal.android.sdk.bm.PPRiskDataTzName;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r0 = r8.m127a(r0);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r0 == 0) goto L_0x030e;
    L_0x02f9:
        r0 = java.util.TimeZone.getDefault();	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = java.util.TimeZone.getDefault();	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = r1.inDaylightTime(r10);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r2 = 1;
        r4 = java.util.Locale.ENGLISH;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r0 = r0.getDisplayName(r1, r2, r4);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r3.f45K = r0;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x030e:
        r0 = com.paypal.android.sdk.bm.PPRiskDataIsDaylightSaving;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r0 = r8.m127a(r0);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r0 == 0) goto L_0x0324;
    L_0x0316:
        r0 = java.util.TimeZone.getDefault();	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r0 = r0.inDaylightTime(r10);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r0 = java.lang.Boolean.valueOf(r0);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r3.f46L = r0;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x0324:
        r0 = com.paypal.android.sdk.bm.PPRiskDataTimeZoneOffset;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r0 = r8.m127a(r0);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r0 == 0) goto L_0x033e;
    L_0x032c:
        r0 = java.util.TimeZone.getDefault();	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r4 = r10.getTime();	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r0 = r0.getOffset(r4);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r0 = java.lang.Integer.valueOf(r0);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r3.f47M = r0;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x033e:
        r0 = com.paypal.android.sdk.bm.PPRiskDataIsEmulator;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r0 = r8.m127a(r0);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r0 == 0) goto L_0x0389;
    L_0x0346:
        r0 = android.os.Build.BRAND;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = "generic";
        r0 = r0.equalsIgnoreCase(r1);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r0 != 0) goto L_0x0382;
    L_0x0350:
        r0 = android.os.Build.PRODUCT;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = "sdk";
        r0 = r0.equals(r1);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r0 != 0) goto L_0x0382;
    L_0x035a:
        r0 = android.os.Build.HARDWARE;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = "goldfish";
        r0 = r0.equals(r1);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r0 != 0) goto L_0x0382;
    L_0x0364:
        r0 = android.os.Build.FINGERPRINT;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = "generic";
        r0 = r0.startsWith(r1);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r0 != 0) goto L_0x0382;
    L_0x036e:
        r0 = android.os.Build.MANUFACTURER;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = "unknown";
        r0 = r0.equals(r1);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r0 != 0) goto L_0x0382;
    L_0x0378:
        r0 = android.os.Build.PRODUCT;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = ".*_?sdk_?.*";
        r0 = r0.matches(r1);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r0 == 0) goto L_0x054d;
    L_0x0382:
        r0 = 1;
    L_0x0383:
        r0 = java.lang.Boolean.valueOf(r0);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r3.f53S = r0;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x0389:
        r0 = com.paypal.android.sdk.bm.PPRiskDataIsRooted;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r0 = r8.m127a(r0);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r0 == 0) goto L_0x039b;
    L_0x0391:
        r0 = com.paypal.android.sdk.az.m110a();	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r0 = java.lang.Boolean.valueOf(r0);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r3.f54T = r0;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x039b:
        r0 = com.paypal.android.sdk.bm.PPRiskDataKnownApps;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r0 = r8.m127a(r0);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r0 == 0) goto L_0x03f1;
    L_0x03a3:
        r1 = new java.util.ArrayList;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1.<init>();	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r0 = r14.f100k;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r0 == 0) goto L_0x03e8;
    L_0x03ac:
        r0 = r14.f100k;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r0 = r0.m61f();	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r2 = r0.iterator();	 Catch:{ Exception -> 0x03df, RuntimeException -> 0x04ee }
    L_0x03b6:
        r0 = r2.hasNext();	 Catch:{ Exception -> 0x03df, RuntimeException -> 0x04ee }
        if (r0 == 0) goto L_0x03e8;
    L_0x03bc:
        r0 = r2.next();	 Catch:{ Exception -> 0x03df, RuntimeException -> 0x04ee }
        r0 = (java.lang.String) r0;	 Catch:{ Exception -> 0x03df, RuntimeException -> 0x04ee }
        r4 = r14.f92c;	 Catch:{ Exception -> 0x03df, RuntimeException -> 0x04ee }
        r4 = r4.getPackageManager();	 Catch:{ Exception -> 0x03df, RuntimeException -> 0x04ee }
        r5 = new android.content.Intent;	 Catch:{ Exception -> 0x03df, RuntimeException -> 0x04ee }
        r5.<init>();	 Catch:{ Exception -> 0x03df, RuntimeException -> 0x04ee }
        r6 = android.content.ComponentName.unflattenFromString(r0);	 Catch:{ Exception -> 0x03df, RuntimeException -> 0x04ee }
        r5 = r5.setComponent(r6);	 Catch:{ Exception -> 0x03df, RuntimeException -> 0x04ee }
        r4 = com.paypal.android.sdk.bn.m145a(r4, r5);	 Catch:{ Exception -> 0x03df, RuntimeException -> 0x04ee }
        if (r4 == 0) goto L_0x03b6;
    L_0x03db:
        r1.add(r0);	 Catch:{ Exception -> 0x03df, RuntimeException -> 0x04ee }
        goto L_0x03b6;
    L_0x03df:
        r0 = move-exception;
        r0 = f88b;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r2 = "knownApps error";
        r4 = 0;
        com.paypal.android.sdk.bn.m142a(r0, r2, r4);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x03e8:
        r0 = r1.size();	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r0 != 0) goto L_0x0550;
    L_0x03ee:
        r0 = 0;
    L_0x03ef:
        r3.f77q = r0;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x03f1:
        r0 = com.paypal.android.sdk.bm.PPRiskDataAppFirstInstallTime;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r0 = r8.m127a(r0);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r0 == 0) goto L_0x0401;
    L_0x03f9:
        r0 = r14.f92c;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r0 = m74a(r0);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r3.f56V = r0;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x0401:
        r0 = com.paypal.android.sdk.bm.PPRiskDataAppLastUpdateTime;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r0 = r8.m127a(r0);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r0 == 0) goto L_0x0411;
    L_0x0409:
        r0 = r14.f92c;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r0 = m81b(r0);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r3.f57W = r0;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x0411:
        r0 = r14.f104o;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r3.ah = r0;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r0 = com.paypal.android.sdk.bm.PPRiskDataGsfId;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r0 = r8.m127a(r0);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r0 == 0) goto L_0x0425;
    L_0x041d:
        r0 = r14.f92c;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r0 = com.paypal.android.sdk.bn.m148b(r0);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r3.ab = r0;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x0425:
        r0 = com.paypal.android.sdk.bm.PPRiskDataVPNSetting;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r0 = r8.m127a(r0);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r0 == 0) goto L_0x0433;
    L_0x042d:
        r0 = com.paypal.android.sdk.bn.m158e();	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r3.ad = r0;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x0433:
        r0 = com.paypal.android.sdk.bm.PPRiskDataProxySetting;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r0 = r8.m127a(r0);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r0 == 0) goto L_0x0441;
    L_0x043b:
        r0 = com.paypal.android.sdk.bn.m156d();	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r3.ac = r0;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x0441:
        r0 = com.paypal.android.sdk.bm.PPRiskDataAdvertisingIdentifier;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r0 = r8.m127a(r0);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r0 == 0) goto L_0x0451;
    L_0x0449:
        r0 = r14.f92c;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r0 = com.paypal.android.sdk.bn.m134a(r0, r3);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r3.f59Y = r0;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x0451:
        r0 = com.paypal.android.sdk.bm.PPRiskDataOsType;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r0 = r8.m127a(r0);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r0 != 0) goto L_0x045c;
    L_0x0459:
        r0 = 0;
        r3.f85y = r0;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x045c:
        r0 = com.paypal.android.sdk.bm.PPRiskDataCounter;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r0 = r8.m127a(r0);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r0 == 0) goto L_0x047b;
    L_0x0464:
        r0 = r3.f51Q;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = com.paypal.android.sdk.ay.PAYPAL;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = r1.m109a();	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r0 != r1) goto L_0x047b;
    L_0x046e:
        r0 = r14.f92c;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        com.paypal.android.sdk.bn.m155c(r0);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r0 = r14.f92c;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r0 = com.paypal.android.sdk.bn.m157d(r0);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r3.ae = r0;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x047b:
        r0 = new java.lang.StringBuilder;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r0.<init>();	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = r14.f93d;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r0 = r0.append(r1);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r4 = r3.f43I;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r0 = r0.append(r4);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r0 = r0.toString();	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r0 = com.paypal.android.sdk.bn.m136a(r0);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r3.af = r0;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r0 = r14.f110u;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r0 = com.paypal.android.sdk.bn.m150b(r0);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r3.ag = r0;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x049e:
        r0 = r3;
        goto L_0x0005;
    L_0x04a1:
        r1 = 0;
        r7 = r1;
        goto L_0x0040;
    L_0x04a5:
        r1 = 0;
        r6 = r1;
        goto L_0x004f;
    L_0x04a9:
        r1 = 0;
        goto L_0x0064;
    L_0x04ac:
        r1 = "none";
        r3.f35A = r1;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r2 = r4;
        r4 = r5;
        goto L_0x0095;
    L_0x04b4:
        r2 = "gsm";
        r3.f35A = r2;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r1 == 0) goto L_0x04ca;
    L_0x04ba:
        r1 = r0.getCellLocation();	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r2 = android.telephony.gsm.GsmCellLocation.class;
        r1 = com.paypal.android.sdk.bn.m131a(r1, r2);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = (android.telephony.gsm.GsmCellLocation) r1;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x04c6:
        r2 = r4;
        r4 = r1;
        goto L_0x0095;
    L_0x04ca:
        r1 = 0;
        goto L_0x04c6;
    L_0x04cc:
        r2 = "cdma";
        r3.f35A = r2;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        if (r1 == 0) goto L_0x04e2;
    L_0x04d2:
        r1 = r0.getCellLocation();	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r2 = android.telephony.cdma.CdmaCellLocation.class;
        r1 = com.paypal.android.sdk.bn.m131a(r1, r2);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = (android.telephony.cdma.CdmaCellLocation) r1;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
    L_0x04de:
        r2 = r1;
        r4 = r5;
        goto L_0x0095;
    L_0x04e2:
        r1 = 0;
        goto L_0x04de;
    L_0x04e4:
        r1 = r14.f108s;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = r1.m109a();	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r3.f51Q = r1;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        goto L_0x00cc;
    L_0x04ee:
        r0 = move-exception;
        throw r0;
    L_0x04f0:
        r1 = r2.getBaseStationId();	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        goto L_0x0127;
    L_0x04f6:
        r1 = r2.getNetworkId();	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        goto L_0x0134;
    L_0x04fc:
        r1 = r2.getSystemId();	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        goto L_0x0141;
    L_0x0502:
        r1 = r7.getBSSID();	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        goto L_0x014e;
    L_0x0508:
        r1 = r4.getCid();	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        goto L_0x015b;
    L_0x050e:
        r1 = r14.f100k;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1 = r1.m57b();	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        goto L_0x0178;
    L_0x0516:
        r1 = r6.getTypeName();	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        goto L_0x0185;
    L_0x051c:
        r1 = 0;
        goto L_0x0195;
    L_0x051f:
        r1 = 0;
        goto L_0x01e8;
    L_0x0522:
        r1 = new android.location.Location;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r2 = r14.f105p;	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        r1.<init>(r2);	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        goto L_0x0229;
    L_0x052b:
        r0 = move-exception;
        r1 = f88b;
        r2 = "Unknown error in RiskComponent";
        com.paypal.android.sdk.bn.m142a(r1, r2, r0);
        goto L_0x049e;
    L_0x0535:
        r1 = r4.getLac();	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        goto L_0x0236;
    L_0x053b:
        r1 = r7.getMacAddress();	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        goto L_0x0243;
    L_0x0541:
        r1 = 0;
        goto L_0x0292;
    L_0x0544:
        r1 = r7.getSSID();	 Catch:{ RuntimeException -> 0x04ee, Exception -> 0x052b }
        goto L_0x02c3;
    L_0x054a:
        r0 = 0;
        goto L_0x02d3;
    L_0x054d:
        r0 = 0;
        goto L_0x0383;
    L_0x0550:
        r0 = r1;
        goto L_0x03ef;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.paypal.android.sdk.at.l():com.paypal.android.sdk.as");
    }

    public final String m97a(Context context, String str, ay ayVar, String str2, Map map) {
        String str3;
        String a = bn.m137a(map, "RISK_MANAGER_CONF_URL", null);
        this.f111v = bn.m146a(map, "RISK_MANAGER_IS_START_ASYNC_SERVICE", Boolean.FALSE);
        String a2 = bn.m137a(map, "RISK_MANAGER_PAIRING_ID", null);
        this.f112w = bn.m137a(map, "RISK_MANAGER_NOTIF_TOKEN", null);
        f87a = (bc) bn.m132a(map, bc.class, "RISK_MANAGER_NETWORK_ADAPTER", new bf());
        boolean a3 = bn.m146a(map, "RISK_MANAGER_IS_DISABLE_REMOTE_CONFIG", Boolean.valueOf(false));
        this.f92c = context;
        this.f93d = bn.m154c(context, str);
        if (ayVar == null) {
            this.f108s = ay.UNKNOWN;
        } else {
            this.f108s = ayVar;
        }
        this.f109t = str2;
        this.f101l = null;
        this.f102m = null;
        this.f97h = 0;
        this.f96g = 0;
        if (a2 == null || a2.trim().length() == 0) {
            this.f110u = m91i();
        } else {
            bn.m139a(3, "PRD", "Using custom pairing id");
            this.f110u = a2.trim();
        }
        if (a == null) {
            try {
                str3 = "https://www.paypalobjects.com/webstatic/risk/dyson_config_android_v3.json";
            } catch (Throwable e) {
                bn.m142a(f88b, null, e);
            }
        } else {
            str3 = a;
        }
        this.f99j = str3;
        m102f();
        if (this.f107r == null) {
            this.f107r = new aw(this);
            LocationManager locationManager = (LocationManager) this.f92c.getSystemService(Param.LOCATION);
            if (locationManager != null) {
                onLocationChanged(bn.m129a(locationManager));
                if (locationManager.isProviderEnabled("network")) {
                    locationManager.requestLocationUpdates("network", 3600000, 10.0f, this);
                }
            }
        }
        m95k();
        m92j();
        m78a(new ap(this.f92c, !a3));
        return this.f110u;
    }

    public final void m98a(Message message) {
        try {
            switch (message.what) {
                case 0:
                    bn.m141a(f88b, "Dyson Async URL: " + message.obj);
                    return;
                case 1:
                    bn.m141a(f88b, "LogRiskMetadataRequest failed." + ((Exception) message.obj).getMessage());
                    return;
                case 2:
                    Object queryParameter;
                    String str = (String) message.obj;
                    bn.m141a(f88b, "LogRiskMetadataRequest Server returned: " + str);
                    try {
                        queryParameter = Uri.parse("?" + str).getQueryParameter("responseEnvelope.ack");
                    } catch (UnsupportedOperationException e) {
                        queryParameter = null;
                    }
                    if ("Success".equals(queryParameter)) {
                        bn.m141a(f88b, "LogRiskMetadataRequest Success");
                        return;
                    }
                    return;
                case 10:
                    bn.m141a(f88b, "Load Configuration URL: " + message.obj);
                    return;
                case 11:
                    bn.m141a(f88b, "LoadConfigurationRequest failed.");
                    return;
                case 12:
                    ap apVar = (ap) message.obj;
                    if (apVar != null) {
                        m78a(apVar);
                        return;
                    }
                    return;
                case 20:
                    bn.m141a(f88b, "Beacon URL: " + message.obj);
                    return;
                case 21:
                    bn.m141a(f88b, "BeaconRequest failed " + ((Exception) message.obj).getMessage());
                    return;
                case 22:
                    bn.m141a(f88b, "Beacon returned: " + message.obj);
                    return;
                default:
                    return;
            }
        } catch (Throwable e2) {
            bn.m142a(f88b, null, e2);
        }
        bn.m142a(f88b, null, e2);
    }

    public final void m99b() {
        new Timer().schedule(new av(this), 0);
    }

    public final JSONObject m100c() {
        ax.m104a();
        this.f101l = m96l();
        return this.f101l == null ? null : this.f101l.m71a();
    }

    public final void m101e() {
        ax.m104a();
        this.f101l = m96l();
        m79a(this.f101l, null);
    }

    public final void m102f() {
        bn.m141a(f88b, "Host activity detected");
        this.f98i = System.currentTimeMillis();
    }

    public final String m103g() {
        return m77a(null, null);
    }

    public void onLocationChanged(Location location) {
        if (location != null) {
            this.f105p = new Location(location);
            bn.m141a(f88b, "Location Update: " + location.toString());
        }
    }

    public void onProviderDisabled(String str) {
    }

    public void onProviderEnabled(String str) {
    }

    public void onStatusChanged(String str, int i, Bundle bundle) {
    }
}
