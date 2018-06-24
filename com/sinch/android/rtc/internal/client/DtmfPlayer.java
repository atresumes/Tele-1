package com.sinch.android.rtc.internal.client;

import android.media.ToneGenerator;
import android.os.Handler;
import android.os.Looper;
import java.util.ArrayList;

class DtmfPlayer {
    private static final int TONE_DURATION_MS = 200;
    private static final int VOLUME_PERCENT = 100;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private boolean mPlaying = false;
    private ToneGenerator mToneGenerator = new ToneGenerator(0, 100);
    private ArrayList<Character> mTonesToPlay = new ArrayList();

    class C05491 implements Runnable {
        C05491() {
        }

        public void run() {
            DtmfPlayer.this.playNextTone();
        }
    }

    DtmfPlayer() {
    }

    private synchronized void playNextTone() {
        if (this.mTonesToPlay.size() > 0) {
            Character ch = (Character) this.mTonesToPlay.get(0);
            this.mTonesToPlay.remove(0);
            this.mToneGenerator.startTone(toDtmfToneType(ch), 200);
        }
        if (this.mTonesToPlay.size() > 0) {
            scheduleNextTone();
        } else {
            this.mPlaying = false;
        }
    }

    private void scheduleNextTone() {
        this.mHandler.postDelayed(new C05491(), 200);
    }

    private static int toDtmfToneType(Character ch) {
        switch (ch.charValue()) {
            case '#':
                return 11;
            case '*':
                return 10;
            case '0':
                return 0;
            case '1':
                return 1;
            case '2':
                return 2;
            case '3':
                return 3;
            case '4':
                return 4;
            case '5':
                return 5;
            case '6':
                return 6;
            case '7':
                return 7;
            case '8':
                return 8;
            case '9':
                return 9;
            case 'A':
                return 12;
            case 'B':
                return 13;
            case 'C':
                return 14;
            case 'D':
                return 15;
            default:
                throw new IllegalArgumentException("Dtmf tone key not supported: " + ch);
        }
    }

    public synchronized void play(String str) {
        for (char valueOf : str.toCharArray()) {
            this.mTonesToPlay.add(Character.valueOf(valueOf));
        }
        if (!this.mPlaying) {
            this.mPlaying = true;
            playNextTone();
        }
    }
}
