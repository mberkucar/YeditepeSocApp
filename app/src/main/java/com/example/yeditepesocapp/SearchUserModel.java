package com.example.yeditepesocapp;

public class SearchUserModel {

    String user_name, user_surname, user_id, user_mail, department_name, faculty_name;

    public SearchUserModel() {
    }

    public SearchUserModel(String user_name, String user_surname, String user_id, String department_name, String faculty_name) {
        this.user_name = user_name;
        this.user_surname = user_surname;
        this.user_id = user_id;
        this.department_name = department_name;
        this.faculty_name = faculty_name;
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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_mail() {
        return user_mail;
    }

    public void setUser_mail(String user_mail) {
        this.user_mail = user_mail;
    }

    public String getDepartment_name() {
        return department_name;
    }

    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }

    public String getFaculty_name() {
        return faculty_name;
    }

    public void setFaculty_name(String faculty_name) {
        this.faculty_name = faculty_name;
    }
}
