package com.sinch.android.rtc.internal.client;

import android.content.Context;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.internal.AndroidLooperCallbackHandler;
import com.sinch.android.rtc.internal.CallbackHandler;
import com.sinch.android.rtc.internal.client.video.NullVideoController;

public class InternalSinchClientFactory {
    public static synchronized SinchClient createSinchClient(Context context, CallbackHandler callbackHandler, String str, String str2, String str3, String str4, String str5) {
        SinchClient defaultSinchClient;
        synchronized (InternalSinchClientFactory.class) {
            Context context2 = context;
            String str6 = str;
            String str7 = str2;
            String str8 = str5;
            String str9 = str3;
            String str10 = str4;
            defaultSinchClient = new DefaultSinchClient(context2, new ServiceFactory(callbackHandler), new DeviceIdReader(), str6, str7, str8, str9, str10, new ConnectivityListener(), new SinchDBPathHelper(context).getPathForInstantMessagingDatabase(str3, str), new SinchDBPathHelper(context).getPathForPersistenceServiceDatabase(str3, str), new NullVideoController(), callbackHandler);
        }
        return defaultSinchClient;
    }

    public static synchronized SinchClient createSinchClient(Context context, String str, String str2, String str3, String str4, String str5) {
        SinchClient defaultSinchClient;
        synchronized (InternalSinchClientFactory.class) {
            String pathForInstantMessagingDatabase = new SinchDBPathHelper(context).getPathForInstantMessagingDatabase(str3, str);
            String pathForPersistenceServiceDatabase = new SinchDBPathHelper(context).getPathForPersistenceServiceDatabase(str3, str);
            CallbackHandler androidLooperCallbackHandler = new AndroidLooperCallbackHandler();
            defaultSinchClient = new DefaultSinchClient(context, new ServiceFactory(androidLooperCallbackHandler), new DeviceIdReader(), str, str2, str5, str3, str4, new ConnectivityListener(), pathForInstantMessagingDatabase, pathForPersistenceServiceDatabase, null, androidLooperCallbackHandler);
        }
        return defaultSinchClient;
    }
}
