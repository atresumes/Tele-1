package org.webrtc.sinch.voiceengine;

import android.annotation.TargetApi;
import android.media.audiofx.AcousticEchoCanceler;
import android.media.audiofx.AudioEffect;
import android.media.audiofx.AudioEffect.Descriptor;
import android.media.audiofx.AutomaticGainControl;
import android.media.audiofx.NoiseSuppressor;
import android.os.Build;
import com.payUMoney.sdk.SdkConstants;
import java.util.UUID;
import org.webrtc.sinch.Logging;

class WebRtcAudioEffects {
    private static final UUID AOSP_ACOUSTIC_ECHO_CANCELER = UUID.fromString("bb392ec0-8d4d-11e0-a896-0002a5d5c51b");
    private static final UUID AOSP_AUTOMATIC_GAIN_CONTROL = UUID.fromString("aa8130e0-66fc-11e0-bad0-0002a5d5c51b");
    private static final UUID AOSP_NOISE_SUPPRESSOR = UUID.fromString("c06c8400-8e06-11e0-9cb6-0002a5d5c51b");
    private static final boolean DEBUG = false;
    private static final String TAG = "WebRtcAudioEffects";
    private static Descriptor[] cachedEffects = null;
    private AcousticEchoCanceler aec = null;
    private AutomaticGainControl agc = null;
    private NoiseSuppressor ns = null;
    private boolean shouldEnableAec = false;
    private boolean shouldEnableAgc = false;
    private boolean shouldEnableNs = false;

    private WebRtcAudioEffects() {
        Logging.m776d(TAG, "ctor" + WebRtcAudioUtils.getThreadInfo());
    }

    private static void assertTrue(boolean z) {
        if (!z) {
            throw new AssertionError("Expected condition to be true");
        }
    }

    public static boolean canUseAcousticEchoCanceler() {
        boolean z = (!isAcousticEchoCancelerSupported() || WebRtcAudioUtils.useWebRtcBasedAcousticEchoCanceler() || isAcousticEchoCancelerBlacklisted() || isAcousticEchoCancelerExcludedByUUID()) ? false : true;
        Logging.m776d(TAG, "canUseAcousticEchoCanceler: " + z);
        return z;
    }

    public static boolean canUseAutomaticGainControl() {
        boolean z = (!isAutomaticGainControlSupported() || WebRtcAudioUtils.useWebRtcBasedAutomaticGainControl() || isAutomaticGainControlBlacklisted() || isAutomaticGainControlExcludedByUUID()) ? false : true;
        Logging.m776d(TAG, "canUseAutomaticGainControl: " + z);
        return z;
    }

    public static boolean canUseNoiseSuppressor() {
        boolean z = (!isNoiseSuppressorSupported() || WebRtcAudioUtils.useWebRtcBasedNoiseSuppressor() || isNoiseSuppressorBlacklisted() || isNoiseSuppressorExcludedByUUID()) ? false : true;
        Logging.m776d(TAG, "canUseNoiseSuppressor: " + z);
        return z;
    }

    static WebRtcAudioEffects create() {
        if (WebRtcAudioUtils.runningOnJellyBeanOrHigher()) {
            return new WebRtcAudioEffects();
        }
        Logging.m780w(TAG, "API level 16 or higher is required!");
        return null;
    }

    @TargetApi(18)
    private boolean effectTypeIsVoIP(UUID uuid) {
        return !WebRtcAudioUtils.runningOnJellyBeanMR2OrHigher() ? false : (AudioEffect.EFFECT_TYPE_AEC.equals(uuid) && isAcousticEchoCancelerSupported()) || ((AudioEffect.EFFECT_TYPE_AGC.equals(uuid) && isAutomaticGainControlSupported()) || (AudioEffect.EFFECT_TYPE_NS.equals(uuid) && isNoiseSuppressorSupported()));
    }

    private static Descriptor[] getAvailableEffects() {
        if (cachedEffects != null) {
            return cachedEffects;
        }
        cachedEffects = AudioEffect.queryEffects();
        return cachedEffects;
    }

    public static boolean isAcousticEchoCancelerBlacklisted() {
        boolean contains = WebRtcAudioUtils.getBlackListedModelsForAecUsage().contains(Build.MODEL);
        if (contains) {
            Logging.m780w(TAG, Build.MODEL + " is blacklisted for HW AEC usage!");
        }
        return contains;
    }

    @TargetApi(18)
    private static boolean isAcousticEchoCancelerEffectAvailable() {
        return isEffectTypeAvailable(AudioEffect.EFFECT_TYPE_AEC);
    }

    @TargetApi(18)
    private static boolean isAcousticEchoCancelerExcludedByUUID() {
        for (Descriptor descriptor : getAvailableEffects()) {
            if (descriptor.type.equals(AudioEffect.EFFECT_TYPE_AEC) && descriptor.uuid.equals(AOSP_ACOUSTIC_ECHO_CANCELER)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAcousticEchoCancelerSupported() {
        return WebRtcAudioUtils.runningOnJellyBeanOrHigher() && isAcousticEchoCancelerEffectAvailable();
    }

    public static boolean isAutomaticGainControlBlacklisted() {
        boolean contains = WebRtcAudioUtils.getBlackListedModelsForAgcUsage().contains(Build.MODEL);
        if (contains) {
            Logging.m780w(TAG, Build.MODEL + " is blacklisted for HW AGC usage!");
        }
        return contains;
    }

    @TargetApi(18)
    private static boolean isAutomaticGainControlEffectAvailable() {
        return isEffectTypeAvailable(AudioEffect.EFFECT_TYPE_AGC);
    }

    @TargetApi(18)
    private static boolean isAutomaticGainControlExcludedByUUID() {
        for (Descriptor descriptor : getAvailableEffects()) {
            if (descriptor.type.equals(AudioEffect.EFFECT_TYPE_AGC) && descriptor.uuid.equals(AOSP_AUTOMATIC_GAIN_CONTROL)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAutomaticGainControlSupported() {
        return WebRtcAudioUtils.runningOnJellyBeanOrHigher() && isAutomaticGainControlEffectAvailable();
    }

    private static boolean isEffectTypeAvailable(UUID uuid) {
        Descriptor[] availableEffects = getAvailableEffects();
        if (availableEffects == null) {
            return false;
        }
        for (Descriptor descriptor : availableEffects) {
            if (descriptor.type.equals(uuid)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNoiseSuppressorBlacklisted() {
        boolean contains = WebRtcAudioUtils.getBlackListedModelsForNsUsage().contains(Build.MODEL);
        if (contains) {
            Logging.m780w(TAG, Build.MODEL + " is blacklisted for HW NS usage!");
        }
        return contains;
    }

    @TargetApi(18)
    private static boolean isNoiseSuppressorEffectAvailable() {
        return isEffectTypeAvailable(AudioEffect.EFFECT_TYPE_NS);
    }

    @TargetApi(18)
    private static boolean isNoiseSuppressorExcludedByUUID() {
        for (Descriptor descriptor : getAvailableEffects()) {
            if (descriptor.type.equals(AudioEffect.EFFECT_TYPE_NS) && descriptor.uuid.equals(AOSP_NOISE_SUPPRESSOR)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNoiseSuppressorSupported() {
        return WebRtcAudioUtils.runningOnJellyBeanOrHigher() && isNoiseSuppressorEffectAvailable();
    }

    public void enable(int i) {
        boolean z;
        boolean z2 = true;
        Logging.m776d(TAG, "enable(audioSession=" + i + ")");
        assertTrue(this.aec == null);
        assertTrue(this.agc == null);
        assertTrue(this.ns == null);
        for (Descriptor descriptor : AudioEffect.queryEffects()) {
            if (effectTypeIsVoIP(descriptor.type)) {
                Logging.m776d(TAG, "name: " + descriptor.name + ", " + "mode: " + descriptor.connectMode + ", " + "implementor: " + descriptor.implementor + ", " + "UUID: " + descriptor.uuid);
            }
        }
        if (isAcousticEchoCancelerSupported()) {
            this.aec = AcousticEchoCanceler.create(i);
            if (this.aec != null) {
                boolean enabled = this.aec.getEnabled();
                z = this.shouldEnableAec && canUseAcousticEchoCanceler();
                if (this.aec.setEnabled(z) != 0) {
                    Logging.m777e(TAG, "Failed to set the AcousticEchoCanceler state");
                }
                Logging.m776d(TAG, "AcousticEchoCanceler: was " + (enabled ? SdkConstants.ENABLED_STRING : "disabled") + ", enable: " + z + ", is now: " + (this.aec.getEnabled() ? SdkConstants.ENABLED_STRING : "disabled"));
            } else {
                Logging.m777e(TAG, "Failed to create the AcousticEchoCanceler instance");
            }
        }
        if (isAutomaticGainControlSupported()) {
            this.agc = AutomaticGainControl.create(i);
            if (this.agc != null) {
                enabled = this.agc.getEnabled();
                z = this.shouldEnableAgc && canUseAutomaticGainControl();
                if (this.agc.setEnabled(z) != 0) {
                    Logging.m777e(TAG, "Failed to set the AutomaticGainControl state");
                }
                Logging.m776d(TAG, "AutomaticGainControl: was " + (enabled ? SdkConstants.ENABLED_STRING : "disabled") + ", enable: " + z + ", is now: " + (this.agc.getEnabled() ? SdkConstants.ENABLED_STRING : "disabled"));
            } else {
                Logging.m777e(TAG, "Failed to create the AutomaticGainControl instance");
            }
        }
        if (isNoiseSuppressorSupported()) {
            this.ns = NoiseSuppressor.create(i);
            if (this.ns != null) {
                z = this.ns.getEnabled();
                if (!(this.shouldEnableNs && canUseNoiseSuppressor())) {
                    z2 = false;
                }
                if (this.ns.setEnabled(z2) != 0) {
                    Logging.m777e(TAG, "Failed to set the NoiseSuppressor state");
                }
                Logging.m776d(TAG, "NoiseSuppressor: was " + (z ? SdkConstants.ENABLED_STRING : "disabled") + ", enable: " + z2 + ", is now: " + (this.ns.getEnabled() ? SdkConstants.ENABLED_STRING : "disabled"));
                return;
            }
            Logging.m777e(TAG, "Failed to create the NoiseSuppressor instance");
        }
    }

    public void release() {
        Logging.m776d(TAG, "release");
        if (this.aec != null) {
            this.aec.release();
            this.aec = null;
        }
        if (this.agc != null) {
            this.agc.release();
            this.agc = null;
        }
        if (this.ns != null) {
            this.ns.release();
            this.ns = null;
        }
    }

    public boolean setAEC(boolean z) {
        Logging.m776d(TAG, "setAEC(" + z + ")");
        if (!canUseAcousticEchoCanceler()) {
            Logging.m780w(TAG, "Platform AEC is not supported");
            this.shouldEnableAec = false;
            return false;
        } else if (this.aec == null || z == this.shouldEnableAec) {
            this.shouldEnableAec = z;
            return true;
        } else {
            Logging.m777e(TAG, "Platform AEC state can't be modified while recording");
            return false;
        }
    }

    public boolean setAGC(boolean z) {
        Logging.m776d(TAG, "setAGC(" + z + ")");
        if (!canUseAutomaticGainControl()) {
            Logging.m780w(TAG, "Platform AGC is not supported");
            this.shouldEnableAgc = false;
            return false;
        } else if (this.agc == null || z == this.shouldEnableAgc) {
            this.shouldEnableAgc = z;
            return true;
        } else {
            Logging.m777e(TAG, "Platform AGC state can't be modified while recording");
            return false;
        }
    }

    public boolean setNS(boolean z) {
        Logging.m776d(TAG, "setNS(" + z + ")");
        if (!canUseNoiseSuppressor()) {
            Logging.m780w(TAG, "Platform NS is not supported");
            this.shouldEnableNs = false;
            return false;
        } else if (this.ns == null || z == this.shouldEnableNs) {
            this.shouldEnableNs = z;
            return true;
        } else {
            Logging.m777e(TAG, "Platform NS state can't be modified while recording");
            return false;
        }
    }
}
