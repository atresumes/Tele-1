package com.sinch.android.rtc.internal.client;

import android.content.Context;
import android.util.Log;
import java.io.File;

class SinchDBPathHelper {
    private static final String DATABASE_SUBDIR = (DefaultSinchClient.GCM_PAYLOAD_TAG_SINCH + File.separator + "db");
    private static final String MESSAGING_DATABASE_FILENAME = "db.sqlite";
    private static final String PERSISTENCE_DATABASE_FILENAME = "persistence_db.sqlite";
    private final Context mContext;

    public SinchDBPathHelper(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context must not be null");
        }
        this.mContext = context;
    }

    private static String getDatabasePath(Context context, String str, String str2) {
        String str3;
        boolean z;
        File filesDir = context.getFilesDir();
        String str4 = "";
        boolean z2 = filesDir != null;
        boolean z3;
        if (z2) {
            str4 = filesDir.getAbsolutePath() + File.separator + DATABASE_SUBDIR + File.separator + str;
            if (!new File(str4).isDirectory()) {
                z2 = new File(str4).mkdirs();
            }
            z3 = z2;
            str3 = str4 + File.separator + str2;
            z = z3;
        } else {
            z3 = z2;
            str3 = str4;
            z = z3;
        }
        if (z) {
            return str3;
        }
        str3 = "";
        Log.e("Sinch", "Unable to get internal files directory, falling back to temporary database");
        return str3;
    }

    private static String persistentStoragePathForUser(String str, String str2) {
        return Sha1Utils.bytesToHex(Sha1Utils.sha1(str + "_" + str2)).substring(0, 8);
    }

    public String getPathForInstantMessagingDatabase(String str, String str2) {
        return getDatabasePath(this.mContext, persistentStoragePathForUser(str, str2), MESSAGING_DATABASE_FILENAME);
    }

    public String getPathForPersistenceServiceDatabase(String str, String str2) {
        return getDatabasePath(this.mContext, persistentStoragePathForUser(str, str2), PERSISTENCE_DATABASE_FILENAME);
    }
}
