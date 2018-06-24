package org.webrtc.sinch.voiceengine;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Process;
import java.nio.ByteBuffer;
import org.webrtc.sinch.AudioDeviceUtil;
import org.webrtc.sinch.Logging;

public class WebRtcAudioTrack {
    private static final int BITS_PER_SAMPLE = 16;
    private static final int BUFFERS_PER_SECOND = 100;
    private static final int CALLBACK_BUFFER_SIZE_MS = 10;
    private static final boolean DEBUG = false;
    private static final String TAG = "WebRtcAudioTrack";
    private static volatile boolean speakerMute = false;
    private final AudioManager audioManager;
    private AudioTrackThread audioThread = null;
    private AudioTrack audioTrack = null;
    private ByteBuffer byteBuffer;
    private final Context context;
    private byte[] emptyBytes;
    private final long nativeAudioTrack;

    class AudioTrackThread extends Thread {
        private volatile boolean keepAlive = true;

        public AudioTrackThread(String str) {
            super(str);
        }

        @TargetApi(21)
        private int writeOnLollipop(AudioTrack audioTrack, ByteBuffer byteBuffer, int i) {
            return audioTrack.write(byteBuffer, i, 0);
        }

        private int writePreLollipop(AudioTrack audioTrack, ByteBuffer byteBuffer, int i) {
            return audioTrack.write(byteBuffer.array(), byteBuffer.arrayOffset(), i);
        }

        public void joinThread() {
            this.keepAlive = false;
            while (isAlive()) {
                try {
                    join();
                } catch (InterruptedException e) {
                }
            }
        }

        public void run() {
            boolean z = true;
            Process.setThreadPriority(-19);
            Logging.m776d(WebRtcAudioTrack.TAG, "AudioTrackThread" + WebRtcAudioUtils.getThreadInfo());
            try {
                WebRtcAudioTrack.this.audioTrack.play();
                WebRtcAudioTrack.assertTrue(WebRtcAudioTrack.this.audioTrack.getPlayState() == 3);
                int capacity = WebRtcAudioTrack.this.byteBuffer.capacity();
                while (this.keepAlive) {
                    WebRtcAudioTrack.this.nativeGetPlayoutData(capacity, WebRtcAudioTrack.this.nativeAudioTrack);
                    WebRtcAudioTrack.assertTrue(capacity <= WebRtcAudioTrack.this.byteBuffer.remaining());
                    if (WebRtcAudioTrack.speakerMute) {
                        WebRtcAudioTrack.this.byteBuffer.clear();
                        WebRtcAudioTrack.this.byteBuffer.put(WebRtcAudioTrack.this.emptyBytes);
                        WebRtcAudioTrack.this.byteBuffer.position(0);
                    }
                    int writeOnLollipop = WebRtcAudioUtils.runningOnLollipopOrHigher() ? writeOnLollipop(WebRtcAudioTrack.this.audioTrack, WebRtcAudioTrack.this.byteBuffer, capacity) : writePreLollipop(WebRtcAudioTrack.this.audioTrack, WebRtcAudioTrack.this.byteBuffer, capacity);
                    if (writeOnLollipop != capacity) {
                        Logging.m777e(WebRtcAudioTrack.TAG, "AudioTrack.write failed: " + writeOnLollipop);
                        if (writeOnLollipop == -3) {
                            this.keepAlive = false;
                        }
                    }
                    WebRtcAudioTrack.this.byteBuffer.rewind();
                }
                try {
                    WebRtcAudioTrack.this.audioTrack.stop();
                } catch (IllegalStateException e) {
                    Logging.m777e(WebRtcAudioTrack.TAG, "AudioTrack.stop failed: " + e.getMessage());
                }
                if (WebRtcAudioTrack.this.audioTrack.getPlayState() != 1) {
                    z = false;
                }
                WebRtcAudioTrack.assertTrue(z);
                WebRtcAudioTrack.this.audioTrack.flush();
            } catch (IllegalStateException e2) {
                Logging.m777e(WebRtcAudioTrack.TAG, "AudioTrack.play failed: " + e2.getMessage());
            }
        }
    }

    WebRtcAudioTrack(Context context, long j) {
        Logging.m776d(TAG, "ctor" + WebRtcAudioUtils.getThreadInfo());
        this.context = context;
        this.nativeAudioTrack = j;
        this.audioManager = (AudioManager) context.getSystemService("audio");
    }

    private static void assertTrue(boolean z) {
        if (!z) {
            throw new AssertionError("Expected condition to be true");
        }
    }

    private int getStreamMaxVolume() {
        Logging.m776d(TAG, "getStreamMaxVolume");
        assertTrue(this.audioManager != null);
        return this.audioManager.getStreamMaxVolume(0);
    }

    private int getStreamVolume() {
        Logging.m776d(TAG, "getStreamVolume");
        assertTrue(this.audioManager != null);
        return this.audioManager.getStreamVolume(0);
    }

    private boolean initPlayout(int i, int i2) {
        Logging.m776d(TAG, "initPlayout(sampleRate=" + i + ", channels=" + i2 + ")");
        int i3 = i2 * 2;
        ByteBuffer byteBuffer = this.byteBuffer;
        this.byteBuffer = ByteBuffer.allocateDirect(i3 * (i / 100));
        Logging.m776d(TAG, "byteBuffer.capacity: " + this.byteBuffer.capacity());
        this.emptyBytes = new byte[this.byteBuffer.capacity()];
        nativeCacheDirectBufferAddress(this.byteBuffer, this.nativeAudioTrack);
        int minBufferSize = AudioTrack.getMinBufferSize(i, 4, 2);
        Logging.m776d(TAG, "AudioTrack.getMinBufferSize: " + minBufferSize);
        assertTrue(this.audioTrack == null);
        assertTrue(this.byteBuffer.capacity() < minBufferSize);
        try {
            this.audioTrack = new AudioTrack(0, i, 4, 2, minBufferSize, 1);
            if (this.audioTrack.getState() == 1) {
                return true;
            }
            Logging.m777e(TAG, "Initialization of audio track failed.");
            return false;
        } catch (Exception e) {
            AudioDeviceUtil.LogException(TAG, e);
            return false;
        }
    }

    @TargetApi(21)
    private boolean isVolumeFixed() {
        return !WebRtcAudioUtils.runningOnLollipopOrHigher() ? false : this.audioManager.isVolumeFixed();
    }

    private native void nativeCacheDirectBufferAddress(ByteBuffer byteBuffer, long j);

    private native void nativeGetPlayoutData(int i, long j);

    public static void setSpeakerMute(boolean z) {
        Logging.m780w(TAG, "setSpeakerMute(" + z + ")");
        speakerMute = z;
    }

    private boolean setStreamVolume(int i) {
        Logging.m776d(TAG, "setStreamVolume(" + i + ")");
        assertTrue(this.audioManager != null);
        if (isVolumeFixed()) {
            Logging.m777e(TAG, "The device implements a fixed volume policy.");
            return false;
        }
        this.audioManager.setStreamVolume(0, i, 0);
        return true;
    }

    private boolean startPlayout() {
        Logging.m776d(TAG, "startPlayout");
        assertTrue(this.audioTrack != null);
        assertTrue(this.audioThread == null);
        if (this.audioTrack.getState() != 1) {
            Logging.m777e(TAG, "Audio track is not successfully initialized.");
            return false;
        }
        this.audioThread = new AudioTrackThread("AudioTrackJavaThread");
        this.audioThread.start();
        return true;
    }

    private boolean stopPlayout() {
        Logging.m776d(TAG, "stopPlayout");
        assertTrue(this.audioThread != null);
        this.audioThread.joinThread();
        this.audioThread = null;
        if (this.audioTrack != null) {
            this.audioTrack.release();
            this.audioTrack = null;
        }
        return true;
    }
}
