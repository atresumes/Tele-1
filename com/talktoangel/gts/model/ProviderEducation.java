package com.talktoangel.gts.model;

public class ProviderEducation {
    private String clg;
    private String degree;
    private String drID;
    private String eduID;
    private String year;

    public ProviderEducation(String drID, String eduID, String degree, String clg, String year) {
        this.drID = drID;
        this.eduID = eduID;
        this.degree = degree;
        this.clg = clg;
        this.year = year;
    }

    public String getDrID() {
        return this.drID;
    }

    public String getEduID() {
        return this.eduID;
    }

    public String getDegree() {
        return this.degree;
    }

    public String getClg() {
        return this.clg;
    }

    public String getYear() {
        return this.year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
