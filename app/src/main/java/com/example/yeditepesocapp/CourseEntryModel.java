package com.example.yeditepesocapp;

public class CourseEntryModel {

    private String user_name, user_surname, course_entry_content;
    private String course_entry_date;


    public CourseEntryModel() {
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

    public String getCourse_entry_content() {
        return course_entry_content;
    }

    public void setCourse_entry_content(String course_entry_content) {
        this.course_entry_content = course_entry_content;
    }

    public String getCourse_entry_date() {
        return course_entry_date;
    }

    public void setCourse_entry_date(String course_entry_date) {
        this.course_entry_date = course_entry_date;
    }
}
