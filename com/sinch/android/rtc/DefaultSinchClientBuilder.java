package com.sinch.android.rtc;

import android.content.Context;
import com.sinch.android.rtc.internal.client.InternalSinchClientFactory;

class DefaultSinchClientBuilder implements SinchClientBuilder {
    private String mApplicationKey;
    private String mApplicationSecret;
    private String mCallerIdentifier;
    private Context mContext;
    private String mEnvironmentHost;
    private String mUserId;

    DefaultSinchClientBuilder() {
    }

    public DefaultSinchClientBuilder applicationKey(String str) {
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException("The application key must not be null or empty.");
        }
        this.mApplicationKey = str;
        return this;
    }

    public DefaultSinchClientBuilder applicationSecret(String str) {
        if (str == null) {
            throw new IllegalArgumentException("The application secret must not be null or empty.");
        }
        this.mApplicationSecret = str;
        return this;
    }

    public SinchClient build() {
        if (this.mContext == null) {
            throw new IllegalArgumentException("Context must be set.");
        } else if (this.mUserId == null) {
            throw new IllegalArgumentException("User id must be set.");
        } else if (this.mApplicationKey == null) {
            throw new IllegalArgumentException("Application key must be set.");
        } else if (this.mEnvironmentHost == null) {
            throw new IllegalArgumentException("Environment host must be set.");
        } else {
            if (this.mCallerIdentifier == null) {
                this.mCallerIdentifier = "";
            }
            if (this.mApplicationSecret == null) {
                this.mApplicationSecret = "";
            }
            return InternalSinchClientFactory.createSinchClient(this.mContext, this.mUserId, this.mCallerIdentifier, this.mApplicationKey, this.mApplicationSecret, this.mEnvironmentHost);
        }
    }

    public DefaultSinchClientBuilder callerIdentifier(String str) {
        if (str == null) {
            throw new IllegalArgumentException("The caller identifier must not be null. Use empty string instead.");
        }
        this.mCallerIdentifier = str;
        return this;
    }

    public DefaultSinchClientBuilder context(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context must not be null.");
        }
        this.mContext = context;
        return this;
    }

    public DefaultSinchClientBuilder environmentHost(String str) {
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException("The environment host must not be null or empty.");
        }
        this.mEnvironmentHost = str;
        return this;
    }

    public DefaultSinchClientBuilder userId(String str) {
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException("The user id must not be null or empty.");
        }
        this.mUserId = str;
        return this;
    }
}
