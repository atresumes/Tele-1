package com.talktoangel.gts.model;

public class DegreeCollege {
    private String deleted;
    private String id;
    private String name;

    public DegreeCollege(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
