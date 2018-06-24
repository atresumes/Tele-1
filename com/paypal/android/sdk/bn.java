package com.paypal.android.sdk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.Looper;
import android.os.StatFs;
import android.util.Base64;
import android.util.Log;
import com.bumptech.glide.load.Key;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.json.JSONObject;

public final class bn {
    private static final boolean f185a = Boolean.valueOf(System.getProperty("dyson.debug.mode", Boolean.FALSE.toString())).booleanValue();
    private static final boolean f186b = Boolean.valueOf(System.getProperty("prd.debug.mode", Boolean.FALSE.toString())).booleanValue();
    private static final String f187c = (at.class.getSimpleName() + "." + bn.class.getSimpleName());
    private static final Uri f188d;

    static {
        Uri parse;
        try {
            parse = Uri.parse("content://com.google.android.gsf.gservices");
        } catch (Exception e) {
            parse = null;
        }
        f188d = parse;
    }

    private bn() {
    }

    public static Location m129a(LocationManager locationManager) {
        Location location = null;
        try {
            List providers = locationManager.getProviders(true);
            int size = providers.size() - 1;
            while (size >= 0) {
                try {
                    Location lastKnownLocation = locationManager.getLastKnownLocation((String) providers.get(size));
                    if (lastKnownLocation != null) {
                        return lastKnownLocation;
                    }
                    size--;
                    location = lastKnownLocation;
                } catch (RuntimeException e) {
                    return location;
                }
            }
            return location;
        } catch (RuntimeException e2) {
            return null;
        }
    }

    public static ao m130a(Context context) {
        ao aoVar = new ao();
        aoVar.m42a(context.getPackageName());
        try {
            aoVar.m44b(context.getPackageManager().getPackageInfo(aoVar.m41a(), 0).versionName);
        } catch (NameNotFoundException e) {
        }
        return aoVar;
    }

    public static Object m131a(Object obj, Class cls) {
        return (obj == null || !cls.isAssignableFrom(obj.getClass())) ? null : cls.cast(obj);
    }

    public static Object m132a(Map map, Class cls, String str, Object obj) {
        if (map == null) {
            return obj;
        }
        Object obj2 = map.get(str);
        if (obj2 == null) {
            return obj;
        }
        if (cls.isAssignableFrom(obj2.getClass())) {
            return cls.cast(obj2);
        }
        m140a(6, "PRD", "cannot parse data for " + str, new Exception("cannot parse data for " + str));
        return obj;
    }

    public static String m133a() {
        try {
            aq aqVar = new aq();
            aqVar.m65a(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.ebay.lid/");
            String str = "fb.bin";
            String b = aqVar.m67b(str);
            if (!"".equals(b.trim())) {
                return b;
            }
            b = m151b(Boolean.TRUE.booleanValue());
            aqVar.m66a(str, b.getBytes(Key.STRING_CHARSET_NAME));
            return b;
        } catch (Exception e) {
            return "";
        }
    }

    public static String m134a(Context context, as asVar) {
        try {
            if (VERSION.SDK_INT >= 9 && GooglePlayServicesUtil.isGooglePlayServicesAvailable(context) == 0) {
                if (Looper.myLooper() != Looper.getMainLooper()) {
                    return AdvertisingIdClient.getAdvertisingIdInfo(context).getId();
                }
                new Thread(new bo(context, asVar)).start();
            }
        } catch (Throwable th) {
            th.getLocalizedMessage();
        }
        return null;
    }

    public static String m135a(Context context, String str, String str2) {
        try {
            new StringBuilder("entering getMetadata loading name=").append(str);
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
            if (applicationInfo.metaData != null) {
                new StringBuilder("leaving getMetadata successfully loading name=").append(str);
                return applicationInfo.metaData.getString(str);
            }
        } catch (NameNotFoundException e) {
            new StringBuilder("load metadata in manifest failed, name=").append(str);
        }
        new StringBuilder("leaving getMetadata with default value,name=").append(str);
        return null;
    }

    public static String m136a(String str) {
        MessageDigest instance = MessageDigest.getInstance("SHA-256");
        instance.update(str.getBytes());
        byte[] digest = instance.digest();
        StringBuffer stringBuffer = new StringBuffer();
        for (byte b : digest) {
            stringBuffer.append(Integer.toString((b & 255) + 256, 16).substring(1));
        }
        return stringBuffer.toString().substring(0, 32);
    }

    public static String m137a(Map map, String str, String str2) {
        return (String) m132a(map, String.class, str, null);
    }

    public static List m138a(boolean z) {
        List arrayList = new ArrayList();
        try {
            Enumeration networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                Enumeration inetAddresses = ((NetworkInterface) networkInterfaces.nextElement()).getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress) inetAddresses.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String hostAddress = inetAddress.getHostAddress();
                        if (!(inetAddress instanceof Inet6Address)) {
                            arrayList.add(hostAddress);
                        } else if (z) {
                            arrayList.add(hostAddress);
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
        return arrayList;
    }

    public static void m139a(int i, String str, String str2) {
        if (f186b) {
            Log.println(i, str, str2);
        }
    }

    public static void m140a(int i, String str, String str2, Throwable th) {
        if (f186b) {
            Log.println(6, str, str2 + '\n' + Log.getStackTraceString(th));
        }
    }

    public static void m141a(String str, String str2) {
    }

    public static void m142a(String str, String str2, Throwable th) {
    }

    public static void m143a(String str, JSONObject jSONObject) {
        if (f185a && jSONObject != null) {
            jSONObject.toString();
        }
    }

    public static boolean m144a(Context context, String str) {
        try {
            return context.getApplicationContext().checkCallingOrSelfPermission(str) == 0;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean m145a(PackageManager packageManager, Intent intent) {
        List queryIntentActivities = packageManager.queryIntentActivities(intent, 65536);
        return queryIntentActivities != null && queryIntentActivities.size() > 0;
    }

    public static boolean m146a(Map map, String str, Boolean bool) {
        return ((Boolean) m132a(map, Boolean.class, str, (Object) bool)).booleanValue();
    }

    public static String m147b() {
        List a = m138a(false);
        return a.isEmpty() ? "" : (String) a.get(0);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String m148b(android.content.Context r6) {
        /*
        r1 = 1;
        r2 = 0;
        r0 = f188d;
        if (r0 == 0) goto L_0x0023;
    L_0x0006:
        r0 = "com.google.android.providers.gsf.permission.READ_GSERVICES";
        r0 = m144a(r6, r0);
        if (r0 == 0) goto L_0x0023;
    L_0x000e:
        r4 = new java.lang.String[r1];
        r0 = 0;
        r1 = "android_id";
        r4[r0] = r1;
        r0 = r6.getContentResolver();
        r1 = f188d;
        r3 = r2;
        r5 = r2;
        r1 = r0.query(r1, r2, r3, r4, r5);
        if (r1 != 0) goto L_0x0024;
    L_0x0023:
        return r2;
    L_0x0024:
        r0 = r1.moveToFirst();	 Catch:{ NumberFormatException -> 0x0046, all -> 0x004b }
        if (r0 == 0) goto L_0x0031;
    L_0x002a:
        r0 = r1.getColumnCount();	 Catch:{ NumberFormatException -> 0x0046, all -> 0x004b }
        r3 = 2;
        if (r0 >= r3) goto L_0x0035;
    L_0x0031:
        r1.close();
        goto L_0x0023;
    L_0x0035:
        r0 = 1;
        r0 = r1.getString(r0);	 Catch:{ NumberFormatException -> 0x0046, all -> 0x004b }
        r4 = java.lang.Long.parseLong(r0);	 Catch:{ NumberFormatException -> 0x0046, all -> 0x004b }
        r2 = java.lang.Long.toHexString(r4);	 Catch:{ NumberFormatException -> 0x0046, all -> 0x004b }
        r1.close();
        goto L_0x0023;
    L_0x0046:
        r0 = move-exception;
        r1.close();
        goto L_0x0023;
    L_0x004b:
        r0 = move-exception;
        r1.close();
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.paypal.android.sdk.bn.b(android.content.Context):java.lang.String");
    }

    public static String m149b(Context context, String str) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(context.getAssets().open(str)));
        StringBuilder stringBuilder = new StringBuilder();
        while (true) {
            String readLine = bufferedReader.readLine();
            if (readLine != null) {
                stringBuilder.append(readLine);
            } else {
                bufferedReader.close();
                return new String(Base64.decode(stringBuilder.toString(), 0), Key.STRING_CHARSET_NAME);
            }
        }
    }

    public static String m150b(String str) {
        int i = 0;
        if (str == null) {
            return null;
        }
        int parseInt;
        StringBuilder stringBuilder = new StringBuilder();
        ArrayList arrayList = new ArrayList();
        int i2 = 0;
        for (int i3 = 0; i3 < str.length(); i3++) {
            char charAt = str.charAt(i3);
            if ((charAt >= '0' && charAt <= '9') || ((charAt >= 'A' && charAt <= 'F') || (charAt >= 'a' && charAt <= 'f'))) {
                parseInt = Integer.parseInt(str.charAt(i3), 16);
                i2 += parseInt;
                arrayList.add(Integer.valueOf(parseInt));
            }
        }
        i2++;
        parseInt = arrayList.size() % 4;
        while (i < arrayList.size() - parseInt) {
            stringBuilder.append(Integer.toHexString((((Integer) arrayList.get((((Integer) arrayList.get(i + 3)).intValue() % 4) + i)).intValue() + i2) % 16));
            i += 4;
        }
        return stringBuilder.toString().equals("") ? null : stringBuilder.toString();
    }

    public static String m151b(boolean z) {
        return z ? UUID.randomUUID().toString() : UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static boolean m152b(String str, String str2) {
        String[] split = str.split("\\.");
        String[] split2 = str2.split("\\.");
        new StringBuilder("Cached version is ").append(str);
        new StringBuilder("default version is ").append(str2);
        int i = 0;
        while (i < split.length && i < split2.length && split[i].equals(split2[i])) {
            i++;
        }
        Integer valueOf = (i >= split.length || i >= split2.length) ? Integer.valueOf(Integer.signum(split.length - split2.length)) : Integer.valueOf(Integer.signum(Integer.valueOf(split[i]).compareTo(Integer.valueOf(split2[i]))));
        return valueOf.intValue() >= 0;
    }

    public static long m153c() {
        try {
            StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
            return ((long) statFs.getBlockCount()) * ((long) statFs.getBlockSize());
        } catch (IllegalArgumentException e) {
            e.getLocalizedMessage();
            return 0;
        }
    }

    public static String m154c(Context context, String str) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RiskManagerAG", 0);
        String string = sharedPreferences.getString("RiskManagerAG", "");
        Editor edit = sharedPreferences.edit();
        if (str != null && !str.equals(string)) {
            edit.putString("RiskManagerAG", str);
            edit.commit();
            return str;
        } else if (!string.equals("")) {
            return string;
        } else {
            str = m151b(Boolean.TRUE.booleanValue());
            edit.putString("RiskManagerAG", str);
            edit.commit();
            return str;
        }
    }

    public static void m155c(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RiskManagerCT", 0);
        int i = sharedPreferences.getInt("RiskManagerCT", 0);
        Editor edit = sharedPreferences.edit();
        int i2 = (i <= 0 || i >= ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED) ? 1 : i + 1;
        edit.putInt("RiskManagerCT", i2);
        edit.commit();
    }

    public static String m156d() {
        if (VERSION.SDK_INT >= 14) {
            String property = System.getProperty("http.proxyHost");
            if (property != null) {
                String property2 = System.getProperty("http.proxyPort");
                if (property2 != null) {
                    return "host=" + property + ",port=" + property2;
                }
            }
        }
        return null;
    }

    public static String m157d(Context context) {
        return context.getSharedPreferences("RiskManagerCT", 0).getInt("RiskManagerCT", 0);
    }

    public static String m158e() {
        try {
            Iterator it = Collections.list(NetworkInterface.getNetworkInterfaces()).iterator();
            while (it.hasNext()) {
                NetworkInterface networkInterface = (NetworkInterface) it.next();
                if (networkInterface.isUp() && networkInterface.getInterfaceAddresses().size() != 0) {
                    String name = networkInterface.getName();
                    if (name.startsWith("ppp") || name.startsWith("tun") || name.startsWith("tap")) {
                        return name;
                    }
                }
            }
        } catch (Exception e) {
        }
        return null;
    }
}
