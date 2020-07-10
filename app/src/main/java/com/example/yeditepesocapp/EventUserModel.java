package com.example.yeditepesocapp;

public class EventUserModel {

    String event_name, user_name, user_surname, department_name;

    public EventUserModel() {
    }

    public EventUserModel(String user_name, String user_surname, String department_name) {
        this.user_name = user_name;
        this.user_surname = user_surname;
        this.department_name = department_name;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_surname() {
        return user_surname;
    }

    public void setUser_surname(String user_surname) {
        this.user_surname = user_surname;
    }

    public String getDepartment_name() {
        return department_name;
    }

    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }
}
