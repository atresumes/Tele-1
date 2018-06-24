package com.talktoangel.gts.model;

public class CardItem {
    private String cvv;
    private String id;
    private String name;
    private String number;
    private String validity;

    public CardItem(String id, String name, String number, String validity, String cvv) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.validity = validity;
        this.cvv = cvv;
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

    public String getNumber() {
        return this.number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getValidity() {
        return this.validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public String getCvv() {
        return this.cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
}
