package com.sinch.android.rtc;

public interface SinchError {
    int getCode();

    ErrorType getErrorType();

    String getMessage();
}
