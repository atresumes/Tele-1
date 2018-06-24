package com.sinch.android.rtc.internal.natives;

import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.CallEndCause;

public class SessionDetails {
    private final int duration;
    private final long endTime;
    private final SinchError error;
    private final long establishTime;
    private final long progressTime;
    private final long startTime;
    private final CallEndCause terminationCause;
    private final boolean videoOffered;

    private SessionDetails(long j, long j2, long j3, long j4, int i, int i2, int i3, SinchError sinchError) {
        this.startTime = j;
        this.endTime = j2;
        this.progressTime = j3;
        this.establishTime = j4;
        this.duration = i;
        if (i3 < 0 || i3 >= MediaOfferFlag.values().length) {
            throw new IllegalStateException("Illegal mediaOfferFlag with value:" + i3);
        }
        this.videoOffered = i3 == MediaOfferFlag.AudioAndVideo.ordinal();
        this.error = sinchError;
        if (i2 < 0 || i2 >= CallEndCause.values().length) {
            throw new IllegalStateException("Illegal terminationCause with value:" + i2);
        }
        this.terminationCause = CallEndCause.values()[i2];
    }

    public int getDuration() {
        return this.duration;
    }

    public long getEndTime() {
        return this.endTime;
    }

    public SinchError getError() {
        return this.error;
    }

    public long getEstablishTime() {
        return this.establishTime;
    }

    public long getProgressTime() {
        return this.progressTime;
    }

    public long getStartTime() {
        return this.startTime;
    }

    public CallEndCause getTerminationCause() {
        return this.terminationCause;
    }

    public boolean isVideoOffered() {
        return this.videoOffered;
    }

    public String toString() {
        return "SessionDetails [startTime=" + this.startTime + ", endTime=" + this.endTime + ", progressTime=" + this.progressTime + ", establishTime=" + this.establishTime + ", duration=" + this.duration + ", terminationCause=" + this.terminationCause + ", error=" + this.error + "]";
    }
}
