package com.talktoangel.gts.model;

public class TimeSlot {
    private String endTime;
    private String startTime;
    private String timingId;

    public String getTimingId() {
        return this.timingId;
    }

    public void setTimingId(String timingId) {
        this.timingId = timingId;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
