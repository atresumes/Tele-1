package com.talktoangel.gts.model;

import java.util.Date;

public class EventObjects {
    private Date date;
    private int id;
    private String message;

    public EventObjects(String message, Date date) {
        this.message = message;
        this.date = date;
    }

    public EventObjects(int id, String message, Date date) {
        this.date = date;
        this.message = message;
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public String getMessage() {
        return this.message;
    }

    public Date getDate() {
        return this.date;
    }
}
