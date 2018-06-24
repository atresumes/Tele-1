package com.talktoangel.gts.sinch;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import com.sinch.android.rtc.AudioController;
import com.sinch.android.rtc.ClientRegistration;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchClientListener;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.video.VideoController;

public class SinchService extends Service {
    private static final String APP_KEY = "895561d7-9ffc-4829-b933-7f216c47f52a";
    private static final String APP_SECRET = "QYuLC0YMSUu0JbMl/8320Q==";
    public static final String CALL_ID = "CALL_ID";
    private static final String ENVIRONMENT = "sandbox.sinch.com";
    static final String TAG = SinchService.class.getSimpleName();
    private StartFailedListener mListener;
    private SinchClient mSinchClient;
    private SinchServiceInterface mSinchServiceInterface = new SinchServiceInterface();
    private String mUserId;

    public class SinchServiceInterface extends Binder {
        public Call callUser(String userId) {
            if (SinchService.this.mSinchClient == null) {
                return null;
            }
            return SinchService.this.mSinchClient.getCallClient().callUser(userId);
        }

        public Call callUserVideo(String userId) {
            return SinchService.this.mSinchClient.getCallClient().callUserVideo(userId);
        }

        public String getUserName() {
            return SinchService.this.mUserId;
        }

        public boolean isStarted() {
            return SinchService.this.isStarted();
        }

        public void startClient(String userName) {
            SinchService.this.start(userName);
        }

        public void stopClient() {
            SinchService.this.stop();
        }

        public void setStartListener(StartFailedListener listener) {
            SinchService.this.mListener = listener;
        }

        public Call getCall(String callId) {
            return SinchService.this.mSinchClient.getCallClient().getCall(callId);
        }

        public VideoController getVideoController() {
            if (isStarted()) {
                return SinchService.this.mSinchClient.getVideoController();
            }
            return null;
        }

        public AudioController getAudioController() {
            if (isStarted()) {
                return SinchService.this.mSinchClient.getAudioController();
            }
            return null;
        }
    }

    public interface StartFailedListener {
        void onStartFailed(SinchError sinchError);

        void onStarted();
    }

    private class MySinchClientListener implements SinchClientListener {
        private MySinchClientListener() {
        }

        public void onClientFailed(SinchClient client, SinchError error) {
            if (SinchService.this.mListener != null) {
                SinchService.this.mListener.onStartFailed(error);
            }
            SinchService.this.mSinchClient.terminate();
            SinchService.this.mSinchClient = null;
        }

        public void onClientStarted(SinchClient client) {
            Log.d(SinchService.TAG, "SinchClient started");
            if (SinchService.this.mListener != null) {
                SinchService.this.mListener.onStarted();
            }
        }

        public void onClientStopped(SinchClient client) {
            Log.e(SinchService.TAG, "SinchClient stopped");
        }

        public void onLogMessage(int level, String area, String message) {
            switch (level) {
                case 2:
                    Log.v(area, message);
                    return;
                case 3:
                    Log.d(area, message);
                    return;
                case 4:
                    Log.i(area, message);
                    return;
                case 5:
                    Log.w(area, message);
                    return;
                case 6:
                    Log.e(area, message);
                    return;
                default:
                    return;
            }
        }

        public void onRegistrationCredentialsRequired(SinchClient client, ClientRegistration clientRegistration) {
        }
    }

    private class SinchCallClientListener implements CallClientListener {
        private SinchCallClientListener() {
        }

        public void onIncomingCall(CallClient callClient, Call call) {
            Log.e(SinchService.TAG, "Incoming call");
            Intent intent = new Intent(SinchService.this, IncomingCallScreenActivity.class);
            intent.putExtra(SinchService.CALL_ID, call.getCallId());
            intent.addFlags(268435456);
            SinchService.this.startActivity(intent);
        }
    }

    public void onCreate() {
        super.onCreate();
    }

    public void onDestroy() {
        if (this.mSinchClient != null && this.mSinchClient.isStarted()) {
            this.mSinchClient.terminate();
        }
        super.onDestroy();
    }

    private void start(String userName) {
        if (this.mSinchClient == null) {
            this.mUserId = userName;
            this.mSinchClient = Sinch.getSinchClientBuilder().context(getApplicationContext()).userId(userName).applicationKey(APP_KEY).applicationSecret(APP_SECRET).environmentHost(ENVIRONMENT).build();
            this.mSinchClient.setSupportCalling(true);
            this.mSinchClient.setSupportActiveConnectionInBackground(true);
            this.mSinchClient.startListeningOnActiveConnection();
            this.mSinchClient.addSinchClientListener(new MySinchClientListener());
            this.mSinchClient.getCallClient().setRespectNativeCalls(false);
            this.mSinchClient.getCallClient().addCallClientListener(new SinchCallClientListener());
            this.mSinchClient.start();
        }
    }

    private void stop() {
        if (this.mSinchClient != null) {
            this.mSinchClient.terminate();
            this.mSinchClient = null;
        }
    }

    private boolean isStarted() {
        return this.mSinchClient != null && this.mSinchClient.isStarted();
    }

    public IBinder onBind(Intent intent) {
        return this.mSinchServiceInterface;
    }
}
