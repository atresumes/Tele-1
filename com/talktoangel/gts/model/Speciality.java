package com.talktoangel.gts.model;

public class Speciality {
    private String deleted;
    private String id;
    private String name;

    public Speciality(String id, String name, String deleted) {
        this.id = id;
        this.name = name;
        this.deleted = deleted;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getDeleted() {
        return this.deleted;
    }
}
