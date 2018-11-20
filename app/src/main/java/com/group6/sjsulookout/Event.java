package com.group6.sjsulookout;

public class Event {
    private String title;
    private String desc;
    private String location;
    private String startDate;
    private String endDate;
    private String startTime;
    private String endTime;
    private int id;
    private String type;
    private String contact;
    private String email;
    private String phone;


    public Event(String title, String desc, String location, String startDate, String endDate, String startTime, String endTime, int id, String type, String contact, String email, String phone) {
        this.title = title;
        this.desc = desc;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.id = id;
        this.type = type;
        this.contact = contact;
        this.email = email;
        this.phone = phone;
    }


}
