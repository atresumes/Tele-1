package com.talktoangel.gts.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import com.talktoangel.gts.userauth.OTPActivity;

public class IncomingSms extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            try {
                Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (Object obj : pdusObj) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) obj);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String message = currentMessage.getDisplayMessageBody();
                    Log.i("senderNum", phoneNumber);
                    try {
                        if (phoneNumber.contains("GTSIND")) {
                            new OTPActivity().receivedSms(message.replaceAll("\\D+", ""));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
}
