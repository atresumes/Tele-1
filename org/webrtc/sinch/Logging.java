package org.webrtc.sinch;

import android.support.v4.internal.view.SupportMenu;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Logging {
    private static final Logger fallbackLogger = Logger.getLogger("org.webrtc.sinch.Logging");
    private static volatile boolean nativeLibLoaded;
    private static volatile boolean tracingEnabled;

    public enum Severity {
        LS_SENSITIVE,
        LS_VERBOSE,
        LS_INFO,
        LS_WARNING,
        LS_ERROR
    }

    public enum TraceLevel {
        TRACE_NONE(0),
        TRACE_STATEINFO(1),
        TRACE_WARNING(2),
        TRACE_ERROR(4),
        TRACE_CRITICAL(8),
        TRACE_APICALL(16),
        TRACE_DEFAULT(255),
        TRACE_MODULECALL(32),
        TRACE_MEMORY(256),
        TRACE_TIMER(512),
        TRACE_STREAM(1024),
        TRACE_DEBUG(2048),
        TRACE_INFO(4096),
        TRACE_TERSEINFO(8192),
        TRACE_ALL(SupportMenu.USER_MASK);
        
        public final int level;

        private TraceLevel(int i) {
            this.level = i;
        }
    }

    static {
        fallbackLogger.setLevel(Level.WARNING);
    }

    public static void m776d(String str, String str2) {
        log(Severity.LS_INFO, str, str2);
    }

    public static void m777e(String str, String str2) {
        log(Severity.LS_ERROR, str, str2);
    }

    public static void m778e(String str, String str2, Throwable th) {
        log(Severity.LS_ERROR, str, str2);
        log(Severity.LS_ERROR, str, th.toString());
        log(Severity.LS_ERROR, str, getStackTraceString(th));
    }

    public static void enableLogThreads() {
        if (nativeLibLoaded) {
            nativeEnableLogThreads();
        } else {
            fallbackLogger.log(Level.WARNING, "Cannot enable log thread because native lib not loaded.");
        }
    }

    public static void enableLogTimeStamps() {
        if (nativeLibLoaded) {
            nativeEnableLogTimeStamps();
        } else {
            fallbackLogger.log(Level.WARNING, "Cannot enable log timestamps because native lib not loaded.");
        }
    }

    public static synchronized void enableTracing(String str, EnumSet<TraceLevel> enumSet, Severity severity) {
        synchronized (Logging.class) {
            if (!nativeLibLoaded) {
                fallbackLogger.log(Level.WARNING, "Cannot enable tracing because native lib not loaded.");
            } else if (!tracingEnabled) {
                Iterator it = enumSet.iterator();
                int i = 0;
                while (it.hasNext()) {
                    i = ((TraceLevel) it.next()).level | i;
                }
                nativeEnableTracing(str, i, severity.ordinal());
                tracingEnabled = true;
            }
        }
    }

    private static String getStackTraceString(Throwable th) {
        if (th == null) {
            return "";
        }
        Writer stringWriter = new StringWriter();
        th.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    public static void log(Severity severity, String str, String str2) {
        if (tracingEnabled) {
            nativeLog(severity.ordinal(), str, str2);
            return;
        }
        Level level;
        switch (severity) {
            case LS_ERROR:
                level = Level.SEVERE;
                break;
            case LS_WARNING:
                level = Level.WARNING;
                break;
            case LS_INFO:
                level = Level.INFO;
                break;
            default:
                level = Level.FINE;
                break;
        }
        fallbackLogger.log(level, str + ": " + str2);
    }

    private static native void nativeEnableLogThreads();

    private static native void nativeEnableLogTimeStamps();

    private static native void nativeEnableTracing(String str, int i, int i2);

    private static native void nativeLog(int i, String str, String str2);

    public static void m779v(String str, String str2) {
        log(Severity.LS_VERBOSE, str, str2);
    }

    public static void m780w(String str, String str2) {
        log(Severity.LS_WARNING, str, str2);
    }

    public static void m781w(String str, String str2, Throwable th) {
        log(Severity.LS_WARNING, str, str2);
        log(Severity.LS_WARNING, str, th.toString());
        log(Severity.LS_WARNING, str, getStackTraceString(th));
    }
}
