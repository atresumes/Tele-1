package org.webrtc.sinch.voiceengine;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.os.Build;
import org.webrtc.sinch.Logging;

public class WebRtcAudioManager {
    private static final String[] AUDIO_MODES = new String[]{"MODE_NORMAL", "MODE_RINGTONE", "MODE_IN_CALL", "MODE_IN_COMMUNICATION"};
    private static final int BITS_PER_SAMPLE = 16;
    private static final int CHANNELS = 1;
    private static final boolean DEBUG = false;
    private static final int DEFAULT_FRAME_PER_BUFFER = 256;
    private static final String TAG = "WebRtcAudioManager";
    private static boolean blacklistDeviceForOpenSLESUsage = false;
    private static boolean blacklistDeviceForOpenSLESUsageIsOverridden = false;
    private final AudioManager audioManager;
    private int channels;
    private final Context context;
    private boolean hardwareAEC;
    private boolean hardwareAGC;
    private boolean hardwareNS;
    private boolean initialized = false;
    private int inputBufferSize;
    private boolean lowLatencyOutput;
    private final long nativeAudioManager;
    private int nativeChannels;
    private int nativeSampleRate;
    private int outputBufferSize;
    private boolean proAudio;
    private int sampleRate;

    WebRtcAudioManager(Context context, long j) {
        Logging.m776d(TAG, "ctor" + WebRtcAudioUtils.getThreadInfo());
        this.context = context;
        this.nativeAudioManager = j;
        this.audioManager = (AudioManager) context.getSystemService("audio");
        storeAudioParameters();
        nativeCacheAudioParameters(this.sampleRate, this.channels, this.hardwareAEC, this.hardwareAGC, this.hardwareNS, this.lowLatencyOutput, this.proAudio, this.outputBufferSize, this.inputBufferSize, j);
    }

    private static void assertTrue(boolean z) {
        if (!z) {
            throw new AssertionError("Expected condition to be true");
        }
    }

    private void dispose() {
        Logging.m776d(TAG, "dispose" + WebRtcAudioUtils.getThreadInfo());
        if (!this.initialized) {
        }
    }

    private int getLowLatencyInputFramesPerBuffer() {
        assertTrue(isLowLatencyInputSupported());
        return getLowLatencyOutputFramesPerBuffer();
    }

    @TargetApi(17)
    private int getLowLatencyOutputFramesPerBuffer() {
        assertTrue(isLowLatencyOutputSupported());
        if (!WebRtcAudioUtils.runningOnJellyBeanMR1OrHigher()) {
            return 256;
        }
        String property = this.audioManager.getProperty("android.media.property.OUTPUT_FRAMES_PER_BUFFER");
        return property != null ? Integer.parseInt(property) : 256;
    }

    private static int getMinInputFrameSize(int i, int i2) {
        boolean z = true;
        int i3 = i2 * 2;
        if (i2 != 1) {
            z = false;
        }
        assertTrue(z);
        return AudioRecord.getMinBufferSize(i, 16, 2) / i3;
    }

    private static int getMinOutputFrameSize(int i, int i2) {
        int i3;
        int i4 = i2 * 2;
        if (i2 == 1) {
            i3 = 4;
        } else if (i2 != 2) {
            return -1;
        } else {
            i3 = 12;
        }
        return AudioTrack.getMinBufferSize(i, i3, 2) / i4;
    }

    private int getNativeOutputSampleRate() {
        if (WebRtcAudioUtils.runningOnEmulator()) {
            Logging.m776d(TAG, "Running emulator, overriding sample rate to 8 kHz.");
            return 8000;
        } else if (WebRtcAudioUtils.isDefaultSampleRateOverridden()) {
            Logging.m776d(TAG, "Default sample rate is overriden to " + WebRtcAudioUtils.getDefaultSampleRateHz() + " Hz");
            return WebRtcAudioUtils.getDefaultSampleRateHz();
        } else {
            int sampleRateOnJellyBeanMR10OrHigher = WebRtcAudioUtils.runningOnJellyBeanMR1OrHigher() ? getSampleRateOnJellyBeanMR10OrHigher() : WebRtcAudioUtils.getDefaultSampleRateHz();
            Logging.m776d(TAG, "Sample rate is set to " + sampleRateOnJellyBeanMR10OrHigher + " Hz");
            return sampleRateOnJellyBeanMR10OrHigher;
        }
    }

    @TargetApi(17)
    private int getSampleRateOnJellyBeanMR10OrHigher() {
        String property = this.audioManager.getProperty("android.media.property.OUTPUT_SAMPLE_RATE");
        return property == null ? WebRtcAudioUtils.getDefaultSampleRateHz() : Integer.parseInt(property);
    }

    private boolean hasEarpiece() {
        return this.context.getPackageManager().hasSystemFeature("android.hardware.telephony");
    }

    private boolean init() {
        Logging.m776d(TAG, "init" + WebRtcAudioUtils.getThreadInfo());
        if (!this.initialized) {
            Logging.m776d(TAG, "audio mode is: " + AUDIO_MODES[this.audioManager.getMode()]);
            this.initialized = true;
        }
        return true;
    }

    private static boolean isAcousticEchoCancelerSupported() {
        return WebRtcAudioEffects.canUseAcousticEchoCanceler();
    }

    private static boolean isAutomaticGainControlSupported() {
        return WebRtcAudioEffects.canUseAutomaticGainControl();
    }

    private boolean isCommunicationModeEnabled() {
        return this.audioManager.getMode() == 3;
    }

    private boolean isDeviceBlacklistedForOpenSLESUsage() {
        boolean deviceIsBlacklistedForOpenSLESUsage = blacklistDeviceForOpenSLESUsageIsOverridden ? blacklistDeviceForOpenSLESUsage : WebRtcAudioUtils.deviceIsBlacklistedForOpenSLESUsage();
        if (deviceIsBlacklistedForOpenSLESUsage) {
            Logging.m777e(TAG, Build.MODEL + " is blacklisted for OpenSL ES usage!");
        }
        return deviceIsBlacklistedForOpenSLESUsage;
    }

    private boolean isLowLatencyOutputSupported() {
        return isOpenSLESSupported() && this.context.getPackageManager().hasSystemFeature("android.hardware.audio.low_latency");
    }

    private static boolean isNoiseSuppressorSupported() {
        return WebRtcAudioEffects.canUseNoiseSuppressor();
    }

    private static boolean isOpenSLESSupported() {
        return false;
    }

    private boolean isProAudioSupported() {
        return WebRtcAudioUtils.runningOnMarshmallowOrHigher() && this.context.getPackageManager().hasSystemFeature("android.hardware.audio.pro");
    }

    private native void nativeCacheAudioParameters(int i, int i2, boolean z, boolean z2, boolean z3, boolean z4, boolean z5, int i3, int i4, long j);

    public static synchronized void setBlacklistDeviceForOpenSLESUsage(boolean z) {
        synchronized (WebRtcAudioManager.class) {
            blacklistDeviceForOpenSLESUsageIsOverridden = true;
            blacklistDeviceForOpenSLESUsage = z;
        }
    }

    private void storeAudioParameters() {
        this.channels = 1;
        this.sampleRate = getNativeOutputSampleRate();
        this.hardwareAEC = isAcousticEchoCancelerSupported();
        this.hardwareAGC = isAutomaticGainControlSupported();
        this.hardwareNS = isNoiseSuppressorSupported();
        this.lowLatencyOutput = isLowLatencyOutputSupported();
        this.proAudio = isProAudioSupported();
        this.outputBufferSize = this.lowLatencyOutput ? getLowLatencyOutputFramesPerBuffer() : getMinOutputFrameSize(this.sampleRate, this.channels);
        this.inputBufferSize = getMinInputFrameSize(this.sampleRate, this.channels);
    }

    public boolean isLowLatencyInputSupported() {
        return WebRtcAudioUtils.runningOnLollipopOrHigher() && isLowLatencyOutputSupported();
    }
}
