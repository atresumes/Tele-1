package com.talktoangel.gts.sinch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.sinch.android.rtc.MissingPermissionException;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.video.VideoCallListener;
import com.talktoangel.gts.C0585R;
import java.util.List;

public class IncomingCallScreenActivity extends BaseActivity {
    static final String TAG = IncomingCallScreenActivity.class.getSimpleName();
    private AudioPlayer mAudioPlayer;
    private String mCallId;
    private OnClickListener mClickListener = new C06251();

    class C06251 implements OnClickListener {
        C06251() {
        }

        public void onClick(View v) {
            switch (v.getId()) {
                case C0585R.id.answerButton:
                    IncomingCallScreenActivity.this.answerClicked();
                    return;
                case C0585R.id.declineButton:
                    IncomingCallScreenActivity.this.declineClicked();
                    return;
                default:
                    return;
            }
        }
    }

    private class SinchCallListener implements VideoCallListener {
        private SinchCallListener() {
        }

        public void onCallEnded(Call call) {
            Log.d(IncomingCallScreenActivity.TAG, "Call ended, cause: " + call.getDetails().getEndCause().toString());
            IncomingCallScreenActivity.this.mAudioPlayer.stopRingtone();
            IncomingCallScreenActivity.this.finish();
        }

        public void onCallEstablished(Call call) {
            Log.d(IncomingCallScreenActivity.TAG, "Call established");
        }

        public void onCallProgressing(Call call) {
            Log.d(IncomingCallScreenActivity.TAG, "Call progressing");
        }

        public void onShouldSendPushNotification(Call call, List<PushPair> list) {
        }

        public void onVideoTrackAdded(Call call) {
        }

        public void onVideoTrackPaused(Call call) {
        }

        public void onVideoTrackResumed(Call call) {
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0585R.layout.incoming);
        getSupportActionBar().hide();
        ((ImageButton) findViewById(C0585R.id.answerButton)).setOnClickListener(this.mClickListener);
        ((ImageButton) findViewById(C0585R.id.declineButton)).setOnClickListener(this.mClickListener);
        this.mAudioPlayer = new AudioPlayer(this);
        this.mAudioPlayer.playRingtone();
        this.mCallId = getIntent().getStringExtra(SinchService.CALL_ID);
    }

    protected void onServiceConnected() {
        Call call = getSinchServiceInterface().getCall(this.mCallId);
        if (call != null) {
            call.addCallListener(new SinchCallListener());
            ((TextView) findViewById(C0585R.id.remoteUser)).setText(call.getRemoteUserId());
            return;
        }
        Log.e(TAG, "Started with invalid callId, aborting");
        finish();
    }

    private void answerClicked() {
        this.mAudioPlayer.stopRingtone();
        Call call = getSinchServiceInterface().getCall(this.mCallId);
        if (call != null) {
            try {
                call.answer();
                Intent intent = new Intent(this, CallScreenActivity.class);
                intent.putExtra(SinchService.CALL_ID, this.mCallId);
                startActivity(intent);
                return;
            } catch (MissingPermissionException e) {
                ActivityCompat.requestPermissions(this, new String[]{e.getRequiredPermission()}, 0);
                return;
            }
        }
        finish();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults[0] == 0) {
            Toast.makeText(this, "You may now answer the call", 1).show();
        } else {
            Toast.makeText(this, "This application needs permission to use your microphone to function properly.", 1).show();
        }
    }

    private void declineClicked() {
        this.mAudioPlayer.stopRingtone();
        Call call = getSinchServiceInterface().getCall(this.mCallId);
        if (call != null) {
            call.hangup();
        }
        finish();
    }
}
