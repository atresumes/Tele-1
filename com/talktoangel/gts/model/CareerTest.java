package com.talktoangel.gts.model;

public class CareerTest {
    private String description;
    private String id;
    private String pdf_url;
    private String status;
    private String title;
    private String with_counseling;
    private String without_counseling;

    public CareerTest(String id, String title, String description, String with_counseling, String without_counseling, String pdf_url, String status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.with_counseling = with_counseling;
        this.without_counseling = without_counseling;
        this.pdf_url = pdf_url;
        this.status = status;
    }

    public String getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public String getWith_counseling() {
        return this.with_counseling;
    }

    public String getWithout_counseling() {
        return this.without_counseling;
    }

    public String getPdf_url() {
        return this.pdf_url;
    }

    public String getStatus() {
        return this.status;
    }
}
