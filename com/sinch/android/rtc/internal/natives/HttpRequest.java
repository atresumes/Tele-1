package com.sinch.android.rtc.internal.natives;

import java.util.Map;

public class HttpRequest {
    public static final int METHOD_DELETE = 3;
    public static final int METHOD_GET = 1;
    public static final int METHOD_POST = 0;
    public static final int METHOD_PUT = 2;
    private final String body;
    private final Map<String, String> headers;
    private final int method;
    private final String url;

    public HttpRequest(String str, int i, String str2, Map<String, String> map) {
        this.url = str;
        this.method = i;
        this.body = str2;
        this.headers = map;
    }

    public String getBody() {
        return this.body;
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public int getMethod() {
        return this.method;
    }

    public String getMethodString() {
        switch (this.method) {
            case 0:
                return "POST";
            case 1:
                return "GET";
            case 2:
                return "PUT";
            case 3:
                return "DELETE";
            default:
                throw new IllegalStateException("Unsupported http method value: " + this.method);
        }
    }

    public String getUrl() {
        return this.url;
    }
}
