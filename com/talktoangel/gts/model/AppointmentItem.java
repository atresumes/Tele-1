package com.talktoangel.gts.model;

public class AppointmentItem {
    private String apoDate;
    private String apoID;
    private String apoStatus;
    private String apoTime;
    private String apoType;
    private String image;
    private String mobile;
    private String name;
    private String userId;

    public String getApoID() {
        return this.apoID;
    }

    public void setApoID(String apoID) {
        this.apoID = apoID;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getApoType() {
        return this.apoType;
    }

    public void setApoType(String apoType) {
        this.apoType = apoType;
    }

    public String getApoDate() {
        return this.apoDate;
    }

    public void setApoDate(String apoDate) {
        this.apoDate = apoDate;
    }

    public String getApoTime() {
        return this.apoTime;
    }

    public void setApoTime(String apoTime) {
        this.apoTime = apoTime;
    }

    public String getApoStatus() {
        return this.apoStatus;
    }

    public void setApoStatus(String apoStatus) {
        this.apoStatus = apoStatus;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
