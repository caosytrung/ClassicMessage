package com.example.mammam.classicmessage.models;

/**
 * Created by Mam  Mam on 10/27/2016.
 */

public class MySms {
    public String body;
    private String phoneNumber;
    private String name;
    private String readState;
    private String type;
    private String date;

    public MySms(String body, String phoneNumber, String name, String readState, String type, String date) {

        this.body = body;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.readState = readState; // 1 la doc roi , 0 la chua doc
        this.type = type; // 2 la gui di,1 la dc nhan
        this.date = date;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReadState() {
        return readState;
    }

    public void setReadState(String readState) {
        this.readState = readState;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
