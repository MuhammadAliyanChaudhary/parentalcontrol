package com.example.parentalcontrol.model;

public class SmsModel {

    private String id;
    private String address;
    private String body;
    private String date;
    private boolean isSent;

    public SmsModel(String id, String address, String body, String date, boolean isSent) {
        this.id = id;
        this.address = address;
        this.body = body;
        this.date = date;
        this.isSent = isSent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }

    public String getAddress() {
        return address;
    }

    public String getBody() {
        return body;
    }

    public String getDate() {
        return date;
    }

    public boolean isSent() {
        return isSent;
    }
}
