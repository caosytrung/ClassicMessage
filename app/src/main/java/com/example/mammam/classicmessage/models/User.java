package com.example.mammam.classicmessage.models;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Mam  Mam on 10/29/2016.
 */

public class User {

    private String name;
    private String phoneNumber;
    private String lastSMs;
    private String date;

    public String getDate() {
        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d = format1.parse(date);
            format1 = new SimpleDateFormat("MM ww");
            String formatDate = format1.format(d);
            return formatDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public User(String name, String phoneNumber, String lastSMs, String date) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.lastSMs = lastSMs;
        this.date = date;
    }

    public String getName() {
        return name;

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLastSMs() {
        return lastSMs;
    }

    public void setLastSMs(String lastSMs) {
        this.lastSMs = lastSMs;
    }


}
