package com.talktoangel.gts.sinch;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog.Builder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallListener;
import com.sinch.android.rtc.calling.CallState;
import com.sinch.android.rtc.video.VideoCallListener;
import com.sinch.android.rtc.video.VideoController;
import com.talktoangel.gts.C0585R;
import com.talktoangel.gts.MainActivity;
import com.talktoangel.gts.controller.Controller;
import com.talktoangel.gts.utils.EndPoints;
import com.talktoangel.gts.utils.SessionManager;
import com.talktoangel.gts.utils.TLSSocketFactory;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONException;
import org.json.JSONObject;

public class CallScreenActivity extends BaseActivity {
    static final String ADDED_LISTENER = "addedListener";
    static final String TAG = CallScreenActivity.class.getSimpleName();
    private String apo_id;
    private boolean mAddedListener = false;
    private AudioPlayer mAudioPlayer;
    private TextView mCallDuration;
    private String mCallId;
    private TextView mCallState;
    private TextView mCallerName;
    private UpdateCallDurationTask mDurationTask;
    private boolean mLocalVideoViewAdded = false;
    private boolean mRemoteVideoViewAdded = false;
    private Timer mTimer;
    SessionManager session;

    class C06201 implements OnClickListener {
        C06201() {
        }

        public void onClick(View v) {
            if (((String) CallScreenActivity.this.session.getUser().get(SessionManager.KEY_TYPE)).equals("t")) {
                CallScreenActivity.this.showAlertDialog();
            } else {
                CallScreenActivity.this.endCall();
            }
        }
    }

    class C06223 implements DialogInterface.OnClickListener {
        C06223() {
        }

        public void onClick(DialogInterface dialogInterface, int arg1) {
            dialogInterface.dismiss();
            CallScreenActivity.this.finish();
        }
    }

    class C06234 implements DialogInterface.OnClickListener {
        C06234() {
        }

        public void onClick(DialogInterface dialog, int which) {
            CallScreenActivity.this.updateApoStatus(CallScreenActivity.this.apo_id, "d");
        }
    }

    private class UpdateCallDurationTask extends TimerTask {

        class C06241 implements Runnable {
            C06241() {
            }

            public void run() {
                CallScreenActivity.this.updateCallDuration();
            }
        }

        private UpdateCallDurationTask() {
        }

        public void run() {
            CallScreenActivity.this.runOnUiThread(new C06241());
        }
    }

    class C10235 implements Listener<String> {
        C10235() {
        }

        public void onResponse(String response) {
            Log.e("Login Response", response);
            try {
                if (new JSONObject(response).getString("status").equals("true")) {
                    CallScreenActivity.this.endCall();
                    CallScreenActivity.this.startActivity(new Intent(CallScreenActivity.this.getApplicationContext(), MainActivity.class).setFlags(268468224));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class C10246 implements ErrorListener {
        C10246() {
        }

        public void onErrorResponse(VolleyError error) {
            Log.e("Login Error: ", "" + error.getMessage());
        }
    }

    private class SinchAudioCallListener implements CallListener {
        private SinchAudioCallListener() {
        }

        public void onCallEnded(Call call) {
            Log.e(CallScreenActivity.TAG, "Call ended. Reason: " + call.getDetails().getEndCause().toString());
            CallScreenActivity.this.mAudioPlayer.stopProgressTone();
            CallScreenActivity.this.setVolumeControlStream(Integer.MIN_VALUE);
            Toast.makeText(CallScreenActivity.this, "Call ended: " + call.getDetails().toString(), 1).show();
            if (((String) CallScreenActivity.this.session.getUser().get(SessionManager.KEY_TYPE)).equals("t")) {
                CallScreenActivity.this.showAlertDialog();
            } else {
                CallScreenActivity.this.endCall();
            }
        }

        public void onCallEstablished(Call call) {
            Log.e(CallScreenActivity.TAG, "Call established");
            CallScreenActivity.this.mAudioPlayer.stopProgressTone();
            CallScreenActivity.this.mCallState.setText(call.getState().toString());
            CallScreenActivity.this.setVolumeControlStream(0);
        }

        public void onCallProgressing(Call call) {
            Log.e(CallScreenActivity.TAG, "Call progressing");
            CallScreenActivity.this.mAudioPlayer.playProgressTone();
        }

        public void onShouldSendPushNotification(Call call, List<PushPair> list) {
        }
    }

    private class SinchVideoCallListener implements VideoCallListener {
        private SinchVideoCallListener() {
        }

        public void onCallEnded(Call call) {
            Log.d(CallScreenActivity.TAG, "Call ended. Reason: " + call.getDetails().getEndCause().toString());
            CallScreenActivity.this.mAudioPlayer.stopProgressTone();
            CallScreenActivity.this.setVolumeControlStream(Integer.MIN_VALUE);
            Toast.makeText(CallScreenActivity.this, "Call ended: " + call.getDetails().toString(), 1).show();
            CallScreenActivity.this.endCall();
        }

        public void onCallEstablished(Call call) {
            Log.d(CallScreenActivity.TAG, "Call established");
            CallScreenActivity.this.mAudioPlayer.stopProgressTone();
            CallScreenActivity.this.mCallState.setText(call.getState().toString());
            CallScreenActivity.this.setVolumeControlStream(0);
            CallScreenActivity.this.getSinchServiceInterface().getAudioController().enableSpeaker();
            Log.d(CallScreenActivity.TAG, "Call offered video: " + call.getDetails().isVideoOffered());
        }

        public void onCallProgressing(Call call) {
            Log.d(CallScreenActivity.TAG, "Call progressing");
            CallScreenActivity.this.mAudioPlayer.playProgressTone();
        }

        public void onShouldSendPushNotification(Call call, List<PushPair> list) {
        }

        public void onVideoTrackAdded(Call call) {
            Log.d(CallScreenActivity.TAG, "Video track added");
            CallScreenActivity.this.addRemoteView();
        }

        public void onVideoTrackPaused(Call call) {
        }

        public void onVideoTrackResumed(Call call) {
        }
    }

    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(ADDED_LISTENER, this.mAddedListener);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        this.mAddedListener = savedInstanceState.getBoolean(ADDED_LISTENER);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0585R.layout.callscreen);
        getSupportActionBar().hide();
        this.mAudioPlayer = new AudioPlayer(this);
        this.mCallDuration = (TextView) findViewById(C0585R.id.callDuration);
        this.mCallerName = (TextView) findViewById(C0585R.id.remoteUser);
        this.mCallState = (TextView) findViewById(C0585R.id.callState);
        ImageButton endCallButton = (ImageButton) findViewById(C0585R.id.hangupButton);
        this.session = new SessionManager(this);
        endCallButton.setOnClickListener(new C06201());
        this.apo_id = getIntent().getStringExtra("apo_id");
        this.mCallId = getIntent().getStringExtra(SinchService.CALL_ID);
    }

    public void onServiceConnected() {
        Call call = getSinchServiceInterface().getCall(this.mCallId);
        if (call == null) {
            Log.e(TAG, "Started with invalid callId, aborting.");
            finish();
        } else if (!this.mAddedListener) {
            call.addCallListener(new SinchVideoCallListener());
            this.mAddedListener = true;
        }
        updateUI();
    }

    private void updateUI() {
        if (getSinchServiceInterface() != null) {
            Call call = getSinchServiceInterface().getCall(this.mCallId);
            if (call != null) {
                this.mCallerName.setText(call.getRemoteUserId());
                this.mCallState.setText(call.getState().toString());
                if (call.getDetails().isVideoOffered()) {
                    addLocalView();
                    if (call.getState() == CallState.ESTABLISHED) {
                        addRemoteView();
                    }
                }
            }
        }
    }

    public void onStop() {
        super.onStop();
        this.mDurationTask.cancel();
        this.mTimer.cancel();
        removeVideoViews();
    }

    public void onStart() {
        super.onStart();
        this.mTimer = new Timer();
        this.mDurationTask = new UpdateCallDurationTask();
        this.mTimer.schedule(this.mDurationTask, 0, 500);
        updateUI();
    }

    public void onBackPressed() {
    }

    private void endCall() {
        this.mAudioPlayer.stopProgressTone();
        Call call = getSinchServiceInterface().getCall(this.mCallId);
        if (call != null) {
            call.hangup();
        }
        finish();
    }

    private String formatTimespan(int totalSeconds) {
        long minutes = (long) (totalSeconds / 60);
        long seconds = (long) (totalSeconds % 60);
        return String.format(Locale.US, "%02d:%02d", new Object[]{Long.valueOf(minutes), Long.valueOf(seconds)});
    }

    private void updateCallDuration() {
        Call call = getSinchServiceInterface().getCall(this.mCallId);
        if (call != null) {
            this.mCallDuration.setText(formatTimespan(call.getDetails().getDuration()));
        }
    }

    private void addLocalView() {
        if (!this.mLocalVideoViewAdded && getSinchServiceInterface() != null) {
            final VideoController vc = getSinchServiceInterface().getVideoController();
            if (vc != null) {
                RelativeLayout localView = (RelativeLayout) findViewById(C0585R.id.localVideo);
                localView.addView(vc.getLocalView());
                localView.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        vc.toggleCaptureDevicePosition();
                    }
                });
                this.mLocalVideoViewAdded = true;
            }
        }
    }

    private void addRemoteView() {
        if (!this.mRemoteVideoViewAdded && getSinchServiceInterface() != null) {
            VideoController vc = getSinchServiceInterface().getVideoController();
            if (vc != null) {
                ((LinearLayout) findViewById(C0585R.id.remoteVideo)).addView(vc.getRemoteView());
                this.mRemoteVideoViewAdded = true;
            }
        }
    }

    private void removeVideoViews() {
        if (getSinchServiceInterface() != null) {
            VideoController vc = getSinchServiceInterface().getVideoController();
            if (vc != null) {
                ((LinearLayout) findViewById(C0585R.id.remoteVideo)).removeView(vc.getRemoteView());
                ((RelativeLayout) findViewById(C0585R.id.localVideo)).removeView(vc.getLocalView());
                this.mLocalVideoViewAdded = false;
                this.mRemoteVideoViewAdded = false;
            }
        }
    }

    void showAlertDialog() {
        Builder alertDialogBuilder = new Builder(this);
        alertDialogBuilder.setMessage((CharSequence) "Do you want to continue Counselling");
        alertDialogBuilder.setPositiveButton((CharSequence) "yes", new C06223());
        alertDialogBuilder.setNegativeButton((CharSequence) "No", new C06234());
        alertDialogBuilder.create().show();
    }

    void updateApoStatus(String id, String s) {
        HttpStack stack;
        GeneralSecurityException e;
        final String str = id;
        final String str2 = s;
        Request strReq = new StringRequest(1, EndPoints.UPDATE_APO_STATUS, new C10235(), new C10246()) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap();
                params.put("apo_id", str);
                params.put("apo_status", str2);
                Log.e("Login params", params.toString());
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        try {
            stack = new HurlStack(null, new TLSSocketFactory());
        } catch (KeyManagementException e2) {
            e = e2;
            e.printStackTrace();
            Log.e("Your Wrapper Class", "Could not create new stack for TLS v1.2");
            stack = new HurlStack();
            Controller.getInstance().addToRequestQueue(strReq, stack);
        } catch (NoSuchAlgorithmException e3) {
            e = e3;
            e.printStackTrace();
            Log.e("Your Wrapper Class", "Could not create new stack for TLS v1.2");
            stack = new HurlStack();
            Controller.getInstance().addToRequestQueue(strReq, stack);
        }
        Controller.getInstance().addToRequestQueue(strReq, stack);
    }
}
