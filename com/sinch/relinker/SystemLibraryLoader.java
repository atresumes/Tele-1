package com.sinch.relinker;

import android.os.Build;
import android.os.Build.VERSION;
import com.sinch.relinker.ReLinker.LibraryLoader;

final class SystemLibraryLoader implements LibraryLoader {
    SystemLibraryLoader() {
    }

    public void loadLibrary(String str) {
        System.loadLibrary(str);
    }

    public void loadPath(String str) {
        System.load(str);
    }

    public String mapLibraryName(String str) {
        return (str.startsWith("lib") && str.endsWith(".so")) ? str : System.mapLibraryName(str);
    }

    public String[] supportedAbis() {
        if (VERSION.SDK_INT >= 21 && Build.SUPPORTED_ABIS.length > 0) {
            return Build.SUPPORTED_ABIS;
        }
        if (TextUtils.isEmpty(Build.CPU_ABI2)) {
            return new String[]{Build.CPU_ABI};
        }
        return new String[]{Build.CPU_ABI, Build.CPU_ABI2};
    }

    public String unmapLibraryName(String str) {
        return str.substring(3, str.length() - 3);
    }
}
