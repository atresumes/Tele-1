package com.sinch.android.rtc.internal;

import com.sinch.android.rtc.ErrorType;
import com.sinch.android.rtc.SinchError;
import java.util.HashMap;
import java.util.Map;

public final class DefaultSinchError implements SinchError {
    private final int code;
    private Map<String, String> data;
    private final ErrorType errorType;
    private final String message;

    public DefaultSinchError(int i, String str, int i2) {
        this.code = i;
        this.message = str;
        this.data = new HashMap();
        if (i2 < 0 || i2 >= ErrorType.values().length) {
            this.errorType = ErrorType.OTHER;
        } else {
            this.errorType = ErrorType.values()[i2];
        }
    }

    public DefaultSinchError(int i, String str, int i2, Map<String, String> map) {
        this.code = i;
        this.message = str;
        this.data = map;
        if (i2 < 0 || i2 >= ErrorType.values().length) {
            throw new IllegalArgumentException("The error type " + i2 + " is invalid");
        }
        this.errorType = ErrorType.values()[i2];
    }

    public int getCode() {
        return this.code;
    }

    public Map<String, String> getData() {
        return this.data;
    }

    public ErrorType getErrorType() {
        return this.errorType;
    }

    public String getMessage() {
        return (this.message.isEmpty() && this.data.containsKey("serverMessage")) ? (String) this.data.get("serverMessage") : this.message;
    }

    public String toString() {
        return "SinchError[errorType=" + this.errorType + ", code=" + this.code + ", message='" + this.message + '\'' + ", data=" + this.data + ']';
    }
}
