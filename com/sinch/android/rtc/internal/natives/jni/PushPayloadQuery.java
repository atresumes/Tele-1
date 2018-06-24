package com.sinch.android.rtc.internal.natives.jni;

import com.sinch.android.rtc.SinchError;

public class PushPayloadQuery {

    public class PushPayloadQueryResult {
        private SinchError error;
        private final boolean isTimedOut;
        private boolean isValid;
        private final String sessionId;
        private final int type;
        private final String userId;
        private boolean videoOffered;

        public PushPayloadQueryResult(String str, String str2, boolean z, boolean z2, int i, boolean z3, SinchError sinchError) {
            this.sessionId = str;
            this.userId = str2;
            this.isTimedOut = z;
            this.isValid = z2;
            this.type = i;
            this.videoOffered = z3;
            this.error = sinchError;
        }

        public SinchError getError() {
            return this.error;
        }

        public String getSessionId() {
            return this.sessionId;
        }

        public int getType() {
            return this.type;
        }

        public String getUserId() {
            return this.userId;
        }

        public boolean getVideoOffered() {
            return this.videoOffered;
        }

        public boolean isTimedOut() {
            return this.isTimedOut;
        }

        public boolean isValid() {
            return this.isValid;
        }

        public String toString() {
            return "PushPayloadQueryResult [sessionId=" + (this.sessionId != null ? this.sessionId : "<null>") + ", userId=" + (this.userId != null ? this.userId : "<null>") + ", isTimedOut=" + this.isTimedOut + ", isValid=" + this.isValid + ", error=" + (this.error != null ? this.error : "<null>") + "]";
        }
    }

    public static native PushPayloadQueryResult queryPayload(byte[] bArr, long j);
}
