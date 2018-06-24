package com.sinch.android.rtc.internal.client.libloader;

import android.content.Context;
import android.util.Log;

public final class NativeLibLoader {
    static final /* synthetic */ boolean $assertionsDisabled = (!NativeLibLoader.class.desiredAssertionStatus());
    private static final String TAG = "NativeLibLoader";
    private static boolean sLoaded = false;
    private static LibraryLoader sLoader;

    public interface LibraryLoader {
        void loadLibrary(Context context, String str, String str2);
    }

    private static void _loadAllRequiredLibraries(Context context) {
        try {
            getLibraryLoader().loadLibrary(context, "sinch-android-rtc", "3.11.0");
            return;
        } catch (Throwable e) {
            Log.e(TAG, "Failed to load Sinch native libraries", e);
        } catch (Throwable th) {
        }
        throw e;
    }

    private static synchronized LibraryLoader getLibraryLoader() {
        LibraryLoader libraryLoader;
        synchronized (NativeLibLoader.class) {
            libraryLoader = sLoader;
            if (libraryLoader == null) {
                libraryLoader = ReLinkerLibraryLoader.getGlobalInstance();
            }
        }
        return libraryLoader;
    }

    public static synchronized void loadAllRequiredLibraries(Context context) {
        synchronized (NativeLibLoader.class) {
            if (context == null) {
                throw new IllegalArgumentException("Context must not be null.");
            }
            if (sLoaded) {
                Log.i(TAG, "Sinch native libraries already loaded, returning early");
            } else {
                _loadAllRequiredLibraries(context);
                sLoaded = true;
            }
        }
    }

    public static synchronized void setLibraryLoader(LibraryLoader libraryLoader) {
        synchronized (NativeLibLoader.class) {
            if (libraryLoader == null) {
                throw new IllegalArgumentException("LibraryLoader must not be null");
            }
            if (sLoader != null) {
                Log.w(TAG, "Re-assigning LibraryLoader");
            }
            sLoader = libraryLoader;
            if ($assertionsDisabled || sLoader != null) {
            } else {
                throw new AssertionError();
            }
        }
    }
}
