package com.talktoangel.gts.sinch;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import com.talktoangel.gts.sinch.SinchService.SinchServiceInterface;

public abstract class BaseActivity extends AppCompatActivity implements ServiceConnection {
    private SinchServiceInterface mSinchServiceInterface;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApplicationContext().bindService(new Intent(this, SinchService.class), this, 1);
    }

    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        if (SinchService.class.getName().equals(componentName.getClassName())) {
            this.mSinchServiceInterface = (SinchServiceInterface) iBinder;
            onServiceConnected();
        }
    }

    public void onServiceDisconnected(ComponentName componentName) {
        if (SinchService.class.getName().equals(componentName.getClassName())) {
            this.mSinchServiceInterface = null;
            onServiceDisconnected();
        }
    }

    protected void onServiceConnected() {
    }

    protected void onServiceDisconnected() {
    }

    public SinchServiceInterface getSinchServiceInterface() {
        return this.mSinchServiceInterface;
    }
}
