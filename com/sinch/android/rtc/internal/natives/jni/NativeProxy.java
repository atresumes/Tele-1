package com.sinch.android.rtc.internal.natives.jni;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class NativeProxy {
    private static Map<Long, WeakReference<Object>> instances = new HashMap();
    private static ReferenceQueue<Object> reaped = new ReferenceQueue();
    private long nativeAddress;

    public NativeProxy(long j) {
        this.nativeAddress = j;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> T get(long r6, java.lang.Class r8) {
        /*
        r1 = 0;
        r2 = instances;
        monitor-enter(r2);
    L_0x0004:
        r0 = reaped;	 Catch:{ all -> 0x0012 }
        r0 = r0.poll();	 Catch:{ all -> 0x0012 }
        if (r0 == 0) goto L_0x0015;
    L_0x000c:
        r3 = instances;	 Catch:{ all -> 0x0012 }
        r3.remove(r0);	 Catch:{ all -> 0x0012 }
        goto L_0x0004;
    L_0x0012:
        r0 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x0012 }
        throw r0;
    L_0x0015:
        r0 = instances;	 Catch:{ all -> 0x0012 }
        r3 = java.lang.Long.valueOf(r6);	 Catch:{ all -> 0x0012 }
        r0 = r0.containsKey(r3);	 Catch:{ all -> 0x0012 }
        if (r0 == 0) goto L_0x0081;
    L_0x0021:
        r0 = instances;	 Catch:{ all -> 0x0012 }
        r3 = java.lang.Long.valueOf(r6);	 Catch:{ all -> 0x0012 }
        r0 = r0.get(r3);	 Catch:{ all -> 0x0012 }
        r0 = (java.lang.ref.WeakReference) r0;	 Catch:{ all -> 0x0012 }
        r0 = r0.get();	 Catch:{ all -> 0x0012 }
        if (r0 == 0) goto L_0x0081;
    L_0x0033:
        r3 = r0.getClass();	 Catch:{ all -> 0x0012 }
        if (r3 != r8) goto L_0x003b;
    L_0x0039:
        monitor-exit(r2);	 Catch:{ all -> 0x0012 }
    L_0x003a:
        return r0;
    L_0x003b:
        r3 = "NativeProxy";
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0012 }
        r4.<init>();	 Catch:{ all -> 0x0012 }
        r5 = "instances already contains weak ref to object for address ";
        r4 = r4.append(r5);	 Catch:{ all -> 0x0012 }
        r4 = r4.append(r6);	 Catch:{ all -> 0x0012 }
        r5 = " of type ";
        r4 = r4.append(r5);	 Catch:{ all -> 0x0012 }
        r5 = r0.getClass();	 Catch:{ all -> 0x0012 }
        r5 = r5.getName();	 Catch:{ all -> 0x0012 }
        r4 = r4.append(r5);	 Catch:{ all -> 0x0012 }
        r5 = ", but ";
        r4 = r4.append(r5);	 Catch:{ all -> 0x0012 }
        r5 = r8.getName();	 Catch:{ all -> 0x0012 }
        r4 = r4.append(r5);	 Catch:{ all -> 0x0012 }
        r5 = " was expected.";
        r4 = r4.append(r5);	 Catch:{ all -> 0x0012 }
        r4 = r4.toString();	 Catch:{ all -> 0x0012 }
        android.util.Log.v(r3, r4);	 Catch:{ all -> 0x0012 }
        r0 = (com.sinch.android.rtc.internal.natives.jni.NativeProxy) r0;	 Catch:{ all -> 0x0012 }
        r0.invalidate();	 Catch:{ all -> 0x0012 }
        monitor-exit(r2);	 Catch:{ all -> 0x0012 }
        r0 = r1;
        goto L_0x003a;
    L_0x0081:
        monitor-exit(r2);	 Catch:{ all -> 0x0012 }
        r0 = r1;
        goto L_0x003a;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sinch.android.rtc.internal.natives.jni.NativeProxy.get(long, java.lang.Class):T");
    }

    protected static void put(long j, Object obj) {
        synchronized (instances) {
            instances.put(Long.valueOf(j), new WeakReference(obj, reaped));
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        return this.nativeAddress == ((NativeProxy) obj).nativeAddress;
    }

    public long getNativeAddress() {
        return this.nativeAddress;
    }

    public int hashCode() {
        return ((int) (this.nativeAddress ^ (this.nativeAddress >>> 32))) + 31;
    }

    public void invalidate() {
        synchronized (instances) {
            instances.remove(Long.valueOf(this.nativeAddress));
            this.nativeAddress = 0;
        }
    }
}
