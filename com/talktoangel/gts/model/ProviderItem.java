package com.talktoangel.gts.model;

public class ProviderItem {
    private String charge;
    private String id;
    private String image;
    private String name;

    public ProviderItem(String id, String name, String image, String charge) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.charge = charge;
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

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCharge() {
        return this.charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }
}
