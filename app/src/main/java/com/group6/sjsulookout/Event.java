package com.group6.sjsulookout;

public class Event {
    private String title;
    private String desc;
    private String location;
    private String date;
    private String startTime;
    private String endTime;
    private int id;
    private String type;
    private String contact;


    public Event(){
        super();
    }

    public Event(String title, String desc, String location, String date, String startTime, String endTime, int id, String type, String contact) {
        this.title = title;
        this.desc = desc;
        this.location = location;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.id = id;
        this.type = type;
        this.contact = contact;

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getLocation() {
        return location;
    }

    public String getDate() {
        return date;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public int getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setId(int id) {
        this.id = id;
    }
}
