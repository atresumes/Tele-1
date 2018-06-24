package com.payu.custombrowser.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.payu.custombrowser.util.CBConstant;

public class CustomBrowserAsyncTaskData implements Parcelable {
    public static final Creator<CustomBrowserAsyncTaskData> CREATOR = new C05241();
    private String contentType;
    private String httpMethod;
    private String postData;
    private String url;

    static class C05241 implements Creator<CustomBrowserAsyncTaskData> {
        C05241() {
        }

        public CustomBrowserAsyncTaskData createFromParcel(Parcel in) {
            return new CustomBrowserAsyncTaskData(in);
        }

        public CustomBrowserAsyncTaskData[] newArray(int size) {
            return new CustomBrowserAsyncTaskData[size];
        }
    }

    protected CustomBrowserAsyncTaskData(Parcel in) {
        this.httpMethod = in.readString();
        this.url = in.readString();
        this.postData = in.readString();
        this.contentType = in.readString();
    }

    public CustomBrowserAsyncTaskData() {
        this.httpMethod = "GET";
        this.contentType = CBConstant.HTTP_URLENCODED;
    }

    public String getHttpMethod() {
        return this.httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPostData() {
        return this.postData;
    }

    public void setPostData(String postData) {
        this.postData = postData;
    }

    public String getContentType() {
        return this.contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.httpMethod);
        parcel.writeString(this.url);
        parcel.writeString(this.postData);
        parcel.writeString(this.contentType);
    }
}
