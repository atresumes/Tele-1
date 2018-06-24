package com.talktoangel.gts.model;

public class AvailableTimes {
    private String slotId;
    private String status;
    private String time;
    private String timeId;

    public String getSlotId() {
        return this.slotId;
    }

    public void setSlotId(String slotId) {
        this.slotId = slotId;
    }

    public String getTimeId() {
        return this.timeId;
    }

    public void setTimeId(String timeId) {
        this.timeId = timeId;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
