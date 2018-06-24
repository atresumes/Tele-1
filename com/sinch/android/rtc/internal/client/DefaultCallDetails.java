package com.sinch.android.rtc.internal.client;

import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.CallDetails;
import com.sinch.android.rtc.calling.CallEndCause;
import com.sinch.android.rtc.internal.natives.SessionDetails;

public class DefaultCallDetails implements CallDetails {
    private final SessionDetails details;

    public DefaultCallDetails(SessionDetails sessionDetails) {
        this.details = sessionDetails;
    }

    public int getDuration() {
        return this.details.getDuration();
    }

    public CallEndCause getEndCause() {
        return this.details.getTerminationCause();
    }

    public long getEndedTime() {
        return this.details.getEndTime();
    }

    public SinchError getError() {
        return this.details.getError();
    }

    public long getEstablishedTime() {
        return this.details.getEstablishTime();
    }

    public long getStartedTime() {
        return this.details.getStartTime();
    }

    public boolean isVideoOffered() {
        return this.details.isVideoOffered();
    }

    public String toString() {
        return this.details.toString();
    }
}
