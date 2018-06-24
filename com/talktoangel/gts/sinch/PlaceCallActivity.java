package com.talktoangel.gts.sinch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.talktoangel.gts.C0585R;

public class PlaceCallActivity extends BaseActivity {
    private OnClickListener buttonClickListener = new C06261();
    private ImageButton mCallButton;
    private EditText mCallName;

    class C06261 implements OnClickListener {
        C06261() {
        }

        public void onClick(View v) {
            switch (v.getId()) {
                case C0585R.id.callButton:
                    PlaceCallActivity.this.callButtonClicked();
                    return;
                case C0585R.id.stopButton:
                    PlaceCallActivity.this.stopButtonClicked();
                    return;
                default:
                    return;
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0585R.layout.main);
        this.mCallName = (EditText) findViewById(C0585R.id.callName);
        this.mCallButton = (ImageButton) findViewById(C0585R.id.callButton);
        this.mCallButton.setEnabled(false);
        this.mCallButton.setOnClickListener(this.buttonClickListener);
        ((Button) findViewById(C0585R.id.stopButton)).setOnClickListener(this.buttonClickListener);
    }

    protected void onServiceConnected() {
        ((TextView) findViewById(C0585R.id.loggedInName)).setText(getSinchServiceInterface().getUserName());
        this.mCallButton.setEnabled(true);
    }

    private void stopButtonClicked() {
        if (getSinchServiceInterface() != null) {
            getSinchServiceInterface().stopClient();
        }
        finish();
    }

    private void callButtonClicked() {
        String userName = this.mCallName.getText().toString();
        if (userName.isEmpty()) {
            Toast.makeText(this, "Please enter a user to call", 1).show();
            return;
        }
        String callId = getSinchServiceInterface().callUserVideo(userName).getCallId();
        Intent callScreen = new Intent(this, CallScreenActivity.class);
        callScreen.putExtra(SinchService.CALL_ID, callId);
        startActivity(callScreen);
    }
}
