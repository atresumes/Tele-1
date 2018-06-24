package org.webrtc.sinch.voiceengine;

import android.content.Context;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.os.Process;
import java.nio.ByteBuffer;
import org.webrtc.sinch.AudioDeviceUtil;
import org.webrtc.sinch.Logging;
import org.webrtc.sinch.ThreadUtils;

public class WebRtcAudioRecord {
    private static final long AUDIO_RECORD_THREAD_JOIN_TIMEOUT_MS = 2000;
    private static final int BITS_PER_SAMPLE = 16;
    private static final int BUFFERS_PER_SECOND = 100;
    private static final int BUFFER_SIZE_FACTOR = 2;
    private static final int CALLBACK_BUFFER_SIZE_MS = 10;
    private static final boolean DEBUG = false;
    private static final String TAG = "WebRtcAudioRecord";
    private static volatile boolean microphoneMute = false;
    private AudioRecord audioRecord = null;
    private AudioRecordThread audioThread = null;
    private ByteBuffer byteBuffer;
    private final Context context;
    private WebRtcAudioEffects effects = null;
    private byte[] emptyBytes;
    private final long nativeAudioRecord;

    class AudioRecordThread extends Thread {
        private volatile boolean keepAlive = true;

        public AudioRecordThread(String str) {
            super(str);
        }

        public void run() {
            Process.setThreadPriority(-19);
            Logging.m776d(WebRtcAudioRecord.TAG, "AudioRecordThread" + WebRtcAudioUtils.getThreadInfo());
            WebRtcAudioRecord.assertTrue(WebRtcAudioRecord.this.audioRecord.getRecordingState() == 3);
            System.nanoTime();
            while (this.keepAlive) {
                int read = WebRtcAudioRecord.this.audioRecord.read(WebRtcAudioRecord.this.byteBuffer, WebRtcAudioRecord.this.byteBuffer.capacity());
                if (read == WebRtcAudioRecord.this.byteBuffer.capacity()) {
                    if (WebRtcAudioRecord.microphoneMute) {
                        WebRtcAudioRecord.this.byteBuffer.clear();
                        WebRtcAudioRecord.this.byteBuffer.put(WebRtcAudioRecord.this.emptyBytes);
                    }
                    WebRtcAudioRecord.this.nativeDataIsRecorded(read, WebRtcAudioRecord.this.nativeAudioRecord);
                } else {
                    Logging.m777e(WebRtcAudioRecord.TAG, "AudioRecord.read failed: " + read);
                    if (read == -3) {
                        this.keepAlive = false;
                    }
                }
            }
            try {
                WebRtcAudioRecord.this.audioRecord.stop();
            } catch (IllegalStateException e) {
                Logging.m777e(WebRtcAudioRecord.TAG, "AudioRecord.stop failed: " + e.getMessage());
            }
        }

        public void stopThread() {
            Logging.m776d(WebRtcAudioRecord.TAG, "stopThread");
            this.keepAlive = false;
        }
    }

    WebRtcAudioRecord(Context context, long j) {
        Logging.m776d(TAG, "ctor" + WebRtcAudioUtils.getThreadInfo());
        this.context = context;
        this.nativeAudioRecord = j;
        this.effects = WebRtcAudioEffects.create();
    }

    private static void assertTrue(boolean z) {
        if (!z) {
            throw new AssertionError("Expected condition to be true");
        }
    }

    private boolean enableBuiltInAEC(boolean z) {
        Logging.m776d(TAG, "enableBuiltInAEC(" + z + ')');
        if (this.effects != null) {
            return this.effects.setAEC(z);
        }
        Logging.m777e(TAG, "Built-in AEC is not supported on this platform");
        return false;
    }

    private boolean enableBuiltInAGC(boolean z) {
        Logging.m776d(TAG, "enableBuiltInAGC(" + z + ')');
        if (this.effects != null) {
            return this.effects.setAGC(z);
        }
        Logging.m777e(TAG, "Built-in AGC is not supported on this platform");
        return false;
    }

    private boolean enableBuiltInNS(boolean z) {
        Logging.m776d(TAG, "enableBuiltInNS(" + z + ')');
        if (this.effects != null) {
            return this.effects.setNS(z);
        }
        Logging.m777e(TAG, "Built-in NS is not supported on this platform");
        return false;
    }

    private int initRecording(int i, int i2) {
        Logging.m776d(TAG, "initRecording(sampleRate=" + i + ", channels=" + i2 + ")");
        if (!WebRtcAudioUtils.hasPermission(this.context, "android.permission.RECORD_AUDIO")) {
            Logging.m777e(TAG, "RECORD_AUDIO permission is missing");
            return -1;
        } else if (this.audioRecord != null) {
            Logging.m777e(TAG, "InitRecording() called twice without StopRecording()");
            return -1;
        } else {
            int i3 = i / 100;
            this.byteBuffer = ByteBuffer.allocateDirect((i2 * 2) * i3);
            Logging.m776d(TAG, "byteBuffer.capacity: " + this.byteBuffer.capacity());
            this.emptyBytes = new byte[this.byteBuffer.capacity()];
            nativeCacheDirectBufferAddress(this.byteBuffer, this.nativeAudioRecord);
            int minBufferSize = AudioRecord.getMinBufferSize(i, 16, 2);
            if (minBufferSize == -1 || minBufferSize == -2) {
                Logging.m777e(TAG, "AudioRecord.getMinBufferSize failed: " + minBufferSize);
                return -1;
            }
            Logging.m776d(TAG, "AudioRecord.getMinBufferSize: " + minBufferSize);
            int max = Math.max(minBufferSize * 2, this.byteBuffer.capacity());
            Logging.m776d(TAG, "bufferSizeInBytes: " + max);
            try {
                this.audioRecord = new AudioRecord(AudioDeviceUtil.getAudioSource(), i, 16, 2, max);
                if (this.audioRecord == null || this.audioRecord.getState() != 1) {
                    Logging.m777e(TAG, "Failed to create a new AudioRecord instance");
                    return -1;
                }
                Logging.m776d(TAG, "AudioRecord session ID: " + this.audioRecord.getAudioSessionId() + ", " + "audio format: " + this.audioRecord.getAudioFormat() + ", " + "channels: " + this.audioRecord.getChannelCount() + ", " + "sample rate: " + this.audioRecord.getSampleRate());
                if (this.effects != null) {
                    this.effects.enable(this.audioRecord.getAudioSessionId());
                }
                return i3;
            } catch (Exception e) {
                AudioDeviceUtil.LogException(TAG, e);
                return -1;
            }
        }
    }

    private native void nativeCacheDirectBufferAddress(ByteBuffer byteBuffer, long j);

    private native void nativeDataIsRecorded(int i, long j);

    public static void setMicrophoneMute(boolean z) {
        Logging.m780w(TAG, "setMicrophoneMute(" + z + ")");
        microphoneMute = z;
    }

    private boolean startRecording() {
        Logging.m776d(TAG, "startRecording");
        assertTrue(this.audioRecord != null);
        assertTrue(this.audioThread == null);
        if (AudioDeviceUtil.shouldOverrideAudioMode() && this.context != null) {
            ((AudioManager) this.context.getSystemService("audio")).setMode(AudioDeviceUtil.getAudioMode());
        }
        try {
            this.audioRecord.startRecording();
            if (this.audioRecord.getRecordingState() != 3) {
                Logging.m777e(TAG, "AudioRecord.startRecording failed");
                return false;
            }
            this.audioThread = new AudioRecordThread("AudioRecordJavaThread");
            this.audioThread.start();
            return true;
        } catch (IllegalStateException e) {
            Logging.m777e(TAG, "AudioRecord.startRecording failed: " + e.getMessage());
            return false;
        }
    }

    private boolean stopRecording() {
        Logging.m776d(TAG, "stopRecording");
        assertTrue(this.audioThread != null);
        this.audioThread.stopThread();
        if (!ThreadUtils.joinUninterruptibly(this.audioThread, AUDIO_RECORD_THREAD_JOIN_TIMEOUT_MS)) {
            Logging.m777e(TAG, "Join of AudioRecordJavaThread timed out");
        }
        this.audioThread = null;
        if (this.effects != null) {
            this.effects.release();
        }
        if (AudioDeviceUtil.shouldOverrideAudioMode() && this.context != null) {
            ((AudioManager) this.context.getSystemService("audio")).setMode(0);
        }
        this.audioRecord.release();
        this.audioRecord = null;
        return true;
    }
}
