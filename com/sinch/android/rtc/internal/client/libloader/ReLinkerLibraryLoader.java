package com.sinch.android.rtc.internal.client.libloader;

import android.content.Context;
import android.util.Log;
import com.sinch.android.rtc.internal.client.libloader.NativeLibLoader.LibraryLoader;
import com.sinch.relinker.ReLinker;
import com.sinch.relinker.ReLinker.Logger;
import com.sinch.relinker.ReLinkerInstance;

final class ReLinkerLibraryLoader implements LibraryLoader {
    private final ReLinkerInstance mReLinker;

    class GlobalHolder {
        static final ReLinkerLibraryLoader INSTANCE = new ReLinkerLibraryLoader();

        private GlobalHolder() {
        }
    }

    class C09231 implements Logger {
        C09231() {
        }

        public void log(String str) {
            Log.d("ReLinkerLibraryLoader", str);
        }
    }

    private ReLinkerLibraryLoader() {
        this.mReLinker = ReLinker.log(new C09231());
    }

    static LibraryLoader getGlobalInstance() {
        return GlobalHolder.INSTANCE;
    }

    public void loadLibrary(Context context, String str, String str2) {
        this.mReLinker.loadLibrary(context, str, str2);
    }
}
