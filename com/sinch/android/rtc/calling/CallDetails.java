package com.sinch.android.rtc.calling;

import com.sinch.android.rtc.SinchError;

public interface CallDetails {
    int getDuration();

    CallEndCause getEndCause();

    long getEndedTime();

    SinchError getError();

    long getEstablishedTime();

    long getStartedTime();

    boolean isVideoOffered();
}
