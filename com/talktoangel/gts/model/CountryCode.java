package com.talktoangel.gts.model;

public class CountryCode {
    private String code;
    private String flag;
    private String id;
    private String name;

    public CountryCode(String id, String name, String code, String flag) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.flag = flag;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getCode() {
        return this.code;
    }

    public String getFlag() {
        return this.flag;
    }
}
