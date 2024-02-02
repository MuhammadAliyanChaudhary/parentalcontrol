package com.example.parentalcontrol.model;

public class CallLogModel {

    private String id;
    private String number;
    private String name;
    private String date;
    private int type;

    public CallLogModel(String id, String number, String name, String date, int type) {
        this.id = id;
        this.number = number;
        this.name = name;
        this.date = date;
        this.type = type;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public int getType() {
        return type;
    }
}
