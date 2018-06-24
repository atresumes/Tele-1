package com.talktoangel.gts.model;

import java.io.Serializable;

public class Message implements Serializable {
    private String createdAt;
    private String message;
    private String msgId;
    private String name;
    private String userId;

    public Message(String userId, String message, String createdAt) {
        this.userId = userId;
        this.message = message;
        this.createdAt = createdAt;
    }

    public Message(String userId, String name, String msgId, String message, String createdAt) {
        this.userId = userId;
        this.name = name;
        this.msgId = msgId;
        this.message = message;
        this.createdAt = createdAt;
    }

    public String getMsgId() {
        return this.msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
