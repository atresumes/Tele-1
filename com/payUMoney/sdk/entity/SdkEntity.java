package com.payUMoney.sdk.entity;

public abstract class SdkEntity {
    private boolean deleted = false;
    private long id = 0;

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isDeleted() {
        return this.deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
