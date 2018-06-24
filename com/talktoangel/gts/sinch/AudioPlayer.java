package com.talktoangel.gts.sinch;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import com.talktoangel.gts.C0585R;
import java.io.FileInputStream;
import java.io.IOException;

public class AudioPlayer {
    static final String LOG_TAG = AudioPlayer.class.getSimpleName();
    private static final int SAMPLE_RATE = 16000;
    private Context mContext;
    private MediaPlayer mPlayer;
    private AudioTrack mProgressTone;

    public AudioPlayer(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public void playRingtone() {
        switch (((AudioManager) this.mContext.getSystemService("audio")).getRingerMode()) {
            case 2:
                this.mPlayer = new MediaPlayer();
                this.mPlayer.setAudioStreamType(2);
                try {
                    this.mPlayer.setDataSource(this.mContext, Uri.parse("android.resource://" + this.mContext.getPackageName() + "/" + C0585R.raw.phone_loud1));
                    this.mPlayer.prepare();
                    this.mPlayer.setLooping(true);
                    this.mPlayer.start();
                    return;
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Could not setup media player for ringtone");
                    this.mPlayer = null;
                    return;
                }
            default:
                return;
        }
    }

    public void stopRingtone() {
        if (this.mPlayer != null) {
            this.mPlayer.stop();
            this.mPlayer.release();
            this.mPlayer = null;
        }
    }

    public void playProgressTone() {
        stopProgressTone();
        try {
            this.mProgressTone = createProgressTone(this.mContext);
            this.mProgressTone.play();
        } catch (Exception e) {
            Log.e(LOG_TAG, "Could not play progress tone", e);
        }
    }

    public void stopProgressTone() {
        if (this.mProgressTone != null) {
            this.mProgressTone.stop();
            this.mProgressTone.release();
            this.mProgressTone = null;
        }
    }

    private static AudioTrack createProgressTone(Context context) throws IOException {
        AssetFileDescriptor fd = context.getResources().openRawResourceFd(C0585R.raw.progress_tone);
        int length = (int) fd.getLength();
        AudioTrack audioTrack = new AudioTrack(0, SAMPLE_RATE, 4, 2, length, 0);
        byte[] data = new byte[length];
        readFileToBytes(fd, data);
        audioTrack.write(data, 0, data.length);
        audioTrack.setLoopPoints(0, data.length / 2, 30);
        return audioTrack;
    }

    private static void readFileToBytes(AssetFileDescriptor fd, byte[] data) throws IOException {
        FileInputStream inputStream = fd.createInputStream();
        int bytesRead = 0;
        while (bytesRead < data.length) {
            int res = inputStream.read(data, bytesRead, data.length - bytesRead);
            if (res != -1) {
                bytesRead += res;
            } else {
                return;
            }
        }
    }
}
