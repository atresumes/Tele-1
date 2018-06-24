package com.paypal.android.sdk;

import android.content.Context;
import android.util.Base64;
import android.util.Log;
import com.bumptech.glide.load.Key;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ap {
    private static final String f27a = ap.class.getSimpleName();
    private Context f28b;
    private String f29c;
    private JSONObject f30d;
    private boolean f31e;

    public ap(Context context, String str) {
        this(context, str, (byte) 0);
    }

    private ap(Context context, String str, byte b) {
        bn.m141a(f27a, "entering Configuration url loading");
        this.f28b = context;
        this.f29c = str;
        String o = m52o();
        if ("".equals(o)) {
            throw new IOException("No valid config found for " + str);
        }
        bn.m141a(f27a, "entering saveConfigDataToDisk");
        try {
            File file = new File(this.f28b.getFilesDir(), "CONFIG_DATA");
            File file2 = new File(this.f28b.getFilesDir(), "CONFIG_TIME");
            C0441d.m261a(file, o);
            C0441d.m261a(file2, String.valueOf(System.currentTimeMillis()));
            bn.m141a(f27a, "leaving saveConfigDataToDisk successfully");
        } catch (IOException e) {
            new StringBuilder("Failed to write config data: ").append(e.toString());
        }
        this.f30d = new JSONObject(o);
        bn.m141a(f27a, "leaving Configuration url loading");
    }

    public ap(Context context, boolean z) {
        this.f28b = context;
        this.f29c = null;
        this.f31e = z;
        bn.m139a(3, "PRD", "confIsUpdatable=" + Boolean.toString(this.f31e));
        this.f30d = m47j();
        bn.m141a(f27a, "Configuation initialize, dumping config");
        bn.m143a(f27a, this.f30d);
    }

    private JSONObject m45a(String str) {
        try {
            bn.m141a(f27a, "entering getIncrementalConfig");
            JSONObject jSONObject = new JSONObject(bn.m149b(this.f28b, str));
            bn.m141a(f27a, "leaving getIncrementalConfig");
            return jSONObject;
        } catch (Throwable e) {
            bn.m140a(6, "PRD", "Error while loading prdc Config " + str, e);
            return null;
        }
    }

    private static JSONObject m46a(JSONObject jSONObject, JSONObject jSONObject2) {
        try {
            bn.m141a(f27a, "entering mergeConfig");
            Iterator keys = jSONObject2.keys();
            while (keys.hasNext()) {
                String str = (String) keys.next();
                bn.m141a(f27a, "overridding " + str);
                jSONObject.put(str, jSONObject2.get(str));
            }
            bn.m141a(f27a, "leaving mergeConfig");
            return jSONObject;
        } catch (Throwable e) {
            bn.m140a(6, "PRD", "Error encountered while applying prdc Config", e);
            return null;
        }
    }

    private JSONObject m47j() {
        try {
            JSONObject l = m49l();
            if (l != null) {
                if (bn.m152b(l.optString("conf_version", ""), "3.0")) {
                    if (this.f31e) {
                        if ((System.currentTimeMillis() > Long.parseLong(m55r()) + (l.optLong("conf_refresh_time_interval", 0) * 1000) ? 1 : null) != null) {
                            m51n();
                        }
                    }
                    bn.m141a(f27a, "Using cached config");
                    return l;
                }
                m54q();
            }
            l = m48k();
            if (l == null) {
                Log.e(f27a, "default Configuration loading failed,Using hardcoded config");
                return m50m();
            }
            String a = bn.m135a(this.f28b, "prdc", null);
            if (a == null) {
                if (this.f31e) {
                    m51n();
                }
                bn.m139a(3, "PRD", "prdc field not configured, using default config");
                return l;
            }
            bn.m139a(3, "PRD", "prdc field is configured, loading path:" + a);
            JSONObject a2 = m45a(a);
            if (a2 == null) {
                bn.m139a(6, "PRD", "prdc Configuration loading failed, using default config");
                return l;
            }
            a2 = m46a(l, a2);
            if (a2 == null) {
                bn.m139a(6, "PRD", "applying prdc Configuration failed, using default config");
                return l;
            }
            bn.m139a(3, "PRD", "prdc configuration loaded successfully");
            return a2;
        } catch (Throwable e) {
            bn.m140a(6, "PRD", "Severe Error while loading config, using hard code version", e);
            return m50m();
        }
    }

    private static JSONObject m48k() {
        bn.m141a(f27a, "entering getDefaultConfigurations");
        try {
            String str = new String(Base64.decode("eyAiY29uZl92ZXJzaW9uIjogIjMuMCIsImFzeW5jX3VwZGF0ZV90aW1lX2ludGVydmFsIjogMzYwMCwgImZvcmNlZF9mdWxsX3VwZGF0ZV90aW1lX2ludGVydmFsIjogMTgwMCwgImNvbmZfcmVmcmVzaF90aW1lX2ludGVydmFsIjogODY0MDAsICJhbmRyb2lkX2FwcHNfdG9fY2hlY2siOiBbICJjb20uZWJheS5jbGFzc2lmaWVkcy9jb20uZWJheS5hcHAuTWFpblRhYkFjdGl2aXR5IiwgImNvbS5lYmF5Lm1vYmlsZS9jb20uZWJheS5tb2JpbGUuYWN0aXZpdGllcy5lQmF5IiwgImNvbS5lYmF5LnJlZGxhc2VyL2NvbS5lYmF5LnJlZGxhc2VyLlNjYW5uZWRJdGVtc0FjdGl2aXR5IiwgImNvbS5taWxvLmFuZHJvaWQvY29tLm1pbG8uYW5kcm9pZC5hY3Rpdml0eS5Ib21lQWN0aXZpdHkiLCAiY29tLnBheXBhbC5hbmRyb2lkLnAycG1vYmlsZS9jb20ucGF5cGFsLmFuZHJvaWQucDJwbW9iaWxlLmFjdGl2aXR5LlNlbmRNb25leUFjdGl2aXR5IiwgImNvbS5yZW50L2NvbS5yZW50LmFjdGl2aXRpZXMuc2Vzc2lvbi5BY3Rpdml0eUhvbWUiLCAiY29tLnN0dWJodWIvY29tLnN0dWJodWIuQWJvdXQiLCAiY29tLnVsb2NhdGUvY29tLnVsb2NhdGUuYWN0aXZpdGllcy5TZXR0aW5ncyIsICJjb20ubm9zaHVmb3UuYW5kcm9pZC5zdS9jb20ubm9zaHVmb3UuYW5kcm9pZC5zdS5TdSIsICJzdGVyaWNzb24uYnVzeWJveC9zdGVyaWNzb24uYnVzeWJveC5BY3Rpdml0eS5NYWluQWN0aXZpdHkiLCAib3JnLnByb3h5ZHJvaWQvb3JnLnByb3h5ZHJvaWQuUHJveHlEcm9pZCIsICJjb20uYWVkLmRyb2lkdnBuL2NvbS5hZWQuZHJvaWR2cG4uTWFpbkdVSSIsICJuZXQub3BlbnZwbi5vcGVudnBuL25ldC5vcGVudnBuLm9wZW52cG4uT3BlblZQTkNsaWVudCIsICJjb20ucGhvbmVhcHBzOTkuYWFiaXByb3h5L2NvbS5waG9uZWFwcHM5OS5hYWJpcHJveHkuT3Jib3QiLCAiY29tLmV2YW5oZS5wcm94eW1hbmFnZXIucHJvL2NvbS5ldmFuaGUucHJveHltYW5hZ2VyLk1haW5BY3Rpdml0eSIsICJjb20uZXZhbmhlLnByb3h5bWFuYWdlci9jb20uZXZhbmhlLnByb3h5bWFuYWdlci5NYWluQWN0aXZpdHkiLCAiY29tLmFuZHJvbW8uZGV2MzA5MzYuYXBwNzYxOTgvY29tLmFuZHJvbW8uZGV2MzA5MzYuYXBwNzYxOTguQW5kcm9tb0Rhc2hib2FyZEFjdGl2aXR5IiwgImNvbS5tZ3JhbmphLmF1dG9wcm94eV9saXRlL2NvbS5tZ3JhbmphLmF1dG9wcm94eV9saXRlLlByb3h5TGlzdEFjdGl2aXR5IiwgImNvbS52cG5vbmVjbGljay5hbmRyb2lkL2NvbS52cG5vbmVjbGljay5hbmRyb2lkLk1haW5BY3Rpdml0eSIsICJuZXQuaGlkZW1hbi9uZXQuaGlkZW1hbi5TdGFydGVyQWN0aXZpdHkiLCAiY29tLmRvZW50ZXIuYW5kcm9pZC52cG4uZml2ZXZwbi9jb20uZG9lbnRlci5hbmRyb2lkLnZwbi5maXZldnBuLkZpdmVWcG4iLCAiY29tLnRpZ2VydnBucy5hbmRyb2lkL2NvbS50aWdlcnZwbnMuYW5kcm9pZC5NYWluQWN0aXZpdHkiLCAiY29tLnBhbmRhcG93LnZwbi9jb20ucGFuZGFwb3cudnBuLlBhbmRhUG93IiwgImNvbS5leHByZXNzdnBuLnZwbi9jb20uZXhwcmVzc3Zwbi52cG4uTWFpbkFjdGl2aXR5IiwgImNvbS5sb25kb250cnVzdG1lZGlhLnZwbi9jb20ubG9uZG9udHJ1c3RtZWRpYS52cG4uVnBuU2VydmljZUFjdGl2aXR5IiwgImZyLm1lbGVjb20uVlBOUFBUUC52MTAxL2ZyLm1lbGVjb20uVlBOUFBUUC52MTAxLlNwbGFzaFNjcmVlbiIsICJjb20uY2hlY2twb2ludC5WUE4vY29tLmNoZWNrcG9pbnQuVlBOLk1haW5IYW5kbGVyIiwgImNvbS50dW5uZWxiZWFyLmFuZHJvaWQvY29tLnR1bm5lbGJlYXIuYW5kcm9pZC5UYmVhck1haW5BY3Rpdml0eSIsICJkZS5ibGlua3Qub3BlbnZwbi9kZS5ibGlua3Qub3BlbnZwbi5NYWluQWN0aXZpdHkiLCAib3JnLmFqZWplLmZha2Vsb2NhdGlvbi9vcmcuYWplamUuZmFrZWxvY2F0aW9uLkZha2UiLCAiY29tLmxleGEuZmFrZWdwcy9jb20ubGV4YS5mYWtlZ3BzLlBpY2tQb2ludCIsICJjb20uZm9yZ290dGVucHJvamVjdHMubW9ja2xvY2F0aW9ucy9jb20uZm9yZ290dGVucHJvamVjdHMubW9ja2xvY2F0aW9ucy5NYWluIiwgImtyLndvb3QwcGlhLmdwcy9rci53b290MHBpYS5ncHMuQ2F0Y2hNZUlmVUNhbiIsICJjb20ubXkuZmFrZS5sb2NhdGlvbi9jb20ubXkuZmFrZS5sb2NhdGlvbi5jb20ubXkuZmFrZS5sb2NhdGlvbiIsICJqcC5uZXRhcnQuYXJzdGFsa2luZy9qcC5uZXRhcnQuYXJzdGFsa2luZy5NeVByZWZlcmVuY2VBY3Rpdml0eSIsICJsb2NhdGlvblBsYXkuR1BTQ2hlYXRGcmVlL2xvY2F0aW9uUGxheS5HUFNDaGVhdEZyZWUuQWN0aXZpdHlTbWFydExvY2F0aW9uIiwgIm9yZy5nb29kZXYubGF0aXR1ZGUvb3JnLmdvb2Rldi5sYXRpdHVkZS5MYXRpdHVkZUFjdGl2aXR5IiwgImNvbS5zY2hlZmZzYmxlbmQuZGV2aWNlc3Bvb2YvY29tLnNjaGVmZnNibGVuZC5kZXZpY2VzcG9vZi5EZXZpY2VTcG9vZkFjdGl2aXR5IiwgImNvbS5wcm94eUJyb3dzZXIvY29tLnByb3h5QnJvd3Nlci5OZXdQcm94eUJyb3dzZXJBY3Rpdml0eSIsICJjb20uaWNlY29sZGFwcHMucHJveHlzZXJ2ZXJwcm8vY29tLmljZWNvbGRhcHBzLnByb3h5c2VydmVycHJvLnZpZXdTdGFydCIsICJob3RzcG90c2hpZWxkLmFuZHJvaWQudnBuL2NvbS5hbmNob3JmcmVlLnVpLkhvdFNwb3RTaGllbGQiLCAiY29tLmRvZW50ZXIub25ldnBuL2NvbS5kb2VudGVyLm9uZXZwbi5WcG5TZXR0aW5ncyIsICJjb20ueWVzdnBuLmVuL2NvbS55ZXN2cG4uTWFpblRhYiIsICJjb20ub2ZmaWNld3l6ZS5wbHV0b3Zwbi9jb20ub2ZmaWNld3l6ZS5wbHV0b3Zwbi5Ib21lQWN0aXZpdHkiLCAib3JnLmFqZWplLmxvY2F0aW9uc3Bvb2ZlcnByby9vcmcuYWplamUubG9jYXRpb25zcG9vZmVycHJvLkZha2UiLCAibG9jYXRpb25QbGF5LkdQU0NoZWF0L2xvY2F0aW9uUGxheS5HUFNDaGVhdC5BY3Rpdml0eVNtYXJ0TG9jYXRpb24iIF0sICJsb2NhdGlvbl9taW5fYWNjdXJhY3kiOiA1MDAsICJsb2NhdGlvbl9tYXhfY2FjaGVfYWdlIjogMTgwMCwgInNlbmRfb25fYXBwX3N0YXJ0IjogMSwgImVuZHBvaW50X3VybCI6ICJodHRwczovL3N2Y3MucGF5cGFsLmNvbS9BY2Nlc3NDb250cm9sL0xvZ1Jpc2tNZXRhZGF0YSIsICJpbnRlcm5hbF9jYWNoZV9tYXhfYWdlIjogMzAsICJjb21wX3RpbWVvdXQiOiA2MDAgfQ==", 0), Key.STRING_CHARSET_NAME);
            bn.m141a(f27a, "leaving getDefaultConfigurations, Default Conf load succeed");
            return new JSONObject(str);
        } catch (Throwable e) {
            bn.m140a(6, "PRD", "Read default config file exception.", e);
            bn.m141a(f27a, "leaving getDefaultConfigurations,returning null");
            return null;
        }
    }

    private JSONObject m49l() {
        bn.m141a(f27a, "entering getCachedConfiguration");
        try {
            String p = m53p();
            if (!"".equals(p)) {
                bn.m141a(f27a, "leaving getCachedConfiguration,cached config load succeed");
                return new JSONObject(p);
            }
        } catch (Throwable e) {
            bn.m142a(f27a, "JSON Exception in creating config file", e);
        }
        bn.m141a(f27a, "leaving getCachedConfiguration,cached config load failed");
        return null;
    }

    private static JSONObject m50m() {
        bn.m141a(f27a, "entering getHardcodedConfig");
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("conf_version", "3.0");
            jSONObject.put("async_update_time_interval", 3600);
            jSONObject.put("forced_full_update_time_interval", 1800);
            jSONObject.put("conf_refresh_time_interval", 86400);
            jSONObject.put("location_min_accuracy", 500);
            jSONObject.put("location_max_cache_age", 1800);
            jSONObject.put("endpoint_url", "https://svcs.paypal.com/AccessControl/LogRiskMetadata");
        } catch (JSONException e) {
        }
        bn.m141a(f27a, "leaving getHardcodedConfig");
        return jSONObject;
    }

    private static void m51n() {
        bn.m141a(f27a, "Loading web config");
        at.m75a().m99b();
    }

    private String m52o() {
        Closeable bufferedReader;
        Throwable th;
        Closeable closeable = null;
        bn.m141a(f27a, "entering getRemoteConfig");
        StringBuilder stringBuilder = new StringBuilder();
        try {
            Closeable openStream = new URL(this.f29c).openStream();
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(openStream, Key.STRING_CHARSET_NAME));
                while (true) {
                    try {
                        String readLine = bufferedReader.readLine();
                        if (readLine != null) {
                            stringBuilder.append(readLine);
                        } else {
                            C0441d.m260a(openStream);
                            C0441d.m260a(bufferedReader);
                            bn.m141a(f27a, "leaving getRemoteConfig successfully");
                            return stringBuilder.toString();
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        closeable = openStream;
                    }
                }
            } catch (Throwable th3) {
                th = th3;
                bufferedReader = null;
                closeable = openStream;
                C0441d.m260a(closeable);
                C0441d.m260a(bufferedReader);
                throw th;
            }
        } catch (Throwable th4) {
            th = th4;
            bufferedReader = null;
            C0441d.m260a(closeable);
            C0441d.m260a(bufferedReader);
            throw th;
        }
    }

    private String m53p() {
        try {
            return C0441d.m256a(new File(this.f28b.getFilesDir(), "CONFIG_DATA"));
        } catch (Throwable e) {
            bn.m142a(f27a, "Load cached config failed", e);
            return "";
        }
    }

    private boolean m54q() {
        try {
            bn.m141a(f27a, "entering deleteCachedConfigDataFromDisk");
            File file = new File(this.f28b.getFilesDir(), "CONFIG_DATA");
            File file2 = new File(this.f28b.getFilesDir(), "CONFIG_TIME");
            if (file.exists()) {
                bn.m141a(f27a, "cached Config Data found, deleting");
                file.delete();
            }
            if (file2.exists()) {
                bn.m141a(f27a, "cached Config Time found, deleting");
                file.delete();
            }
            bn.m141a(f27a, "leaving deleteCachedConfigDataFromDisk");
            return true;
        } catch (Throwable e) {
            bn.m142a(f27a, "error encountered while deleteCachedConfigData", e);
            return false;
        }
    }

    private String m55r() {
        try {
            return C0441d.m256a(new File(this.f28b.getFilesDir(), "CONFIG_TIME"));
        } catch (IOException e) {
            return "";
        }
    }

    public final String m56a() {
        return this.f29c;
    }

    public final String m57b() {
        return this.f30d.optString("conf_version", "");
    }

    public final long m58c() {
        return this.f30d.optLong("async_update_time_interval", 0);
    }

    public final long m59d() {
        return this.f30d.optLong("forced_full_update_time_interval", 0);
    }

    public final long m60e() {
        return this.f30d.optLong("comp_timeout", 0);
    }

    public final List m61f() {
        List arrayList = new ArrayList();
        JSONArray optJSONArray = this.f30d.optJSONArray("android_apps_to_check");
        int i = 0;
        while (optJSONArray != null && i < optJSONArray.length()) {
            arrayList.add(optJSONArray.getString(i));
            i++;
        }
        return arrayList;
    }

    public final String m62g() {
        return this.f30d.optString("endpoint_url", null);
    }

    public final boolean m63h() {
        return this.f30d.optBoolean("endpoint_is_stage", false);
    }

    public final bl m64i() {
        try {
            String optString = this.f30d.optString("CDS", "");
            if (optString == null || "".equals(optString)) {
                bn.m139a(3, "PRD", "No CDS is configured, enabling all variables");
                return bl.f130a;
            }
            bn.m139a(3, "PRD", "CDS field was found");
            return new bl(optString.trim());
        } catch (Throwable e) {
            bn.m140a(6, "PRD", "Failed to decode CDS", e);
            return bl.f130a;
        }
    }
}
